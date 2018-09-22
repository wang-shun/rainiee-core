package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;

/**
 * 借款统计信息实体类
 *
 */
public class LoanCount {
	/**
	 * 借款总金额
	 */
	public BigDecimal countMoney = new BigDecimal(0);
	/**
	 * 逾期金额
	 */
	public BigDecimal overdueMoney = new BigDecimal(0);
	/**
	 * 待还金额
	 */
	public BigDecimal repayMoney = new BigDecimal(0);
	/**
	 * 近30天应还金额
	 */
	public BigDecimal newRepayMoney = new BigDecimal(0);
}

