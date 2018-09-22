package com.dimeng.p2p.common.enums;

public enum UserSourceStatus {
	ZC("注册"), HT("后台");

	protected final String name;

	private UserSourceStatus(String name) {
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
