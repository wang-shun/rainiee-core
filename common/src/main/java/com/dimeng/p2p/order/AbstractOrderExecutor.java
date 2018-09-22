package com.dimeng.p2p.order;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.P2PConst;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6123_F05;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.EnumParser;

public abstract class AbstractOrderExecutor extends Resource
{
    
    protected static final int DECIMAL_SCALE = 9;
    
    protected final Logger logger = Logger.getLogger(getClass());
    
    public AbstractOrderExecutor(ResourceProvider resourceProvider)
    {
        super(resourceProvider);
    }
    
    /**
    * add by ZhangXu 20151118
    * 更新充值订单的状态
    * @param t6501_F03
    * @param F02
    * @throws Throwable
    */
    public void updateQuickChargeStatus(T6501_F03 t6501_F03, int F02)
        throws Throwable
    {
        try (SQLConnection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F06 = ? WHERE F01 = ?"))
            {
                pstmt.setString(1, t6501_F03.name());
                pstmt.setTimestamp(2, getCurrentTimestamp(connection));
                pstmt.setInt(3, F02);
                pstmt.execute();
            }
        }
        
    }
    
    @Override
    public void initilize(Connection connection)
        throws Throwable
    {
        
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
                    throw new LogicalException("平台账号不存在");
                }
            }
        }
    }
    
    protected Timestamp getCurrentTimestamp(Connection connection)
        throws Throwable
    {
        try
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
        }
        catch (Exception e)
        {
            logger.error(e, e);
            throw e;
        }
        return null;
    }
    
    protected Date getCurrentDate(Connection connection)
        throws Throwable
    {
        try
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
        }
        catch (Exception e)
        {
            logger.error(e, e);
            throw e;
        }
        return null;
    }
    
    protected T6501 lock(Connection connection, int orderId, T6501_F03 t6501_F03)
        throws SQLException
    {
        T6501 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S65.T6501 WHERE T6501.F01 = ? AND T6501.F03 = ?  FOR UPDATE"))
        {
            pstmt.setInt(1, orderId);
            pstmt.setString(2, t6501_F03.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6501();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = T6501_F03.parse(resultSet.getString(3));
                    record.F04 = resultSet.getTimestamp(4);
                    record.F05 = resultSet.getTimestamp(5);
                    record.F06 = resultSet.getTimestamp(6);
                    record.F07 = EnumParser.parse(T6501_F07.class, resultSet.getString(7));
                }
            }
        }
        return record;
    }
    
    protected void updateSubmit(Connection connection, T6501_F03 t6501_F03, int F02)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F05 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, t6501_F03.name());
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, F02);
            pstmt.execute();
        }
    }
    
    protected void updateConfirm(Connection connection, T6501_F03 t6501_F03, int F02)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F06 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, t6501_F03.name());
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, F02);
            pstmt.execute();
        }
    }
    
    /**
     * 用于更新第三方返回的流水号信息
     * <功能详细描述>
     * @param connection
     * @param F10
     * @param F01
     * @throws SQLException
     */
    protected void updateT6501(SQLConnection connection, String F10, Integer F01)
        throws SQLException
    {
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        boolean escrowPrefix = BooleanParser.parse(configureProvider.getProperty(SystemVariable.ESCROW_PREFIX));
        
        if (!"huifu".equals(escrowPrefix))
        {
            try (PreparedStatement ps = connection.prepareStatement("UPDATE S65.T6501 SET F10 = ? WHERE F01 = ?"))
            {
                ps.setString(1, F10);
                ps.setInt(2, F01);
                ps.executeUpdate();
            }
        }
        else
        {
            try (PreparedStatement ps =
                connection.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F10 = ? WHERE F01 = ?"))
            {
                ps.setString(1, T6501_F03.CG.name());
                ps.setString(2, F10);
                ps.setInt(3, F01);
                ps.executeUpdate();
            }
        }
        
    }
    
    /**
     * 修改订单状态
     * <功能详细描述>
     * @param connection
     * @param F10
     * @param F01
     * @throws SQLException
     */
    protected void updateT6501Status(SQLConnection connection, String F03, Integer F01)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S65.T6501 SET F03 = ? , WHERE F01 = ? "))
        {
            pstmt.setString(1, F03);
            pstmt.setInt(2, F01);
            pstmt.execute();
        }
    }
    
    protected void log(Connection connection, int F01, Throwable exception)
        throws Exception
    {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream())
        {
            Charset charset = Charset.forName(resourceProvider.getCharset());
            try (PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(out, charset)))
            {
                exception.printStackTrace(printWriter);
                printWriter.flush();
            }
            try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO S65.T6550 SET F02 = ?, F03 = ?"))
            {
                pstmt.setInt(1, F01);
                pstmt.setString(2, new String(out.toByteArray(), charset));
                pstmt.execute();
            }
        }
        catch (Exception e)
        {
            logger.error(e, e);
            throw e;
        }
    }
    
    public void submit(int orderId, Map<String, String> params)
        throws Throwable
    {
        // 锁订单
        Throwable exception = null;
        try (SQLConnection connection = getConnection())
        {
            try
            {
                connection.setAutoCommit(false);// 打开事务
                T6501 order = lock(connection, orderId, T6501_F03.DTJ);
                if (order != null)
                {
                    // 执行业务操作
                    Savepoint businessSavepoint = connection.setSavepoint();
                    try
                    {
                        doSubmit(connection, orderId, params);
                    }
                    catch (Throwable e)
                    {
                        exception = e;
                        connection.rollback(businessSavepoint);
                        // 异常处理
                        handleError(connection, orderId);
                    }
                    // 修改订单状态
                    updateSubmit(connection, exception == null ? T6501_F03.DQR : T6501_F03.SB, orderId);
                    connection.commit();
                }
            }
            catch (Throwable t)
            {
                connection.rollback();
                exception = t;
                logger.error(t, t);
            }
            finally
            {
                if (connection != null)
                {
                    connection.setAutoCommit(true);
                    
                    if (exception != null)
                    {
                        log(connection, orderId, exception);
                    }
                    
                    connection.close();
                }
            }
            if (exception != null)
            {
                throw exception;
            }
        }
    }
    
    public void confirm(int orderId, Map<String, String> params)
        throws Throwable
    {
        // 锁订单
        Throwable exception = null;
        try (SQLConnection connection = getConnection())
        {
            try
            {
                connection.setAutoCommit(false);// 打开事务
                T6501 order = lock(connection, orderId, T6501_F03.DQR);
                if (order != null)
                {
                    // 执行业务操作
                    Savepoint businessSavepoint = connection.setSavepoint();
                    try
                    {
                        doConfirm(connection, orderId, params);
                    }
                    catch (Throwable e)
                    {
                        exception = e;
                        connection.rollback(businessSavepoint);
                        // 异常处理
                        handleError(connection, orderId);
                    }
                    // 修改订单状态
                    updateConfirm(connection, exception == null ? T6501_F03.CG : T6501_F03.SB, orderId);
                    connection.commit();
                }
            }
            catch (Throwable t)
            {
                connection.rollback();
                exception = t;
                logger.error(t, t);
            }
            finally
            {
                if (connection != null)
                {
                    connection.setAutoCommit(true);
                    
                    if (exception != null)
                    {
                        log(connection, orderId, exception);
                    }
                    
                    connection.close();
                }
            }
            
            if (exception != null)
            {
                throw exception;
            }
        }
    }
    
    protected void sendLetter(Connection connection, int userId, String title, String content)
        throws Throwable
    {
        int letterId = insertT6123(connection, userId, title, T6123_F05.WD);
        insertT6124(connection, letterId, content);
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
    
    protected void sendEmail(Connection connection, String title, String content, String address)
        throws Throwable
    {
        try
        {
            if (StringHelper.isEmpty(content) || StringHelper.isEmpty(title) || StringHelper.isEmpty(address))
            {
                return;
            }
            long msgId = 0;
            try (PreparedStatement ps =
                connection.prepareStatement("INSERT INTO S10._1046(F02,F03,F04,F05,F07) VALUES(?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS))
            {
                ps.setString(1, title);
                ps.setString(2, content);
                ps.setInt(3, 0);
                ps.setTimestamp(4, getCurrentTimestamp(connection));
                ps.setString(5, "W");
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
                try (PreparedStatement ps = connection.prepareStatement("INSERT INTO S10._1047(F01,F02) VALUES(?,?)"))
                {
                    ps.setLong(1, msgId);
                    ps.setString(2, address);
                    ps.execute();
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e, e);
            throw e;
        }
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
    
    protected SQLConnection getConnection()
        throws SQLException
    {
        try
        {
            SQLConnectionProvider connectionProvider =
                resourceProvider.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER);
            return connectionProvider.getConnection();
        }
        catch (ResourceNotFoundException e)
        {
            logger.error(e, e);
            throw e;
        }
    }
    
    protected T6110 selectT6110(Connection connection, int F01)
        throws SQLException
    {
        T6110 record = null;
        try
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, F01);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6110();
                        record.F02 = resultSet.getString(1);
                        record.F03 = resultSet.getString(2);
                        record.F04 = resultSet.getString(3);
                        record.F05 = resultSet.getString(4);
                        record.F06 = T6110_F06.parse(resultSet.getString(5));
                        record.F07 = T6110_F07.parse(resultSet.getString(6));
                        record.F08 = T6110_F08.parse(resultSet.getString(7));
                        record.F09 = resultSet.getTimestamp(8);
                        record.F10 = T6110_F10.parse(resultSet.getString(9));
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
    
    /**
    * 查询订单信息
    * 
    * @param connection
    * @param F01
    * @return
    * @throws SQLException
    */
    protected T6501 selectT6501(Connection connection, int F01)
        throws Throwable
    {
        T6501 record = null;
        try
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S65.T6501 WHERE T6501.F01 = ? "))
            {
                pstmt.setInt(1, F01);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6501();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = T6501_F03.parse(resultSet.getString(3));
                        record.F04 = resultSet.getTimestamp(4);
                        record.F05 = resultSet.getTimestamp(5);
                        record.F06 = resultSet.getTimestamp(6);
                        record.F07 = T6501_F07.parse(resultSet.getString(7));
                        record.F08 = resultSet.getInt(8);
                        record.F09 = resultSet.getInt(9);
                        record.F10 = resultSet.getString(10);
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
    
    protected abstract void doConfirm(SQLConnection connection, int orderId, Map<String, String> params)
        throws Throwable;
    
    protected void doSubmit(SQLConnection connection, int orderId, Map<String, String> params)
        throws Throwable
    {
        
    }
    
    protected void handleError(Connection connection, int orderId)
    {
        
    }
    
    /**
     * 获取第三方标识，托管会使用到
     * <功能详细描述>
     * @param connection
     * @param accoutId
     * @return
     * @throws SQLException
     */
    protected String getUserCustId(SQLConnection connection, int accoutId)
        throws SQLException
    {
        try (PreparedStatement ps = connection.prepareStatement("SELECT F03 FROM S61.T6119 WHERE T6119.F01 = ?"))
        {
            ps.setInt(1, accoutId);
            try (ResultSet resultSet = ps.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getString(1);
                }
            }
        }
        return null;
    }
    
    /**
     * 标信息
     * <查询用户借款人ID>
     * @param connection
     * @param F01
     * @return
     * @throws SQLException
     */
    protected int selectT6230s(Connection connection, int F01)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F02 FROM S62.T6230 WHERE T6230.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                    
                }
            }
        }
        return 0;
    }
    
    /**
     * 托管调用接口
     * <第三方接口调用>
     * @param connection
     * @param orderId
     * @param params
    * @throws SQLException 
     */
    protected void callFace(SQLConnection connection, int orderId, Map<String, String> params)
        throws SQLException
    {
        
    }
    
    /**
     * 托管调用接口
     * <异常回滚>
     * @param connection
     * @param orderId
     * @param params
     * @throws IOException 
     * @throws Throwable 
     */
    protected void rollbackFace(SQLConnection connection, int orderId, Map<String, String> params)
        throws Throwable
    {
        
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
    
    public void confirmAll(Map<String, String> params, int... orderIds)
        throws Throwable
    {
        try (SQLConnection connection = this.getConnection())
        {
            // 锁订单
            Throwable exception = null;
            try
            {
                connection.setAutoCommit(false);// 打开事务
                List<T6501> records = lockOrders(connection, T6501_F03.DQR, orderIds);
                T6501 order = null;
                for (int i = 0; i < records.size(); i++)
                {
                    order = records.get(i);
                    if (order != null)
                    {
                        try
                        {
                            doConfirm(connection, order.F01, params);
                        }
                        catch (Throwable e)
                        {
                            exception = e;
                            throw e;
                        }
                        // 修改订单状态
                        updateConfirm(connection, T6501_F03.CG, order.F01);
                    }
                }
                connection.commit();
            }
            catch (Throwable t)
            {
                connection.rollback();
                exception = t;
            }
            finally
            {
                if (connection != null)
                {
                    connection.setAutoCommit(true);
                    
                    if (exception != null)
                    {
                        try
                        {
                            //将所有的订单全部处理成失败并记录日志
                            for (int orderId : orderIds)
                            {
                                // 修改订单状态
                                updateConfirm(connection, T6501_F03.SB, orderId);
                                log(connection, orderId, exception);
                            }
                        }
                        catch (Throwable e)
                        {
                            logger.error(e);
                        }
                        
                    }
                }
            }
            if (exception != null)
            {
                throw exception;
            }
        }
    }
    
    protected List<T6501> lockOrders(Connection connection, T6501_F03 t6501_F03, int... orderId)
        throws SQLException
    {
        List<T6501> list = new ArrayList<T6501>();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < orderId.length; i++)
        {
            sb.append("?");
            if (i != orderId.length - 1)
            {
                sb.append(",");
            }
        }
        
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S65.T6501 WHERE T6501.F01 IN("
                + sb + ") AND T6501.F03 = ?  FOR UPDATE"))
        {
            for (int i = 0; i < orderId.length; i++)
            {
                pstmt.setInt(i + 1, orderId[i]);
            }
            pstmt.setString(orderId.length + 1, t6501_F03.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    T6501 record = new T6501();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = T6501_F03.parse(resultSet.getString(3));
                    record.F04 = resultSet.getTimestamp(4);
                    record.F05 = resultSet.getTimestamp(5);
                    record.F06 = resultSet.getTimestamp(6);
                    record.F07 = EnumParser.parse(T6501_F07.class, resultSet.getString(7));
                    list.add(record);
                }
            }
        }
        return list;
    }
    
    public void confirmAll(Map<String, String> params, List<Integer> orderIds)
        throws Throwable
    {
        try (SQLConnection connection = this.getConnection())
        {
            // 锁订单
            Throwable exception = null;
            try
            {
                connection.setAutoCommit(false);// 打开事务
                List<T6501> records = lockOrders(connection, T6501_F03.DQR, orderIds);
                T6501 order = null;
                for (int i = 0; i < records.size(); i++)
                {
                    order = records.get(i);
                    if (order != null)
                    {
                        try
                        {
                            doConfirm(connection, order.F01, params);
                        }
                        catch (Throwable e)
                        {
                            exception = e;
                            throw e;
                        }
                        // 修改订单状态
                        updateConfirm(connection, T6501_F03.CG, order.F01);
                    }
                }
                connection.commit();
            }
            catch (Throwable t)
            {
                connection.rollback();
                exception = t;
            }
            finally
            {
                if (connection != null)
                {
                    connection.setAutoCommit(true);
                    
                    if (exception != null)
                    {
                        try
                        {
                            //将所有的订单全部处理成失败并记录日志
                            for (int orderId : orderIds)
                            {
                                // 修改订单状态
                                updateConfirm(connection, T6501_F03.SB, orderId);
                                log(connection, orderId, exception);
                            }
                        }
                        catch (Throwable e)
                        {
                            logger.error(e);
                        }
                        
                    }
                }
            }
            if (exception != null)
            {
                throw exception;
            }
        }
    }
    
    protected List<T6501> lockOrders(Connection connection, T6501_F03 t6501_F03, List<Integer> orderId)
        throws SQLException
    {
        List<T6501> list = new ArrayList<T6501>();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < orderId.size(); i++)
        {
            sb.append("?");
            if (i != orderId.size() - 1)
            {
                sb.append(",");
            }
        }
        
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S65.T6501 WHERE T6501.F01 IN("
                + sb + ") AND T6501.F03 = ?  FOR UPDATE"))
        {
            for (int i = 0; i < orderId.size(); i++)
            {
                pstmt.setInt(i + 1, orderId.get(i));
            }
            pstmt.setString(orderId.size() + 1, t6501_F03.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    T6501 record = new T6501();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = T6501_F03.parse(resultSet.getString(3));
                    record.F04 = resultSet.getTimestamp(4);
                    record.F05 = resultSet.getTimestamp(5);
                    record.F06 = resultSet.getTimestamp(6);
                    record.F07 = EnumParser.parse(T6501_F07.class, resultSet.getString(7));
                    list.add(record);
                }
            }
        }
        return list;
    }
    
}
