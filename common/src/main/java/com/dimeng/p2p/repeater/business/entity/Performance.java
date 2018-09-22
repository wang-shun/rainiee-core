package com.dimeng.p2p.repeater.business.entity;

import java.sql.Date;

public class Performance {

	/**
	 * 业务员Id
	 */
	public int userId;
	/**
	 * 业务员姓名
	 */
	public String name;
	/**
	 * 业务员工号
	 */
	public String employNum;
	/**
	 * 名下客户数
	 */
	public int customerNumber;
	
	/**
	 * 一级名下客户数
	 */
	public int levelCustomerNumber;
	
	/**
	 * 二级名下客户数
	 */
	public int secondaryCustomerNumber;
	
	/**
	 * 累计投资金额
	 */
	public double investmentAmount; 
	
	/**
	 * 累计借款金额
	 */
	public double loanAmount;
	/**
	 * 累计充值金额
	 */
	public double chargeAmount;
	/**
	 * 累计提现金额
	 */
	public double withdrawAmount;
	/**
	 * 一级投资金额共计
	 */
	public double levelInvestmentAmount; 
	/**
	 * 二级投资金额共计
	 */
	public double secondaryInvestmentAmount; 
	/**
	 * 一级借款金额共计
	 */
	public double levelLoanAmount;
	/**
	 * 二级借款金额共计
	 */
	public double secondaryLoanAmount;

	/**
	 * 一级用户充值金额
	 */
	public double levelChargeAmount;

	/**
	 * 二级用户充值金额
	 */
	public double secondChargeAmount;
	/**
	 * 一级用户提现金额
	 */
	public double levelWithDrawAmount;
	/**
	 * 二级用户提现金额
	 */
	public double secondWithDrawAmount;
	/**
	 * 时间
	 */
	public Date F01;
	/**
	 * 一级投资金额记录    
	 */
	public double F02;
	/**
	 * 一级借款记录
	 */
	public double F03;
	
	/**
	 * 二级投资金额记录
	 */
	public double F04; 
	/**
	 * 二级借款记录   
	 */
	public double F05; 
	
}
