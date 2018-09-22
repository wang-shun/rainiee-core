package com.dimeng.p2p.modules.nciic.service.achieve;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import cn.com.nciic.www.SimpleCheckByJson;
import cn.com.nciic.www.SimpleCheckByJsonResponse;
import cn.com.nciic.www.service.IdentifierServiceStub;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.AbstractService;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.P2PConst;
import com.dimeng.p2p.modules.nciic.service.INciicManageService;
import com.dimeng.p2p.modules.nciic.service.entity.QueryResult;
import com.dimeng.p2p.modules.nciic.service.util.JSONUtil;
import com.dimeng.p2p.modules.nciic.service.variables.RzbVariable;
import com.dimeng.util.parser.IntegerParser;

public class NciicManageServiceImpl extends AbstractService implements INciicManageService
{
    
    public NciicManageServiceImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    private static class Result
    {
        boolean idPassed = false;
        
        boolean namePassed = false;
        
        InputStream image = null;
        
        int errorCode = 0;
        
        public boolean isPassed()
        {
            return idPassed && namePassed;
        }
    }
    
    protected Connection getConnection()
        throws ResourceNotFoundException, SQLException
    {
        return serviceResource.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
            .getConnection();
    }
    
    private Result doCheck(String id, String name)
        throws Throwable
    {
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        String req = String.format("{\"IDNumber\":\"%s\",\"Name\":\"%s\"}", id, name);
        String cred =
            String.format("{\"UserName\":\"%s\",\"Password\":\"%s\"}",
                configureProvider.getProperty(RzbVariable.RZB_ACCOUNT),
                configureProvider.getProperty(RzbVariable.RZB_PASSWORD));
        
        IdentifierServiceStub client = new IdentifierServiceStub();
        SimpleCheckByJson scbj = new SimpleCheckByJson();
        scbj.setCred(cred);
        scbj.setRequest(req);
        
        logger.info("认证宝实名请求参数：" + req);
        
        SimpleCheckByJsonResponse scbr = client.simpleCheckByJson(scbj);
        
        String resultStr = scbr.getSimpleCheckByJsonResult();
        logger.info("认证宝实名返回：" + resultStr);
        
        QueryResult queryResult = JSONUtil.toObject(resultStr, QueryResult.class);
        Result result = new Result();
        if ("一致".equals(queryResult.Identifier.Result))
        {
            result.idPassed = true;
            result.namePassed = true;
        }
        else
        {
            result.errorCode = IntegerParser.parse(queryResult.ResponseCode);
        }
        return result;
    }
    
    @Override
    public boolean check(String id, String name, String terminal, int accountId)
        throws Throwable
    {
        return check(id, name, false, terminal, accountId);
    }
    
    @Override
    public boolean check(String id, String name, boolean duplicatedName, String terminal, int accountId)
        throws Throwable
    {
        logger.info("check() start ");
        try (Connection connection = getConnection())
        {
            if (duplicatedName)
            {
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F02 FROM S71.T7122 WHERE F01 = ? AND F03 = 'TG' LIMIT 1"))
                {
                    pstmt.setString(1, id);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            return name.equalsIgnoreCase(resultSet.getString(1));
                        }
                    }
                }
            }
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03 FROM S71.T7122 WHERE F01 = ? AND F02 = ?"))
            {
                pstmt.setString(1, id);
                pstmt.setString(2, name);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return "TG".equalsIgnoreCase(resultSet.getString(1));
                    }
                }
            }
            
            Result result = null;
            try
            {
                result = doCheck(id, name);
            }
            catch (Throwable t)
            {
                serviceResource.log(t);
            }
            boolean passed = false;
            try
            {
                serviceResource.openTransactions(connection);
                if (result != null)
                {
                    passed = result.isPassed();
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("INSERT INTO S71.T7122 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F06 = ?, F07 = ?"))
                    {
                        pstmt.setString(1, id);
                        pstmt.setString(2, name);
                        pstmt.setString(3, passed ? "TG" : "SB");
                        if (result.image == null)
                        {
                            pstmt.setNull(4, Types.BLOB);
                        }
                        else
                        {
                            pstmt.setBlob(4, result.image);
                        }
                        pstmt.setInt(5, accountId);
                        pstmt.setString(6, terminal);
                        pstmt.execute();
                    }
                    
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("INSERT INTO S71.T7124 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F07 = ?"))
                    {
                        pstmt.setString(1, id);
                        pstmt.setString(2, name);
                        pstmt.setString(3, passed ? "TG" : "SB");
                        pstmt.setInt(4, result.errorCode);
                        pstmt.setString(5, terminal);
                        pstmt.execute();
                    }
                }
                
                int userId = serviceResource.getSession().getAccountId();
                
                if (passed)
                {
                    updateT6198F06(connection, userId, terminal);
                }
                else
                {
                    updateT6198F03(connection, userId, terminal);
                }
                logger.info("check() end ");
                serviceResource.commit(connection);
                return passed;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                logger.error(e.getMessage());
                throw e;
            }
        }
    }
    
    /**
     * 更新错误认证次数
     * @return
     * @throws Throwable
     */
    private void updateT6198F03(Connection connection, int userId, String terminal)
        throws Throwable
    {
        execute(connection, "UPDATE  S61.T6198 SET F03=F03+1,F04=? WHERE F02 = ?", terminal, userId);
    }
    
    /**
     * 更新认证通过时间
     * @return
     * @throws Throwable
     */
    private void updateT6198F06(Connection connection, int userId, String terminal)
        throws Throwable
    {
        execute(connection, "UPDATE  S61.T6198 SET F04=?,F06=now() WHERE F02 = ?", terminal, userId);
    }
    
}
