package com.dimeng.p2p.escrow.fuyou.cond;

/**
 * 
 * 富友—最新充值接口实体类
 * 
 * @author heshiping
 * @version [版本号, 2015年5月25日]
 */
public interface ChargeCond
{
    
    /**
     * 商户代码
     */
    public String mchntCd();
    
    /**
     * 流水号
     */
    public String mchntTxnSsn();
    
    /**
     * 登录账户
     */
    public String loginId();
    
    /**
     * 商户返回地址
     */
    public String pageNotifyUrl()
        throws Throwable;
    
    /**
     * 金额
     */
    public String amt();
    
    /**
     * 手续费金额
     */
    public String mchntAmt();
    
    /**
     * 商户后台通知地址
     */
    public String backNotifyUrl()
        throws Throwable;
    
    /**
     * 签名数据
     */
    public String signature();
    
}
