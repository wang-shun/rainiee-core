package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;

/**
 * 已还借款实体类
 *
 */
public class PayCreditTotal {
	/**
	 * 已还本金
	 */
	public BigDecimal payMoney = new BigDecimal(0);
	/**
	 * 已还利息
	 */
	public BigDecimal payAccMoney = new BigDecimal(0);
	/**
	 * 已交逾期费用
	 */
	public BigDecimal payArrMoney = new BigDecimal(0);
	/**
	 * 已交提前还款违约金
	 */
	public BigDecimal payBeforeMoney = new BigDecimal(0);
	/**
	 * 已交借款管理费
	 */
	public BigDecimal payManageMoney = new BigDecimal(0);
	/**
	 * 已还总额
	 */
	public BigDecimal payTotalMoney = new BigDecimal(0);
}
