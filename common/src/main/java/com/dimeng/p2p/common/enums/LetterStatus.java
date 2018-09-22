package com.dimeng.p2p.common.enums;

/**
 * 站内信状态
 */
public enum LetterStatus {
	YD("已读"), WD("未读"), SC("删除");

	protected final String name;

	private LetterStatus(String name) {
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
