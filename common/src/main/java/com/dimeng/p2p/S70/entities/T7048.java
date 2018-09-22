package com.dimeng.p2p.S70.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 优选理财计划-统计
 */
public class T7048 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 累计计划总金额
	 */
	public BigDecimal F01 = BigDecimal.ZERO;

	/**
	 * 为用户累计赚取
	 */
	public BigDecimal F02 = BigDecimal.ZERO;

	/**
	 * 累计实际总金额
	 */
	public BigDecimal F03 = BigDecimal.ZERO;

	/**
	 * 加入总人数
	 */
	public int F04;

	/**
	 * 加权平均收益率
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 最后更新时间
	 */
	public Timestamp F06;

}
