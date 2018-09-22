package com.dimeng.p2p.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.AbstractService;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.p2p.P2PConst;
import com.dimeng.p2p.XyType;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6110_F17;
import com.dimeng.p2p.S61.enums.T6123_F05;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.enums.T6211_F03;
import com.dimeng.p2p.S62.enums.T6272_F06;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.enums.T6501_F11;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;

public abstract class AbstractP2PService extends AbstractService
{
    public AbstractP2PService(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    protected SQLConnectionProvider getSQLConnectionProvider()
        throws ResourceNotFoundException, SQLException
    {
        return serviceResource.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER);
    }
    
    protected Connection getConnection()
        throws ResourceNotFoundException, SQLException
    {
        return serviceResource.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
            .getConnection();
    }
    
    protected Connection getConnection(String db)
        throws ResourceNotFoundException, SQLException
    {
        return serviceResource.getDataConnectionProvider(SQLConnectionProvider.class,
            com.dimeng.p2p.variables.P2PConst.DB_MASTER_PROVIDER).getConnection(db);
    }
    
    protected Timestamp getCurrentTimestamp(Connection connection)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT CURRENT_TIMESTAMP()"))
        {
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getTimestamp(1);
                }
            }
        }
        return null;
    }
    
    protected Date getCurrentDate(Connection connection)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT CURRENT_DATE()"))
        {
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getDate(1);
                }
            }
        }
        return null;
    }
    
    /**
     * <dt>
     * <dl>
     * 描述：写操作日志.
     * </dl>
     * 
     * <dl>
     * 数据校验：
     * <ol>
     * <li>无</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 逻辑校验：
     * <ol>
     * <li>无</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 业务处理：
     * <ol>
     * <li>...</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 返回结果说明：
     * <ol>
     * <li>无</li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * @param type
     *            操作类别
     * @param log
     *            日志内容
     * @throws Throwable
     */
    protected void writeLog(Connection connection, String type, String log)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S71.T7120 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, serviceResource.getSession().getAccountId());
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setString(3, type);
            pstmt.setString(4, log);
            pstmt.setString(5, serviceResource.getSession().getRemoteIP());
            pstmt.execute();
        }
    }
    
    /** 操作类别
     *  日志内容
     * @param type
     * @param log
     * @throws Throwable
     */
    public void writeLog(String type, String log)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO S71.T7120 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?",
                    PreparedStatement.RETURN_GENERATED_KEYS))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setTimestamp(2, getCurrentTimestamp(connection));
                pstmt.setString(3, type);
                pstmt.setString(4, log);
                pstmt.setString(5, serviceResource.getSession().getRemoteIP());
                pstmt.execute();
            }
        }
    }
    
    /** 操作类别
     *  记录前台日志内容 从session中获取AccountId
     * @param type
     * @param log
     * @throws Throwable
     */
    public void writeFrontLog(String type, String log)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO S61.T6190 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?",
                    PreparedStatement.RETURN_GENERATED_KEYS))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setTimestamp(2, getCurrentTimestamp(connection));
                pstmt.setString(3, type);
                pstmt.setString(4, log);
                pstmt.setString(5, serviceResource.getSession().getRemoteIP());
                pstmt.execute();
            }
        }
    }
    
    /** 操作类别
     *  记录前台日志内容 传入AccountId
     * @param type
     * @param log
     * @throws Throwable
     */
    public void writeFrontLog(String type, String log, int accountId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO S61.T6190 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?",
                    PreparedStatement.RETURN_GENERATED_KEYS))
            {
                pstmt.setInt(1, accountId);
                pstmt.setTimestamp(2, getCurrentTimestamp(connection));
                pstmt.setString(3, type);
                pstmt.setString(4, log);
                pstmt.setString(5, "localhost");
                pstmt.execute();
            }
        }
    }
    
    public T6211[] getBidType()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<T6211> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02 FROM S62.T6211 WHERE T6211.F03 = ?"))
            {
                pstmt.setString(1, T6211_F03.QY.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T6211 record = new T6211();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T6211[list.size()]));
        }
    }
    
    /**
     * 容联云通讯发送短信方法
     *
     * @param connection
     * @param mobile
     *            手机
     * @param content
     *            内容
     * @param tempId
     *            模板id
     * @throws Throwable
     */
    protected void sendMsg(Connection connection, String mobile, String content, int tempId)
        throws Throwable
    {
        try
        {
            if (!StringHelper.isEmpty(content) && !StringHelper.isEmpty(mobile))
            {
                long msgId = 0;
                try (PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO S10._1040(F02,F03,F04,F05) values(?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS))
                {
                    ps.setInt(1, tempId);
                    ps.setString(2, content);
                    ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                    ps.setString(4, "W");
                    ps.execute();
                    try (ResultSet resultSet = ps.getGeneratedKeys())
                    {
                        if (resultSet.next())
                        {
                            msgId = resultSet.getLong(1);
                        }
                    }
                }
                if (msgId > 0)
                {
                    try (PreparedStatement ps =
                        connection.prepareStatement("INSERT INTO S10._1041(F01,F02) VALUES(?,?)"))
                    {
                        ps.setLong(1, msgId);
                        ps.setString(2, mobile);
                        ps.execute();
                    }
                }
                return;
            }
        }
        catch (Exception e)
        {
            logger.error(e, e);
            throw e;
        }
    }
    
    protected void sendMsg(Connection connection, String mobile, String content)
        throws Throwable
    {
        try
        {
            if (!StringHelper.isEmpty(content) && !StringHelper.isEmpty(mobile))
            {
                long msgId = 0;
                try (PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO S10._1040(F02,F03,F04,F05) values(?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS))
                {
                    ps.setInt(1, 0);
                    ps.setString(2, content);
                    ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                    ps.setString(4, "W");
                    ps.execute();
                    try (ResultSet resultSet = ps.getGeneratedKeys())
                    {
                        if (resultSet.next())
                        {
                            msgId = resultSet.getLong(1);
                        }
                    }
                }
                if (msgId > 0)
                {
                    try (PreparedStatement ps =
                        connection.prepareStatement("INSERT INTO S10._1041(F01,F02) VALUES(?,?)"))
                    {
                        ps.setLong(1, msgId);
                        ps.setString(2, mobile);
                        ps.execute();
                    }
                }
                return;
            }
        }
        catch (Exception e)
        {
            logger.error(e, e);
            throw e;
        }
    }
    
    protected void sendLetter(Connection connection, int userId, String title, String content)
        throws Throwable
    {
        int letterId = insertT6123(connection, userId, title, T6123_F05.WD);
        insertT6124(connection, letterId, content);
    }
    
    private int insertT6123(Connection connection, int F02, String F03, T6123_F05 F05)
        throws Throwable
    {
        try
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO S61.T6123 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?",
                    PreparedStatement.RETURN_GENERATED_KEYS))
            {
                pstmt.setInt(1, F02);
                pstmt.setString(2, F03);
                pstmt.setTimestamp(3, getCurrentTimestamp(connection));
                pstmt.setString(4, F05.name());
                
                pstmt.execute();
                try (ResultSet resultSet = pstmt.getGeneratedKeys();)
                {
                    if (resultSet.next())
                    {
                        return resultSet.getInt(1);
                    }
                    return 0;
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e, e);
            throw e;
        }
    }
    
    protected void insertT6124(Connection connection, int F01, String F02)
        throws SQLException
    {
        try
        {
            try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO S61.T6124 SET F01 = ?, F02 = ?"))
            {
                pstmt.setInt(1, F01);
                pstmt.setString(2, F02);
                pstmt.execute();
            }
        }
        catch (Exception e)
        {
            logger.error(e, e);
            throw e;
        }
    }
    
    protected BigDecimal selectBigDecimal(Connection connection, String sql, ArrayList<Object> paramters)
        throws SQLException
    {
        final BigDecimal decimal = new BigDecimal(0);
        return select(connection, new ItemParser<BigDecimal>()
        {
            @Override
            public BigDecimal parse(ResultSet resultSet)
                throws SQLException
            {
                if (resultSet.next())
                {
                    return resultSet.getBigDecimal(1);
                }
                return decimal;
            }
        }, sql, paramters);
    }
    
    /**
     * 根据用户ID、账户类型查询账户信息表
     * @param connection
     * @param F02
     * @param F03
     * @return
     * @throws SQLException
     */
    protected T6101 selectT6101(Connection connection, int F02, T6101_F03 F03, boolean isForUpdate)
        throws SQLException
    {
        T6101 record = null;
        StringBuffer sqlStr =
            new StringBuffer(
                "SELECT T6101.F01, T6101.F02, T6101.F03, T6101.F04, T6101.F05, T6101.F06, T6101.F07 FROM S61.T6101 T6101 WHERE F01 = (SELECT T.F01 FROM S61.T6101 T WHERE T.F02 = ? AND T.F03 = ? LIMIT 1) ");
        if (isForUpdate)
        {
            sqlStr.append(" FOR UPDATE");
        }
        try (PreparedStatement pstmt = connection.prepareStatement(sqlStr.toString()))
        {
            pstmt.setInt(1, F02);
            pstmt.setString(2, F03.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6101();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = T6101_F03.parse(resultSet.getString(3));
                    record.F04 = resultSet.getString(4);
                    record.F05 = resultSet.getString(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getTimestamp(7);
                }
            }
        }
        return record;
    }
    
    /**
     * 获取平台ID
     * @param connection
     * @return
     * @throws Throwable
     */
    protected int getPTID(Connection connection)
        throws Throwable
    {
        try (PreparedStatement ps = connection.prepareStatement("SELECT F01 FROM S71.T7101 LIMIT 1"))
        {
            try (ResultSet resultSet = ps.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
                else
                {
                    throw new LogicalException("平台账号不存在！");
                }
            }
        }
    }
    
    protected String getBatchId(String ids, List<Object> params)
    {
        if (!StringHelper.isEmpty(ids))
        {
            String[] idarr = ids.split(",");
            StringBuilder idsql = new StringBuilder();
            if (idarr.length > 0)
            {
                for (String id : idarr)
                {
                    idsql.append("?,");
                    params.add(id);
                }
                idsql = new StringBuilder(idsql.toString().substring(0, idsql.toString().length() - 1));
            }
            else
            {
                idsql.append("?");
                params.add(ids);
            }
            return idsql.toString();
        }
        return null;
    }
    
    protected int insertT6501(Connection connection, T6501 entity)
        throws Throwable
    {
        
        int orderId = 0;
        StringBuilder sql =
            new StringBuilder(
                "INSERT INTO S65.T6501 SET F02 = ?,F03 = ?,F04 = ?,F05 = ?,F06 = ?,F07 = ?,F08 = ?,F10 = ?,F11 = ?,F12 = ?,F13 = ?");
        if (entity.F09 != null)
        {
            sql.append(",F09 = ?");
        }
        try (PreparedStatement pstmt =
            connection.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, entity.F02);
            pstmt.setString(2, entity.F03.name());
            pstmt.setTimestamp(3, entity.F04);
            pstmt.setTimestamp(4, entity.F05);
            pstmt.setTimestamp(5, entity.F06);
            pstmt.setString(6, entity.F07.name());
            pstmt.setInt(7, entity.F08);
            pstmt.setString(8, entity.F10);
            pstmt.setString(9, entity.F11 == null ? T6501_F11.F.name() : entity.F11.name());
            pstmt.setString(10, entity.F12);
            pstmt.setBigDecimal(11, entity.F13);
            if (entity.F09 != null)
            {
                pstmt.setInt(12, entity.F09);
            }
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys();)
            {
                if (resultSet.next())
                {
                    orderId = resultSet.getInt(1);
                }
            }
        }
        if (orderId == 0)
        {
            logger.error("AbstractP2PService.insertT6501():数据库异常");
            throw new SQLException("数据库异常");
        }
        return orderId;
        
    }
    
    /**
     * 记录交易流水信息表
     * @param connection
     * @param entity
     * @return
     * @throws Throwable
     */
    protected int insertT6102(Connection connection, T6102 entity)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S61.T6102 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F12 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, entity.F02);
            pstmt.setInt(2, entity.F03);
            pstmt.setInt(3, entity.F04);
            pstmt.setTimestamp(4, getCurrentTimestamp(connection));
            pstmt.setBigDecimal(5, entity.F06);
            pstmt.setBigDecimal(6, entity.F07);
            pstmt.setBigDecimal(7, entity.F08);
            pstmt.setString(8, entity.F09);
            pstmt.setInt(9, entity.F12);
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        }
    }
    
    /**
     * 查询用户信息
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
    protected T6110 selectT6110(Connection connection, int F01)
        throws SQLException
    {
        T6110 record = null;
        try
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,F02, F03, F04, F05, F06, F07, F08, F09, F10, F17 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, F01);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6110();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = T6110_F06.parse(resultSet.getString(6));
                        record.F07 = T6110_F07.parse(resultSet.getString(7));
                        record.F08 = T6110_F08.parse(resultSet.getString(8));
                        record.F09 = resultSet.getTimestamp(9);
                        record.F10 = T6110_F10.parse(resultSet.getString(10));
                        record.F17 = T6110_F17.parse(resultSet.getString(11));
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e, e);
            throw e;
        }
        return record;
    }
    
    protected Map<String, Object> getEmpInfo(int userId, Connection connection)
        throws SQLException
    {
        Map<String, Object> result = new HashMap<String, Object>();
        String sql =
            "SELECT T7110.F12 AS F01, T7110.F05 AS F02 FROM S61.T6110 LEFT JOIN S71.T7110 ON T6110.F14 = T7110.F12 WHERE T6110.F01 = ? LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    result.put("empNum", rs.getString(1));
                    result.put("empStatus", rs.getString(2));
                }
            }
        }
        
        if (result.get("empNum") == null || StringHelper.isEmpty((String)result.get("empNum")))
        {
            sql =
                "SELECT T7110.F12 AS F01, T7110.F05 AS F02 FROM S61.T6110 LEFT JOIN S71.T7110 ON T6110.F14 = T7110.F12 WHERE T6110.F01 IN (SELECT F01 FROM S61.T6111 T1 WHERE F02 = (SELECT T2.F03 FROM S61.T6111 T2 WHERE T2.F01 = ? LIMIT 1)) LIMIT 1 ";
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        result.put("empNum", rs.getString(1));
                        result.put("empStatus", rs.getString(2));
                    }
                }
            }
        }
        return result;
    }
    
    /**
     * 判断用户是否网签
     * @param userId
     * @param connection
     * @return
     * @throws Throwable
     */
    protected boolean isNetSign(int userId, Connection connection)
        throws Throwable
    {
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        boolean isHasNetSign = BooleanParser.parse(configureProvider.getProperty(SystemVariable.IS_HAS_NETSIGN));
        if (isHasNetSign)
        {
            try (PreparedStatement ps =
                connection.prepareStatement(" SELECT T6272.F01 FROM S62.T6272 WHERE T6272.F02 = ? AND T6272.F03 = ( SELECT T5126.F02 FROM S51.T5126 WHERE T5126.F01 = ? ORDER BY T5126.F02 DESC LIMIT 1 ) LIMIT 1 "))
            {
                ps.setInt(1, userId);
                ps.setInt(2, XyType.WQXY);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }
    
    /**
     * 判断用户的网签协议是否保全
     * @param userId
     * @param connection
     * @return
     * @throws Throwable
     */
    protected boolean isSaveAgreement(int userId, Connection connection)
        throws Throwable
    {
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT T6272.F01 FROM S62.T6272 WHERE T6272.F02 = ? AND T6272.F03 = (SELECT T5125.F02 FROM S51.T5125 WHERE T5125.F01 = ? ORDER BY T5125.F02 DESC LIMIT 1)  AND T6272.F06 = ?  LIMIT 1"))
        {
            ps.setInt(1, userId);
            ps.setInt(2, XyType.WQXY);
            ps.setString(3, T6272_F06.YBQ.name());
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return true;
                }
            }
        }
        return false;
    }
}
