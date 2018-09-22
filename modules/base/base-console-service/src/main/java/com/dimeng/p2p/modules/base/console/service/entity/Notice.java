package com.dimeng.p2p.modules.base.console.service.entity;

import com.dimeng.p2p.common.enums.NoticePublishStatus;
import com.dimeng.p2p.common.enums.NoticeType;
/**
 * 公告信息
 *
 */
public abstract interface Notice {

	/**
	 * 获取标题.
	 * 
	 * @return {@link String}
	 */
	public abstract String getTitle();

	/**
	 * 获取类型.
	 * 
	 * @return {@link NoticeType}
	 */
	public abstract NoticeType getType();

	/**
	 * 获取发布状态.
	 * 
	 * @return {@link NoticePublishStatus}
	 */
	public abstract NoticePublishStatus getPublishStatus();

	/**
	 * 获取内容.
	 * 
	 * @return {@link String}
	 */
	public abstract String getContent();

}
