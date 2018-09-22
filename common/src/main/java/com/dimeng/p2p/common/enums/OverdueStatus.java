package com.dimeng.p2p.common.enums;

public enum OverdueStatus {
	S("是"), F("否");

	protected final String name;

	private OverdueStatus(String name) {
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
