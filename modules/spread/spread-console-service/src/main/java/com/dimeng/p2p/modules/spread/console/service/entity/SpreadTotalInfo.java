package com.dimeng.p2p.modules.spread.console.service.entity;

import java.math.BigDecimal;

/**
 *推广奖励概要统计信息
 *
 */
public class SpreadTotalInfo {
	/**
	 * 推广人总数
	 */
	public int personNum;
	/**
	 * 被推广人总数
	 */
	public int spreadPersonNum;
	/**
	 * 被推广人投资总额
	 */
	public BigDecimal totalMoney = new BigDecimal(0);
	/**
	 *  推广返利总额
	 */
	public BigDecimal returnMoney = new BigDecimal(0);
}
