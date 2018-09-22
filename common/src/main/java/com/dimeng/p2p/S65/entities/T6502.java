package com.dimeng.p2p.S65.entities
;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.common.enums.BusinessStatus;

/** 
 * 充值订单
 */
public class T6502 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 订单号,参考T6501.F01
     */
    public int F01;

    /** 
     * 用户ID,参考T6110.F01
     */
    public int F02;

    /** 
     * 充值金额
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
     * 支付公司代号
     */
    public int F07;

    /** 
     * 流水单号,充值成功时记录
     */
    public String F08;

    /**
     * 业务员工号
     */
    public String F09;

    /**
     * 业务员状态
     */
    public BusinessStatus F10;
    
    /**
     * 充值来源
     */
    public String F11;
    
    /**
     * 充值类型
     */
    public String F12;
    
    /**
     * 充值来源
     */
    public String chargeSource;
}
