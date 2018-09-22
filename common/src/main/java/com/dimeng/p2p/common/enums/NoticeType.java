package com.dimeng.p2p.common.enums;

public enum NoticeType {
	/**
	 * 活动
	 */
	HD("活动"),
	/**
	 * 系统
	 */
	XT("系统");

	protected final String name;

	private NoticeType(String name) {
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
