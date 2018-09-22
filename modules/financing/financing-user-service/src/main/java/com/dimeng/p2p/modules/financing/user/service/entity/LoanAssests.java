package com.dimeng.p2p.modules.financing.user.service.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.dimeng.p2p.common.enums.CreditLevel;

/**
 * 投资中的债权
 * 
 */
public class LoanAssests {
	/**
	 * 债权ID
	 */
	public int assestsID;
	/**
	 * 原始投资金额
	 */
	public BigDecimal originalMoney = new BigDecimal(0);
	/**
	 * 年化利率
	 */
	public double rate;
	/**
	 * 期限
	 */
	public int assestsNum;
	/**
	 * 信用等级
	 */
	public CreditLevel creditLevel;
	/**
	 * 剩余时间
	 */
	public String surTime;
	/**
	 * 筹款时间
	 */
	public int ckTime;
	/**
	 * 审核时间
	 */
	public Date shTime;
	/**
	 * 进度
	 */
	public double progress;
	/**
	 * 借款标ID
	 */
	public int jkbId;
}
