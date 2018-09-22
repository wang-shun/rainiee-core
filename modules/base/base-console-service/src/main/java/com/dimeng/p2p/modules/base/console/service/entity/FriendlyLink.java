package com.dimeng.p2p.modules.base.console.service.entity;
/**
 * 友情链接信息
 *
 */
public abstract interface FriendlyLink {

	/**
	 * 获取名称.
	 * 
	 * @return {@link String}
	 */
	public abstract String getName();

	/**
	 * 获取链接地址.
	 * 
	 * @return {@link String}
	 */
	public abstract String getURL();

	/**
	 * 获取排序值.
	 * 
	 * @return {@code int}
	 */
	public abstract int getSortIndex();
}
