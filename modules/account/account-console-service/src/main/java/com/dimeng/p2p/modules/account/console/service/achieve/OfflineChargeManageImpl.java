package com.dimeng.p2p.modules.account.console.service.achieve;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6118_F02;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.S71.entities.T7150;
import com.dimeng.p2p.S71.enums.T7150_F03;
import com.dimeng.p2p.S71.enums.T7150_F05;
import com.dimeng.p2p.modules.account.console.service.OfflineChargeManage;
import com.dimeng.p2p.modules.account.console.service.entity.OfflineChargeImportRecord;
import com.dimeng.p2p.modules.account.console.service.entity.OfflineChargeRecord;
import com.dimeng.p2p.modules.account.console.service.query.OfflineChargeExtendsQuery;
import com.dimeng.p2p.modules.account.console.service.query.OfflineChargeQuery;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.DateTimeParser;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class OfflineChargeManageImpl extends AbstractUserService implements OfflineChargeManage
{
    
    public OfflineChargeManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
        
    }
    
    @Override
    public PagingResult<OfflineChargeRecord> search(OfflineChargeQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T7150.F01 AS F01, T7150.F02 AS F02, T7150.F03 AS F03, T7150.F04 AS F04, T7150.F05 AS F05, T7150.F06 AS F06, T7150.F07 AS F07, T7150.F08 AS F08, T7150.F09 AS F09, T7150.F10 AS F10, T7150.F11 AS F11, T6110.F02 AS F12 FROM S71.T7150 INNER JOIN S61.T6110 ON T7150.F02 = T6110.F01 WHERE 1 = 1");
        ArrayList<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            String account = query.getAccount();
            if (!StringHelper.isEmpty(account))
            {
                sql.append(" AND T6110.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(account));
            }
            Date date = query.getCreateStartDate();
            if (date != null)
            {
                sql.append(" AND DATE(T7150.F07) >= ?");
                parameters.add(date);
            }
            date = query.getCreateEndDate();
            if (date != null)
            {
                sql.append(" AND DATE(T7150.F07) <= ?");
                parameters.add(date);
            }
            T7150_F05 status = query.getStatus();
            if (status != null)
            {
                sql.append(" AND T7150.F05 = ?");
                parameters.add(status.name());
            }
        }
        sql.append(" ORDER BY T7150.F01 DESC");
        try (Connection connection = getConnection(P2PConst.DB_CONSOLE))
        {
            return selectPaging(connection, new ArrayParser<OfflineChargeRecord>()
            {
                ArrayList<OfflineChargeRecord> list = new ArrayList<OfflineChargeRecord>();
                
                @Override
                public OfflineChargeRecord[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    while (resultSet.next())
                    {
                        OfflineChargeRecord record = new OfflineChargeRecord();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = T7150_F03.parse(resultSet.getString(3));
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = T7150_F05.parse(resultSet.getString(5));
                        record.F06 = resultSet.getInt(6);
                        record.F07 = resultSet.getTimestamp(7);
                        record.F08 = resultSet.getString(8);
                        record.F09 = resultSet.getInt(9);
                        record.F10 = resultSet.getTimestamp(10);
                        record.F11 = resultSet.getString(11);
                        record.F12 = resultSet.getString(12);
                        record.F13 = getName(record.F06, connection);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return list.toArray(new OfflineChargeRecord[list.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    private String getName(int id, Connection connection)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F02 FROM S71.T7110 WHERE T7110.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, id);
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
    public int add(String accountName, BigDecimal amount, String remark)
        throws Throwable
    {
        if (StringHelper.isEmpty(accountName))
        {
            throw new ParameterException("用户账号不存在");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0)
        {
            throw new ParameterException("必须填写充值金额");
        }
        if (amount.compareTo(new BigDecimal("0.01")) == -1)
        {
            throw new ParameterException("充值金额不能少于0.01");
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                int userId = 0;
                try (PreparedStatement ps = connection.prepareStatement("SELECT F01 FROM S61.T6110 WHERE F02 = ?"))
                {
                    ps.setString(1, accountName);
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            userId = resultSet.getInt(1);
                        }
                        else
                        {
                            throw new LogicalException("用户账号不存在");
                        }
                    }
                }
                
                try (PreparedStatement pstmt = connection.prepareStatement("SELECT F02 FROM S61.T6118 WHERE F01 = ?"))
                {
                    pstmt.setInt(1, userId);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            if (T6118_F02.BTG == T6118_F02.parse(resultSet.getString(1)))
                            {
                                throw new LogicalException("用户账号实名认证未通过");
                            }
                        }
                    }
                }
                Map<String, Object> result = getEmpInfo(userId, connection);
                int rtnId =
                    insert(connection,
                        "INSERT INTO S71.T7150 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?,F08=?, F16 = ?, F17 = ? ",
                        userId,
                        T7150_F03.WLZH,
                        amount,
                        T7150_F05.DSH,
                        serviceResource.getSession().getAccountId(),
                        getCurrentTimestamp(connection),
                        remark,
                        result.get("empNum"),
                        result.get("empStatus"));
                writeLog(connection, "操作日志", "增加线下充值");
                serviceResource.commit(connection);
                return rtnId;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public void cancel(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT F05 FROM S71.T7150 WHERE F01 = ? FOR UPDATE "))
                {
                    ps.setInt(1, id);
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            T7150_F05 t7049_F05 = T7150_F05.parse(resultSet.getString(1));
                            if (t7049_F05 == T7150_F05.YQX)
                            {
                                serviceResource.rollback(connection);
                                return;
                            }
                            if (t7049_F05 != T7150_F05.DSH)
                            {
                                throw new LogicalException("不是待审核状态,不能取消");
                            }
                        }
                        else
                        {
                            throw new LogicalException("充值记录不存在");
                        }
                    }
                }
                
                try (PreparedStatement ps =
                    connection.prepareStatement("UPDATE S71.T7150 SET F05 = ?, F09 = ?, F10 = ? WHERE F01 = ?"))
                {
                    ps.setString(1, T7150_F05.YQX.name());
                    ps.setInt(2, serviceResource.getSession().getAccountId());
                    ps.setTimestamp(3, getCurrentTimestamp(connection));
                    ps.setInt(4, id);
                    ps.executeUpdate();
                }
                writeLog(connection, "操作日志", "线下充值审核不通过");
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public void check(int id, boolean passed)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new LogicalException("充值记录不存在！");
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                T7150 t7150 = selectT7150(connection, id);
                if (t7150 == null)
                {
                    throw new LogicalException("充值记录不存在！");
                }
                if (t7150.F05 != T7150_F05.DSH)
                {
                    throw new LogicalException("不是待审核状态,不能审核通过！");
                }
                Timestamp time = getCurrentTimestamp(connection);
                if (passed)
                {
                    T6101 t6101 = selectT6101(connection, t7150.F02, T6101_F03.WLZH, true);
                    if (t6101 == null)
                    {
                        throw new LogicalException("用户资金账户不存在！");
                    }
                    // 更新用户账号信息
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S61.T6101 SET F06 = F06 + ?, F07 = ? WHERE F01 = ?"))
                    {
                        pstmt.setBigDecimal(1, t7150.F04);
                        pstmt.setTimestamp(2, time);
                        pstmt.setInt(3, t6101.F01);
                        pstmt.executeUpdate();
                    }
                    
                    // 插入资金交易记录，充值
                    T6102 t6102 = new T6102();
                    t6102.F02 = t6101.F01;
                    t6102.F03 = FeeCode.CZ_XX;
                    t6102.F04 = t6101.F01;
                    t6102.F06 = t7150.F04;
                    t6102.F08 = t6101.F06.add(t7150.F04);
                    t6102.F09 = String.format("线下充值:%s", t7150.F08);
                    insertT6102(connection, t6102);
                    
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S71.T7150 SET F05 = ?, F09 = ?, F10 = ? WHERE F01 = ?"))
                    {
                        pstmt.setString(1, T7150_F05.YCZ.name());
                        pstmt.setInt(2, serviceResource.getSession().getAccountId());
                        pstmt.setTimestamp(3, time);
                        pstmt.setInt(4, id);
                        pstmt.executeUpdate();
                    }
                }
                else
                {
                    try (PreparedStatement ps =
                        connection.prepareStatement("UPDATE S71.T7150 SET F05 = ?, F09 = ?, F10 = ? WHERE F01 = ?"))
                    {
                        ps.setString(1, T7150_F05.YQX.name());
                        ps.setInt(2, serviceResource.getSession().getAccountId());
                        ps.setTimestamp(3, time);
                        ps.setInt(4, id);
                        ps.executeUpdate();
                    }
                }
                writeLog(connection, "操作日志", "线下充值审核通过");
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public int checkCharge(int id, boolean passed)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new LogicalException("充值记录不存在！");
        }
        int orderId = 0;
        try (Connection connection = getConnection())
        {
            try
            {
                T7150 t7150 = selectT7150(connection, id);
                if (t7150 == null)
                {
                    throw new LogicalException("充值记录不存在！");
                }
                if (t7150.F05 != T7150_F05.DSH)
                {
                    throw new LogicalException("不是待审核状态,不能审核通过！");
                }
                if (passed)
                {
                    orderId = addOfflineChargeOrder(connection, t7150);
                }
                else
                {
                    try (PreparedStatement ps =
                        connection.prepareStatement("UPDATE S71.T7150 SET F05 = ?, F09 = ?, F10 = ? WHERE F01 = ?"))
                    {
                        ps.setString(1, T7150_F05.YQX.name());
                        ps.setInt(2, serviceResource.getSession().getAccountId());
                        ps.setTimestamp(3, getCurrentTimestamp(connection));
                        ps.setInt(4, id);
                        ps.executeUpdate();
                    }
                }
            }
            catch (Exception e)
            {
                throw e;
            }
        }
        return orderId;
    }
    
    /**
     * 添加线下充值订单
     * 
     * @param connection
     * @param t7150
     * @return
     * @throws Throwable
     */
    private int addOfflineChargeOrder(Connection connection, T7150 t7150)
        throws Throwable
    {
        //int orderId = 0;
        T6501 t6501 = new T6501();
        t6501.F02 = OrderType.OFFLINECHARGE.orderType();
        t6501.F03 = T6501_F03.DTJ;
        t6501.F04 = getCurrentTimestamp(connection);
        t6501.F07 = T6501_F07.HT;
        t6501.F08 = t7150.F02;
        t6501.F09 = serviceResource.getSession().getAccountId();
        t6501.F13 = t7150.F04;
        int orderId = insertT6501(connection, t6501);
        /*try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6501 SET F02 = ?,F03 = ?, F04 = ?, F07 = ?, F09 = ?, F08 = ? ",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, OrderType.OFFLINECHARGE.orderType());
            pstmt.setString(2, T6501_F03.DTJ.name());
            pstmt.setTimestamp(3, getCurrentTimestamp(connection));
            pstmt.setString(4, T6501_F07.HT.name());
            pstmt.setInt(5, serviceResource.getSession().getAccountId());
            pstmt.setInt(6, t7150.F02);
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys();)
            {
                if (resultSet.next())
                {
                    orderId = resultSet.getInt(1);
                }
            }
        }*/
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6509 SET F01 = ?, F02 = ?, F03 = ?, F06 = ?"))
        {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, t7150.F02);
            pstmt.setBigDecimal(3, t7150.F04);
            pstmt.setInt(4, t7150.F01);
            pstmt.execute();
        }
        
        // 更新线下充值申请的审核人、审核时间，在订单提交成功后才更新申请的状态为“已充值”
        try (PreparedStatement ps = connection.prepareStatement("UPDATE S71.T7150 SET F09 = ?, F10 = ? WHERE F01 = ?"))
        {
            ps.setInt(1, serviceResource.getSession().getAccountId());
            ps.setTimestamp(2, getCurrentTimestamp(connection));
            ps.setInt(3, t7150.F01);
            ps.executeUpdate();
        }
        return orderId;
        
    }
    
    private T7150 selectT7150(Connection connection, int F01)
        throws SQLException
    {
        T7150 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S71.T7150 WHERE T7150.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T7150();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = T7150_F03.parse(resultSet.getString(3));
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = T7150_F05.parse(resultSet.getString(5));
                    record.F06 = resultSet.getInt(6);
                    record.F07 = resultSet.getTimestamp(7);
                    record.F08 = resultSet.getString(8);
                    record.F09 = resultSet.getInt(9);
                    record.F10 = resultSet.getTimestamp(10);
                    record.F11 = resultSet.getString(11);
                }
            }
        }
        return record;
    }
    
    @Override
    public void export(OfflineChargeRecord[] records, OutputStream outputStream, String charset)
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
            writer.write("用户名");
            writer.write("姓名或企业名称");
            writer.write("手机号");
            writer.write("用户类型");
            writer.write("充值时间");
            writer.write("建立操作人");
            writer.write("状态");
            writer.write("金额(元)");
            writer.write("审核人");
            writer.write("审核时间");
            writer.write("备注");
            writer.newLine();
            int index = 1;
            for (OfflineChargeRecord userRecharge : records)
            {
                if (userRecharge == null)
                {
                    continue;
                }
                writer.write(index++);
                writer.write(userRecharge.F12);
                writer.write(userRecharge.chargeUserName);
                writer.write(userRecharge.telPhone);
                writer.write(userRecharge.chargeUserType);
                writer.write(DateTimeParser.format(userRecharge.F07) + "\t");
                writer.write(userRecharge.createUserName);
                writer.write(userRecharge.F05.getChineseName());
                writer.write(Formater.formatAmount(userRecharge.F04));
                writer.write(userRecharge.auditorUserName);
                writer.write(Formater.formatDateTime(userRecharge.auditorTime) + "\t");
                writer.write(userRecharge.F08 + "\t");
                writer.newLine();
            }
        }
    }
    
    private void parameterProcess(StringBuffer sql, OfflineChargeExtendsQuery query, List<Object> parameters)
        throws ResourceNotFoundException, SQLException
    {
        if (query != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            String account = query.getAccount();
            if (!StringHelper.isEmpty(account))
            {
                sql.append(" AND T6110.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(account));
            }
            String createAccount = query.getCreateAccount();
            if (!StringHelper.isEmpty(createAccount))
            {
                sql.append(" AND (T7110_1.F02 LIKE ? ");
                parameters.add(sqlConnectionProvider.allMatch(createAccount));
                sql.append(" OR  T7150.F14 LIKE ?) ");
                parameters.add(sqlConnectionProvider.allMatch(createAccount));
            }
            String auditor = query.getAuditor();
            if (!StringHelper.isEmpty(auditor))
            {
                sql.append(" AND T7110_2.F02 LIKE ? ");
                parameters.add(sqlConnectionProvider.allMatch(auditor));
            }
            T7150_F05 status = query.getStatus();
            if (status != null)
            {
                sql.append(" AND T7150.F05 = ?");
                parameters.add(status.name());
            }
            Date date = query.getCreateStartDate();
            if (date != null)
            {
                sql.append(" AND DATE(T7150.F07) >= ?");
                parameters.add(date);
            }
            date = query.getCreateEndDate();
            if (date != null)
            {
                sql.append(" AND DATE(T7150.F07) <= ?");
                parameters.add(date);
            }
            date = query.getAuditorStartDate();
            if (date != null)
            {
                sql.append(" AND DATE(T7150.F10) >= ?");
                parameters.add(date);
            }
            date = query.getAuditorEndDate();
            if (date != null)
            {
                sql.append(" AND DATE(T7150.F10) <= ?");
                parameters.add(date);
            }
            String telPhone = query.getTelPhone();
            if (!StringHelper.isEmpty(telPhone))
            {
                sql.append(" AND T6110.F04 = ?");
                parameters.add(telPhone);
            }
            String name = query.getName();
            if (!StringHelper.isEmpty(name))
            {
                sql.append(" AND (T6141.F02 like ?");
                parameters.add(sqlConnectionProvider.allMatch(name));
                sql.append(" OR T6161.F04 like ?)");
                parameters.add(sqlConnectionProvider.allMatch(name));
            }
            
        }
    }
    
    @Override
    public PagingResult<OfflineChargeRecord> search(OfflineChargeExtendsQuery query, Paging paging)
        throws Throwable
    {
        StringBuffer sql =
            new StringBuffer(
                "SELECT T7150.F01 AS F01, T7150.F02 AS F02, T7150.F03 AS F03, T7150.F04 AS F04, T7150.F05 AS F05, T7150.F06 AS F06, T7150.F07 AS F07,"
                    + " T7150.F08 AS F08, T7150.F09 AS F09, T7150.F10 AS F10, T7150.F11 AS F11, T6110.F02 AS F12, T6141.F02 AS F13,  concat_ws('',T7110_1.F02,T6110_2.F02) AS F14,"
                    + " T7110_2.F02 AS F15,T6110.F06 AS F16,T6110.F10 AS F17, T7150.F10 AS F18, T6110.F04 AS F19, "
                    + " T6161.F04 AS F20"
                    + " FROM S71.T7150"
                    + " INNER JOIN S61.T6110 ON T7150.F02 = T6110.F01 "
                    + " LEFT JOIN S61.T6110 AS T6110_2 ON T7150.F06 = T6110_2.F01 "
                    + " LEFT JOIN S71.T7110 AS T7110_1 ON T7110_1.F01 = T7150.F06 "
                    + " LEFT JOIN S61.T6141 ON T6110.F01 = T6141.F01 LEFT JOIN S71.T7110 AS T7110_2 ON T7110_2.F01 = T7150.F09"
                    + " LEFT JOIN S61.T6161 ON T7150.F02 = T6161.F01" + " WHERE 1 = 1");
        List<Object> parameters = new ArrayList<>();
        parameterProcess(sql, query, parameters);
        sql.append(" ORDER BY T7150.F01 DESC");
        try (Connection connection = getConnection(P2PConst.DB_CONSOLE))
        {
            return selectPaging(connection, new ArrayParser<OfflineChargeRecord>() {
                ArrayList<OfflineChargeRecord> list = new ArrayList<OfflineChargeRecord>();

                @Override
                public OfflineChargeRecord[] parse(ResultSet resultSet)
                        throws SQLException {
                    while (resultSet.next()) {
                        OfflineChargeRecord record = new OfflineChargeRecord();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = T7150_F03.parse(resultSet.getString(3));
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = T7150_F05.parse(resultSet.getString(5));
                        record.F06 = resultSet.getInt(6);
                        record.F07 = resultSet.getTimestamp(7);
                        record.F08 = resultSet.getString(8);
                        record.F09 = resultSet.getInt(9);
                        record.F10 = resultSet.getTimestamp(10);
                        record.F11 = resultSet.getString(11);
                        record.F12 = resultSet.getString(12);
                        record.F13 = getName(record.F06, connection);

                        record.createUserName = resultSet.getString(14);
                        record.auditorUserName = resultSet.getString(15);
                        T6110_F06 F06 = T6110_F06.parse(resultSet.getString(16));
                        T6110_F10 F10 = T6110_F10.parse(resultSet.getString(17));
                        record.auditorTime = resultSet.getTimestamp(18);
                        record.telPhone = resultSet.getString(19);
                        if (T6110_F06.ZRR == F06) {
                            record.chargeUserType = "个人";
                            record.chargeUserName = resultSet.getString(13);
                        } else if (T6110_F06.FZRR == F06 && T6110_F10.F == F10) {
                            record.chargeUserType = "企业";
                            record.chargeUserName = resultSet.getString(20);
                        } else {
                            record.chargeUserType = "机构";
                            record.chargeUserName = resultSet.getString(20);
                        }
                        if (list == null) {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return list.toArray(new OfflineChargeRecord[list.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public BigDecimal xxczAmountCount(OfflineChargeExtendsQuery query)
        throws Throwable
    {
        StringBuffer sql =
            new StringBuffer("SELECT IFNULL(SUM(T7150.F04),0) AS F01 " + " FROM S71.T7150"
                + " INNER JOIN S61.T6110 ON T7150.F02 = T6110.F01 "
                + " LEFT JOIN S71.T7110 AS T7110_1 ON T7110_1.F01 = T7150.F06 "
                + " LEFT JOIN S61.T6141 ON T6110.F01 = T6141.F01 "
                + " LEFT JOIN S71.T7110 AS T7110_2 ON T7110_2.F01 = T7150.F09"
                + " LEFT JOIN S61.T6161 ON T7150.F02 = T6161.F01" + " WHERE 1 = 1");
        List<Object> parameters = new ArrayList<>();
        parameterProcess(sql, query, parameters);
        try (Connection connection = getConnection())
        {
            return selectBigDecimal(connection, sql.toString(), parameters);
        }
    }
    
    /**
     * 平台线下充值（没有实际资金流动）
     */
    @Override
    public void ptCharge(BigDecimal amount)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            int pid = getPTID(connection); // 平台用户id
            
            try
            {
                serviceResource.openTransactions(connection);
                // 平台往来账户信息
                T6101 cT6101 = selectT6101(connection, pid, T6101_F03.WLZH, true);
                cT6101.F06 = cT6101.F06.add(amount);
                // 更新平台资金账户
                updateT6101(connection, cT6101.F06, cT6101.F01);
                T6102 t6102 = new T6102();
                t6102.F02 = cT6101.F01;
                t6102.F03 = FeeCode.CZ_XX;
                t6102.F04 = cT6101.F01;
                t6102.F06 = amount;
                t6102.F08 = cT6101.F06;
                t6102.F09 = "平台线下充值";
                insertT6102(connection, t6102);
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
     * 平台线下提现
     */
    @Override
    public void ptWithdraw(BigDecimal amount)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            int pid = getPTID(connection); // 平台用户id
            
            try
            {
                serviceResource.openTransactions(connection);
                // 平台往来账户信息
                T6101 cT6101 = selectT6101(connection, pid, T6101_F03.WLZH, true);
                cT6101.F06 = cT6101.F06.subtract(amount);
                // 更新平台资金账户
                updateT6101(connection, cT6101.F06, cT6101.F01);
                T6102 t6102 = new T6102();
                t6102.F02 = cT6101.F01;
                t6102.F03 = FeeCode.TX_XX;
                t6102.F04 = cT6101.F01;
                t6102.F07 = amount;
                t6102.F08 = cT6101.F06;
                t6102.F09 = "平台线下提现";
                insertT6102(connection, t6102);
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    private void updateT6101(Connection connection, BigDecimal F01, int F02)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = ? WHERE F01 = ?"))
        {
            pstmt.setBigDecimal(1, F01);
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, F02);
            pstmt.execute();
        }
    }
    
    @Override
    public boolean isAccountExists(String accountName)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01 FROM S61.T6110 WHERE T6110.F02 = ? LIMIT 1"))
            {
                pstmt.setString(1, accountName);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 批量导入线下充值记录
     *
     * @param inputStream
     * @param contentType
     * @return
     * @throws Throwable
     */
    @Override
    public List<OfflineChargeImportRecord> batchImportRecord(InputStream inputStream, String contentType) throws Throwable {
        if (inputStream == null)
        {
            throw new ParameterException("读取文件流不存在.");
        }
        List<OfflineChargeImportRecord> records = new ArrayList<>();
        try (Connection connection = getConnection())
        {
            serviceResource.openTransactions(connection);
            Workbook wb = null;
            try {

                if (contentType.equals("application/vnd.ms-excel"))
                {
                    wb = new HSSFWorkbook(inputStream);//2003
                }
                else if (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                {
                    wb = new XSSFWorkbook(inputStream);//2007 2010
                }
                Sheet sheet = wb.getSheetAt(0);
                if(sheet.getLastRowNum() > 50 || sheet.getLastRowNum() <= 0)
                {
                    throw  new LogicalException("导入的数据行范围必须在1~50之间");
                }
                List<OfflineChargeImportRecord> rightRecords = new ArrayList<OfflineChargeImportRecord>();
                for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
                    Row currentRow = sheet.getRow(i);
                    String userName = currentRow.getCell(0).getStringCellValue();
                    String chargeAmount = currentRow.getCell(1).getNumericCellValue() + "";
                    String remark = currentRow.getCell(2).getStringCellValue();
                    OfflineChargeImportRecord record = new OfflineChargeImportRecord();
                    record.setUserName(userName);
                    record.setAmount(chargeAmount);
                    record.setRemark(remark);

                    if(StringHelper.isEmpty(remark) || remark.length() > 140)
                    {
                        record.setRemark(remark + "【原因：备注不能为空且内容长度不能超过140】");
                        records.add(record);
                        continue;
                    }
                    if (StringHelper.isEmpty(userName)
                            || StringHelper.isEmpty(chargeAmount))
                    {
                        record.setRemark(remark + "【原因：用户名或金额为空】");
                        records.add(record);
                        continue;
                    }

                    BigDecimal amount = BigDecimalParser.parse(chargeAmount);
                    if (null == amount || amount == BigDecimal.ZERO)
                    {
                        record.setRemark(remark + "【原因：金额格式错误】");
                        records.add(record);
                        continue;
                    }

                    int userId = isExistUser(connection, userName);
                    if (userId <= 0)
                    {
                        record.setRemark(remark + "【原因：用户名不存在】");
                        records.add(record);
                        continue;
                    }

                    record.setUserId(userId);
                    rightRecords.add(record);
                }


                if(records.size() == 0) {
                    for(OfflineChargeImportRecord record : rightRecords) {
                        Map<String, Object> result = getEmpInfo(record.getUserId(), connection);
                        insert(connection,
                                "INSERT INTO S71.T7150 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?,F08=?, F16 = ?, F17 = ? ",
                                record.getUserId(),
                                T7150_F03.WLZH,
                                BigDecimalParser.parse(record.getAmount()),
                                T7150_F05.DSH,
                                serviceResource.getSession().getAccountId(),
                                getCurrentTimestamp(connection),
                                record.getRemark(),
                                result.get("empNum"),
                                result.get("empStatus"));
                    }
                    writeLog(connection, "操作日志", "批量导入线下充值");
                }
                serviceResource.commit(connection);
            }
            catch (Throwable e)
            {
                serviceResource.rollback(connection);
                logger.error(e,e);
                throw e;
            }
        }
        return records;
    }

    /**
     * 检查用户是否存在
     * @param userName
     * @return
     * @throws Throwable
     */
    private int isExistUser(Connection connection,String userName) throws Throwable
    {
        try(PreparedStatement pstmt = connection.prepareStatement("SELECT T6110.F01 FROM S61.T6110 INNER JOIN S61.T6118 ON T6110.F01 = T6118.F01 WHERE T6110.F07 = 'QY' AND T6118.F02 = 'TG' AND T6110.F02 = ?"))
        {
            pstmt.setString(1, userName);
            try (ResultSet resultSet = pstmt.executeQuery()){
                if(resultSet.next())
                {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }

}
