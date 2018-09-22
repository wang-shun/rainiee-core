package com.dimeng.p2p.modules.spread.console.service.entity;

import java.math.BigDecimal;

/**
 * 参与人统计信息
 */
public class PartakeTotalInfo {
	/**
	 * 活动ID
	 */
	public int id;
	/**
	 * 总奖励
	 */
	public BigDecimal totalMoney = new BigDecimal(0);
	/**
	 * 总受益人数
	 */
	public int personNum;
}
