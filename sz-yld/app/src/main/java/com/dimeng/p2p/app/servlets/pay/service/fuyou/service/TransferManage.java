/*package com.dimeng.p2p.app.servlets.pay.service.fuyou.service;

import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.escrow.fuyou.cond.TransferCond;

*//**
 * 
 * 转账或划拨
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月25日]
 *//*
public abstract interface TransferManage extends Service
{
    
    *//**
     * 转账或划拨
     * 
     * @param transferAccountsCond
     * @return
     * @throws Throwable
     *//*
    public abstract Map<String, String> createTransferMap(TransferCond cond)
        throws Throwable;
    
    *//**
     * 验签
     * @param cond
     * @return 
     * @throws Throwable
     *//*
     public abstract boolean verifyByRSA(String plain, String chkValue)
         throws Throwable;
}
*/