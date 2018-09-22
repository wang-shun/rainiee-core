package com.dimeng.p2p.common.enums;

/**
 * 是否第一次登录修改密码
 */
public enum IsPass {
	/**
	 * 是
	 */
	S("是"),
	/**
	 * 否
	 */
	F("否");

	protected final String name;

	private IsPass(String name) {
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
