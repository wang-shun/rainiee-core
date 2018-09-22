/**
 * 
 */
package com.dimeng.p2p.app.servlets.bid.domain;

import java.io.Serializable;

/**
 * @author luoxiaoyan
 *
 */
public class Dysx implements Serializable
{

	/**
     * 注释内容
     */
    private static final long serialVersionUID = 8669618078235643687L;

    /** 抵押属性名称 */
	private String dxsxName;
	
	/** 抵押属性值 */
	private String dxsxVal;

	public String getDxsxName() 
	{
		return dxsxName;
	}

	public void setDxsxName(String dxsxName) 
	{
		this.dxsxName = dxsxName;
	}

	public String getDxsxVal() 
	{
		return dxsxVal;
	}

	public void setDxsxVal(String dxsxVal) 
	{
		this.dxsxVal = dxsxVal;
	}

    @Override
    public String toString()
    {
        return "Dysx [dxsxName=" + dxsxName + ", dxsxVal=" + dxsxVal + "]";
    }
	
}
