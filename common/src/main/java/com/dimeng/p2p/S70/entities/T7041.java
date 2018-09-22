package com.dimeng.p2p.S70.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 充值提现数据统计-按月
 */
public class T7041 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 年份
	 */
	public int F01;

	/**
	 * 月份
	 */
	public short F02;

	/**
	 * 充值金额
	 */
	public BigDecimal F03 = BigDecimal.ZERO;

	/**
	 * 提现金额
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 充值笔数
	 */
	public int F05;

	/**
	 * 提现笔数
	 */
	public int F06;

	/**
	 * 更新时间
	 */
	public Timestamp F07;

}
