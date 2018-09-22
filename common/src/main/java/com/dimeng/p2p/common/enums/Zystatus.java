package com.dimeng.p2p.common.enums;

/**
 * 职业状态
 */
public enum Zystatus {
	/**
	 * 工薪阶层
	 */
	GXJC("工薪阶层"),
	/**
	 * 私营企业主
	 */
	SYQYZ("私营企业主");

	protected final String name;

	private Zystatus(String name) {
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
