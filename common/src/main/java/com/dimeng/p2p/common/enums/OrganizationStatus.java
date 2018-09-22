package com.dimeng.p2p.common.enums;

public enum OrganizationStatus {
	YX("有效"), WX("无效"), SC("删除");

	protected final String name;

	private OrganizationStatus(String name) {
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
