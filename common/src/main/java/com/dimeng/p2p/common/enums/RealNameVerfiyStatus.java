package com.dimeng.p2p.common.enums;

public enum RealNameVerfiyStatus {
	/**
	 * 未验证
	 */
	WYZ("未验证"),
	/**
	 * 未验证
	 */
	YYZ("未验证"),
	/**
	 * 驳回
	 */
	BH("驳回");

	protected final String name;

	private RealNameVerfiyStatus(String name) {
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
