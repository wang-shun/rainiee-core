package com.dimeng.p2p.modules.spread.console.service.entity;

import java.math.BigDecimal;

/**
 * 推广奖励概要列表
 *
 */
public class SpreadTotalList {
	/**
	 * 推广人ID
	 */
	public int personID;
	/**
	 * 推广人用户名
	 */
	public String userName;
	/**
	 * 推广人姓名
	 */
	public String name;
	/**
	 * 推广客户数
	 */
	public int customNum;
	/**
	 * 推广投资金额
	 */
	public BigDecimal spreadMoney = new BigDecimal(0);
	/**
	 * 连续奖励总计
	 */
	public BigDecimal seriesRewarMoney = new BigDecimal(0);
	/**
	 * 有效推广奖励总计
	 */
	public BigDecimal validRewardMoney = new BigDecimal(0);
	/**
	 * 推广奖励总计
	 */
	public BigDecimal rewardTotalMoney = new BigDecimal(0);



}
