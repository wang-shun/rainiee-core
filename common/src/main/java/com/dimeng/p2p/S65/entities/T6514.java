package com.dimeng.p2p.S65.entities;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S65.enums.T6514_F07;

/**
 * 垫付订单
 */
public class T6514 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 订单ID,参考T6501.F01
	 */
	public int F01;

	/**
	 * 标ID,参考T6230.F01
	 */
	public int F02;

	/**
	 * 债权ID,参考T6251.F01
	 */
	public int F03;

	/**
	 * 垫付人ID,参考T6110.F01
	 */
	public int F04;

	/**
	 * 垫付金额
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 交易类型ID,参考T5122.F01
	 */
	public int F06;

	/**
	 * 是否垫付,S:是;F:否;
	 */
	public T6514_F07 F07;

	/**
	 * 垫付期数
	 */
	public int F08;

}
