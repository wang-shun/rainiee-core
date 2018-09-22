package com.dimeng.p2p.app.servlets.creditor.domain;

import java.io.Serializable;

public class Creditor implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 635528829772208479L;

    private int id;
    
    /**债券id*/
    private int creId;
    
    /**标id*/
    private int bidId;
    
    /**类型标志*/
    private String flag;
    
    /**债券标题*/
    public String creditorTitle;
    
    /**年化利率*/
    public String rate;
    
    /**还款总期数*/
    public int cycle;
    
    /**剩余周期*/
    public int remainCycle;
    
    /**理财方式*/
    public String financialType;
    
    /**债权价值*/
    public String creditorVal;
    
    /**出售价格*/
    public String salePrice;
    
    /**待收本息*/
    public String revInterest;
    
    /**还款方式*/
    public String paymentType;
    
    /**状态*/
    public String status;
    
    /**是否按天*/
    public String isDay;
    
    /**
     * 奖励利率
     */
    public String jlRate;
    
    /**天数*/
    private String days;
    
    public String getIsDay()
    {
        return isDay;
    }
    
    public void setIsDay(String isDay)
    {
        this.isDay = isDay;
    }
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getCreId()
    {
        return creId;
    }
    
    public void setCreId(int creId)
    {
        this.creId = creId;
    }
    
    public String getFlag()
    {
        return flag;
    }
    
    public void setFlag(String flag)
    {
        this.flag = flag;
    }
    
    public int getBidId()
    {
        return bidId;
    }
    
    public void setBidId(int bidId)
    {
        this.bidId = bidId;
    }
    
    public String getCreditorTitle()
    {
        return creditorTitle;
    }
    
    public void setCreditorTitle(String creditorTitle)
    {
        this.creditorTitle = creditorTitle;
    }
    
    public String getRate()
    {
        return rate;
    }
    
    public void setRate(String rate)
    {
        this.rate = rate;
    }
    
    public int getCycle()
    {
        return cycle;
    }
    
    public void setCycle(int cycle)
    {
        this.cycle = cycle;
    }
    
    public int getRemainCycle()
    {
        return remainCycle;
    }
    
    public void setRemainCycle(int remainCycle)
    {
        this.remainCycle = remainCycle;
    }
    
    public String getFinancialType()
    {
        return financialType;
    }
    
    public void setFinancialType(String financialType)
    {
        this.financialType = financialType;
    }
    
    public String getCreditorVal()
    {
        return creditorVal;
    }
    
    public void setCreditorVal(String creditorVal)
    {
        this.creditorVal = creditorVal;
    }
    
    public String getSalePrice()
    {
        return salePrice;
    }
    
    public void setSalePrice(String salePrice)
    {
        this.salePrice = salePrice;
    }
    
    public String getRevInterest()
    {
        return revInterest;
    }
    
    public void setRevInterest(String revInterest)
    {
        this.revInterest = revInterest;
    }
    
    public String getPaymentType()
    {
        return paymentType;
    }
    
    public void setPaymentType(String paymentType)
    {
        this.paymentType = paymentType;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public String getJlRate()
    {
        return jlRate;
    }
    
    public void setJlRate(String jlRate)
    {
        this.jlRate = jlRate;
    }
    
    public String getDays()
    {
        return days;
    }
    
    public void setDays(String days)
    {
        this.days = days;
    }

    @Override
    public String toString()
    {
        return "Creditor [id=" + id + ", creId=" + creId + ", bidId=" + bidId + ", flag=" + flag + ", creditorTitle="
            + creditorTitle + ", rate=" + rate + ", cycle=" + cycle + ", remainCycle=" + remainCycle
            + ", financialType=" + financialType + ", creditorVal=" + creditorVal + ", salePrice=" + salePrice
            + ", revInterest=" + revInterest + ", paymentType=" + paymentType + ", status=" + status + ", isDay="
            + isDay + ", jlRate=" + jlRate + ", days=" + days + "]";
    }
    
}
