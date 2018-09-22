package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;

import com.dimeng.p2p.common.enums.PlanState;

/**
 * 持有中的优先理财
 *
 */
public class SqzBestFinacing {
	/**
	 * 计划Id
	 */
	public int planId;
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
	 * 借款期数
	 */
	public int jkTime;
	/**
	 * 状态
	 */
	public PlanState status;
	/**
	 * 剩余时间
	 */
	public String sysj;
	/**
	 * 进度
	 */
	public double jindu;
}
