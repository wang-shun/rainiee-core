package com.dimeng.p2p.modules.financing.user.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 可以转出的债权
 */
public class MaySettleFinacing {
	/**
	 * 借款标ID
	 */
	public int jkbId;
	/**
	 * 原借款债权ID
	 */
	public int assestsID;
	/**
	 * 剩余期数
	 */
	public int overNum;
	/**
	 * 借款期数
	 */
	public int jkTime;
	/**
	 * 下一还款日
	 */
	public Timestamp nextDay;
	/**
	 * 年化利率
	 */
	public double rate;
	/**
	 * 待收本息
	 */
	public BigDecimal money = new BigDecimal(0);
	/**
	 * 债权价值
	 */
	public BigDecimal assestsValue = new BigDecimal(0);
	/**
	 * 可转份数
	 */
	public int mayNum;
	/**
	 * 持有金额
	 */
	public BigDecimal tbMoney = new BigDecimal(0);
}
