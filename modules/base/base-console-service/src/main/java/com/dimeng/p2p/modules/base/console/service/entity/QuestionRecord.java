package com.dimeng.p2p.modules.base.console.service.entity;

import java.sql.Timestamp;

import com.dimeng.p2p.S50.enums.T5011_F02;
import com.dimeng.p2p.common.enums.ArticlePublishStatus;

public class QuestionRecord
{
    /**
     * 问题类型ID
     */
    public int id;
    
    /**
     * 问题类型ID
     */
    public int questionTypeID;
    
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
     * 问题
     */
    public String question;
    
    /**
     * 问题答案
     */
    public String questionAnswer;
    
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
     * 最后修改时间
     */
    public Timestamp lastModifyTime;
}