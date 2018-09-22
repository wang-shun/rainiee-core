package com.dimeng.p2p.modules.statistics.console.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 充值提现统计总额
 */
public class RecWitTotal implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 充值总额
	 */
	public BigDecimal recharge = new BigDecimal(0);
	/**
	 * 提现总额
	 */
	public BigDecimal withdraw = new BigDecimal(0);
	/**
	 * 充值笔数总和
	 */
	public int rechargeCount;
	/**
	 * 提现笔数总和
	 */
	public int withdrawCount;
}
