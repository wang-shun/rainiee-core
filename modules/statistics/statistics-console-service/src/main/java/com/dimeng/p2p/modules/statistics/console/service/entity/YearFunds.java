package com.dimeng.p2p.modules.statistics.console.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class YearFunds implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 充值手续费
     */
    public BigDecimal czsxf = BigDecimal.ZERO;
    
    /**
     * 提现手续费
     */
    public BigDecimal txsxf = BigDecimal.ZERO;
    
    /**
     * 充值成本
     */
    public BigDecimal czcb = BigDecimal.ZERO;
    
    /**
     * 提现成本
     */
    public BigDecimal txcb = BigDecimal.ZERO;
    
    /**
     * 身份验证手续费
     */
    public BigDecimal sfyzsxf = BigDecimal.ZERO;
    
    /**
     * 借款管理费
     */
    public BigDecimal jkglf = BigDecimal.ZERO;
    
    /**
     * 逾期管理费
     */
    public BigDecimal yqglf = BigDecimal.ZERO;
    
    /**
     * 债权转让费
     */
    public BigDecimal zqzrf = BigDecimal.ZERO;
    
    /**
     * 活动费用
     */
    public BigDecimal hdfy = BigDecimal.ZERO;
    
    /**
     * 理财管理费
     */
    public BigDecimal lcglf = BigDecimal.ZERO;
    
    /**
     * 持续推广费用
     */
    public BigDecimal cxtgfy = BigDecimal.ZERO;
    
    /**
     * 有效推广费用
     */
    public BigDecimal yxtgfy = BigDecimal.ZERO;
    
    /**
     * 违约金手续费
     */
    public BigDecimal wyjsxf = BigDecimal.ZERO;
    
    /**
     * 本金垫付返还
     */
    public BigDecimal bjdffh = BigDecimal.ZERO;
    
    /**
     * 利息垫付返还
     */
    public BigDecimal lxdffh = BigDecimal.ZERO;
    
    /**
     * 罚息垫付返还
     */
    public BigDecimal fxdffh = BigDecimal.ZERO;
    
    /**
     * 本金垫付支出
     */
    public BigDecimal bjdfzc = BigDecimal.ZERO;
    
    /**
     * 利息垫付支出
     */
    public BigDecimal lxdfzc = BigDecimal.ZERO;
    
    /**
     * 罚息垫付支出
     */
    public BigDecimal fxdfzc = BigDecimal.ZERO;
    
    /**
     * 加息奖励费用
     */
    public BigDecimal jxjlfy = BigDecimal.ZERO;
    
    /**
     * 体验金投资费用
     */
    public BigDecimal tyjtzfy = BigDecimal.ZERO;
    
    /**
     * 红包奖励费用
     */
    public BigDecimal hbjlfy = BigDecimal.ZERO;
    
    /**
     * 奖励标奖励费用
     */
    public BigDecimal jlbjlfy = BigDecimal.ZERO;
    
    /**
     * 线下充值
     */
    public BigDecimal xxcz = BigDecimal.ZERO;
    
}
