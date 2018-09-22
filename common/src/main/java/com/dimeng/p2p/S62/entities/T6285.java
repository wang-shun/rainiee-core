package com.dimeng.p2p.S62.entities
;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6285_F09;

/** 
 * 体验金返还记录
 */
public class T6285 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 标ID,参考T6230.F01
     */
    public int F02;

    /** 
     * 付款平台ID,参考T6110.F01
     */
    public int F03;

    /** 
     * 收款用户ID,参考T6110.F01
     */
    public int F04;

    /** 
     * 交易类型ID,参考T5122.F01
     */
    public int F05;

    /** 
     * 期号
     */
    public int F06;

    /** 
     * 体验金返还利息
     */
    public BigDecimal F07 = BigDecimal.ZERO;

    /** 
     * 返还日期
     */
    public Date F08;
    
    /** 
     * 状态,WFH:未返还;YFH:已返还
     */
    public T6285_F09 F09;

    /** 
     * 实际返还时间
     */
    public Timestamp F10;

    /** 
     * 剩余体验金返还利息
     */
    public BigDecimal F11 = BigDecimal.ZERO;

    /**
     * 体验金ID
     */
    public int F12;
}
