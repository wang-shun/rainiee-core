package com.dimeng.p2p.modules.statistics.console.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 充值提现统计报表
 */
public class RecWitReport implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 充值/提现的日期
     */
    public Date date;
    
    /**
     * 类型
     */
    public String type;
    
    /**
     * 金额
     */
    public BigDecimal amount = new BigDecimal(0);
    
    /**
     * 笔数
     */
    public int typeCount;
    
    /**
     * 用户数
     */
    public int userCount;
    
    /**
     * 线上
     */
    public BigDecimal onLineRecharge = new BigDecimal(0);
    
    /**
     * 线下
     */
    public BigDecimal offLineRecharge = new BigDecimal(0);
}
