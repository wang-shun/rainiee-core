package com.dimeng.p2p.modules.nciic.util;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.bouncycastle.util.encoders.Base64;

public class QueryThread extends Thread
{
    
    public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
    
    public static final String ENCODE_KEY = "12345678";
    
    private String param;
    
    public QueryThread(String param)
    {
        super();
        this.param = param;
    }
    
    @Override
    public void run()
    {
        
    }
    
    public static byte[] decode(String key, byte[] data)
        throws Exception
    {
        try
        {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key 的长度不能够小于 8 位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec(ENCODE_KEY.getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            return cipher.doFinal(data);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Exception(e);
        }
    }
    
    public static String encode(String key, byte[] data)
        throws Exception
    {
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // key的长度不能够小于8位字节
        Key secretKey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(ENCODE_KEY.getBytes());// 向量
        AlgorithmParameterSpec paramSpec = iv;
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
        byte[] bytes = cipher.doFinal(data);
        return new String(Base64.encode(bytes), "UTF-8");
    }
    
    public String getParam()
    {
        return param;
    }
    
    public void setParam(String param)
    {
        this.param = param;
    }
    
}
