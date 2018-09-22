package com.dimeng.p2p.S61.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6103_F06;
import com.dimeng.p2p.S61.enums.T6103_F08;

/** 
 * 用户体验金
 */
public class T6103 extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /** 
     * 体验金ID
     */
    public int F01;
    
    /** 
     * 用户ID
     */
    public int F02;
    
    /** 
     * 体验金金额
     */
    public BigDecimal F03 = BigDecimal.ZERO;
    
    /** 
     * 生效时间
     */
    public Timestamp F04;
    
    /** 
     * 失效时间
     */
    public Timestamp F05;
    
    /** 
     * 使用状态：已过期：''YGQ'',未使用：''WSY'',已委托：''YWT'',已投资：''YTZ'',已结清：''YJQ''',
     */
    public T6103_F06 F06;
    
    /** 
     * 有效收益期
     */
    public int F07;
    
    /** 
     * 体验金来源
     */
    public T6103_F08 F08;
    
    /** 
     * 备注
     */
    public String F09;
    
    /** 
     * 最后更新时间
     */
    public Timestamp F10;
    
    /**
     * 利息开始时间
     */
    public Timestamp F11;
    
    /**
     * 操作人ID，参考T7110.F01
     */
    public int F12;
    
    /**
     * 标ID，参考T6230.F01
     */
    public int F13;

    /**
     * 活动id：参考T6340.F01
     */
    public int F14;

    /**
     * 使用时间
     */
    public Timestamp F15;

    /**
     * 体验金投资收益计算方式(true:按月;false:按天)
     */
    public String F16;
}
