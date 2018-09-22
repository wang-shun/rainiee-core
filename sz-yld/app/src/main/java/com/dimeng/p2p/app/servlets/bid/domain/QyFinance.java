package com.dimeng.p2p.app.servlets.bid.domain;

import java.io.Serializable;

/**
 * 企业财务
 * @author tanhui
 *
 */
public class QyFinance implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -1173006670016322376L;

    /**年份*/
    private String years;
    
    /**主营收入*/
    private String revenue;
    
    /**净利润*/
    private String profit;
    
    /**总资产*/
    private String totalAssets;
    
    /**净资产*/
    private String netAssets;
    
    /**备注*/
    private String remark;
    
    public String getYears()
    {
        return years;
    }
    
    public void setYears(String years)
    {
        this.years = years;
    }
    
    public String getRevenue()
    {
        return revenue;
    }
    
    public void setRevenue(String revenue)
    {
        this.revenue = revenue;
    }
    
    public String getProfit()
    {
        return profit;
    }
    
    public void setProfit(String profit)
    {
        this.profit = profit;
    }
    public String getTotalAssets()
    {
        return totalAssets;
    }
    
    public void setTotalAssets(String totalAssets)
    {
        this.totalAssets = totalAssets;
    }
    
    public String getNetAssets()
    {
        return netAssets;
    }
    
    public void setNetAssets(String netAssets)
    {
        this.netAssets = netAssets;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    @Override
    public String toString()
    {
        return "QyFinance [years=" + years + ", revenue=" + revenue + ", profit=" + profit + ", totalAssets="
            + totalAssets + ", netAssets=" + netAssets + ", remark=" + remark + "]";
    }
    
}
