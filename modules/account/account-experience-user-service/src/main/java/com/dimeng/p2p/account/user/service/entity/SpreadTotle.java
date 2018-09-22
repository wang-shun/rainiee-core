package com.dimeng.p2p.account.user.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class SpreadTotle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3838941175463728940L;
	
	/**
	 * 邀请人数统计
	 */
	public int yqCount;
	/**
	 * 奖励总计
	 */
	public BigDecimal rewardTotle = new BigDecimal(0);
	/**
	 * 推广持续奖励金额
	 */
	public BigDecimal rewardCxtg = new BigDecimal(0);
	/**
	 * 有效推广奖励金额
	 */
	public BigDecimal rewardYxtg = new BigDecimal(0);

}
