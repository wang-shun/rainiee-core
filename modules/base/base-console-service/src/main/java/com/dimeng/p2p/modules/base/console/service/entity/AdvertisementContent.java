package com.dimeng.p2p.modules.base.console.service.entity;
/**
 * 广告信息——内容
 *
 */
public interface AdvertisementContent extends Advertisement{
	/**
	 * 获取广告内容.
	 * 
	 * @return {@link String} 广告内容
	 */
	public abstract String getContent();
}
