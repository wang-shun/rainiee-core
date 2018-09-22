/*
 * 文 件 名:  MyOrderInfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年1月29日
 */
package com.dimeng.p2p.app.servlets.user.domain.mall;

import java.io.Serializable;
import java.util.List;

/**
 * 用户的订单信息
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月29日]
 */
public class MyOrderInfo implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -1775577035260673414L;

    /**
     * 订单号
     */
    private String orderId;
    
    /**
     * 订单下商品数量
     */
    private String orderNum;
    
    /**
     * 订单总计金额
     */
    private String orderSumAmount;
    
    /**
     * 付费方式
     */
    private String payment;
    
    /**
     * 订单下商品
     */
    private List<MyOrderExtInfo> myOrderExtInfos;
    
    /**
     * 订单时间
     */
    private String orderTime;
    
    /**
     * 发货时间
     */
    private String dispatchDate;
    
    /**
     * 物流公司
     */
    private String logistics;
    
    /**
     * 物流订单号
     */
    private String logisticsOrderNum;
    
    public String getOrderId()
    {
        return orderId;
    }
    
    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }
    
    public String getOrderNum()
    {
        return orderNum;
    }
    
    public void setOrderNum(String orderNum)
    {
        this.orderNum = orderNum;
    }
    
    public String getOrderSumAmount()
    {
        return orderSumAmount;
    }
    
    public void setOrderSumAmount(String orderSumAmount)
    {
        this.orderSumAmount = orderSumAmount;
    }
    
    public List<MyOrderExtInfo> getMyOrderExtInfos()
    {
        return myOrderExtInfos;
    }
    
    public void setMyOrderExtInfos(List<MyOrderExtInfo> myOrderExtInfos)
    {
        this.myOrderExtInfos = myOrderExtInfos;
    }
    
    public String getOrderTime()
    {
        return orderTime;
    }
    
    public void setOrderTime(String orderTime)
    {
        this.orderTime = orderTime;
    }
    
    public String getDispatchDate()
    {
        return dispatchDate;
    }
    
    public void setDispatchDate(String dispatchDate)
    {
        this.dispatchDate = dispatchDate;
    }
    
    public String getLogistics()
    {
        return logistics;
    }
    
    public void setLogistics(String logistics)
    {
        this.logistics = logistics;
    }
    
    public String getLogisticsOrderNum()
    {
        return logisticsOrderNum;
    }
    
    public void setLogisticsOrderNum(String logisticsOrderNum)
    {
        this.logisticsOrderNum = logisticsOrderNum;
    }

    public String getPayment()
    {
        return payment;
    }

    public void setPayment(String payment)
    {
        this.payment = payment;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "MyOrderInfo [orderId=" + orderId + ", orderNum=" + orderNum + ", orderSumAmount=" + orderSumAmount
            + ", payment=" + payment + ", myOrderExtInfos=" + myOrderExtInfos + ", orderTime=" + orderTime
            + ", dispatchDate=" + dispatchDate + ", logistics=" + logistics + ", logisticsOrderNum=" + logisticsOrderNum
            + "]";
    }
    
}
