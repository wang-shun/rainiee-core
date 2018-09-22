package com.dimeng.p2p.S62.enums;

import com.dimeng.util.StringHelper;

/**
 * 用户类型 
 * 文  件  名：T6273_F10.java
 * 版        权：深圳市迪蒙网络科技有限公司
 * 类  描  述：
 * 修  改  人：ZhangXu
 * 修改时间：2016年12月14日
 */
public enum T6273_F10
{
    
    /** 
     * TZR-投资人
     */
    TZR("投资人"),
    
    /** 
     * JKR-借款人
     */
    JKR("借款人"),
    
    /** 
     * ZCR-转出人
     */
    ZCR("转出人"),
    
    /** 
     * SRR-受让人
     */
    SRR("受让人"), DFR("垫付人"), BDFR("被垫付人"),DBR("担保人");;
    
    protected final String chineseName;
    
    private T6273_F10(String chineseName)
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
     * @return {@link T6273_F10}
     */
    public static final T6273_F10 parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return T6273_F10.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
}
