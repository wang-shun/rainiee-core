package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 推广奖励统计
 */
public class T6053 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 推广用户ID,参考T6010.F01
	 */
	public int F01;

	/**
	 * 推广数
	 */
	public int F02;

	/**
	 * 推广投资金额
	 */
	public BigDecimal F03 = BigDecimal.ZERO;

	/**
	 * 推广持续奖励金额
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 有效推广奖励金额
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

}
