package com.dimeng.p2p.modules.financing.user.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <%=configureProvider.getProperty(SystemVariable.SITE_NAME) %>平台已赚取统计
 * 
 */
public class EarnFinancingTotal implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 总计
	 */
	public BigDecimal total = new BigDecimal(0);
	/**
	 * 优选理财收益
	 */
	public BigDecimal plan = new BigDecimal(0);
	/**
	 * 已赚利息
	 */
	public BigDecimal interest = new BigDecimal(0);
	/**
	 * 已赚罚息
	 */
	public BigDecimal penalty = new BigDecimal(0);
	/**
	 * 已赚违约金
	 */
	public BigDecimal breach = new BigDecimal(0);
	/**
	 * 债权转让盈亏
	 */
	public BigDecimal transfer = new BigDecimal(0);
}
