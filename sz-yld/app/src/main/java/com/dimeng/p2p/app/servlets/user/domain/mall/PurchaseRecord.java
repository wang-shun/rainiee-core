package com.dimeng.p2p.app.servlets.user.domain.mall;

import java.io.Serializable;
import java.math.BigDecimal;

import com.dimeng.p2p.S63.enums.T6352_F06;

/**
 * 积分商城商品详情-购买记录
 * 
 * @author  yuanguangjie
 * @version  [版本号, 2016年2月24日]
 */

public class PurchaseRecord implements Serializable {

	 /**
     * 注释内容
     */
    private static final long serialVersionUID = 5136253465259326925L;
    
    /**
     * 用户名称
     */
    private String loginName;
    
    /**
     * 购买数量
     */
    private int comdNum;
    
	/**
	 * 支付方式：积分;余额
	 */
	private T6352_F06 payment;
	
	/**
	 * 价值-积分
	 */
	private int comdScore;
	
	/**
	 * 价值-余额
	 */
	private BigDecimal comdPrices;
	
	/**
	 * 订单时间
	 */
	private String orderTime;
	
	public String getLoginName() 
	{
		return loginName;
	}
	
	public void setLoginName(String loginName) 
	{
		this.loginName = loginName;
	}
	
	public int getComdNum()
	{
		return comdNum;
	}
	
	public void setComdNum(int comdNum)
	{
		this.comdNum = comdNum;
	}
	
	public T6352_F06 getPayment() 
	{
		return payment;
	}
	
	public void setPayment(T6352_F06 payment) 
	{
		this.payment = payment;
	}
	
	public int getComdScore() 
	{
		return comdScore;
	}
	
	public void setComdScore(int comdScore)
	{
		this.comdScore = comdScore;
	}
	
	public BigDecimal getComdPrices()
	{
		return comdPrices;
	}
	
	public void setComdPrices(BigDecimal comdPrices)
	{
		this.comdPrices = comdPrices;
	}
	
	public String getOrderTime() 
	{
		return orderTime;
	}
	
	public void setOrderTime(String orderTime) 
	{
		this.orderTime = orderTime;
	}

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "PurchaseRecord [loginName=" + loginName + ", comdNum=" + comdNum + ", payment=" + payment
            + ", comdScore=" + comdScore + ", comdPrices=" + comdPrices + ", orderTime=" + orderTime + "]";
    }
	
}
