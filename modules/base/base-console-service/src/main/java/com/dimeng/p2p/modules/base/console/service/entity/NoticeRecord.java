package com.dimeng.p2p.modules.base.console.service.entity;

import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.NoticePublishStatus;
import com.dimeng.p2p.common.enums.NoticeType;

public class NoticeRecord {

	/**
	 * ID
	 */
	public int id;
	/**
	 * 公告类型
	 */
	public NoticeType type;
	/**
	 * 浏览次数
	 */
	public int viewTimes;
	/**
	 * 发布状态
	 */
	public NoticePublishStatus publishStatus;
	/**
	 * 标题
	 */
	public String title;
	/**
	 * 内容
	 */
	public String content;
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
