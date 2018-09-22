package com.dimeng.p2p.account.user.service;

import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.account.user.service.entity.Dbxxmx;
import com.dimeng.p2p.account.user.service.entity.Dfxxmx;

/**
 * 风险备用金
 */
public abstract interface FxbyjManage extends Service
{
    
    /**
     * 担保信息明细
     * 
     * @return
     */
    public abstract PagingResult<Dbxxmx> searchDb(Paging paging)
        throws Throwable;
    
    /**
     * 垫付信息明细
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<Dfxxmx> searchDf(Paging paging)
        throws Throwable;
    
    /**
     * 获取风险保证金账户信息
     * 
     * @return
     * @throws Throwable
     */
    public abstract T6101 getT6101()
        throws Throwable;
    
    /**
     * 备用金充值
     */
    public abstract int recharge(BigDecimal amount, int type)
        throws Throwable;
    
}
