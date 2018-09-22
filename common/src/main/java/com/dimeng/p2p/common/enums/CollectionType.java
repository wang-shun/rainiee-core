package com.dimeng.p2p.common.enums;

/**
 * 催收方式.
 * 
 */
public enum CollectionType {
	/**
	 * 电话
	 */
	DH("电话"),
	/**
	 * 现场
	 */
	XC("现场"),
	/**
	 * 法律
	 */
	FL("法律");

	protected final String name;

	private CollectionType(String name) {
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
