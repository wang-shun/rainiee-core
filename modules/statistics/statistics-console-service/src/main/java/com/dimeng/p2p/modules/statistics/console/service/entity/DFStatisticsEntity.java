package com.dimeng.p2p.modules.statistics.console.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
/**
 * 垫付统计表
 */
public class DFStatisticsEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 借款ID，T6230.F01
	 */
	public String loanId;
	
	/**
	 * 期数
	 */
	public String periods;
	
	/**
	 * 合约还款日期
	 */
	public Timestamp shouldTheDate;
	
	/**
	 * 当期应还金额
	 */
	public BigDecimal shouldTheMoney = BigDecimal.ZERO;
	
	/**
	 * 垫付账户名账号名
	 */
	public String dfAccount;
	
	/**
	 * 垫付机构名称
	 */
	public String dfAccountName;
	
	/**
	 * 实际垫付金额
	 */
	public BigDecimal actualMoney = BigDecimal.ZERO;
	
	/**
	 * 垫付时间
	 */
	public Timestamp dfTime;
	
	/**
	 * 返还金额
	 */
	public BigDecimal reMoney = BigDecimal.ZERO;
	
	/**
	 * 返还时间
	 */
	public Timestamp reMoneyTime ;
	
	/**
	 * 垫付盈亏
	 */
	public BigDecimal dfEarn;

	/**
	 * 借款人ID
	 */
	public String jkrAccount;
	
}
