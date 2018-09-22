package com.dimeng.p2p.app.tool;

import java.security.MessageDigest;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * 采用MD5加密解密  
 * @author tanhui
 *
 */
public abstract class MD5Util
{
    private static Logger logger = Logger.getLogger(MD5Util.class);
    
    /*** 
     * MD5加码 生成32位md5码 
     */
    public static String string2MD5(String inStr)
    {
        MessageDigest md5 = null;
        try
        {
            md5 = MessageDigest.getInstance("MD5");
        }
        catch (Exception e)
        {
            logger.error(e);
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        
        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte)charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++)
        {
            int val = (md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
        
    }
    
    /** 
     * 加密解密算法 执行一次加密，两次解密 
     */
    public static String convertSec(String inStr)
    {
        
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++)
        {
            a[i] = (char)(a[i] ^ 'z');
        }
        String s = new String(a);
        return s;
        
    }
    
    /**
     * 随机生成5位随机数  
     * 
     * @return
     */
    private static String get5Radom()
    {
        String newString = null;
        
        //得到0.0到1.0之间的数字,并扩大100000倍  
        double doubleP = Math.random() * 100000;
        
        //如果数据等于100000,则减少1  
        if (doubleP >= 100000)
        {
            doubleP = 99999;
        }
        
        //然后把这个数字转化为不包含小数点的整数  
        int tempString = (int)Math.ceil(doubleP);
        
        //转化为字符串  
        newString = "" + tempString;
        
        //把得到的数增加为固定长度,为5位  
        while (newString.length() < 5)
        {
            newString = "0" + newString;
        }
        
        return newString;
    }
    
    /**
     * 获取随机5位数MD5加密
     * 
     * @return
     */
    public static String getState()
    {
        return string2MD5(get5Radom().concat(String.valueOf(System.currentTimeMillis())));
    }
    
    // 测试主函数  
    public static void main(String args[])
    {
        String s = new String("root|123456|" + new Date().getTime());
        System.out.println("原始：" + s);
        System.out.println("MD5后：" + string2MD5(s));
        System.out.println("加密的：" + convertSec(s));
        System.out.println("解密的：" + convertSec(convertSec(s)));
        
    }
}