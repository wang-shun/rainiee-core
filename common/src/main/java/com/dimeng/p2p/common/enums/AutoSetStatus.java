package com.dimeng.p2p.common.enums;

/**
 * 自动投资标识
 * 
 * @author gaoshaolong
 * 
 */
public enum AutoSetStatus {
	QY("启用"), TY("停用");

	protected final String name;

	private AutoSetStatus(String name) {
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
