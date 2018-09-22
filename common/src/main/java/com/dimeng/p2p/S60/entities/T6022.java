package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 用户信用额度调整记录
 */
public class T6022 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户信用额度变更记录自增ID
	 */
	public int F01;

	/**
	 * 用户帐号ID,参考T6010.F01
	 */
	public int F02;

	/**
	 * 调整人ID,参考T7011.F01
	 */
	public int F03;

	/**
	 * 调整前总额度
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 调整后总额度
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 调整时间
	 */
	public Timestamp F06;

}
