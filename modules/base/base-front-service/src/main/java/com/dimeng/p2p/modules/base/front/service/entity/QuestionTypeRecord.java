package com.dimeng.p2p.modules.base.front.service.entity;

import java.sql.Timestamp;

import com.dimeng.p2p.S50.enums.T5011_F02;
import com.dimeng.p2p.common.enums.ArticlePublishStatus;

public class QuestionTypeRecord {
    /**
     * 问题类型ID
     */
    public int id;

    /**
     * 文章类型
     */
    public T5011_F02 articleType;

    /**
     * 发布状态
     */
    public ArticlePublishStatus publishStatus;

    /**
     * 问题类型
     */
    public String questionType;

    /**
     * 创建时间
     */
    public Timestamp createtime;

    /**
     * 发布时间
     */
    public Timestamp publishTime;

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
     * 问题及答案集合
     */
    public QuestionRecord[] questionRecord;
}