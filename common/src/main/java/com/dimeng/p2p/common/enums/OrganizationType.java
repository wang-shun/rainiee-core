package com.dimeng.p2p.common.enums;

/**
 * 机构类型.
 * 
 */
public enum OrganizationType {

	SDRZ("实地认证"), DYDB("抵押担保"), SDRZJDYDB("实地认证+抵押担保");

	protected final String name;

	private OrganizationType(String name) {
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
