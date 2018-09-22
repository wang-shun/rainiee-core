package com.dimeng.p2p.modules.financing.front.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class CreditAssignmentInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 911160737053523373L;

	/**
	 * id
	 */
	public int id;
	/**
	 * 借款标id
	 */
	public int jkbId;
	
	/**
	 * 转出者ID（投资用户）
	 */
	public String zczName;
	
	/**
	 * 标题
	 */
	public String title;
	
	/**
	 * 剩余期限
	 */
	public String syqx;
	
	/**
	 * 转让价格
	 */
	public BigDecimal zrjg = new BigDecimal(0);
	
	/**
	 * 下个还款日
	 */
	public String nextHuankuan;
	
	/**
	 * 债权价值
	 */
	public BigDecimal zqjz = new BigDecimal(0);
	
	/**
	 * 原始投资金额
	 */
	public BigDecimal ystzje = new BigDecimal(0);
	
	/**
	 * 预计收益
	 */
	public BigDecimal yjsy = new BigDecimal(0);
	
	
	
	
	/**
	 * 类型
	 */
	public String type;
	
	

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

	/**
	 * 借款描述
	 */
	public String jkDesc;
	

	

}
