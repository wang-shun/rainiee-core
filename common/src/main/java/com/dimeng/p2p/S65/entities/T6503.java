package com.dimeng.p2p.S65.entities;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/** 
 * 提现订单
 */
public class T6503 extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /** 
     * 订单ID,参考T6501.F01
     */
    public int F01;
    
    /** 
     * 用户ID,参考T6110.F01
     */
    public int F02;
    
    /** 
     * 提现金额
     */
    public BigDecimal F03 = BigDecimal.ZERO;
    
    /** 
     * 应收手续费
     */
    public BigDecimal F04 = BigDecimal.ZERO;
    
    /** 
     * 实收手续费
     */
    public BigDecimal F05 = BigDecimal.ZERO;
    
    /** 
     * 银行卡号
     */
    public String F06;
    
    /** 
     * 支付公司代码
     */
    public int F07;
    
    /** 
     * 流水单号,提现成功时填入
     */
    public String F08;
    
    /** 
     * 提现申请记录ID,参考T6130.F01
     */
    public int F09;
    
    /** 
     * 提现手续费分润收入
     */
    public BigDecimal F10 = BigDecimal.ZERO;
    
    /** 
     * 实际到账金额
     */
    public BigDecimal F11 = BigDecimal.ZERO;
}
