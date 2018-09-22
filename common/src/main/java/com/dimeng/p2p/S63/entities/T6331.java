package com.dimeng.p2p.S63.entities
;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S63.enums.T6331_F06;

/** 
 * 业务员推荐统计表
 */
public class T6331 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 主键ID
     */
    public int F01;

    /** 
     * 业务员ID,参考T6330.F01
     */
    public int F02;

    /** 
     * 推荐人ID,参考T6110.F01
     */
    public int F03;

    /** 
     * 推荐时间
     */
    public Timestamp F04;

    /** 
     * 金额
     */
    public BigDecimal F05 = BigDecimal.ZERO;

    /** 
     * 交易类型('ZQZR','YXLC','FX','TB',债权转让，优选理财，返现，投资)
     */
    public T6331_F06 F06;

    /** 
     * 当前余额
     */
    public BigDecimal F07 = BigDecimal.ZERO;

}
