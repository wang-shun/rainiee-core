package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;
import java.sql.Date;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6057_F08;

/**
 * 优选理财债权人还款记录表
 */
public class T6057 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 自增ID
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
	 * 用户ID,参考T6010.F01
	 */
	public int F04;

	/**
	 * 还本金
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 还利息
	 */
	public BigDecimal F06 = BigDecimal.ZERO;

	/**
	 * 应还日期
	 */
	public Date F07;

	/**
	 * 还款状态,WH:未还;YH:已还
	 */
	public T6057_F08 F08;

}
