package com.dimeng.p2p.modules.financing.user.service.entity;

import java.math.BigDecimal;

/**
 * 平台总计投资
 * 
 */
public class FinancingTotal
{
    /**
     * 总计
     */
    public BigDecimal total = BigDecimal.ZERO;
    
    /**
     * 优选理财计划
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
