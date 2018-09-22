package com.dimeng.p2p.common.enums;

/**
 * 保障方式.
 * 
 */
public enum SafeguardWay {
	/**
	 * 全额本息保障
	 */
	QEBXBZ("全额本息保障"),

	/**
	 * 本金
	 */
	BJ("本金");

	protected final String name;

	private SafeguardWay(String name) {
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
