package com.dimeng.p2p.escrow.fuyou.entity.freeze;

import java.io.Serializable;

/**
 * 
 * 资金冻结管理 Freeze.java
 * <功能详细描述>
 * 
 * @author  lingyuanjie
 * @version  [版本号, 2016年6月2日]
 */
public class Freeze implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    //商户代码
    private String mchntCd;
    
    //流水号
    private String mchntTxnSsn;
    
    //冻结目标登录账户
    private String custNo;
    
    //到期自动解冻
    private String expired;
    
    //冻结金额
    private String amt;
    
    //备注
    private String rem;
    
    public String getMchntCd()
    {
        return mchntCd;
    }
    
    public void setMchntCd(String mchntCd)
    {
        this.mchntCd = mchntCd;
    }
    
    public String getMchntTxnSsn()
    {
        return mchntTxnSsn;
    }
    
    public void setMchntTxnSsn(String mchntTxnSsn)
    {
        this.mchntTxnSsn = mchntTxnSsn;
    }
    
    public String getCustNo()
    {
        return custNo;
    }
    
    public void setCustNo(String custNo)
    {
        this.custNo = custNo;
    }
    
    public String getAmt()
    {
        return amt;
    }
    
    public void setAmt(String amt)
    {
        this.amt = amt;
    }
    
    public String getRem()
    {
        return rem;
    }
    
    public void setRem(String rem)
    {
        this.rem = rem;
    }

    public String getExpired()
    {
        return expired;
    }

    public void setExpired(String expired)
    {
        this.expired = expired;
    }
    
}
