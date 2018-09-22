package com.dimeng.p2p.modules.financing.user.service.entity;

import java.math.BigDecimal;

/**
 * 转让中的债权
 *
 */
public class SellFinacing {
	/**
	 * 借款标ID
	 */
	public int jkbId;
	/**
	 * 转出表ID
	 */
	public int zcbId;
	/**
	 * 转让ID
	 */
	public int sellID;
	/**
	 * 债权ID
	 */
	public int assetsID;
	/**
	 * 剩余期数
	 */
	public int num;
	/**
	 * 借款期数
	 */
	public int jkTime;
	/**
	 * 年化利率
	 */
	public double rate;
	/**
	 * 债权价值
	 */
	public BigDecimal assetsValue = new BigDecimal(0);
	/**
	 * 转让价格
	 */
	public BigDecimal assetsMoney = new BigDecimal(0);
	/**
	 * 剩余份数
	 */
	public int overNum;
}
