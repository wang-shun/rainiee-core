package com.dimeng.p2p.common.enums;

/**
 * 认证状态.
 * 
 */
public enum AttestationState {
	/**
	 * 未验证
	 */
	WYZ("未验证"),
	/**
	 * 待审核
	 */
	DSH("待审核"),
	/**
	 * 已验证
	 */
	YYZ("已验证"),
	/**
	 * 未通过
	 */
	WTG("未通过");

	protected final String name;

	private AttestationState(String name) {
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
