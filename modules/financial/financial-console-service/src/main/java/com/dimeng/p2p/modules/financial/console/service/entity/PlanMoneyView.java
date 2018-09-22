package com.dimeng.p2p.modules.financial.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.S64.enums.T6410_F07;
import com.dimeng.p2p.S64.enums.T6410_F14;
import com.dimeng.p2p.S64.enums.T6410_F24;

/**
 * 理财计划详情信息
 * @author gongliang
 *
 */
public class PlanMoneyView {
	
	/**
	 * 计划Id
	 */
	public int planMoneyId;
	
	/**
	 * 计划名称
	 */
	public String planName;
	
	/**
	 * 计划金额
	 */
	public BigDecimal planMoneys = new BigDecimal(0);
	
	/**
	 * 投资类型
	 */
	public int tblx;
	
	/**
	 * 投资类型名称
	 */
	public String tblxmc;
	
	/**
	 * 计划状态
	 */
	public T6410_F07 state;
	
	/**
	 * 锁定时间
	 */
	public int lockTime;
	
	/**
	 * 收益处理
	 */
	public T6410_F14 earningsWay;
	
	/**
	 * 预期收益
	 */
	public double expectEarnings;
	
	/**
	 * 保障方式
	 */
	public T6410_F24 safeguardWay;
	
	/**
	 * 满额用时
	 */
	public int fullTime;
	
	/**
	 * 锁定结束时间
	 */
	public Timestamp lockEndTime;
	
	/**
	 * 计划开始时间
	 */
	public Timestamp planStart;
	/**
	 * 计划截止
	 */
	public Timestamp planEnd;
	
	/**
	 * 加入费率
	 */
	public double addRate;
	
	/**
	 * 服务费率
	 */
	public double serveRate;
	
	/**
	 * 退出费率
	 */
	public double quitRate;
	
	/**
	 * 剩余金额
	 */
	public BigDecimal residueMoney = new BigDecimal(0);
	
	/**
	 * 每人可加入金额上限
	 */
	public BigDecimal investCeiling = new BigDecimal(0);
	
	/**
	 * 每人可加入金额下限
	 */
	public BigDecimal investFloor = new BigDecimal(0);
	
	/**
	 * 计划介绍
	 */
	public String planMoneyDesc;
	/**
	 * 系统时间
	 */
	public Timestamp currentTime;
}
