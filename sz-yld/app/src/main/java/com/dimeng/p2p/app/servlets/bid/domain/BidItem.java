package com.dimeng.p2p.app.servlets.bid.domain;

import java.io.Serializable;
import java.util.List;

public class BidItem implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 4198682300284186554L;

    /** 企业名字 */
    private String qyName;
    
    /** 本次融资金额 */
    private String amount;
    
    /** 区域 */
    private String area;
    
    /** 还款日期 */
    private String repayDate;
    
    /** 年化利率 */
    private String rate;
    
    /** 投资截止日期 */
    private String endDate;
    
    /** 借款用途 */
    private String bidUse;
    
    /** 还款来源 */
    private String repaySource;
    
    /** 项目描述 */
    private String desc;
    
    /** 企业注册年 */
    private String regYear;
    
    /** 企业注册金额 */
    private String regAmount;
    
    /** 企业净资产 */
    private String earnAmount;
    
    /** 现金流 */
    private String cash;
    
    /** 行业 */
    private String business;
    
    /** 经营情况 */
    private String operation;
    
    /** 投诉情况 */
    private String complaints;
    
    /** 征信记录 */
    private String credit;
    
    /** 企业财务 */
    private List<QyFinance> qyFinanceList;
    
    /** 担保机构 */
    private String dbjg;
    
    /** 担保机构介绍 */
    private String dbdesc;
    
    /** 担保情况 */
    private String dbinfo;
    
    /** 风险控制措施 */
    private String fkcs;
    
    /** 反担保情况 */
    private String fdbinfo;
    
    /** 抵押名称 */
    private List<Dyinfo> dys;
    
    /** 是否是担保标*/
    private String isDd;
    
    /**
     * 信用等级
     */
    private String xyLevel;
    
    /**
     * 是否显示风险提示函
     */
    private boolean isShowFXTS;
    
    
    /**
     * 增加是否显示风控
     */
    private boolean isOpenRisk;
    
    /**
     * 增加是否显示企业信息
     */
    private String isQy;
    
    public String getQyName()
    {
        return qyName;
    }
    
    public void setQyName(String qyName)
    {
        this.qyName = qyName;
    }
    
    public String getAmount()
    {
        return amount;
    }
    
    public void setAmount(String amount)
    {
        this.amount = amount;
    }
    
    public String getArea()
    {
        return area;
    }
    
    public void setArea(String area)
    {
        this.area = area;
    }
    
    public String getRepayDate()
    {
        return repayDate;
    }
    
    public void setRepayDate(String repayDate)
    {
        this.repayDate = repayDate;
    }
    
    public String getRate()
    {
        return rate;
    }
    
    public void setRate(String rate)
    {
        this.rate = rate;
    }
    
    public String getEndDate()
    {
        return endDate;
    }
    
    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }
    
    public String getBidUse()
    {
        return bidUse;
    }
    
    public void setBidUse(String bidUse)
    {
        this.bidUse = bidUse;
    }
    
    public String getRepaySource()
    {
        return repaySource;
    }
    
    public void setRepaySource(String repaySource)
    {
        this.repaySource = repaySource;
    }
    
    public String getDesc()
    {
        return desc;
    }
    
    public void setDesc(String desc)
    {
        this.desc = desc;
    }
    
    public String getRegYear()
    {
        return regYear;
    }
    
    public void setRegYear(String regYear)
    {
        this.regYear = regYear;
    }
    
    public String getRegAmount()
    {
        return regAmount;
    }
    
    public void setRegAmount(String regAmount)
    {
        this.regAmount = regAmount;
    }
    
    public String getEarnAmount()
    {
        return earnAmount;
    }
    
    public void setEarnAmount(String earnAmount)
    {
        this.earnAmount = earnAmount;
    }
    
    public String getCash()
    {
        return cash;
    }
    
    public void setCash(String cash)
    {
        this.cash = cash;
    }
    
    public String getBusiness()
    {
        return business;
    }
    
    public void setBusiness(String business)
    {
        this.business = business;
    }
    
    public String getOperation()
    {
        return operation;
    }
    
    public void setOperation(String operation)
    {
        this.operation = operation;
    }
    
    public String getComplaints()
    {
        return complaints;
    }
    
    public void setComplaints(String complaints)
    {
        this.complaints = complaints;
    }
    
    public String getCredit()
    {
        return credit;
    }
    
    public void setCredit(String credit)
    {
        this.credit = credit;
    }
    
    public List<QyFinance> getQyFinanceList()
    {
        return qyFinanceList;
    }
    
    public void setQyFinanceList(List<QyFinance> qyFinanceList)
    {
        this.qyFinanceList = qyFinanceList;
    }
    
    public String getDbjg()
    {
        return dbjg;
    }
    
    public void setDbjg(String dbjg)
    {
        this.dbjg = dbjg;
    }
    
    public String getDbdesc()
    {
        return dbdesc;
    }
    
    public void setDbdesc(String dbdesc)
    {
        this.dbdesc = dbdesc;
    }
    
    public String getDbinfo()
    {
        return dbinfo;
    }
    
    public void setDbinfo(String dbinfo)
    {
        this.dbinfo = dbinfo;
    }
    
    public String getFkcs()
    {
        return fkcs;
    }
    
    public void setFkcs(String fkcs)
    {
        this.fkcs = fkcs;
    }
    
    public String getFdbinfo()
    {
        return fdbinfo;
    }
    
    public void setFdbinfo(String fdbinfo)
    {
        this.fdbinfo = fdbinfo;
    }
    
    public List<Dyinfo> getDys()
    {
        return dys;
    }
    
    public void setDys(List<Dyinfo> dys)
    {
        this.dys = dys;
    }
    
    public String getIsDd()
    {
        return isDd;
    }
    
    public void setIsDd(String isDd)
    {
        this.isDd = isDd;
    }
    
    public String getXyLevel()
    {
        return xyLevel;
    }
    
    public void setXyLevel(String xyLevel)
    {
        this.xyLevel = xyLevel;
    }
    
    public boolean isOpenRisk()
    {
        return isOpenRisk;
    }
    
    public void setOpenRisk(boolean isOpenRisk)
    {
        this.isOpenRisk = isOpenRisk;
    }

    public String getIsQy()
    {
        return isQy;
    }

    public void setIsQy(String isQy)
    {
        this.isQy = isQy;
    }

	public boolean isShowFXTS() {
		return isShowFXTS;
	}

	public void setIsShowFXTS(boolean isShowFXTS) {
		this.isShowFXTS = isShowFXTS;
	}

    @Override
    public String toString()
    {
        return "BidItem [qyName=" + qyName + ", amount=" + amount + ", area=" + area + ", repayDate=" + repayDate
            + ", rate=" + rate + ", endDate=" + endDate + ", bidUse=" + bidUse + ", repaySource=" + repaySource
            + ", desc=" + desc + ", regYear=" + regYear + ", regAmount=" + regAmount + ", earnAmount=" + earnAmount
            + ", cash=" + cash + ", business=" + business + ", operation=" + operation + ", complaints=" + complaints
            + ", credit=" + credit + ", qyFinanceList=" + qyFinanceList + ", dbjg=" + dbjg + ", dbdesc=" + dbdesc
            + ", dbinfo=" + dbinfo + ", fkcs=" + fkcs + ", fdbinfo=" + fdbinfo + ", dys=" + dys + ", isDd=" + isDd
            + ", xyLevel=" + xyLevel + ", isShowFXTS=" + isShowFXTS + ", isOpenRisk=" + isOpenRisk + ", isQy=" + isQy
            + "]";
    }
	
}
