package com.dimeng.p2p.common.enums;

/**
 * 还款状态.
 * 
 */
public enum RestoreStatus {

	YFK("还款中"), YJQ("已结清"), S("逾期"), YZYQ("严重逾期");

	protected final String name;

	private RestoreStatus(String name) {
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
