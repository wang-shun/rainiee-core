/*
 * 文 件 名:  BidRankInfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年11月21日
 */
package com.dimeng.p2p.app.servlets.platinfo.domain;

import java.io.Serializable;

/**
 * 排行榜详情
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月21日]
 */
public class BidRankInfo implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 2452922574399680490L;

    /**
     * 排行榜名次
     */
    private int rankId;
    
    /**
     * 用户名
     */
    private String userName;
    
    /**
     * 用户投资金额
     */
    private String amount;
    
    public int getRankId()
    {
        return rankId;
    }
    
    public void setRankId(int rankId)
    {
        this.rankId = rankId;
    }
    
    public String getUserName()
    {
        return userName;
    }
    
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    public String getAmount()
    {
        return amount;
    }
    
    public void setAmount(String amount)
    {
        this.amount = amount;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "BidRankInfo [rankId=" + rankId + ", userName=" + userName + ", amount=" + amount + "]";
    }
    
}
