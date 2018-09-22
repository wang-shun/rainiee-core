package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;

/**
 * 还款信息实体类
 * 
 */
public class RepayInfo {
	/**
	 * 还款标ID
	 */
	public int loanID;
	/**
	 * 当期还款总需
	 */
	public BigDecimal loanTotalMoney = new BigDecimal(0);
	/**
	 * 用户账户余额
	 */
	public BigDecimal accountAmount = new BigDecimal(0);
	/**
	 * 当期应还本息
	 */
	public BigDecimal loanMustMoney = new BigDecimal(0);
	/**
	 * 当期应还本金
	 */
	public BigDecimal yhbj = new BigDecimal(0);
	/**
	 * 当期借款管理费
	 */
	public BigDecimal loanManageAmount = new BigDecimal(0);
	/**
	 * 逾期罚息
	 */
	public BigDecimal overdueInterest = new BigDecimal(0);
	/**
	 * 逾期管理费
	 */
	public BigDecimal overdueManage = new BigDecimal(0);
	/**
	 * 逾期费用
	 */
	public BigDecimal loanArrMoney = new BigDecimal(0);
	/**
	 * 当前期号
	 */
	public int number;
}
