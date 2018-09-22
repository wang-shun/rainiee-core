/*
 * 文 件 名:  MyOrderExtInfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年1月29日
 */
package com.dimeng.p2p.app.servlets.user.domain.mall;

import java.io.Serializable;

/**
 * 订单下商品信息
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年1月29日]
 */
public class MyOrderExtInfo implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -7587767212366110669L;

    /**
     * 订单ID
     */
    private int orderId;
    
    /**
     * 订单明细ID
     */
    private int orderExtId;
    
    /**
     * 商品ID
     */
    private int productId;
    
    /**
     * 积分
     */
    private int score;
    
    /**
     * 购买金额
     */
    private String price;
    
    /**
     * 购买数量
     */
    private int num;
    
    /**
     * 充值话费手机号
     */
    private String phone;
    
    /**
     * 订单状态
     */
    private String status;
    
    /**
     * 商品类型
     */
    private String category;
    
    /**
     * 商品名字 T6351.F03
     */
    private String comdName;
    
    /**
     * 支付方式
     */
    private String payment;
    
    /**
     * 商品图片
     */
    private String comdPicture;
    
    public int getOrderId()
    {
        return orderId;
    }
    
    public void setOrderId(int orderId)
    {
        this.orderId = orderId;
    }
    
    public int getOrderExtId()
    {
        return orderExtId;
    }
    
    public void setOrderExtId(int orderExtId)
    {
        this.orderExtId = orderExtId;
    }
    
    public int getProductId()
    {
        return productId;
    }
    
    public void setProductId(int productId)
    {
        this.productId = productId;
    }
    
    public int getScore()
    {
        return score;
    }
    
    public void setScore(int score)
    {
        this.score = score;
    }
    
    public String getPrice()
    {
        return price;
    }
    
    public void setPrice(String price)
    {
        this.price = price;
    }
    
    public int getNum()
    {
        return num;
    }
    
    public void setNum(int num)
    {
        this.num = num;
    }
    
    public String getPhone()
    {
        return phone;
    }
    
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public String getCategory()
    {
        return category;
    }
    
    public void setCategory(String category)
    {
        this.category = category;
    }
    
    public String getComdName()
    {
        return comdName;
    }
    
    public void setComdName(String comdName)
    {
        this.comdName = comdName;
    }
    
    public String getPayment()
    {
        return payment;
    }
    
    public void setPayment(String payment)
    {
        this.payment = payment;
    }
    
    public String getComdPicture()
    {
        return comdPicture;
    }
    
    public void setComdPicture(String comdPicture)
    {
        this.comdPicture = comdPicture;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "MyOrderExtInfo [orderId=" + orderId + ", orderExtId=" + orderExtId + ", productId=" + productId
            + ", score=" + score + ", price=" + price + ", num=" + num + ", phone=" + phone + ", status=" + status
            + ", category=" + category + ", comdName=" + comdName + ", payment=" + payment + ", comdPicture="
            + comdPicture + "]";
    }
}
