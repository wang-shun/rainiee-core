/*
 * 文 件 名:  ExperienceInfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年12月2日
 */
package com.dimeng.p2p.app.servlets.user.domain;

import java.io.Serializable;

/**
 * 体验金信息
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月2日]
 */
public class ExperienceInfo  implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 460820345468387337L;

    /**
     * 体验金ID
     */
    private int experienceId;
    
    /**
     * 体验金金额
     */
    private String expAmount;
    
    /**
     * 赠送时间
     */
    private String beginDate;
    
    /**
     * 失效时间
     */
    private String endDate;
    
    /**
     * 体验金状态
     */
    private String status;
    
    /**
     * 体验金状态(中文)
     */
    private String statusDes;
    
    /**
     * 体验金投资的年化利率
     */
    private String rate;
    
    /**
     * 借款标名称
     */
    private String bidTitile;
    
    /**
     * 待收金额
     */
    private String dueInAmount;
    
    /**
     * 已收金额
     */
    private String receivedAmount;
    
    /**
     * 有效收益月份数
     */
    private int months;
    
    /**
     * 结清日期
     */
    private String receivedDate;
    
    /**
     * 体验金投资时间
     */
    private String bidDate;
    
    /**
     * 下一还款日
     */
    private String nextRepaymentDate;
    
    /**
     * 体验收益期
     */
    private String returnPeriod;
    
    public int getExperienceId()
    {
        return experienceId;
    }
    
    public void setExperienceId(int experienceId)
    {
        this.experienceId = experienceId;
    }
    
    public String getExpAmount()
    {
        return expAmount;
    }
    
    public void setExpAmount(String expAmount)
    {
        this.expAmount = expAmount;
    }
    
    public String getBeginDate()
    {
        return beginDate;
    }
    
    public void setBeginDate(String beginDate)
    {
        this.beginDate = beginDate;
    }
    
    public String getEndDate()
    {
        return endDate;
    }
    
    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public String getRate()
    {
        return rate;
    }
    
    public void setRate(String rate)
    {
        this.rate = rate;
    }
    
    public String getBidTitile()
    {
        return bidTitile;
    }
    
    public void setBidTitile(String bidTitile)
    {
        this.bidTitile = bidTitile;
    }
    
    public String getDueInAmount()
    {
        return dueInAmount;
    }
    
    public void setDueInAmount(String dueInAmount)
    {
        this.dueInAmount = dueInAmount;
    }
    
    public String getReceivedAmount()
    {
        return receivedAmount;
    }
    
    public void setReceivedAmount(String receivedAmount)
    {
        this.receivedAmount = receivedAmount;
    }
    
    public int getMonths()
    {
        return months;
    }
    
    public void setMonths(int months)
    {
        this.months = months;
    }
    
    public String getReceivedDate()
    {
        return receivedDate;
    }
    
    public void setReceivedDate(String receivedDate)
    {
        this.receivedDate = receivedDate;
    }
    
    public String getStatusDes()
    {
        return statusDes;
    }
    
    public void setStatusDes(String statusDes)
    {
        this.statusDes = statusDes;
    }
    
    public String getBidDate()
    {
        return bidDate;
    }
    
    public void setBidDate(String bidDate)
    {
        this.bidDate = bidDate;
    }
    
    public String getNextRepaymentDate()
    {
        return nextRepaymentDate;
    }
    
    public void setNextRepaymentDate(String nextRepaymentDate)
    {
        this.nextRepaymentDate = nextRepaymentDate;
    }
    
    public String getReturnPeriod()
    {
        return returnPeriod;
    }
    
    public void setReturnPeriod(String returnPeriod)
    {
        this.returnPeriod = returnPeriod;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "ExperienceInfo [experienceId=" + experienceId + ", expAmount=" + expAmount + ", beginDate=" + beginDate
            + ", endDate=" + endDate + ", status=" + status + ", statusDes=" + statusDes + ", rate=" + rate
            + ", bidTitile=" + bidTitile + ", dueInAmount=" + dueInAmount + ", receivedAmount=" + receivedAmount
            + ", months=" + months + ", receivedDate=" + receivedDate + ", bidDate=" + bidDate + ", nextRepaymentDate="
            + nextRepaymentDate + ", returnPeriod=" + returnPeriod + "]";
    }
    
    
}
