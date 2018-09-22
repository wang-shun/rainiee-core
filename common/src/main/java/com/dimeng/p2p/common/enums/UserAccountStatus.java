package com.dimeng.p2p.common.enums;

public enum UserAccountStatus {
	WYZ("未验证(审核中)"), YYZ("已验证(通过"), BH("驳回");

	protected final String name;

	private UserAccountStatus(String name) {
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
