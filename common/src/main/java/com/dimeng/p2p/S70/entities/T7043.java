package com.dimeng.p2p.S70.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S70.enums.T7043_F05;

/**
 * 成交数据统计-按月-按类型
 */
public class T7043 extends AbstractEntity {

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
	 * 类型（信用认证、机构担保、实地认证）
	 */
	public T7043_F05 F05;

	/**
	 * 更新时间
	 */
	public Timestamp F06;

}
