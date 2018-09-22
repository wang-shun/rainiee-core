package com.dimeng.p2p.S62.entities
;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/** 
 * 垫付记录
 */
public class T6253 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 标ID,参考T6230.F01
     */
    public int F02;

    /** 
     * 垫付人,参考T6110.F01
     */
    public int F03;

    /** 
     * 被垫付人,参考T6110.F01
     */
    public int F04;

    /** 
     * 垫付金额
     */
    public BigDecimal F05 = BigDecimal.ZERO;

    /** 
     * 垫付返回金额
     */
    public BigDecimal F06 = BigDecimal.ZERO;

    /** 
     * 垫付时间
     */
    public Timestamp F07;
    
    /**
     * 垫付开始期数
     */
    public int F08;

    /**
     * 垫付人
     */
    public String F09;

    /**
     * 垫付方式
     */
    public String F10;

    /**
     * 从哪一期开始垫付de
     */
    public int F11;
}
