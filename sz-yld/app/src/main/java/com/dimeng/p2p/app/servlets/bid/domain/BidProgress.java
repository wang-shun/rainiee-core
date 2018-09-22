/*
 * 文 件 名:  BidProgres.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年11月25日
 */
package com.dimeng.p2p.app.servlets.bid.domain;

import java.io.Serializable;

/**
 * 投资进度
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月25日]
 */
public class BidProgress implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 4623560586630159816L;

    /**
     * 标题时间
     */
    private String titleTime;
    
    /**
     * 简介
     */
    private String introduction;
    
    /**
     * 更多
     */
    private String moreUrl;
    
    public String getTitleTime()
    {
        return titleTime;
    }
    
    public void setTitleTime(String titleTime)
    {
        this.titleTime = titleTime;
    }
    
    public String getIntroduction()
    {
        return introduction;
    }
    
    public void setIntroduction(String introduction)
    {
        this.introduction = introduction;
    }
    
    public String getMoreUrl()
    {
        return moreUrl;
    }
    
    public void setMoreUrl(String moreUrl)
    {
        this.moreUrl = moreUrl;
    }

    @Override
    public String toString()
    {
        return "BidProgress [titleTime=" + titleTime + ", introduction=" + introduction + ", moreUrl=" + moreUrl + "]";
    }
    
}
