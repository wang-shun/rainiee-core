package com.dimeng.p2p.escrow.fuyou.service;

import java.util.HashMap;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.escrow.fuyou.entity.TransactionQueryRequestEntity;
import com.dimeng.p2p.escrow.fuyou.entity.TransactionQueryResponseEntity;

/**
 * 
 * 交易查询接
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月27日]
 */
public abstract interface TransactionQueryManage extends Service
{
    
    /**
     * 交易查询接口-签名数据
     * 
     * @param requestEntity
     *            交易查询接口请求实体类
     * @return TransactionQueryRequestEntity 返回签名结果后的 交易查询接口请求实体类
     * @throws Throwable
     */
    public abstract TransactionQueryRequestEntity queryTransactionQuery(TransactionQueryRequestEntity requestEntity)
        throws Throwable;
    
    /**
     * 交易查询解析加载实体类
     * 
     * @param xmlMap
     * @param plain
     * @return
     * @throws Throwable
     */
    public abstract TransactionQueryResponseEntity queryDecoder(HashMap<String, Object> xmlMap, String plain)
        throws Throwable;
}
