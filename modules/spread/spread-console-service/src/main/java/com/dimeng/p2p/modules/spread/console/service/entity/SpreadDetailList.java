package com.dimeng.p2p.modules.spread.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 推广详情列表
 *
 */
public class SpreadDetailList {
	
	/**
	 * 推广人ID
	 */
	public int id;
	/**
	 * 推广人用户名
	 */
	public String userName;
	/**
	 * 推广人姓名
	 */
	public String name;
	/**
	 * 被推广人ID
	 */
	public int personID;
	/**
	 * 被推广人用户名
	 */
	public String personUserName;
	/**
	 * 推被广人姓名
	 */
	public String personName;
	/**
	 * 首次充值金额
	 */
	public BigDecimal firstMoney = new BigDecimal(0);
	/**
	 * 首次充值时间
	 */
	public Timestamp firstTime;
	/**
	 * 有效推广奖励
	 */
	public BigDecimal spreadRewardMoney = new BigDecimal(0);


}
