package com.dimeng.p2p.modules.financing.user.service.entity;

import java.math.BigDecimal;

import com.dimeng.p2p.common.enums.CreditStatus;

/**
 * 借款标信息
 */
public class CreditInfo {

	/**
	 * 借款表ID
	 */
	public int id;
	/**
	 * 还需金额
	 */
	public BigDecimal remainAmount = new BigDecimal(0);
	/**
	 * 每份金额
	 */
	public BigDecimal perAmount = new BigDecimal(0);
	/**
	 * 状态
	 */
	public CreditStatus creditStatus;
	/**
	 * 年化利率
	 */
	public double rate;
	/**
	 * 借款期限
	 */
	public int jkqx;
	/**
	 * 持有份数
	 */
	public int cyfs;
	
}
