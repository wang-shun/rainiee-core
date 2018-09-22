package com.dimeng.p2p.escrow.fuyou.entity.unfreeze;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 
 * 解冻状态码
 * <功能详细描述>
 * 
 * @author  sunqiuyan
 * @version  [版本号, 2016年7月12日]
 */
public class UnFreezeRet implements Serializable
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    //响应码 0000：成功
    private String respCode;
    
    //商户代码 
    private String mchntCd;
    
    //解冻目标登录账户
    
    private String custNo;
    
    //请求流水号（解冻流水号）
    private String mchntTxnSsn;
    
    //冻结流水号
    private String freezeSerialNo;
    
    //请求解冻金额
    private String amt;
    
    //成功解冻金额 成功(响应码0000)时有效 
    private String sucAmt;
    
    //解冻时间
    private Timestamp thawDate;
    
    //签名数据
    private String signature;
    
    public String getRespCode()
    {
        return respCode;
    }
    
    public void setRespCode(String respCode)
    {
        this.respCode = respCode;
    }
    
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
    
    public String getAmt()
    {
        return amt;
    }
    
    public void setAmt(String amt)
    {
        this.amt = amt;
    }
    
    public String getSucAmt()
    {
        return sucAmt;
    }
    
    public void setSucAmt(String sucAmt)
    {
        this.sucAmt = sucAmt;
    }
    
    public String getSignature()
    {
        return signature;
    }
    
    public void setSignature(String signature)
    {
        this.signature = signature;
    }
    
    public String getCustNo()
    {
        return custNo;
    }
    
    public void setCustNo(String custNo)
    {
        this.custNo = custNo;
    }
    
    public String getFreezeSerialNo()
    {
        return freezeSerialNo;
    }
    
    public void setFreezeSerialNo(String freezeSerialNo)
    {
        this.freezeSerialNo = freezeSerialNo;
    }
    
    public Timestamp getThawDate()
    {
        return thawDate;
    }
    
    public void setThawDate(Timestamp thawDate)
    {
        this.thawDate = thawDate;
    }
    
}
