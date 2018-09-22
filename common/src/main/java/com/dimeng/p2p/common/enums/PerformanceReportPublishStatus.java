package com.dimeng.p2p.common.enums;

/**
 * 业绩报告发布状态.
 * 
 * 
 */
public enum PerformanceReportPublishStatus {
	/**
	 * 已发布
	 */
	YFB("已发布"),
	/**
	 * 未发布
	 */
	WFB("未发布");

	protected final String name;

	private PerformanceReportPublishStatus(String name) {
		this.name = name;
	}

	/**
	 * 获取中文名称.
	 * 
	 * @return {@link String}
	 */
	public String getName() {
		return name;

	}
}
