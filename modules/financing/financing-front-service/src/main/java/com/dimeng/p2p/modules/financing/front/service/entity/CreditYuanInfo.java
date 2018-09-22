package com.dimeng.p2p.modules.financing.front.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 原始借款标详情
 * @author gaoshaolong
 *
 */
public class CreditYuanInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6923506673252400705L;
	
	/**
	 * 借款用户
	 */
	public String jkyh;
	
	
	/**
	 * 借款期限
	 */
	public String jkqx;
	
	/**
	 * 年化利率
	 */
	public BigDecimal nly = new BigDecimal(0);
	
	/**
	 * 还款方式
	 */
	public String hkfs;
	
	/**
	 * 保障方式
	 */
	public String bzfs;
	
	/**
	 * 提前还款费率
	 */
	public BigDecimal tqhkfl = new BigDecimal(0);
	
	/**
	 * 借款金额
	 */
	public BigDecimal jkje = new BigDecimal(0);
	
	/**
	 * 转出份数
	 */
	public int zcfs;
	
	/**
	 * 剩余份数
	 */
	public int syfs;

}
