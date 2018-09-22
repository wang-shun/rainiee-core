package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 线下债权转让表
 */
public class T6220 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 借款标ID
	 */
	public int F01;

	/**
	 * 线下债权转让人ID(关联T6010.F01)
	 */
	public int F02;

	/**
	 * 持有金额
	 */
	public BigDecimal F03 = BigDecimal.ZERO;

	/**
	 * 持有时间
	 */
	public Timestamp F04;

	/**
	 * 转让价格
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 债权转让标题
	 */
	public String F06;

	/**
	 * 债权价值
	 */
	public BigDecimal F07 = BigDecimal.ZERO;

	/**
	 * 剩余债权总额
	 */
	public BigDecimal F08 = BigDecimal.ZERO;

	/**
	 * 持有份数
	 */
	public int F09;

	/**
	 * 剩余份数
	 */
	public int F10;

	/**
	 * 预计收益
	 */
	public BigDecimal F11 = BigDecimal.ZERO;

}
