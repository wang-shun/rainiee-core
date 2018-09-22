package com.dimeng.p2p.S65.entities
;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S65.enums.T6505_F06;

/** 
 * 放款订单
 */
public class T6505 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 订单ID,参考T6501.F01
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
     * 投资记录ID,参考T6250.F01
     */
    public int F04;

    /** 
     * 投资金额
     */
    public BigDecimal F05 = BigDecimal.ZERO;
    
    /**
     * 是否是友金币订单
     */
    public T6505_F06 F06;

}
