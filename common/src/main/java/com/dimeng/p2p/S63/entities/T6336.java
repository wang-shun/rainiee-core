package com.dimeng.p2p.S63.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S63.enums.T6336_F05;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/** 
 * 券发放记录表
 */
public class T6336 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增主键ID
     */
    public int F01;

    /** 
     * 用户ID,参考T6110.F01
     */
    public int F02;

    /** 
     * 活动ID,参考T6333.F01
     */
    public int F03;

    /** 
     * 券面值
     */
    public BigDecimal F04 = BigDecimal.ZERO;

    /** 
     * 使用状态,YSY:已使用;WSY:未使用
     */
    public T6336_F05 F05;

    /** 
     * 券生效日期
     */
    public Date F06;

    /** 
     * 券失效日期
     */
    public Date F07;

    /** 
     * 奖励来源文字说明
     */
    public String F08;
    
    /**
     * 使用时间
     */
    public Timestamp F09;
    
    /**
     * 实际抵扣金额
     */
    public BigDecimal F10 = BigDecimal.ZERO;
}
