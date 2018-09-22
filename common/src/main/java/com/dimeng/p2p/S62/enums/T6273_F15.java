package com.dimeng.p2p.S62.enums;

import com.dimeng.util.StringHelper;

/**
 * 签章状态:
 * 文  件  名：T6273_F10.java
 * 版        权：深圳市迪蒙网络科技有限公司
 * 类  描  述：
 * 修  改  人：ZhangXu
 * 修改时间：2016年12月18日
 */
public enum T6273_F15
{
    /** 
     * DSQ-待申请
     */
    DSQ("待申请"),

    /** 
     * DSC-待上传
     */
    DSC("待上传"),

    /** 
     * DQM-待签名
     */
    DQM("待签名"),

    /** 
     * DGD-待归档
     */
    DGD("待归档"),
    
    /** 
     * YGD-待归档
     */
    YGD("已归档");
    
    protected final String chineseName;
    
    private T6273_F15(String chineseName)
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
     * @return {@link T6273_F15}
     */
    public static final T6273_F15 parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return T6273_F15.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
}
