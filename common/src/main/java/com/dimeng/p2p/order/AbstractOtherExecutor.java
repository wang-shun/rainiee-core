package com.dimeng.p2p.order;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Timestamp;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.p2p.P2PConst;

public abstract class AbstractOtherExecutor extends Resource
{
    
    protected static final int DECIMAL_SCALE = 9;
    
    protected final Logger logger = Logger.getLogger(getClass());
    
    public AbstractOtherExecutor(ResourceProvider resourceProvider)
    {
        super(resourceProvider);
    }
    
    @Override
    public void initilize(Connection connection)
        throws Throwable
    {
        
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
    
/*    
    protected void log(Connection connection, int F01, Throwable exception)
        throws SQLException
    {
        try
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Charset charset = Charset.forName(resourceProvider.getCharset());
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(out, charset));
            exception.printStackTrace(printWriter);
            printWriter.flush();
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
    }*/
    
    public void submit(Map<String, String> params)
        throws Throwable
    {
        // 锁订单
        Throwable exception = null;
        try (SQLConnection connection = getConnection())
        {
            try
            {
                connection.setAutoCommit(false);// 打开事务
                    // 执行业务操作
                    Savepoint businessSavepoint = connection.setSavepoint();
                    try
                    {
                        doSubmit(connection, params);
                    }
                    catch (Throwable e)
                    {
                        exception = e;
                        connection.rollback(businessSavepoint);
                        // 异常处理
                        handleError(connection);
                    }
                    connection.commit();
                    connection.setAutoCommit(true);
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
                  /*  if (exception != null)
                    {
                        log(connection, exception);
                    }*/
                    try
                    {
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                    catch (Throwable e)
                    {
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
    
    protected void doSubmit(SQLConnection connection, Map<String, String> params)
        throws Throwable
    {
        
    }
    
    protected void handleError(Connection connection)
    {
        
    }
    
}
