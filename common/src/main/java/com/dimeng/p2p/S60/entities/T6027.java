package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 债权转让统计表
 */
public class T6027 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户账号ID,参考T6010.F01
	 */
	public int F01;

	/**
	 * 债权转让盈亏
	 */
	public BigDecimal F02 = BigDecimal.ZERO;

	/**
	 * 成功转入金额
	 */
	public BigDecimal F03 = BigDecimal.ZERO;

	/**
	 * 债权转入盈亏
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 已转入债权笔数
	 */
	public int F05;

	/**
	 * 成功转出金额
	 */
	public BigDecimal F06 = BigDecimal.ZERO;

	/**
	 * 债权转出盈亏
	 */
	public BigDecimal F07 = BigDecimal.ZERO;

	/**
	 * 已转出债权笔数
	 */
	public int F08;

}
