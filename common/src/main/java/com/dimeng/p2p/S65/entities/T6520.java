package com.dimeng.p2p.S65.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.math.BigDecimal;

/** 
 * 体验金利息返还
 */
public class T6520 extends AbstractEntity{

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
     * 标ID,参考T6230.F01
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
    
    /** 
     * 返还利息平台ID
     */
    public int F07;

    /**
     * 体验金返还ID,T6285.F01
     */
    public int F08;

}
