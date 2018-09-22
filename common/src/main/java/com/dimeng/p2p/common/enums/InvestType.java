package com.dimeng.p2p.common.enums;

/**
 * 投资项目类型.
 */
public enum InvestType
{
    
    /**
     * 信用认证标
     */
    XYRZB("信用认证标"),
    /**
     * 实地认证标
     */
    SDRZB("实地认证标"),
    /**
     * 抵押认证标
     */
    DYRZB("抵押认证标"),
    /**
     * 担保标
     */
    JGDBB("担保标");
    
    protected final String name;
    
    private InvestType(String name)
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
