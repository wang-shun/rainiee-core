package com.dimeng.p2p.S65.entities
;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/** 
 * 优选理财放款订单
 */
public class T6511 extends AbstractEntity{

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
     * 优选理财ID,参考T6410.F01
     */
    public int F03;

    /** 
     * 投资金额
     */
    public BigDecimal F04 = BigDecimal.ZERO;

}
