package com.dimeng.p2p.app.servlets.pay.domain;

import java.io.Serializable;

public class Address implements Serializable
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = -3057741777234906061L;
    
    /**
     * 区域id
     */
    private int id;
    
    private String sheng;
    
    private String shi;
    
    private String xian;
    
    
	public int getId() 
	{
		return id;
	}
	
	public void setId(int id) 
	{
		this.id = id;
	}
	
	public String getSheng() 
	{
		return sheng;
	}
	
	public void setSheng(String sheng) 
	{
		this.sheng = sheng;
	}
	
	public String getShi() 
	{
		return shi;
	}
	
	public void setShi(String shi) 
	{
		this.shi = shi;
	}
	
	public String getXian() 
	{
		return xian;
	}
	
	public void setXian(String xian) 
	{
		this.xian = xian;
	}

    @Override
    public String toString()
    {
        return "Address [id=" + id + ", sheng=" + sheng + ", shi=" + shi + ", xian=" + xian + "]";
    }
    
}
