package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 优选计划统计表
 */
public class T6026 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户账号ID,参考T6010.F01
	 */
	public int F01;

	/**
	 * 已赚总金额
	 */
	public BigDecimal F02 = BigDecimal.ZERO;

	/**
	 * 优选理财账户资产
	 */
	public BigDecimal F03 = BigDecimal.ZERO;

	/**
	 * 平均收益率
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 持有中的计划数量
	 */
	public int F05;

	/**
	 * 加入费用
	 */
	public BigDecimal F06 = BigDecimal.ZERO;

	/**
	 * 优选理财投资金额
	 */
	public BigDecimal F07 = BigDecimal.ZERO;

}
