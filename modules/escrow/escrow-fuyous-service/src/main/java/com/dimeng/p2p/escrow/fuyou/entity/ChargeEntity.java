package com.dimeng.p2p.escrow.fuyou.entity;

import java.io.Serializable;

/**
 * 
 * 充值返回信息实体类
 * 
 * @author heshiping
 * @version [版本号, 2015年5月25日]
 */
public class ChargeEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    // 响应码
    private String respCode;
    
    // 响应消息
    private String respDesc;
    
    // 商户代码
    private String mchntCd;
    
    // 请求流水号
    private String mchntTxnSsn;
    
    // 交易用户
    private String loginId;
    
    // 交易金额
    private String amt;
    
    // 手续费金额
    private String mchntAmt;
    
    // 备注
    private String rem;
    
    // 签名数据
    private String signature;
    
    public String getResp_code()
    {
        return respCode;
    }
    
    public String getMchnt_amt()
    {
        return mchntAmt;
    }
    
    public void setMchnt_amt(String mchnt_amt)
    {
        this.mchntAmt = mchnt_amt;
    }
    
    public void setResp_code(String resp_code)
    {
        this.respCode = resp_code;
    }
    
    public String getResp_desc()
    {
        return respDesc;
    }
    
    public void setResp_desc(String resp_desc)
    {
        this.respDesc = resp_desc;
    }
    
    public String getMchnt_cd()
    {
        return mchntCd;
    }
    
    public void setMchnt_cd(String mchnt_cd)
    {
        this.mchntCd = mchnt_cd;
    }
    
    public String getMchnt_txn_ssn()
    {
        return mchntTxnSsn;
    }
    
    public void setMchnt_txn_ssn(String mchnt_txn_ssn)
    {
        this.mchntTxnSsn = mchnt_txn_ssn;
    }
    
    public String getLogin_id()
    {
        return loginId;
    }
    
    public void setLogin_id(String login_id)
    {
        this.loginId = login_id;
    }
    
    public String getAmt()
    {
        return amt;
    }
    
    public void setAmt(String amt)
    {
        this.amt = amt;
    }
    
    public String getSignature()
    {
        return signature;
    }
    
    public void setSignature(String signature)
    {
        this.signature = signature;
    }
    
    public String getRem()
    {
        return rem;
    }
    
    public void setRem(String rem)
    {
        this.rem = rem;
    }
    
}
