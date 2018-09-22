package com.dimeng.p2p.modules.bid.user.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.modules.bid.user.service.entity.Dzxy;
import com.dimeng.p2p.modules.bid.user.service.entity.DzxyDf;
import com.dimeng.p2p.modules.bid.user.service.entity.DzxyDy;
import com.dimeng.p2p.modules.bid.user.service.entity.DzxyZqzr;

/**
 * 
 *电子协议
 */
public interface DzxyManage extends Service
{
    /**
     * 获取标的电子协议
     * @return
     * @throws Throwable
     */
    public abstract Dzxy getBidContent(int loanId)
        throws Throwable;
    
    /**
     * 获取线上债权转让的电子协议
     * @return
     * @throws Throwable
     */
    public abstract Dzxy getZqzr(int zqsqId)
        throws Throwable;
    
    /**
     * 获取抵押担保标的信息
     * @param loandId
     * @return
     * @throws Throwable
     */
    public abstract DzxyDy getDzxyDy(int loanId)
        throws Throwable;
    
    /**
     * 获取信用标的信息
     * @param loandId
     * @return
     * @throws Throwable
     */
    public abstract DzxyDy getDzxyXy(int loanId)
        throws Throwable;
    
    /**
     * 获取线上债权转让申请的信息
     * @param loandId
     * @return
     * @throws Throwable
     */
    public abstract DzxyZqzr getDzxyZqzr(int zqzrjlId)
        throws Throwable;
    
    /**
     * 获取企业信息
     * @param id 企业账号id
     * @return
     * @throws Throwable
     */
    public abstract T6161 getT6161(int id)
        throws Throwable;
    
    /**
     * 获取垫付的电子协议
     * <功能详细描述>
     * @param dfId
     * @return
     * @throws Throwable
     */
    public abstract Dzxy getDf(int bId)
        throws Throwable;
    
    /**
     * 获取垫付的信息
     * @param loandId
     * @return
     * @throws Throwable
     */
    public abstract DzxyDf getDzxyDf(int bId)
        throws Throwable;
    
}
