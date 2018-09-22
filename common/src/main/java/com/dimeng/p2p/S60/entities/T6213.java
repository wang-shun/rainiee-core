package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 标的房产信息表
 */
public class T6213 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 标的ID，参考(T6036.F01)
	 */
	public int F01;

	/**
	 * 小区名称
	 */
	public String F02;

	/**
	 * 建筑面积(m2)
	 */
	public float F03;

	/**
	 * 使用年限
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
