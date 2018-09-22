package com.dimeng.p2p.S65.entities
;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/** 
 * 体验金放款订单
 */
public class T6519 extends AbstractEntity{

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

}
