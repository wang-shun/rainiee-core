package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 标的车产信息表
 */
public class T6214 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 标的ID，参考(T6036.F01)
	 */
	public int F01;

	/**
	 * 汽车品牌
	 */
	public String F02;

	/**
	 * 车牌号码
	 */
	public String F03;

	/**
	 * 购车年份
	 */
	public int F04;

	/**
	 * 评估价格(元)
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 参考价格1
	 */
	public BigDecimal F06 = BigDecimal.ZERO;

	/**
	 * 参考价格1URL
	 */
	public String F07;

	/**
	 * 参考价格2
	 */
	public BigDecimal F08 = BigDecimal.ZERO;

	/**
	 * 参考价格2URL
	 */
	public String F09;

}
