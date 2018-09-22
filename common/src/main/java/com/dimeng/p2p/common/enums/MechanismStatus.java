package com.dimeng.p2p.common.enums;

public enum MechanismStatus {
	SDRZ("实地认证"), DYDB("抵押担保");

	protected final String name;

	private MechanismStatus(String name) {
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
