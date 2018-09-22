package com.dimeng.p2p.S64.entities;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S64.enums.T6410_F07;
import com.dimeng.p2p.S64.enums.T6410_F14;
import com.dimeng.p2p.S64.enums.T6410_F24;

/** 
 * 优选理财计划
 */
public class T6410 extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /** 
     * 自增ID
     */
    public int F01;
    
    /** 
     * 优选理财标题
     */
    public String F02;
    
    /** 
     * 总金额
     */
    public BigDecimal F03 = BigDecimal.ZERO;
    
    /** 
     * 可投余额
     */
    public BigDecimal F04 = BigDecimal.ZERO;
    
    /** 
     * 年化利率
     */
    public BigDecimal F05 = BigDecimal.ZERO;
    
    /** 
     * 投资类型ID,参考T6211.F01
     */
    public int F06;
    
    /** 
     * 计划状态,XJ:新建;YFB:已发布;YSX:已生效;YJZ:已截止
     */
    public T6410_F07 F07;
    
    /** 
     * 满额用时（秒计算）
     */
    public int F08;
    
    /** 
     * 申请开始时间
     */
    public Timestamp F09;
    
    /** 
     * 申请结束日期
     */
    public Date F10;
    
    /** 
     * 锁定期限(月)
     */
    public int F11;
    
    /** 
     * 满额时间
     */
    public Timestamp F12;
    
    /** 
     * 锁定结束日期
     */
    public Date F13;
    
    /** 
     * 收益处理,MYHXDQHB:每月还息，到期还本;YCXHBFX:一次性还本付息;DEBX:等额本息
     */
    public T6410_F14 F14;
    
    /** 
     * 加入费率
     */
    public BigDecimal F15 = BigDecimal.ZERO;
    
    /** 
     * 服务费率
     */
    public BigDecimal F16 = BigDecimal.ZERO;
    
    /** 
     * 退出费率
     */
    public BigDecimal F17 = BigDecimal.ZERO;
    
    /** 
     * 计划介绍
     */
    public String F18;
    
    /** 
     * 创建人,参考T7010.F01
     */
    public int F19;
    
    /** 
     * 创建时间
     */
    public Timestamp F20;
    
    /** 
     * 下一还款日
     */
    public Date F21;
    
    /** 
     * 投资下限
     */
    public BigDecimal F22 = BigDecimal.ZERO;
    
    /** 
     * 投资上限
     */
    public BigDecimal F23 = BigDecimal.ZERO;
    
    /** 
     * 保障方式,QEBXBZ:全额本息保障
     */
    public T6410_F24 F24;
    
}
