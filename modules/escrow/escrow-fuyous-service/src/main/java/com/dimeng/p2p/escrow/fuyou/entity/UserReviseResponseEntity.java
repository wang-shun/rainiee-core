package com.dimeng.p2p.escrow.fuyou.entity;

import java.io.Serializable;

/**
 * 用户信息修改后返回的实体
 * 
 * @author zhouchuanlong
 * @date 2014年9月11日
 */
public class UserReviseResponseEntity implements Serializable{
	
	/**
     * 注释内容
     */
    private static final long serialVersionUID = -3413736741428558608L;

    // 响应码
	private String respCode;
	
	// 商户代码
	private String mchntCd;
	
	// 请求流水号
	private String mchntTxnSsn;
	
	// 签名数据
	private String signature;
	
	public UserReviseResponseEntity(){}
	
	public UserReviseResponseEntity(String resp_code, String mchnt_cd, 
			String mchnt_txn_ssn, String signature){
		this.respCode = resp_code;
		this.mchntCd = mchnt_cd;
		this.mchntTxnSsn = mchnt_txn_ssn;
		this.signature = signature;
	}

	public String getResp_code() {
		return respCode;
	}

	public void setResp_code(String resp_code) {
		this.respCode = resp_code;
	}

	public String getMchnt_cd() {
		return mchntCd;
	}

	public void setMchnt_cd(String mchnt_cd) {
		this.mchntCd = mchnt_cd;
	}

	public String getMchnt_txn_ssn() {
		return mchntTxnSsn;
	}

	public void setMchnt_txn_ssn(String mchnt_txn_ssn) {
		this.mchntTxnSsn = mchnt_txn_ssn;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
	
}
