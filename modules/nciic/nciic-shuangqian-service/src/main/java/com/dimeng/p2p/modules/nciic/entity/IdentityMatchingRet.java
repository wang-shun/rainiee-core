package com.dimeng.p2p.modules.nciic.entity;

import java.math.BigDecimal;

public class IdentityMatchingRet
{
    
    /**
     * 姓名
     */
    private String realName;
    
    /**
     * 身份证ID号
     */
    private String identificationNo;
    
    /**
     * 匹配结果
     */
    private boolean matchingResult;
    
    /**
     * 返回信息
     */
    private String message;
    
    /**
     * 金额
     */
    private BigDecimal Amount;
    
    /**
     * 签名码
     */
    private String signStr;
    
    /**
     * 返回结果码
     */
    private String resultCode;
    
    public String getRealName()
    {
        return realName;
    }
    
    public void setRealName(String realName)
    {
        this.realName = realName;
    }
    
    public String getIdentificationNo()
    {
        return identificationNo;
    }
    
    public void setIdentificationNo(String identificationNo)
    {
        this.identificationNo = identificationNo;
    }
    
    public boolean getMatchingResult()
    {
        return matchingResult;
    }
    
    public void setMatchingResult(boolean matchingResult)
    {
        this.matchingResult = matchingResult;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    public BigDecimal getAmount()
    {
        return Amount;
    }
    
    public void setAmount(BigDecimal amount)
    {
        Amount = amount;
    }
    
    public String getSignStr()
    {
        return signStr;
    }
    
    public void setSignStr(String signStr)
    {
        this.signStr = signStr;
    }
    
    public String getResultCode()
    {
        return resultCode;
    }
    
    public void setResultCode(String resultCode)
    {
        this.resultCode = resultCode;
    }
    
}
