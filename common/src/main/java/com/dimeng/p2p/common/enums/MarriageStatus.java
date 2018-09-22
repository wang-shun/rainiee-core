package com.dimeng.p2p.common.enums;

public enum MarriageStatus {
	/**
	 * 已婚
	 */
	YH("已婚"),
	/**
	 * 未婚
	 */
	WH("未婚");

	protected final String name;

	private MarriageStatus(String name) {
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
