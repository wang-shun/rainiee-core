package com.dimeng.p2p.common.enums;

public enum DsStatus {
	YS("已收"), WS("未收");
	protected final String name;

	private DsStatus(String name) {
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
