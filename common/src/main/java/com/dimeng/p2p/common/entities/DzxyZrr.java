package com.dimeng.p2p.common.entities;

import java.math.BigDecimal;

/**
 * 电子协议-不良债权转让甲方转让人实体
 * @author huqinfu
 *
 */
public class DzxyZrr
{
    
    /**
     * 用户名（甲方）
     */
    public String loginName;
    
    /**
     * 真实姓名(甲方)
     */
    public String realName;
    
    /**
     * 公司名称(甲方)
     */
    public String companyName;
    
    /**
     * 营业执照号/社会信用代码(甲方)
     */
    public String papersNum;
    
    /**
     * 身份证号(甲方)
     */
    public String sfzh;
    
    /**
     * 用户类型（甲方）
     */
    public String userType;
    
    /**
     * 转让价格
     */
    public BigDecimal zrAmount = BigDecimal.ZERO;
    
    public String getLoginName()
    {
        return loginName;
    }
    
    public void setLoginName(String loginName)
    {
        this.loginName = loginName;
    }
    
    public String getRealName()
    {
        return realName;
    }
    
    public void setRealName(String realName)
    {
        this.realName = realName;
    }
    
    public String getCompanyName()
    {
        return companyName;
    }
    
    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }
    
    public String getPapersNum()
    {
        return papersNum;
    }
    
    public void setPapersNum(String papersNum)
    {
        this.papersNum = papersNum;
    }
    
    public String getSfzh()
    {
        return sfzh;
    }
    
    public void setSfzh(String sfzh)
    {
        this.sfzh = sfzh;
    }
    
    public String getUserType()
    {
        return userType;
    }
    
    public void setUserType(String userType)
    {
        this.userType = userType;
    }
    
    public BigDecimal getZrAmount()
    {
        return zrAmount;
    }
    
    public void setZrAmount(BigDecimal zrAmount)
    {
        this.zrAmount = zrAmount;
    }
    
}
