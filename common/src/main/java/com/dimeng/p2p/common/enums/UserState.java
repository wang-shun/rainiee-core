package com.dimeng.p2p.common.enums;

/**
 * 用户状态.
 * 
 */
public enum UserState {
	/**
	 * 正常
	 */
	ZC("正常"),
	/**
	 * 锁定
	 */
	SD("锁定");

	protected final String name;

	private UserState(String name) {
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
