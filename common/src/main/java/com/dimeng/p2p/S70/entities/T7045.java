package com.dimeng.p2p.S70.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 成交数据统计-按月-按地域
 */
public class T7045 extends AbstractEntity {

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
	 * 成交金额金额
	 */
	public BigDecimal F03 = BigDecimal.ZERO;

	/**
	 * 成交笔数
	 */
	public int F04;

	/**
	 * 地域代码
	 */
	public int F05;

	/**
	 * 更新时间
	 */
	public Timestamp F06;

}
