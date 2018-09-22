package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;
import java.sql.Date;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6044_F07;

/**
 * 优选理财还款记录表
 */
public class T6044 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 优选理财还款记录表自增ID
	 */
	public int F01;

	/**
	 * 优选理财计划表ID,参考T6042.F01
	 */
	public int F02;

	/**
	 * 期号
	 */
	public int F03;

	/**
	 * 还本金
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 还利息
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 应还日期
	 */
	public Date F06;

	/**
	 * 还款状态,WH:未还;YH:已还
	 */
	public T6044_F07 F07;

}
