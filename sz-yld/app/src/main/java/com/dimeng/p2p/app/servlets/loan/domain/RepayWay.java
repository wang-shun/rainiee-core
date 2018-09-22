package com.dimeng.p2p.app.servlets.loan.domain;

import java.io.Serializable;

/**
 * 还款方式封装类
 * @author suwei
 *
 */
public class RepayWay implements Serializable
{

	/**
     * 注释内容
     */
    private static final long serialVersionUID = 4472623260834601399L;

    /**
     * 还款方式类型
     */
    private String type;
	
    /**
     * 还款方式名称
     */
	private String name;
	
	public String getType() 
	{
		return type;
	}
	
	public void setType(String type) 
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
        return "RepayWay [type=" + type + ", name=" + name + "]";
    }
	
}
