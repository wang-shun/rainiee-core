package com.dimeng.p2p.modules.bid.console.service;

import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.modules.bid.console.service.entity.Dzxy;
import com.dimeng.p2p.modules.bid.console.service.entity.DzxyDf;
import com.dimeng.p2p.modules.bid.console.service.entity.DzxyDy;
import com.dimeng.p2p.modules.bid.console.service.entity.DzxyZqzr;

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
    public abstract DzxyDy getDzxyDy(int loanId, int tzUserId)
        throws Throwable;
    
    /**
     * 获取信用标的信息
     * @param loandId
     * @return
     * @throws Throwable
     */
    public abstract DzxyDy getDzxyXy(int loanId, int tzUserId)
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
     * 获取垫付的电子协议
     * <功能详细描述>
     * @param bid 标的Id
     * @param dfuid 垫付人账号Id
     * @return
     * @throws Throwable
     */
    public abstract Dzxy getDf(int bId, int dfuid)
        throws Throwable;
    
    /**
     * 获取垫付的信息
     * @param loandId
     * @return
     * @throws Throwable
     */
    public abstract DzxyDf getDzxyDf(int bId)
        throws Throwable;
    
    /**
     * 根据标的ID和用户ID获取合同中的参数
     * @param loanId
     * @param userId
     * @return
     * @throws Throwable
     */
    public abstract Map<String, Object> getValueMap(int loanId, int userId)
        throws Throwable;
}
