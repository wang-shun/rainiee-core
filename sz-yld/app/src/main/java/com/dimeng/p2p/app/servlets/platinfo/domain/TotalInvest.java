/*
 * 文 件 名:  TotalInvest.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年3月23日
 */
package com.dimeng.p2p.app.servlets.platinfo.domain;

import java.io.Serializable;

/**
 * 最近一年累计投资
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年3月23日]
 */
public class TotalInvest implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -2967474709125397905L;

    /**
     * 累计投资金额
     */
    private String totalInvestAmount;
    
    /**
     * 统计时间
     */
    private String times;
    
    public String getTotalInvestAmount()
    {
        return totalInvestAmount;
    }
    
    public void setTotalInvestAmount(String totalInvestAmount)
    {
        this.totalInvestAmount = totalInvestAmount;
    }
    
    public String getTimes()
    {
        return times;
    }
    
    public void setTimes(String times)
    {
        this.times = times;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "TotalInvest [totalInvestAmount=" + totalInvestAmount + ", times=" + times + "]";
    }
    
}
