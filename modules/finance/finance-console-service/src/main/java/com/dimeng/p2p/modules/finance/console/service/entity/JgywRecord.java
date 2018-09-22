package com.dimeng.p2p.modules.finance.console.service.entity;

import java.math.BigDecimal;

import com.dimeng.p2p.common.enums.CreditStatus;

public class JgywRecord {
	

	/** 
	 * 借款标题
	 */
	public String title;
	/** 
	 * 借款账户
	 */
	public String accountName;
	
	/** 
	 * 借款金额（元）
	 */
	public BigDecimal loanAmount=new BigDecimal(0);
	/** 
	 * 年化利率
	 */
	public BigDecimal proportion=new BigDecimal(0);
	
	/** 
	 * 期限
	 */
	public int deadline;

	/** 
	 * 待还本金（元）
	 */
	public BigDecimal dfAmount=new BigDecimal(0);

	/** 
	 * 状态
	 */
	public CreditStatus status ;	

}
