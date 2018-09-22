package com.dimeng.p2p.escrow.fuyou.cond;

import java.io.IOException;

/**
 * 
 * 修改银行卡
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2016年1月19日]
 */
public interface BankCond
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
     * 商户返回地址
     * @throws IOException 
     */
    public String pageNotifyUrl() throws IOException;
    
}
