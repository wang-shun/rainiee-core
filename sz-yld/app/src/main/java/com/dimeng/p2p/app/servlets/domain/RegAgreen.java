/**
 * 
 */
package com.dimeng.p2p.app.servlets.domain;

import java.io.Serializable;

/**
 * @author luoxiaoyan
 *
 */
public class RegAgreen implements Serializable
{

	/**
     * 注释内容
     */
    private static final long serialVersionUID = -6540720615165377320L;
    
    /**
     * 协议阅读次数
     */
    private int readCount;
    
    /**
     * 协议内容
     */
	private String content;  
	
	/**
	 * 协议创建时间
	 */
	private String createTime;
	
	/**
	 * 最后更新时间
	 */
	private String udpateTime;

	public int getReadCount() 
	{
		return readCount;
	}

	public void setReadCount(int readCount) 
	{
		this.readCount = readCount;
	}

	public String getContent() 
	{
		return content;
	}

	public void setContent(String content) 
	{
		this.content = content;
	}


	public String getCreateTime() 
	{
		return createTime;
	}

	public void setCreateTime(String createTime) 
	{
		this.createTime = createTime;
	}

	public String getUdpateTime() 
	{
		return udpateTime;
	}

	public void setUdpateTime(String udpateTime) 
	{
		this.udpateTime = udpateTime;
	}

    @Override
    public String toString()
    {
        return "RegAgreen [readCount=" + readCount + ", content=" + content + ", createTime=" + createTime
            + ", udpateTime=" + udpateTime + "]";
    }

}
