package com.dimeng.p2p.modules.base.console.service.entity;

import com.dimeng.framework.http.upload.UploadFile;
/**
 * 合作伙伴信息
 *
 */
public interface Partner {

	/**
	 * 获取公司名字
	 * 
	 * @return {@link String}
	 */
	public String getName();

	/**
	 * 获取排序值.
	 * 
	 * @return {@code int}
	 */
	public int getSortIndex();

	/**
	 * 获取链接地址.
	 * 
	 * @return {@link String}
	 */
	public String getURL();

	/**
	 * 获取图片.
	 * 
	 * @return {@link UploadFile}
	 * @throws Throwable
	 */
	public UploadFile getImage() throws Throwable;

	/**
	 * 获取公司地址.
	 * 
	 * @return {@link String}
	 */
	public String getAddress();

	/**
	 * 获取公司简介.
	 * 
	 * @return {@link String}
	 */
	public String getDescription();

}
