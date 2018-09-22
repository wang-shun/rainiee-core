package com.dimeng.p2p.modules.financing.user.service.entity;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * 还款
 *
 */
public class CreditHk {
	/**
	 * 期号
	 */
	public int qh;
	/**
	 * 应收日期
	 */
	public Date ysrq;
	/**
	 * 每份应收本金
	 */
	public BigDecimal mfysbj;
	/**
	 * 每份应收利息
	 */
	public BigDecimal mfyslx;
}
