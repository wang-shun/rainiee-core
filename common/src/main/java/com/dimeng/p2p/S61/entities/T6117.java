package com.dimeng.p2p.S61.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.math.BigDecimal;
import java.sql.Timestamp;

/** 
 * 用户信用流水
 */
public class T6117 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 用户账号ID,参考T6110.F01
     */
    public int F02;

    /** 
     * 交易类型ID,参考T5122.F01
     */
    public int F03;

    /** 
     * 发生时间
     */
    public Timestamp F04;

    /** 
     * 收入
     */
    public BigDecimal F05 = BigDecimal.ZERO;

    /** 
     * 支出
     */
    public BigDecimal F06 = BigDecimal.ZERO;

    /** 
     * 余额
     */
    public BigDecimal F07 = BigDecimal.ZERO;

    /** 
     * 备注
     */
    public String F08;

}
