/*
 * 文 件 名:  SQFee.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年1月18日
 */
package com.dimeng.p2p.app.servlets.user.domain;

import java.io.Serializable;

/**
 * 双乾的费率
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月18日]
 */
public class SQFee implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -1563035750896091830L;

    /**
     * 双乾提现费率
     */
    private Double withdrawRate;
    
    /**
     * 双乾快捷充值费率
     */
    private Double kuaiJieRate;
    
    /**
     * 快捷支付方式
     */
    private String kjFeeType;
    
    public Double getWithdrawRate()
    {
        return withdrawRate;
    }
    
    public void setWithdrawRate(Double withdrawRate)
    {
        this.withdrawRate = withdrawRate;
    }
    
    public Double getKuaiJieRate()
    {
        return kuaiJieRate;
    }
    
    public void setKuaiJieRate(Double kuaiJieRate)
    {
        this.kuaiJieRate = kuaiJieRate;
    }
    
    public String getKjFeeType()
    {
        return kjFeeType;
    }
    
    public void setKjFeeType(String kjFeeType)
    {
        this.kjFeeType = kjFeeType;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "SQFee [withdrawRate=" + withdrawRate + ", kuaiJieRate=" + kuaiJieRate + ", kjFeeType=" + kjFeeType
            + "]";
    }
    
}
