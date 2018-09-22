package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 持续投资奖励记录
 */
public class T6055 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 持续投资奖励记录自增ID
	 */
	public int F01;

	/**
	 * 推广用户ID,参考T6010.F01
	 */
	public int F02;

	/**
	 * 被推广用户ID,参考T6010.F01
	 */
	public int F03;

	/**
	 * 投资金额
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 奖励金额
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 投资时间
	 */
	public Timestamp F06;

}
