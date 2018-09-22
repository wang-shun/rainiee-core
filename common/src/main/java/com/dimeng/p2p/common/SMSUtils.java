package com.dimeng.p2p.common;

import java.util.HashMap;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.p2p.variables.defines.smses.SmsVaribles;

public class SMSUtils
{
    private static Map<String, Integer> tempMap;
    private static String regex = "^(13|14|15|17|18)[0-9]{9}$";
    
    /**
     * 初始化短信模板，并保存到map中
     */
    public SMSUtils(ConfigureProvider configureProvider)
    {
        String tempStr = configureProvider.getProperty(SmsVaribles.SMS_YTX_TEMPLE);
        String[] temps = tempStr.split("&");
        tempMap = new HashMap<>();
        for (String temp : temps)
        {
            String[] tem = temp.split("=");
            if(tem.length<2){continue;}
            tempMap.put(tem[0], Integer.valueOf(tem[1]));
        }
    }
    
    /**
     * 根据模板名称从map中获取模板id
     * @param tempName
     * @return 模板id
     */
    public Integer getTempleId(String tempName)
    {
        if (tempMap.containsKey(tempName))
        {
            return tempMap.get(tempName);
        }
        return -1;
    }
    
    /**
     * 根据传入的参数用“,”拼接返回字符串
     * @param params
     * @return 用“,”拼接返回字符串
     */
    public String getSendContent(String... params)
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < params.length; i++)
        {
            sb.append(params[i]);
            if (i < params.length - 1)
            {
                sb.append(",");
            }
        }
        return sb.toString();
    }
    
    /**
     * 手机号段验证
     * @param phoneNumber
     * @return
     */
    public static boolean isPhoneNumber(String phoneNumber) {
		return phoneNumber.matches(regex);
    }
    
}
