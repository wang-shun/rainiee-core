/*
 * 文 件 名:  MyOrderItems.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年2月24日
 */
package com.dimeng.p2p.app.servlets.user.domain.mall;

import java.io.Serializable;
import java.util.List;

/**
 * 我的订单详情
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年2月24日]
 */
public class MyOrderItems implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -3311259718197375385L;

    /**
     * 收件人
     */
    private String buyer;
    
    /**
     * 收件人联系电话
     */
    private String telephone;
    
    /**
     * 收件人联系地址
     */
    private String address;
    
    /**
     * 订单下商品
     */
    private List<MyOrderInfo> myOrderInfos;
    
    public String getBuyer()
    {
        return buyer;
    }
    
    public void setBuyer(String buyer)
    {
        this.buyer = buyer;
    }
    
    public String getTelephone()
    {
        return telephone;
    }
    
    public void setTelephone(String telephone)
    {
        this.telephone = telephone;
    }
    
    public String getAddress()
    {
        return address;
    }
    
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    public List<MyOrderInfo> getMyOrderInfos()
    {
        return myOrderInfos;
    }
    
    public void setMyOrderInfos(List<MyOrderInfo> myOrderInfos)
    {
        this.myOrderInfos = myOrderInfos;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "MyOrderItems [buyer=" + buyer + ", telephone=" + telephone + ", address=" + address + ", myOrderInfos="
            + myOrderInfos + "]";
    }
    
}
