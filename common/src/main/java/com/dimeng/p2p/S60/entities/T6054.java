package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 首次充值奖励记录
 */
public class T6054 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 首次充奖励自增ID
	 */
	public int F01;

	/**
	 * 推广用户ID,,参考T6010.F01
	 */
	public int F02;

	/**
	 * 被推广用户ID,参考T6010.F01
	 */
	public int F03;

	/**
	 * 首次充值金额
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 奖励金额
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 首次充值时间
	 */
	public Timestamp F06;

}
