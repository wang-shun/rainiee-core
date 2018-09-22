/**
 * 
 */
package com.dimeng.p2p.app.servlets.domain;

import java.io.Serializable;

/**
 * @author luoxiaoyan
 *
 */
public class Notice implements Serializable
{
	/**
     * 注释内容
     */
    private static final long serialVersionUID = -6795895484875008905L;

    /** ID */
	private int id;
	
	/** 标题 */
	private String title;
	
	/** 类型 */
	private String type;
	
	/** 内容 */
	private String content;
	
	/** 来源 */
	private String from;
	
	/** 摘要 */
	private String desc;
	
	/** 发布时间 */
	private String releaseTime;
	
	/** 分享链接 */
	private String shareUrl;

	public int getId() 
	{
		return id;
	}

	public void setId(int id) 
	{
		this.id = id;
	}

	public String getTitle() 
	{
		return title;
	}

	public void setTitle(String title) 
	{
		this.title = title;
	}

	public String getType() 
	{
		return type;
	}

	public void setType(String type) 
	{
		this.type = type;
	}

	public String getContent() 
	{
		return content;
	}

	public void setContent(String content) 
	{
		this.content = content;
	}

	public String getFrom() 
	{
		return from;
	}

	public void setFrom(String from) 
	{
		this.from = from;
	}

	public String getDesc() 
	{
		return desc;
	}

	public void setDesc(String desc) 
	{
		this.desc = desc;
	}

	public String getReleaseTime() 
	{
		return releaseTime;
	}

	public void setReleaseTime(String releaseTime) 
	{
		this.releaseTime = releaseTime;
	}

	public String getShareUrl() 
	{
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) 
	{
		this.shareUrl = shareUrl;
	}

    @Override
    public String toString()
    {
        return "Notice [id=" + id + ", title=" + title + ", type=" + type + ", content=" + content + ", from=" + from
            + ", desc=" + desc + ", releaseTime=" + releaseTime + ", shareUrl=" + shareUrl + "]";
    }

}
