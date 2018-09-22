package com.dimeng.p2p.S71.entities;

import com.dimeng.framework.service.AbstractEntity;

/** 
 * 平台信息表
 */
public class T7101 extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /** 
     * 平台用户ID,参考T6110.F01,一旦设置则不能修改
     */
    public int F01;
    
    /** 
     * 前台页头LOGO文件编码
     */
    public String F02;
    
    /** 
     * 后台登录LOGO文件编码
     */
    public String F03;
    
    /** 
     * 后台首页LOGO文件编码
     */
    public String F04;
    
    /**
     * 微信二维码文件编码
     */
    public String F05;
    
    /**
     * 微博二维码文件编码
     */
    public String F06;
    
    /**
     * 手机客户端文件编码
     */
    public String F07;
    
    /**
     * 标的默认图标文件编码
     */
    public String F08;
    
    /**
     * 设置平台水印图标
     */
    public String F09;
    
    /**
     * 用户中心头像默认图标（男）
     */
    public String F10;
    
    /**
     * 用户中心头像默认图标（女）
     */
    public String F11;
    
    /** 
     * 前台页尾LOGO文件编码
     */
    public String F12;
}
