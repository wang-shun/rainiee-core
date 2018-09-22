package com.dimeng.p2p.common.enums;

/**
 * 机构类型.
 * 
 */
public enum IntentionType {

	SDRZ("实地认证"), JGDB("机构担保");

	protected final String name;

	private IntentionType(String name) {
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
