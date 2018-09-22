package com.dimeng.p2p.account.user.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.S71.enums.T7150_F05;

/**
 * 线下充值订单
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  beiweiyuan
 * @version  [版本号, 2015年11月13日]
 */
public class OrderXxcz implements Serializable
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
    public T7150_F05 status;
    
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
    /**
     * 备注
     */
    public String remark;
}
