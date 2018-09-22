package com.dimeng.p2p.modules.statistics.console.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class YearRiskFunds implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 垫付
	 */
	public BigDecimal df = new BigDecimal(0);
	/**
	 * 垫付返还
	 */
	public BigDecimal dffh = new BigDecimal(0);
	/**
	 * 借款成交服务费
	 */
	public BigDecimal jkcjfwf = new BigDecimal(0);
	/**
	 * 手动增加保证金
	 */
	public BigDecimal sdzjbzj = new BigDecimal(0);
	/**
	 * 手动扣除保证金
	 */
	public BigDecimal sdkcbzj = new BigDecimal(0);
}
