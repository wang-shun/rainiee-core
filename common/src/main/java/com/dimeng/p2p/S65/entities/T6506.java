package com.dimeng.p2p.S65.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.math.BigDecimal;

/** 
 * 还款订单
 */
public class T6506 extends AbstractEntity{

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
     * 债权ID,参考T6251.F01
     */
    public int F04;

    /** 
     * 还款期号
     */
    public int F05;

    /** 
     * 还款金额
     */
    public BigDecimal F06 = BigDecimal.ZERO;

    /** 
     * 科目类型ID,参考T5122.F01
     */
    public int F07;
    
    /**
     * 还款记录ID,参考T6252.F01
     */
    public int F08;

}
