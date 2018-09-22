package com.dimeng.p2p.modules.spread.console.service.entity;

import com.dimeng.p2p.S62.entities.T6230;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class BonusList extends T6230 {

	private static final long serialVersionUID = 1L;

	/**
	 * 借款用户名
	 */
	public String loanUserName;

	/**
	 * 放款时间
	 */
	public Timestamp fkTime;

	/**
	 * 投资帐户
	 */
	public String investUserName;

	/**
	 * 投资金额
	 */
	public BigDecimal investAmount = BigDecimal.ZERO;

	/**
	 * 奖励利率
	 */
	public BigDecimal jlRate = BigDecimal.ZERO;

	/**
	 * 奖励金额
	 */
	public BigDecimal jlAmount = BigDecimal.ZERO;

	/**
	 * 借款期数
	 */
	public int total;

	/**
	 * 是否按天借款
	 */
	public String day;
}
