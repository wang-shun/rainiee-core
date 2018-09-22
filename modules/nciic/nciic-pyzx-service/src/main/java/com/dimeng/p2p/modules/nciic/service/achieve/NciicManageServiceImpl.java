package com.dimeng.p2p.modules.nciic.service.achieve;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.xfire.client.Client;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.AbstractService;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.P2PConst;
import com.dimeng.p2p.S61.entities.T6141;
import com.dimeng.p2p.S61.enums.T6141_F03;
import com.dimeng.p2p.S61.enums.T6141_F04;
import com.dimeng.p2p.modules.nciic.entity.IdentifierAttr;
import com.dimeng.p2p.modules.nciic.entity.PoliceCheckInfo;
import com.dimeng.p2p.modules.nciic.service.INciicManageService;
import com.dimeng.p2p.modules.nciic.service.variables.PyzxVariable;
import com.dimeng.p2p.modules.nciic.util.Base64;
import com.dimeng.p2p.modules.nciic.util.Bean2XmlUtils;
import com.dimeng.p2p.modules.nciic.util.CompressStringUtil;
import com.dimeng.p2p.modules.nciic.util.DocumentUtil;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.TimestampParser;

public class NciicManageServiceImpl extends AbstractService implements INciicManageService
{
    
    private static Log LOG = LogFactory.getLog(NciicManageServiceImpl.class);
    
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
    
    protected Connection getConnection()
        throws ResourceNotFoundException, SQLException
    {
        return serviceResource.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
            .getConnection();
    }
    
    private PoliceCheckInfo doCheck(String id, String name)
        throws Throwable
    {
        
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        
        boolean isCertificate = Boolean.parseBoolean(configureProvider.getProperty(PyzxVariable.IS_CERTIFICATE));
        if (isCertificate)
        {
            LOG.info("ID:" + id + "\t Name:" + name);
            LOG.info("CertificatePath:" + configureProvider.getProperty(PyzxVariable.CERTIFICATE_PATH) + "##");
            LOG.info("CertificatePassword:" + configureProvider.getProperty(PyzxVariable.CERTIFICATE_PASSWORD) + "##");
            LOG.info("WEBSERVICE:" + configureProvider.getProperty(PyzxVariable.WEBSERVICE) + "##");
            
            System.setProperty("javax.net.ssl.keyStore", configureProvider.getProperty(PyzxVariable.CERTIFICATE_PATH));
            System.setProperty("javax.net.ssl.keyStorePassword",
                configureProvider.getProperty(PyzxVariable.CERTIFICATE_PASSWORD));
        }
        
        String queryInfo =
            String.format("<?xml version=\"1.0\" encoding=\"GBK\"?><conditions><condition queryType=\"25160\">"
                + "<item><name>name</name><value>%s</value></item><item><name>documentNo</name><value>%s</value></item>"
                + "<item><name>subreportIDs</name><value>10602</value></item><item><name>refID</name><value>1</value></item>"
                + "</condition></conditions>",
                name,
                id);
        
        System.out.println(configureProvider.getProperty(PyzxVariable.WEBSERVICE) + "\n"
            + configureProvider.getProperty(PyzxVariable.ID5DATAACCOUNT) + "\n"
            + configureProvider.getProperty(PyzxVariable.ID5DATAPASSWORD));
        
        Client client = new Client(new URL(configureProvider.getProperty(PyzxVariable.WEBSERVICE)));
        Object[] results =
            client.invoke("queryReport", new Object[] {configureProvider.getProperty(PyzxVariable.ID5DATAACCOUNT),
                configureProvider.getProperty(PyzxVariable.ID5DATAPASSWORD), queryInfo, "xml"});
        
        if (results[0] instanceof String)
        {
            LOG.info("resut:" + results[0].toString());
            String xmls = results[0].toString();
            String ba64 = DocumentUtil.readStringNode(xmls, "returnValue");
            System.out.println("获取密文：" + ba64);
            Base64 base64 = new Base64();
            byte[] re = base64.decode(ba64);
            String xml = new CompressStringUtil().decompress(re);
            System.out.println("解析后的XML：" + xml);
            String str = DocumentUtil.readStringNodeAll(xml, "policeCheckInfo");
            PoliceCheckInfo result = Bean2XmlUtils.xml2bean(str, PoliceCheckInfo.class);
            
            return result;
            
        }
        else if (results[0] instanceof org.w3c.dom.Document)
        {
            org.w3c.dom.Document doc = (org.w3c.dom.Document)results[0];
            Element element = doc.getDocumentElement();
            NodeList children = element.getChildNodes();
            Node node = children.item(0);
            LOG.info("result content:" + node.getNodeValue());
            
            String ba64 = DocumentUtil.readStringNode(node.getNodeValue(), "returnValue");
            LOG.info("获取密文：" + ba64);
            Base64 base64 = new Base64();
            byte[] re = base64.decode(ba64);
            String xml = new CompressStringUtil().decompress(re);
            LOG.info("解析后的XML：" + xml);
            String str = DocumentUtil.readStringNodeAll(xml, "policeCheckInfo");
            PoliceCheckInfo result = Bean2XmlUtils.xml2bean(str, PoliceCheckInfo.class);
            return result;
            
        }
        
        return null;
        
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
            
            PoliceCheckInfo result = null;
            try
            {
                result = doCheck(id, name);
            }
            catch (Throwable t)
            {
                serviceResource.log(t);
            }
            if (result != null)
            {
                try
                {
                    serviceResource.openTransactions(connection);
                    //boolean passed = result.isPassed();
                    IdentifierAttr idAttr = result.item.get(0);
                    String passed = result.item.get(0).getResult();
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("INSERT INTO S71.T7122 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F06 = ?, F07 = ?"))
                    {
                        pstmt.setString(1, id);
                        pstmt.setString(2, name);
                        pstmt.setString(3, "1".equals(passed) ? "TG" : "SB");
                        if (idAttr.getPhoto() == null)
                        {
                            pstmt.setNull(4, Types.BLOB);
                        }
                        else
                        {
                            //pstmt.setBlob(4, idAttr.getPhoto());
                            pstmt.setNull(4, Types.BLOB);
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
                        pstmt.setString(3, "1".equals(passed) ? "TG" : "SB");
                        pstmt.setInt(4, (idAttr.getResult() != null) ? Integer.parseInt(idAttr.getResult()) : 2);
                        pstmt.setString(5, terminal);
                        pstmt.execute();
                    }
                    
                    int userId = serviceResource.getSession().getAccountId();
                    if ("1".equals(passed))
                    {
                        updateT6198F06(connection, userId, terminal);
                    }
                    else
                    {
                        updateT6198F03(connection, userId, terminal);
                    }
                    
                    logger.info("check() end ");
                    serviceResource.commit(connection);
                    return "1".equals(passed) ? true : false;
                }
                catch (Exception e)
                {
                    serviceResource.rollback(connection);
                    logger.error(e.getMessage());
                    throw e;
                }
            }
            return false;
        }
    }
    
    @Override
    public void updateName(String name, String idcard, String status)
        throws Throwable
    {
        int acount = serviceResource.getSession().getAccountId();
        if (StringHelper.isEmpty(name) || acount <= 0)
        {
            throw new ParameterException("参数错误");
        }
        
        Timestamp date = getBirthday(idcard);
        
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S61.T6141 SET F02 = ?, F04 = ?, F06 = ?, F07 = ?, F08 = ? WHERE F01 = ?"))
            {
                pstmt.setString(1, name);
                pstmt.setString(2, T6141_F04.TG.name());
                pstmt.setString(3, idcard.substring(0, 2) + "***************");
                pstmt.setString(4, StringHelper.encode(idcard));
                pstmt.setTimestamp(5, date);
                pstmt.setInt(6, acount);
                pstmt.execute();
            }
        }
    }
    
    /**
    * 根据身份证得到出生年月
    * @param cardID
    * @return
    */
    protected static Timestamp getBirthday(String cardID)
    {
        StringBuffer tempStr = new StringBuffer("");
        if (cardID != null && cardID.trim().length() > 0)
        {
            if (cardID.trim().length() == 15)
            {
                tempStr.append(cardID.substring(6, 12));
                tempStr.insert(4, '-');
                tempStr.insert(2, '-');
                tempStr.insert(0, "19");
            }
            else if (cardID.trim().length() == 18)
            {
                tempStr = new StringBuffer(cardID.substring(6, 14));
                tempStr.insert(6, '-');
                tempStr.insert(4, '-');
            }
        }
        return TimestampParser.parse(tempStr.toString());
    }
    
    @Override
    public T6141 selectT6141(String idcard)
        throws Throwable
    {
        T6141 t6141 = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S61.T6141 WHERE F07 = ?"))
            {
                pstmt.setString(1, StringHelper.encode(idcard));
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        t6141 = new T6141();
                        t6141.F01 = resultSet.getInt(1);
                        t6141.F02 = resultSet.getString(2);
                        t6141.F03 = T6141_F03.parse(resultSet.getString(3));
                        t6141.F04 = T6141_F04.parse(resultSet.getString(4));
                        t6141.F05 = resultSet.getString(5);
                        t6141.F06 = resultSet.getString(6);
                        t6141.F07 = resultSet.getString(7);
                        t6141.F08 = resultSet.getDate(8);
                    }
                }
            }
        }
        
        return t6141;
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
