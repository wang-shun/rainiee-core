package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 用户信用信息表
 */
public class T6021 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户帐号ID,参考T6010.F01
	 */
	public int F01;

	/**
	 * 信用积分
	 */
	public int F02;

	/**
	 * 总额度
	 */
	public BigDecimal F03 = BigDecimal.ZERO;

	/**
	 * 可用额度
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

}
