package com.dimeng.p2p.S61.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.math.BigDecimal;
import java.sql.Timestamp;

/** 
 * 我的借款还款按月份统计表
 */
public class T6192 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 用户账号ID,参考T6110.F01
     */
    public int F01;

    /** 
     * 年
     */
    public int F02;

    /** 
     * 月份
     */
    public short F03;

    /** 
     * 还款总金额
     */
    public BigDecimal F04 = BigDecimal.ZERO;

    /** 
     * 最后更新时间
     */
    public Timestamp F05;

}
