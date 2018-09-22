package com.dimeng.p2p.modules.base.console.service.query;

import java.sql.Timestamp;

import com.dimeng.p2p.S50.enums.T5011_F02;
import com.dimeng.p2p.common.enums.ArticlePublishStatus;
import com.dimeng.p2p.common.enums.ArticleType;

/**
 * 文章查询条件接口.
 */
public abstract interface ArticleQuery
{
    
    /**
     * 类型查询,等于匹配.
     * 
     * @return {@link ArticleType} 非{@code null}有效
     */
    public abstract T5011_F02 getType();
    
    /**
     * 发布状态查询,等于匹配.
     * 
     * @return {@link ArticlePublishStatus} 非{@code null}有效
     */
    public abstract ArticlePublishStatus getPublishStatus();
    
    /**
     * 标题查询,全模糊匹配.
     * 
     * @return {@link String} 非空值有效
     */
    public abstract String getTitle();
    
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
    
    /**
     * 最后更新时间查询,大于等于匹配.
     * 
     * @return {@link Timestamp} 非{@code null}有效
     */
    public abstract Timestamp getLastUpdateTimeStart();
    
    /**
     * 最后更新时间查询,小于等于匹配.
     * 
     * @return {@link Timestamp} 非{@code null}有效
     */
    public abstract Timestamp getLastUpdateTimeEnd();
    
    /**
     * 来源查询,全模糊匹配.
     * 
     * @return {@link String} 非空值有效
     */
    public abstract String getSource();
    
    /**
     * 摘要查询,全模糊匹配.
     * 
     * @return {@link String} 非空值有效
     */
    public abstract String getSummary();
    
    /**
     * 发布者查询,全模糊匹配.
     * 
     * @return {@link String} 非空值有效
     */
    public abstract String getPublisherName();
}
