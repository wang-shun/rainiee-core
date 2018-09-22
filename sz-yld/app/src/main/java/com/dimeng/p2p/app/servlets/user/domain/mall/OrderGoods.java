package com.dimeng.p2p.app.servlets.user.domain.mall;

import java.io.Serializable;

/**
 * 
 * <确认订单参数实体>
 * <功能详细描述>
 * 
 * @author  yuanguangjie
 * @version  [版本号, 2016年02月29日]
 */
public class OrderGoods implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -5182089158605238293L;
    /**
     * 商品Id
     */
    private int goodsId;
    /**
     * 购买的商品数量
     */
    private int goodsNum;
    
    /**
     * 话费：手机号码
     */
    private String mobile;
    
    /**
     * 购物车Id
     */
    private int id;
    
    public int getGoodsId()
    {
        return goodsId;
    }
    
    public void setGoodsId(int goodsId)
    {
        this.goodsId = goodsId;
    }
    
    public int getGoodsNum()
    {
        return goodsNum;
    }
    public void setGoodsNum(int goodsNum)
    {
        this.goodsNum = goodsNum;
    }
    
    public String getMobile()
    {
        return mobile;
    }
    
    public void setMobile(String mobile)
    {
        this.mobile = mobile;
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
        return "OrderGoods [goodsId=" + goodsId + ", goodsNum=" + goodsNum + ", mobile=" + mobile + ", id=" + id + "]";
    }
    
}
