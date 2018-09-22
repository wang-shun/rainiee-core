package com.dimeng.p2p.modules.base.front.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S51.entities.T5122;
import com.dimeng.p2p.S51.entities.T5132;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;

/**
 * 交易类型管理
 * 
 */
public abstract interface TradeTypeManage extends Service
{
    /**
     * 查询列表
     * 
     * @param userType
     * @return
     * @throws Throwable
     */
    public abstract T5122[] search(T6110_F06 userType, T6110_F10 t6110_F10)
        throws Throwable;
    
    /**
     * 根据ID查询名称
     * 
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract String get(int id)
        throws Throwable;
    
    /**
     * 查询订单类型列表
     * 
     * @param userType
     * @return
     * @throws Throwable
     */
    public abstract T5132[] searchT5132(T6110_F06 userType, T6110_F10 t6110_F10)
        throws Throwable;
    
}
