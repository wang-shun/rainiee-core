package com.dimeng.p2p.modules.spread.console.service.entity;

import java.sql.Timestamp;

/**
 * 推广持续奖励详情列表查询条件
 *
 */
public interface SpreadQuery {
	/**
	 * 推广人ID
	 */
	public int id();
	/**
	 * 推广人用户名
	 */
	public String userName();
	/**
	 * 推广人姓名
	 */
	public String name();
	/**
	 * 被推广人ID
	 */
	public int personID();
	/**
	 * 被推广人姓名
	 */
	public String personName();
	/**
	 * 被推广人用户名
	 */
	public String personUserName();
	/**
	 * 投资开始时间
	 */
	public Timestamp investStartTime();
	/**
	 * 投资结束时间
	 */
	public Timestamp investEndTime();
}
