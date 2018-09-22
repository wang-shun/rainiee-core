/**
 * 
 */
package com.dimeng.p2p.modules.bid.front.service.entity;

import java.math.BigDecimal;

/**
 * @author guopeng
 * 
 */
public class IndexStatic {
	/**
	 * 累计成交 
	 */
	public BigDecimal ljcj = BigDecimal.ZERO;
	/**
	 * 借款总额
	 */
	public BigDecimal jkze = BigDecimal.ZERO;
	/**
	 * 已还本息
	 */
	public BigDecimal yhbx = BigDecimal.ZERO;
	/**
	 * 待还本息
	 */
	public BigDecimal dhbx = BigDecimal.ZERO;
	/**
	 * 逾期还款
	 */
	public BigDecimal yqhk = BigDecimal.ZERO;
	/**
	 * 昨日成交
	 */
	public BigDecimal zrcj = BigDecimal.ZERO;
	/**
	 * 本年成交
	 */
	public BigDecimal bncj = BigDecimal.ZERO;
	
	/**
	 * 累计投资金额
	 */
	public BigDecimal rzzje = BigDecimal.ZERO;
	
	/**
	 * 累计投资总收益
	 */
	public BigDecimal yhzsy = BigDecimal.ZERO;
	
	/**
	 * 累计投资人数
	 */
	public BigDecimal yhjys = BigDecimal.ZERO;
}
