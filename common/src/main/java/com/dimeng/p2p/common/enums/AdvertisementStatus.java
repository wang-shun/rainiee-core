package com.dimeng.p2p.common.enums;

/**
 * 广告状态
 * 
 * 
 */
public enum AdvertisementStatus {
	/**
	 * 待上架
	 */
	DSJ("待上架"),
	/**
	 * 已上架
	 */
	YSJ("已上架"),
	/**
	 * 已下架
	 */
	YXJ("已下架");
	protected final String name;

	private AdvertisementStatus(String name) {
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
