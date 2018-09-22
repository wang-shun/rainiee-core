/*
 * 文 件 名:  OperateReportQuery.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2018年2月1日
 */
package com.dimeng.p2p.modules.base.console.service.query;

import java.sql.Timestamp;

import com.dimeng.p2p.S50.enums.T5011_F02;
import com.dimeng.p2p.common.enums.ArticlePublishStatus;
import com.dimeng.p2p.common.enums.ArticleType;

/**
 * <运营报告>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2018年2月1日]
 */
public interface OperateReportQuery
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
     * 发布者查询,全模糊匹配.
     * 
     * @return {@link String} 非空值有效
     */
    public abstract String getPublisherName();
    
    /**
	 * 修改时间查询,大于等于匹配.
	 * 
	 * @return {@link Timestamp} 非{@code null}有效
	 */
	public abstract Timestamp getLastUpdateTimeStart();

	/**
	 * 修改时间查询,小于等于匹配.
	 * 
	 * @return {@link Timestamp} 非{@code null}有效
	 */
	public abstract Timestamp getLastUpdateTimeEnd();
}
