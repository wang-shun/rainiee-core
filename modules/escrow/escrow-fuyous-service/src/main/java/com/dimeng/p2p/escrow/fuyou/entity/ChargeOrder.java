package com.dimeng.p2p.escrow.fuyou.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.S65.enums.T6501_F03;

/**
 * 
 * 充值订单表
 * 
 * @author heshiping
 * @version [版本号, 2015年5月25日]
 */
public class ChargeOrder implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 订单id
     */
    public int id;
    
    /**
     * 下单时间
     */
    public Timestamp orderTime;
    
    /**
     * 充值金额
     */
    public BigDecimal amount;
    
    /**
     * 支付公司代码
     */
    public int payCompanyCode;
    
    /**
     * 支付状态
     */
    public T6501_F03 status;
    
    /**
     * 流水号
     */
    public String serialNumber;
    
}
