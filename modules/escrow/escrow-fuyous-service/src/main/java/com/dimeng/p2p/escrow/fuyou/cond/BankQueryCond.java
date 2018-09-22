package com.dimeng.p2p.escrow.fuyou.cond;

import java.io.IOException;

/**
 * 
 * 银行卡更换查询
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2016年1月21日]
 */
public interface BankQueryCond
{
    /**
     *  商户代码
     * @return
     * @throws IOException 
     */
    public String mchntCd()
        throws IOException;
    
    /**
     * 商户流水号
     */
    public String mchntTxnSsn();
    
    /**
     * 个人用户
     */
    public String loginId();
    
    /**
     * 交易流水
     */
    public String txnSsn();
}
