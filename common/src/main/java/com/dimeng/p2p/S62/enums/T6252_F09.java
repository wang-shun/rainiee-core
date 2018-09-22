package com.dimeng.p2p.S62.enums;

import com.dimeng.util.StringHelper;

/** 
 * 状态
 */
public enum T6252_F09
{
    
    /** 
     * 未还
     */
    WH("未还"),
    
    /** 
     * 还款中
     */
    HKZ("还款中"),
    
    /** 
     * 已还
     */
    YH("已还"),
    
    /** 
     * 垫付
     */
    DF("垫付"),
    
    /**
     * 提前还款
     */
    TQH("提前还款");
    
    protected final String chineseName;
    
    private T6252_F09(String chineseName)
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
     * @return {@link T6252_F09}
     */
    public static final T6252_F09 parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return T6252_F09.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
}
