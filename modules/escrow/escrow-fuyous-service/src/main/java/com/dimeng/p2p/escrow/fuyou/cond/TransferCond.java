package com.dimeng.p2p.escrow.fuyou.cond;

/**
 * 
 * 划拨\转账
 * <个人与个人间的转账>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月25日]
 */
public interface TransferCond
{
    /**
     *  商户代码   
     */
    public String mchntCd();
    
    /**
     * 商户流水号  
     */
    public String mchntTxnSsn();
    
    /**
     *  FK：放款 ，多转一HK：还款，一转多 必传
     */
    public String tranName();
    
    /**
     * 付款登录账户
     */
    public String outCustNo();
    
    /**
     * 收款登录账户
     */
    public String inCustNo();
    
    /**
     * 金额 <转账金额/预授金额>
     */
    public String amt();
    
    /**
     * 手续/红包金额   
     */
    public String mchntAmt();
    
    /**
     * 合同号
     */
    public String contractNo();
    
    /**
     *  签名数据   
     */
    public String signature();
    
}
