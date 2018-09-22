package com.dimeng.p2p.modules.financial.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.S64.enums.T6410_F07;

/**
 * 查询理财计划信息
 * @author gongliang
 *
 */
public class PlanMoney {
	/**
	 * 计划ID
	 * 
	 */
	public int planMoneyId;
	
	/**
	 * 计划名称
	 * 
	 */
	public String planName;
	
	/**
	 * 总金额
	 * 
	 */
	public BigDecimal zje = new BigDecimal(0);
	
	/**
	 * 可投余额
	 * 
	 */
	public BigDecimal ktye = new BigDecimal(0);
	
	/**
	 * 收益率
	 * 
	 */
	public double earningsRate;
	
	/**
	 * 加入人数
	 * 
	 */
	public int joinNumber;

	/**
	 * 发布时间
	 * 
	 */
	public Timestamp issueTime;
	
	/**
	 * 计划状态
	 * 
	 */
	public T6410_F07 state;
	/**
	 * 计划开始时间
	 */
	public Timestamp startTime;
	/**
	 * 数据库时间
	 */
	public Timestamp currentTime;
}
