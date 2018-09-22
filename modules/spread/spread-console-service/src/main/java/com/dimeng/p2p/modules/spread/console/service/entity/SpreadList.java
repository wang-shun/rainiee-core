package com.dimeng.p2p.modules.spread.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 推广持续奖励详情列表
 *
 */
public class SpreadList {
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
	 * 被推广人姓名
	 */
	public String personName;
	/**
	 * 推广投资金额
	 */
	public BigDecimal spreadTotalMoney = new BigDecimal(0);
	/**
	 * 持续奖励
	 */
	public BigDecimal rewardMoney = new BigDecimal(0);
	/**
	 * 投资时间
	 */
	public Timestamp investTime;

}
