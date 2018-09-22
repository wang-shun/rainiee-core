package com.dimeng.p2p.S62.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6254_F07;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/** 
 * 标总还款记录
 */
public class T6254 extends AbstractEntity{

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
     * 交易类型ID,参考T5122.F01
     */
    public int F03;

    /** 
     * 期号
     */
    public int F04;

    /** 
     * 金额
     */
    public BigDecimal F05 = BigDecimal.ZERO;

    /** 
     * 应还日期
     */
    public Date F06;

    /** 
     * 状态,WH:未还;YH:已还;HKZ:还款中;
     */
    public T6254_F07 F07;

    /** 
     * 实际还款时间
     */
    public Timestamp F08;
    
    /** 
     * 生成日期
     */
    public Date F09;

}
