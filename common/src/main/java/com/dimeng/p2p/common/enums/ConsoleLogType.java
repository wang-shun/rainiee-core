package com.dimeng.p2p.common.enums;

public enum ConsoleLogType {
    DLRZ("登录日志"), CZRZ("操作日志");

	protected final String name;

	private ConsoleLogType(String name) {
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
