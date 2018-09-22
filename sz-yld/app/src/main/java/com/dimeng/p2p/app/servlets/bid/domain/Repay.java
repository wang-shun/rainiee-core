package com.dimeng.p2p.app.servlets.bid.domain;

import java.io.Serializable;

public class Repay implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 217069899441952434L;
    
    /**期数*/
    private int term;
    
    /**还款日期*/
    private String repayDate;
    
    /**还款类型*/
    private String repayType;
    
    /**还款金额*/
    private String amount;
    
    /**还款日期*/
    private String realDate;
    
    /**还款状态*/
    private String status;
    
    public int getTerm()
    {
        return term;
    }
    
    public void setTerm(int term)
    {
        this.term = term;
    }
    
    public String getRepayDate()
    {
        return repayDate;
    }
    
    public void setRepayDate(String repayDate)
    {
        this.repayDate = repayDate;
    }
    
    public String getRepayType()
    {
        return repayType;
    }
    
    public void setRepayType(String repayType)
    {
        this.repayType = repayType;
    }
    
    public String getAmount()
    {
        return amount;
    }
    
    public void setAmount(String amount)
    {
        this.amount = amount;
    }
    
    public String getRealDate()
    {
        return realDate;
    }
    
    public void setRealDate(String realDate)
    {
        this.realDate = realDate;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "Repay [term=" + term + ", repayDate=" + repayDate + ", repayType=" + repayType + ", amount=" + amount
            + ", realDate=" + realDate + ", status=" + status + "]";
    }
    
}
