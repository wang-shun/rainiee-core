package com.dimeng.p2p.account.user.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.PlanState;

/**
 * 优选理财
 */
public class FinancialPlan implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 优选理财id
	 */
	public int id;
	/**
	 * 标题
	 */
	public String title;
	/**
	 * 标的状态
	 */
	public PlanState planState; 
	/**
	 * 总体额度
	 */
	public BigDecimal total = new BigDecimal(0);
	/**
	 * 剩余额度
	 */
	public BigDecimal remaining = new BigDecimal(0);
	/**
	 * 加入条件
	 */
	public BigDecimal condition = new BigDecimal(0);
	/**
	 * 预期收益率
	 */
	public BigDecimal yield;
	/**
	 * 申请进度
	 */
	public double progress;
	/**
	 * 距离发售时间
	 */
	public Timestamp fromSellTime;
	/**
	 * 数据库时间
	 */
	public Timestamp currentTime;

}
