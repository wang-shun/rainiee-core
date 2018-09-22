package com.dimeng.p2p.common.enums;

public enum EmailVerfiyStatus {
	/**
	 * 未验证
	 */
	WYZ("未验证"),
	/**
	 * 未验证
	 */
	YYZ("未验证");
	protected final String name;

	private EmailVerfiyStatus(String name) {
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
