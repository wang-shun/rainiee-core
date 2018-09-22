package com.dimeng.p2p.modules.financing.user.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 已截止的优选理财计划
 * 
 */
public class EndBestFinacing {
	/**
	 * 计划名称
	 */
	public String name;
	/**
	 * 加入金额
	 */
	public BigDecimal addMoney = new BigDecimal(0);
	/**
	 * 年化利率
	 */
	public double rate;
	/**
	 * 回收金额
	 */
	public BigDecimal money = new BigDecimal(0);
	/**
	 * 已赚金额
	 */
	public BigDecimal takeMoney = new BigDecimal(0);
	/**
	 * 截止时间
	 */
	public Timestamp endTime;
	/**
	 * 优选理财ID
	 */
	public int planId;
}
