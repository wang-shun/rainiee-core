package com.dimeng.p2p.common.enums;

public enum SysMessageStatus {
	QY("启用"), TY("停用");

	protected final String name;

	private SysMessageStatus(String name) {
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
