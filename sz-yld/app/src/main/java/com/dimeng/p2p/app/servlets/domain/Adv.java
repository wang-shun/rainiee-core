/**
 * 
 */
package com.dimeng.p2p.app.servlets.domain;

import java.io.Serializable;

/**
 * @author luoxiaoyan
 *
 */
public class Adv implements Serializable
{
	/**
     * 注释内容
     */
    private static final long serialVersionUID = -395978449301053045L;

    /**
     * 广告标题
     */
    private String advTitle;
	
    /**
     * 广告图片
     */
	private String advImg;
	
	/**
	 * 广告图片url地址
	 */
	private String advUrl;

	public String getAdvTitle() 
	{
		return advTitle;
	}

	public void setAdvTitle(String advTitle) 
	{
		this.advTitle = advTitle;
	}

	public String getAdvImg() 
	{
		return advImg;
	}

	public void setAdvImg(String advImg) 
	{
		this.advImg = advImg;
	}

	public String getAdvUrl() 
	{
		return advUrl;
	}

	public void setAdvUrl(String advUrl) 
	{
		this.advUrl = advUrl;
	}

    @Override
    public String toString()
    {
        return "Adv [advTitle=" + advTitle + ", advImg=" + advImg + ", advUrl=" + advUrl + "]";
    }
	
}
