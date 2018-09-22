package com.dimeng.p2p.S70.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 平台资金统计-按季度
 */
public class T7037 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 年度
	 */
	public int F01;

	/**
	 * 季度
	 */
	public short F02;

	/**
	 * 收入
	 */
	public BigDecimal F03 = BigDecimal.ZERO;

	/**
	 * 支出
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 盈亏
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 更新时间
	 */
	public Timestamp F06;

}
