package com.dimeng.p2p.app.servlets.user.domain;

import java.io.Serializable;

/**
 * 银行卡信息
 * @author Administrator
 *
 */
public class BankCard  implements Serializable
{
	
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
	 * id
	 */
	private int id;
	
    /**
	 * 用户ID
	 */
	private int account;
	
	/**
	 * 银行ID
	 */
	private int bankID;
	
	/**
	 * 银行名称
	 */
	private String bankname;
	
	/**
	 * 开户行地址
	 */
	private String city;
	
	/**
	 * 开户行地址Id
	 */
	private int cityId;
	
	/**
	 * 开户行名称
	 */
	private String bankKhhName;
	
	/**
	 * 银行卡号
	 */
	private String bankNumber;
	
	/**
	 * 银行卡号
	 */
	private String bankRealNumber;
	
	/**
	 * 银行卡状态
	 */
	private String status;
	
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getAccount()
    {
        return account;
    }
    
    public void setAccount(int account)
    {
        this.account = account;
    }
    
    public int getBankID()
    {
        return bankID;
    }
    
    public void setBankID(int bankID)
    {
        this.bankID = bankID;
    }
    
    public String getBankname()
    {
        return bankname;
    }
    
    public void setBankname(String bankname)
    {
        this.bankname = bankname;
    }
    
    public String getCity()
    {
        return city;
    }
    
    public void setCity(String city)
    {
        this.city = city;
    }
    
    public int getCityId() {
		return cityId;
	}
    
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	
	public String getBankKhhName()
    {
        return bankKhhName;
    }
	
    public void setBankKhhName(String bankKhhName)
    {
        this.bankKhhName = bankKhhName;
    }
    
    public String getBankNumber()
    {
        return bankNumber;
    }
    
    public void setBankNumber(String bankNumber)
    {
        this.bankNumber = bankNumber;
    }
    
    public String getBankRealNumber() {
		return bankRealNumber;
	}
    
	public void setBankRealNumber(String bankRealNumber) {
		this.bankRealNumber = bankRealNumber;
	}
	
	public String getStatus()
    {
        return status;
    }
	
    public void setStatus(String status)
    {
        this.status = status;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "BankCard [id=" + id + ", account=" + account + ", bankID=" + bankID + ", bankname=" + bankname
            + ", city=" + city + ", cityId=" + cityId + ", bankKhhName=" + bankKhhName + ", bankNumber=" + bankNumber
            + ", bankRealNumber=" + bankRealNumber + ", status=" + status + "]";
    }
	
    
}
