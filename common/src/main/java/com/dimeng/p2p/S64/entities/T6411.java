package com.dimeng.p2p.S64.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.math.BigDecimal;
import java.sql.Timestamp;

/** 
 * 优选理财持有人表
 */
public class T6411 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 优选理财持有人表自增ID
     */
    public int F01;

    /** 
     * 优选理财计划表ID,参考T6410.F01
     */
    public int F02;

    /** 
     * 持有人ID,参考T6110.F01
     */
    public int F03;

    /** 
     * 原始债权
     */
    public BigDecimal F04 = BigDecimal.ZERO;

    /** 
     * 剩余债权
     */
    public BigDecimal F05 = BigDecimal.ZERO;

    /** 
     * 加入时间
     */
    public Timestamp F06;

    /** 
     * 最后更新时间
     */
    public Timestamp F07;

}
