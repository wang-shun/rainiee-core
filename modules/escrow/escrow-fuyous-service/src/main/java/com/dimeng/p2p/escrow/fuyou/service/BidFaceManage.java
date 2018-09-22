package com.dimeng.p2p.escrow.fuyou.service;

import java.util.Map;

import com.dimeng.framework.service.Service;

/**
 * 
 * BidFace 接口类
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月22日]
 */
public interface BidFaceManage extends Service {
	
    /**
     * 预授权申请参数
     * @param mchntTxnSsn  流水号
     * @param outCustNo    出账账户
     * @param inCustNo     入账账户
     * @param amt          金额
     * @return
     * @throws Throwable
     */
    public abstract Map<String, Object> createBidFace(String mchntTxnSsn, String outCustNo, String inCustNo, String amt) throws Throwable;
    
    /**
     * 预授权撤销
     * @param mchnt_txn_ssn 流水号
     * @param out_cust_no   出账账户
     * @param in_cust_no    入账账户
     * @param contract_no   预授权合同号
     * @return
     * @throws Throwable
     */
    public abstract Map<String, Object> createBidCancel(String mchnt_txn_ssn, String out_cust_no, String in_cust_no, String contract_no) throws Throwable;
        
}
