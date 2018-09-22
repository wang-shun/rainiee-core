package com.dimeng.p2p.modules.base.console.service.entity;

import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.PerformanceReportPublishStatus;

public class PerformanceReportRecord {

	/**
	 * ID
	 */
	public int id;
	/**
	 * 排序值
	 */
	public int sortIndex;
	/**
	 * 发布状态
	 */
	public PerformanceReportPublishStatus publishStatus;
	/**
	 * 浏览次数
	 */
	public int viewTimes;
	/**
	 * 标题
	 */
	public String title;
	/**
	 * 附件编码
	 */
	public String attachmentCode;
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
