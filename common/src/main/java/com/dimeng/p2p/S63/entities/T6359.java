package com.dimeng.p2p.S63.entities
        ;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S63.enums.T6359_F08;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 平台商城订单明细表
 */
public class T6359 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /**
     * 订单明细ID
     */
    public int F01;

    /**
     * 订单ID,参考T6352.F01
     */
    public int F02;

    /**
     * 商品ID,参考T6351.F01
     */
    public int F03;

    /**
     * 积分
     */
    public int F04;

    /**
     * 购买金额
     */
    public BigDecimal F05 = new BigDecimal(0);

    /**
     * 数量
     */
    public int F06;

    /**
     * 充值话费手机号
     */
    public String F07;

    /**
     * 状态（pendding:待审核、pass:待发货、 nopass：审核不通过、sended：已发货,returned：已退货、refunding：申请退款、norefund:拒绝退款、refund：已退款）
     */
    public T6359_F08 F08;

    /**
     * 发货人
     */
    public int F09;

    /**
     * 发货时间
     */
    public Timestamp F10;

    /**
     * 物流方
     */
    public String F11;

    /**
     * 物流单号
     */
    public String F12;

    /**
     * 收货人
     */
    public String F13;

    /**
     * 收货人省市区
     */
    public int F14;

    /**
     * 收货人街道地址
     */
    public String F15;

    /**
     * 收货人联系电话
     */
    public String F16;

    /**
     * 收货人邮编
     */
    public String F17;

    /**
     * 退款金额
     */
    public BigDecimal F18 = new BigDecimal(0);

    /**
     * 退款时间
     */
    public Timestamp F19;

}
