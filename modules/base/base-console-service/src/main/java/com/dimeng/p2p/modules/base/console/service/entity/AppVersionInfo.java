package com.dimeng.p2p.modules.base.console.service.entity;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 *App 版本号
 * 
 */
public class AppVersionInfo extends AbstractEntity
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    /** 
     * 版本ID
     */
    public int id;
    
    /** 
     * 版本号类型：1:android；2:IOS。默认1
     */
    public int verType;
    
    /** 
     * 版本号
     */
    public String verNO;
    
    /** 
     * 是否必须更新（0:否；1:是）
     */
    public int isMustUpdate;
    
    /** 
     * 升级描述
     */
    public String mark;
    
    /** 
     * 保存路径
     */
    public String file;
    
    /** 
     * 状态 0:未使用；1:使用中
     */
    public int status;
    
    /** 
     * 发布时间
     */
    public Timestamp publishTime;
    
    /** 
     * 发布者ID
     */
    public int publisherId;
    
    /** 
     * 发布者用户名
     */
    public String publisher;
    
    /** 
     * 更新者ID
     */
    public int updateId;
    
    /** 
     * 更新者用户名
     */
    public String updater;
    
    /** 
     * 更新时间
     */
    public Timestamp updateTime;
    
    /** 
     * 网络URL地址
     */
    public String url;
    
}
