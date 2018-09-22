package com.dimeng.p2p.modules.base.console.service.entity;

import java.sql.Timestamp;

import com.dimeng.p2p.S50.enums.T5010_F04;
import com.dimeng.p2p.S50.enums.T5011_F02;
import com.dimeng.p2p.common.enums.ArticlePublishStatus;

public class ArticleRecord {
	/**
	 * 文章ID
	 */
	public int id;

	/**
	 * 文章类型
	 */
	public T5011_F02 articleType;

	/**
	 * 浏览次数
	 */
	public int viewTimes;

	/**
	 * 文章排序值
	 */
	public int sortIndex;

	/**
	 * 发布状态
	 */
	public ArticlePublishStatus publishStatus;

	/**
	 * 文章标题
	 */
	public String title;

	/**
	 * 创建时间
	 */
	public Timestamp createtime;

	/**
	 * 发布时间
	 */
	public Timestamp publishTime;
    
    /**
     * 更新时间
     */
    public Timestamp updateTime;

	/**
	 * 文章来源
	 */
	public String source;
	/**
	 * 摘要
	 */
	public String summary;
	/**
	 * 封面图片编码
	 */
	public String imageCode;
	/**
	 * 发布者ID
	 */
	public int publisherId;
	/**
	 * 发布者姓名
	 */
	public String publisherName;
	
	/**
	 * 文章类别id
	 */
	public int categoryId;
	/**
	 * 文章类别编码
	 */
	public String categoryCode;
	
	/**
	 * 文章类别名称
	 */
	public String categoryName;
	
	/**
	 * 文章类别状态
	 */
	public T5010_F04 categoryStatus;

}