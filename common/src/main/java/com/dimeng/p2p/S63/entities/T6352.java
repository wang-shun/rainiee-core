package com.dimeng.p2p.S63.entities ;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S63.enums.T6352_F06;

/**
 * 平台商城订单记录表
 */
public class T6352 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    public int F01;

    /**
     * 用户ID,参考T6110.F01
     */
    public int F02;

    /**
     * 订单编号
     */
    public String F03;

    /**
     * 订单时间
     */
    public Timestamp F04;

    /**
     * 来源
     */
    public String F05;

    /**
     * 支付方式（score:积分支付,balance:余额支付）
     */
    public T6352_F06 F06;

}
