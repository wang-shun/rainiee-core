package com.dimeng.p2p.common.enums;

public enum SkinStatus {
	/**
	 * 位置
	 */
	SY("首页"),
	/**
	 * 位置
	 */
	GRZX("个人中心");

	protected final String name;

	private SkinStatus(String name) {
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
