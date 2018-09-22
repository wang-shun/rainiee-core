package com.dimeng.p2p.modules.base.console.service.entity;

import java.sql.Timestamp;

import com.dimeng.framework.http.upload.UploadFile;

/**
 *App 版本号
 * 
 */
public abstract interface AppVersion {
    /** 
     * 版本号类型：1:android；2:IOS。默认1
     */
    public int getVerType(); 
    public String getFileName();
    /** 
     * 版本号
     */
    public String getVerNO();

    /** 
     * 是否必须更新（0:否；1:是）
     */
    public int getIsMustUpdate();

    /** 
     * 升级描述
     */
    public String getMark();

    /** 
     * 保存路径
     */
	public abstract UploadFile getFile() throws Throwable;
	 
    
    /** 
     * 状态 0:未使用；1:使用中
     */
    public int getStatus();
    
    /** 
     * 起始发布时间
     */
    public Timestamp getStartPublishDate();
    
    /** 
     * 结束发布时间
     */
    public Timestamp getEndPublishDate();
    
    /** 
     * 发布人
     */
    public String getPublisher();
    
    /** 
     * 网络URL地址
     */
    public String getUrl();
    
    /**
     * 是否只去一个(true 只去一个)limit 1
     */
    public boolean getLimit();
}
