package com.dimeng.p2p.modules.statistics.console.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class Profile implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 成交总金额
     */
    public BigDecimal totalAmount = BigDecimal.ZERO;
    
    /**
     * 成交额增粘率
     */
    public BigDecimal amountRate = BigDecimal.ZERO;
    
    /**
     * 成交笔数
     */
    public BigDecimal totalCount = BigDecimal.ZERO;
    
    /**
     * 成交笔数增长率
     */
    public BigDecimal countRate = BigDecimal.ZERO;
}
