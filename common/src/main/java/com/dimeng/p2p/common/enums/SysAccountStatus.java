package com.dimeng.p2p.common.enums;

public enum SysAccountStatus {
	QY("启用"), TY("锁定");

	protected final String name;

	private SysAccountStatus(String name) {
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
