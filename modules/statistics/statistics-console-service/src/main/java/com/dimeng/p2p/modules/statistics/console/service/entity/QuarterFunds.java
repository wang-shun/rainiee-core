package com.dimeng.p2p.modules.statistics.console.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 平台资金统计
 *
 */
public class QuarterFunds implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 季度
	 */
	public int quarter;
	/**
	 * 收入
	 */
	public BigDecimal amountIn = new BigDecimal(0);
	/**
	 * 支出
	 */
	public BigDecimal amountOut = new BigDecimal(0);
	/**
	 * 盈亏
	 */
	public BigDecimal sum = new BigDecimal(0);
}
