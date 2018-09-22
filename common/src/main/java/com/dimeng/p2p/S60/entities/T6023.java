package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 用户账号信息
 */
public class T6023 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户账号ID,参考T6010.F01
	 */
	public int F01;

	/**
	 * 交易密码
	 */
	public String F02;

	/**
	 * 余额
	 */
	public BigDecimal F03 = BigDecimal.ZERO;

	/**
	 * 冻结金额
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 可用金额
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

}
