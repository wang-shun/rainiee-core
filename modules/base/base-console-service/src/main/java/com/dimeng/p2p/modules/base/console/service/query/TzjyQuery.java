package com.dimeng.p2p.modules.base.console.service.query;

import com.dimeng.p2p.common.enums.NoticePublishStatus;

import java.sql.Timestamp;

/**
 * 投诉建议-查询类
 */
public interface TzjyQuery {


	/**
	 * 投诉时间查询,大于等于匹配.
	 * 
	 * @return {@link Timestamp} 非{@code null}有效
	 */
	public abstract Timestamp getCreateTimeStart();

	/**
	 * 投诉时间查询,小于等于匹配.
	 * 
	 * @return {@link Timestamp} 非{@code null}有效
	 */
	public abstract Timestamp getCreateTimeEnd();

	/**
	 * 发布时间查询,大于等于匹配.
	 * 
	 * @return {@link Timestamp} 非{@code null}有效
	 */
	public abstract Timestamp getPublishTimeStart();

	/**
	 * 发布时间查询,小于等于匹配.
	 * 
	 * @return {@link Timestamp} 非{@code null}有效
	 */
	public abstract Timestamp getPublishTimeEnd();

	/**
	 * 回复状态,全模糊匹配.
	 * 
	 * @return {@link String} 非空值有效
	 */
	public abstract String getReplyStatus();

	/**
	 * 发布状态查询,等于匹配.
	 * 
	 * @return {@link NoticePublishStatus} 非{@code null}有效
	 */
	public abstract String getPublishStatus();

	/**
	 * 主键ID查询,等于匹配.
	 *
	 * @return {@link NoticePublishStatus} 非{@code null}有效
	 */
	public abstract int getId();

}
