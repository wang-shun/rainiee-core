package com.dimeng.p2p.modules.base.console.service.entity;

import java.sql.Timestamp;

public class FriendlyLinkRecord {

	/**
	 * ID
	 */
	public int id;
	/**
	 * 名称
	 */
	public String name;
	/**
	 * 排序值
	 */
	public int sortIndex;
	/**
	 * 浏览次数
	 */
	public int viewTimes;
	/**
	 * URL
	 */
	public String url;
	/**
	 * 创建时间
	 */
	public Timestamp createTime;
	/**
	 * 更新时间
	 */
	public Timestamp updateTime;
	/**
	 * 发布者姓名
	 */
	public String publisherName;
	/**
	 * 发布者ID
	 */
	public int publisherId;
}
