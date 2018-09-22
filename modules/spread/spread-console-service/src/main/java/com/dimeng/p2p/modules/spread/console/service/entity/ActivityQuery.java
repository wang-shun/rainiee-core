package com.dimeng.p2p.modules.spread.console.service.entity;

import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.ActivityStatus;
/**
 * 活动列表查询条件 
 *
 */
public interface ActivityQuery {
	/**
	 * 活动主题
	 */
	public String title();
	/**
	 * 开始时间
	 */
	public Timestamp startTime();
	/**
	 * 结束时间
	 */
	public Timestamp endTime();
	/**
	 * 状态
	 */
	public ActivityStatus status();
}
