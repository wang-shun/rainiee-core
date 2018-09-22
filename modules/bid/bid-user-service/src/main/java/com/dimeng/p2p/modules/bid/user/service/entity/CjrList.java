package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;

/**
 * 出借人列表
 * @author gaoshaolong
 *
 */
public class CjrList {
    /**
     * 债券号码
     */
    public String bondsNo;
	/**
	 * 出借人
	 */
	public String loginName;
    
    /**
     * 出借人的账户类型
     */
    public String userType;
	/**
	 * 登陆名全称 没马赛克
	 */
	public String loginNameAll;
	/**
	 * 借出金额
	 */
	public BigDecimal cjje;
	/**
	 * 身份证号码
	 */
	public String card;

	/**
     * 全文显示身份证号码，非加密，非星号
     */
    public String realCard;
    
	/**
	 * 真实姓名
	 */
	public String realName;
    
    /**
     * 公司名称
     */
    public String companyName;
    
    /**
     * 公司营业执照或者社会信用代码（三证合一时显示社会信用代码）
     */
    public String papersNum;
	    
    /**
     * @return 返回 userType
     */
    public String getUserType()
    {
        return userType;
    }
    
    /**
     * @param 对userType进行赋值
     */
    public void setUserType(String userType)
    {
        this.userType = userType;
    }
    
    /**
     * @return 返回 companyName
     */
    public String getCompanyName()
    {
        return companyName;
    }
    
    /**
     * @param 对companyName进行赋值
     */
    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }
    
    /**
     * @return 返回 papersNum
     */
    public String getPapersNum()
    {
        return papersNum;
    }
    
    /**
     * @param 对papersNum进行赋值
     */
    public void setPapersNum(String papersNum)
    {
        this.papersNum = papersNum;
    }
    
    /**
     * 真实名全部没马赛克
     */
	public String realNameAll;
	/**
     * 全文真实姓名，非加密，非星号
     */
    public String noStartRealName;
	/**
	 * 借款期限
	 */
	public String jkqx;
	/**
	 * 全部应收
	 */
	public BigDecimal qbys;
	
    /**
     * 用户手机号
     */
    public String userPhone;
    
    /**
     * 用户邮箱
     */
    public String userEmail;
	
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getJkqx() {
		return jkqx;
	}
	public void setJkqx(String jkqx) {
		this.jkqx = jkqx;
	}
	public BigDecimal getCjje() {
		return cjje;
	}
	public void setCjje(BigDecimal cjje) {
		this.cjje = cjje;
	}
	public BigDecimal getQbys() {
		return qbys;
	}
	public void setQbys(BigDecimal qbys) {
		this.qbys = qbys;
	}
	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
    
    public String getUserPhone()
    {
        return userPhone;
    }
    
    public void setUserPhone(String userPhone)
    {
        this.userPhone = userPhone;
    }
    
    public String getUserEmail()
    {
        return userEmail;
    }
    
    public void setUserEmail(String userEmail)
    {
        this.userEmail = userEmail;
    }

}
