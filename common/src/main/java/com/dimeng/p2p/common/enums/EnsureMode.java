package com.dimeng.p2p.common.enums;

public enum EnsureMode {
	/**
	 * 全额本息保障
	 */
	QEBXBZ("全额本息保障");
	protected final String name;

	private EnsureMode(String name) {
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
