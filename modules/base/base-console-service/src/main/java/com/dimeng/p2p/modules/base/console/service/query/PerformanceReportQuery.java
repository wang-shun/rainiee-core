package com.dimeng.p2p.modules.base.console.service.query;

import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.PerformanceReportPublishStatus;

public interface PerformanceReportQuery {
	/**
	 * 发布者姓名,全模糊匹配.
	 * 
	 * @return {@link String} 空值无效
	 */
	public String getPublisherName();

	/**
	 * 创建时间查询,大于等于匹配.
	 * 
	 * @return {@link Timestamp} 非{@code null}有效
	 */
	public abstract Timestamp getCreateTimeStart();

	/**
	 * 创建时间查询,下于等于匹配.
	 * 
	 * @return {@link Timestamp} 非{@code null}有效
	 */
	public abstract Timestamp getCreateTimeEnd();

	/**
	 * 修改时间查询,大于等于匹配.
	 * 
	 * @return {@link Timestamp} 非{@code null}有效
	 */
	public abstract Timestamp getUpdateTimeStart();

	/**
	 * 修改时间查询,小于等于匹配.
	 * 
	 * @return {@link Timestamp} 非{@code null}有效
	 */
	public abstract Timestamp getUpdateTimeEnd();

	/**
	 * 标题查询,全模糊匹配.
	 * 
	 * @return {@link String} 非空值有效
	 */
	public abstract String getTitle();

	/**
	 * 发布状态查询,等于匹配.
	 * 
	 * @return {@link PerformanceReportPublishStatus} 非{@code null}有效
	 */
	public abstract PerformanceReportPublishStatus getPublishStatus();
}
