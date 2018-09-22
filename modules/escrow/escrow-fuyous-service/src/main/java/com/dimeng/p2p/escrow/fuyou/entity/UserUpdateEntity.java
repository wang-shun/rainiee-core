package com.dimeng.p2p.escrow.fuyou.entity;

/**
 * 用户信息更新通知实体
 */
public class UserUpdateEntity {
	
	/**
	 * 商户代码
	 */
	private String mchntCd;
	
	/**
	 * 流水号
	 */
	private String mchntTxnSsn;
	
	/**
	 * 用户在商户系统的标志
	 */
	private String userIdFrom;
	
	/**
	 * 手机号码
	 */
	private String mobileNo;
	
	/**
	 * 法人姓名
	 */
	private String  custNm;
	
	/**
	 * 身份证号码
	 */
	private String  certifId;
	
	/**
	 * 邮箱地址
	 */
	private String  email;
	
	/**
	 * 开户行地区代码
	 */
	private String  cityId;
	
	/**
	 * 开户行行别
	 */
	private String parentBankId;
	
	/**
	 * 开户行支行名称
	 */
	private String bankNm;
	
	/**
	 * 银行帐号
	 */
	private String  capAcntNo;
	
	/**
	 * 返回码
	 */
	private String  respCode;
	
	/**
	 * 签名数据
	 */
	private String signature;

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

	public String getUser_id_from() {
		return userIdFrom;
	}

	public void setUser_id_from(String user_id_from) {
		this.userIdFrom = user_id_from;
	}

	public String getMobile_no() {
		return mobileNo;
	}

	public void setMobile_no(String mobile_no) {
		this.mobileNo = mobile_no;
	}

	public String getCust_nm() {
		return custNm;
	}

	public void setCust_nm(String cust_nm) {
		this.custNm = cust_nm;
	}

	public String getCertif_id() {
		return certifId;
	}

	public void setCertif_id(String certif_id) {
		this.certifId = certif_id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCity_id() {
		return cityId;
	}

	public void setCity_id(String city_id) {
		this.cityId = city_id;
	}

	public String getParent_bank_id() {
		return parentBankId;
	}

	public void setParent_bank_id(String parent_bank_id) {
		this.parentBankId = parent_bank_id;
	}

	public String getBank_nm() {
		return bankNm;
	}

	public void setBank_nm(String bank_nm) {
		this.bankNm = bank_nm;
	}

	public String getCapAcntNo() {
		return capAcntNo;
	}

	public void setCapAcntNo(String capAcntNo) {
		this.capAcntNo = capAcntNo;
	}

	public String getResp_code() {
		return respCode;
	}

	public void setResp_code(String resp_code) {
		this.respCode = resp_code;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
	
}
