package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;

/**
 * 最近一年成功借款的实体类
 *
 */
public class YearCreditTotal {
	/**
	 * 年时间
	 */
	public int year;
	/**
	 * 季度时间
	 */
	public int quarter;
	/**
	 * 金额
	 */
	public BigDecimal money = new BigDecimal(0);
}
