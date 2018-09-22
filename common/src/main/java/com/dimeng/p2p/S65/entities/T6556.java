package com.dimeng.p2p.S65.entities;

import com.dimeng.framework.service.AbstractEntity;

import java.math.BigDecimal;

/**
 * 
 * 商品购买订单参数
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月25日]
 */
public class T6556 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 参数ID
     */
    public int F01;

    /** 
     * 商品ID,参考T6351.F01
     */
    public int F02;

    /** 
     * 订单ID,参考T6555.F01
     */
    public int F03;

    /** 
     * 购买数量
     */
    public int F04;

    /** 
     * 商购物车ID,参考T6358.F01
     */
    public int F05;

    /** 
     * 充值话费手机号
     */
    public String F06;

    /**
     * 商品单价
     */
    public BigDecimal F07;

}