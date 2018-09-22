package com.dimeng.p2p.app.servlets.user.domain.mall;

import java.io.Serializable;
import java.math.BigDecimal;

import com.dimeng.p2p.S63.enums.T6350_F07;

/**
 * 积分商城商品列表
 * 
 * @author  yuanguangjie
 * @version  [版本号, 2016年2月23日]
 */

public class ShoppingCarInfo implements Serializable {

	/**
     * 注释内容
     */
    private static final long serialVersionUID = 5449416996033810189L;
    
    /**
	 * 购物车Id
	 */
	private int carId;
	
	/**
	 * 商品Id
	 */
	private int id;
	
	 /**
     * 商品名称
     */
    private String name;
    
    /**
     * 商品图片
     */
    private String image;
    
	/**
	 * 积分
	 */
	private int score;
	
	/**
	 * 价格
	 */
	private BigDecimal amount;
	
	/**
     * 商品库存
     */
	private int stock;
      
	 /**
     * 是否支持积分购买，No：不支持；Yes：支持
     */
	private String isCanScore;
	
	 /**
     * 是否支持余额购买，No：不支持；Yes：支持
     */
	private String isCanMoney;
	
	/**
     * 购买数量
     */
    private int num;
    
    /**
     * 商品类别 kind:实物，fee:话费
     */
    private T6350_F07 commTypeEnum;
    
    /**
     * 单用户限购数量
     */
	private int purchase;
	
	/**
     * 已购买数量
     */
    private int ygCount;
	
	
	public int getYgCount()
	{
		return ygCount;
	}

	public void setYgCount(int ygCount)
	{
		this.ygCount = ygCount;
	}

	public int getPurchase()
	{
		return purchase;
	}

	public void setPurchase(int purchase) 
	{
		this.purchase = purchase;
	}

	public int getCarId() 
	{
		return carId;
	}

	public void setCarId(int carId) 
	{
		this.carId = carId;
	}

	public int getScore() 
	{
		return score;
	}
	
	public void setScore(int score)
	{
		this.score = score;
	}

	public String getIsCanScore() 
	{
		return isCanScore;
	}

	public void setIsCanScore(String isCanScore)
	{
		this.isCanScore = isCanScore;
	}

	public String getIsCanMoney() 
	{
		return isCanMoney;
	}

	public void setIsCanMoney(String isCanMoney) 
	{
		this.isCanMoney = isCanMoney;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getImage()
	{
		return image;
	}

	public void setImage(String image) 
	{
		this.image = image;
	}

	public int getStock()
	{
		return stock;
	}

	public void setStock(int stock)
	{
		this.stock = stock;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount)
	{
		this.amount = amount;
	}

	public int getNum() 
	{
		return num;
	}

	public void setNum(int num) 
	{
		this.num = num;
	}

	public T6350_F07 getCommTypeEnum()
	{
		return commTypeEnum;
	}

	public void setCommTypeEnum(T6350_F07 commTypeEnum)
	{
		this.commTypeEnum = commTypeEnum;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "ShoppingCarInfo [carId=" + carId + ", id=" + id + ", name=" + name + ", image=" + image + ", score="
            + score + ", amount=" + amount + ", stock=" + stock + ", isCanScore=" + isCanScore + ", isCanMoney="
            + isCanMoney + ", num=" + num + ", commTypeEnum=" + commTypeEnum + ", purchase=" + purchase + ", ygCount="
            + ygCount + "]";
    }
	
}
