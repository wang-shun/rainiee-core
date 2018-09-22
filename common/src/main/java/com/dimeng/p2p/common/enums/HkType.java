package com.dimeng.p2p.common.enums;

/**
 * 还款方式
 * 
 */
public enum HkType {
	MYHXDQHB("等额本息"), DEBX("每月还息，到期还本");

	protected final String name;

	private HkType(String name) {
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
