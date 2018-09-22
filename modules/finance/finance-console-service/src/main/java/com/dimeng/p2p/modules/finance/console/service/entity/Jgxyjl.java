package com.dimeng.p2p.modules.finance.console.service.entity;

import java.math.BigDecimal;

public class Jgxyjl {

	/** 
	 * 申请借款（笔）
	 */
	public int  sqjkCount;
	/** 
	 * 成功借款（笔）
	 */
	public int cgjkCount;
	
	/** 
	 * 还清笔数（笔）
	 */
	public int hqbsCount;
	/** 
	 *  信用额度（元）
	 */
	public BigDecimal xyedAmount=new BigDecimal(0);	

	/** 
	 * 借款总额（元）
	 */
	public BigDecimal jkTotalAmount=new BigDecimal(0);	
	
	/** 
	 * 待还本金（元）
	 */
	public BigDecimal dhbjAmount=new BigDecimal(0);	
	/** 
	 *  逾期金额（元）
	 */
	public BigDecimal yqjeAmount=new BigDecimal(0);	
	/** 
	 * 逾期次数（次）
	 */
	public int yqcsCount;	
	/** 
	 * 严重逾期（笔）
	 */
	public int yzyqCount;	

}
