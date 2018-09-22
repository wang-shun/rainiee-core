package com.dimeng.p2p.S70.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S70.enums.T7028_F06;

/**
 * 平台保证金流水表
 */
public class T7028 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 平台保证金流水表自增ID
	 */
	public int F01;

	/**
	 * 时间
	 */
	public Timestamp F02;

	/**
	 * 收入
	 */
	public BigDecimal F03 = BigDecimal.ZERO;

	/**
	 * 支出
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 余额
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 类型,DF:垫付;DFHF:垫付返还;JKCJFWF:借款成交服务费;SDZJBZJ:手动增加保证金;SDKCBZJ:手动扣除保证金;
	 */
	public T7028_F06 F06;

	/**
	 * 备注
	 */
	public String F07;

	/**
	 * 引用ID,根据类型引用不同表ID
	 */
	public int F08;

	/**
	 * 用户ID,参考T6010.F01
	 */
	public int F09;

}
