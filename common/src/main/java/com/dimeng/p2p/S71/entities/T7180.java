package com.dimeng.p2p.S71.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/** 
 * App版本管理
 */
public class T7180 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 版本号类型：1:android；2:IOS。默认1
     */
    public int F02;
  
    /** 
     * 版本号
     */
    public String F03;
    
    /** 
     * 是否必须更新（0:否；1:是）
     */
    public int F04;

    /** 
     * 升级描述
     */
    public String F05;
    
    /** 
     * 本地保存路径
     */
    public String F06;
 
    /** 
     * 0:未使用；1:使用中
     */
    public int F07;
    /** 
     * 发布时间
     */
    public Timestamp F08;
    /** 
     * 操作者
     */
    public String F09;
    /** 
     * 修改时间
     */
    public Timestamp F10;
    /** 
     * 更新者
     */
    public String F11;
    
    /** 
     * 网络URL地址
     */
    public String F12;
    
}
