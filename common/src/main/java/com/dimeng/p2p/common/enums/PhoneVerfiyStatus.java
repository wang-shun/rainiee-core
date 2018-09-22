package com.dimeng.p2p.common.enums;

/**
 * 手机是否验证
 * 
 * @author Administrator
 * 
 */
public enum PhoneVerfiyStatus {
	/**
	 * 未验证
	 */
	WYZ("未验证"),
	/**
	 * 已验证
	 */
	YYZ("已验证");
	protected final String name;

	private PhoneVerfiyStatus(String name) {
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
