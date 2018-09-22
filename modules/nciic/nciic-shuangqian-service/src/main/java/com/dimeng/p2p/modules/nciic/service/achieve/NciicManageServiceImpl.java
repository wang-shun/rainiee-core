package com.dimeng.p2p.modules.nciic.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.AbstractService;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.P2PConst;
import com.dimeng.p2p.modules.nciic.entity.Identity;
import com.dimeng.p2p.modules.nciic.entity.IdentityFailRet;
import com.dimeng.p2p.modules.nciic.entity.IdentityMatching;
import com.dimeng.p2p.modules.nciic.entity.IdentityMatchingRet;
import com.dimeng.p2p.modules.nciic.entity.IdentityRet;
import com.dimeng.p2p.modules.nciic.service.INciicManageService;
import com.dimeng.p2p.modules.nciic.service.variables.NciicShuangQianVariable;
import com.dimeng.p2p.modules.nciic.util.Common;
import com.dimeng.p2p.modules.nciic.util.HttpClientHandler;
import com.dimeng.p2p.modules.nciic.util.MD5;
import com.dimeng.p2p.modules.nciic.util.ResultCode;
import com.dimeng.p2p.modules.nciic.util.RsaHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
        
        try (Connection connection =
            serviceResource.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
                .getConnection())
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
                compStatusResult = doCheck(id, name);
                logger.info("check() compStatusResult: " + compStatusResult);
            }
            catch (Throwable t)
            {
                serviceResource.log(t);
            }
            if (null != compStatusResult)
            {
                try
                {
                    serviceResource.openTransactions(connection);
                    boolean passed = false;
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
                    logger.info("check() end ");
                    serviceResource.commit(connection);// 提交事务
                    return passed;
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
    
    private String doCheck(String id, String name)
        throws Throwable
    {
        
        IdentityMatching entity = new IdentityMatching();
        entity.setIdentificationNo(id);
        entity.setRealName(name);
        
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        String actionUrl = configureProvider.format(NciicShuangQianVariable.SERVICE_VALID_URL);
        String notifyURL = configureProvider.format(NciicShuangQianVariable.ID_VALID_CALLBACK_URL);
        Gson gs = new Gson();
        IdentityMatchingRet imr = null;
        List<Identity> identityList = new ArrayList<Identity>();
        
        Identity identity = new Identity();
        identity.setRealName(entity.getRealName());
        identity.setIdentificationNo(entity.getIdentificationNo());
        identityList.add(identity);
        
        String IdentityJsonList = Common.JSONEncode(identityList);
        
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("PlatformMoneymoremore", configureProvider.format(NciicShuangQianVariable.SQ_PLATFORMMONEYMOREMORE));
        params.put("IdentityJsonList", IdentityJsonList);
        String RandomTimeStamp = "";
        String antistateStr = configureProvider.format(NciicShuangQianVariable.SQ_ANTISTATE);
        if ("1".equals(antistateStr))
        {
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            RandomTimeStamp = Common.getRandomNum(2) + sdf.format(d);
        }
        params.put("RandomTimeStamp", RandomTimeStamp);
        params.put("NotifyURL", notifyURL);
        
        String dataStr = paramConcat(params);//拼接签名所需字段
        
        IdentityJsonList =
            Common.UrlEncoder(IdentityJsonList, configureProvider.format(NciicShuangQianVariable.CHARSET));//发送时需要urlEncoder
        params.put("IdentityJsonList", IdentityJsonList);
        
        RsaHelper rsa = RsaHelper.getInstance();
        if ("1".equals(antistateStr))
        {
            dataStr = new MD5().getMD5Info(dataStr);
        }
        String SignInfo = rsa.signData(dataStr, configureProvider.format(NciicShuangQianVariable.SQ_PRIVATEPKCS8_KEY));
        params.put("SignInfo", SignInfo);
        
        logger.info("Http doPost params:" + params.toString());
        
        String result = HttpClientHandler.doPost(params, actionUrl);
        Map<String, String> retsultMap = gs.fromJson(result, new TypeToken<Map<String, String>>()
        {
        }.getType());
        
        logger.info("return callback res:" + result);
        
        String platformMoneymoremore = retsultMap.get("PlatformMoneymoremore");
        String identityJsonList = retsultMap.get("IdentityJsonList");
        String identityFailJsonList = retsultMap.get("IdentityFailJsonList");
        String amount = retsultMap.get("Amount");
        String randomTimeStamp = retsultMap.get("RandomTimeStamp");
        String resultCode = retsultMap.get("ResultCode");
        String signInfoRet = retsultMap.get("SignInfo");
        
        identityJsonList =
            Common.UrlDecoder(identityJsonList, configureProvider.format(NciicShuangQianVariable.CHARSET));//解码
        identityFailJsonList =
            Common.UrlDecoder(identityFailJsonList, configureProvider.format(NciicShuangQianVariable.CHARSET));//解码
        
        Map<String, String> paramsRet = new LinkedHashMap<String, String>();
        paramsRet.put("platformMoneymoremore", platformMoneymoremore);
        paramsRet.put("identityJsonList", identityJsonList);
        paramsRet.put("identityFailJsonList", identityFailJsonList);
        paramsRet.put("amount", amount);
        paramsRet.put("randomTimeStamp", randomTimeStamp);
        paramsRet.put("resultCode", resultCode);
        
        String str = paramConcat(paramsRet);
        if (verifyByRSA(str, signInfoRet) && "88".equals(resultCode))
        {
            List<IdentityRet> irList = gs.fromJson(identityJsonList, new TypeToken<List<IdentityRet>>()
            {
            }.getType());
            imr = new IdentityMatchingRet();
            imr.setRealName(Common.UrlDecoder(irList.get(0).getRealName(),
                configureProvider.format(NciicShuangQianVariable.CHARSET)));
            imr.setIdentificationNo(irList.get(0).getIdentificationNo());
            if (irList.get(0).getState().equals("0"))
            {
                imr.setMatchingResult(false);
                imr.setResultCode(ResultCode.FAIL.getCode());
                imr.setMessage(Common.UrlDecoder("匹配失败", configureProvider.format(NciicShuangQianVariable.CHARSET)));
                return "4";
            }
            else if (irList.get(0).getState().equals("1"))
            {
                imr.setMatchingResult(true);
                imr.setResultCode(ResultCode.SUCCESS.getCode());
                imr.setMessage(Common.UrlDecoder("匹配成功", configureProvider.format(NciicShuangQianVariable.CHARSET)));
                return "3";
            }
            imr.setAmount(BigDecimalParser.parse(amount));
            //String signStr = createMD5SignStr(imr);
            //imr.setSignStr(signStr);
        }
        else
        {
            List<IdentityFailRet> irList = gs.fromJson(identityJsonList, new TypeToken<List<IdentityFailRet>>()
            {
            }.getType());
            imr = new IdentityMatchingRet();
            imr.setRealName(Common.UrlDecoder(irList.get(0).getRealName(),
                configureProvider.format(NciicShuangQianVariable.CHARSET)));
            imr.setIdentificationNo(irList.get(0).getIdentificationNo());
            imr.setMatchingResult(false);
            imr.setResultCode("失败:9999");
            imr.setMessage(retsultMap.get("Message"));
            imr.setAmount(BigDecimalParser.parse(amount));
            //String signStr = createMD5SignStr(imr);
            //imr.setSignStr(signStr);
            return "4";
        }
        return "4";
        //return gs.toJson(imr);
        
    }
    
    protected String paramConcat(Map<String, String> param)
    {
        
        StringBuilder builder = new StringBuilder();
        for (String key : param.keySet())
        {
            builder.append(param.get(key));
        }
        return builder.toString();
    }
    
    /**
     * 第三方返回数据验签
     * <功能详细描述>
     * @param forEncryptionStr
     * @param chkValue
     * @return
     * @throws Exception
     */
    protected boolean verifyByRSA(String forEncryptionStr, String chkValue)
        throws Exception
    {
        
        RsaHelper rsa = RsaHelper.getInstance();
        
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        String antistateStr = configureProvider.format(NciicShuangQianVariable.SQ_PLATFORMMONEYMOREMORE);
        
        if ("1".equals(antistateStr))
        {
            forEncryptionStr = new MD5().getMD5Info(forEncryptionStr);
        }
        try
        {
            
            // 签名
            boolean verifySignature =
                rsa.verifySignature(chkValue,
                    forEncryptionStr,
                    configureProvider.format(NciicShuangQianVariable.SQ_PUBLIC_KEY));
            return verifySignature;
        }
        catch (Exception e)
        {
            logger.error(e);
            // 打印日志
            throw new Exception();
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
