/**
 * 
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
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.PaymentInstitution;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.modules.account.console.service.UserRechargeManage;
import com.dimeng.p2p.modules.account.console.service.entity.Czgl;
import com.dimeng.p2p.modules.account.console.service.entity.UserRecharge;
import com.dimeng.p2p.modules.account.console.service.query.CzglRecordExtendsQuery;
import com.dimeng.p2p.modules.account.console.service.query.CzglRecordQuery;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.EnumParser;

/**
 * @author guopeng
 * 
 */
public class UserRechargeManageImpl extends AbstractUserService implements UserRechargeManage
{
    public UserRechargeManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<UserRecharge> serarch(CzglRecordQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6502.F01 AS F01, T6502.F02 AS F02, T6502.F03 AS F03, T6502.F04 AS F04, T6502.F05 AS F05, T6502.F06 AS F06, T6502.F07 AS F07, T6502.F08 AS F08, T6110.F02 AS F09, T6110.F06 AS F10,T6501.F04 AS F11, T6141.F02 AS F12  FROM S65.T6502 INNER JOIN S61.T6110 ON T6502.F02 = T6110.F01 INNER JOIN S65.T6501 ON T6502.F01=T6501.F01 INNER JOIN S61.T6141 on T6141.F01 = T6502.F02 WHERE 1=1");
        ArrayList<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            String serialNumber = query.getSerialNumber();
            if (!StringHelper.isEmpty(serialNumber))
            {
                sql.append(" AND T6502.F08 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(serialNumber));
            }
            String loginName = query.getLoginName();
            if (!StringHelper.isEmpty(loginName))
            {
                sql.append(" AND T6110.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(loginName));
            }
            T6110_F06 userType = query.getUserType();
            if (userType != null)
            {
                sql.append(" AND T6110.F06=?");
                parameters.add(userType);
            }
            Timestamp datetime = query.getStartRechargeTime();
            if (datetime != null)
            {
                sql.append(" AND DATE(T6501.F04) >= ?");
                parameters.add(datetime);
            }
            datetime = query.getEndRechargeTime();
            if (datetime != null)
            {
                sql.append(" AND DATE(T6501.F04) <= ?");
                parameters.add(datetime);
            }
        }
        sql.append(" ORDER BY T6502.F01 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<UserRecharge>()
            {
                
                @Override
                public UserRecharge[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<UserRecharge> lists = new ArrayList<>();
                    while (resultSet.next())
                    {
                        UserRecharge record = new UserRecharge();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getBigDecimal(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F06 = resultSet.getString(6);
                        record.F07 = resultSet.getInt(7);
                        record.F08 = resultSet.getString(8);
                        record.userName = resultSet.getString(9);
                        record.userType = EnumParser.parse(T6110_F06.class, resultSet.getString(10));
                        record.createTime = resultSet.getTimestamp(11);
                        record.userRealName = resultSet.getString(12);
                        lists.add(record);
                    }
                    return lists.toArray(new UserRecharge[lists.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public Czgl getRechargeInfo()
        throws Throwable
    {
        String sql = "SELECT F01, F02, F03 FROM S70.T7022 ORDER BY F03 DESC LIMIT 1";
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Czgl>()
            {
                @Override
                public Czgl parse(ResultSet resultSet)
                    throws SQLException
                {
                    Czgl recharge = new Czgl();
                    if (resultSet.next())
                    {
                        recharge.totalAmount = resultSet.getBigDecimal(1);
                        recharge.charge = resultSet.getBigDecimal(2);
                        recharge.updateTime = resultSet.getTimestamp(3);
                    }
                    return recharge;
                }
            }, sql);
        }
    }
    
    @Override
    public int getAchieveVersion()
    {
        return 0;
    }
    
    @Override
    public BigDecimal getCzze()
        throws Throwable
    {
        BigDecimal ccze = new BigDecimal(0);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(T6502.F03) FROM S65.T6502 INNER JOIN S65.T6501 ON T6501.F01=T6502.F01 WHERE T6501.F03=? AND T6502.F02 <> (SELECT T7101.F01 FROM S71.T7101)"))
            {
                ps.setString(1, T6501_F03.CG.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        ccze = rs.getBigDecimal(1);
                    }
                }
            }
        }
        return ccze;
    }
    
    @Override
    public BigDecimal getCzsxf()
        throws Throwable
    {
        BigDecimal czsxf = new BigDecimal(0);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(T6502.F05) FROM S65.T6502 INNER JOIN S65.T6501 ON T6501.F01=T6502.F01 WHERE T6501.F03=? AND T6502.F02 <> (SELECT T7101.F01 FROM S71.T7101)"))
            {
                ps.setString(1, T6501_F03.CG.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        czsxf = rs.getBigDecimal(1);
                    }
                }
            }
        }
        return czsxf;
    }
    
    @Override
    public void export(UserRecharge[] records, OutputStream outputStream, String charset)
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
            writer.write("用户名");
            writer.write("姓名或企业名称");
            writer.write("充值金额(元)");
            writer.write("应收手续费(元)");
            writer.write("实收手续费(元)");
            writer.write("用户类型");
            writer.write("提交时间");
            writer.write("完成时间");
            writer.write("支付公司名称");
            writer.write("充值状态");
            writer.write("流水单号");
            writer.newLine();
            int index = 1;
            for (UserRecharge userRecharge : records)
            {
                if (userRecharge == null)
                {
                    continue;
                }
                writer.write(index++);
                writer.write(userRecharge.F01);
                writer.write(userRecharge.userName);
                writer.write(userRecharge.userRealName);
                writer.write(Formater.formatAmount(userRecharge.F03));
                writer.write(Formater.formatAmount(userRecharge.F04));
                writer.write(Formater.formatAmount(userRecharge.F05));
                if (T6110_F06.ZRR == userRecharge.userType)
                {
                    writer.write("个人");
                }
                else if (T6110_F06.FZRR == userRecharge.userType && T6110_F10.F == userRecharge.t6110_F10)
                {
                    writer.write("企业");
                }
                else
                {
                    writer.write("机构");
                }
                writer.write(DateTimeParser.format(userRecharge.createTime, "yyyy-MM-dd HH:mm:ss"));
                writer.write(userRecharge.chargeFinishTime == null ? ""
                    : DateTimeParser.format(userRecharge.chargeFinishTime, "yyyy-MM-dd HH:mm:ss") + "\t");
                writer.write(PaymentInstitution.getDescription(userRecharge.F07));
                writer.write(StringHelper.isEmpty(userRecharge.F08) ? "未支付" : "支付成功");
                if (userRecharge.F08 != null)
                {
                    writer.write("\t" + userRecharge.F08);
                }
                else
                {
                    writer.write("");
                }
                writer.newLine();
            }
        }
    }
    
    private void sqlAndParameterProcess(CzglRecordExtendsQuery query, StringBuffer sql, List<Object> parameters)
        throws Throwable
    {
        
        if (query != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            String orderNo = query.getOrderNo();
            if (!StringHelper.isEmpty(orderNo))
            {
                sql.append(" AND T6502.F01 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(orderNo));
            }
            String loginName = query.getLoginName();
            if (!StringHelper.isEmpty(loginName))
            {
                sql.append(" AND T6110.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(loginName));
            }
            String payComName = query.getPayComName();
            if (!StringHelper.isEmpty(payComName))
            {
                List<String> listCode = PaymentInstitution.getCodes(payComName);
                if (listCode != null && listCode.size() != 0)
                {
                    StringBuffer strsb = new StringBuffer(" AND ( ");
                    for (String code : listCode)
                    {
                        strsb.append(" T6502.F07 = ? OR");
                        parameters.add(code);
                    }
                    sql.append(strsb.substring(0, strsb.length() - 2)).append(") ");
                }
                else
                {
                    sql.append(" AND T6502.F07 = ?");
                    parameters.add("0" + payComName);
                }
            }
            
            Timestamp startTime = query.getStartRechargeTime();
            if (startTime != null)
            {
                sql.append(" AND DATE(T6501.F04) >= ?");
                parameters.add(startTime);
            }
            Timestamp endTime = query.getEndRechargeTime();
            if (endTime != null)
            {
                sql.append(" AND DATE(T6501.F04) <= ?");
                parameters.add(endTime);
            }
            String chargeStatus = query.getChargeStatus();
            Timestamp finishStartTime = query.getFinishStartRechargeTime();
            Timestamp finishEndTime = query.getFinishEndRechargeTime();
            boolean appendFinishTime = true;
            if (!StringHelper.isEmpty(chargeStatus))
            {
                if ("0".equals(chargeStatus))
                {
                    sql.append(" AND T6501.F03 <> ? ");
                    parameters.add(T6501_F03.CG.name());
                    appendFinishTime = false;
                }
                else if ("1".equals(chargeStatus))
                {
                    sql.append(" AND T6501.F03 = ? ");
                    parameters.add(T6501_F03.CG.name());
                }
            }
            else
            {
                if (finishStartTime != null || finishEndTime != null)
                {
                    sql.append(" AND T6501.F03 = ? ");
                    parameters.add(T6501_F03.CG.name());
                }
                
            }
            if (appendFinishTime)
            {
                if (finishStartTime != null)
                {
                    sql.append(" AND DATE(T6501.F06) >= ?");
                    parameters.add(finishStartTime);
                }
                if (finishEndTime != null)
                {
                    sql.append(" AND DATE(T6501.F06) <= ?");
                    parameters.add(finishEndTime);
                }
            }
            String serialNumber = query.getSerialNumber();
            if (!StringHelper.isEmpty(serialNumber))
            {
                sql.append(" AND T6502.F08 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(serialNumber));
            }
            String usersType = query.getUsersType();
            if (!StringHelper.isEmpty(usersType))
            {
                if ("1".equals(usersType))
                {
                    sql.append(" AND T6110.F06 = ? AND T6110.F10 = ?");
                    parameters.add(T6110_F06.ZRR.name());
                    parameters.add(T6110_F10.F.name());
                }
                else if ("2".equals(usersType))
                {
                    sql.append(" AND T6110.F06 = ? AND T6110.F10 = ?");
                    parameters.add(T6110_F06.FZRR.name());
                    parameters.add(T6110_F10.F.name());
                }
                else
                {
                    sql.append(" AND T6110.F06 = ? AND T6110.F10 = ?");
                    parameters.add(T6110_F06.FZRR.name());
                    parameters.add(T6110_F10.S.name());
                }
            }
        }
        sql.append(" ORDER BY T6502.F01 DESC");
    }
    
    @Override
    public PagingResult<UserRecharge> serarch(CzglRecordExtendsQuery query, Paging paging)
        throws Throwable
    {
        StringBuffer sql =
            new StringBuffer(
                "SELECT T6502.F01 AS F01, T6502.F02 AS F02, T6502.F03 AS F03, T6502.F04 AS F04, T6502.F05 AS F05, T6502.F06 AS F06, T6502.F07 AS F07, T6502.F08 AS F08, T6110.F02 AS F09, T6110.F06 AS F10, T6501.F04 AS F11, IFNULL(T6141.F02,'') AS F12, T6110.F10 AS F13, T6501.F06 AS F14 FROM S65.T6502 "
                    + "INNER JOIN S61.T6110 ON T6502.F02 = T6110.F01 INNER JOIN S65.T6501 ON T6502.F01=T6501.F01 INNER JOIN S61.T6141 ON T6110.F01 = T6141.F01 WHERE 1=1");
        List<Object> parameters = new ArrayList<Object>();
        // sql语句和查询参数处理
        sqlAndParameterProcess(query, sql, parameters);
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<UserRecharge>()
            {
                
                @Override
                public UserRecharge[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<UserRecharge> lists = new ArrayList<>();
                    while (resultSet.next())
                    {
                        UserRecharge record = new UserRecharge();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getBigDecimal(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F06 = resultSet.getString(6);
                        record.F07 = resultSet.getInt(7);
                        record.F08 = resultSet.getString(8);
                        record.userName = resultSet.getString(9);
                        if (T6110_F06.ZRR == EnumParser.parse(T6110_F06.class, resultSet.getString(10)))
                        {
                            record.userRealName = resultSet.getString(12);
                        }
                        else
                        {
                            record.userRealName = selectQyName(connection, resultSet.getInt(2));
                        }
                        record.userType = EnumParser.parse(T6110_F06.class, resultSet.getString(10));
                        record.createTime = resultSet.getTimestamp(11);
                        record.t6110_F10 = T6110_F10.parse(resultSet.getString(13));
                        record.payComName = PaymentInstitution.getDescription(resultSet.getInt(7));
                        record.chargeFinishTime = resultSet.getTimestamp(14);
                        lists.add(record);
                    }
                    return lists.toArray(new UserRecharge[lists.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    //查询企业名称
    protected String selectQyName(Connection connection, int F01)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F04 FROM S61.T6161 WHERE T6161.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getString(1);
                }
            }
        }
        return "";
    }
    
    @Override
    public UserRecharge chargeRecordCount(CzglRecordExtendsQuery query)
        throws Throwable
    {
        StringBuffer sql =
            new StringBuffer(
                "SELECT IFNULL(SUM(T6502.F03),0) AS F01,IFNULL(SUM(T6502.F04),0) AS F02, IFNULL(SUM(T6502.F05),0) AS F03 FROM S65.T6502 "
                    + "INNER JOIN S61.T6110 ON T6502.F02 = T6110.F01 INNER JOIN S65.T6501 ON T6502.F01=T6501.F01 INNER JOIN S61.T6141 ON T6110.F01 = T6141.F01 WHERE 1=1");
        List<Object> parameters = new ArrayList<Object>();
        // sql语句和查询参数处理
        sqlAndParameterProcess(query, sql, parameters);
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<UserRecharge>()
            {
                @Override
                public UserRecharge parse(ResultSet resultSet)
                    throws SQLException
                {
                    UserRecharge count = new UserRecharge();
                    if (resultSet.next())
                    {
                        count.countChargeAmount = resultSet.getBigDecimal(1);
                        count.countReceivableAmount = resultSet.getBigDecimal(2);
                        count.countPaidAmount = resultSet.getBigDecimal(3);
                    }
                    return count;
                }
            }, sql.toString(), parameters);
        }
    }
}
