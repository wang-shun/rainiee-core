package com.dimeng.p2p.S51.enums;

import com.dimeng.util.StringHelper;

/** 
 * 用户类型
 */
public enum T5123_F06
{
    
    /** 
     * 个人
     */
    GR("个人"),
    
    /** 
     * 企业
     */
    QY("企业");
    
    protected final String chineseName;
    
    private T5123_F06(String chineseName)
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
     * @return {@link T5123_F06}
     */
    public static final T5123_F06 parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return T5123_F06.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
}
