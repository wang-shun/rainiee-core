package com.dimeng.p2p.repeater.score.query;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

public class ScoreOrderQuery extends AbstractEntity
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
     * 成交开始时间
     */
    public Timestamp startTime;

    /**
     * 成交结束时间
     */
    public Timestamp endTime;

    /**
     * 发货开始时间
     */
    public Timestamp fhStartTime;

    /**
     * 发货结束时间
     */
    public Timestamp fhEndTime;

    /**
     * 审核开始时间
     */
    public Timestamp shStartTime;

    /**
     * 审核结束时间
     */
    public Timestamp shEndTime;

    /**
     * 退货开始时间
     */
    public Timestamp thStartTime;

    /**
     * 退货结束时间
     */
    public Timestamp thEndTime;

    /**
     * 商品名称
     */
    public String productName;
    
    /**
     * 支付方式
     */
    public String payType;

    /**
     * 商品类别
     */
    public String productType;

    /**
     * 状态
     */
    public String status;
}
