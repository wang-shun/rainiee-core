package com.dimeng.p2p.modules.nciic.achieve;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.AbstractService;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.P2PConst;
import com.dimeng.p2p.modules.nciic.entity.QrReqObject;
import com.dimeng.p2p.modules.nciic.entity.QueryEntity;
import com.dimeng.p2p.modules.nciic.entity.QueryListEntity;
import com.dimeng.p2p.modules.nciic.entity.QueryResult;
import com.dimeng.p2p.modules.nciic.entity.QueryResultList;
import com.dimeng.p2p.modules.nciic.entity.ReqObjectEntity;
import com.dimeng.p2p.modules.nciic.entity.ReqQueryResult;
import com.dimeng.p2p.modules.nciic.entity.RespObject;
import com.dimeng.p2p.modules.nciic.service.NciicManage;
import com.dimeng.p2p.modules.nciic.service.variables.XingyezxVariable;
import com.dimeng.p2p.modules.nciic.util.Bean2XmlUtils;
import com.dimeng.p2p.variables.defines.nciic.NciicVariable;
import com.dimeng.util.parser.BooleanParser;

/*
 * 文 件 名:  NciicManageImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  YINKE
 * 修改时间:  2015年10月20日
 */
public class NciicManageImpl extends AbstractService implements NciicManage
{
    private static String method = "POST";
    
    private static Logger log = Logger.getLogger(NciicManageImpl.class);
    
    public NciicManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public boolean check(String id, String name, String terminal)
        throws Throwable
    {
        return check(id, name, false, terminal);
    }
    
    @Override
    public boolean check(String id, String name, boolean duplicatedName, String terminal)
        throws Throwable
    {
        logger.info("check() start ");
        
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        boolean enable = BooleanParser.parse(configureProvider.getProperty(NciicVariable.ENABLE));
        if (!enable)
        {
            return true;
        }
        String channelNo = configureProvider.getProperty(XingyezxVariable.CHANNELNO);
        String querier = configureProvider.getProperty(XingyezxVariable.QUERIER);
        String orgName = configureProvider.getProperty(XingyezxVariable.ORGNAME);
        String actionUrl = configureProvider.getProperty(XingyezxVariable.QUERYURL);
        ReqObjectEntity r = new ReqObjectEntity();
        r.setChannelNo(channelNo);
        r.setQuerier(querier);
        r.setQueryReason("3");
        r.setOrgName(orgName);
        r.setGetBackWay("1");
        r.setReserve1("");
        r.setReserve2("");
        r.setReserve3("");
        r.setReserve4("");
        
        QueryListEntity ql = new QueryListEntity();
        List<QueryEntity> list = new ArrayList<>();
        QueryEntity q = new QueryEntity();
        q.setName(name);
        q.setCertType("0");
        q.setCertNo(id);
        q.setDataSourceNo("1");
        q.setInterfaceNo("101");
        q.setVersion("1");
        q.setTelephoneNo("");
        q.setContactPersonTels("");
        q.setIsRealQuery("1");
        q.setReserve5("");
        q.setReserve6("");
        list.add(q);
        ql.setQuery(list);
        r.setQueryList(ql);
        String xmlStr = Bean2XmlUtils.bean2xml(r, r.getClass(), false);
        log.info("征信查询接口发送参数：" + xmlStr);
        String result = sendParamByPOST(actionUrl, xmlStr);
        log.info("征信查询接口返回参数：" + result);
        RespObject resp = Bean2XmlUtils.xml2bean(result, RespObject.class);
        int userId = serviceResource.getSession().getAccountId();//获取当前登录用户的ID
        boolean authenticationResult = false;
        boolean doWhileTag = false;
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                
                if ("0".equals(resp.getResult()))
                {
                    while (!doWhileTag)
                    {
                        String queryActionUrl = configureProvider.getProperty(XingyezxVariable.QUERYRESULTURL);
                        String queryResult = doResultQuery(resp.getMessage(), queryActionUrl);
                        ReqQueryResult rqr = Bean2XmlUtils.xml2bean(queryResult, ReqQueryResult.class);
                        
                        if (rqr != null)
                        {
                            QueryResultList qrl = rqr.getQueryResultList();
                            if (qrl != null)
                            {
                                List<QueryResult> qrs = qrl.getQueryResult();
                                for (QueryResult qr : qrs)
                                {
                                    if ("4".equals(qr.getRespCode()))
                                    {
                                        authenticationResult = false;
                                        doWhileTag = true;
                                        //更新错误认证次数
                                        updateT6198F03(connection, userId, terminal);
                                    }
                                    else if ("1".equals(qr.getRespCode()) || "3".equals(qr.getRespCode()))
                                    {// 1-待查询
                                     // 3-查询中时持续查询
                                    }
                                    else if ("0".equals(qr.getRespCode()))
                                    {
                                        authenticationResult = true;
                                        doWhileTag = true;
                                    }
                                }
                            }
                            else
                            {
                                authenticationResult = false;
                                doWhileTag = true;
                                //更新错误认证次数
                                updateT6198F03(connection, userId, terminal);
                            }
                            
                        }
                        else
                        {
                            authenticationResult = false;
                            doWhileTag = true;
                            //更新错误认证次数
                            updateT6198F03(connection, userId, terminal);
                        }
                    }
                }
                else
                {
                    authenticationResult = false;
                    //更新错误认证次数
                    updateT6198F03(connection, userId, terminal);
                }
                
                //更新认证通过时间
                updateT6198F06(connection, userId, terminal);
                
                logger.info("check() end ");
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                logger.error(e.getMessage());
                throw e;
            }
        }
        return authenticationResult;
    }
    
    private String doResultQuery(String queryId, String actionUrl)
        throws Throwable
    {
        QrReqObject qr = new QrReqObject();
        qr.setQueryId(queryId);
        String queryXml = Bean2XmlUtils.bean2xml(qr, qr.getClass(), false);
        logger.info("认证结果主动查询发送参数：" + queryXml);
        String result = sendParamByPOST(actionUrl, queryXml);
        logger.info("认证结果主动查询返回结果：" + result);
        return result;
    }
    
    /**
     * 发送至BOSS请求
     * 以POST方式，参数默认GBK编码发送http请求
     * @param xmlparam 发在请求体发送
     * @param actionUrl 请求地址
     * **/
    private String sendParamByPOST(String actionUrl, String xmlparam)
    {
        String sb = String.valueOf(xmlparam);
        StringBuffer outSB = new StringBuffer();
        try
        {
            byte[] b = connect(actionUrl, method, sb.toString().getBytes("utf-8"));
            outSB.append(new String(b, "utf-8"));
        }
        catch (Exception e)
        {
            log.error("请求boss异常：", e);
            outSB.append("\n[5=请求boss异常：]" + String.valueOf(e.getMessage()));
        }
        return outSB.toString();
    }
    
    private byte[] connect(String actionUrl, String method, byte[] input)
    {
        HttpURLConnection conn = null;
        byte[] destStr = null;
        try
        {
            log.info("请求URL：" + actionUrl);
            log.info("请求方式：" + method);
            log.info("请求数据(编码utf-8显示)：" + new String(input, "utf-8"));
            log.info("请求数据(编码GBK显示)：" + new String(input, "GBK"));
            // 生成URL对象
            URL url = new URL(actionUrl);
            // 根据url.opernConnection()得到HttpURLConnection对象,并可以进行按照Http协议通信
            conn = (HttpURLConnection)url.openConnection();
            // 设置请求的方式为GET
            conn.setRequestMethod(method);
            // 设置连接超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", "100000");
            // 设置读取超时时间
            System.setProperty("sun.net.client.defaultReadTimeout", "100000");
            // URL 连接可用于输入和/或输出。将 doInput 标志设置为 true，指示应用程序要从 URL 连接读取数据。
            conn.setDoInput(true);
            // 根据通信输出流con.getOutputStream() 得到文本输出流打印对象的格式化表示形式
            if (input != null)
            {
                // URL 连接可用于输入和/或输出。将 doOutput 标志设置为 true，指示应用程序要将数据写入 URL 连接。
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                try
                {
                    os.write(input);
                }
                finally
                {
                    os.flush();
                    os.close();
                }
            }
            
            // 输入
            InputStream in = conn.getInputStream();
            try
            {
                destStr = null;
                int i = 0;
                int num = 0;
                byte[] tempb = new byte[5000000];
                while ((i = in.read()) != -1)
                {
                    tempb[num] = (byte)i;
                    num++;
                }
                destStr = new byte[num];
                System.arraycopy(tempb, 0, destStr, 0, destStr.length);
            }
            finally
            {
                in.close();
            }
        }
        catch (Exception e)
        {
            log.info("URLConnector异常：", e);
            try
            {
                destStr = e.getMessage().getBytes("UTF-8");
            }
            catch (UnsupportedEncodingException e1)
            {
                e1.printStackTrace();
            }
        }
        finally
        {
            if (conn != null)
            {
                conn.disconnect();
            }
        }
        return destStr;
    }
    
    public static void main(String[] args)
        throws Throwable
    {
        /*ReqObjectEntity r = new ReqObjectEntity();
        r.setChannelNo("11");
        r.setQuerier("querier");
        r.setQueryReason("queryReason");
        r.setOrgName("orgName");
        r.setGetBackWay("getBackWay");
        r.setReserve1("reserve1");
        r.setReserve2("reserve2");
        r.setReserve3("reserve3");
        r.setReserve4("reserve4");
        
        QueryListEntity ql = new QueryListEntity();
        List<QueryEntity> list = new ArrayList<>();
        QueryEntity q = null;
        for (int i = 0; i < 1; i++)
        {
            q = new QueryEntity();
            q.setName("name" + i);
            q.setCertType("certType" + i);
            q.setCertNo("certNo" + i);
            q.setDataSourceNo("dataSourceNo" + i);
            q.setInterfaceNo("interfaceNo" + i);
            q.setVersion("version" + i);
            q.setTelephoneNo("telephoneNo" + i);
            q.setContactPersonTels("contactPersonTels" + i);
            q.setIsRealQuery("isRealQuery" + i);
            q.setReserve5("reserve5" + i);
            q.setReserve6("reserve6" + i);
            list.add(q);
        }
        ql.setQuery(list);
        r.setQueryList(ql);
        String xmlStr = Bean2XmlUtils.bean2xml(r, r.getClass(), false);
        log.info(xmlStr);
        
        String queryResult =
            "<reqQueryResult><queryId>100120151021090804219</queryId><applyId></applyId><respTime>2015-10-21 09:08:23</respTime><queryResultList><queryResult><name>王步重</name><certType>0</certType><certNo>110101195505052018</certNo><telephoneNo></telephoneNo><dataSourceNo>1</dataSourceNo><interfaceNo>101</interfaceNo><version>1</version><taskId>100120151021090804219050</taskId><respCode>4</respCode><respMessage>预付费用不足</respMessage></queryResult><queryResult><name>王步重</name><certType>0</certType><certNo>110101195505052018</certNo><telephoneNo></telephoneNo><dataSourceNo>1</dataSourceNo><interfaceNo>101</interfaceNo><version>1</version><taskId>100120151021090804219050</taskId><respCode>4</respCode><respMessage>预付费用不足</respMessage></queryResult></queryResultList></reqQueryResult>";
        ReqQueryResult rqr = Bean2XmlUtils.xml2bean(queryResult, ReqQueryResult.class);
        System.out.println(rqr.getQueryResultList().getQueryResult().get(0).getRespCode());*/
    }
    
    protected Connection getConnection()
        throws ResourceNotFoundException, SQLException
    {
        return serviceResource.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
            .getConnection();
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
