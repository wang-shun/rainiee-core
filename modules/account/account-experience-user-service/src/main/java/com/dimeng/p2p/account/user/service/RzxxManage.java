package com.dimeng.p2p.account.user.service;

import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S51.entities.T5123;
import com.dimeng.p2p.S51.enums.T5123_F03;
import com.dimeng.p2p.S61.entities.T6120;
import com.dimeng.p2p.account.user.service.entity.RzxxInfo;
import com.dimeng.p2p.account.user.service.entity.XyInfo;
import com.dimeng.p2p.account.user.service.entity.Xyjl;

/**
 * 认证信息管理
 * 
 * @author Administrator
 * 
 */
public abstract interface RzxxManage extends Service
{
    /**
     * 查询个人非必要认证信息 
     * @return
     * @throws Throwable
     */
    public abstract RzxxInfo[] getGRInfo()
        throws Throwable;
    
    /**
     * 查询认证信息表中状态为启用信息
     * @param T5123_F03 是否为必要认证项
     * @return
     * @throws Throwable
     */
    public abstract T5123[] getT5123(T5123_F03 t5123_F03)
        throws Throwable;
    
    /**
     * 查询个人必要认证信息
     * @return
     * @throws Throwable
     */
    public abstract RzxxInfo[] getGRMustInfo()
        throws Throwable;
    
    /**
     * 得到信用信息
     * @return
     * @throws Throwable
     */
    public abstract XyInfo getXyInfo()
        throws Throwable;
    
    /**
     * 得到信用记录信息
     * @return
     * @throws Throwable
     */
    public abstract Xyjl getXyjl()
        throws Throwable;
    
    /**
     * 上传认证附件
     * @param rzxID 认证项ID
     * @param file
     * @throws Throwable
     */
    public void uploadFile(int rzxID, String fileCode, UploadFile file)
        throws Throwable;
    
    /**
     * 查询企业非必要认证信息 
     * @return
     * @throws Throwable
     */
    public abstract RzxxInfo[] getQYInfo()
        throws Throwable;
    
    /**
     * 查询企业必要认证信息
     * @return
     * @throws Throwable
     */
    public abstract RzxxInfo[] getQYMustInfo()
        throws Throwable;
    
    /**
     * 插入认证记录
     * @param rzxID
     * @return
     * @throws Throwable
     */
    public int insertRzjl(int rzxID)
        throws Throwable;
    
    /** 
     * 插入附件信息
     * @param rzjlID
     * @param fileCode
     * @param file
     * @throws Throwable
     */
    public void insertFile(int rzjlID, String fileCode, UploadFile file)
        throws Throwable;
    
    /** 
     * 获取认证信息
     * @param rzxID
     * @return
     * @throws Throwable
     */
    public T6120 getRzxx(int rzxID)
        throws Throwable;
}
