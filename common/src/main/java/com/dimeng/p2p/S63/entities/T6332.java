package com.dimeng.p2p.S63.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.math.BigDecimal;
import java.sql.Timestamp;

/** 
 * 购买标的信息记录表
 */
public class T6332 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 主键ID
     */
    public int F01;

    /** 
     * 购买用户ID,参考T6110.F01
     */
    public int F02;

    /** 
     * 标的ID,参考T6230.F01
     */
    public int F03;

    /** 
     * 购买时间
     */
    public Timestamp F04;

    /** 
     * 购买服务费
     */
    public BigDecimal F05 = BigDecimal.ZERO;

}
