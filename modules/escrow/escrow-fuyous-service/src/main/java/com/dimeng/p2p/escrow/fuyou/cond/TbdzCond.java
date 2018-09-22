package com.dimeng.p2p.escrow.fuyou.cond;

import java.sql.Timestamp;

public interface TbdzCond
{
    /**
    * 订单号
    */
    public String f01();
    
    /**
    * 流水号
    */
    public String f10();

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
     * 平台用户名
     */
    public String userName();
    
    /**
     * 交易类型
     * <功能详细描述>
     * @return
     */
    public String tradingType();
    
    /**
     * 订单状态
     */
    public String state();
}
