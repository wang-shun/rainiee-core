package com.dimeng.p2p.modules.finance.console.service.entity;

import java.math.BigDecimal;

public class Dfsdzq {

	/** 
	 * 垫付总金额
	 */
	public BigDecimal paymentAmount=new BigDecimal(0);
	/** 
	 * 垫付返还金额
	 */
	public BigDecimal paymentRestore=new BigDecimal(0);	
	/**	
	 * 待收总金额
	 */
	public BigDecimal restoreAmount=new BigDecimal(0);
	
	/**	
	 * 盈亏
	 */
	public BigDecimal profit=new BigDecimal(0);
}
