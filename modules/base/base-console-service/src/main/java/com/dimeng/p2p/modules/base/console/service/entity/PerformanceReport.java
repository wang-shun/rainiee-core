package com.dimeng.p2p.modules.base.console.service.entity;

import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.p2p.common.enums.PerformanceReportPublishStatus;
/**
 * 业绩报告信息
 *
 */
public interface PerformanceReport {

	/**
	 * 标题 .
	 * 
	 * @return {@link String}
	 */
	public String getTitle();

	/**
	 * 发布状态.
	 * 
	 * @return {@link PerformanceReportPublishStatus}
	 */
	public PerformanceReportPublishStatus getPublishStatus();

	/**
	 * 附件.
	 * 
	 * @return {@link UploadFile}
	 * @throws Throwable
	 */
	public UploadFile getAttachment() throws Throwable;
}
