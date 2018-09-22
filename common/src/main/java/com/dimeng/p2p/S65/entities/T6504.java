package com.dimeng.p2p.S65.entities;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S65.enums.T6504_F06;
import com.dimeng.p2p.S65.enums.T6504_F07;

/** 
 * 投资订单
 */
public class T6504 extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /** 
     * 订单ID
     */
    public int F01;
    
    /** 
     * 投资用户ID,参考T6110.F01
     */
    public int F02;
    
    /** 
     * 标ID,参考T6230.F01
     */
    public int F03;
    
    /** 
     * 投资金额
     */
    public BigDecimal F04 = BigDecimal.ZERO;
    
    /** 
     * 投资记录ID,参考T6250.F01,投资成功时记录
     */
    public int F05;
    
    /**
     * 是否自动投资订单,F:否;S:是;
     */
    public T6504_F06 F06;
    
    /**
     * 来源，PC:PC;APP:APP;WEIXIN:微信
     */
    public T6504_F07 F07;
    
}
