package com.dimeng.p2p.modules.statistics.console.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 充值提现统计报表
 */
public class RecWitReportStatistics implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 充值总金额
     */
    public BigDecimal rechargeSum = new BigDecimal(0);
    
    /**
     * 提现总金额
     */
    public BigDecimal withdrawSum = new BigDecimal(0);
    
    /**
     * 线上充值总金额
     */
    public BigDecimal onLineRecharge = new BigDecimal(0);
    
    /**
     * 线下充值总金额
     */
    public BigDecimal offLineRecharge = new BigDecimal(0);
}
