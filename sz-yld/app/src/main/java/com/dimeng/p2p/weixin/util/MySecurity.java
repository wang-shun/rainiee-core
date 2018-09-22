package com.dimeng.p2p.weixin.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * 加密算法
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年4月11日]
 */
public abstract class MySecurity
{
    /**
     * 加密字符串
     * 
     * @param strSrc 加密前字符串
     * @return 加密后字符串
     */
    public static String encode(final String strSrc)
    {
        MessageDigest md = null;
        String strDes = null;
        try
        {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行sha1加密
            byte[] digest = md.digest(strSrc.toString().getBytes());
            strDes = byteToStr(digest);
        }
        catch (NoSuchAlgorithmException e)
        {
            return strSrc;
        }
        return strDes;
    }
    
    /**
     * 将字节数组转换为十六进制字符串
     * 
     * @param byteArray
     * @return
     */
    private static String byteToStr(byte[] byteArray)
    {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++)
        {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }
    
    /**
     * 将字节转换为十六进制字符串
     * 
     * @param mByte
     * @return
     */
    private static String byteToHexStr(byte mByte)
    {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        
        String s = new String(tempArr);
        return s;
    }
    
}