package com.dimeng.p2p.common.enums;

/**
 * 标的状态.
 * 
 */
public enum Sextype {
	/**
	 * 女
	 */
	F("女"),
	/**
	 * 男
	 */
	M("男");

	protected final String name;

	private Sextype(String name) {
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
