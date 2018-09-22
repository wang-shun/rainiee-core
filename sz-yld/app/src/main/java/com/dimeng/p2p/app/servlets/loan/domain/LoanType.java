package com.dimeng.p2p.app.servlets.loan.domain;

import java.io.Serializable;

/**
 * 
 * 借款类型实体类
 * 
 * @author  suwei
 * @version  [版本号, 2016年6月3日]
 */
public class LoanType implements Serializable
{
	
	/**
     * 注释内容
     */
    private static final long serialVersionUID = -238088377567751754L;
    
    /**
     * 借款类型
     */
    private int type;
    
    /**
     * 借款名称
     */
	private String name;
	
	public int getType() 
	{
		return type;
	}
	
	public void setType(int type) 
	{
		this.type = type;
	}
	
	public String getName() 
	{
		return name;
	}
	
	public void setName(String name) 
	{
		this.name = name;
	}

    @Override
    public String toString()
    {
        return "LoanType [type=" + type + ", name=" + name + "]";
    }
	
}
