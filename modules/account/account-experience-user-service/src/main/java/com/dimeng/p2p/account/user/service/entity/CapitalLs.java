package com.dimeng.p2p.account.user.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6102_F10;

/**
 * 资金流水
 *
 */
public class CapitalLs extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /** 
     * 自增ID
     */
    public int F01;
    
    /** 
     * 资金账号ID,参考T6101.F01
     */
    public int F02;
    
    /** 
     * 交易类型ID,参考T5122.F01
     */
    public int F03;
    
    /** 
     * 对方账户ID,参考T6101.F01
     */
    public int F04;
    
    /** 
     * 创建时间
     */
    public Timestamp F05;
    
    /** 
     * 收入
     */
    public BigDecimal F06 = new BigDecimal(0);
    
    /** 
     * 支出
     */
    public BigDecimal F07 = new BigDecimal(0);
    
    /** 
     * 余额
     */
    public BigDecimal F08 = new BigDecimal(0);
    
    /** 
     * 备注
     */
    public String F09;
    
    /** 
     * 对账状态,WDZ:未对账;YDZ:已对账;
     */
    public T6102_F10 F10;
    
    /** 
     * 对账时间
     */
    public Timestamp F11;
    
    /** 
     * 用户ID,参考T6110.F01
     */
    public int F12;
    
    /** 
     * 账户类型,WLZH:往来账户;FXBZJZH:风险保证金账户;SDZH:锁定账户;
     */
    public T6101_F03 F13;
    
    /** 
     * 资金账号
     */
    public String F14;
    
    /** 
     * 账户名称
     */
    public String F15;
    
    /** 
     * 余额
     */
    public BigDecimal F16 = new BigDecimal(0);
    
    /**
     * 标的ID
     */
    public int F17 = 0;
    
    /**
     * 交易类型
     */
    public String tradeType;
    
    /**
     * 资金流水日期时间字符串
     */
    public String F18;
}
