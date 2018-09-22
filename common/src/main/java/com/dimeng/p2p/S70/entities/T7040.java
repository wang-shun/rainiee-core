package com.dimeng.p2p.S70.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 风险保证金统计-按年度
 */
public class T7040 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 年度
	 */
	public int F01;

	/**
	 * 更新时间
	 */
	public Timestamp F02;

	/**
	 * 垫付
	 */
	public BigDecimal F03 = BigDecimal.ZERO;

	/**
	 * 垫付返还
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 借款成交服务费
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 手动增加保证金
	 */
	public BigDecimal F06 = BigDecimal.ZERO;

	/**
	 * 手动扣除保证金
	 */
	public BigDecimal F07 = BigDecimal.ZERO;

	/**
	 * 机构ID 如果为0代表平台
	 */
	public int F08;

}
