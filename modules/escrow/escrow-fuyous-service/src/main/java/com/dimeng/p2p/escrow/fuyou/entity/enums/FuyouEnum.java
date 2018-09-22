package com.dimeng.p2p.escrow.fuyou.entity.enums;

import com.dimeng.util.StringHelper;

/**
 * 
 * 富友-交易类型
 * <busi_tp>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月14日]
 */
public enum FuyouEnum
{
    /** 
     * 还款
     */
    HK("还款"),
    
    /** 
     * 放款
     */
    FK("放款"),
    
    /** 
     * 转账
     */
    PWPC("转账"),
    
    /** 
     * 预授权
     */
    PW13("预授权"),
    
    /** 
     * 预授权撤销
     */
    PWCF("预授权撤销"),
    
    /** 
     * 划拨
     */
    PW03("划拨"),
    
    /** 
     * 转账冻结
     */
    PW14("转账冻结"),
    
    /** 
     * 划拨冻结
     */
    PW15("划拨冻结"),
    
    /** 
     * 冻结
     */
    PWDJ("冻结"),
    
    /** 
     * 解冻
     */
    PWJD("解冻"),
    
    /** 
     * 充值
     */
    PW11("充值"),
    
    /** 
     * 提现
     */
    PWTX("提现"),
    
    /** 
     * 冻结付款到冻结
     */
    PW19("冻结付款到冻结");
    
    protected final String chineseName;
    
    private FuyouEnum(String chineseName)
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
     * @return {@link FuyouEnum}
     */
    public static final FuyouEnum parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return FuyouEnum.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
}
