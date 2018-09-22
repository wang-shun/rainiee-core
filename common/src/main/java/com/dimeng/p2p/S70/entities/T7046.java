package com.dimeng.p2p.S70.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 活动信息表
 */
public class T7046 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 活动自增ID
	 */
	public int F01;

	/**
	 * 活动主题
	 */
	public String F02;

	/**
	 * 开始时间
	 */
	public Timestamp F03;

	/**
	 * 结束时间
	 */
	public Timestamp F04;

	/**
	 * 发布人,参考T7011.F01
	 */
	public int F05;

	/**
	 * 活动描述
	 */
	public String F06;

	/**
	 * 活动费用
	 */
	public BigDecimal F07 = BigDecimal.ZERO;

	/**
	 * 参与人数
	 */
	public int F08;

	/**
	 * 受益人数
	 */
	public int F09;

	/**
	 * 创建时间
	 */
	public Timestamp F10;

}
