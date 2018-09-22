package com.dimeng.p2p.modules.finance.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Ptfxbyjgl {
	
	/**
	 * 风险保证金余额
	 */
	public BigDecimal riskFund=new BigDecimal(0);
	/** 
	 * 总支出
	 */
	public BigDecimal totalReplay=new BigDecimal(0);
	/** 
	 * 总收入
	 */
	public BigDecimal  totalIncome=new BigDecimal(0);	
	/**
	 * 盈亏
	 */
	public BigDecimal profitLoss=new BigDecimal(0);
	/**
	 * 更新时间
	 */
	public Timestamp updateTime;
	

}
