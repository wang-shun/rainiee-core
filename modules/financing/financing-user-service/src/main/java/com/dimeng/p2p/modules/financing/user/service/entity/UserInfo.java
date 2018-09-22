package com.dimeng.p2p.modules.financing.user.service.entity;

import java.math.BigDecimal;

import com.dimeng.p2p.common.enums.AttestationStatus;

public class UserInfo {
	/**
	 * 登陆Id
	 */
	public int loginId;
	/**
	 * 登陆名
	 */
	public String loginName;
	/**
	 * 可用余额
	 */
	public BigDecimal kyMoney = new BigDecimal(0);
	/**
	 * 冻结金额
	 */
	public BigDecimal djMoney = new BigDecimal(0);
	/**
	 * 总余额
	 */
	public BigDecimal totleMoney = new BigDecimal(0);
	/**
	 * 是否实名认证
	 */
	public AttestationStatus attestationStatus;
	/**
	 * 交易密码
	 */
	public String txPassword;

}
