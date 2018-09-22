package com.dimeng.p2p.app.servlets.user.domain.mall;

import java.io.Serializable;
import java.math.BigDecimal;

import com.dimeng.p2p.S63.enums.T6350_F07;
import com.dimeng.p2p.common.enums.YesOrNo;

/**
 * 积分商城商品列表
 * 
 * @author  yuanguangjie
 * @version  [版本号, 2016年2月23日]
 */

public class ScoreMall implements Serializable {

	/**
     * 注释内容
     */
    private static final long serialVersionUID = 4938741590611749162L;
    
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
	 * 成交笔数
	 */
	private int dealNum;
	
	/**
	 * 商品类别,参考T6350.F01
	 */
	private String type;
	
	/**
     * 商品库存
     */
	private int stock;
	
    /**
     * 单用户限购数量
     */
	private int purchase;
	
    /**
     * 商品详情
     */
	private String details;
	
	 /**
     * 是否支持积分购买，No：不支持；Yes：支持
     */
	private YesOrNo isCanScore;
	
	 /**
     * 是否支持余额购买，No：不支持；Yes：支持
     */
	private YesOrNo isCanMoney;
	
	/**
     * 购买数量
     */
    private int num;
    
    /**
     * 商品类别 kind:实物，fee:话费
     */
    private T6350_F07 commTypeEnum;
    
    /**
     * 市场参考值
     */
    private BigDecimal marketPrice = BigDecimal.ZERO;
	
	public int getId() 
	{
		return id;
	}
	
	public void setId(int id) 
	{
		this.id = id;
	}
	
	public int getScore() 
	{
		return score;
	}
	
	public void setScore(int score)
	{
		this.score = score;
	}

	public int getDealNum()
	{
		return dealNum;
	}
	
	public void setDealNum(int dealNum) 
	{
		this.dealNum = dealNum;
	}
	
	public YesOrNo getIsCanScore()
	{
		return isCanScore;
	}
	public void setIsCanScore(YesOrNo isCanScore)
	{
		this.isCanScore = isCanScore;
	}
	
	public YesOrNo getIsCanMoney() {
		return isCanMoney;
	}
	
	public void setIsCanMoney(YesOrNo isCanMoney) 
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

	public String getType() 
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public int getStock()
	{
		return stock;
	}

	public void setStock(int stock)
	{
		this.stock = stock;
	}

	public int getPurchase()
	{
		return purchase;
	}

	public void setPurchase(int purchase) 
	{
		this.purchase = purchase;
	}

	public String getDetails()
	{
		return details;
	}

	public void setDetails(String details)
	{
		this.details = details;
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

    public BigDecimal getMarketPrice()
    {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice)
    {
        this.marketPrice = marketPrice;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "ScoreMall [id=" + id + ", name=" + name + ", image=" + image + ", score=" + score + ", amount=" + amount
            + ", dealNum=" + dealNum + ", type=" + type + ", stock=" + stock + ", purchase=" + purchase + ", details="
            + details + ", isCanScore=" + isCanScore + ", isCanMoney=" + isCanMoney + ", num=" + num + ", commTypeEnum="
            + commTypeEnum + ", marketPrice=" + marketPrice + "]";
    }
	
}
