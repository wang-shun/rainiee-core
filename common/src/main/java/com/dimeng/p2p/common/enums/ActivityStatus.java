package com.dimeng.p2p.common.enums;

/**
 * 活动状态
 * 
 */
public enum ActivityStatus {
	WKS("未开始"), JXZ("进行中"), YJS("已结束");

	protected final String name;

	private ActivityStatus(String name) {
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
