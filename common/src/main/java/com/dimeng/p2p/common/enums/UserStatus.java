package com.dimeng.p2p.common.enums;

/**
 * 用户状态是否拉黑
 * 
 * @author gaoshaolong
 * 
 */
public enum UserStatus {
	S("是"), F("否");
	protected final String name;

	private UserStatus(String name) {
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
