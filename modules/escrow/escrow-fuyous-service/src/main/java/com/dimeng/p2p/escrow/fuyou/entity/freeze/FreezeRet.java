package com.dimeng.p2p.escrow.fuyou.entity.freeze;

import java.io.Serializable;

/**
 * 
 * 资金冻结管理回调 FreezeRet.java
 * <功能详细描述>
 * 
 * @author  lingyuanjie
 * @version  [版本号, 2016年6月2日]
 */
public class FreezeRet implements Serializable
{
    
    /**
    * 注释内容
    */
    private static final long serialVersionUID = 1L;
    
    //响应码
    private String respCode;
    
    //商户代码   
    private String mchntCd;
    
    //请求流水号
    private String mchntTxnSsn;
    
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
    
    public String getSignature()
    {
        return signature;
    }
    
    public void setSignature(String signature)
    {
        this.signature = signature;
    }
    
}
