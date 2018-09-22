/*
 * 文 件 名:  MyScoreExchangeInfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年1月28日
 */
package com.dimeng.p2p.app.servlets.user.domain.mall;

import java.io.Serializable;

/**
 * 我的积分兑换记录
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月28日]
 */
public class MyScoreExchangeInfo implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 4426880794284793924L;

    /**
     * 商品名称
     */
    private String comdName;
    
    /**
     * 商品数量
     */
    private int comdNum;
    
    /**
     * 商品积分
     */
    private int comdScore;
    
    /**
     * 订单时间
     */
    private String orderTime;
    
    public String getComdName()
    {
        return comdName;
    }
    
    public void setComdName(String comdName)
    {
        this.comdName = comdName;
    }
    
    public int getComdNum()
    {
        return comdNum;
    }
    
    public void setComdNum(int comdNum)
    {
        this.comdNum = comdNum;
    }
    
    public int getComdScore()
    {
        return comdScore;
    }
    
    public void setComdScore(int comdScore)
    {
        this.comdScore = comdScore;
    }
    
    public String getOrderTime()
    {
        return orderTime;
    }
    
    public void setOrderTime(String orderTime)
    {
        this.orderTime = orderTime;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "MyScoreExchangeInfo [comdName=" + comdName + ", comdNum=" + comdNum + ", comdScore=" + comdScore
            + ", orderTime=" + orderTime + "]";
    }
    
}
