package com.dimeng.p2p.modules.financial.front.service.entity;

import java.math.BigDecimal;

import com.dimeng.p2p.S64.entities.T6410;

public class YxlbEntity extends T6410 {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7174242456866273658L;

	/**
	 * 已赚金额
	 */
	public BigDecimal yzje = new BigDecimal(0);
	/**
	 * 加入人次
	 */
	public int jrrc;
	/**
	 * 平均收益率
	 */
	public double pjsyl;
}
