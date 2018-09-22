package com.dimeng.p2p.common.enums;

/**
 * 登录状态.
 * 
 */
public enum LoginStatus {
	/**
	 * 成功
	 */
	CG("成功"),
	/**
	 * 失败
	 */
	SB("失败");

	protected final String name;

	private LoginStatus(String name) {
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
