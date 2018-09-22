package com.dimeng.p2p.repeater.score.entity;

import java.math.BigDecimal;

/**
 * 
 * 平台商品购物车
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月16日]
 */
public class ShoppingCarResult
{
    /**
     * 购物车id
     */
    public int id;
    /**
     * 商品ID
     */
    public int goodsId;
    /**
     * 商品名称
     */
    public String goodsName;
    /**
     * 商品图片
     */
    public String goodsImg;
    /**
     * 兑换积分
     */
    public int score;
    /**
     * 购买金额
     */
    public BigDecimal amount = new BigDecimal(0);
    /**
     * 购买数量
     */
    public int num;
    /**
     * 商品库存数量
     */
    public int goodsCount;
    /**
     * 是否支持积分兑换
     */
    public String isBuyScore;
    /**
     * 是否支持现金购买
     */
    public String isBuyCash;
    
    /**
     * 用户限购数量
     */
    public int singleCount;
    
    /**
     * 商品状态
     */
    public String status;
    
    /**
     * 商品描述
     */
    public String desc;
    
    /**
     * 已购买数量
     */
    public int ygCount;
}
