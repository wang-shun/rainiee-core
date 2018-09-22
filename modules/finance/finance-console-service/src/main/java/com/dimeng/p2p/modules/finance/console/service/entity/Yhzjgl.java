package com.dimeng.p2p.modules.finance.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Yhzjgl {

	/**
	 * 可用金额总额
	 */
	public BigDecimal usableAmount = new BigDecimal(0);
	/**
	 * 冻结金额
	 */
	public BigDecimal djAmount = new BigDecimal(0);
	/**
	 * 账户余额金额
	 */
	public BigDecimal userBalance = new BigDecimal(0);
	/**
	 * 用户总收益
	 */
	public BigDecimal userIncome = new BigDecimal(0);

	/**
	 * 借款负债总额
	 */
	public BigDecimal jkfzAmount = new BigDecimal(0);
	/**
	 * 优选理财资产总额
	 */
	public BigDecimal yxlcAmount = new BigDecimal(0);
	/**
	 * 债权资产总额
	 */
	public BigDecimal zqzcAmount = new BigDecimal(0);
	/**
	 * 更新时间
	 */
	public Timestamp updateTime;

}
