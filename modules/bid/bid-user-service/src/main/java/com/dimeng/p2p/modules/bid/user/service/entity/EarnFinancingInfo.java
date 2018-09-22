package com.dimeng.p2p.modules.bid.user.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 理财统计-实体类
 *
 */
public class EarnFinancingInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 年
	 */
	public int year;

	/**
	 * 月
	 */
	public int month;
	/**
	 * 收益
	 */
	public BigDecimal ljsy = new BigDecimal(0);
	/**
	 * 投资金额
	 */
	public BigDecimal ljtzje = new BigDecimal(0);
	/**
	 * 已回收金额
	 */
	public BigDecimal yhsje = new BigDecimal(0);
	/**
	 * 待回收金额
	 */
	public BigDecimal dhsje = new BigDecimal(0);


}
