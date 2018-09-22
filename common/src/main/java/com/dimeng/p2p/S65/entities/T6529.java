package com.dimeng.p2p.S65.entities;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S65.enums.T6529_F08;

/** 
 * 不良债权转让订单
 */
public class T6529 extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /** 
     * 订单ID,参考T6501.F01
     */
    public int F01;
    
    /** 
     * 标ID,参考T6230.F01
     */
    public int F02;
    
    /** 
     * 不良债权申请ID,参考T6264.F01
     */
    public int F03;
    
    /** 
     * 购买人ID,参考T6110.F01
     */
    public int F04;
    
    /**
     * 债权ID,参考T6251.F01
     */
    public int F05;
    
    /** 
     * 债权金额
     */
    public BigDecimal F06 = BigDecimal.ZERO;
    
    /** 
     * 交易类型ID,参考T5122.F01
     */
    public int F07;
    
    /** 
     * 是否完成,S:是;F:否;
     */
    public T6529_F08 F08;
    
    /**
     * 转让期数
     */
    public int F09;
    
}
