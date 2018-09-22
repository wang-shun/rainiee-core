package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 用户信用档案表
 */
public class T6045 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户账号ID,参考T6010.F01
	 */
	public int F01;

	/**
	 * 逾期次数
	 */
	public int F02;

	/**
	 * 严重逾期次数
	 */
	public int F03;

	/**
	 * 最长逾期天数
	 */
	public int F04;

	/**
	 * 待还金额
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 最后更新时间
	 */
	public Timestamp F06;

	/**
	 * 还清次数
	 */
	public int F07;

	/**
	 * 申请借款笔数
	 */
	public int F08;

	/**
	 * 成功借款笔数
	 */
	public int F09;

}
