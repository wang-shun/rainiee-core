package com.dimeng.p2p.app.servlets.bid.domain;

import java.io.Serializable;

public class BidInfo implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 8511858129446135117L;

    /** 借款标题 */
    private String bidTitle;
    
    /** 投资人次 */
    private int peopleNum;
    
    /** 利率 */
    private String rate;
    
    /** 借款周期 */
    private int cycle;
    
    /** 借款期限 */
    private int term;
    
    /** 标的类型，1企业标，2个人标 */
    private int type;
    
    /** 借款总金额 */
    private String amount;
    
    /** 剩余可投金额 */
    private String remainAmount;
    
    /** 已投金额 */
    private String alrAmount;
    
    /** 还款方式方式 */
    private String paymentType;
    
    /** 还款状态 */
    private String status;
    
    /** 发布时间 */
    private String publishTime;
    
    /** 结束时间 */
    private String endTime;
    
    /** 借款类型 */
    private String financialType;
    
    /** 是否是按天计算 */
    private String isDay;
    
    /** 担保机构 */
    private String guarantee;
    
    /** 担保方式 */
    private String guaSch;
    
    /** 是否有担保 */
    private String isDanBao;
    
    /** 用户ID */
    private int userId;
    
    /** 最低起投金额 */
    private String minBidingAmount;
    
    /** 还款日期 */
    private String hkDate;
    
    /** 待还本息 */
    private String dhjeAmount;
    
    /** 剩余期数 */
    private int days;
    
    /** 最高投资金额 */
    private String maxBidingAmount;
    
    /**类型标志*/
    private String flag;
    
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
    private String jlRate;
    
    /**
     * 是否为推荐标
     */
    private String isTjb = "false";
    
    /**
     * 投资人风险等级
     */
    private String riskLevel;
    
    /**
     * 风险等级是否匹配
     */
    private boolean isRiskMatch;
    
    /**
     * 用户风险等级
     */
    private String userRiskLevel;
    
    /**
     * 增加是否显示风控
     */
    private boolean isOpenRisk;
    
    /**
     * 发布时间
     */
    private String publicDate;
    
    /**
     * 标产品是否增加投资限制
     */
    private boolean isinvestLimit;
    
    /**
     * 是否显示风险邀请函
     */
    private boolean isShowFXTS;
    
    /**
     * 筹款用时、还清时间、垫付时间
     */
    private String fundDate;
    
    /**
     * 数据库时间
     */
    private long timemp;
    
    public String getBidTitle()
    {
        return bidTitle;
    }
    
    public void setBidTitle(String bidTitle)
    {
        this.bidTitle = bidTitle;
    }
    
    public int getPeopleNum()
    {
        return peopleNum;
    }
    
    public void setPeopleNum(int peopleNum)
    {
        this.peopleNum = peopleNum;
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
    
    public int getTerm()
    {
        return term;
    }
    
    public void setTerm(int term)
    {
        this.term = term;
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
    
    public String getAlrAmount()
    {
        return alrAmount;
    }
    
    public void setAlrAmount(String alrAmount)
    {
        this.alrAmount = alrAmount;
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
    
    public String getPublishTime()
    {
        return publishTime;
    }
    
    public void setPublishTime(String publishTime)
    {
        this.publishTime = publishTime;
    }
    
    public String getEndTime()
    {
        return endTime;
    }
    
    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }
    
    public String getFinancialType()
    {
        return financialType;
    }
    
    public void setFinancialType(String financialType)
    {
        this.financialType = financialType;
    }
    
    public int getType()
    {
        return type;
    }
    
    public void setType(int type)
    {
        this.type = type;
    }
    
    public String getIsDay()
    {
        return isDay;
    }
    
    public void setIsDay(String isDay)
    {
        this.isDay = isDay;
    }
    
    public String getGuarantee()
    {
        return guarantee;
    }
    
    public void setGuarantee(String guarantee)
    {
        this.guarantee = guarantee;
    }
    
    public String getGuaSch()
    {
        return guaSch;
    }
    
    public void setGuaSch(String guaSch)
    {
        this.guaSch = guaSch;
    }
    
    public String getIsDanBao()
    {
        return isDanBao;
    }
    
    public void setIsDanBao(String isDanBao)
    {
        this.isDanBao = isDanBao;
    }
    
    public int getUserId()
    {
        return userId;
    }
    
    public void setUserId(int userId)
    {
        this.userId = userId;
    }
    
    public String getMinBidingAmount()
    {
        return minBidingAmount;
    }
    
    public void setMinBidingAmount(String minBidingAmount)
    {
        this.minBidingAmount = minBidingAmount;
    }
    
    public String getHkDate()
    {
        return hkDate;
    }
    
    public void setHkDate(String hkDate)
    {
        this.hkDate = hkDate;
    }
    
    public String getDhjeAmount()
    {
        return dhjeAmount;
    }
    
    public void setDhjeAmount(String dhjeAmount)
    {
        this.dhjeAmount = dhjeAmount;
    }
    
    public int getDays()
    {
        return days;
    }
    
    public void setDays(int days)
    {
        this.days = days;
    }
    
    public String getMaxBidingAmount()
    {
        return maxBidingAmount;
    }
    
    public void setMaxBidingAmount(String maxBidingAmount)
    {
        this.maxBidingAmount = maxBidingAmount;
    }
    
    public String getFlag()
    {
        return flag;
    }
    
    public void setFlag(String flag)
    {
        this.flag = flag;
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
    
    public String getJlRate()
    {
        return jlRate;
    }
    
    public void setJlRate(String jlRate)
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
    
    public String getPublicDate()
    {
        return publicDate;
    }
    
    public void setPublicDate(String publicDate)
    {
        this.publicDate = publicDate;
    }
    
    public String getRiskLevel()
    {
        return riskLevel;
    }
    
    public void setRiskLevel(String riskLevel)
    {
        this.riskLevel = riskLevel;
    }
    
    public boolean isRiskMatch()
    {
        return isRiskMatch;
    }
    
    public void setRiskMatch(boolean isRiskMatch)
    {
        this.isRiskMatch = isRiskMatch;
    }
    
    public String getUserRiskLevel()
    {
        return userRiskLevel;
    }
    
    public void setUserRiskLevel(String userRiskLevel)
    {
        this.userRiskLevel = userRiskLevel;
    }

	public boolean isOpenRisk() {
		return isOpenRisk;
	}

	public void setOpenRisk(boolean isOpenRisk) {
		this.isOpenRisk = isOpenRisk;
	}

	public void setShowFXTS(boolean isShowFXTS) {
		this.isShowFXTS = isShowFXTS;
	}

	public boolean isIsinvestLimit()
    {
        return isinvestLimit;
    }
    
    public void setIsinvestLimit(boolean isinvestLimit)
    {
        this.isinvestLimit = isinvestLimit;
    }

	public boolean isShowFXTS() {
		return isShowFXTS;
	}

	public void setIsShowFXTS(boolean isShowFXTS) {
		this.isShowFXTS = isShowFXTS;
	}

    public String getFundDate()
    {
        return fundDate;
    }

    public void setFundDate(String fundDate)
    {
        this.fundDate = fundDate;
    }

    @Override
    public String toString()
    {
        return "BidInfo [bidTitle=" + bidTitle + ", peopleNum=" + peopleNum + ", rate=" + rate + ", cycle=" + cycle
            + ", term=" + term + ", type=" + type + ", amount=" + amount + ", remainAmount=" + remainAmount
            + ", alrAmount=" + alrAmount + ", paymentType=" + paymentType + ", status=" + status + ", publishTime="
            + publishTime + ", endTime=" + endTime + ", financialType=" + financialType + ", isDay=" + isDay
            + ", guarantee=" + guarantee + ", guaSch=" + guaSch + ", isDanBao=" + isDanBao + ", userId=" + userId
            + ", minBidingAmount=" + minBidingAmount + ", hkDate=" + hkDate + ", dhjeAmount=" + dhjeAmount + ", days="
            + days + ", maxBidingAmount=" + maxBidingAmount + ", flag=" + flag + ", isXsb=" + isXsb + ", isJlb="
            + isJlb + ", jlRate=" + jlRate + ", isTjb=" + isTjb + ", riskLevel=" + riskLevel + ", isRiskMatch="
            + isRiskMatch + ", userRiskLevel=" + userRiskLevel + ", isOpenRisk=" + isOpenRisk + ", publicDate="
            + publicDate + ", isinvestLimit=" + isinvestLimit + ", isShowFXTS=" + isShowFXTS + ", fundDate=" + fundDate
            + "]";
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
