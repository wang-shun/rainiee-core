package com.dimeng.p2p.common.enums;

/**
 * 散标发布状态
 */
public enum BidPublishStatus {
	S("是"), F("否");
	protected final String name;

	private BidPublishStatus(String name) {
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
