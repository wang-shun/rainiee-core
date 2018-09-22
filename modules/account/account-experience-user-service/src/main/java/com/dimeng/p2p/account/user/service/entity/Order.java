package com.dimeng.p2p.account.user.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.S65.enums.T6501_F03;

public class Order implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 订单id
     */
    public int id;
    
    /**
     * 金额
     */
    public BigDecimal amount;
    
    /**
     * 下单时间
     */
    public Timestamp orderTime;
    
    /**
     * 支付状态
     */
    public T6501_F03 status;
    
    /**
     * 支付类型
     */
    public String payType;
    
    /**
     * 金额字符串
     */
    public String amountStr;
    
    /**
     * 状态字符串
     */
    public String statusName;
    
    /**
     * 订单时间字符串
     */
    public String orderTimeStr;
}
