/*
 * 文 件 名:  CreditorInfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年11月27日
 */
package com.dimeng.p2p.app.servlets.user.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 债权详情
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月27日]
 */
public class CreditorInfo  implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -4967675760096941700L;

    /**
     * 标ID
     */
    private int bidId;
    
    /**
     * 债权编码
     */
    private String creditorId;
    
    /**
     * 剩余期数
     */
    private int subTerm;
    
    /**
     * 总期数
     */
    private int totalTerm;
    
    /**
     * 年化利率
     */
    private String rate;
    
    /**
     * 债权价格
     */
    private BigDecimal creditorVal = new BigDecimal(0);
    
    /**
     * 转让价格
     */
    private BigDecimal salePrice = new BigDecimal(0);
    
    /**
     * 转出时间
     */
    private String saleTime;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 转让手续费
     */
    private BigDecimal transferPrice = new BigDecimal(0);
    
    /**
     * 转出盈亏
     */
    private BigDecimal turnOutBalance = new BigDecimal(0);
    
    /**
     * 转入盈亏
     */
    private BigDecimal turnInBalance = new BigDecimal(0);
    
    /**
     * 买入时间
     */
    private String BuyingDate;
    
    public int getBidId()
    {
        return bidId;
    }
    
    public void setBidId(int bidId)
    {
        this.bidId = bidId;
    }
    
    public String getCreditorId()
    {
        return creditorId;
    }
    
    public void setCreditorId(String creditorId)
    {
        this.creditorId = creditorId;
    }
    
    public int getSubTerm()
    {
        return subTerm;
    }
    
    public void setSubTerm(int subTerm)
    {
        this.subTerm = subTerm;
    }
    
    public int getTotalTerm()
    {
        return totalTerm;
    }
    
    public void setTotalTerm(int totalTerm)
    {
        this.totalTerm = totalTerm;
    }
    
    public String getRate()
    {
        return rate;
    }
    
    public void setRate(String rate)
    {
        this.rate = rate;
    }
    
    public BigDecimal getCreditorVal()
    {
        return creditorVal;
    }
    
    public void setCreditorVal(BigDecimal creditorVal)
    {
        this.creditorVal = creditorVal;
    }
    
    public BigDecimal getSalePrice()
    {
        return salePrice;
    }
    
    public void setSalePrice(BigDecimal salePrice)
    {
        this.salePrice = salePrice;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public BigDecimal getTransferPrice()
    {
        return transferPrice;
    }
    
    public void setTransferPrice(BigDecimal transferPrice)
    {
        this.transferPrice = transferPrice;
    }
    
    public BigDecimal getTurnOutBalance()
    {
        return turnOutBalance;
    }
    
    public void setTurnOutBalance(BigDecimal turnOutBalance)
    {
        this.turnOutBalance = turnOutBalance;
    }
    
    public BigDecimal getTurnInBalance()
    {
        return turnInBalance;
    }
    
    public void setTurnInBalance(BigDecimal turnInBalance)
    {
        this.turnInBalance = turnInBalance;
    }
    
    public String getBuyingDate()
    {
        return BuyingDate;
    }
    
    public void setBuyingDate(String buyingDate)
    {
        BuyingDate = buyingDate;
    }
    
    public String getSaleTime()
    {
        return saleTime;
    }
    
    public void setSaleTime(String saleTime)
    {
        this.saleTime = saleTime;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "CreditorInfo [bidId=" + bidId + ", creditorId=" + creditorId + ", subTerm=" + subTerm + ", totalTerm="
            + totalTerm + ", rate=" + rate + ", creditorVal=" + creditorVal + ", salePrice=" + salePrice + ", saleTime="
            + saleTime + ", status=" + status + ", transferPrice=" + transferPrice + ", turnOutBalance="
            + turnOutBalance + ", turnInBalance=" + turnInBalance + ", BuyingDate=" + BuyingDate + "]";
    }
    
    
}
