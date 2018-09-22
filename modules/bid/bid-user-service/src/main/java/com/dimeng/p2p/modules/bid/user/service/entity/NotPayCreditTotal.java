package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;

/**
 * 待还统计实体类
 *
 */
public class NotPayCreditTotal {
	/**
	 * 待还本金
	 */
	public BigDecimal notPayMoney = new BigDecimal(0);
	/**
	 * 待还利息
	 */
	public BigDecimal notPayAccMoney = new BigDecimal(0);
	/**
	 * 待还逾期费用
	 */
	public BigDecimal notPayArrMoney = new BigDecimal(0);
	/**
	 * 待还借款管理费
	 */
	public BigDecimal notPayManageMoney = new BigDecimal(0);
	/**
	 * 待还总额
	 */
	public BigDecimal notPayTotal = new BigDecimal(0);
}
