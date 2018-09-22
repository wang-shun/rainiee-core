package com.dimeng.p2p.repeater.score.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S63.enums.T6350_F07;
import com.dimeng.p2p.S63.enums.T6352_F06;
import com.dimeng.p2p.S63.enums.T6359_F08;

public class ScoreOrderDetailRecord extends AbstractEntity
{

    private static final long serialVersionUID = 1L;
    
    /**
     * 订单号
     */
    public String orderNo;

    /**
     * 用户名
     */
    public String loginName;
    
    /**
     * 真实姓名
     */
    public String realName;
    
    /**
     * 手机号码
     */
    public String phoneNum;

    /**
     * 商品编码
     */
    public String productNum;
    
    /**
     * 商品名称
     */
    public String productName;
    
    /**
     * 商品类别名称
     */
    public String productTypeName;
    
    /**
     * 价值
     */
    public BigDecimal worth;
    
    /**
     * 支付方式
     */
    public T6352_F06 payType;
    
    /**
     * 数量
     */
    public int quantity;

    /**
     * 状态
     */
    public T6359_F08 status;
    
    /**
     * 成交开始时间
     */
    public Timestamp dealTime;
    
    /**
     * 物流方
     */
    public String express;
    
    /**
     * 物流单号
     */
    public String expressNum;
    
    /**
     * 收货人
     */
    public String receiver;
    
    /**
     * 收货人省市区
     */
    public String addressMain;
    
    /**
     * 收货人街道地址
     */
    public String addressDetail;
    
    /**
     * 收货人联系电话
     */
    public String receiverPhoneNum;
    
    /**
     * 收货人邮编
     */
    public String zipCode;
    
    /**
     * 充值手机号码
     */
    public String chargePhoneNum;
    
    /**
     * 收货人省市区ID
     */
    public int areaID;
    
    /**
     * 商品类别
     */
    public T6350_F07 productType;

    /**
     * 操作备注
     */
    public String updateReason;
}
