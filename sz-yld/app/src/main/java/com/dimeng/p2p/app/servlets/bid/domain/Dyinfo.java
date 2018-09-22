/**
 * 
 */
package com.dimeng.p2p.app.servlets.bid.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author luoxiaoyan
 *
 */
public class Dyinfo implements Serializable
{
	/**
     * 注释内容
     */
    private static final long serialVersionUID = -1594942800352798295L;

    /** 抵押名称 */
	private String dyName;
	
	/** 抵押属性 */
	private List<Dysx> dysxs;

	public String getDyName() 
	{
		return dyName;
	}

	public void setDyName(String dyName) 
	{
		this.dyName = dyName;
	}

	public List<Dysx> getDysxs() 
	{
		return dysxs;
	}

	public void setDysxs(List<Dysx> dysxs) 
	{
		this.dysxs = dysxs;
	}

    @Override
    public String toString()
    {
        return "Dyinfo [dyName=" + dyName + ", dysxs=" + dysxs + "]";
    }
    
}
