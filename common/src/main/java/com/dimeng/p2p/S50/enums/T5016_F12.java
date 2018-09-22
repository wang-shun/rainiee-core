package com.dimeng.p2p.S50.enums;

import com.dimeng.util.StringHelper;

/**
 * 广告类型
 */
public enum T5016_F12
{
    
    /**
     * PC广告
     */
    PC("PC广告"),
    
    /**
     * APP广告
     */
    APP("APP广告"),
    
    /**
     * PC公益广告
     */
    GYJZ("公益捐赠"),
    
    /**
     * APP公益捐赠
     */
    APPGYJZ("APP公益捐赠"),

    /**
     * PC登录页广告
     */
    PCLOGIN("PC登录页广告"),

    /**
     * PC注册页广告
     */
    PCREGISTER("PC注册页广告");

    /**
     * IOS启动页图片
     *//*
    IOSPIC("IOS启动页图片"),

    *//**
     * Android启动页图片
     *//*
    ANDROIDPIC("Android启动页图片"),

    *//**
     * 发现页广告图片
     *//*
    FINDPIC("发现页广告图片");*/
    
    protected final String chineseName;
    
    private T5016_F12(String chineseName)
    {
        this.chineseName = chineseName;
    }
    
    /**
     * 获取中文名称.
     * 
     * @return {@link String}
     */
    public String getChineseName()
    {
        return chineseName;
    }
    
    /**
     * 解析字符串.
     * 
     * @return {@link T5016_F12}
     */
    public static final T5016_F12 parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return T5016_F12.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
}
