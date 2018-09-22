package com.dimeng.p2p.escrow.fuyou.cond;

import java.sql.Timestamp;

public interface TxdzCond
{
    /**
     * 订单号
     */
    public String f01();
    
    /**
     * 创建时间， 大于等于查询
     * 
     * @return {@link Timestamp}空值无效
     */
    public abstract Timestamp getStartExpireDatetime();
    
    /**
     * 创建时间， 小于等于查询
     * 
     * @return {@link Timestamp}空值无效
     */
    public abstract Timestamp getEndExpireDatetime();
    
    /**
     * 订单流水号
     */
    public String f10();
    
    /**
     * 平台用户名
     */
    public String userName();
    
    public String state();
}
