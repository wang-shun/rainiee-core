/*
 * 文 件 名:  BidDynamic.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年3月25日
 */
package com.dimeng.p2p.app.servlets.bid.domain;

import java.io.Serializable;

/**
 * 标的动态
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年3月25日]
 */
public class BidDynamic implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -5906448612175692657L;

    /**
     * 动态ID
     */
    private int id;
    
    /**
     * 发布用户ID
     */
    private int userId;
    
    /**
     * 标ID
     */
    private int bidId;
    
    /**
     * 标题
     */
    private String title;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 简要介绍
     */
    private String about;
    
    /**
     * 发布时间
     */
    private String publishTime;
    
    /**
     * 标题时间 
     */
    private String titleDate;
    
    /**
     * 更多
     */
    private String more;
     
    /**
     * 发布者
     */
    private String sysName;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public int getBidId()
    {
        return bidId;
    }

    public void setBidId(int bidId)
    {
        this.bidId = bidId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getAbout()
    {
        return about;
    }

    public void setAbout(String about)
    {
        this.about = about;
    }

    public String getPublishTime()
    {
        return publishTime;
    }

    public void setPublishTime(String publishTime)
    {
        this.publishTime = publishTime;
    }

    public String getTitleDate()
    {
        return titleDate;
    }

    public void setTitleDate(String titleDate)
    {
        this.titleDate = titleDate;
    }

    public String getMore()
    {
        return more;
    }

    public void setMore(String more)
    {
        this.more = more;
    }

    public String getSysName()
    {
        return sysName;
    }

    public void setSysName(String sysName)
    {
        this.sysName = sysName;
    }

    @Override
    public String toString()
    {
        return "BidDynamic [id=" + id + ", userId=" + userId + ", bidId=" + bidId + ", title=" + title + ", status="
            + status + ", about=" + about + ", publishTime=" + publishTime + ", titleDate=" + titleDate + ", more="
            + more + ", sysName=" + sysName + "]";
    }
    
}
