package com.dimeng.p2p.S70.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S70.enums.T7030_F07;

/**
 * 机构风险备用金流水表
 */
public class T7030 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 机构风险备用金表自增ID
	 */
	public int F01;

	/**
	 * 机构信息表ID,参考T7029.F01
	 */
	public int F02;

	/**
	 * 时间
	 */
	public Timestamp F03;

	/**
	 * 收入
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 支出
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 余额
	 */
	public BigDecimal F06 = BigDecimal.ZERO;

	/**
	 * 类型,DF:垫付;DFHF:垫付返还;JKCJFWF:借款成交服务费;SDZJBZJ:手动增加保证金;SDKCBZJ:手动扣除保证金;
	 */
	public T7030_F07 F07;

	/**
	 * 备注
	 */
	public String F08;

	/**
	 * 引用ID,具体参考类型引用表ID
	 */
	public int F09;

	/**
	 * 用户ID,参考T6010.F01
	 */
	public int F10;

}
