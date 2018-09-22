/*
 * 文 件 名:  Gyinfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年11月25日
 */
package com.dimeng.p2p.app.servlets.bid.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 公益标基础信息
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月25日]
 */
public class Gyinfo implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -7718610435886133438L;

    /**
     * 公益标ID
     */
    private int bidId;
    
    /**
     * 公益标名称
     */
    private String loanName;
    
    /**
     * 公益标金额
     */
    private String loanAmount;
    
    /**
     * 开始投资时间
     */
    private String loanStartTime;
    
    /**
     * 结束投资时间
     */
    private String loanEndTime;
    
    /**
     * 最低起投金额 单位元
     */
    private String minAmount;
    
    /**
     * 公益机构 举办方
     */
    private String organisers;
    
    /**
     * 剩余可投金额 单位元
     */
    private String remaindAmount;
    
    /**
     * 进度
     */
    private double progress;
    
    /**
     * 简介
     */
    private String introduction;
    
    /**
     * 总共捐赠人数
     */
    private int totalNum;
    
    /**
     * 总共捐赠金额
     */
    private String donationsAmount;
    
    /**
     * 活动是否结束标识
     */
    private boolean isTimeEnd;
    
    /**
     * 倡导书
     */
    private String advocacyContent;
    
    /**
     * 进度
     */
    private List<BidProgress> bidProgres;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 中文状态
     */
    private String statusCn;
    
    public String getLoanName()
    {
        return loanName;
    }
    
    public void setLoanName(String loanName)
    {
        this.loanName = loanName;
    }
    
    public String getLoanAmount()
    {
        return loanAmount;
    }
    
    public void setLoanAmount(String loanAmount)
    {
        this.loanAmount = loanAmount;
    }
    
    public String getLoanStartTime()
    {
        return loanStartTime;
    }
    
    public void setLoanStartTime(String loanStartTime)
    {
        this.loanStartTime = loanStartTime;
    }
    
    public String getLoanEndTime()
    {
        return loanEndTime;
    }
    
    public void setLoanEndTime(String loanEndTime)
    {
        this.loanEndTime = loanEndTime;
    }
    
    public String getMinAmount()
    {
        return minAmount;
    }
    
    public void setMinAmount(String minAmount)
    {
        this.minAmount = minAmount;
    }
    
    public String getOrganisers()
    {
        return organisers;
    }
    
    public void setOrganisers(String organisers)
    {
        this.organisers = organisers;
    }
    
    public String getRemaindAmount()
    {
        return remaindAmount;
    }
    
    public void setRemaindAmount(String remaindAmount)
    {
        this.remaindAmount = remaindAmount;
    }
    
    public double getProgress()
    {
        return progress;
    }
    
    public void setProgress(double progress)
    {
        this.progress = progress;
    }
    
    public String getIntroduction()
    {
        return introduction;
    }
    
    public void setIntroduction(String introduction)
    {
        this.introduction = introduction;
    }
    
    public int getTotalNum()
    {
        return totalNum;
    }
    
    public void setTotalNum(int totalNum)
    {
        this.totalNum = totalNum;
    }
    
    public String getDonationsAmount()
    {
        return donationsAmount;
    }
    
    public void setDonationsAmount(String donationsAmount)
    {
        this.donationsAmount = donationsAmount;
    }
    
    public boolean isTimeEnd()
    {
        return isTimeEnd;
    }
    
    public void setTimeEnd(boolean isTimeEnd)
    {
        this.isTimeEnd = isTimeEnd;
    }
    
    public String getAdvocacyContent()
    {
        return advocacyContent;
    }
    
    public void setAdvocacyContent(String advocacyContent)
    {
        this.advocacyContent = advocacyContent;
    }
    
    public List<BidProgress> getBidProgres()
    {
        return bidProgres;
    }
    
    public void setBidProgres(List<BidProgress> bidProgres)
    {
        this.bidProgres = bidProgres;
    }
    
    public int getBidId()
    {
        return bidId;
    }
    
    public void setBidId(int bidId)
    {
        this.bidId = bidId;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public String getStatusCn()
    {
        return statusCn;
    }
    
    public void setStatusCn(String statusCn)
    {
        this.statusCn = statusCn;
    }

    @Override
    public String toString()
    {
        return "Gyinfo [bidId=" + bidId + ", loanName=" + loanName + ", loanAmount=" + loanAmount + ", loanStartTime="
            + loanStartTime + ", loanEndTime=" + loanEndTime + ", minAmount=" + minAmount + ", organisers="
            + organisers + ", remaindAmount=" + remaindAmount + ", progress=" + progress + ", introduction="
            + introduction + ", totalNum=" + totalNum + ", donationsAmount=" + donationsAmount + ", isTimeEnd="
            + isTimeEnd + ", advocacyContent=" + advocacyContent + ", bidProgres=" + bidProgres + ", status=" + status
            + ", statusCn=" + statusCn + "]";
    }
    
}
