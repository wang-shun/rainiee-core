package com.dimeng.p2p.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

public class MD5Utils
{
	// 全局数组
    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public MD5Utils() {
    }

    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        // System.out.println("iRet="+iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    // 转换字节数组为16进制字串
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    public static String GetMD5Code(String strObj) {
        String resultString = null;
        try {
            //resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
                resultString = byteToString(md.digest(strObj.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return resultString;
    }

    public static void main(String[] args) {
        System.out.println(UnixCrypt.crypt(MD5Utils.GetMD5Code("123456"),
				DigestUtils.sha256Hex(MD5Utils.GetMD5Code("123456"))));
    }

    /**
     * 随机生成5位随机数
     *
     * @return
     */
    public static String get5Radom()
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
        newString = newString.replace("0","");
        //把得到的数增加为固定长度,为5位
        while (newString.length() < 5)
        {
            newString = "1" + newString;
        }

        return newString;
    }
}
