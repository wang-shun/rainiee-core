/*
 * 文 件 名:  HkzEntity.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2015年11月27日
 */
package com.dimeng.p2p.app.servlets.user.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 回款中页面信息
 * 
 * @author  zhoulantao
 * @version  [版本号, 2015年11月27日]
 */
public class MyZqEntity implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -7369641301176699395L;

    /**
     * 标ID
     */
    private int bidId;
    
    /**
     * 债权ID
     */
    private int zqId;
    
    /**
     * 标名称
     */
    private String bidName;
    
    /**
     * 债权编码
     */
    private String creditorId;
    
    /**
     * 购买价格
     */
    private BigDecimal gmjg = new BigDecimal(0);
    
    /**
     * 年化利率
     */
    private BigDecimal nhl = new BigDecimal(0);
    
    /**
     * 加息利率
     */
    private BigDecimal jxl = new BigDecimal(0);
    
    /**
     * 待收本息
     */
    private BigDecimal dsbx = new BigDecimal(0);
    
    /**
     * 剩余期数
     */
    private int syqs;
    
    /**
     * 还款期数
     */
    private int hkqs;
    
    /**
     * 下个还款日
     */
    private String xghkr;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 借款周期
     */
    private int jkzq;
    
    /**
     * 进度
     */
    private BigDecimal process = new BigDecimal(0);
    
    /**
     * 剩余时间
     */
    private String surTime;
    
    /**
     * 借款周期是否为天
     */
    private boolean isDay = false;
    
    /**
     * 已赚金额
     */
    private BigDecimal yzje = new BigDecimal(0);
    
    /**
     * 结算时间
     */
    private String jqsj;
    
    /**
     * 转让费率
     */
    private BigDecimal transferRate = new BigDecimal(0);
    
    /**
     * 原始债权价值
     */
    private BigDecimal sourceZqPrice = new BigDecimal(0);
    
    /**
     * 是否正在转让
     */
    private String isTransfered;
    
    /**
     * 是否能转让
     */
    private boolean isCanTrans = false;
    
    /**
     * 债权价值比例设置最小
     */
    private String zqzrRateMin;
    
    /**
     * 债权价值比例设置最大
     */
    private String zqzrRateMax;
    


    /**
     * @return 返回 zqzrRateMin
     */
    public String getZqzrRateMin()
    {
        return zqzrRateMin;
    }

    /**
     * @param 对zqzrRateMin进行赋值
     */
    public void setZqzrRateMin(String zqzrRateMin)
    {
        this.zqzrRateMin = zqzrRateMin;
    }

    /**
     * @return 返回 zqzrRateMax
     */
    public String getZqzrRateMax()
    {
        return zqzrRateMax;
    }

    /**
     * @param 对zqzrRateMax进行赋值
     */
    public void setZqzrRateMax(String zqzrRateMax)
    {
        this.zqzrRateMax = zqzrRateMax;
    }


    public int getBidId()
    {
        return bidId;
    }
    
    public void setBidId(int bidId)
    {
        this.bidId = bidId;
    }
    
    public String getCreditorId()
    {
        return creditorId;
    }
    
    public void setCreditorId(String creditorId)
    {
        this.creditorId = creditorId;
    }
    
    public BigDecimal getGmjg()
    {
        return gmjg;
    }
    
    public void setGmjg(BigDecimal gmjg)
    {
        this.gmjg = gmjg;
    }
    
    public BigDecimal getNhl()
    {
        return nhl;
    }
    
    public void setNhl(BigDecimal nhl)
    {
        this.nhl = nhl;
    }
    
    public BigDecimal getJxl()
    {
        return jxl;
    }
    
    public void setJxl(BigDecimal jxl)
    {
        this.jxl = jxl;
    }
    
    public BigDecimal getDsbx()
    {
        return dsbx;
    }
    
    public void setDsbx(BigDecimal dsbx)
    {
        this.dsbx = dsbx;
    }
    
    public int getSyqs()
    {
        return syqs;
    }
    
    public void setSyqs(int syqs)
    {
        this.syqs = syqs;
    }
    
    public int getHkqs()
    {
        return hkqs;
    }
    
    public void setHkqs(int hkqs)
    {
        this.hkqs = hkqs;
    }
    
    public String getXghkr()
    {
        return xghkr;
    }
    
    public void setXghkr(String xghkr)
    {
        this.xghkr = xghkr;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public int getJkzq()
    {
        return jkzq;
    }
    
    public void setJkzq(int jkzq)
    {
        this.jkzq = jkzq;
    }
    
    public BigDecimal getProcess()
    {
        return process;
    }
    
    public void setProcess(BigDecimal process)
    {
        this.process = process;
    }
    
    public String getSurTime()
    {
        return surTime;
    }
    
    public void setSurTime(String surTime)
    {
        this.surTime = surTime;
    }
    
    public boolean isDay()
    {
        return isDay;
    }
    
    public void setDay(boolean isDay)
    {
        this.isDay = isDay;
    }
    
    public BigDecimal getYzje()
    {
        return yzje;
    }
    
    public void setYzje(BigDecimal yzje)
    {
        this.yzje = yzje;
    }
    
    public String getJqsj()
    {
        return jqsj;
    }
    
    public void setJqsj(String jqsj)
    {
        this.jqsj = jqsj;
    }
    
    public BigDecimal getTransferRate()
    {
        return transferRate;
    }
    
    public void setTransferRate(BigDecimal transferRate)
    {
        this.transferRate = transferRate;
    }
    
    public String getBidName()
    {
        return bidName;
    }
    
    public void setBidName(String bidName)
    {
        this.bidName = bidName;
    }
    
    public int getZqId()
    {
        return zqId;
    }
    
    public void setZqId(int zqId)
    {
        this.zqId = zqId;
    }
    
    public BigDecimal getSourceZqPrice()
    {
        return sourceZqPrice;
    }
    
    public void setSourceZqPrice(BigDecimal sourceZqPrice)
    {
        this.sourceZqPrice = sourceZqPrice;
    }
    
    public String getIsTransfered()
    {
        return isTransfered;
    }
    
    public void setIsTransfered(String isTransfered)
    {
        this.isTransfered = isTransfered;
    }
    
    public boolean isCanTrans()
    {
        return isCanTrans;
    }
    
    public void setCanTrans(boolean isCanTrans)
    {
        this.isCanTrans = isCanTrans;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "MyZqEntity [bidId=" + bidId + ", zqId=" + zqId + ", bidName=" + bidName + ", creditorId=" + creditorId
            + ", gmjg=" + gmjg + ", nhl=" + nhl + ", jxl=" + jxl + ", dsbx=" + dsbx + ", syqs=" + syqs + ", hkqs="
            + hkqs + ", xghkr=" + xghkr + ", status=" + status + ", jkzq=" + jkzq + ", process=" + process
            + ", surTime=" + surTime + ", isDay=" + isDay + ", yzje=" + yzje + ", jqsj=" + jqsj + ", transferRate="
            + transferRate + ", sourceZqPrice=" + sourceZqPrice + ", isTransfered=" + isTransfered + ", isCanTrans="
            + isCanTrans + ", zqzrRateMin=" + zqzrRateMin + ", zqzrRateMax=" + zqzrRateMax + "]";
    }
}
