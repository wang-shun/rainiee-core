package com.dimeng.p2p.escrow.fuyou.cond;

/**
 * 
 * 用户余额查询
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月31日]
 */
public interface BalanceCond
{
    /**
     *  商户代码
     * @return
     */
    public String mchntCd();
    
    /**
     * 商户流水号
     */
    public String mchntTxnSsn();
    
    /**
     * 交易日期
     */
    public String mchntTxnDt();
    
    /**
     * 待查询的登录帐户
     * 查询企业注册的手机号或者用户名，多个帐号请以”|”分隔 （最多只能同时查10个用户）
     * 13678424821|13312324854
     */
    public String custNo();
    
}
