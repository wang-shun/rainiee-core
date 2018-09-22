package com.dimeng.p2p.S63.entities
;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S63.enums.T6338_F05;

/** 
 * 友金币发放记录表
 */
public class T6338 extends AbstractEntity{

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
     * 活动ID,参考T6337.F01
     */
    public int F03;

    /** 
     * 友金币金额
     */
    public BigDecimal F04 = BigDecimal.ZERO;

    /** 
     * 使用状态,YSY:已使用;WSY:未使用
     */
    public T6338_F05 F05;

    /** 
     * 友金币生效日期
     */
    public Timestamp F06;

    /** 
     * 友金币失效日期
     */
    public Timestamp F07;

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

    /** 
     * 友金币券号
     */
    public String F11;
    
    /**
     * 投资记录Id,参考S62.T6250.F01
     */
    public int F12;

}
