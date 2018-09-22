package com.dimeng.p2p.modules.statistics.console.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
/**
 * 投资统计表
 */
public class TransferCreditorEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 债权ID
	 */
	public String creditorId;
	/**
	 * 借款ID，T6230.F01
	 */
	public String loanId;
	/**
	 * 剩余期限
	 */
	public String surplusLimit;
	/**
	 * 卖出账户
	 */
	public String sellAccount;
	/**
	 * 卖出姓名
	 */
	public String sellName;
	/**
	 * 申请卖出时间
	 */
	public Timestamp applyTime;
	/**
	 * 卖出时待收本息
	 */
	public BigDecimal dueInBX = BigDecimal.ZERO;
	/**
	 * 卖出时债权价值
	 */
	public BigDecimal creditorWorth = BigDecimal.ZERO;
	/**
	 * 成交金额
	 */
	public BigDecimal lastMoney = BigDecimal.ZERO;;
	/**
	 * 交易费率
	 */
	public BigDecimal dealRate = BigDecimal.ZERO;;
	/**
	 * 交易费用
	 */
	public BigDecimal dealMoney = BigDecimal.ZERO;;
	/**
	 * 转让盈亏
	 */
	public BigDecimal transferEarn = BigDecimal.ZERO;
	/**
	 * 买入账户
	 */
	public String buyAccount;
	/**
	 * 买入姓名
	 */
	public String buyName;
	/**
	 * 购买时间
	 */
	public Timestamp buyTime;
	/**
	 * 来源
	 */
	public String source;
    
    /**
     * 卖出账户类型（个人、机构、企业）
     */
    public String sellUserType;
    
    /**
     * 买入账户类型（个人、机构、企业）
     */
    public String buyUserType;
    
    /**
     * 转入盈亏
     */
    public BigDecimal IntoEarn = BigDecimal.ZERO;
}
