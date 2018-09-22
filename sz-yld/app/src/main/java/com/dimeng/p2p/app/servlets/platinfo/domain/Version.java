package com.dimeng.p2p.app.servlets.platinfo.domain;

import java.io.Serializable;
import java.sql.Date;


/**
 * 版本类型实现类
 * 
 * @author  ningjingan
 * @version  [版本号, 2016年3月23日]
 */
public class Version implements Serializable
{

    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -500627906038134241L;

    /**
     * 自增ID
     */
    private int id;
    
    /**
     * 版本号类型：1:android；2:IOS。默认1
     */
    private int verType;
    
    /**
     * 版本号
     */
    private String verNO;
    
    /**
     * 是否必须更新（0:否；1:是）
     */
    private int isMust;
    
    /**
     * 升级描述
     */
    private String mark;
    
    /**
     * 保存路径
     */
    private String localUrl;
    
    /**
     * 网络路径
     */
    private String url;
    
    /**
     * 0:未使用；1:使用中
     */
    private int status;
    
    /**
     * 发布时间
     */
    private Date publishTime;
    
    /**
     * 操作者
     */
    private String createBy;
    
    /**
     * 修改时间
     */
    private Date updateDate;
    
    /**
     * 更新者
     */
    private String updateBy;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public int getVerType()
    {
        return verType;
    }
    
    public void setVerType(int verType)
    {
        this.verType = verType;
    }
    
    public String getVerNO()
    {
        return verNO;
    }
    
    public void setVerNO(String verNO)
    {
        this.verNO = verNO;
    }
    
    public int getIsMust()
    {
        return isMust;
    }
    
    public void setIsMust(int isMust)
    {
        this.isMust = isMust;
    }
    
    public String getMark()
    {
        return mark;
    }
    
    public void setMark(String mark)
    {
        this.mark = mark;
    }
    
    public String getLocalUrl()
    {
        return localUrl;
    }
    
    public void setLocalUrl(String localUrl)
    {
        this.localUrl = localUrl;
    }
    
    public int getStatus()
    {
        return status;
    }
    
    public void setStatus(int status)
    {
        this.status = status;
    }
    
    public Date getPublishTime()
    {
        return publishTime;
    }
    
    public void setPublishTime(Date publishTime)
    {
        this.publishTime = publishTime;
    }
    
    public String getCreateBy()
    {
        return createBy;
    }
    
    public void setCreateBy(String createBy)
    {
        this.createBy = createBy;
    }
    
    public Date getUpdateDate()
    {
        return updateDate;
    }
    
    public void setUpdateDate(Date updateDate)
    {
        this.updateDate = updateDate;
    }
    
    public String getUpdateBy()
    {
        return updateBy;
    }
    
    public void setUpdateBy(String updateBy)
    {
        this.updateBy = updateBy;
    }
    
    public String getUrl()
    {
        return url;
    }
    
    public void setUrl(String url)
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "Version [id=" + id + ", verType=" + verType + ", verNO=" + verNO + ", isMust=" + isMust + ", mark="
            + mark + ", localUrl=" + localUrl + ", url=" + url + ", status=" + status + ", publishTime=" + publishTime
            + ", createBy=" + createBy + ", updateDate=" + updateDate + ", updateBy=" + updateBy + "]";
    }
    
}
