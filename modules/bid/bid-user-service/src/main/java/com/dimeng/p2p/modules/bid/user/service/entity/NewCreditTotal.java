package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;

/**
 * 借款统计实体类
 *
 */
public class NewCreditTotal
{
    /**
     * 累计借款金额
     */
    public BigDecimal loanTotal = new BigDecimal(0);
    
    /**
     * 累计借款利息
     */
    public BigDecimal interestTotal = new BigDecimal(0);
    
    /**
     * 累计管理费
     */
    public BigDecimal manageTotal = new BigDecimal(0);
    
    /**
     * 已还总额
     */
    public BigDecimal payTotal = new BigDecimal(0);
    
    /**
     * 待还总额
     */
    public BigDecimal notPayTotal = new BigDecimal(0);
}
