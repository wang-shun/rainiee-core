package com.dimeng.p2p.app.servlets.user.domain;

import java.io.Serializable;

/**
 * 
 * @author  
 * @version  [版本号, 2016年06月03日]
 */
public class UserCreditor implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 7906491462756710949L;
    
    /**债券的ID*/
    private int bidId;
    
    /**债券的编号*/
    private String creditorNo;
    
    /**利率*/
    private String rate;
    
    /**借款总金额*/
    private String totalAmount;
    
    /**投资金额*/
    private String invAmount;
    
    /**剩余可投金额*/
    private String remainAmount;
    
    /**已赚金额*/
    private String earnAmount;
    
    /**结清日期*/
    private String endDate;
    
    /**待收本息*/
    private String revInterest;
    
    /**总期数*/
    private int totalTerm;
    
    /**已还期数*/
    private int backTerm;
    
    /**下个还款日期*/
    private String backTime;
    
    /*** 是否为按天借款,S:是;F:否*/
    private String isDay;
    
    /**剩余时间*/
    private String surTime;
    
    /**结清方式*/
    private String payType;
    
    public String getIsDay()
    {
        return isDay;
    }
    
    public void setIsDay(String isDay)
    {
        this.isDay = isDay;
    }
    
    public int getBidId()
    {
        return bidId;
    }
    
    public void setBidId(int bidId)
    {
        this.bidId = bidId;
    }
    
    public String getInvAmount()
    {
        return invAmount;
    }
    
    public void setInvAmount(String invAmount)
    {
        this.invAmount = invAmount;
    }
    
    public String getCreditorNo()
    {
        return creditorNo;
    }
    
    public void setCreditorNo(String creditorNo)
    {
        this.creditorNo = creditorNo;
    }
    
    public String getRate()
    {
        return rate;
    }
    
    public void setRate(String rate)
    {
        this.rate = rate;
    }
    
    public String getEarnAmount()
    {
        return earnAmount;
    }
    
    public void setEarnAmount(String earnAmount)
    {
        this.earnAmount = earnAmount;
    }
    
    public String getEndDate()
    {
        return endDate;
    }
    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }
    
    public String getRevInterest()
    {
        return revInterest;
    }
    public void setRevInterest(String revInterest)
    {
        this.revInterest = revInterest;
    }
    
    public int getTotalTerm()
    {
        return totalTerm;
    }
    
    public void setTotalTerm(int totalTerm)
    {
        this.totalTerm = totalTerm;
    }
    
    public int getBackTerm()
    {
        return backTerm;
    }
    
    public void setBackTerm(int backTerm)
    {
        this.backTerm = backTerm;
    }
    
    public String getBackTime()
    {
        return backTime;
    }
    
    public void setBackTime(String backTime)
    {
        this.backTime = backTime;
    }
    
    public String getSurTime()
    {
        return surTime;
    }
    
    public void setSurTime(String surTime)
    {
        this.surTime = surTime;
    }
    
    public String getTotalAmount()
    {
        return totalAmount;
    }
    
    public void setTotalAmount(String totalAmount)
    {
        this.totalAmount = totalAmount;
    }
    
    public String getRemainAmount()
    {
        return remainAmount;
    }
    
    public void setRemainAmount(String remainAmount)
    {
        this.remainAmount = remainAmount;
    }
    
    public String getPayType()
    {
        return payType;
    }
    
    public void setPayType(String payType)
    {
        this.payType = payType;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "UserCreditor [bidId=" + bidId + ", creditorNo=" + creditorNo + ", rate=" + rate + ", totalAmount="
            + totalAmount + ", invAmount=" + invAmount + ", remainAmount=" + remainAmount + ", earnAmount=" + earnAmount
            + ", endDate=" + endDate + ", revInterest=" + revInterest + ", totalTerm=" + totalTerm + ", backTerm="
            + backTerm + ", backTime=" + backTime + ", isDay=" + isDay + ", surTime=" + surTime + ", payType=" + payType
            + "]";
    }
    
}
