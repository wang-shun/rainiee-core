package com.dimeng.p2p.modules.base.console.service.entity;

import java.sql.Timestamp;

import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.p2p.common.enums.ArticlePublishStatus;

/**
 * 文章信息.
 * 
 */
public abstract interface Article {
	/**
	 * 获取文章标题.
	 * 
	 * @return {@link String} 文章标题
	 */
	public abstract String getTitle();

	/**
	 * 获取文章排序值.
	 * 
	 * @return {@code int} 文章排序值
	 */
	public abstract int getSortIndex();

	/**
	 * 获取文章发布状态.
	 * 
	 * @return {@link ArticlePublishStatus} 文章发布状态
	 */
	public abstract ArticlePublishStatus getPublishStatus();

	/**
	 * 获取文章来源.
	 * 
	 * @return {@link String} 文章来源
	 */
	public abstract String getSource();

	/**
	 * 获取文章内容.
	 * 
	 * @return {@link String} 文章内容
	 */
	public abstract String getContent();

	/**
	 * 获取文章摘要.
	 * 
	 * @return {@link String} 文章摘要
	 */
	public abstract String getSummary();

	/**
	 * 获取封面图片上传文件.
	 * 
	 * @return {@link UploadFile} 上传文件.
	 * @throws Throwable
	 */
	public abstract UploadFile getImage() throws Throwable;
	/**
	 * 获取文章发布时间
	 * 
	 * @return
	 */
	public abstract Timestamp publishTime();
}
