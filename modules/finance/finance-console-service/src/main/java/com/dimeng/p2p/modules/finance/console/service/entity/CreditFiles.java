package com.dimeng.p2p.modules.finance.console.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 信用档案
 * @author gaoshaolong
 *
 */
public class CreditFiles implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2550644134242154295L;
	/**
	 * 申请借款（笔）
	 */
	public int sqjkCount;
	/**
	 * 成功借款（笔）
	 */
	public int cgCount;
	/**
	 * 还清借款（笔）
	 */
	public int hqjkCount;
	/**
	 * 信用额度（元）
	 */
	public BigDecimal xyedMoney=new BigDecimal(0);
	/**
	 * 借款总额（元）
	 */
	public BigDecimal jkzeMoney=new BigDecimal(0);
	/**
	 * 待还本息（元）
	 */
	public BigDecimal dhbxMoney=new BigDecimal(0);
	/**
	 * 逾期金额（元）
	 */
	public BigDecimal yqjeMoney=new BigDecimal(0);
	/**
	 * 逾期次数（次）
	 */
	public int yqcsCount;
	/**
	 * 严重逾期（笔）
	 */
	public int yzyqCount;

}
