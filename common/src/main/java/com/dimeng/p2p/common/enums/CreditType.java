package com.dimeng.p2p.common.enums;

/**
 * 借款类型.
 */
public enum CreditType
{
    
    /**
     * 薪金贷
     */
    XJD("薪金贷"),
    /**
     * 担保贷
     */
    DBD("担保贷"),
    /**
     * 生意贷
     */
    SYD("生意贷"),
    /**
     * 实地认证
     */
    SDRZ("实地认证"),
    /**
     * 信用担保
     */
    XYDB("信用担保");
    
    protected final String name;
    
    private CreditType(String name)
    {
        this.name = name;
    }
    
    /**
     * 获取中文名称.
     * 
     * @return {@link String}
     */
    public String getName()
    {
        return name;
        
    }
}
