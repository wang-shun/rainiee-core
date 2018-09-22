package com.dimeng.p2p.S65.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.math.BigDecimal;

/** 
 * 优选理财还款订单
 */
public class T6512 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 订单ID,参考T6501.F01
     */
    public int F01;

    /** 
     * 投资用户ID,参考T6501.F01
     */
    public int F02;

    /** 
     * 优选理财ID,参考T6410.F01
     */
    public int F03;

    /** 
     * 还款期号
     */
    public int F04;

    /** 
     * 还款金额
     */
    public BigDecimal F05 = BigDecimal.ZERO;

    /** 
     * 科目类型ID,参考T5122.F01
     */
    public int F06;

}
