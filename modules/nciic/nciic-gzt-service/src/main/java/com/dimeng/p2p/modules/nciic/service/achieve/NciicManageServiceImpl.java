package com.dimeng.p2p.modules.nciic.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.AbstractService;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.P2PConst;
import com.dimeng.p2p.modules.nciic.service.INciicManageService;
import com.dimeng.p2p.modules.nciic.service.variables.GztVariable;
import com.dimeng.p2p.modules.nciic.util.DesUtil;
import com.dimeng.p2p.modules.nciic.util.webservices.QueryValidatorServices;
import com.dimeng.p2p.modules.nciic.util.webservices.QueryValidatorServicesService;
import com.dimeng.util.StringHelper;

public class NciicManageServiceImpl extends AbstractService implements INciicManageService
{
    
    private static Log logger = LogFactory.getLog(NciicManageServiceImpl.class);
    
    public NciicManageServiceImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    /**
     * 实名认证.
     * 
     * @param id
     *            身份证号码
     * @param name
     *            姓名
     * @return {@code boolean} 是否验证通过
     * @throws Throwable
     */
    @Override
    public boolean check(String id, String name, String terminal, int accountId)
        throws Throwable
    {
        return check(id, name, false, terminal, accountId);
    }
    
    protected Connection getConnection()
        throws ResourceNotFoundException, SQLException
    {
        return serviceResource.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
            .getConnection();
    }
    
    /**
     * 实名认证.
     * 
     * @param id
     *            身份证号码
     * @param name
     *            姓名
     * @param duplicatedName
     *            是否允许重名
     * @return {@code boolean} 是否验证通过
     * @throws Throwable
     */
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
            String compStatusResult = null;
            try
            {
                ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
                compStatusResult =
                    doCheck(configureProvider.format(GztVariable.ID5DATATYPE),
                        configureProvider.format(GztVariable.ID5DATAKEY),
                        configureProvider.format(GztVariable.ID5DATAACCOUNT),
                        configureProvider.format(GztVariable.ID5DATAPASSWORD),
                        id,
                        name);
                logger.info("check() compStatusResult: " + compStatusResult);
            }
            catch (Throwable t)
            {
                serviceResource.log(t);
            }
            boolean passed = false;
            try
            {
                serviceResource.openTransactions(connection);
                if (null != compStatusResult)
                {
                    if ("3".equals(compStatusResult))
                    {
                        passed = true;
                    }
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("INSERT INTO S71.T7122 SET F01 = ?, F02 = ?, F03 = ?, F06 = ?, F07 = ?"))
                    {
                        pstmt.setString(1, id);
                        pstmt.setString(2, name);
                        pstmt.setString(3, passed ? "TG" : "SB");
                        pstmt.setInt(4, accountId);
                        pstmt.setString(5, terminal);
                        pstmt.execute();
                    }
                    
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("INSERT INTO S71.T7124 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F07 = ?"))
                    {
                        pstmt.setString(1, id);
                        pstmt.setString(2, name);
                        pstmt.setString(3, passed ? "TG" : "SB");
                        pstmt.setInt(4, Integer.parseInt(compStatusResult));
                        pstmt.setString(5, terminal);
                        pstmt.execute();
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
                }
                logger.info("check() end ");
                serviceResource.commit(connection);
                return passed;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                logger.error(e, e);
                throw e;
            }
        }
    }
    
    private String doCheck(String type, String key, String account, String password, String id, String name)
        throws Throwable
    {
        
        logger.info("doCheck() start ");
        String compStatusResult = null;
        final QueryValidatorServicesService client = new QueryValidatorServicesService();
        final QueryValidatorServices service = client.getQueryValidatorServices();
        String check = name + "," + id;
        logger.info("doCheck() check: " + check);
        String resultXML =
            service.querySingle(DesUtil.encode(key, account),
                DesUtil.encode(key, password),
                DesUtil.encode(key, type),
                DesUtil.encode(key, check));
        resultXML = DesUtil.decodeValue(key, resultXML);
        logger.info("doCheck() resultXML: " + resultXML);
        if (!StringHelper.isEmpty(resultXML) && resultXML.indexOf("policeCheckInfos") != -1
            && resultXML.indexOf("<compStatus desc=\"比对状态\">") != -1)
        {
            compStatusResult = getCompStatusResult(resultXML);
            logger.info("doCheck() compStatusResult: " + compStatusResult);
        }
        return compStatusResult;
    }
    
    //获取compStatus数据
    private String getCompStatusResult(String resultXML)
    {
        logger.info("getCompStatusResult() start");
        int start = resultXML.indexOf("<compStatus desc=\"比对状态\">");
        int end = resultXML.indexOf("</compStatus>");
        String compStatus = resultXML.substring(start, end);
        return compStatus.substring(compStatus.lastIndexOf(">") + 1);
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
