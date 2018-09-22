package com.dimeng.p2p.modules.finance.console.service.entity;

import java.math.BigDecimal;

public class JgfxbyjRecord {
	
	/**
	 * 机构名称
	 */
	public String orgName;
	/** 
	 * 风险备用金余额
	 */
	public BigDecimal riskAmount=new BigDecimal(0); 
	/** 
	 * 收入总额
	 */
	public BigDecimal incomeAmount=new BigDecimal(0);	
	/**	
	 * 支出总额
	 */
	public BigDecimal payAmount=new BigDecimal(0);
	
	/**	
	 * 盈亏
	 */
	public BigDecimal lossAmount=new BigDecimal(0);


}
