package com.dimeng.p2p.S65.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.math.BigDecimal;

/** 
 * 债权转让订单
 */
public class T6507 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 订单ID,参考T6501.F01
     */
    public int F01;

    /** 
     * 债权申请ID,参考T6260.F01
     */
    public int F02;

    /** 
     * 购买人ID,参考T6110.F01
     */
    public int F03;

    /** 
     * 购买债权
     */
    public BigDecimal F04 = BigDecimal.ZERO;

    /** 
     * 受让价格
     */
    public BigDecimal F05 = BigDecimal.ZERO;

    /** 
     * 转让手续费
     */
    public BigDecimal F06 = BigDecimal.ZERO;

}
