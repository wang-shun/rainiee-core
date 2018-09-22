package com.dimeng.p2p.modules.bid.user.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 理财统计-统计总数实体类
 * 
 */
public class EarnFinancingTotal implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 累计收益
	 */
	public BigDecimal ljsy = new BigDecimal(0);
	/**
	 * 累计投资金额
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
