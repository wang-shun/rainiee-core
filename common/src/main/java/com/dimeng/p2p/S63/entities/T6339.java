package com.dimeng.p2p.S63.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S63.enums.T6339_F04;
import java.math.BigDecimal;
import java.sql.Timestamp;

/** 
 * 友金币兑换表
 */
public class T6339 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 兑换号
     */
    public String F01;

    /** 
     * 兑换用户ID,参考T6110.F01
     */
    public int F02;

    /** 
     * 友金币金额
     */
    public BigDecimal F03 = BigDecimal.ZERO;

    /** 
     * 兑换状态,YDH:已兑换;WDH:未兑换
     */
    public T6339_F04 F04;

    /** 
     * 实际兑换时间
     */
    public Timestamp F05;

    /** 
     * 兑换截止时间
     */
    public Timestamp F06;

}
