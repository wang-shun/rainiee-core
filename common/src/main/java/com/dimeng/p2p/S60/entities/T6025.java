package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 债权统计表
 */
public class T6025 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户账号ID,参考T6010.F01
	 */
	public int F01;

	/**
	 * 债权已赚金额
	 */
	public BigDecimal F02 = BigDecimal.ZERO;

	/**
	 * 利息收益
	 */
	public BigDecimal F03 = BigDecimal.ZERO;

	/**
	 * 债权转让盈亏
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 债权账户资产
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 回收中的债权数量
	 */
	public int F06;

	/**
	 * 投信用认证金额
	 */
	public BigDecimal F07 = BigDecimal.ZERO;

	/**
	 * 投机构担保金额
	 */
	public BigDecimal F08 = BigDecimal.ZERO;

	/**
	 * 投实地认证金额
	 */
	public BigDecimal F09 = BigDecimal.ZERO;

	/**
	 * 已赚信用认证金额
	 */
	public BigDecimal F10 = BigDecimal.ZERO;

	/**
	 * 已赚机构担保金额
	 */
	public BigDecimal F11 = BigDecimal.ZERO;

	/**
	 * 已赚实地认证金额
	 */
	public BigDecimal F12 = BigDecimal.ZERO;

	/**
	 * 待赚信用认证金额
	 */
	public BigDecimal F13 = BigDecimal.ZERO;

	/**
	 * 待赚机构担保金额
	 */
	public BigDecimal F14 = BigDecimal.ZERO;

	/**
	 * 待赚实地认证金额
	 */
	public BigDecimal F15 = BigDecimal.ZERO;

	/**
	 * 信用认证金额资产
	 */
	public BigDecimal F16 = BigDecimal.ZERO;

	/**
	 * 机构担保金额资产
	 */
	public BigDecimal F17 = BigDecimal.ZERO;

	/**
	 * 实地认证金额资产
	 */
	public BigDecimal F18 = BigDecimal.ZERO;

}
