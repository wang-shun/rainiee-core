package com.dimeng.p2p.S70.entities;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 法人用户资金统计表
 */
public class T7203 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 年
	 */
	public int F01;

	/**
	 * 月
	 */
	public int F02;

	/**
	 * 投资金额
	 */
	public BigDecimal F03 = BigDecimal.ZERO;

	/**
	 * 债权转让盈亏
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 已收本金
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 已收利息
	 */
	public BigDecimal F06 = BigDecimal.ZERO;

	/**
	 * 已收罚息
	 */
	public BigDecimal F07 = BigDecimal.ZERO;

	/**
	 * 已收违约金
	 */
	public BigDecimal F08 = BigDecimal.ZERO;

	/**
	 * 待收本金
	 */
	public BigDecimal F09 = BigDecimal.ZERO;

	/**
	 * 待收利息
	 */
	public BigDecimal F10 = BigDecimal.ZERO;

	/**
	 * 待收罚息
	 */
	public BigDecimal F11 = BigDecimal.ZERO;

	/**
	 * 用户ID
	 */
	public int F12;
    
    /**
     * 理财管理费
     */
    public BigDecimal F13 = BigDecimal.ZERO;

}
