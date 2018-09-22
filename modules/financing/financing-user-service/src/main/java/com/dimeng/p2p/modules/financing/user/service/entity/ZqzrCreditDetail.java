package com.dimeng.p2p.modules.financing.user.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class ZqzrCreditDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 借款标id
	 */
	public int id;
	/**
	 * 借款人姓名
	 */
	public String bName;
	/**
	 * 借款本金数额
	 */
	public BigDecimal amount = new BigDecimal(0);
	/**
	 * 借款年化利率
	 */
	public BigDecimal rate = new BigDecimal(0);
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
	 * 月偿还本息数额
	 */
	public BigDecimal monthAmount = new BigDecimal(0);
}
