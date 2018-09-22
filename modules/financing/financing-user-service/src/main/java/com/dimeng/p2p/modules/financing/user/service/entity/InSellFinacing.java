package com.dimeng.p2p.modules.financing.user.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 *已转入的债权 
 *
 */
public class InSellFinacing {
	/**
	 * 借款标ID
	 */
	public int jkbId;
	/**
	 * 转让债权ID
	 */
	public int assestsID;
	/**
	 * 剩余期数
	 */
	public int overNum;
	/**
	 * 借款期数
	 */
	public int jkNum;
	/**
	 * 年化利率
	 */
	public BigDecimal rate = new BigDecimal(0);
	/**
	 * 转入时债权价值
	 */
	public BigDecimal inValue = new BigDecimal(0);
	/**
	 * 债权价值
	 */
	public BigDecimal zqjg = new BigDecimal(0);
	
	/**
	 * 转入份数
	 */
	public int inNum;
	/**
	 * 交易金额
	 */
	public BigDecimal busMoney = new BigDecimal(0);
	/**
	 * 盈亏
	 */
	public BigDecimal money = new BigDecimal(0);
	/**
	 * 转入时间
	 */
	public Timestamp inTime;
	/**
	 * 转入id
	 */
	public int inId;
	
}
