package com.dimeng.p2p.app.servlets.user.domain;

import java.io.Serializable;

/**
 * 账户信息实现类
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年3月23日]
 */
public class AccountInfo implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 569145311516279643L;

    private int id;
    
    /** 用户名 */
    private String userName;
    
    /** 安全等级 */
    private int safeLevel;
    
    /** 头像 */
    private String photo;
    
    /** 是否已经绑定手机号码 */
    private boolean mobileVerified;
    
    /** 是否已经身份验证 */
    private boolean idcardVerified;
    
    /** 是否设置交易密码 */
    private boolean withdrawPsw;
    
    /** 是否绑定邮箱 */
    private boolean emailVerified;
    
    /** 账户总资产 */
    private String totalAmount;
    
    /** 冻结资产 */
    private String freezeAmount;
    
    /** 净资产 */
    private String merelyAmount;
    
    /** 投资资产 */
    private String investAmount;
    
    /** 借款负债 */
    private String loanAmount;
    
    /** 待还总金额 */
    private String alsoAmount;
    
    /** 已赚总金额 */
    private String earnAmount;
    
    /** 账户余额 */
    private String overAmount;
    
    /** 是否要托管需 */
    private boolean tg;
    
    /** 托管方id */
    private String usrCustId;
    
    /**
     * 体验金
     */
    private String experienceAmount;
    
    /**
     * 未使用加息券张数
     */
    private int unUserJxqCount;
    
    /**
     * 未使用红包金额
     */
    private String hbJe;
    
    /**
     * 判断用户是否已签到
     */
    private boolean signed;
    
    /**
     * 用户可用积分
     */
    private int usableScore;
    
    /**
     * 我的借款总额
     */
    private String countMoney;
    
    /**
     * 我的投资数量
     */
    private int tzCount;
    
    /**
     * 我的债权数量
     */
    private int zqCount;
    
    /**
     * 我的订单数量
     */
    private int orderCount;
    
    /**
     * 我的购物车数量
     */
    private int carCount;
    
    /**
     * 风险保证金
     */
    private String fxbzj;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getUserName()
    {
        return userName;
    }
    
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    public int getSafeLevel()
    {
        return safeLevel;
    }
    
    public void setSafeLevel(int safeLevel)
    {
        this.safeLevel = safeLevel;
    }
    
    public String getPhoto()
    {
        return photo;
    }
    
    public void setPhoto(String photo)
    {
        this.photo = photo;
    }
    
    public boolean isMobileVerified()
    {
        return mobileVerified;
    }
    
    public void setMobileVerified(boolean mobileVerified)
    {
        this.mobileVerified = mobileVerified;
    }
    
    public boolean isIdcardVerified()
    {
        return idcardVerified;
    }
    
    public void setIdcardVerified(boolean idcardVerified)
    {
        this.idcardVerified = idcardVerified;
    }
    
    public boolean isWithdrawPsw()
    {
        return withdrawPsw;
    }
    
    public void setWithdrawPsw(boolean withdrawPsw)
    {
        this.withdrawPsw = withdrawPsw;
    }
    
    public boolean isEmailVerified()
    {
        return emailVerified;
    }
    
    public void setEmailVerified(boolean emailVerified)
    {
        this.emailVerified = emailVerified;
    }
    
    public String getTotalAmount()
    {
        return totalAmount;
    }
    
    public void setTotalAmount(String totalAmount)
    {
        this.totalAmount = totalAmount;
    }
    
    public String getFreezeAmount()
    {
        return freezeAmount;
    }
    
    public void setFreezeAmount(String freezeAmount)
    {
        this.freezeAmount = freezeAmount;
    }
    
    public String getMerelyAmount()
    {
        return merelyAmount;
    }
    
    public void setMerelyAmount(String merelyAmount)
    {
        this.merelyAmount = merelyAmount;
    }
    
    public String getInvestAmount()
    {
        return investAmount;
    }
    
    public void setInvestAmount(String investAmount)
    {
        this.investAmount = investAmount;
    }
    
    public String getLoanAmount()
    {
        return loanAmount;
    }
    
    public void setLoanAmount(String loanAmount)
    {
        this.loanAmount = loanAmount;
    }
    
    public String getAlsoAmount()
    {
        return alsoAmount;
    }
    
    public void setAlsoAmount(String alsoAmount)
    {
        this.alsoAmount = alsoAmount;
    }
    
    public String getEarnAmount()
    {
        return earnAmount;
    }
    
    public void setEarnAmount(String earnAmount)
    {
        this.earnAmount = earnAmount;
    }
    
    public String getOverAmount()
    {
        return overAmount;
    }
    
    public void setOverAmount(String overAmount)
    {
        this.overAmount = overAmount;
    }
    
    public boolean isTg()
    {
        return tg;
    }
    
    public void setTg(boolean tg)
    {
        this.tg = tg;
    }
    
    public String getUsrCustId()
    {
        return usrCustId;
    }
    
    public void setUsrCustId(String usrCustId)
    {
        this.usrCustId = usrCustId;
    }
    
    public String getExperienceAmount()
    {
        return experienceAmount;
    }
    
    public void setExperienceAmount(String experienceAmount)
    {
        this.experienceAmount = experienceAmount;
    }
    
    public int getUnUserJxqCount()
    {
        return unUserJxqCount;
    }
    
    public void setUnUserJxqCount(int unUserJxqCount)
    {
        this.unUserJxqCount = unUserJxqCount;
    }
    
    public String getHbJe()
    {
        return hbJe;
    }
    
    public void setHbJe(String hbJe)
    {
        this.hbJe = hbJe;
    }
    
    public boolean isSigned()
    {
        return signed;
    }
    
    public void setSigned(boolean signed)
    {
        this.signed = signed;
    }
    
    public int getUsableScore()
    {
        return usableScore;
    }
    
    public void setUsableScore(int usableScore)
    {
        this.usableScore = usableScore;
    }

    public String getCountMoney()
    {
        return countMoney;
    }

    public void setCountMoney(String countMoney)
    {
        this.countMoney = countMoney;
    }

    public int getTzCount()
    {
        return tzCount;
    }

    public void setTzCount(int tzCount)
    {
        this.tzCount = tzCount;
    }

    public int getZqCount()
    {
        return zqCount;
    }

    public void setZqCount(int zqCount)
    {
        this.zqCount = zqCount;
    }

    public int getOrderCount()
    {
        return orderCount;
    }

    public void setOrderCount(int orderCount)
    {
        this.orderCount = orderCount;
    }

    public int getCarCount()
    {
        return carCount;
    }

    public void setCarCount(int carCount)
    {
        this.carCount = carCount;
    }

    public String getFxbzj()
    {
        return fxbzj;
    }

    public void setFxbzj(String fxbzj)
    {
        this.fxbzj = fxbzj;
    }

    @Override
    public String toString()
    {
        return "AccountInfo [id=" + id + ", userName=" + userName + ", safeLevel=" + safeLevel + ", photo=" + photo
            + ", mobileVerified=" + mobileVerified + ", idcardVerified=" + idcardVerified + ", withdrawPsw="
            + withdrawPsw + ", emailVerified=" + emailVerified + ", totalAmount=" + totalAmount + ", freezeAmount="
            + freezeAmount + ", merelyAmount=" + merelyAmount + ", investAmount=" + investAmount + ", loanAmount="
            + loanAmount + ", alsoAmount=" + alsoAmount + ", earnAmount=" + earnAmount + ", overAmount=" + overAmount
            + ", tg=" + tg + ", usrCustId=" + usrCustId + ", experienceAmount=" + experienceAmount
            + ", unUserJxqCount=" + unUserJxqCount + ", hbJe=" + hbJe + ", signed=" + signed + ", usableScore="
            + usableScore + ", countMoney=" + countMoney + ", tzCount=" + tzCount + ", zqCount=" + zqCount
            + ", orderCount=" + orderCount + ", carCount=" + carCount + ", fxbzj=" + fxbzj + "]";
    }
}
