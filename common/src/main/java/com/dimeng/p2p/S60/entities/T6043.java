package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 优选理财持有人表
 */
public class T6043 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 优选理财持有人表自增ID
	 */
	public int F01;

	/**
	 * 优选理财计划表ID,参考T6042.F01
	 */
	public int F02;

	/**
	 * 持有人ID,参考T6010.F01
	 */
	public int F03;

	/**
	 * 加入金额
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 加入时间
	 */
	public Timestamp F05;

	/**
	 * 最后更新时间
	 */
	public Timestamp F06;

	/**
	 * 待收本息
	 */
	public BigDecimal F07 = BigDecimal.ZERO;

	/**
	 * 回收金额
	 */
	public BigDecimal F08 = BigDecimal.ZERO;

	/**
	 * 已赚金额
	 */
	public BigDecimal F09 = BigDecimal.ZERO;

}
