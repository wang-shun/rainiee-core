package com.dimeng.p2p.repeater.score.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.S63.entities.T6352;
import com.dimeng.p2p.S63.enums.T6352_F06;

public class ScoreOrderInfoExt extends T6352 {

    public static final long serialVersionUID = 7423121218129319726L;
    
    public String name;
    
    public String statusDesc;
    
    /**
     * 用户名
     */
    public String loginName;
    
    /**
     * 真实姓名
     */
    public String realName;

    /**
     * 商品数量
     */
    public int comdNum;
    
    /**
     * 订单时间
     */
    public Timestamp orderTime;
    
    public String reviewer;
    
    public String sender;
    
    /**
     * 商品名字
     */
    public String comdName;
    
    /**
     * 商品积分
     */
    public int comdScore;
    
    /**
     * 商品价格
     */
    public BigDecimal comdPrices;
    
    /**
     * 购买总数
     */
    public int buyTotal;
    
    /**
     * 支付方式
     */
    public T6352_F06 payment;

}
