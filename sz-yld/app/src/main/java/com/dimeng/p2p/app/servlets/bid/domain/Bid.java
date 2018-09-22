package com.dimeng.p2p.app.servlets.bid.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class Bid implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 4919050458406952220L;

    private int id;
    
    /**类型标志*/
    private String flag;
    
    /**借款标题*/
    private String bidTitle;
    
    /**利率*/
    private String rate;
    
    /**筹款期限*/
    private int term;
    
    /**借款周期*/
    private int cycle;
    
    /**借款金额*/
    private String amount;
    
    /**剩余可借款*/
    private String remainAmount;
    
    /**理财方式*/
    private String financialType;
    
    /**还款方式*/
    private String paymentType;
    
    /**状态*/
    private String status;
    
    /**是否按天计算*/
    private String isDay;
    
    /**发布时间，预发布时有效*/
    private String publicDate;
    
    /**
     * 是否为新手标
     */
    private String isXsb = "false";
    
    /**
     * 是否为奖励标
     */
    private String isJlb = "false";
    
    /**
     * 奖励利率
     */
    private BigDecimal jlRate = new BigDecimal(0);
    
    /**
     * 是否为推荐标
     */
    private String isTjb = "false";
    
    /**
     * 是否为预发布
     */
    private String isYfb = "false";
    
    /**进度*/
    private BigDecimal proess;
    
    /** 最低起投金额 */
    private String minBidingAmount;
    
    /**
     * 允许投资的终端类型
     */
    private String teminalType;
    
    /**
     * 数据库时间
     */
    private long timemp;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getFlag()
    {
        return flag;
    }
    
    public void setFlag(String flag)
    {
        this.flag = flag;
    }
    
    public String getBidTitle()
    {
        return bidTitle;
    }
    
    public void setBidTitle(String bidTitle)
    {
        this.bidTitle = bidTitle;
    }
    
    public String getRate()
    {
        return rate;
    }
    
    public void setRate(String rate)
    {
        this.rate = rate;
    }
    
    public int getTerm()
    {
        return term;
    }
    
    public void setTerm(int term)
    {
        this.term = term;
    }
    
    public int getCycle()
    {
        return cycle;
    }
    
    public void setCycle(int cycle)
    {
        this.cycle = cycle;
    }
    
    public String getAmount()
    {
        return amount;
    }
    
    public void setAmount(String amount)
    {
        this.amount = amount;
    }
    
    public String getRemainAmount()
    {
        return remainAmount;
    }
    
    public void setRemainAmount(String remainAmount)
    {
        this.remainAmount = remainAmount;
    }
    
    public String getFinancialType()
    {
        return financialType;
    }
    
    public void setFinancialType(String financialType)
    {
        this.financialType = financialType;
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
    
    public String getIsDay()
    {
        return isDay;
    }
    
    public void setIsDay(String isDay)
    {
        this.isDay = isDay;
    }
    
    public String getPublicDate()
    {
        return publicDate;
    }
    
    public void setPublicDate(String publicDate)
    {
        this.publicDate = publicDate;
    }
    
    public String getIsXsb()
    {
        return isXsb;
    }
    
    public void setIsXsb(String isXsb)
    {
        this.isXsb = isXsb;
    }
    
    public String getIsJlb()
    {
        return isJlb;
    }
    
    public void setIsJlb(String isJlb)
    {
        this.isJlb = isJlb;
    }
    
    public BigDecimal getJlRate()
    {
        return jlRate;
    }
    
    public void setJlRate(BigDecimal jlRate)
    {
        this.jlRate = jlRate;
    }
    
    public String getIsTjb()
    {
        return isTjb;
    }
    
    public void setIsTjb(String isTjb)
    {
        this.isTjb = isTjb;
    }
    
    public String getIsYfb()
    {
        return isYfb;
    }
    
    public void setIsYfb(String isYfb)
    {
        this.isYfb = isYfb;
    }

    public BigDecimal getProess()
    {
        return proess;
    }

    public void setProess(BigDecimal proess)
    {
        this.proess = proess;
    }

    public String getMinBidingAmount()
    {
        return minBidingAmount;
    }

    public void setMinBidingAmount(String minBidingAmount)
    {
        this.minBidingAmount = minBidingAmount;
    }

    @Override
    public String toString()
    {
        return "Bid [id=" + id + ", flag=" + flag + ", bidTitle=" + bidTitle + ", rate=" + rate + ", term=" + term
            + ", cycle=" + cycle + ", amount=" + amount + ", remainAmount=" + remainAmount + ", financialType="
            + financialType + ", paymentType=" + paymentType + ", status=" + status + ", isDay=" + isDay
            + ", publicDate=" + publicDate + ", isXsb=" + isXsb + ", isJlb=" + isJlb + ", jlRate=" + jlRate
            + ", isTjb=" + isTjb + ", isYfb=" + isYfb + ", proess=" + proess + ", minBidingAmount=" + minBidingAmount
            + "]";
    }

    public String getTeminalType()
    {
        return teminalType;
    }

    public void setTeminalType(String teminalType)
    {
        this.teminalType = teminalType;
    }

    public long getTimemp()
    {
        return timemp;
    }

    public void setTimemp(long timemp)
    {
        this.timemp = timemp;
    }
    
}
