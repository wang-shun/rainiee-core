package com.dimeng.p2p.modules.spread.console.service.entity;

import java.math.BigDecimal;
/**
 * 活动统计信息
 */
public class ActivityTotalInfo {
	/**
	 * 总费用
	 */
	public BigDecimal totalMoney = new BigDecimal(0);
	/**
	 * 总受益人数
	 */
	public int totalPerson;
}
