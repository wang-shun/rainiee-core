package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;

/**
 * 借款统计实体类。
 *
 */
public class CreditTotal {
	/**
	 * 借款总额
	 */
	public BigDecimal total = new BigDecimal(0);
	/**
	 * 总计需支付
	 */
	public BigDecimal totalPay = new BigDecimal(0);
	/**
	 * 借款利息
	 */
	public BigDecimal accMoney = new BigDecimal(0);
	/**
	 * 逾期费用
	 */
	public BigDecimal arrMoney = new BigDecimal(0);
	/**
	 * 提前还款违约金
	 */
	public BigDecimal beforeMoney = new BigDecimal(0);
	/**
	 * 借款管理费
	 */
	public BigDecimal manageMoney = new BigDecimal(0);
	/**
	 * 总计
	 */
	public BigDecimal totalMoney;  
}
