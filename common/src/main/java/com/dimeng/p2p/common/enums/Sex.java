package com.dimeng.p2p.common.enums;

/**
 * 性别
 */
public enum Sex {
	F("女"), M("男");

	protected final String name;

	private Sex(String name) {
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
