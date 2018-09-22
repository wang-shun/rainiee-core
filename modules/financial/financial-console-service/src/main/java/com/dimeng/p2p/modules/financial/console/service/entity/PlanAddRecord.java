package com.dimeng.p2p.modules.financial.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 计划加入记录
 * @author gongliang
 *
 */
public class PlanAddRecord {
	
	/**
	 * 投资人
	 */
	public String userName;
	
	/**
	 * 投资金额
	 */
	public BigDecimal addMoney = new BigDecimal(0);

	/**
	 * 投资时间
	 */
	public Timestamp investTime;
}
