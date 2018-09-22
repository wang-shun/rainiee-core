package com.dimeng.p2p.escrow.fuyou.service;

import java.util.Map;

import com.dimeng.framework.service.Service;

public abstract interface ChargeWithdrawManage extends Service {
	
    /**
     * 充值提现查询
     * @param cond
     * @return
     * @throws Throwable
     */
    public abstract Map<String, Object> createChargeWithdraw(String mchnt_txn_ssn, String busi_tp, String txn_ssn, String cust_no) throws Throwable;
        
}
