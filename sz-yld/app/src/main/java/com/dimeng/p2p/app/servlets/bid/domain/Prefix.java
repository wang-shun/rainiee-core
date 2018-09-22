package com.dimeng.p2p.app.servlets.bid.domain;

import com.dimeng.util.StringHelper;

/**
 * 托管类型
 */
public enum Prefix 
{

	/**
	 * 双乾
	 */
	SHUANGQIAN("双乾"),

	/**
	 * 宝付
	 */
	BAOFU("宝付"),
	
	/**
	 * 易宝
	 */
	YEEPAY("易宝"),
	
	/**
	 * 富友
	 */
	FUYOU("富友");

	protected final String chineseName;

	private Prefix(String chineseName) 
	{
		this.chineseName = chineseName;
	}

	/**
	 * 获取中文名称.
	 * 
	 * @return {@link String}
	 */
	public String getChineseName() 
	{
		return chineseName;
	}

	/**
	 * 解析字符串.
	 * 
	 * @return {@link Prefix}
	 */
	public static final Prefix parse(String value) 
	{
		if (StringHelper.isEmpty(value)) 
		{
			return null;
		}
		try 
		{
			return Prefix.valueOf(value);
		} 
		catch (Throwable t) 
		{
			return null;
		}
	}
	
}
