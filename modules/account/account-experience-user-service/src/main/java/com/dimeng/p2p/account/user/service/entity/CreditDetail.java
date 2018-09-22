package com.dimeng.p2p.account.user.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 标的详情
 */
public class CreditDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 借款用途
	 */
	public String use;
	/**
	 * 借款数额
	 */
	public BigDecimal amount = new BigDecimal(0);
	/**
	 * 借款期限
	 */
	public int limitMonth;
	/**
	 * 起始时间
	 */
	public Timestamp startDate;
	/**
	 * 结束时间
	 */
	public Timestamp endDate;
	/**
	 * 偿还利息数额
	 */
	public BigDecimal monthAmount = new BigDecimal(0);
	/**
	 * 借款年化利率
	 */
	public BigDecimal rate = new BigDecimal(0);
}
