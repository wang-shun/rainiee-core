package com.dimeng.p2p.S70.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 风险保证金统计-按季度
 */
public class T7039 extends AbstractEntity {

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

	/**
	 * 机构ID 如果为0为平台
	 */
	public int F07;

}
