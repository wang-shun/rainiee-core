package com.dimeng.p2p.modules.financing.user.service.entity;

import java.math.BigDecimal;

/**
 * 已转出的债权
 *
 */
public class OutSellFinacing {
	/**
	 * 借款标ID
	 */
	public int jkbId;
	/**
	 * 转让债权ID
	 */
	public int asstestsID;
	/**
	 * 债权转入id
	 */
	public int inId;
	/**
	 * 转出份数
	 */
	public int zcNum;
	/**
	 * 成交份数
	 */
	public int outNum;
	/**
	 * 转出时债权总价值
	 */
	public BigDecimal outTotalValue = new BigDecimal(0);
	/**
	 * 转出时总成交金额
	 */
	public BigDecimal outTotalMoney = new BigDecimal(0);
	/**
	 * 交易费用
	 */
	public BigDecimal cost = new BigDecimal(0);
	/**
	 * 实际收入
	 */
	public BigDecimal realityMoney = new BigDecimal(0);
	/**
	 * 盈亏
	 */
	public BigDecimal money = new BigDecimal(0);
	/**
	 * 债权价格
	 */
	public BigDecimal zqjg = new BigDecimal(0);
	/**
	 * 转让价格
	 */
	public BigDecimal zrjg = new BigDecimal(0);
}
