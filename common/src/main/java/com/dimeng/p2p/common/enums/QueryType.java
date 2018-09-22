package com.dimeng.p2p.common.enums;

/**
 * 查询类型
 * 
 */
public enum QueryType {

	YS("已收"), DS("待收");

	protected final String name;

	private QueryType(String name) {
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
