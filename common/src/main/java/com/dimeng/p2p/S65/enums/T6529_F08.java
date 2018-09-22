package com.dimeng.p2p.S65.enums;

import com.dimeng.util.StringHelper;

/** 
 * 是否转让
 */
public enum T6529_F08
{
    
    /** 
     * 否
     */
    F("否"),
    
    /** 
     * 是
     */
    S("是");
    
    protected final String chineseName;
    
    private T6529_F08(String chineseName)
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
     * @return {@link T6529_F08}
     */
    public static final T6529_F08 parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return T6529_F08.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
}
