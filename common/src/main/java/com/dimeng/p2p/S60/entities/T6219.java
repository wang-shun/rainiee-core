package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 标的企业财务状况表
 */
public class T6219 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 借款标ID
	 */
	public int F01;

	/**
	 * 年份
	 */
	public int F02;

	/**
	 * 主营收入（元）
	 */
	public BigDecimal F03 = BigDecimal.ZERO;

	/**
	 * 毛利率（元）
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 净利润（元）
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 总资产（元）
	 */
	public BigDecimal F06 = BigDecimal.ZERO;

	/**
	 * 净资产（元）
	 */
	public BigDecimal F07 = BigDecimal.ZERO;

	/**
	 * 备注
	 */
	public String F08;

}
