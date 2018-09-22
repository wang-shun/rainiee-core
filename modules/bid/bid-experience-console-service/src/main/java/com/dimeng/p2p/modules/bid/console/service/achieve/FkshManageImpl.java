package com.dimeng.p2p.modules.bid.console.service.achieve;

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

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6123_F05;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.modules.bid.console.service.FkshManage;
import com.dimeng.p2p.modules.bid.console.service.entity.CjRecord;
import com.dimeng.p2p.modules.bid.console.service.entity.Fksh;
import com.dimeng.p2p.modules.bid.console.service.entity.Tzr;
import com.dimeng.p2p.modules.bid.console.service.query.CjRecordQuery;
import com.dimeng.p2p.modules.bid.console.service.query.FkshQuery;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.TimestampParser;

public class FkshManageImpl extends AbstractBidService implements FkshManage
{
    
    public static class LoanCheckManageFactory implements ServiceFactory<FkshManage>
    {
        
        @Override
        public FkshManage newInstance(ServiceResource serviceResource)
        {
            return new FkshManageImpl(serviceResource);
        }
    }
    
    public FkshManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
        
    }
    
    @Override
    public PagingResult<Fksh> search(FkshQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6230.F01 AS F01, T6230.F02 AS F02, T6230.F03 AS F03, T6230.F04 AS F04, T6230.F05 AS F05, T6230.F06 AS F06, T6230.F25 AS F07, T6231.F02 AS F08, T6231.F11 AS F09,T6110.F02 AS F10,T6230.F07 AS F11, T6231.F21 AS F12, T6231.F22 AS F13,T6230.F20 AS F14,1 AS F15,(SELECT IFNULL(SUM(T6286.F04),0) FROM S62.T6286 WHERE T6286.F02 = T6230.F01)AS  F16,(SELECT IFNULL(SUM(T6292.F04),0) FROM S62.T6292 WHERE T6292.F02 = T6230.F01) AS F17 FROM S62.T6230 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 INNER JOIN S61.T6110 ON T6230.F02=T6110.F01 WHERE 1 = 1 ");
        ArrayList<Object> parameters = new ArrayList<>();
        searchParameter(sql, query, parameters);
        sql.append(" GROUP BY T6230.F01 ORDER BY T6230.F20 ASC, T6231.F11 DESC ");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Fksh>()
            {
                
                @Override
                public Fksh[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<Fksh> lists = new ArrayList<>();
                    while (resultSet.next())
                    {
                        Fksh record = new Fksh();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getInt(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F25 = resultSet.getString(7);
                        record.days = resultSet.getInt(8);
                        record.mbTime = resultSet.getTimestamp(9);
                        record.userName = resultSet.getString(10);
                        record.F07 = resultSet.getBigDecimal(11);
                        record.t6231_F21 = T6231_F21.parse(resultSet.getString(12));
                        record.limitDays = resultSet.getInt(13);
                        record.F20 = T6230_F20.parse(resultSet.getString(14));
                        record.experAmount = resultSet.getBigDecimal(16);
                        record.hbAmount = resultSet.getBigDecimal(17);
                        lists.add(record);
                    }
                    return lists.toArray(new Fksh[lists.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public Fksh searchAmount(FkshQuery query)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT IFNULL(SUM(t1.F01), 0), IFNULL(SUM(t1.F02), 0), IFNULL(SUM(t1.F16), 0), IFNULL(SUM(t1.F17), 0) FROM ( SELECT IFNULL(SUM(T6230.F05), 0) AS F01, IFNULL(SUM(T6230.F07), 0) AS F02, ( SELECT IFNULL(SUM(T6286.F04), 0) FROM S62.T6286 WHERE T6286.F02 = T6230.F01 ) AS F16, ( SELECT IFNULL(SUM(T6292.F04), 0) FROM S62.T6292 WHERE T6292.F02 = T6230.F01 ) AS F17 FROM S62.T6230 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 INNER JOIN S61.T6110 ON T6230.F02 = T6110.F01 WHERE 1 = 1 ");
        List<Object> parameters = new ArrayList<Object>();
        // sql语句和查询参数处理
        searchParameter(sql, query, parameters);
        sql.append(" GROUP BY T6230.F01 ORDER BY T6230.F20 ASC, T6231.F11 DESC ) AS t1");
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Fksh>()
            {
                @Override
                public Fksh parse(ResultSet resultSet)
                    throws SQLException
                {
                    Fksh count = new Fksh();
                    if (resultSet.next())
                    {
                        count.F05 = resultSet.getBigDecimal(1);
                        count.F07 = resultSet.getBigDecimal(2);
                        count.experAmount = resultSet.getBigDecimal(3);
                        count.hbAmount = resultSet.getBigDecimal(4);
                    }
                    return count;
                }
            }, sql.toString(), parameters);
        }
    }
    
    private void searchParameter(StringBuilder sql, FkshQuery query, List<Object> parameters)
        throws ResourceNotFoundException, SQLException
    {
        if (query != null)
        {
            String account = query.getAccount();
            if (!StringHelper.isEmpty(account))
            {
                sql.append(" AND T6110.F02 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(account));
            }
            String zqId = query.getZqId();
            if (!StringHelper.isEmpty(zqId))
            {
                sql.append(" AND T6230.F25 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(zqId));
            }
            
            String statesType = query.getStatus();
            
            if ("1".equals(statesType))
            {
                sql.append(" AND T6230.F20=?");
                parameters.add(T6230_F20.DFK);
                
            }
            else if ("2".equals(statesType))
            {
                sql.append(" AND T6230.F20=?");
                parameters.add(T6230_F20.YLB);
            }
            else
            {
                sql.append(" AND T6230.F20 IN (?,?)");
                parameters.add(T6230_F20.DFK);
                parameters.add(T6230_F20.YLB);
            }
            Timestamp datetime = query.getStartExpireDatetime();
            if (datetime != null)
            {
                sql.append(" AND DATE(T6231.F11) >=?");
                parameters.add(datetime);
            }
            datetime = query.getEndExpireDatetime();
            if (datetime != null)
            {
                sql.append(" AND DATE(T6231.F11) <= ?");
                parameters.add(DateParser.format(datetime) + " 23:59:59.999");
            }
        }
    }
    
    @Override
    public void checkBatch(int[] ids, T6230_F20 status)
        throws Throwable
    {
        if (ids == null || ids.length <= 0)
        {
            throw new ParameterException("指定的记录ID不存在.");
        }
        for (int id : ids)
        {
            if (id <= 0)
            {
                continue;
            }
            check(id, status, "");
        }
    }
    
    private boolean checkStatus(T6230_F20 status, int id)
        throws Throwable
    {
        String select = "SELECT F20 FROM S62.T6230 WHERE F01=? FOR UPDATE";
        T6230_F20 oldStatus = null;
        try (Connection connection = getConnection(P2PConst.DB_USER))
        {
            try (PreparedStatement ps = connection.prepareStatement(select))
            {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        oldStatus = EnumParser.parse(T6230_F20.class, rs.getString(1));
                    }
                }
            }
        }
        if (oldStatus == null)
        {
            return true;
        }
        if (status == T6230_F20.HKZ && oldStatus != T6230_F20.DFK)
        {
            return true;
        }
        if (status == T6230_F20.YLB && oldStatus != T6230_F20.DFK)
        {
            return true;
        }
        return false;
    }
    
    @Override
    public void check(int id, T6230_F20 status, String des)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new ParameterException("指定的记录ID不存在.");
        }
        if (checkStatus(status, id))
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                Timestamp timestamp = getCurrentTimestamp(connection);
                String sql = "UPDATE S62.T6230 SET F20=? WHERE F01=?";
                execute(connection, sql, status, id);
                int userId = 0;
                BigDecimal amount = new BigDecimal(0);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F02,F05 FROM S62.T6230 WHERE T6230.F01 = ? LIMIT 1"))
                {
                    pstmt.setInt(1, id);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            userId = resultSet.getInt(1);
                            amount = resultSet.getBigDecimal(2);
                        }
                    }
                }
                BigDecimal F13 = BigDecimal.ZERO;
                List<Tzr> list = new ArrayList<>();
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F01, F04, F05,F03 FROM S62.T6251 WHERE T6251.F03 = ?"))
                {
                    pstmt.setInt(1, id);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        while (resultSet.next())
                        {
                            Tzr record = new Tzr();
                            record.id = resultSet.getInt(1);
                            record.userId = resultSet.getInt(2);
                            record.amount = resultSet.getBigDecimal(3);
                            record.title = resultSet.getString(4);
                            F13 = F13.add(record.amount);
                            list.add(record);
                        }
                    }
                }
                if (status == T6230_F20.HKZ)
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S62.T6231 SET F12 = ? WHERE F01 = ?"))
                    {
                        pstmt.setTimestamp(1, timestamp);
                        pstmt.setInt(2, id);
                        pstmt.execute();
                    }
                    /*int orderId = 0;
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("INSERT INTO S65.T6501 SET F02 = ?,F03 = ?, F04 = ?, F07 = ?, F08 = ?, F09 = ?",
                            PreparedStatement.RETURN_GENERATED_KEYS))
                    {
                        pstmt.setInt(1, OrderType.BID_CONFIRM.orderType());
                        pstmt.setString(2, T6501_F03.DTJ.name());
                        pstmt.setTimestamp(3, timestamp);
                        pstmt.setString(4, T6501_F07.HT.name());
                        pstmt.setInt(5, userId);
                        pstmt.setInt(6, serviceResource.getSession().getAccountId());
                        pstmt.execute();
                        try (ResultSet resultSet = pstmt.getGeneratedKeys();)
                        {
                            if (resultSet.next())
                            {
                                orderId = resultSet.getInt(1);
                            }
                        }
                    }*/
                    T6501 t6501 = new T6501();
                    t6501.F02 = OrderType.BID_CONFIRM.orderType();
                    t6501.F03 = T6501_F03.DTJ;
                    t6501.F04 = timestamp;
                    t6501.F07 = T6501_F07.HT;
                    t6501.F08 = userId;
                    t6501.F09 = serviceResource.getSession().getAccountId();
                    t6501.F13 = F13;
                    int orderId = insertT6501(connection, t6501);
                    
                    for (Tzr tzr : list)
                    {
                        try (PreparedStatement pstmt =
                            connection.prepareStatement("INSERT INTO S65.T6505 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?"))
                        {
                            pstmt.setInt(1, orderId);
                            pstmt.setInt(2, tzr.userId);
                            pstmt.setInt(3, id);
                            pstmt.setInt(4, tzr.id);
                            pstmt.setBigDecimal(5, tzr.amount);
                            pstmt.execute();
                        }
                    }
                    writeLog(connection, "操作日志", "放款审核通过");
                }
                else
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S62.T6231 SET F15 = ? WHERE F01 = ?"))
                    {
                        pstmt.setTimestamp(1, timestamp);
                        pstmt.setInt(2, id);
                        pstmt.execute();
                    }
                    BigDecimal creditAmount = new BigDecimal(0);
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("SELECT F03 FROM S61.T6116 WHERE T6116.F01 = ? FOR UPDATE"))
                    {
                        pstmt.setInt(1, userId);
                        try (ResultSet resultSet = pstmt.executeQuery())
                        {
                            if (resultSet.next())
                            {
                                creditAmount = resultSet.getBigDecimal(1);
                            }
                        }
                    }
                    if (creditAmount.compareTo(amount) < 0)
                    {
                        amount = creditAmount;
                    }
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S61.T6116 SET F03 = F03-? WHERE F01 = ?"))
                    {
                        pstmt.setBigDecimal(1, amount);
                        pstmt.setInt(2, userId);
                        pstmt.execute();
                    }
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("INSERT INTO S61.T6117 SET F02 = ?, F03 = ?, F04 = ?, F06 = ?, F07 = ?, F08 = ?",
                            PreparedStatement.RETURN_GENERATED_KEYS))
                    {
                        pstmt.setInt(1, userId);
                        pstmt.setInt(2, FeeCode.XY_LB_FH);
                        pstmt.setTimestamp(3, timestamp);
                        pstmt.setBigDecimal(4, amount);
                        pstmt.setBigDecimal(5, creditAmount.subtract(amount));
                        pstmt.setString(6, "放款失败流标");
                        pstmt.execute();
                    }
                    for (Tzr tzr : list)
                    {
                        String select = "SELECT F06 FROM S61.T6101 WHERE T6101.F02 = ? AND T6101.F03 = ? FOR UPDATE";
                        BigDecimal sdAmount = selectBigDecimal(connection, select, userId, T6101_F03.SDZH);
                        // 锁定金额
                        BigDecimal lock = tzr.amount;
                        Timestamp now = new Timestamp(System.currentTimeMillis());
                        if (sdAmount.compareTo(lock) < 0)
                        {
                            lock = sdAmount;
                        }
                        String t6101_1 = "UPDATE S61.T6101 SET F06=F06-?, F07 = ?  WHERE F02=? AND T6101.F03=?";
                        execute(connection, t6101_1, lock, timestamp, userId, T6101_F03.SDZH);
                        String t6101_2 = "UPDATE S61.T6101 SET F06=F06+?, F07 = ?  WHERE F02=? AND T6101.F03=?";
                        execute(connection, t6101_2, lock, timestamp, userId, T6101_F03.WLZH);
                        // 发站内信
                        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
                        String template = configureProvider.getProperty(LetterVariable.LOAN_FAILED);
                        Envionment envionment = configureProvider.createEnvionment();
                        envionment.set("datetime", TimestampParser.format(now));
                        envionment.set("title", tzr.title);
                        int letterId =
                            insert(connection,
                                "INSERT INTO S61.T6123 SET F02=?,F03=?,F04=?,F05=?",
                                now,
                                LetterVariable.LOAN_FAILED.getDescription(),
                                "",
                                timestamp,
                                userId,
                                T6123_F05.WD);
                        execute(connection,
                            "INSERT INTO S61.T6124 SET F01=?,F02=?",
                            letterId,
                            StringHelper.format(template, envionment));
                        writeLog(connection, "操作日志", "放款审核不通过,原因:" + des);
                    }
                }
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
    public PagingResult<CjRecord> search(CjRecordQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6230.F01 AS F01, T6230.F02 AS F02, T6230.F03 AS F03, T6230.F04 AS F04, T6230.F05 AS F05, T6230.F25 AS F06, T6230.F09 AS F07, T6231.F12 AS F08,T6110.F02 AS F09,T6230.F07 AS F10 , T6231.F21 AS F11,T6231.F22 AS F12,(SELECT IFNULL(SUM(T6286.F04),0) FROM S62.T6286 WHERE T6286.F02 = T6230.F01) AS F13,(SELECT IFNULL(SUM(T6292.F04),0) FROM S62.T6292 WHERE T6292.F02 = T6230.F01) AS F14 FROM S62.T6230 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 INNER JOIN S61.T6110 ON T6230.F02=T6110.F01 WHERE T6230.F20 IN(?, ?, ?, ?)");
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(T6230_F20.HKZ);
        parameters.add(T6230_F20.YDF);
        parameters.add(T6230_F20.YJQ);
        parameters.add(T6230_F20.YZR);
        searchCJParameter(sql, query, parameters);
        sql.append(" GROUP BY T6230.F01 ORDER BY T6231.F12 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<CjRecord>()
            {
                
                @Override
                public CjRecord[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<CjRecord> lists = new ArrayList<>();
                    while (resultSet.next())
                    {
                        CjRecord record = new CjRecord();
                        T6231 t6231 = new T6231();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getInt(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F25 = resultSet.getString(6);
                        record.days = resultSet.getInt(7);
                        record.fkTime = resultSet.getTimestamp(8);
                        record.accountName = resultSet.getString(9);
                        record.F07 = resultSet.getBigDecimal(10);
                        record.fkName = getName(record.F01);
                        record.experAmount = resultSet.getBigDecimal(13);
                        t6231.F21 = T6231_F21.parse(resultSet.getString(11));
                        t6231.F22 = resultSet.getInt(12);
                        record.F28 = t6231;
                        record.hbAmount = resultSet.getBigDecimal(14);
                        lists.add(record);
                    }
                    return lists.toArray(new CjRecord[lists.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public CjRecord searchCjAmount(CjRecordQuery query)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT IFNULL(SUM(t1.F01), 0), IFNULL(SUM(t1.F02), 0), IFNULL(SUM(t1.F16), 0), IFNULL(SUM(t1.F17), 0) FROM ( SELECT IFNULL(SUM(T6230.F05), 0) AS F01, IFNULL(SUM(T6230.F07), 0) AS F02, ( SELECT IFNULL(SUM(T6286.F04), 0) FROM S62.T6286 WHERE T6286.F02 = T6230.F01 ) AS F16, ( SELECT IFNULL(SUM(T6292.F04), 0) FROM S62.T6292 WHERE T6292.F02 = T6230.F01 ) AS F17 FROM S62.T6230 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 INNER JOIN S61.T6110 ON T6230.F02 = T6110.F01 WHERE T6230.F20 IN(?, ?, ?, ?) ");
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(T6230_F20.HKZ);
        parameters.add(T6230_F20.YDF);
        parameters.add(T6230_F20.YJQ);
        parameters.add(T6230_F20.YZR);
        // sql语句和查询参数处理
        searchCJParameter(sql, query, parameters);
        sql.append(" GROUP BY T6230.F01 ORDER BY T6231.F12 DESC ) AS t1");
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<CjRecord>()
            {
                @Override
                public CjRecord parse(ResultSet resultSet)
                    throws SQLException
                {
                    CjRecord count = new CjRecord();
                    if (resultSet.next())
                    {
                        count.F05 = resultSet.getBigDecimal(1);
                        count.F07 = resultSet.getBigDecimal(2);
                        count.experAmount = resultSet.getBigDecimal(3);
                        count.hbAmount = resultSet.getBigDecimal(4);
                    }
                    return count;
                }
            }, sql.toString(), parameters);
        }
    }
    
    private void searchCJParameter(StringBuilder sql, CjRecordQuery query, List<Object> parameters)
        throws ResourceNotFoundException, SQLException
    {
        if (query != null)
        {
            String userName = query.getUserName();
            if (!StringHelper.isEmpty(userName))
            {
                sql.append(" AND T6110.F02 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(userName));
            }
            int investType = query.getType();
            if (investType > 0)
            {
                sql.append(" AND T6230.F04=?");
                parameters.add(investType);
            }
            Timestamp datetime = query.getStartTime();
            if (datetime != null)
            {
                sql.append(" AND DATE(T6231.F12) >=?");
                parameters.add(datetime);
            }
            datetime = query.getEndTime();
            if (datetime != null)
            {
                sql.append(" AND DATE(T6231.F12) <= ?");
                parameters.add(datetime);
            }
            if (!StringHelper.isEmpty(query.getLoanNum()))
            {
                sql.append(" AND T6230.F25 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(query.getLoanNum()));
            }
        }
    }
    
    private String getName(int loanId)
        throws SQLException
    {
        int orderId = 0;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT F01 FROM S65.T6505 WHERE F03=? LIMIT 1"))
            {
                ps.setInt(1, loanId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        orderId = rs.getInt(1);
                    }
                }
            }
            int userId = 0;
            try (PreparedStatement ps = connection.prepareStatement("SELECT F09 FROM S65.T6501 WHERE F01=? LIMIT 1"))
            {
                ps.setInt(1, orderId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        userId = rs.getInt(1);
                    }
                }
            }
            try (PreparedStatement ps = connection.prepareStatement("SELECT F02 FROM S71.T7110 WHERE F01=? LIMIT 1"))
            {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getString(1);
                    }
                }
            }
        }
        return "";
    }
    
    @Override
    public BigDecimal totalAmount()
        throws Throwable
    {
        String sql = "SELECT SUM(F05) FROM S62.T6230 WHERE (F20=? OR F20=? OR F20=? OR F20=?)";
        try (Connection connection = getConnection())
        {
            return selectBigDecimal(connection, sql, T6230_F20.HKZ, T6230_F20.YDF, T6230_F20.YJQ, T6230_F20.YZR);
        }
    }
    
    @Override
    public BigDecimal totalFkAmount()
        throws Throwable
    {
        String sql = "SELECT SUM(F05-F07) FROM S62.T6230 WHERE (F20=? OR F20=? OR F20=? OR F20=?)";
        try (Connection connection = getConnection())
        {
            return selectBigDecimal(connection, sql, T6230_F20.HKZ, T6230_F20.YDF, T6230_F20.YJQ, T6230_F20.YZR);
        }
    }
    
    @Override
    public void export(CjRecord[] records, OutputStream outputStream, String charset)
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
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, charset)))
        {
            writer.write("序号");
            writer.write(",");
            writer.write("放款时间");
            writer.write(",");
            writer.write("借款账号");
            writer.write(",");
            writer.write("借款ID");
            writer.write(",");
            writer.write("借款金额(元)");
            writer.write(",");
            writer.write("投资金额(元)");
            writer.write(",");
            writer.write("红包金额(元)");
            writer.write(",");
            writer.write("体验金金额(元)");
            writer.write(",");
            writer.write("借款期限");
            writer.write(",");
            writer.write("放款人");
            writer.newLine();
            int index = 1;
            for (CjRecord record : records)
            {
                if (record == null)
                {
                    continue;
                }
                writer.write(" " + index++);
                writer.write(",");
                writer.write(" " + DateTimeParser.format(record.fkTime, "yyyy-MM-dd HH:mm"));
                writer.write(",");
                writer.write(record.accountName == null ? "" : record.accountName);
                writer.write(",");
                writer.write("\t" + record.F25);
                writer.write(",");
                writer.write(format(record.F05));
                writer.write(",");
                writer.write(format(record.F05.subtract(record.F07)));
                writer.write(",");
                writer.write(format(record.hbAmount));
                writer.write(",");
                writer.write(format(record.experAmount));
                writer.write(",");
                if (record.F28.F21 == T6231_F21.F)
                {
                    writer.write(Integer.toString(record.days) + "个月");
                }
                else
                {
                    writer.write(Integer.toString(record.F28.F22) + "天");
                }
                writer.write(",");
                writer.write(record.fkName);
                writer.newLine();
            }
            writer.flush();
        }
    }
    
    @Override
    public void exportFkshInfo(Fksh[] paramArrayOfYFundRecord, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (paramArrayOfYFundRecord == null)
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
            writer.write("借款ID");
            writer.write("借款标题");
            writer.write("借款账户");
            writer.write("借款金额(元)");
            writer.write("投资金额(元)");
            writer.write("红包金额(元)");
            writer.write("年化利率");
            writer.write("期限");
            writer.write("满标时间");
            writer.write("体验金金额(元)");
            writer.newLine();
            int index = 1;
            for (Fksh fksh : paramArrayOfYFundRecord)
            {
                if (fksh == null)
                {
                    continue;
                }
                writer.write(index++);
                writer.write("\t" + fksh.F25);
                writer.write(fksh.F03);
                writer.write(fksh.userName);
                writer.write(Formater.formatAmount(fksh.F05));
                writer.write(Formater.formatAmount(fksh.F05.subtract(fksh.F07)));
                writer.write(Formater.formatAmount(fksh.hbAmount));
                writer.write(Formater.formatRate(fksh.F06));
                if (T6231_F21.S == fksh.t6231_F21)
                {
                    writer.write(fksh.limitDays + "天");
                }
                else
                {
                    writer.write(fksh.days + "个月");
                }
                writer.write(DateTimeParser.format(fksh.mbTime, "yyyy-MM-dd HH:mm") + "\t");
                writer.write(Formater.formatAmount(fksh.experAmount));
                writer.newLine();
            }
        }
    }
}
