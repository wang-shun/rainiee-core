/*
 * 文 件 名:  ApplyGuarantorManageImpl
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间: 2016/6/13
 */
package com.dimeng.p2p.modules.apply.guarantor.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S51.entities.T5122;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6125;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6125_F05;
import com.dimeng.p2p.common.enums.FundAccountType;
import com.dimeng.p2p.repeater.guarantor.ApplyGuarantorManage;
import com.dimeng.p2p.repeater.guarantor.entity.DbRecordEntity;
import com.dimeng.p2p.repeater.guarantor.entity.GuarantorEntity;
import com.dimeng.p2p.repeater.guarantor.query.DbRecordQuery;
import com.dimeng.p2p.repeater.guarantor.query.GuarantorQuery;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;

/**
 * 
 * 申请担保方业务实现类
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version [版本号, 2016/6/13]
 */
public class ApplyGuarantorManageImpl extends AbstractGuarantorService implements ApplyGuarantorManage
{
    
    public ApplyGuarantorManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public T6125 getGuanterInfo()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05 FROM S61.T6125 WHERE F02 = ? LIMIT 1 "))
            {
                ps.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        T6125 t6125 = new T6125();
                        t6125.F01 = rs.getInt(1);
                        t6125.F02 = rs.getInt(2);
                        t6125.F03 = rs.getString(3);
                        t6125.F04 = rs.getBigDecimal(4);
                        t6125.F05 = T6125_F05.parse(rs.getString(5));
                        return t6125;
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * 申请担保人
     *
     * @return
     * @throws Throwable
     */
    @Override
    public String applyGuarantor()
        throws Throwable
    {
        String code = "";
        int id = 0;
        try (Connection connection = getConnection())
        {
            int userId = serviceResource.getSession().getAccountId();
            
            T6110 t6110 = selectT6110(connection, userId);
            if (T6110_F10.S == t6110.F10)
            {
                throw new LogicalException("机构账号不能申请担保方！");
            }
            if (!isNetSign(userId, connection))
            {
                throw new LogicalException("申请担保必须先网签担保协议！");
            }
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S61.T6125 WHERE F02 = ? AND F05 IN ('SQCG','SQDCL') LIMIT 1 "))
            {
                pstmt.setInt(1, userId);
                try (ResultSet rs = pstmt.executeQuery())
                {
                    if (rs.next())
                    {
                        throw new LogicalException("不能重复申请！");
                    }
                }
            }
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F03 FROM S61.T6125 WHERE F02 = ? AND F05 IN ('SQSB','QXCG') LIMIT 1 "))
            {
                pstmt.setInt(1, userId);
                try (ResultSet rs = pstmt.executeQuery())
                {
                    if (rs.next())
                    {
                        id = rs.getInt(1);
                        code = rs.getString(2);
                    }
                }
            }
            if (StringHelper.isEmpty(code))
            {
                //担保码
                code = getDBM(connection);
                serviceResource.openTransactions(connection);
                BigDecimal defaultAmount =
                    BigDecimalParser.parse(serviceResource.getResource(ConfigureProvider.class)
                        .getProperty(SystemVariable.DEFAULT_GUARATTE_AMOUNT));
                insert(connection,
                    "INSERT INTO S61.T6125 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ? ",
                    userId,
                    code,
                    defaultAmount,
                    T6125_F05.SQDCL.name(),
                    getCurrentTimestamp(connection));
                try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S61.T6126 SET F02 = ?, F03 = ?, F04 = ?,F05 = ?, F07 = ?, F08 = ?"))
                {
                    pstmt.setInt(1, userId);
                    pstmt.setInt(2, FeeCode.DB_MR);
                    pstmt.setTimestamp(3, getCurrentTimestamp(connection));
                    pstmt.setBigDecimal(4, defaultAmount);
                    pstmt.setBigDecimal(5, defaultAmount);
                    pstmt.setString(6, "默认担保额度");
                    pstmt.execute();
                }
                serviceResource.commit(connection);
            }
            else
            {
                execute(connection, "UPDATE S61.T6125 SET F05 = 'SQDCL' WHERE F01 = ? ", id);
            }
            return code;
        }
    }
    
    /**
     * <生成担保码>
     * <功能详细描述>
     * @param connection
     * @return
     * @throws Throwable
     */
    private String getDBM(Connection connection)
        throws Throwable
    {
        String code = "";
        int rLength = 0;
        int randomNum = 0;
        boolean isTrue = false;
        Random r = new Random();
        do
        {
            randomNum = r.nextInt(1000000);
            rLength = String.valueOf(randomNum).length();
            if (rLength > 5)
            {
                code = "D" + randomNum;
            }
            else if (randomNum > 4)
            {
                code = "D0" + randomNum;
            }
            else if (randomNum > 3)
            {
                code = "D00" + randomNum;
            }
            else if (randomNum > 2)
            {
                code = "D000" + randomNum;
            }
            else if (randomNum > 1)
            {
                code = "D0000" + randomNum;
            }
            else
            {
                code = "D00000" + randomNum;
            }
            isTrue = isExistT6125F03(connection, code);
        } while (isTrue);
        return code;
    }
    
    /**
     * 用户担保列表
     *
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    @Override
    public PagingResult<GuarantorEntity> searchGuarantorInfos(GuarantorQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT * FROM (SELECT T6125.F01 AS F01, T6110.F02 AS F02,(CASE T6110.F06 WHEN 'ZRR' THEN (SELECT T6141.F02 FROM S61.T6141 WHERE T6141.F01 = T6110.F01 ) ELSE (SELECT T6161.F04 FROM S61.T6161 WHERE T6161.F01 = T6110.F01) END) AS F03, T6125.F03 AS F04, T6125.F04 AS F05, T6125.F05 AS F06, T6125.F09 AS F07, T6110.F06 AS F08,T7110.F02 AS F09,T6125.F08 AS F10, T6125.F10 AS F11, T6110.F10 AS F12, T6125.F02 AS F13 FROM S61.T6125 ");
        sql.append("LEFT JOIN S61.T6110 ON T6125.F02 = T6110.F01 LEFT JOIN S71.T7110 ON T7110.F01 = T6125.F07) TEMP ");
        sql.append(" WHERE 1=1 ");
        List<Object> params = new ArrayList<Object>();
        if (!StringHelper.isEmpty(query.getUserName()))
        {
            sql.append(" AND TEMP.F02 LIKE ? ");
            params.add(getSQLConnectionProvider().allMatch(query.getUserName()));
        }
        
        if (!StringHelper.isEmpty(query.getRealName()))
        {
            sql.append(" AND TEMP.F03 LIKE ? ");
            params.add(getSQLConnectionProvider().allMatch(query.getRealName()));
        }
        
        if (!StringHelper.isEmpty(query.getCode()))
        {
            sql.append(" AND TEMP.F04 LIKE ? ");
            params.add(getSQLConnectionProvider().allMatch(query.getCode()));
        }
        
        if (!StringHelper.isEmpty(query.getUserType()))
        {
            if (query.getUserType().equals(FundAccountType.JG.name()))
            {
                sql.append(" AND TEMP.F12 = 'S' AND TEMP.F08 = 'FZRR' ");
            }
            else if (query.getUserType().equals(FundAccountType.QY.name()))
            {
                sql.append(" AND TEMP.F12 = 'F' AND TEMP.F08 = 'FZRR' ");
            }
            else
            {
                sql.append(" AND TEMP.F08 = 'ZRR' ");
            }
        }
        sql.append(" ORDER BY TEMP.F07 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<GuarantorEntity>()
            {
                @Override
                public GuarantorEntity[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<GuarantorEntity> list = null;
                    while (resultSet.next())
                    {
                        GuarantorEntity entity = new GuarantorEntity();
                        entity.id = resultSet.getInt(1);
                        entity.userName = resultSet.getString(2);
                        entity.realName = resultSet.getString(3);
                        entity.code = resultSet.getString(4);
                        entity.guarantAmount = resultSet.getBigDecimal(5);
                        entity.status = T6125_F05.parse(resultSet.getString(6));
                        entity.updateTime = resultSet.getTimestamp(7);
                        if (T6110_F10.parse(resultSet.getString(12)) == T6110_F10.S
                            && T6110_F06.parse(resultSet.getString(8)) == T6110_F06.FZRR)
                        {
                            entity.userType = "机构";
                        }
                        else if (T6110_F10.parse(resultSet.getString(12)) == T6110_F10.F
                            && T6110_F06.parse(resultSet.getString(8)) == T6110_F06.FZRR)
                        {
                            entity.userType = "企业";
                        }
                        else
                        {
                            entity.userType = "个人";
                        }
                        entity.auditor = resultSet.getString(9);
                        entity.auditTime = resultSet.getTimestamp(10);
                        entity.auditDesc = resultSet.getString(11);
                        entity.userId = resultSet.getInt(13);
                        if (null == list)
                        {
                            list = new ArrayList<GuarantorEntity>();
                        }
                        list.add(entity);
                    }
                    return null == list || list.size() <= 0 ? null : list.toArray(new GuarantorEntity[list.size()]);
                }
            },
                paging,
                sql.toString(),
                params);
        }
        
    }
    
    /**
     * 申请担保审核
     *
     * @param id
     * @param status
     * @param desc
     * @throws Throwable
     */
    @Override
    public void auditApply(int id, String status, String desc)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new ParameterException("审核的申请担保不存在");
        }
        
        if (StringHelper.isEmpty(desc))
        {
            throw new ParameterException("审核意见不能为空");
        }
        try (Connection connection = getConnection())
        {
            String result = select(connection, new ItemParser<String>()
            {
                @Override
                public String parse(ResultSet resultSet)
                    throws SQLException
                {
                    if (resultSet.next())
                    {
                        return resultSet.getString(1);
                    }
                    return null;
                }
            }, "SELECT F05 FROM S61.T6125 WHERE F01 = ? ", id);
            
            if (T6125_F05.SQCG.name().equals(result) || T6125_F05.SQSB.name().equals(result))
            {
                throw new ParameterException("该申请已经被审核了");
            }
            
            execute(connection,
                "UPDATE S61.T6125 SET F05 = ?, F07 = ?, F08 = ?, F10 = ? WHERE F01 = ? ",
                status,
                serviceResource.getSession().getAccountId(),
                getCurrentTimestamp(connection),
                desc,
                id);
            int userId = select(connection, new ItemParser<Integer>()
            {
                @Override
                public Integer parse(ResultSet resultSet)
                    throws SQLException
                {
                    if (resultSet.next())
                    {
                        return resultSet.getInt(1);
                    }
                    return 0;
                }
            }, "SELECT F02 FROM S61.T6125 WHERE F01 = ? ", id);
            
            T6110 t6110 = selectT6110(connection, userId);
            ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
            Envionment envionment = configureProvider.createEnvionment();
            envionment.set("name", t6110.F02);
            if (T6125_F05.SQCG.name().equals(status))
            {
                String content = configureProvider.format(LetterVariable.GUARANT_AUDIT_YES, envionment);
                sendLetter(connection, userId, "申请担保，审核通过", content);
            }
            else
            {
                envionment.set("reason", desc);
                String content = configureProvider.format(LetterVariable.GUARANT_AUDIT_NO, envionment);
                sendLetter(connection, userId, "申请担保，审核不通过", content);
            }
            
        }
        
    }
    
    /**
     * 修改担保额度
     *
     * @param id
     * @param amount
     * @throws Throwable
     */
    @Override
    public void updateGuarantAmount(int id, BigDecimal amount)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            serviceResource.openTransactions(connection);
            int userId = 0;
            BigDecimal remain = BigDecimal.ZERO;
            //收入
            BigDecimal amountIn = BigDecimal.ZERO;
            //支出
            BigDecimal amountOut = BigDecimal.ZERO;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02, F04 FROM S61.T6125 WHERE F01 = ? FOR UPDATE "))
            {
                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery())
                {
                    if (rs.next())
                    {
                        userId = rs.getInt(1);
                        remain = rs.getBigDecimal(2);
                    }
                }
            }
            remain = amount.subtract(remain);
            if (remain.compareTo(BigDecimal.ZERO) < 0)
            {
                amountOut = remain.abs();
            }
            else
            {
                amountIn = remain;
            }
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO S61.T6126 SET F02 = ?, F03 = ?, F04 = ?,F05 = ?,F06 = ?, F07 = ?, F08 = ?"))
            {
                pstmt.setInt(1, userId);
                pstmt.setInt(2, FeeCode.DB_CZ);
                pstmt.setTimestamp(3, getCurrentTimestamp(connection));
                pstmt.setBigDecimal(4, amountIn);
                pstmt.setBigDecimal(5, amountOut);
                pstmt.setBigDecimal(6, amount);
                pstmt.setString(7, "手动设置，调整担保额度");
                pstmt.execute();
            }
            execute(connection, "UPDATE S61.T6125 SET F04 = ? WHERE F01 = ? ", amount, id);
            serviceResource.commit(connection);
        }
    }
    
    /**
     * 申请担保人
     * @param id
     * @throws Throwable
     */
    @Override
    public void cancelGuarantor(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new ParameterException("取消的申请不存在！");
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                execute(connection, "UPDATE S61.T6125 SET F05 = 'QXCG' WHERE F01 = ? ", id);
                
                int userId = select(connection, new ItemParser<Integer>()
                {
                    @Override
                    public Integer parse(ResultSet resultSet)
                        throws SQLException
                    {
                        if (resultSet.next())
                        {
                            return resultSet.getInt(1);
                        }
                        return 0;
                    }
                }, "SELECT F02 FROM S61.T6125 WHERE F01 = ? ", id);
                T6110 t6110 = selectT6110(connection, userId);
                ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
                Envionment envionment = configureProvider.createEnvionment();
                envionment.set("name", t6110.F02);
                String content = configureProvider.format(LetterVariable.GUARANT_CANCEL, envionment);
                sendLetter(connection, userId, "取消担保", content);
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    /**
     * 查询担保额度交易记录
     *
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    @Override
    public PagingResult<DbRecordEntity> searchAmountTrandRecords(DbRecordQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6126.F01, T6126.F02, T6126.F03, T6126.F04, T6126.F05, T6126.F06, T6126.F07, T6126.F08, T5122.F02 AS F09 FROM S61.T6126 LEFT JOIN S51.T5122 ON T6126.F03 = T5122.F01 WHERE T6126.F02 = ? ");
        List<Object> params = new ArrayList<Object>();
        params.add(query.getUserId());
        
        if (query.getType() > 0)
        {
            sql.append(" AND T6126.F03 = ? ");
            params.add(query.getType());
        }
        
        if (query.getStartPayTime() != null)
        {
            sql.append(" AND T6126.F04 >= ? ");
            params.add(query.getStartPayTime());
        }
        
        if (query.getEndPayTime() != null)
        {
            sql.append(" AND T6126.F04 <= ? ");
            params.add(query.getEndPayTime());
        }
        sql.append(" ORDER BY T6126.F04 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<DbRecordEntity>()
            {
                @Override
                public DbRecordEntity[] parse(ResultSet rs)
                    throws SQLException
                {
                    List<DbRecordEntity> list = null;
                    while (rs.next())
                    {
                        DbRecordEntity entity = new DbRecordEntity();
                        entity.F01 = rs.getInt(1);
                        entity.F02 = rs.getInt(2);
                        entity.F03 = rs.getInt(3);
                        entity.F04 = rs.getTimestamp(4);
                        entity.F05 = rs.getBigDecimal(5);
                        entity.F06 = rs.getBigDecimal(6);
                        entity.F07 = rs.getBigDecimal(7);
                        entity.F08 = rs.getString(8);
                        entity.type = rs.getString(9);
                        if (null == list)
                        {
                            list = new ArrayList<DbRecordEntity>();
                        }
                        list.add(entity);
                    }
                    return list == null || list.size() <= 0 ? null : list.toArray(new DbRecordEntity[list.size()]);
                }
            }, paging, sql.toString(), params);
        }
    }
    
    /**
     * 查询交易类型
     *
     * @return
     * @throws Throwable
     */
    @Override
    public T5122[] searchTypes()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<T5122[]>()
            {
                @Override
                public T5122[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<T5122> list = new ArrayList<>();
                    while (rs.next())
                    {
                        T5122 record = new T5122();
                        record.F01 = rs.getInt(1);
                        record.F02 = rs.getString(2);
                        list.add(record);
                    }
                    return list.toArray(new T5122[list.size()]);
                }
            }, "SELECT F01, F02 FROM S51.T5122 WHERE T5122.F03 = 'QY' AND F09 = 'yes'");
        }
        
    }
    
    /**
     * 判断用户是否网签
     *
     * @return
     * @throws Throwable
     */
    @Override
    public boolean isNetSign()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return isNetSign(serviceResource.getSession().getAccountId(), connection);
        }
    }
    
    @Override
    public int getGuarantId(String gCode, boolean flag)
        throws Throwable
    {
        StringBuffer sql = new StringBuffer("SELECT F02 FROM S61.T6125 WHERE T6125.F03 = ?");
        if (flag)
        {
            sql.append(" AND T6125.F05 = ?");
        }
        sql.append(" LIMIT 1");
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement(sql.toString()))
            {
                pstmt.setString(1, gCode);
                if (flag)
                {
                    pstmt.setString(2, T6125_F05.SQCG.name());
                }
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        if (isNetSign(resultSet.getInt(1), connection))
                        {
                            return resultSet.getInt(1);
                        }
                        else
                        {
                            return 0;
                        }
                        
                    }
                }
            }
        }
        return 0;
    }
    
    /**
     * 修改担保码
     * @param F03 担保码
     * @throws Throwable
     */
    @Override
    public void updateT6125F03(String F03)
        throws Throwable
    {
        if (StringHelper.isEmpty(F03))
        {
            throw new ParameterException("参数错误！");
        }
        try (Connection connection = getConnection())
        {
            int userId = serviceResource.getSession().getAccountId();
            if (isExistT6125F03(connection, F03))
            {
                throw new ParameterException("担保码已存在");
            }
            T6125 result = getT6125ByF02(connection, userId);
            if (T6125_F05.SQCG != result.F05)
            {
                throw new ParameterException("用户不是担保方");
            }
            updateT6125ForF03(connection, F03, userId);
        }
    }
    
    /**
     * <判断是否存储担保码>
     * <功能详细描述>
     * @param connection
     * @param F03
     * @return
     * @throws Throwable 
     */
    private boolean isExistT6125F03(Connection connection, String F03)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6125.F03 FROM S61.T6125 WHERE T6125.F03 = ? LIMIT 1"))
        {
            pstmt.setString(1, F03);
            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                {
                    return true;
                }
            }
            return false;
        }
    }
    
    /**
     * <判断是否存储担保码>
     * <功能详细描述>
     * @param connection
     * @param userId
     * @return
     * @throws Throwable 
     */
    private T6125 getT6125ByF02(Connection connection, int userId)
        throws Throwable
    {
        return select(connection, new ItemParser<T6125>()
        {
            @Override
            public T6125 parse(ResultSet resultSet)
                throws SQLException
            {
                if (resultSet.next())
                {
                    T6125 t6125 = new T6125();
                    t6125.F03 = resultSet.getString(1);
                    t6125.F05 = T6125_F05.parse(resultSet.getString(2));
                    return t6125;
                }
                return null;
            }
        }, "SELECT T6125.F03,T6125.F05 FROM S61.T6125 WHERE F02 = ? LIMIT 1", userId);
    }
    
    /**
     * <修改担保码>
     * <功能详细描述>
     * @param connection
     * @param F03
     * @param userId
     * @throws Throwable 
     */
    private void updateT6125ForF03(Connection connection, String F03, int userId)
        throws Throwable
    {
        execute(connection,
            "UPDATE S61.T6125 SET F03 = ?, F09 = ? WHERE F02 = ? ",
            F03,
            getCurrentTimestamp(connection),
            userId);
    }
}
