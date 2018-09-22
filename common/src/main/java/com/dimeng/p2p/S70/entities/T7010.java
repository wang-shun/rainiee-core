package com.dimeng.p2p.S70.entities;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 首页统计表
 */
public class T7010 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 今日注册用户数
	 */
	public int F01;

	/**
	 * 历史注册用户总数
	 */
	public int F02;

	/**
	 * 今日登录用户数
	 */
	public int F03;

	/**
	 * 今日用户总充值
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 用户历史总充值
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 今日用户总提现
	 */
	public BigDecimal F06 = BigDecimal.ZERO;

	/**
	 * 用户历史总提现
	 */
	public BigDecimal F07 = BigDecimal.ZERO;

	/**
	 * 平台总收益
	 */
	public BigDecimal F08 = BigDecimal.ZERO;

	/**
	 * 用户投资总收益
	 */
	public BigDecimal F09 = BigDecimal.ZERO;

}
