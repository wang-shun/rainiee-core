package com.dimeng.p2p.S62.enums;

import com.dimeng.util.StringHelper;

/**
 * 状态
 */
public enum T6230_F20
{
    
    /**
     * 申请中
     */
    SQZ("申请中"),
    
    /**
     * 待审核
     */
    DSH("待审核"),
    
    /**
     * 待发布
     */
    DFB("待发布"),
    
    /**
     * 预发布
     */
    YFB("预发布"),
    
    /**
     * 投资中
     */
    TBZ("投资中"),
    
    /**
     * 待放款
     */
    DFK("待放款"),
    
    /**
     * 还款中
     */
    HKZ("还款中"),
    
    /**
     * 已结清
     */
    YJQ("已结清"),
    
    /**
     * 已垫付
     */
    YDF("已垫付"),
    
    /**
     * 已流标
     */
    YLB("已流标"),
    
    /**
     * 垫付中
     */
    DFZ("垫付中"),
    
    /**
     * 已作废
     */
    YZF("已作废"),
    
    /**
     * 已转让
     */
    YZR("已转让")
    //,
    //	/**
    //	 * 审核中
    //	 */
    //	SHZ("审核中"),
    //	/**
    //	 * 待补录
    //	 */
    //	DBL("待补录")
    ;
    
    protected final String chineseName;
    
    private T6230_F20(String chineseName)
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
     * @return {@link T6230_F20}
     */
    public static final T6230_F20 parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return T6230_F20.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
}
