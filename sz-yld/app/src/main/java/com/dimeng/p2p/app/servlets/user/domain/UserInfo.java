package com.dimeng.p2p.app.servlets.user.domain;

import java.io.Serializable;

/**
 * 
 * @author  
 * @version  [版本号, 2016年06月03日]
 */
public class UserInfo implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -6162449046910759010L;

    private int id;
    
    /**昵称*/
    private String accountName;
    
    /**真实姓名*/
    private String realName;
    
    /**带*号身份证*/
    private String idCard;
    
    /**手机号码*/
    private String phone;
    
    /**邮箱地址*/
    private String email;
    
    /**安全等级*/
    private int safeLevel;
    
    /**是否需要托管*/
    private boolean tg;
    
    /**托管方id*/
    private String usrCustId;
    
    /**未读消息数量*/
    private int letterCount;
    
    /**银行卡数量*/
    private int bankCount;
    
    /**是否已经绑定手机号码*/
    private boolean mobileVerified;
    
    /**是否已经身份验证*/
    private boolean idcardVerified;
    
    /**是否设置交易密码*/
    private boolean withdrawPsw;
    
    /**是否绑定邮箱*/
    private boolean emailVerified;
    
    /**
     * 用户真实姓名(不带*号)
     */
    private String name;
    
    /**
     * 用户手机号码
     */
    private String phoneNumber;
    
    /**
     * 用户是否已签到
     */
    private boolean signed;
    
    /**
     * 用户可用积分
     */
    private int usableScore;
    
    /**
     * 是否开启风险评估
     */
    private boolean isOpenRisk;
    
    /**
     * 用户风估类型
     */
    private String riskType;
    
    /**
     * 用户剩余风估次数
     */
    private int riskTimes;
    
    /**
     * 判断用户是否被拉黑
     */
    private boolean isHMD = false;
    
    /**
     * 判断用户是否是新手
     */
    private boolean isXS = true;
    
    /**
     * 推广码
     */
    private String tgm;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getAccountName()
    {
        return accountName;
    }
    
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }
    
    public String getRealName()
    {
        return realName;
    }
    
    public void setRealName(String realName)
    {
        this.realName = realName;
    }
    
    public String getIdCard()
    {
        return idCard;
    }
    
    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }
    
    public String getPhone()
    {
        return phone;
    }
    
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    
    public String getEmail()
    {
        return email;
    }
    
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    public int getSafeLevel()
    {
        return safeLevel;
    }
    
    public void setSafeLevel(int safeLevel)
    {
        this.safeLevel = safeLevel;
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
    
    public int getLetterCount()
    {
        return letterCount;
    }
    
    public void setLetterCount(int letterCount)
    {
        this.letterCount = letterCount;
    }
    
    public int getBankCount()
    {
        return bankCount;
    }
    
    public void setBankCount(int bankCount)
    {
        this.bankCount = bankCount;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getPhoneNumber()
    {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
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
    
    public boolean isOpenRisk()
    {
        return isOpenRisk;
    }
    
    public void setOpenRisk(boolean isOpenRisk)
    {
        this.isOpenRisk = isOpenRisk;
    }
    
    public String getRiskType()
    {
        return riskType;
    }
    
    public void setRiskType(String riskType)
    {
        this.riskType = riskType;
    }
    
    public int getRiskTimes()
    {
        return riskTimes;
    }
    
    public void setRiskTimes(int riskTimes)
    {
        this.riskTimes = riskTimes;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "UserInfo [id=" + id + ", accountName=" + accountName + ", realName=" + realName + ", idCard=" + idCard
            + ", phone=" + phone + ", email=" + email + ", safeLevel=" + safeLevel + ", tg=" + tg + ", usrCustId="
            + usrCustId + ", letterCount=" + letterCount + ", bankCount=" + bankCount + ", mobileVerified="
            + mobileVerified + ", idcardVerified=" + idcardVerified + ", withdrawPsw=" + withdrawPsw
            + ", emailVerified=" + emailVerified + ", name=" + name + ", phoneNumber=" + phoneNumber + ", signed="
            + signed + ", usableScore=" + usableScore + ", isOpenRisk=" + isOpenRisk + ", riskType=" + riskType
            + ", riskTimes=" + riskTimes + "]";
    }

    public boolean isHMD()
    {
        return isHMD;
    }

    public void setHMD(boolean isHMD)
    {
        this.isHMD = isHMD;
    }

    public boolean isXS()
    {
        return isXS;
    }

    public void setXS(boolean isXS)
    {
        this.isXS = isXS;
    }

    public String getTgm()
    {
        return tgm;
    }

    public void setTgm(String tgm)
    {
        this.tgm = tgm;
    }
}
