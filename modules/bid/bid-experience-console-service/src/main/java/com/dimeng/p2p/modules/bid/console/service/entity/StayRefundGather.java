package com.dimeng.p2p.modules.bid.console.service.entity;

import java.math.BigDecimal;

public class StayRefundGather {
	/**
	 * 待还本金总额
	 */
	public BigDecimal dhAmount = new BigDecimal(0);

	/**
	 * 逾期待还总额
	 */
	public BigDecimal yqdhAmount = new BigDecimal(0);

	/**
	 * 严重逾期待还总额
	 */
	public BigDecimal yzyqdhAmount = new BigDecimal(0);
}
