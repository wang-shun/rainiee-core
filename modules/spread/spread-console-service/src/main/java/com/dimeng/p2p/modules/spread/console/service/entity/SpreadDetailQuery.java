package com.dimeng.p2p.modules.spread.console.service.entity;

import java.sql.Timestamp;

/**
 * 推广详情列表查询条件 
 *
 */
public interface SpreadDetailQuery {
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
	 * 被推广人用户名
	 */
	public String personUserName();
	/**
	 * 被推广人姓名
	 */
	public String personName();
	/**
	 * 首次充值开始时间
	 */
	public Timestamp startTime();
	/**
	 * 首次充值结束时间
	 */
	public Timestamp endTime();
}
