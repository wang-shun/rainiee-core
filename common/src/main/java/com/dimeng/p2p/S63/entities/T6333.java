package com.dimeng.p2p.S63.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S63.enums.T6333_F13;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/** 
 * 活动信息表
 */
public class T6333 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增主键ID
     */
    public int F01;

    /** 
     * 活动开始时间
     */
    public Timestamp F02;

    /** 
     * 活动结束时间
     */
    public Timestamp F03;

    /** 
     * 券生效日期
     */
    public Date F04;

    /** 
     * 券失效日期
     */
    public Date F05;

    /** 
     * 券发放上限
     */
    public int F06;

    /** 
     * 券面值，单位：元
     */
    public BigDecimal F07 = BigDecimal.ZERO;

    /** 
     * 活动说明
     */
    public String F08;

    /** 
     * 券库存数
     */
    public int F09;

    /** 
     * 券剩余库存数
     */
    public int F10;

    /** 
     * 录入人ID，参考T7110.F01
     */
    public int F11;

    /** 
     * 录入时间
     */
    public Timestamp F12;

    /** 
     * 状态,QY:启用;TY:停用
     */
    public T6333_F13 F13;
    
    /**
     * 活动名称
     */
    public String F14;

}
