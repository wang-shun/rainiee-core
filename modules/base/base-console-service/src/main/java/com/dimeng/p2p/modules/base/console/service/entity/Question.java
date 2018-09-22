package com.dimeng.p2p.modules.base.console.service.entity;

import java.sql.Timestamp;

import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.p2p.common.enums.ArticlePublishStatus;

/**
 * 问题类型.
 * 
 */
public abstract interface Question {
	
	/**
	 * 获取问题类型ID..
	 * 
	 * @return {@link int} 问题类型ID
	 */
	public abstract int getQuestionTypeID();
	
	/**
	 * 获取问题类型..
	 * 
	 * @return {@link String} 问题类型标题
	 */
	public abstract String getQuestionType();

	/**
	 * 获取问题类型发布状态.
	 * 
	 * @return {@link ArticlePublishStatus} 问题类型发布状态
	 */
	public abstract ArticlePublishStatus getPublishStatus();

	/**
	 * 获取封面图片上传文件.
	 * 
	 * @return {@link UploadFile} 上传文件.
	 * @throws Throwable
	 */
	public abstract UploadFile getImage() throws Throwable;
	/**
	 * 获取问题类型发布时间
	 * 
	 * @return
	 */
	public abstract Timestamp publishTime();
	
	
	/**
	 * 获取问题..
	 * 
	 */
	public abstract String getQuestion();
	
	/**
	 * 获取问题答案..
	 * 
	 */
	public abstract String getQuestionAnswer();
	
}
