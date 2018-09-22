package com.dimeng.p2p.escrow.fuyou.cond;

/**
 * 法人注册请求参数
 * 
 * @author heshiping
 * @date 2015.5.12
 */
public interface ComRegisterCond {

	/**
	 * 商户代码(必填)
	 */
	public abstract String mchntCd() throws Throwable;

	/**
	 * 流水号(必填)
	 */
	public abstract String mchntTxnSsn();

	/**
	 * 用户在商户系统的标志（必选）
	 */
	public abstract String userIdFrom();

	/**
	 * 手机号码（可选）
	 */
	public abstract String mobileNo();

	/**
	 * 企业名称（可选）
	 */
	public abstract String custNm();

	/**
	 * 法人姓名
	 */
	public abstract String artifNm();

	/**
	 * 身份证号码（可选）
	 */
	public abstract String certifId();

	/**
	 * 邮箱地址（可选）
	 */
	public abstract String email();

	/**
	 * 开户行地区代码（可选）
	 */
	public abstract String cityId();

	/**
	 * 开户行行别（可选）
	 */
	public abstract String parentBankId();

	/**
	 * 开户行支行名称（可选）
	 */
	public abstract String bankNm();

	/**
	 * 帐号（可选）
	 */
	public abstract String capAcntNo();

	/**
	 * 商户返回地址(必填)
	 */
	public abstract String pageNotifyUrl() throws Throwable;

	/**
	 * 商户后台通知地址(必填)
	 */
	public abstract String backNotifyUrl() throws Throwable;

	/**
	 * 签名数据(必填)
	 */
	public abstract String signature();

}
