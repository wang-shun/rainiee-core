package com.dimeng.p2p.modules.account.console.service.entity;

import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.service.AbstractEntity;

public class Ptgl extends AbstractEntity
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /** 
     * 平台用户ID,参考T6110.F01,一旦设置则不能修改
     */
    public int F01;
    
    /**
     * 前台页头LOGO文件编码
     */
    public UploadFile qtlg;
    
    /**
     * 前台页尾LOGO文件编码
     */
    public UploadFile qtywlg;
    
    /**
     * 后台登录LOGO文件编码
     */
    public UploadFile htdl;
    
    /**
     * 后台首页LOGO文件编码
     */
    public UploadFile htsy;
    
    /**
     * 前台微信文件编码
     */
    public UploadFile qtwx;
    
    /**
     * 前台微博文件编码
     */
    public UploadFile qtwb;
    
    /**
     * 前台手机客户端文件编码
     */
    public UploadFile qtapp;
    
    /**
     * 前台标的默认图标文件编码
     */
    public UploadFile qtbd;
    
    /**
     * 平台水印图标
     */
    public UploadFile sytb;
    
    /**
     * 用户中心头像图标（男）
     */
    public UploadFile txnantb;
    
    /**
     * 用户中心头像图标（女）
     */
    public UploadFile txnvtb;
    
    /**
     *序号 
     */
    public int index;
    
}
