package com.dimeng.p2p.modules.base.console.service.query;

import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.ArticlePublishStatus;

/**
 * 帮助中心问题类型.
 * 
 */
public abstract interface QuestionType {
	/**
	 * 获取问题类型.
	 * 
	 * @return {@link String} 问题类型
	 */
	public abstract String getQuestionType();


	/**
	 * 获取文章发布状态.
	 * 
	 * @return {@link ArticlePublishStatus} 文章发布状态
	 */
	public abstract ArticlePublishStatus getPublishStatus();

	/**
	 * 获取文章发布时间
	 * 
	 * @return
	 */
	public abstract Timestamp publishTime();
}
