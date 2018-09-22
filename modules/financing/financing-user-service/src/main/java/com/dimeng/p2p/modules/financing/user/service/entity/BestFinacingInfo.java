package com.dimeng.p2p.modules.financing.user.service.entity;

import java.math.BigDecimal;

/**
 * 优先理财统计信息
 * 
 */
public class BestFinacingInfo {
	/**
	 * 优选计划已赚总金额
	 */
	public BigDecimal makeMoney = new BigDecimal(0);
	/**
	 * 优选理财账户资产
	 */
	public BigDecimal accountMoney = new BigDecimal(0);
	/**
	 * 平均收益率
	 */
	public BigDecimal rate = new BigDecimal(0);
	/**
	 * 持有中的计划数量
	 */
	public int num;
	/**
	 * 加入费用
	 */
	public BigDecimal addMoney = new BigDecimal(0);
}
