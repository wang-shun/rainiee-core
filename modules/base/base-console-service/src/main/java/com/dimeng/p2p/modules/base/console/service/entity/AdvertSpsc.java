package com.dimeng.p2p.modules.base.console.service.entity;

import com.dimeng.framework.http.upload.PartFile;
import com.dimeng.framework.http.upload.UploadFile;
/**
 * 视频信息
 *
 */
public abstract class AdvertSpsc {
	

	/**
	 * 获取标题.
	 * 
	 * @return {@link String}
	 */
	public abstract String getTitle();
	/**
	 * 获取置顶值.
	 * 
	 * @return {@link String}
	 */
	public abstract String getsortIndex();

	/**
	 * 获取文件
	 * 
	 * @return {@link PartFile}
	 * @throws Throwable
	 */
	public abstract UploadFile getFile() throws Throwable;

	/**
	 * 获取发布状态.
	 * 
	 * @return {@link String}
	 */
	public abstract String getStatus();

	/**
	 * 获取是否自动播放.
	 * 
	 * @return {@code int}
	 */
	public abstract int getIsAuto();
	
	/**
	 * 文件格式.
	 * 
	 * @return {@code String}
	 */
	public abstract String fileFormat();
	
	/**
	 * 文件大小.
	 * 
	 * @return {@code String}
	 */
	public abstract String fileSize();
}
