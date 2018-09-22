package com.dimeng.p2p.modules.account.console.service.entity;

import java.math.BigDecimal;

import com.dimeng.p2p.common.enums.UserType;

public class YhzjglRecord {

	public int id;
	/**
	 * 用户名
	 */
	public String loginName;
	/**
	 * 姓名
	 */
	public String userName;
	/**
	 * 用户类型
	 */
	public UserType userType;
	/**
	 * 可用金额
	 */
	public BigDecimal usableAmount=new BigDecimal(0);
	/**
	 * 冻结金额
	 */
	public BigDecimal blockAmount=new BigDecimal(0);
	/**
	 * 账户余额金额
	 */
	public BigDecimal userBalance=new BigDecimal(0);
	/**
	 * 债权资产
	 */
	public BigDecimal zqAmount=new BigDecimal(0);
	/**
	 * 优选理财资产
	 */
	public BigDecimal yxlcAmount=new BigDecimal(0);
	/**
	 * 借款负债
	 */
	public BigDecimal jkhzAmount=new BigDecimal(0);
	/**
	 * 用户总收益
	 */
	public BigDecimal userIncome=new BigDecimal(0);
}
