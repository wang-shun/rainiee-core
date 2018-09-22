package com.dimeng.p2p.common.enums;

public enum AttestationStatus {
	/**
	 * 未验证
	 */
	WYZ("未验证"),
	/**
	 * 已验证(通过)
	 */
	YYZ("已验证"),
	/**
	 * 驳回
	 */
	BH("驳回");

	protected final String name;

	private AttestationStatus(String name) {
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
