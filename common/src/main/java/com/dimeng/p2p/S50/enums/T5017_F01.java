package com.dimeng.p2p.S50.enums;

import com.dimeng.util.StringHelper;

/** 
 * 协议类型
 */
public enum T5017_F01
{
    
    /** 
     * 债权转让协议(债权转让及受让协议)
     */
    ZQZRXY("债权转让协议(债权转让及受让协议)"), YXLCXY("YXLCXY"),
    
    /** 
     * 实地认证标协议(借款协议(实))
     */
    SDRZBXY("实地认证标协议(借款协议(实))"),
    
    /** 
     * 信用认证标协议(借款协议(信))
     */
    XYRZBXY("信用认证标协议(借款协议(信))"),
    
    /** 
     * 担保标协议(借款协议(担保))
     */
    DBBXY("担保标协议(借款协议(担保))"),
    
    /** 
     * 注册协议
     */
    ZCXY("注册协议"),
    
    /** 
     * 借款协议(实/保)
     */
    JKXYSB("借款协议(实/保)"),
    
    /** 
     * 债权转让说明书
     */
    ZQZRSMS("债权转让说明书"),
    
    /** 
     * 线上债权转让协议
     */
    XSZQZRXY("线上债权转让协议"),
    
    /** 
     * 线下债权转让协议
     */
    XXZQZRXY("线下债权转让协议"),
    
    /** 
     * 企业信用标协议
     */
    QYXYBXY("企业信用标协议"),
    
    /** 
     * 企业抵押担保标协议
     */
    QYDYDBBXY("企业抵押担保标协议"),
    
    /** 
     * 个人信用标协议
     */
    GRXYBXY("个人信用标协议"),
    
    /** 
     * 个人抵押担保标协议
     */
    GRDYDBBXY("个人抵押担保标协议"),
    
    /** 
     * 友金所资金管理规定
     */
    ZJGLGD("友金所资金管理规定"),
    
    /** 
     * 个人借款及担保协议
     */
    GRJKJDBXY("个人借款及担保协议"),
    
    /** 
     * 催收授权委托书
     */
    CSSQWTS("催收授权委托书"),
    
    /** 
     * 本息保障计划
     */
    BXBZ("本息保障计划"),
    
    /**
     * 不良债权转让协议
     */
    BLZQZRXY("不良债权转让协议");
    
    protected final String chineseName;
    
    private T5017_F01(String chineseName)
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
     * @return {@link T5017_F01}
     */
    public static final T5017_F01 parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return T5017_F01.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
}
