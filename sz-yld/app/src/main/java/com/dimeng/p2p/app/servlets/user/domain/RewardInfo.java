/*
 * 文 件 名:  HbInfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年12月3日
 */
package com.dimeng.p2p.app.servlets.user.domain;

import java.io.Serializable;

/**
 * 奖励信息
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年12月3日]
 */
public class RewardInfo implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -7455785082104297419L;

    /**
     * 红包或者加息券的ID
     */
    private int id;
    
    /**
     * 红包金额
     */
    private String amount;
    
    /**
     * 加息券折扣
     */
    private String rate;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 活动类型
     */
    private String type;
    
    /**
     * 投资使用规则（满多少就能使用）
     */
    private String investUseRule;
    
    /**
     * 投资额度/倍数/最低充值额度(投资倍数赠送加息券)
     */
    private String quota;
    
    /**
     * 过期时间
     */
    private String timeOut;
    
    public String getAmount()
    {
        return amount;
    }
    
    public void setAmount(String amount)
    {
        this.amount = amount;
    }
    
    public String getRate()
    {
        return rate;
    }
    
    public void setRate(String rate)
    {
        this.rate = rate;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public String getInvestUseRule()
    {
        return investUseRule;
    }
    
    public void setInvestUseRule(String investUseRule)
    {
        this.investUseRule = investUseRule;
    }
    
    public String getQuota()
    {
        return quota;
    }
    
    public void setQuota(String quota)
    {
        this.quota = quota;
    }
    
    public String getTimeOut()
    {
        return timeOut;
    }
    
    public void setTimeOut(String timeOut)
    {
        this.timeOut = timeOut;
    }
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "RewardInfo [id=" + id + ", amount=" + amount + ", rate=" + rate + ", status=" + status + ", type="
            + type + ", investUseRule=" + investUseRule + ", quota=" + quota + ", timeOut=" + timeOut + "]";
    }
    
}
