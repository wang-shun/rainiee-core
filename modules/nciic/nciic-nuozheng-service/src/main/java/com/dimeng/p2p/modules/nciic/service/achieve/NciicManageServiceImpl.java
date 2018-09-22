package com.dimeng.p2p.modules.nciic.service.achieve;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.AbstractService;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.P2PConst;
import com.dimeng.p2p.modules.nciic.entity.CheckBankCard;
import com.dimeng.p2p.modules.nciic.entity.CheckBankCardRet;
import com.dimeng.p2p.modules.nciic.service.INciicManageService;
import com.dimeng.p2p.modules.nciic.service.variables.NciicNuoZhengVariable;
import com.dimeng.util.parser.BooleanParser;

public class NciicManageServiceImpl extends AbstractService implements INciicManageService
{
    
    ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
    
    String mall_id = configureProvider.getProperty(NciicNuoZhengVariable.MALL_ID);
    
    String appkey = configureProvider.getProperty(NciicNuoZhengVariable.APP_KEY);
    
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
            
            /**
             * 1000=一致
            	1001=不一致
            	1002=库中无此号
            	1003=请稍后再试
            	1110=核验失败，请稍候再试  
            	1101=商家ID不合法
            	1102=身份证姓名不合法
            	1103=身份证号码不合法
            	1104=签名不合法
            	1105=第三方服务器异常
            	1106=账户余额不足
            	1107=tm不合法
            	1108=其他异常
            	1109=账号被暂停
             */
            int result = 0;
            try
            {
                result = doCheck(name, id);
            }
            catch (Throwable t)
            {
                logger.error("实名认证请求失败：", t);
            }
            boolean passed = false;
            if (result == 1000)
            {//1000=一致
                passed = true;
            }
            try
            {
                serviceResource.openTransactions(connection);
                //明确的成功或错误提示才记录实名认证状态
                if (result == 1000 || result == 1001 || result == 1102 || result == 1103)
                {//1000=一致 1001=不一致 1102=身份证姓名不合法	1103=身份证号码不合法
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
                
                serviceResource.commit(connection);
                logger.info("check() end ");
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
    {
        idcard = idcard.toLowerCase();
        String url = configureProvider.getProperty(NciicNuoZhengVariable.URL);
        long tm = System.currentTimeMillis();
        String md5_param = mall_id + realname + idcard + tm + appkey;
        String sign = md5(md5_param);
        String param =
            new StringBuffer().append("?mall_id=" + mall_id)
                .append("&realname=" + realname)
                .append("&idcard=" + idcard)
                .append("&tm=" + tm)
                .append("&sign=" + sign)
                .toString();
        String url_v = url + param;
        
        logger.info("实名认证请求： " + url_v);
        try
        {
            url_v = url_v.replace(realname, URLEncoder.encode(realname, "UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            logger.error("实名认证失败：", e);
        }
        String jsonString = url2string(url_v);
        
        JSONObject result = JSONObject.parseObject(jsonString);
        
        logger.info("实名认证返回： " + result);
        
        int code = Integer.parseInt(result.getJSONObject("data").getString("code"));
        return code;
    }
    
    private String md5(String s)
    {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try
        {
            byte[] btInput = s.getBytes("utf-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++)
            {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        }
        catch (Exception e)
        {
            logger.error("实名认证失败，加密异常：", e);
            return null;
        }
    }
    
    private String url2string(String url)
    {
        StringBuffer sb = new StringBuffer();
        try
        {
            try (InputStream is = new URL(url).openStream())
            {
                byte[] buf = new byte[1024 * 10];
                int len = 0;
                while ((len = is.read(buf, 0, 1024 * 10)) > 0)
                {
                    sb.append(new String(buf, 0, len, "UTF-8"));
                }
            }
        }
        catch (MalformedURLException e)
        {
            logger.error("实名认证失败，解析异常：", e);
        }
        catch (IOException e)
        {
            logger.error("实名认证失败，解析异常：", e);
        }
        return sb.toString();
    }
    
    @Override
    public CheckBankCard checkBankCard(String realname, String cardnum)
        throws Throwable
    {
        CheckBankCard cbc = null;
        boolean isCheckCard =
            BooleanParser.parse(configureProvider.getProperty(NciicNuoZhengVariable.CHECKCARD_ENABLE));
        if (!isCheckCard)
        {
            logger.info("尚未启用银行卡认证");
            cbc = new CheckBankCard();
            cbc.setSuccess(true);
            cbc.setMessage("认证成功！");
            return cbc;
        }
        String actionUrl = configureProvider.getProperty(NciicNuoZhengVariable.BANKCARD_URL);
        long tm = System.currentTimeMillis();
        String md5_param = mall_id + realname + cardnum + tm + appkey;
        String sign = md5(md5_param);
        String param =
            new StringBuffer().append("?mall_id=" + mall_id)
                .append("&realname=" + URLEncoder.encode(realname, "UTF-8"))
                .append("&cardnum=" + cardnum)
                .append("&tm=" + tm)
                .append("&sign=" + sign)
                .toString();
        String url_v = actionUrl + param;
        
        logger.info("诺证银行卡认证请求： " + url_v);
        String jsonString = url2string(url_v);
        logger.info("诺证银行卡认证返回： " + jsonString);
        CheckBankCardRet cbcr = JSON.parseObject(jsonString, CheckBankCardRet.class);
        if (cbcr != null)
        {
            if ("2001".equals(cbcr.getStatus()) && "1000".equals(cbcr.getData().getCode()))
            {//当且仅当status=2001且code=1000时才认为是验证成功
                cbc = new CheckBankCard();
                cbc.setSuccess(true);
                cbc.setMessage("银行卡验证成功");
            }
            else
            {
                cbc = new CheckBankCard();
                cbc.setSuccess(false);
                cbc.setMessage("提示信息：" + cbcr.getData().getMessage());
            }
        }
        else
        {
            cbc = new CheckBankCard();
            cbc.setSuccess(false);
            cbc.setMessage("银行卡验证失败！");
        }
        return cbc;
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
