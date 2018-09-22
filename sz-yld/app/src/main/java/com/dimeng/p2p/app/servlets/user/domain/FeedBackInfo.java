/*
 * 文 件 名:  FeedBackInfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年3月22日
 */
package com.dimeng.p2p.app.servlets.user.domain;

import java.io.Serializable;

/**
 * 
 * 反馈信息实现类
 * @author  zhoulantao
 * @version  [版本号, 2016年3月22日]
 */
public class FeedBackInfo  implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 9025923236261645595L;

    /**
     * 反馈信息ID
     */
    private int feedId;
    
    /**
     * 反馈人ID
     */
    private int userId;
    
    /**
     * 反馈内容
     */
    private String feedContent;
    
    /**
     * 反馈时间
     */
    private String createTime;
    
    /**
     * 平台回复内容
     */
    private String backContent;
    
    /**
     * 是否已回复 yes:已回复；no:未回复;
     */
    private String isBack;
    
    /**
     * 回复用户ID
     */
    private int backUserId;
    
    /**
     * 回复时间
     */
    private String backTime;
    
    /**
     * 是否已发布
     */
    private String isPublish;
    
    public int getFeedId()
    {
        return feedId;
    }
    
    public void setFeedId(int feedId)
    {
        this.feedId = feedId;
    }
    
    public int getUserId()
    {
        return userId;
    }
    
    public void setUserId(int userId)
    {
        this.userId = userId;
    }
    
    public String getFeedContent()
    {
        return feedContent;
    }
    
    public void setFeedContent(String feedContent)
    {
        this.feedContent = feedContent;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    public String getBackContent()
    {
        return backContent;
    }
    
    public void setBackContent(String backContent)
    {
        this.backContent = backContent;
    }
    
    public String getIsBack()
    {
        return isBack;
    }
    
    public void setIsBack(String isBack)
    {
        this.isBack = isBack;
    }
    
    public int getBackUserId()
    {
        return backUserId;
    }
    
    public void setBackUserId(int backUserId)
    {
        this.backUserId = backUserId;
    }
    
    public String getBackTime()
    {
        return backTime;
    }
    
    public void setBackTime(String backTime)
    {
        this.backTime = backTime;
    }
    
    public String getIsPublish()
    {
        return isPublish;
    }
    
    public void setIsPublish(String isPublish)
    {
        this.isPublish = isPublish;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "FeedBackInfo [feedId=" + feedId + ", userId=" + userId + ", feedContent=" + feedContent
            + ", createTime=" + createTime + ", backContent=" + backContent + ", isBack=" + isBack + ", backUserId="
            + backUserId + ", backTime=" + backTime + ", isPublish=" + isPublish + "]";
    }
    
    
}
