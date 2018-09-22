/*
 * 文 件 名:  GetUserScoreInfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年1月28日
 */
package com.dimeng.p2p.app.servlets.user.domain.mall;

import java.io.Serializable;

/**
 * 用户积分获取记录
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月28日]
 */
public class GetUserScoreInfo implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1683250729906358400L;

    /**
     * 获取积分的时间
     */
    private String getDate;
    
    /**
     * 获取的积分
     */
    private int getScore;
    
    /**
     * 获取积分的途径
     */
    private String getType;
    
    public String getGetDate()
    {
        return getDate;
    }
    
    public void setGetDate(String getDate)
    {
        this.getDate = getDate;
    }
    
    public int getGetScore()
    {
        return getScore;
    }
    
    public void setGetScore(int getScore)
    {
        this.getScore = getScore;
    }
    
    public String getGetType()
    {
        return getType;
    }
    
    public void setGetType(String getType)
    {
        this.getType = getType;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "GetUserScoreInfo [getDate=" + getDate + ", getScore=" + getScore + ", getType=" + getType + "]";
    }
}
