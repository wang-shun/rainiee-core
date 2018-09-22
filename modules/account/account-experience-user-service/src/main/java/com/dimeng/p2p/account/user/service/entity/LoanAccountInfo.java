/**
 * 
 */
package com.dimeng.p2p.account.user.service.entity;

import java.math.BigDecimal;

import com.dimeng.p2p.S62.enums.T6231_F21;

/**
 * 贷款帐户详情
 * 新增了年化利率，期限，进 度等信息
 * @author guomianyun
 *
 */
public class LoanAccountInfo extends LoanAccount {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 年化利率
	 */
	public BigDecimal  F06= new BigDecimal(0);
	
	/**
	 * 借款期限:天
	 */
	public int F08 ;
	
	/**
	 * 借款期限：月
	 */
	public int F09 ;
	
	/**
	 * 是否为按天借款,S:是;F:否
	 */
	public T6231_F21 F21;
	
	/**
	 * 进度
	 */
	public double proess;
	
	/**
	 * 借款金额 
	 */
	public BigDecimal  F05= new BigDecimal(0);
	
	/**
	 * 可投金额 
	 */
	public BigDecimal  F07= new BigDecimal(0);
}
