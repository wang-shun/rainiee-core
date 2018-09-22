package com.dimeng.p2p.modules.base.console.service.entity;

import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.AdvertisementStatus;

public class AdvertisementRecord {

	/**
	 * 广告ID
	 */
	public int id;
	/**
	 * 排序值
	 */
	public int sortIndex;
	/**
	 * 广告标题
	 */
	public String title;
	/**
	 * 链接地址
	 */
	public String url;
	/**
	 * 图片编码
	 */
	public String imageCode;
	/**
	 * 广告状态
	 */
	public AdvertisementStatus status;
	/**
	 * 发布者ID
	 */
	public int publisherId;
	/**
	 * 发布者姓名
	 */
	public String publisherName;
	/**
	 * 上架时间
	 */
	public Timestamp showTime;
	/**
	 * 下架时间
	 */
	public Timestamp unshowTime;

	/**
	 * 创建时间
	 */
	public Timestamp createTime;
	/**
	 * 最后修改时间
	 */
	public Timestamp updateTime;
	/**
	 * 广告内容
	 */
	public String content;
	/**
	 * 广告类型
	 */
	public String advType;
}
