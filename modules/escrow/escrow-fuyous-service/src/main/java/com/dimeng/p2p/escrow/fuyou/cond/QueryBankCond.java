package com.dimeng.p2p.escrow.fuyou.cond;

/**
 * 
 * 更换银行卡查询
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2016年2月24日]
 */
public interface QueryBankCond
{
    /**
     *  商户代码
     * @return
     */
    public String mchnt_cd();
    
    /**
     * 商户流水号
     */
    public String mchnt_txn_ssn();
    
    /**
     * 交易日期
     */
    public String login_id();
    
    /**
     * 交易流水
     */
    public String txn_ssn();
}
