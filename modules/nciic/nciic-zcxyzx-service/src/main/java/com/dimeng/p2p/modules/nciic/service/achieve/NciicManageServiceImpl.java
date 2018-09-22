package com.dimeng.p2p.modules.nciic.service.achieve;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.bouncycastle.util.encoders.Base64;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.AbstractService;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.P2PConst;
import com.dimeng.p2p.modules.nciic.service.INciicManageService;
import com.dimeng.p2p.modules.nciic.service.variables.NciicZhongChengXinYuanVariable;
import com.dimeng.p2p.modules.nciic.util.QueryThread;
import com.dimeng.p2p.modules.nciic.util.QueryValidatorServices;

public class NciicManageServiceImpl extends AbstractService implements INciicManageService
{
    
    public NciicManageServiceImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    protected Connection getConnection()
        throws ResourceNotFoundException, SQLException
    {
        return serviceResource.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
            .getConnection();
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
            
            int result = -1;
            try
            {
                result = doCheck(name, id);
            }
            catch (Throwable t)
            {
                logger.error(t);
            }
            try
            {
                serviceResource.openTransactions(connection);
                boolean passed = false;
                if (result == 3)
                {// 一致3.查询的身份证号码与姓名对应（计费）
                    passed = true;
                }
                
                if (result == 1 || result == 2 || result == 3)
                {//一致3.查询的身份证号码与姓名对应（计费） 不一致2.查询的身份证号码与姓名不对应（计费）无身份证信1.息服务结果：库中无此号，请到户籍所在地进行核实！（计费）
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("INSERT INTO  S71.T7122 SET F01 = ?, F02 = ?, F03 = ?, F06 = ?, F07 = ?"))
                    {
                        pstmt.setString(1, id);
                        pstmt.setString(2, name);
                        pstmt.setString(3, passed ? "TG" : "SB");
                        pstmt.setInt(4, accountId);
                        pstmt.setString(5, terminal);
                        pstmt.execute();
                    }
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S71.T7124 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F07 = ?"))
                {
                    pstmt.setString(1, id);
                    pstmt.setString(2, name);
                    pstmt.setString(3, passed ? "TG" : "SB");
                    pstmt.setInt(4, result);
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
    
    public int doCheck(String realname, String idcard)
        throws UnsupportedEncodingException, Exception
    {
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        
        long start = System.currentTimeMillis();
        JaxWsProxyFactoryBean svr = new JaxWsProxyFactoryBean();
        svr.setServiceClass(QueryValidatorServices.class);
        svr.setAddress(configureProvider.getProperty(NciicZhongChengXinYuanVariable.URL)); // 这个地址有可能调整，请根据实际需要配置
        
        QueryValidatorServices service = (QueryValidatorServices)svr.create();
        System.setProperty("javax.net.ssl.trustStore", "");
        String resultXML = "";
        String datasource = "1A020202";// 数据类型1A020202=身份证信息查询
        
        String encodeUserName =
            QueryThread.encode(QueryThread.ENCODE_KEY,
                configureProvider.getProperty(NciicZhongChengXinYuanVariable.USER_NAME).getBytes("UTF-8")).toString();
        String encodePassword =
            QueryThread.encode(QueryThread.ENCODE_KEY,
                configureProvider.getProperty(NciicZhongChengXinYuanVariable.PASSWORD).getBytes("UTF-8")).toString();
        String encodeDatasource = QueryThread.encode(QueryThread.ENCODE_KEY, datasource.getBytes("UTF-8")).toString();
        String encodeParam =
            QueryThread.encode(QueryThread.ENCODE_KEY, (realname + "," + idcard).getBytes("GBK")).toString();
        
        int compStatus = -1;
        
        // 单条
        resultXML = service.querySingle(encodeUserName, encodePassword, encodeDatasource, encodeParam);
        String xml = new String(QueryThread.decode(QueryThread.ENCODE_KEY, Base64.decode(resultXML.getBytes())), "GBK");
        logger.info("实名认证返回： " + xml);
        
        Document xmlDoc = DocumentHelper.parseText(xml);
        Element data = xmlDoc.getRootElement();
        Element message = data.element("message");
        int status = Integer.parseInt(message.element("status").getText());
        if (0 == status)
        {// 0.处理成功
            Element policeCheckInfos = data.element("policeCheckInfos");
            compStatus = Integer.parseInt(policeCheckInfos.element("policeCheckInfo").element("compStatus").getText());
        }
        long end = System.currentTimeMillis();
        logger.info("实名认证耗时： " + (end - start) + "");
        return compStatus;
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
