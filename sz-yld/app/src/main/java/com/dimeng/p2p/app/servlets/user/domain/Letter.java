package com.dimeng.p2p.app.servlets.user.domain;

import java.io.Serializable;

/**
 * 站内信实现类
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年3月22日]
 */
public class Letter implements Serializable 
{

	/**
     * 注释内容
     */
    private static final long serialVersionUID = 8888853559136772131L;
    /**
	 * 站内信id
	 */
	private int id;
	
	/**
	 * 发送时间
	 */
	private String sendTime;
	
	/**
	 * 标题
	 */
	private String title;
	
	/**
	 * 状态
	 */
	private String status;
	
	/**
	 * 内容
	 */
	private String content;
	
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getSendTime()
    {
        return sendTime;
    }
    
    public void setSendTime(String sendTime)
    {
        this.sendTime = sendTime;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "Letter [id=" + id + ", sendTime=" + sendTime + ", title=" + title + ", status=" + status + ", content="
            + content + "]";
    }
	
	
}
