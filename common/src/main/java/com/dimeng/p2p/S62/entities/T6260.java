package com.dimeng.p2p.S62.entities;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6260_F07;

/** 
 * 债权转让申请
 */
public class T6260 extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /** 
     * 自增ID
     */
    public int F01;
    
    /** 
     * 债权ID,参考T6251.F01
     */
    public int F02;
    
    /** 
     * 转让价格
     */
    public BigDecimal F03 = BigDecimal.ZERO;
    
    /** 
     * 转让债权
     */
    public BigDecimal F04 = BigDecimal.ZERO;
    
    /** 
     * 创建时间
     */
    public Timestamp F05;
    
    /** 
     * 结束日期
     */
    public Date F06;
    
    /** 
     * 状态,DSH:待审核;ZRZ:转让中;YJS:已结束;
     */
    public T6260_F07 F07;
    
    /** 
     * 转让手续费率
     */
    public BigDecimal F08 = BigDecimal.ZERO;
    
    /** 
     * 购买债权、下架、取消时的剩余期数
     */
    public int F09;
    
    /** 
     * 购买债权时的本息
     */
    public BigDecimal F10 = BigDecimal.ZERO;
    
    /**
     * 第三方债权使用编码
     */
    public String F11;
}
