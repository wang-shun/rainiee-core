package com.dimeng.p2p.escrow.fuyou.entity;

/**
 * 转账或划拨请求参数实体
 * @author chentongbing
 * @date 2014年9月12日
 */
public class TransferAccountsEntity {
	
	public String mchntCd;			//商户代码
	
	public String mchntTxnSsn;	//流水号
	
	public String outCustNo;		//付款登录账户
	
	public String inCustNo;		//收款登录账户
	
	public String amt;				//转账或划拨金额
	
	public String contractNo;		//预授权合同号
	
	public String rem;				//备注
	
	public String signature;		//签名数据

	/**
	 * @return the mchnt_cd
	 */
	public String getMchnt_cd() {
		return mchntCd;
	}

	/**
	 * @param mchnt_cd the mchnt_cd to set
	 */
	public void setMchnt_cd(String mchnt_cd) {
		this.mchntCd = mchnt_cd;
	}

	/**
	 * @return the mchnt_txn_ssn
	 */
	public String getMchnt_txn_ssn() {
		return mchntTxnSsn;
	}

	/**
	 * @param mchnt_txn_ssn the mchnt_txn_ssn to set
	 */
	public void setMchnt_txn_ssn(String mchnt_txn_ssn) {
		this.mchntTxnSsn = mchnt_txn_ssn;
	}

	/**
	 * @return the out_cust_no
	 */
	public String getOut_cust_no() {
		return outCustNo;
	}

	/**
	 * @param out_cust_no the out_cust_no to set
	 */
	public void setOut_cust_no(String out_cust_no) {
		this.outCustNo = out_cust_no;
	}

	/**
	 * @return the in_cust_no
	 */
	public String getIn_cust_no() {
		return inCustNo;
	}

	/**
	 * @param in_cust_no the in_cust_no to set
	 */
	public void setIn_cust_no(String in_cust_no) {
		this.inCustNo = in_cust_no;
	}

	/**
	 * @return the amt
	 */
	public String getAmt() {
		return amt;
	}

	/**
	 * @param amt the amt to set
	 */
	public void setAmt(String amt) {
		this.amt = amt;
	}

	/**
	 * @return the contract_no
	 */
	public String getContract_no() {
		return contractNo;
	}

	/**
	 * @param contract_no the contract_no to set
	 */
	public void setContract_no(String contract_no) {
		this.contractNo = contract_no;
	}

	/**
	 * @return the rem
	 */
	public String getRem() {
		return rem;
	}

	/**
	 * @param rem the rem to set
	 */
	public void setRem(String rem) {
		this.rem = rem;
	}

	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * @param signature the signature to set
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}

}
