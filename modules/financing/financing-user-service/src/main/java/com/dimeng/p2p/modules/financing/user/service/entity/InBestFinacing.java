package com.dimeng.p2p.modules.financing.user.service.entity;

import java.math.BigDecimal;
import java.sql.Date;

import com.dimeng.p2p.S60.enums.T6042_F14;
import com.dimeng.p2p.common.enums.PlanState;

/**
 * 申请中的优先理财
 *
 */
public class InBestFinacing {
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
	 * 待收本息
	 */
	public BigDecimal notMoney = new BigDecimal(0);
	/**
	 * 月收本息
	 */
	public BigDecimal monthMoney = new BigDecimal(0);
	/**
	 * 剩余期数
	 */
	public int num;
	/**
	 * 借款期数
	 */
	public int jkTime;
	/**
	 * 下一回款日
	 */
	public Date nextDay;
	/**
	 * 状态
	 */
	public PlanState status;
	/**
	 * 收益类型
	 */
	public T6042_F14 type;
}
