package com.dimeng.p2p.escrow.fuyou.service;

import java.util.Map;

import com.dimeng.framework.service.Service;

/**
 * 
 * 转账或划拨
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月25日]
 */
public abstract interface TransferManage extends Service {
    
    /**
     * 转账或划拨
     * @param mchnt_txn_ssn  流水号
     * @param out_cust_no    付款登录账户
     * @param in_cust_no     收款登录账户
     * @param amt            划拨金额
     * @param contract_no    预授权合同号
     * @return
     * @throws Throwable
     */
    public abstract Map<String, Object> createTransferMap(String mchnt_txn_ssn, String out_cust_no, String in_cust_no, String amt, String contract_no) throws Throwable;
         
}
