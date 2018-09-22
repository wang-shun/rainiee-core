package com.dimeng.p2p.modules.base.console.service.query;

import java.sql.Timestamp;

import com.dimeng.p2p.S50.enums.T5011_F02;
import com.dimeng.p2p.common.enums.ArticlePublishStatus;

/**
 * 帮助中心问题类型.
 * 
 */
public abstract interface QuestionQuery {
	
	
	/**
	 * 根据问题.
	 * 
	 * @return {@link String} 问题
	 */
	public abstract int getQuestionTypeID();
	
	/**
	 * 根据问题.
	 * 
	 * @return {@link String} 问题
	 */
	public abstract String getQuestion();


	/**
	 * 根据文章发布状态.
	 * 
	 * @return {@link ArticlePublishStatus} 文章发布状态
	 */
	public abstract ArticlePublishStatus getPublishStatus();

	/**
	 * 根据文章发布时间
	 * 
	 * @return
	 */
	public abstract Timestamp publishTime();
	
	/**
	 * 根据发布者.
	 * 
	 * @return {@link String} 发布者
	 */
	public abstract String getPublisher();
	
	/**
	 * 根据问题类型
	 * 
	 * @return {@link ArticlePublishStatus} 获取问题类型
	 */
	public abstract String getQuestionType();
	
	/**
	 * 根据文章类型查询
	 * 
	 * @return
	 */
	public abstract T5011_F02 getArticleType();
	
	/**
	 * 创建时间查询,大于等于匹配.
	 * 
	 * @return {@link Timestamp} 非{@code null}有效
	 */
	public abstract Timestamp getCreateTimeStart();

	/**
	 * 创建时间查询,小于等于匹配.
	 * 
	 * @return {@link Timestamp} 非{@code null}有效
	 */
	public abstract Timestamp getCreateTimeEnd();

	/**
	 * 发布时间查询,大于等于匹配.
	 * 
	 * @return {@link Timestamp} 非{@code null}有效
	 */
	public abstract Timestamp getPublishTimeStart();

	/**
	 * 发布时间查询,小于等于匹配.
	 * 
	 * @return {@link Timestamp} 非{@code null}有效
	 */
	public abstract Timestamp getPublishTimeEnd();
	
}
