package com.dimeng.p2p.S65.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.math.BigDecimal;

/** 
 * 保证金充值订单
 */
public class T6513 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 订单ID
     */
    public int F01;

    /** 
     * 用户ID
     */
    public int F02;

    /** 
     * 金额
     */
    public BigDecimal F03 = BigDecimal.ZERO;

}
