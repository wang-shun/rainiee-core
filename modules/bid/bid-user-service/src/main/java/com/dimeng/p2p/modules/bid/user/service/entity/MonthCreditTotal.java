package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;

/**
 * 最近六个月的还款统计的实体类
 *
 */
public class MonthCreditTotal {
	/**
	 * 年时间
	 */
	public int year;
	/**
	 * 月时间
	 */
	public int month;
	/**
	 * 金额
	 */
	public BigDecimal money = new BigDecimal(0);
}
