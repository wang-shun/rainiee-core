package com.dimeng.p2p.modules.nciic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.dimeng.p2p.modules.nciic.entity.Identity;
import com.dimeng.p2p.modules.nciic.entity.IdentityFailRet;
import com.dimeng.p2p.modules.nciic.entity.IdentityMatching;
import com.dimeng.p2p.modules.nciic.entity.IdentityMatchingRet;
import com.dimeng.p2p.modules.nciic.entity.IdentityRet;
import com.dimeng.p2p.modules.nciic.util.Common;
import com.dimeng.p2p.modules.nciic.util.HttpClientHandler;
import com.dimeng.p2p.modules.nciic.util.MD5;
import com.dimeng.p2p.modules.nciic.util.ResultCode;
import com.dimeng.p2p.modules.nciic.util.RsaHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class MyClient
{
    
    private final String charSet = "utf-8";
    
    /**
     * 乾多多公钥
     */
    protected String publickey =
        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYfEpdX5mTc+57J/51Asv9SDk/OF8/tRRSSAYXQg5OZZhBa0/WciVliAG07garWzIgWqCGK3+7jpnDwwM3uaAYFxReNN5Nsy4wEzOBZPcmc5avPZLmIJSeTlkecPpuBg3Sy0MI/HH5tlTrcZ203fyWScR0HMPMq5xpuCJ4Qr266QIDAQAB";
    
    /**
     * 乾多多秘钥
     */
    protected String privatekey =
        "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJh8Sl1fmZNz7nsn/nUCy/1IOT84Xz+1FFJIBhdCDk5lmEFrT9ZyJWWIAbTuBqtbMiBaoIYrf7uOmcPDAze5oBgXFF403k2zLjATM4Fk9yZzlq89kuYglJ5OWR5w+m4GDdLLQwj8cfm2VOtxnbTd/JZJxHQcw8yrnGm4InhCvbrpAgMBAAECgYBt3phigQCSKyU5Xc7Nlq9Ol1yQPdj7eUjkJHsnBPRz7mXvNRg4htSFPKMmL59klngesc4Z/nuxs4T9daT64OgFdUKIW8f+0kun5HZJ+fkBTSAGDMq+gqWQfweJ2Bys27qxDHCTyUGydRyAj9oQDAk0z7Jrtup4EePMIQwp/jb9VQJBANuiPsOtfOhIsOwK5vxI0BTdHrfMs5heY7BNKTymML8D7AbcLgkt53mEgIHQu8QBBSC7qS3s65+Rl9EpuoP4mdsCQQCxu8osiePWCOFvdNBLng+kj30e47socaErULChz0rmKYUO3vrGN7IoFrIuhwArnIbZduRaCLkGYhTS5Rl7j+OLAkB5ODvh7f+xiGU1cfL4rQtDaKNKmE1LPFVS+dNXqPXghz6erqkt4csO84WloFnxnQqCfXCra0bEpCuhgqFxsyTfAkBi2G24F3f+sTGvKugtJdrNSn/rjfuooolf7aBOXVrqZmz5uEj/tDoA0Z6HAc22c3cLunOFHxTH2AR8xa1Gat/BAkEAiCBTYHmw0zICBLrwloAlV4S2XRr9YIvAGlZrO2Zf+/SD8bQyU/Ad99qQzPVas29qVkXrgtIWNk8YZJNZ2Zn0fg==";
    
    public static void main(String[] args)
    {
        try
        {
            MyClient client = new MyClient();
            client.ms();
        }
        catch (Exception e)
        {
        }
    }
    
    public String ms()
        throws JsonSyntaxException, Exception
    {
        IdentityMatching entity = new IdentityMatching();
        entity.setIdentificationNo("440103198909018361");
        entity.setRealName("李高格");
        
        String actionUrl = "http://218.4.234.150:88/main/authentication/identityMatching.action";
        String notifyURL = "http://61.145.159.156:8241/user/ret/nciicCallBack.htm";
        Gson gs = new Gson();
        IdentityMatchingRet imr = null;
        List<Identity> identityList = new ArrayList<Identity>();
        
        Identity identity = new Identity();
        identity.setRealName(entity.getRealName());
        identity.setIdentificationNo(entity.getIdentificationNo());
        identityList.add(identity);
        
        String IdentityJsonList = Common.JSONEncode(identityList);
        
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("PlatformMoneymoremore", "p185");
        params.put("IdentityJsonList", IdentityJsonList);
        String RandomTimeStamp = "";
        String antistateStr = "0";
        if ("1".equals(antistateStr))
        {
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            RandomTimeStamp = Common.getRandomNum(2) + sdf.format(d);
        }
        params.put("RandomTimeStamp", RandomTimeStamp);
        params.put("NotifyURL", notifyURL);
        
        String dataStr = paramConcat(params);//拼接签名所需字段
        
        IdentityJsonList = Common.UrlEncoder(IdentityJsonList, charSet);//发送时需要urlEncoder
        params.put("IdentityJsonList", IdentityJsonList);
        
        RsaHelper rsa = RsaHelper.getInstance();
        if ("1".equals(antistateStr))
        {
            dataStr = new MD5().getMD5Info(dataStr);
        }
        String SignInfo = rsa.signData(dataStr, privatekey);
        params.put("SignInfo", SignInfo);
        
        String result = HttpClientHandler.doPost(params, actionUrl);
        Map<String, String> retsultMap = gs.fromJson(result, new TypeToken<Map<String, String>>()
        {
        }.getType());
        
        System.out.println("result:" + result);
        System.out.println("resultMap:" + retsultMap.toString());
        
        String platformMoneymoremore = retsultMap.get("PlatformMoneymoremore");
        String identityJsonList = retsultMap.get("IdentityJsonList");
        String identityFailJsonList = retsultMap.get("IdentityFailJsonList");
        String amount = retsultMap.get("Amount");
        String randomTimeStamp = retsultMap.get("RandomTimeStamp");
        String resultCode = retsultMap.get("ResultCode");
        String signInfoRet = retsultMap.get("SignInfo");
        
        identityJsonList = Common.UrlDecoder(identityJsonList, charSet);//解码
        identityFailJsonList = Common.UrlDecoder(identityFailJsonList, charSet);//解码
        
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
            imr.setRealName(Common.UrlDecoder(irList.get(0).getRealName(), charSet));
            imr.setIdentificationNo(irList.get(0).getIdentificationNo());
            if (irList.get(0).getState().equals("0"))
            {
                imr.setMatchingResult(false);
                imr.setResultCode(ResultCode.FAIL.getCode());
                imr.setMessage(Common.UrlDecoder("匹配失败", charSet));
                return "false";
            }
            else if (irList.get(0).getState().equals("1"))
            {
                imr.setMatchingResult(true);
                imr.setResultCode(ResultCode.SUCCESS.getCode());
                imr.setMessage(Common.UrlDecoder("匹配成功", charSet));
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
            imr.setRealName(Common.UrlDecoder(irList.get(0).getRealName(), charSet));
            imr.setIdentificationNo(irList.get(0).getIdentificationNo());
            imr.setMatchingResult(false);
            imr.setResultCode("失败:9999");
            imr.setMessage(retsultMap.get("Message"));
            imr.setAmount(BigDecimalParser.parse(amount));
            //String signStr = createMD5SignStr(imr);
            //imr.setSignStr(signStr);
            return "false";
        }
        return "false";
        //return gs.toJson(imr);
    }
    
    protected static String paramConcat(Map<String, String> param)
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
    protected static boolean verifyByRSA(String forEncryptionStr, String chkValue)
        throws Exception
    {
        
        RsaHelper rsa = RsaHelper.getInstance();
        
        String antistateStr = "0";
        
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
                    "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYfEpdX5mTc+57J/51Asv9SDk/OF8/tRRSSAYXQg5OZZhBa0/WciVliAG07garWzIgWqCGK3+7jpnDwwM3uaAYFxReNN5Nsy4wEzOBZPcmc5avPZLmIJSeTlkecPpuBg3Sy0MI/HH5tlTrcZ203fyWScR0HMPMq5xpuCJ4Qr266QIDAQAB");
            return verifySignature;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            // 打印日志
            throw new Exception();
        }
    }
}
