package com.dimeng.p2p.common.enums;

public enum WorkStatus {
	/**
	 * 工薪阶层
	 */
	GXJC("工薪阶层");

	protected final String name;

	private WorkStatus(String name) {
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
