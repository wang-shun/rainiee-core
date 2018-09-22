package com.dimeng.p2p.modules.bid.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S62.entities.T6256;

/**
 * 垫付管理
 */
public abstract interface AdvanceManage extends Service
{
    
    /**
     * 添加垫付记录协议版本号
     * @param t6256
     * @throws Throwable
     */
    void insertT6256(T6256 t6256)
        throws Throwable;
    
    /**
     * 添加垫付记录协议版本号
     * @param int 垫付记录ID,参考T6253.F01
     * @throws Throwable
     */
    void insertT6256(int dfRecordId)
        throws Throwable;
}
