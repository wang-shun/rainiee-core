package com.dimeng.p2p.common.enums;

public enum IsHovestatus {
	/**
	 * 有
	 */
	Y("有"),
	/**
	 * 无
	 */
	W("无");

	protected final String name;

	private IsHovestatus(String name) {
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
