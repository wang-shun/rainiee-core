/*
 * 文 件 名:  GyRecordList.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年11月25日
 */
package com.dimeng.p2p.app.servlets.bid.domain;

import java.io.Serializable;

/**
 * 公益标投资记录基础信息
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月25日]
 */
public class GyRecordList implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 6376408132081658671L;

    /**
     * 投资人
     */
    private String userName;
    
    /**
     * 投资金额
     */
    private String loanAmount;
    
    /**
     * 投资时间
     */
    private String locanTime;
    
    public String getUserName()
    {
        return userName;
    }
    
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    public String getLoanAmount()
    {
        return loanAmount;
    }
    
    public void setLoanAmount(String loanAmount)
    {
        this.loanAmount = loanAmount;
    }
    
    public String getLocanTime()
    {
        return locanTime;
    }
    
    public void setLocanTime(String locanTime)
    {
        this.locanTime = locanTime;
    }

    @Override
    public String toString()
    {
        return "GyRecordList [userName=" + userName + ", loanAmount=" + loanAmount + ", locanTime=" + locanTime + "]";
    }
    
}
