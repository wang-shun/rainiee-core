package com.dimeng.p2p.modules.nciic.service.achieve;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.codehaus.xfire.client.Client;
import org.codehaus.xfire.client.XFireProxy;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;
import org.codehaus.xfire.transport.http.CommonsHttpMessageSender;
import org.codehaus.xfire.transport.http.EasySSLProtocolSocketFactory;
import org.codehaus.xfire.util.Base64;
import org.codehaus.xfire.util.dom.DOMOutHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.AbstractService;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.P2PConst;
import com.dimeng.p2p.modules.nciic.service.INciicManageService;
import com.dimeng.p2p.modules.nciic.service.variables.GajVariable;
import com.dimeng.util.StringHelper;

public class NciicManageServiceImpl extends AbstractService implements INciicManageService
{
    
    private static final HttpClientParams params = new HttpClientParams();
    
    static
    {
        params.setParameter(HttpClientParams.USE_EXPECT_CONTINUE, Boolean.FALSE);
        params.setParameter(HttpClientParams.CONNECTION_MANAGER_TIMEOUT, 1000L);
    }
    
    public NciicManageServiceImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    private static class Result
    {
        boolean idPassed = false;
        
        boolean namePassed = false;;
        
        InputStream image = null;
        
        int errorCode = 0;
        
        public Result()
        {
        }
        
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
    
    private Result doCheck(String url, String license, String account, String id, String name)
        throws Throwable
    {
        ProtocolSocketFactory easy = new EasySSLProtocolSocketFactory();
        Protocol protocol = new Protocol("https", easy, 443);
        Protocol.registerProtocol("https", protocol);
        Service serviceModel = new ObjectServiceFactory().create(CheckService.class, "NciicServices", null, null);
        CheckService service = (CheckService)new XFireProxyFactory().create(serviceModel, url);
        Client client = ((XFireProxy)Proxy.getInvocationHandler(service)).getClient();
        client.addOutHandler(new DOMOutHandler());
        client.setProperty(CommonsHttpMessageSender.GZIP_ENABLED, Boolean.TRUE);
        client.setProperty(CommonsHttpMessageSender.HTTP_CLIENT_PARAMS, params);
        StringBuilder condition = new StringBuilder();
        condition.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><ROWS><INFO><SBM>")
            .append(account)
            .append("</SBM></INFO><ROW><GMSFHM>公民身份号码</GMSFHM><XM>姓名</XM></ROW><ROW FSD=\"440300\" YWLX=\"会员身份认证\"><GMSFHM>")
            .append(id)
            .append("</GMSFHM><XM>")
            .append(name)
            .append("</XM></ROW></ROWS>");
        String string = service.nciicCheck(license, condition.toString());
        if (new String(string.getBytes("utf-8"), "utf-8").equals(string))
        {
            serviceResource.log("实名认证返回的字符编码为：utf-8");
        }
        else if (new String(string.getBytes("GBK"), "GBK").equals(string))
        {
            serviceResource.log("实名认证返回的字符编码为：GBK");
        }
        serviceResource.log("实名认证日志:" + string);
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        final Result result = new Result();
        parser.parse(new ByteArrayInputStream(string.getBytes("UTF-8")), new DefaultHandler()
        {
            StringBuilder builder = new StringBuilder();
            
            boolean read = false;
            
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException
            {
                if ("RESPONSE".equalsIgnoreCase(qName))
                {
                    result.errorCode = Integer.parseInt(attributes.getValue("errorcode"));
                    read = false;
                }
                else if ("result_gmsfhm".equalsIgnoreCase(qName))
                {
                    read = true;
                }
                else if ("result_xm".equalsIgnoreCase(qName))
                {
                    read = true;
                }
                else if ("xp".equalsIgnoreCase(qName))
                {
                    read = true;
                }
                else
                {
                    read = false;
                }
            }
            
            @Override
            public void endElement(String uri, String localName, String qName)
                throws SAXException
            {
                if ("result_gmsfhm".equalsIgnoreCase(qName))
                {
                    result.idPassed = "一致".equals(builder.toString());
                }
                else if ("result_xm".equalsIgnoreCase(qName))
                {
                    result.namePassed = "一致".equals(builder.toString());
                }
                else if ("xp".equalsIgnoreCase(qName))
                {
                    String string = builder.toString();
                    if (StringHelper.isEmpty(string))
                    {
                        result.image = null;
                    }
                    else
                    {
                        result.image = new ByteArrayInputStream(Base64.decode(string));
                    }
                }
                read = false;
                builder.setLength(0);
            }
            
            @Override
            public void characters(char[] ch, int start, int length)
                throws SAXException
            {
                if (read)
                {
                    builder.append(ch, start, length);
                }
            }
        });
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
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
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
                result =
                    doCheck(configureProvider.format(GajVariable.URL),
                        configureProvider.format(GajVariable.LICENCE),
                        configureProvider.format(GajVariable.ACCOUNT),
                        id,
                        name);
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
                        connection.prepareStatement("REPLACE INTO S71.T7122 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F06 = ?, F07 = ?"))
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
