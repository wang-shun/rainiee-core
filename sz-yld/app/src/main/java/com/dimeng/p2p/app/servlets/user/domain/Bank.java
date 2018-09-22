package com.dimeng.p2p.app.servlets.user.domain;

import java.io.Serializable;

/**
 * 银行卡实现类
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年3月23日]
 */
public class Bank implements Serializable 
{

	/**
     * 注释内容
     */
    private static final long serialVersionUID = -9220415889217569911L;
    
    /**
	 * 银行卡id
	 */
	private int id;
	
	/**
	 * 银行卡名称
	 */
	private String bankName;
	
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getBankName()
    {
        return bankName;
    }
    public void setBankName(String bankName)
    {
        this.bankName = bankName;
    }
    
    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "Bank [id=" + id + ", bankName=" + bankName + "]";
    }
	
}
