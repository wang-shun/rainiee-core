package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 我的借款统计表
 */
public class T6029 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户账号ID,参考T6010.F01
	 */
	public int F01;

	/**
	 * 借款总额
	 */
	public BigDecimal F02 = BigDecimal.ZERO;

	/**
	 * 已还金额
	 */
	public BigDecimal F03 = BigDecimal.ZERO;

	/**
	 * 借款管理费
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 待还本金
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 已还本金
	 */
	public BigDecimal F06 = BigDecimal.ZERO;

	/**
	 * 待还利息
	 */
	public BigDecimal F07 = BigDecimal.ZERO;

	/**
	 * 已还利息（借款利息）
	 */
	public BigDecimal F08 = BigDecimal.ZERO;

	/**
	 * 待还逾期费用
	 */
	public BigDecimal F09 = BigDecimal.ZERO;

	/**
	 * 已还逾期费用
	 */
	public BigDecimal F10 = BigDecimal.ZERO;

	/**
	 * 已还提前还款违约金
	 */
	public BigDecimal F11 = BigDecimal.ZERO;

	/**
	 * 待还借款管理费
	 */
	public BigDecimal F12 = BigDecimal.ZERO;

	/**
	 * 已还借款管理费
	 */
	public BigDecimal F13 = BigDecimal.ZERO;

	/**
	 * 待还逾期罚息
	 */
	public BigDecimal F14 = BigDecimal.ZERO;

}
