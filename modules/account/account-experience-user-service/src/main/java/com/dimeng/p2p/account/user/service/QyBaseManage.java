package com.dimeng.p2p.account.user.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6112;
import com.dimeng.p2p.S61.entities.T6113;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S61.entities.T6162;
import com.dimeng.p2p.S61.entities.T6163;
import com.dimeng.p2p.S61.entities.T6164;
import com.dimeng.p2p.account.user.service.entity.Cwzk;
import com.dimeng.p2p.account.user.service.entity.QyContactInfo;

/**
 * 企业基本信息管理
 * 
 * @author Administrator
 * 
 */
public abstract interface QyBaseManage extends Service
{
    
    /**
     * 企业联系信息
     * @return
     * @throws Throwable
     */
    public abstract T6164 getQylxxx()
        throws Throwable;
    
    public abstract QyContactInfo getQyContactInfo()
        throws Throwable;
    
    /**
     * 企业基本信息
     * @return
     * @throws Throwable
     */
    public abstract T6161 getQyjbxx()
        throws Throwable;
    
    /**
     * 企业财务状况
     * @return
     * @throws Throwable
     */
    public abstract T6163[] getQycwzk()
        throws Throwable;
    
    /**
     *  企业介绍
     * @return
     * @throws Throwable
     */
    public abstract T6162 getQyjs()
        throws Throwable;
    
    /**
     * 企业车产信息
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<T6113> getQyccxx(Paging paging)
        throws Throwable;
    
    /**
     * 企业房产信息
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<T6112> getQyfcxx(Paging paging)
        throws Throwable;
    
    /** 
     * 修改企业基础信息
     * @param entity
     * @return
     * @throws Throwable
     */
    public abstract void updateQyBases(T6161 entity)
        throws Throwable;
    
    /**
     * 修改企业介绍资料
     * @param entity
     * @throws Throwable
     */
    public abstract void updateJscl(T6162 entity)
        throws Throwable;
    
    /**
     * 修改企业联系信息
     * @param entity
     * @throws Throwable
     */
    public abstract void updateLxxx(T6164 entity)
        throws Throwable;
    
    /**
     * 更新财务状况
     * @param entitys
     * @throws Throwable
     */
    public abstract void updateCwzk(Cwzk cwzk)
        throws Throwable;
}
