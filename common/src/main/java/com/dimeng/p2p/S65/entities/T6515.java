package com.dimeng.p2p.S65.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.math.BigDecimal;

/** 
 * 冻结订单
 */
public class T6515 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 订单id
     */
    public int F01;

    /** 
     * 关联订单id
     */
    public int F02;

    /** 
     * 订单金额
     */
    public BigDecimal F03 = BigDecimal.ZERO;

}
