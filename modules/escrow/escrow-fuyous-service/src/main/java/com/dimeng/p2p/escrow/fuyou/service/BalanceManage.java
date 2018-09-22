package com.dimeng.p2p.escrow.fuyou.service;

import java.util.ArrayList;
import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.escrow.fuyou.cond.BalanceCond;
import com.dimeng.p2p.escrow.fuyou.entity.console.BalanceEntity;

public interface BalanceManage extends Service
{
    /**
     * 余额查询参数
     * @param cond
     * @return 
     * @throws Throwable
     */
    public abstract Map<String, String> createBalance(BalanceCond cond)
        throws Throwable;
    
    /**
     * 余额查询
     * 
     * @param xmlMap
     * @param plain
     * @return
     * @throws Throwable
     */
    public abstract ArrayList<BalanceEntity> balanceDecoder(Map<String, Object> xmlMap, String plain)
        throws Throwable;
}
