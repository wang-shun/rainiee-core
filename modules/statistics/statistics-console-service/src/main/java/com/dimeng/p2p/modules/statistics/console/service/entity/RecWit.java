package com.dimeng.p2p.modules.statistics.console.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 充值提现统计
 */
public class RecWit implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 月份
	 */
	public int month;
	/**
	 * 充值
	 */
	public BigDecimal recharge = new BigDecimal(0);
	/**
	 * 提现
	 */
	public BigDecimal withdraw = new BigDecimal(0);
	/**
	 * 充值笔数
	 */
	public int rechargeCount;
	/**
	 * 提现笔数
	 */
	public int withdrawCount;
}
