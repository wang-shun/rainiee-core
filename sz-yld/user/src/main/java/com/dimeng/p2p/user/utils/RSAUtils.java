package com.dimeng.p2p.user.utils;


import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;


/**
 * @author fuiou honcyGao
 * 20160815
 * RSA公钥/私钥/签名工具包
 * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 */
public class RSAUtils {

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";
    
    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";
    
    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 234;
    
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 256;
    
    /**
     *RSA规定版本(3.0及3.0以上) 
     */
   private static final String RSA_VER="3.0";
    /**
     * 生成密钥对(公钥和私钥)
     * @return
     * @throws Exception
     */
    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(2048);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }
    
    public static String signMD5WithRSA(String data, String key) throws Exception{
		byte[] bytesKey = Base64Utils.decode(key.trim());
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(bytesKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
		Signature signature = Signature.getInstance("MD5WithRSA");
		signature.initSign(priKey);
		signature.update(data.getBytes("GBK"));
		return Base64Utils.encode(signature.sign());
	}
	
    public static boolean verifySignMD5WithRSA(String sign, String signPlain, String key) throws Exception
	{
		byte[] keyBytes = Base64Utils.decode(key);
		// 构造X509EncodedKeySpec对象
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		// KEY_ALGORITHM 指定的加密算法
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		// 取公钥匙对象
		PublicKey pubKey = keyFactory.generatePublic(keySpec);
		Signature signature = Signature.getInstance("MD5withRSA");
		signature.initVerify(pubKey);
		signature.update(signPlain.toString().getBytes("GBK"));
		// 验证签名是否正常
		return signature.verify(Base64Utils.decode(sign));
	}
	
    
    /**
     * 用私钥对信息生成数字签名
     * @param data 已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64Utils.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64Utils.encode(signature.sign());
    }

    /**
     * 校验数字签名
     * @param data 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign 数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64Utils.decode(sign));
    }

    /**
     * 私钥解密
     * @param encryptedData 已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 公钥解密
     * @param encryptedData 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 公钥加密
     * @param data 源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 私钥加密
     * @param data 源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64Utils.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * 获取私钥
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64Utils.encode(key.getEncoded());
    }

    /**
     * 获取公钥
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64Utils.encode(key.getEncoded());
    }
	
	public static void main(String[] args) throws Exception {
		String source ="0002900F0022256|<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><root><ver>1.0</ver><reqDate>20170727</reqDate><mchntSsn>1000000000000023</mchntSsn><projectId>001</projectId><orgCd>3021200F0000455</orgCd><subAmt>10000</subAmt><mchntSubAmt>2000</mchntSubAmt><orgSubAmt>8000</orgSubAmt><refundPeriod>第一期</refundPeriod><busiType>1</busiType><accntNm>陈文杰 </accntNm><accntNo>6228480128391367470</accntNo><bankCd>0103</bankCd><certTp>0</certTp><certNo>441481199311096112</certNo><mobile>13750580410</mobile><memo>demo</memo></root>";
		//String source ="0002900F0022256|<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><root><ver>1.0</ver><reqDate>20170727</reqDate><mchntSsn>1000000000000001</mchntSsn><projectId>001</projectId><orgCd>3021200F0000455</orgCd><tranAmt>1000</tranAmt><refundPeriod>第一期</refundPeriod><tranType>1</tranType><borrower>陈文杰 </borrower><certNo>6228480128391367470</certNo><memo>demo</memo></root>";
		Map<String, Object> map = genKeyPair();
		String privateKey =  getPrivateKey(map);
		 //String  privateKey ="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCb2I/33lDfw131uxKgR6pu4POoX6AID8bkIlVc0OkdoYD2Zu3jJ8UcE8FgJSrDETVZhuaaBXUj8RX9d5kmJS3S14DMYnSsKlxgc5dAIgZPOj89frNIMA8q78gjgJUrEHKhIgUVmlM7ZDAAV9EqIyT7AJePvTUcHDotM4GORRC2noY4dvnhVYVHtHLldFcEOSoVqZx4wAa+ggUhasSMyw8fBNmQ/l0I77sL5viTFBR7RdZ851eBikCycG8riXCLILKKsrGXuUSgDjDrCLBRY69bTTYj2GSARFLVBdwYrXY3lChz3kuwzaVZo4yc/yszZIcaQeJ/1lbKdEgXfDJg9WNXAgMBAAECggEAArt4QrM+Hb4yHoXKHM9E2K7O9su+ZYXJSeSkNH1hKOjU9vz0/L+MwCJEiKf+KO9bTReFYXvpAtlONmRrZfTO6dxnYK31N7OCbRp5s2ElVSxDQZPAFuroySrtClpNwzSFnfeXuTo8bVUjVk4z15ZIJrJq9dUuYjDIgFRf2Buz4E+9+ufwHJddLmmzQnBXgbxzSCNmgmnzE9Dc70w2WMghi5CPycLFUKylShEexOA2Cj1rHiMyhVGV64rEFKsOQR+x0vH9VXXw3ED9jJ8FiBT+fwXBsBovvSoiRXyyEj9xRUWNWn2MZlPlGbNhURHU8oxAqoiGByECjNcsvVyGaGQQEQKBgQD+cUQ7Ac/cw5MiaEs5l7FRWG9wMdsnL9HkjLBKfwwO1CxIIpzeR6G7Wbi/KDBakb2RkFqEW3PR/fouD85PCrQYdS/oLT15k3kOoYtmNlIN9PEg7e4WMKAL8z0+hPyqByI/+rHVVLijYSLSo/IC1ILzt7wTupt5hd885e6wkboWmQKBgQCczMlbKZv9OxPL4/Ya9Kwo0SChERitQZyTgt8Yd7GPZjoTsuEk3dUGcbMNFKtgPsN0etnEjTiw2UtWZo/sqFE1dSJ5VF1rpXVRKYBf6tOQBH48dIz8r+uzBo2r2VA1amm/d9vVFA0dPiIIE4gkDYOBYrQGjGgqKwVt/5YGZUuvbwKBgQDBBMOxz6oT2vle/Kc0RXLqNqAzCF8r1wbuzhteKArJpQMCpd9CcCysp1+TA7XKvwyDxHImPgoanwSOtMbZB1GfzCZPhyI/fDQ1eS76lvKgS9QwGs6mXrTK2/b12oShp0/AITmOj/qRVjrIeD47D2NX52XxwXn4MiorVHJB2ArG4QKBgDboCxZuIp5xIoSCJfrYdLSyTm+4UeDRmeeZq0+hEj85t9qQCADl4SOpit5ov34rXDwcjMckAJcE8DmoHnvrkMxcYR8FaNU6EjPCiZ6M11qWCDJZScluPOXqxI9JwKmDwtX2PbeGlvUHfdlAJasiREt5abrlTdB+Uh9roXhJ3k/nAoGBAO/xlbeGRc/82oxIRhFvbOuUuPWT48MAaZdlyuPcaNP+8Z1o//1vUD0rchWBAkxGWg67J18T6KrqoOK5XijydnbLrxbNJIw3CibaC8uCy/R9G9NlUHqoLlQqfWwVoqzVGHt6nrDgyC75GZ6pMQtXo0Cp7/u9z5Ri7q1X34HLy1Xk";
		 System.out.println("privateKey:"+privateKey);
		String publicKey = getPublicKey(map);
		 //String publicKey ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAm9iP995Q38Nd9bsSoEeqbuDzqF+gCA/G5CJVXNDpHaGA9mbt4yfFHBPBYCUqwxE1WYbmmgV1I/EV/XeZJiUt0teAzGJ0rCpcYHOXQCIGTzo/PX6zSDAPKu/II4CVKxByoSIFFZpTO2QwAFfRKiMk+wCXj701HBw6LTOBjkUQtp6GOHb54VWFR7Ry5XRXBDkqFamceMAGvoIFIWrEjMsPHwTZkP5dCO+7C+b4kxQUe0XWfOdXgYpAsnBvK4lwiyCyirKxl7lEoA4w6wiwUWOvW002I9hkgERS1QXcGK12N5Qoc95LsM2lWaOMnP8rM2SHGkHif9ZWynRIF3wyYPVjVwIDAQAB";
		 System.out.println("publicKey:"+publicKey);
		String sign = signMD5WithRSA(source, privateKey);
		System.out.println(verifySignMD5WithRSA(sign,source,publicKey));
		// String sign ="dWd2+irw+jWGVVJ5GuSLeyJJDs/h0e9gMURQsJiL7JWCb7BmOcwRxAELlS3oTLkP+UIuiZuTiJfbvAzn4IOk0nzwqkZNsLKxOY3yo6UJdVpDxekht4y8JczCkMvpU++CfCCQOq9b+0FPGv05Ob+L+M9trJXhMsKKZJ/wAzO5ACSv9nyzjIQq1lIFfRwX0ZriHNoXqn7uZkjKEz3nN3fD3RO6hTws3cGGjJEGHM/Zrngh/hEeALGVPJ5A4wGgI2oD4s/4d3flki3vDDDzJB416zFK6BNmk8lcKJRWBpxhGtLnPeqJ9SIQ4uG/0O/LscChYjjoWr3yUbH+Ji/06zh+xg==";
		//System.out.println("sign:"+sign);
		//System.out.println(verify(source.getBytes("UTF-8"), publicKey, sign));
		/*
		String  secret = Base64Utils.encode((encryptByPublicKey(source.getBytes("UTF-8"), publicKey)));
		System.out.println("密文:"+secret);
		byte[] bytes = Base64Utils.decode(secret);
		byte[] resultbytes = decryptByPrivateKey(bytes,privateKey);
		String  cleartext  = new String(resultbytes);
		//String  cleartext  = Base64Utils.encode(resultbytes);
		System.out.println("明文："+cleartext);*/
		
		
		
	}
	
}
