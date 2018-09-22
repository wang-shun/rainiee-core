package com.dimeng.p2p.modules.financial.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.S64.enums.T6410_F07;
import com.dimeng.p2p.S64.enums.T6410_F14;

/**
 * 新增理财计划信息
 * 
 * @author gongliang
 * 
 */
public abstract interface PlanMoneyAdd {
	/**
	 * 计划名称
	 * 
	 * @return {@link String}计划名称不允许为空.
	 */
	public abstract String getPlanName();

	/**
	 * 用户投资上限
	 * 
	 * @return {@link BigDecimal}用户投资上限不允许为空.
	 */
	public abstract BigDecimal getInvestCeiling();

	/**
	 * 用户投资下限
	 * 
	 * @return {@link BigDecimal}用户投资下限不允许为空.
	 */
	public abstract BigDecimal getInvestFloor();

	/**
	 * 计划金额
	 * 
	 * @return {@link BigDecimal}计划金额不允许为空.
	 */
	public abstract BigDecimal getPlanMoneys();

	/**
	 * 收益年化利率
	 * 
	 * @return {@code double} 收益年化利率不允许为空.
	 */
	public abstract double getEarningsYearRate();

	/**
	 * 加入费利率
	 * 
	 * @return {@code double}加入费年化利率不允许为空.
	 */
	public abstract double getAddYearRate();

	/**
	 * 服务费率
	 * 
	 * @return {@code double} 服务费率不允许为空.
	 */
	public abstract double getServeRate();

	/**
	 * 退出费率
	 * 
	 * @return {@code double} 退出费率不允许为空.
	 */
	public abstract double getQuitRate();

	/**
	 * 投资范围
	 * 
	 * @return {@link int}投资范围不允许为空.
	 */
	public abstract int getTenderScope();

	/**
	 * 锁定时间
	 * 
	 * @return {@link int}锁定时间不允许为空.
	 */
	public abstract int getLockTime();

	/**
	 * 申请开始时间
	 * 
	 * @return {@link Timestamp}申请开始时间不允许为空.
	 */
	public abstract Timestamp getApplyStartTime();

	/**
	 * 申请截止时间
	 * 
	 * @return {@link Timestamp}申请截止时间不允许为空.
	 */
	public abstract Timestamp getApplyEndTime();

	/**
	 * 收益方式
	 * 
	 * @return {@link T6410_F14}收益方式不允许为空.
	 */
	public abstract T6410_F14 getEarningsWay();

	/**
	 * 计划状态
	 * 
	 * @return {@link T6410_F07}计划状态不允许为空.
	 */
	public abstract T6410_F07 getState();

	/**
	 * 计划介绍
	 * 
	 * @return {@link String}计划状态不允许为空.
	 */
	public abstract String getPlanDesc();

}
