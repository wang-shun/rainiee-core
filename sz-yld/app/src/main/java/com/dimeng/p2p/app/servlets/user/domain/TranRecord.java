package com.dimeng.p2p.app.servlets.user.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * 交易记录
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月27日]
 */
public class TranRecord implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 5951941038202361956L;
    
    /**
     * 交易时间
     */
    private String tranTime;
    
    /**
     * 交易类型
     */
    private String tranType;
    
    /**
     * 结余金额
     */
    private BigDecimal revAmount = new BigDecimal(0);
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 收入
     */
    private BigDecimal inAmount = new BigDecimal(0);
    
    /**
     * 支出
     */
    private BigDecimal expAmount = new BigDecimal(0);
    
    public String getTranTime()
    {
        return tranTime;
    }
    
    public void setTranTime(String tranTime)
    {
        this.tranTime = tranTime;
    }
    
    public String getTranType()
    {
        return tranType;
    }
    
    public void setTranType(String tranType)
    {
        this.tranType = tranType;
    }
    
    public BigDecimal getRevAmount()
    {
        return revAmount;
    }
    
    public void setRevAmount(BigDecimal revAmount)
    {
        this.revAmount = revAmount;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public BigDecimal getInAmount()
    {
        return inAmount;
    }
    
    public void setInAmount(BigDecimal inAmount)
    {
        this.inAmount = inAmount;
    }
    
    public BigDecimal getExpAmount()
    {
        return expAmount;
    }
    
    public void setExpAmount(BigDecimal expAmount)
    {
        this.expAmount = expAmount;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "TranRecord [tranTime=" + tranTime + ", tranType=" + tranType + ", revAmount=" + revAmount + ", remark="
            + remark + ", inAmount=" + inAmount + ", expAmount=" + expAmount + "]";
    }
    
}
