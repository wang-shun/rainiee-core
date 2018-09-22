package com.dimeng.p2p.common.enums;

/**
 * 是否逾期
 * 
 */
public enum OverdueEnum {
	S("是"), F("否");
	protected final String name;

	private OverdueEnum(String name) {
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
