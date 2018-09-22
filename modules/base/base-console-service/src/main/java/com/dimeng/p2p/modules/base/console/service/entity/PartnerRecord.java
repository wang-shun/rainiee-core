package com.dimeng.p2p.modules.base.console.service.entity;

import java.sql.Timestamp;

public class PartnerRecord {

	/**
	 * ID
	 */
	public int id;
	/**
	 * 公司名称
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
	 * 链接地址
	 */
	public String url;
	/**
	 * 图片编码
	 */
	public String imageCode;
	/**
	 * 公司地址
	 */
	public String address;
	/**
	 * 描述
	 */
	public String description;
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
