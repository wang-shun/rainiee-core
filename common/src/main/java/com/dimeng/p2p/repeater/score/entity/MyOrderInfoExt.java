package com.dimeng.p2p.repeater.score.entity;

import java.sql.Timestamp;

import com.dimeng.p2p.S63.entities.T6359;
import com.dimeng.p2p.S63.enums.T6350_F07;
import com.dimeng.p2p.S63.enums.T6352_F06;

public class MyOrderInfoExt extends T6359
{

    public static final long serialVersionUID = 7423121218129319726L;
    
    /**
     * 订单时间
     */
    public Timestamp orderTime;
    
    /**
     * 商品名字 T6351.F03
     */
    public String comdName;
    
    /**
     * 订单号 T6352.F03
     */
    public String orderNum;

    /**
     * 支付方式
     */
    public T6352_F06 payment;
    
    /**
     * 商品图片
     */
    public String comdPicture;
    
    /**
     * 移动端商品图片
     */
    public String appComdPicture;
    
    /**
     * 商品类别
     */
    public T6350_F07 category;
    
    /**
     * 手机号
     */
    public String phoneNum;
    
    /**
     * 省市信息
     */
    public String region;
}
