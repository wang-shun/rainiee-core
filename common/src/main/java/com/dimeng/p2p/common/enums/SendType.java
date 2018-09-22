package com.dimeng.p2p.common.enums;

public enum SendType {
	ZDR("发送给指定人"), SY("发送给所有人");
	protected final String name;

	private SendType(String name) {
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
