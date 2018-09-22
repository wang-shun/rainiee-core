/*
 * 文 件 名:  OperateReport.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2018年2月1日
 */
package com.dimeng.p2p.modules.base.console.service.entity;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.common.enums.ArticlePublishStatus;

/**
 * <运营报告>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2018年2月1日]
 */
public class OperateReport extends AbstractEntity
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1709821669564265136L;
    
    /**
     * 文章ID
     */
    public int id;
    
    /**
     * 发布状态
     */
    public ArticlePublishStatus publishStatus;
    
    /**
     * 文章标题
     */
    public String title;
    /**
     * 封面图片
     */
    public String imageCode;
    
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
     * 发布者姓名
     */
    public String publisherName;
    
    /**
     * pdf地址
     */
    public String pdfUrl;
    
    
}
