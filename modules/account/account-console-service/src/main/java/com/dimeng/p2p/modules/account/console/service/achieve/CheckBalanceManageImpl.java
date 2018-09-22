/*
 * 文 件 名:  CheckBalanceManageImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年11月20日
 */
package com.dimeng.p2p.modules.account.console.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.entities.T6104;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6104_F07;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6502;
import com.dimeng.p2p.S65.entities.T6503;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.modules.account.console.service.CheckBalanceManage;
import com.dimeng.p2p.modules.account.console.service.entity.CheckBalanceRecord;
import com.dimeng.p2p.modules.account.console.service.entity.CheckBalanceTotalAmount;
import com.dimeng.p2p.modules.account.console.service.query.CheckBalanceQuery;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author xiaoqi
 * @version [版本号, 2015年11月20日]
 */
public class CheckBalanceManageImpl extends AbstractUserService implements CheckBalanceManage
{
    
    public CheckBalanceManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public int recharge(BigDecimal amount, String remark, boolean tg)
        throws Throwable
    {
        if (amount.compareTo(new BigDecimal(0)) <= 0)
        {
            throw new ParameterException("充值金额必须大于0");
        }
        int consoleAccountId = serviceResource.getSession().getAccountId();
        try (Connection connection = getConnection())
        {
            try
            {
                Timestamp time = getCurrentTimestamp(connection);
                serviceResource.openTransactions(connection);
                int ptid = getPTID(connection);
                T6501 t6501 = new T6501();
                t6501.F02 = OrderType.PLATFORM_CHARGE.orderType();
                t6501.F03 = T6501_F03.DTJ;
                t6501.F04 = time;
                t6501.F07 = T6501_F07.HT;
                t6501.F08 = ptid;
                t6501.F09 = consoleAccountId;
                t6501.F13 = amount;
                int oid = insertT6501(connection, t6501);
                
                T6502 t6502 = new T6502();
                t6502.F01 = oid;
                t6502.F02 = ptid;
                t6502.F03 = amount;
                t6502.F07 = 0;
                insertT6502(connection, t6502);
                
                T6104 t6104 = new T6104();
                t6104.F02 = ptid;
                t6104.F03 = oid;
                t6104.F04 = FeeCode.PTCZ;
                t6104.F05 = amount;
                t6104.F06 = new BigDecimal(0);
                t6104.F07 = T6104_F07.DTJ;
                t6104.F08 = time;
                t6104.F09 = consoleAccountId;
                t6104.F10 = remark;
                insertT6104(connection, t6104);
                
                serviceResource.commit(connection);
                return oid;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public int withdraw(BigDecimal amount, String remark, String bankCard, boolean tg)
        throws Throwable
    {
        if (amount.compareTo(new BigDecimal(0)) <= 0)
        {
            throw new ParameterException("提现金额必须大于0!");
        }
        if (amount.compareTo(getPTZHBalance(tg)) > 0)
        {
            throw new LogicalException("账户余额不足");
        }
        if (StringHelper.isEmpty(bankCard))
        {
            throw new LogicalException("提现银行卡号不能为空");
        }
        int consoleAccountId = serviceResource.getSession().getAccountId();
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                Timestamp time = getCurrentTimestamp(connection);
                int ptid = getPTID(connection);
                T6501 t6501 = new T6501();
                t6501.F02 = OrderType.PLATFORM_WITHDRAW.orderType();
                t6501.F03 = T6501_F03.DTJ;
                t6501.F04 = time;
                t6501.F07 = T6501_F07.HT;
                t6501.F08 = ptid;
                t6501.F09 = consoleAccountId;
                t6501.F13 = amount;
                int oid = insertT6501(connection, t6501);
                
                T6503 t6503 = new T6503();
                t6503.F01 = oid;
                t6503.F02 = ptid;
                t6503.F03 = amount;
                t6503.F07 = 0;
                t6503.F06 = bankCard;
                insertT6503(connection, t6503);
                
                T6104 t6104 = new T6104();
                t6104.F02 = ptid;
                t6104.F03 = oid;
                t6104.F04 = FeeCode.PTTX;
                t6104.F05 = new BigDecimal(0);
                t6104.F06 = amount;
                t6104.F07 = T6104_F07.DTJ;
                t6104.F08 = time;
                t6104.F09 = consoleAccountId;
                t6104.F10 = remark;
                insertT6104(connection, t6104);
                
                serviceResource.commit(connection);
                return oid;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public void export(CheckBalanceRecord[] records, OutputStream outputStream, String charset, boolean tg)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (records == null)
        {
            return;
        }
        if (StringHelper.isEmpty(charset))
        {
            charset = "GBK";
        }
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, charset)))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("订单号");
            writer.write("收入(元)");
            writer.write("支出(元)");
            writer.write("类型");
            if (tg)
            {
                writer.write("状态");
            }
            writer.write("操作人");
            writer.write("操作时间");
            writer.write("备注");
            writer.newLine();
            int index = 1;
            for (CheckBalanceRecord cbr : records)
            {
                if (cbr == null)
                {
                    continue;
                }
                writer.write(index++);
                writer.write(cbr.orderId);
                writer.write(cbr.income);
                writer.write(cbr.expend);
                writer.write(cbr.type);
                if (tg)
                {
                    writer.write(cbr.status.getChineseName());
                }
                writer.write(cbr.operationer);
                writer.write(cbr.operationTime);
                writer.write(cbr.remark + "\t");
                writer.newLine();
            }
        }
    }
    
    @Override
    public PagingResult<CheckBalanceRecord> search(CheckBalanceQuery query, Paging page, boolean tg)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6104.F01 AS F01,T6104.F02 AS F02,T6104.F03 AS F03,T6104.F04 AS F04,T6104.F05 AS F05,T6104.F06 AS F06,T6104.F07 AS F07,T6104.F08 AS F08,"
                    + "T6104.F09 AS F09,T6104.F10 AS F10,T7110.F02 AS F11 FROM S61.T6104,S71.T7110 WHERE T6104.F09=T7110.F01 ");
        ArrayList<Object> parameters = new ArrayList<Object>();
        if (!tg)
        {
            sql.append(" AND T6104.F07='CG' ");
        }
        dealSqlAndParams(query, sql, parameters);
        sql.append(" ORDER BY T6104.F08 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<CheckBalanceRecord>()
            {
                ArrayList<CheckBalanceRecord> list = new ArrayList<CheckBalanceRecord>();
                
                @Override
                public CheckBalanceRecord[] parse(ResultSet rs)
                    throws SQLException
                {
                    while (rs.next())
                    {
                        CheckBalanceRecord record = new CheckBalanceRecord();
                        record.orderId = rs.getString(3);
                        record.income = rs.getBigDecimal(5);
                        record.expend = rs.getBigDecimal(6);
                        record.type = getTransType(connection, rs.getString(4));
                        record.operationer = rs.getString(11);
                        record.operationTime = rs.getTimestamp(8);
                        record.remark = rs.getString(10);
                        record.status = T6104_F07.parse(rs.getString(7));
                        if (list == null)
                        {
                            list = new ArrayList<CheckBalanceRecord>();
                        }
                        list.add(record);
                    }
                    return list.toArray(new CheckBalanceRecord[list.size()]);
                }
            }, page, sql.toString(), parameters);
        }
    }
    
    @Override
    public CheckBalanceRecord searchAmount(CheckBalanceQuery query, boolean tg)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT IFNULL(SUM(T6104.F05), 0),IFNULL(SUM(T6104.F06), 0),"
                    + "T6104.F09 AS F09,T6104.F10 AS F10,T7110.F02 AS F11 FROM S61.T6104,S71.T7110 WHERE T6104.F09=T7110.F01 ");
        List<Object> parameters = new ArrayList<Object>();
        if (!tg)
        {
            sql.append(" AND T6104.F07='CG' ");
        }
        // sql语句和查询参数处理
        dealSqlAndParams(query, sql, parameters);
        sql.append(" ORDER BY T6104.F08 DESC");
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<CheckBalanceRecord>()
            {
                @Override
                public CheckBalanceRecord parse(ResultSet resultSet)
                    throws SQLException
                {
                    CheckBalanceRecord count = new CheckBalanceRecord();
                    if (resultSet.next())
                    {
                        count.income = resultSet.getBigDecimal(1);
                        count.expend = resultSet.getBigDecimal(2);
                    }
                    return count;
                }
            }, sql.toString(), parameters);
        }
    }
    
    @Override
    public CheckBalanceTotalAmount getSelectTotal(CheckBalanceQuery query, boolean tg)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT IFNULL(SUM(T6104.F05),0) AS F01,IFNULL(SUM(T6104.F06),0) AS F02 FROM S61.T6104,S71.T7110 WHERE T6104.F09=T7110.F01 ");
        ArrayList<Object> parameters = new ArrayList<Object>();
        if (!tg)
        {
            sql.append(" AND T6104.F07='CG' ");
        }
        dealSqlAndParams(query, sql, parameters);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql.toString()))
            {
                int index = 1;
                for (Object obj : parameters)
                {
                    ps.setObject(index++, obj);
                }
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        CheckBalanceTotalAmount cbta = new CheckBalanceTotalAmount();
                        cbta.totalIncomeAmount = rs.getBigDecimal(1);
                        cbta.totalExpendAmount = rs.getBigDecimal(2);
                        return cbta;
                    }
                }
            }
            return null;
        }
    }
    
    private void dealSqlAndParams(CheckBalanceQuery query, StringBuilder sql, List<Object> parameters)
        throws SQLException
    {
        if (query != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            String orderId = query.getOrderId();
            if (!StringHelper.isEmpty(orderId))
            {
                sql.append(" AND T6104.F03 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(orderId));
            }
            String type = query.getType();
            if (!StringHelper.isEmpty(type))
            {
                sql.append(" AND T6104.F04 = ?");
                parameters.add(Integer.parseInt(type));
            }
            String status = query.getStatus();
            if (!StringHelper.isEmpty(status))
            {
                sql.append(" AND T6104.F07 = ?");
                parameters.add(status);
            }
            String opertion = query.getOperationer();
            if (!StringHelper.isEmpty(opertion))
            {
                sql.append(" AND T7110.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(opertion));
            }
            Timestamp date = query.getOptStart();
            if (date != null)
            {
                sql.append(" AND DATE(T6104.F08) >= ?");
                parameters.add(date);
            }
            date = query.getOptEnd();
            if (date != null)
            {
                sql.append(" AND DATE(T6104.F08) <= ?");
                parameters.add(date);
            }
        }
    }
    
    private String getTransType(Connection connection, String type)
        throws SQLException
    {
        try (PreparedStatement ps = connection.prepareStatement("SELECT F02 FROM S51.T5122 WHERE T5122.F01=? "))
        {
            ps.setString(1, type);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getString(1);
                }
            }
        }
        return null;
    }
    
    /**
     * 插入T6501订单 <功能详细描述>
     * 
     * @param connection
     * @param entity
     * @return
     * @throws SQLException
     */
    /*private int insertT6501(Connection connection, T6501 entity) throws SQLException {
    	try (PreparedStatement pstmt = connection
    			.prepareStatement(
    					"INSERT INTO S65.T6501 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?",
    					PreparedStatement.RETURN_GENERATED_KEYS)) {
    		pstmt.setInt(1, entity.F02);
    		pstmt.setString(2, entity.F03.name());
    		pstmt.setTimestamp(3, entity.F04);
    		pstmt.setTimestamp(4, entity.F05);
    		pstmt.setTimestamp(5, entity.F06);
    		pstmt.setString(6, entity.F07.name());
    		pstmt.setInt(7, entity.F08);
    		pstmt.setInt(8, entity.F09);
    		pstmt.execute();
    		try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
    			if (resultSet.next()) {
    				return resultSet.getInt(1);
    			}
    			return 0;
    		}
    	}
    }*/
    
    private void insertT6503(Connection connection, T6503 entity)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6503 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?,F09 = ?"))
        {
            pstmt.setInt(1, entity.F01);
            pstmt.setInt(2, entity.F02);
            pstmt.setBigDecimal(3, entity.F03);
            pstmt.setBigDecimal(4, entity.F04);
            pstmt.setBigDecimal(5, entity.F05);
            pstmt.setString(6, entity.F06);
            pstmt.setInt(7, entity.F09);
            pstmt.execute();
        }
    }
    
    /**
     * 插入充值订单表 <功能详细描述>
     * 
     * @param connection
     * @param entity
     * @throws SQLException
     */
    private void insertT6502(Connection connection, T6502 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6502 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?"))
        {
            pstmt.setInt(1, entity.F01);
            pstmt.setInt(2, entity.F02);
            pstmt.setBigDecimal(3, entity.F03);
            pstmt.setBigDecimal(4, entity.F04);
            pstmt.setBigDecimal(5, entity.F05);
            pstmt.setString(6, entity.F06);
            pstmt.setInt(7, entity.F07);
            pstmt.execute();
        }
    }
    
    /**
     * 插入平台调账信息 <功能详细描述>
     * 
     * @param connection
     * @param entity
     * @throws SQLException
     */
    private void insertT6104(Connection connection, T6104 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S61.T6104 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08=? , F09=?, F10=? "))
        {
            pstmt.setInt(1, entity.F02);
            pstmt.setInt(2, entity.F03);
            pstmt.setInt(3, entity.F04);
            pstmt.setBigDecimal(4, entity.F05);
            pstmt.setBigDecimal(5, entity.F06);
            pstmt.setString(6, entity.F07.name());
            pstmt.setTimestamp(7, entity.F08);
            pstmt.setInt(8, entity.F09);
            pstmt.setString(9, entity.F10);
            pstmt.execute();
        }
    }
    
    @Override
    public BigDecimal getPTZHBalance(boolean tg)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F06 FROM S61.T6101 WHERE T6101.F02=? AND T6101.F03=? LIMIT 1"))
            {
                ps.setInt(1, getPTID(connection));
                ps.setString(2, T6101_F03.WLZH.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                    else
                    {
                        throw new LogicalException("平台往来账户不存在");
                    }
                }
            }
        }
    }
    
    @Override
    public CheckBalanceTotalAmount getTotal(boolean tg)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT IFNULL(SUM(F05),0) AS F01,IFNULL(SUM(F06),0) AS F02 FROM S61.T6104 WHERE F07='CG' ");
        CheckBalanceTotalAmount cbta = new CheckBalanceTotalAmount();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql.toString()))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        
                        cbta.totalIncomeAmount = rs.getBigDecimal(1);
                        cbta.totalExpendAmount = rs.getBigDecimal(2);
                        return cbta;
                    }
                }
            }
            return cbta;
        }
    }
    
    /** {@inheritDoc} */
    
    @Override
    public String getBankCardById(Integer bankCardId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F07 FROM S61.T6114 WHERE T6114.F01=? LIMIT 1"))
            {
                ps.setInt(1, bankCardId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getString(1);
                    }
                }
            }
        }
        return null;
    }
    
}
