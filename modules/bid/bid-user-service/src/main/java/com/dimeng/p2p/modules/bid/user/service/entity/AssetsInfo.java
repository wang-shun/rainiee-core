package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;

/**
 * 债权信息实体类
 * 
 */
public class AssetsInfo
{
    /**
     * 债权已赚金额
     */
    public BigDecimal makeMoney = BigDecimal.ZERO;
    
    /**
     * 利息收益
     */
    public BigDecimal accMakeMoney = BigDecimal.ZERO;
    
    /**
     * 线上债权转让盈亏
     */
    public BigDecimal sellMakeMoney = BigDecimal.ZERO;
    
    /**
     * 债权账户资产
     */
    public BigDecimal money = BigDecimal.ZERO;
    
    /**
     * 回收中的债权数量
     */
    public int assetsNum;
}
