package com.dimeng.p2p.common.enums;

/**
 * 还款状态.
 * 
 */
public enum HkStatus {

	ZCHK("正常还款"), YQ("逾期"), YZYQ("严重逾期");

	protected final String name;

	private HkStatus(String name) {
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
