package com.dimeng.p2p.modules.financing.user.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 理财账户资产
 */
public class AccountBalance implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 总计
     */
    public BigDecimal total = BigDecimal.ZERO;
    
    /**
     * 优选理财收益
     */
    public BigDecimal plan = BigDecimal.ZERO;
    
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
