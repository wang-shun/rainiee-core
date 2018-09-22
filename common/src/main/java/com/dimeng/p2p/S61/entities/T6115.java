package com.dimeng.p2p.S61.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.math.BigDecimal;
import java.sql.Timestamp;

/** 
 * 用户理财统计
 */
public class T6115 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 用户ID,参考T6110.F01
     */
    public int F01;

    /** 
     * 借款总额,本金
     */
    public BigDecimal F02 = BigDecimal.ZERO;

    /** 
     * 投资总额,本金
     */
    public BigDecimal F03 = BigDecimal.ZERO;

    /** 
     * 最后更新时间
     */
    public Timestamp F04;

}
