package com.dimeng.p2p.modules.finance.console.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Tg implements Serializable {
	private static final long serialVersionUID = 1L;
	public int id;

	/**
	 * 推广用户ID
	 */
	public int tgId;

	/**
	 * 被推广用户ID
	 */
	public int btgId;
	/**
	 * 投资金额
	 */
	public BigDecimal amount = new BigDecimal(0);
	/**
	 * 奖励金额
	 */
	public BigDecimal jlAmount = new BigDecimal(0);

	/**
	 * 推广码
	 */
	public String code;
	/**
	 * 投资时间
	 */
	public Timestamp time;
	/**
	 * 借款标题
	 */
	public String title;
}
