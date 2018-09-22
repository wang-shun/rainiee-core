package com.dimeng.p2p.modules.bid.front.service.entity;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 投资人信息统计
 */
public class InvestorTotal extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 投资总人数
	 */
	public long investors;
	
	/**
	 * 投资总额
	 */
	public BigDecimal investAmount;
	
	/**
	 * 投资收益总额
	 */
	public BigDecimal investProfits;
	
}
