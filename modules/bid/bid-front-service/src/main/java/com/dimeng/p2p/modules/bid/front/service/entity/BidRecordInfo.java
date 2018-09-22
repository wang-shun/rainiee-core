/**
 * 
 */
package com.dimeng.p2p.modules.bid.front.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 投资记录
 */
public class BidRecordInfo implements Serializable
{
    private static final long serialVersionUID = 5677894943547333886L;
    
    /**
     * 账号名(显示3位，其他用*代替)
     */
    private String accountName;
    
    /**
     * 投资金额
     */
    private BigDecimal bidAmount;
    
    /**
     * 投资时间
     */
    private String bidTime;
    
    public String getAccountName()
    {
        return accountName;
    }
    
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    
    public BigDecimal getBidAmount()
    {
        return bidAmount;
    }
    
    public void setBidAmount(BigDecimal bidAmount)
    {
        this.bidAmount = bidAmount;
    }
    
    public String getBidTime()
    {
        return bidTime;
    }
    
    public void setBidTime(String bidTime)
    {
        this.bidTime = bidTime;
    }
    
}
