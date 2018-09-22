package com.dimeng.p2p.modules.bid.console.service.entity;

import java.math.BigDecimal;

/**
 * 投资统计信息.
 */
public class Tztjxx {
	/**
	 * 累计成交总金额
	 */
	public BigDecimal totleMoney = new BigDecimal(0);

	/**
	 * 累计成交总笔数
	 */
	public long totleCount;

	/**
	 * 为用户累计赚取
	 */
	public BigDecimal userEarnMoney = new BigDecimal(0);
}
