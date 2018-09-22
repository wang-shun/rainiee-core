package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;

/**
 * 借款统计实体类
 *
 */
public class NewCreditList
{
    /**
     * 年
     */
    public int year;
    
    /**
     * 月
     */
    public int month;
    
    /**
     * 借款金额
     */
    public BigDecimal loanMoney = BigDecimal.ZERO;
    
    /**
     * 借款利息
     */
    public BigDecimal loanInterest = BigDecimal.ZERO;
    
    /**
     * 借款管理费
     */
    public BigDecimal manageMoney = BigDecimal.ZERO;
    
    /**
     * 已还本金
     */
    public BigDecimal payMoney = BigDecimal.ZERO;
    
    /**
     * 已还利息
     */
    public BigDecimal payInterest = BigDecimal.ZERO;
    
    /**
     * 已交逾期罚息
     */
    public BigDecimal payDefaultIns = BigDecimal.ZERO;
    
    /**
     * 违约金
     */
    public BigDecimal deditMoney = BigDecimal.ZERO;
    
    /**
     * 待还本金
     */
    public BigDecimal notPayMoney = BigDecimal.ZERO;
    
    /**
     * 待还利息
     */
    public BigDecimal notPayInterest = BigDecimal.ZERO;
    
    /**
     * 待还逾期罚息
     */
    public BigDecimal notPayDefaultIns = BigDecimal.ZERO;
    
    /**
     * 已还金额
     */
    public BigDecimal payMoneyTotal = BigDecimal.ZERO;
    
    /**
     * 待还金额
     */
    public BigDecimal notPayMoneyTotal = BigDecimal.ZERO;
    
    /**
     * 提前还款手续费
     */
    public BigDecimal prepaymentFee = BigDecimal.ZERO;
}
