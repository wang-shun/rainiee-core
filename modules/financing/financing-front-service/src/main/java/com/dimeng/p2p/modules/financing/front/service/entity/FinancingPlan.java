package com.dimeng.p2p.modules.financing.front.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
/**
 * 优选理财列表
 * @author gaoshaolong
 *
 */
public class FinancingPlan implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7664475460511628242L;
	/**
	 * 优选理财计划id
	 */
	public int id;
	/**
	 * 标题
	 */
	public String planTitle;
	/**
	 * 计划金额
	 */
	public String planMoney;
	/**
	 * 加入人数
	 */
	public int joinCount;
	/**
	 * 人均投资
	 */
	public int avgTender;
	/**
	 * 资金利用率
	 */
	public double moneyUtilization;
	/**
	 * 平均收益率
	 */
	public double avgYield;
	/**
	 * 累计赚取
	 */
	public BigDecimal totleEarn = new BigDecimal(0);
	/**
	 * 发布时间
	 */
	public Timestamp releaseTime;
	

}
