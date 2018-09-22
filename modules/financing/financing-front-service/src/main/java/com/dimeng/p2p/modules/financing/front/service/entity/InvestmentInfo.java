package com.dimeng.p2p.modules.financing.front.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 散标详情头部
 * @author gaoshaolong
 *
 */
public class InvestmentInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1221778662883340870L;
	
	/**
	 * 借款标id
	 */
	public int jkbId;
	
	/**
	 * 借款用户
	 */
	public String jkName;
	
	/**
	 * 标题
	 */
	public String title;
	
	/**
	 * 借款金额
	 */
	public BigDecimal jkje = new BigDecimal(0);
	
	/**
	 * 标类型
	 */
	public String type;
	
	
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
	 * 月还本息
	 */
	public BigDecimal yhbenxi = new BigDecimal(0);
	/**
	 * 标状态
	 */
	public String biaoStatus;
	/**
	 * 借款描述
	 */
	public String jkDesc;

}
