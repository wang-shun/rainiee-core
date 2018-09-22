package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 标的企业信息表
 */
public class T6218 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 标ID
	 */
	public int F01;

	/**
	 * 注册年限
	 */
	public int F02;

	/**
	 * 注册资金(元)
	 */
	public BigDecimal F03 = BigDecimal.ZERO;

	/**
	 * 资产净值(元)
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 上年度经营现金流入(元)
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 行业
	 */
	public String F06;

	/**
	 * 经营情况
	 */
	public String F07;

	/**
	 * 涉诉情况
	 */
	public String F08;

	/**
	 * 征信记录
	 */
	public String F09;

	/**
	 * 融资记录
	 */
	public String F10;

}
