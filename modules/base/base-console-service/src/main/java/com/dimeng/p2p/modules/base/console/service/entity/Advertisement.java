package com.dimeng.p2p.modules.base.console.service.entity;

import java.sql.Timestamp;

import com.dimeng.framework.http.upload.UploadFile;
/**
 * 广告信息
 *
 */
public interface Advertisement {

	/**
	 * 获取广告标题.
	 * 
	 * @return {@link String}
	 */
	public String getTitle();

	/**
	 * 获取封面图片.
	 * 
	 * @return {@link UploadFile}
	 * @throws Throwable
	 */
	public UploadFile getImage() throws Throwable;

	/**
	 * 获取链接地址.
	 * 
	 * @return {@link String}
	 */
	public String getURL();

	/**
	 * 获取排序值.
	 * 
	 * @return {@code int}
	 */
	public int getSortIndex();

	/**
	 * 获取上架时间.
	 * 
	 * @return {@link Timestamp}
	 */
	public Timestamp getShowTime();

	/**
	 * 获取下架时间.
	 * 
	 * @return {@link Timestamp}
	 */
	public Timestamp getUnshowTime();
}
