package com.dimeng.p2p.common.enums;

/**
 * 投资范围.
 * 
 */
public enum SignType {
	/**
	 * 实地认证
	 */
	SDRZ("实地认证"),
	/**
	 * 机构担保
	 */
	JGDB("机构担保"),
	/**
	 * 实地认证+机构担保
	 */
	SDRZJJGDB("实地认证+机构担保");

	protected final String name;

	private SignType(String name) {
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
