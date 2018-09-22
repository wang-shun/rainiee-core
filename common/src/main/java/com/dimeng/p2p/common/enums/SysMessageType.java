package com.dimeng.p2p.common.enums;

public enum SysMessageType {
	/**
	 * 消息类型
	 */
	ZNX("站内信"), YJ("邮件"), DX("短信");

	protected final String name;

	private SysMessageType(String name) {
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
