/*package com.dimeng.p2p.app.servlets.pay.service.fuyou.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.AbstractService;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.escrow.fuyou.util.SecurityUtils;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.util.StringHelper;

public class AbstractEscrowService extends AbstractService
{
    
    protected final String charSet = "UTF-8";
    
    protected static ConfigureProvider configureProvider;
    
    protected String url;
    
    public AbstractEscrowService(ServiceResource serviceResource)
    {
        super(serviceResource);
        configureProvider = serviceResource.getResource(ConfigureProvider.class);
    }
    
    protected Connection getConnection()
        throws ResourceNotFoundException, SQLException
    {
        return serviceResource.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
            .getConnection(P2PConst.DB_CONSOLE);
    }
    
    protected Connection getConnection(String db)
        throws ResourceNotFoundException, SQLException
    {
        return serviceResource.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
            .getConnection(db);
    }
    
    *//**
     * 集合排序aA-zZ
     * <空串不参加>
     * @param param 集合参数
     * @return String 返回
     *//*
    protected String getSignature(Map<String, String> param)
    {
        StringBuffer sb = new StringBuffer();
        List<String> keys = new ArrayList<String>(param.keySet());
        Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < keys.size(); i++)
        {
            String key = keys.get(i);
            if ("signature".equals(key))
            {
                continue;
            }
            String value = "";
            if (param.get(key) != null)
            {
                value = param.get(key);
            }
            sb.append((i == 0 ? "" : "|") + value.trim());
        }
        String signSrc = sb.toString();
        //判断字符串signSrc 是不是以字符串|开头.
        if (signSrc.startsWith("|"))
        {
            signSrc = signSrc.replaceFirst("|", "");
        }
        return signSrc;
    }
    
    *//**
     * 集合排序aA-zZ
     * <空串不参加>
     * @param param 集合参数
     * @return String 返回
     *//*
    protected String getSignatureForRet(Map<String, String> param)
    {
        StringBuffer sb = new StringBuffer();
        List<String> keys = new ArrayList<String>();
        keys.add(param.get("amt"));
        keys.add(param.get("login_id"));
        keys.add(param.get("mchnt_amt"));
        keys.add(param.get("mchnt_cd"));
        keys.add(param.get("mchnt_txn_ssn"));
        keys.add(param.get("resp_code"));
        keys.add(param.get("resp_desc"));
        for (int i = 0; i < keys.size(); i++)
        {
            String key = keys.get(i);
            if ("signature".equals(key))
            {
                continue;
            }
            //            String value = "";
            //            if (param.get(key) != null)
            //            {
            //                value = param.get(key);
            //            }
            sb.append((i == 0 ? "" : "|") + key.trim());
        }
        String signSrc = sb.toString();
        //判断字符串signSrc 是不是以字符串|开头.
        if (signSrc.startsWith("|"))
        {
            signSrc = signSrc.replaceFirst("|", "");
        }
        return signSrc;
    }
    
    *//**
     * 获取需要签名的字段拼接的字符串 List
     * 
     * @param params
     *            参数列表
     * @return
     * @throws Exception
     *//*
    protected String forEncryptionStr(List<String> params)
        throws Exception
    {
        if (params == null)
        {
            return null;
        }
        StringBuffer buffer = null;
        //对签名数据进行拼接，按照需求，如果值为空，也是需要拼进去的。
        for (int i = 0; i < params.size(); i++)
        {
            if (buffer == null)
            {
                buffer = new StringBuffer();
            }
            if (i < params.size() - 1)
            {
                buffer.append(StringUtils.trimToEmpty(params.get(i))).append("|");
            }
            else if (i == (params.size() - 1))
            {
                buffer.append(StringUtils.trimToEmpty(params.get(i)));
            }
            
        }
        
        return buffer == null ? null : buffer.toString();
    }
    
    *//**
     * 获取签名字符串 List
     * 
     * @param params
     *            参加签名的参数列表
     * @return
     * @throws Exception
     *//*
    protected String chkValue(List<String> params)
        throws Exception
    {
        //获取需要签名的字段拼接的字符串
        String str = forEncryptionStr(params);
        System.out.println("要转换为签名的参数为：" + str);
        return StringHelper.isEmpty(str) ? null : encryptByRSA(str);
    }
    
    *//**
     * 签名——HSP
     * <信息加密>
     * @param param
     * @return
     * @throws Exception
     *//*
    protected String encryptByRSA(String param)
        throws Exception
    {
        new SecurityUtils(configureProvider.format(FuyouVariable.FUYOU_PRIVATEKEY_PATH),
            configureProvider.format(FuyouVariable.FUYOU_PUBLICKEY_PATH));
        //        new SecurityUtils("E:/fuyou/prkey.key", "E:/fuyou/pbkey.key");
//        new SecurityUtils("/weixin/apache-tomcat-7.0.64/webapps/40082/WEB-INF/certs/fuyou/prkey.key",
//            "/weixin/apache-tomcat-7.0.64/webapps/40082/WEB-INF/certs/fuyou/pbkey.key");
        return SecurityUtils.sign(param);
    }
    
    *//**
     * 验证——HSP
     * <信息验证>
     * @param forEncryptionStr
     * @param chkValue
     * @return
     * @throws Exception
     *//*
    public boolean verifyByRSA(String forEncryptionStr, String chkValue)
        throws Exception
    {
        new SecurityUtils(configureProvider.format(FuyouVariable.FUYOU_PRIVATEKEY_PATH),
            configureProvider.format(FuyouVariable.FUYOU_PUBLICKEY_PATH));
        //        new SecurityUtils("E:/fuyou/prkey.key", "E:/fuyou/pbkey.key");
//        new SecurityUtils("/weixin/apache-tomcat-7.0.64/webapps/40082/WEB-INF/certs/fuyou/prkey.key",
//            "/weixin/apache-tomcat-7.0.64/webapps/40082/WEB-INF/certs/fuyou/pbkey.key");
        return SecurityUtils.verifySign(forEncryptionStr, chkValue);
    }
    
    *//**
     * 根据验签所需字段接收所需数据，并执行验签操作——HSP
     * <功能详细描述>
     * @param paramsIn 接收相应集合字段
     * @throws Throwable
     *//*
    public boolean getReturnParams(Map<String, String> paramsIn)
        throws Throwable
    {
        // 验签字符串
        String signature = getSignature(paramsIn);
        // 返回签名
        String signValue = paramsIn.get("signature");
        // 验证
        if (verifyByRSA(signature, signValue))
        { //验签通过
            logger.info("验签通过");
            return true;
        }
        logger.error("验签失败");
        return false;
    }
    
    *//**
     * 去占位符\空格\换行——HSP
     * @param str
     * @return
     *//*
    public String trimBlank(String str)
    {
        String dest = "";
        if (str != null)
        {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
    
    *//**
     * 金额格式化——HSP
     * <单位分>
     * @param money
     * @return
     *//*
    public String getAmt(BigDecimal money)
    {
        DecimalFormat format = new DecimalFormat("0.00");
        
        String amt = format.format(money);
        if ("0.00".equals(amt))
        {
            return "0";
        }
        return formatAmt(amt);
    }
    
    *//**
     * 金额格式化（单位：分）
     * 
     * @作者：何石平 （20151012）
     * @param amt
     *            金额
     * @return String 格式化后
     *//*
    protected String formatAmt(String amt)
    {
        int i = amt.indexOf(".");
        if (i > 0)
        {
            String z = amt.substring(0, i);
            String f = amt.substring(i + 1);
            if (f.length() > 2)
            {
                f = f.substring(0, 2);
            }
            else if (f.length() == 0)
            {
                f = f + "00";
            }
            else if (f.length() == 1)
            {
                f = f + "0";
            }
            amt = z + f;
        }
        else
        {
            amt = amt + "00";
        }
        return amt;
    }
    
    *//**
     * 金额格式化（单位：元）
     * 
     * @param amt 金额
     * @return String 格式化后
     *//*
    protected BigDecimal formatAmtRet(String amt)
    {
        if (amt == null || amt.equals(""))
        {
            return new BigDecimal(0);
        }
        int i = amt.length();
        if (i > 1)
        {
            String z = amt.substring(0, i - 2);
            String f = amt.substring(i - 2);
            
            if (i == 2)
            {
                amt = "0." + f;
            }
            else
            {
                amt = z + "." + f;
            }
        }
        BigDecimal decimal = new BigDecimal(amt);
        DecimalFormat format = new DecimalFormat("0.00");
        String amtc = format.format(decimal);
        return new BigDecimal(amtc);
    }
    
    *//**
     * 第三方账号查询
     * <功能详细描述>
     * @param userCustId
     * @return
     * @throws Throwable
     *//*
    protected String getAccountId(int userCustId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03 FROM S61.T6119 WHERE T6119.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userCustId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getString(1);
                    }
                }
            }
        }
        return null;
    }
    
    *//**
     * 增加流水号
     * <暂未使用>
     * @param trxId
     * @param orderId
     * @throws Throwable
     *//*
    protected void trxId(String trxId, int orderId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("UPDATE S65.T6501 SET F10 = ? WHERE F01 = ?"))
            {
                ps.setString(1, trxId);
                ps.setInt(2, orderId);
                ps.executeUpdate();
            }
        }
    }
    
    *//**
     * 用户第三方账号
     * <功能详细描述>
     * @param accountId
     * @return
     * @throws Throwable
     *//*
    protected String userCustId(int accountId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT F03 FROM S61.T6119 WHERE F01 = ?"))
            {
                ps.setInt(1, accountId);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getString(1);
                    }
                }
            }
        }
        return null;
    }
    
    *//**
     * 系统时间
     * <功能详细描述>
     * @param connection
     * @return
     * @throws Throwable
     *//*
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
    
    *//** 操作类别
     *  记录前台日志内容
     * @param type
     * @param log
     * @throws Throwable
     *//*
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
    
    *//**
     * 响应
     * 20160222
     * @param param
     * @return
     * @throws Throwable
     *//*
    protected String plainRSA(Map<String, String> param)
        throws Throwable
    {
        StringBuilder builder = new StringBuilder();
        builder.append("<plain>");
        builder.append("<resp_code>" + param.get("resp_code") + "</resp_code>");
        builder.append("<mchnt_cd>" + param.get("mchnt_cd") + "</mchnt_cd>");
        builder.append("<mchnt_txn_ssn>" + param.get("mchnt_txn_ssn") + "</mchnt_txn_ssn>");
        builder.append("</plain>");
        String signature = encryptByRSA(builder.toString());
        return createXml(builder.toString(), signature);
    }
    
    *//**
     * 响应XML构建
     * 20160222
     * @param plain
     * @param signature
     * @return
     * @throws Throwable
     *//*
    private String createXml(String plain, String signature)
        throws Throwable
    {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<ap>");
        builder.append(plain);
        builder.append("<signature>".concat(signature).concat("</signature>"));
        builder.append("</ap>");
        return builder.toString();
    }
    
    public static void main(String[] args)
    {
        new SecurityUtils("E:/fuyou/prkey.key", "E:/fuyou/pbkey.key");
        String forEncryptionStr = "650000|13923837044|2600|0003930F0074981|YHCZ145700025545056232||0000|成功";
        String chkValue =
            "0dFtdMFpDl4lY6UueFfFLZzRJjAuhG2UrHtqsHhXU6aLqdCT+9SRYuC2P7H94FiIcB4n71C+7WVjFBe15xApLLcbPE0NpWLEa1GhQF/wyFRxL1QqTyZheHlIMDriPP/o1nofmmQuSYZsxxqWFFwq52qynMaz3MZMZ16VOSzZrQA=";
        boolean flg = SecurityUtils.verifySign(forEncryptionStr, chkValue);
        System.out.println(flg);
    }
}
*/