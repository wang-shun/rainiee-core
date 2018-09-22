/*
 * 文 件 名:  OrderDetQuery.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年5月16日
 */
package com.dimeng.p2p.repeater.score.query;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * <订单详情>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年5月16日]
 */
public class OrderDetQuery extends AbstractEntity
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -7571024552564571551L;
    
    /**
     * 商品id
     */
    public int id;
    
    /**
     * 订单号
     */
    public String orderNo;
    
    /**
     * 用户名
     */
    public String loginName;
    
    /**
     * 手机号码
     */
    public String phoneNum;
    
    /**
     * 支付方式
     */
    public String payType;
    
    /**
     * 成交开始时间
     */
    public Timestamp startTime;
    
    /**
     * 成交结束时间
     */
    public Timestamp endTime;
    
    /**
     * 状态
     */
    public String status;
    
}
