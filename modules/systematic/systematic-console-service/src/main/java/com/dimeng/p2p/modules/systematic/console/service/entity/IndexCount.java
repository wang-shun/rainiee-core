package com.dimeng.p2p.modules.systematic.console.service.entity;

import java.math.BigDecimal;

public class IndexCount {
    
	/**
	 * 今日注册用户数
	 */
	public int todayRegisterUser;

	/**
	 * 历史注册用户总数
	 */
	public int historyRegisterUser;

	/**
	 * 今日登录用户数
	 */
	public int todayLoginUser;

	/**
	 * 当前在线用户数
	 */
	public int onlineUser;

	/**
	 * 今日用户总充值
	 */
	public BigDecimal todayTotalUserRecharge = BigDecimal.ZERO;

	/**
	 * 用户历史总充值
	 */
	public BigDecimal historyTotalUserRecharge = BigDecimal.ZERO;

	/**
	 * 今日用户总提现
	 */
	public BigDecimal todayTotalUserExtract = BigDecimal.ZERO;

	/**
	 * 用户历史总提现
	 */
	public BigDecimal historyTotalUserExtract = BigDecimal.ZERO;

	/**
	 * 平台总收益
	 */
	public BigDecimal platformTotalIncome = BigDecimal.ZERO;

	/**
	 * 用户投资总收益
	 */
	public BigDecimal userInvestTotalIncome = BigDecimal.ZERO;
	
	/**
	 * 今日用户待还总金额
	 */
	public BigDecimal todayTotalUserToRepay = BigDecimal.ZERO;
	
	/**
	 * 用户待还总金额
	 */
	public BigDecimal totalUserToRepay = BigDecimal.ZERO;

}
