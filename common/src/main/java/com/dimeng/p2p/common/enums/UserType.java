package com.dimeng.p2p.common.enums;

/**
 * 用户类型.
 * 
 */
public enum UserType {
	/**
	 * 理财
	 */
	LC("我要理财"),
	/**
	 * 借款
	 */
	JK("我要借款");

	protected final String name;

	private UserType(String name) {
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
