package com.dimeng.p2p.modules.nciic.entity;

/*
 * 文 件 名:  CheckBankCard.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  银行卡号二元素验证接口返回实体类
 * 修 改 人:  YINKE
 * 修改时间:  2016年3月8日
 */
public class CheckBankCard
{
    /**
     * 校验是否成功
     */
    private boolean success;
    
    /**
     * 提示信息
     */
    private String message;

    public boolean getSuccess()
    {
        return success;
    }
    
    public void setSuccess(boolean success)
    {
        this.success = success;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }

}
