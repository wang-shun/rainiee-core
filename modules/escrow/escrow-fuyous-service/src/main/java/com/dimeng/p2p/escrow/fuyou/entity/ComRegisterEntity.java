package com.dimeng.p2p.escrow.fuyou.entity;

import java.io.Serializable;

/**
 * 
 * 法人注册实休类
 * 
 * @author heshiping
 * @version [版本号, 2015年5月25日]
 */
public class ComRegisterEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	// 商户代码
	private String mchntCd;
	// 流水号
	private String mchntxnSsn;
	// 企业名称
	private String custNm;
	// 法人姓名
	private String artifNm;
	// 身份证号码
	private String certifId;
	// 手机号码
	private String mobileNo;
	// 邮箱地址
	private String email;
	// 开户行地区代码
	private String cityId;
	// 开户行行别
	private String parentBankId;
	// 开户行支行名称
	private String bankNm;
	// 帐号
	private String capAcntNo;
	// 提现密码
	private String password;
	// 登录密码
	private String lpassword;
	// 备注
	private String rem;
	// 签名信息
	private String signature;
	// 用户在商户系统的标志
	private String userIdFrom;

	public String getMchntCd() {
		return mchntCd;
	}

	public void setMchntCd(String mchntCd) {
		this.mchntCd = mchntCd;
	}

	public String getMchntxnSsn() {
		return mchntxnSsn;
	}

	public void setMchntxnSsn(String mchntxnSsn) {
		this.mchntxnSsn = mchntxnSsn;
	}

	public String getCustNm() {
		return custNm;
	}

	public void setCustNm(String custNm) {
		this.custNm = custNm;
	}

	public String getArtifNm() {
		return artifNm;
	}

	public void setArtifNm(String artifNm) {
		this.artifNm = artifNm;
	}

	public String getCertifId() {
		return certifId;
	}

	public void setCertifId(String certifId) {
		this.certifId = certifId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getParentBankId() {
		return parentBankId;
	}

	public void setParentBankId(String parentBankId) {
		this.parentBankId = parentBankId;
	}

	public String getBankNm() {
		return bankNm;
	}

	public void setBankNm(String bankNm) {
		this.bankNm = bankNm;
	}

	public String getCapAcntNo() {
		return capAcntNo;
	}

	public void setCapAcntNo(String capAcntNo) {
		this.capAcntNo = capAcntNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLpassword() {
		return lpassword;
	}

	public void setLpassword(String lpassword) {
		this.lpassword = lpassword;
	}

	public String getRem() {
		return rem;
	}

	public void setRem(String rem) {
		this.rem = rem;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getUserIdFrom() {
		return userIdFrom;
	}

	public void setUserIdFrom(String userIdFrom) {
		this.userIdFrom = userIdFrom;
	}

}
