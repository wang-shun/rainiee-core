package com.dimeng.p2p.modules.nciic.util;

public enum ResultCode
{
    
    SUCCESS("88", "成功"),
    
    FAIL("07", "认证失败"),
    
    PTMarkError("01", "平台标识错误"),
    
    RetUrlError("02", "返回地址错误"),
    
    SingError("03", "签名验证错误"),
    
    TimeRendom("04", "随机时间戳错误"),
    
    IDInfoError("05", "身份信息列表错误"),
    
    CreditLow("06", "余额不足");
    
    private String code;
    
    private String text;
    
    private ResultCode(String code, String text)
    {
        this.code = code;
        this.text = text;
    }
    
    public String getCode()
    {
        return code;
    }
    
    public String getText()
    {
        return text;
    }
    
}
