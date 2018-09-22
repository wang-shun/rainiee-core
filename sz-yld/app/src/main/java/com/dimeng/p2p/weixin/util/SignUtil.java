package com.dimeng.p2p.weixin.util;

import java.util.Arrays;

/**
 * 
 * 请求校验工具类
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年4月9日]
 */
public abstract class SignUtil
{
    /**
     * 验证签名
     * 
     * @param token 微信公众号的token值
     * @param signature 微信加密签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @return
     */
    public static boolean checkSignature(final String token, final String signature, final String timestamp, final String nonce)
    {
        String[] arr = new String[] {token, timestamp, nonce};
        
        // 将token、timestamp、nonce三个参数进行字典序排序
        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++)
        {
            content.append(arr[i]);
        }
        
        // 加密字符串
        String tmpStr = MySecurity.encode(content.toString());
        content = null;
        
        // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
    }
}