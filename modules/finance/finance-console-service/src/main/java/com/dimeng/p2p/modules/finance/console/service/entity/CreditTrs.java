package com.dimeng.p2p.modules.finance.console.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 债权转让记录
 */
public class CreditTrs implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 债权买入者id
	 */
	public int inId;
	/**
	 * 债权买入者
	 */
	public String in;
	/**
	 * 债权卖出者id
	 */
	public int outId;
	/**
	 * 债权卖出者
	 */
	public String out;
	/**
	 * 交易金额
	 */
	public BigDecimal amount = new BigDecimal(0);
	/**
	 * 交易时间
	 */
	public Timestamp time;
}
