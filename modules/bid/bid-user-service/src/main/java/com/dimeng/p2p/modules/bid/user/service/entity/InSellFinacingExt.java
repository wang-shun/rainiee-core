package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;

/**
 * 已转入的债权
 *
 */
public class InSellFinacingExt extends InSellFinacing {
	/**
	 * 总期数
	 */
	private int totalTerm;

	/**
	 * 剩余期数
	 */
	private int subTerm;

	/**
	 * 年化利率
	 */
	private BigDecimal rate = new BigDecimal(0);

	public int getTotalTerm() {
		return totalTerm;
	}

	public void setTotalTerm(int totalTerm) {
		this.totalTerm = totalTerm;
	}

	public int getSubTerm() {
		return subTerm;
	}

	public void setSubTerm(int subTerm) {
		this.subTerm = subTerm;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

}
