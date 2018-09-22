package com.dimeng.p2p.S61.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6131_F07;
import java.math.BigDecimal;
import java.sql.Timestamp;

/** 
 * 用户在线充值订单
 */
public class T6131 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 用户ID,参考T6110.F01
     */
    public int F02;

    /** 
     * 充值金额
     */
    public BigDecimal F03 = BigDecimal.ZERO;

    /** 
     * 应收手续费
     */
    public BigDecimal F04 = BigDecimal.ZERO;

    /** 
     * 实收手续费
     */
    public BigDecimal F05 = BigDecimal.ZERO;

    /** 
     * 创建时间
     */
    public Timestamp F06;

    /** 
     * 状态,WRZ:未入账;YRZ:已入账;
     */
    public T6131_F07 F07;

    /** 
     * 支付公司
     */
    public String F08;

    /** 
     * 水单号
     */
    public String F09;

    /** 
     * 入账时间
     */
    public int F10;

}
