package com.dimeng.p2p.modules.bid.front.service.entity;

import java.math.BigDecimal;

/**
 * 首页统计数据
 *
 */
public class Sytjsj {
	/**
	 * 累计成交
	 */
	public BigDecimal ljcj = new BigDecimal(0);
	/**
	 * 借款总额
	 */
	public BigDecimal jkze = new BigDecimal(0);
	/**
	 * 已还本息
	 */
	public BigDecimal yhbx = new BigDecimal(0);
	/**
	 * 待还本息
	 */
	public BigDecimal dhbx = new BigDecimal(0);
	/**
	 * 逾期还款
	 */
	public BigDecimal yqhk = new BigDecimal(0);
	/**
	 * 昨日成交
	 */
	public BigDecimal zrcj = new BigDecimal(0);
	/**
	 * 本年度成交
	 */
	public BigDecimal bncj = new BigDecimal(0);

}
