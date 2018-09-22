package com.dimeng.p2p.modules.bid.user.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 债权待收收益
 */
public class UnpayEarnings implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 总计
     */
    public BigDecimal total = BigDecimal.ZERO;
    
    /**
     * 信用认证标
     */
    public BigDecimal credit = BigDecimal.ZERO;
    
    /**
     * 担保标
     */
    public BigDecimal warrant = BigDecimal.ZERO;
    
    /**
     * 实地认证标
     */
    public BigDecimal certification = BigDecimal.ZERO;
}
