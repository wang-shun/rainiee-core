package com.dimeng.p2p.account.user.service.entity;

import com.dimeng.p2p.S62.enums.T6231_F21;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 体验金计划
 */
public class MyExperience implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 计划ID,参考T6230.F01
     */
    public int F01;
    
    /** 
     * 计划名称
     */
    public String F02;
    
    /**
     * 体验金加入金额
     */
    public BigDecimal F03 = new BigDecimal(0);
    
    /**
     * 年化利率
     */
    public BigDecimal F04 = new BigDecimal(0);
    
    /**
     * 已赚金额
     */
    public BigDecimal F05 = new BigDecimal(0);
    
    /**
     * 截止时间
     */
    public Timestamp F06;
    
    /**
     * 借款期限
     */
    public String F07;
    
    /**
     * 待赚金额
     */
    public BigDecimal F08 = new BigDecimal(0);
    
    /**
     * 下个还款日
     */
    public Timestamp F09;
    
    /**
     * 体验金有收益月份数
     */
    public int F10;

    /**
     * 是否按天借款
     */
    public T6231_F21 F11;

    /**
     * 借款天数
     */
    public int F12;

    /**
     * 借款期数
     */
    public int F13;
    
    /**
     * 结清日期
     */
    public Timestamp F14;

    /**
     * 体验金投资收益计算方式(true:按月;false:按天)
     */
    public String F15;
    
}
