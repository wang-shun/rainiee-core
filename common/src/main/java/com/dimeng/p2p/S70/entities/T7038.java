package com.dimeng.p2p.S70.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 平台资金统计-按年度
 */
public class T7038 extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 年度
     */
    public int F01;
    
    /**
     * 更新时间
     */
    public Timestamp F02;
    
    /**
     * 充值手续费
     */
    public BigDecimal F03 = BigDecimal.ZERO;
    
    /**
     * 提现手续费
     */
    public BigDecimal F04 = BigDecimal.ZERO;
    
    /**
     * 充值成本
     */
    public BigDecimal F05 = BigDecimal.ZERO;
    
    /**
     * 提现成本
     */
    public BigDecimal F06 = BigDecimal.ZERO;
    
    /**
     * 身份验证手续费
     */
    public BigDecimal F07 = BigDecimal.ZERO;
    
    /**
     * 借款管理费
     */
    public BigDecimal F08 = BigDecimal.ZERO;
    
    /**
     * 逾期管理费
     */
    public BigDecimal F09 = BigDecimal.ZERO;
    
    /**
     * 债权转让费
     */
    public BigDecimal F10 = BigDecimal.ZERO;
    
    /**
     * 活动费用
     */
    public BigDecimal F11 = BigDecimal.ZERO;
    
    /**
     * 理财管理费
     */
    public BigDecimal F12 = BigDecimal.ZERO;
    
    /**
     * 持续推广费用
     */
    public BigDecimal F13 = BigDecimal.ZERO;
    
    /**
     * 有效推广费用
     */
    public BigDecimal F14 = BigDecimal.ZERO;
    
    /**
     * 违约金手续费
     */
    public BigDecimal F15 = BigDecimal.ZERO;
    
    /**
     * 本金垫付返还
     */
    public BigDecimal F16 = BigDecimal.ZERO;
    
    /**
     * 利息垫付返还
     */
    public BigDecimal F17 = BigDecimal.ZERO;
    
    /**
     * 罚息垫付返还
     */
    public BigDecimal F18 = BigDecimal.ZERO;
    
    /**
     * 本金垫付支出
     */
    public BigDecimal F19 = BigDecimal.ZERO;
    
    /**
     * 利息垫付支出
     */
    public BigDecimal F20 = BigDecimal.ZERO;
    
    /**
     * 罚息垫付支出
     */
    public BigDecimal F21 = BigDecimal.ZERO;
    
}
