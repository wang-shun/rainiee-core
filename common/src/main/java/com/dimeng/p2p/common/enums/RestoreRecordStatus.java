package com.dimeng.p2p.common.enums;

/**
 * 还款记录状态.
 * 
 */
public enum RestoreRecordStatus {

	WH("未还"), YH("已还");

	protected final String name;

	private RestoreRecordStatus(String name) {
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
