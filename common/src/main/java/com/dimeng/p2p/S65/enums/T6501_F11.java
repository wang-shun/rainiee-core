package com.dimeng.p2p.S65.enums;

import com.dimeng.util.StringHelper;

/**
 * 
 * 是否已对账
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月23日]
 */
public enum T6501_F11
{
    
    /** 
     * 未对账
     */
    F("未对账"),
    
    /** 
     * 已对账
     */
    S("已对账");
    
    protected final String chineseName;
    
    private T6501_F11(String chineseName)
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
     * @return {@link T6501_F11}
     */
    public static final T6501_F11 parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return T6501_F11.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
}
