package com.dimeng.p2p.repeater.score.entity;
/**
 * 
 * 兑换参数实体
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月17日]
 */
public class BuyGoods
{
    /**
     * 商品ID
     */
    public int goodsId;
    /**
     * 商品数量
     */
    public int num;
    
    /**
     * 充话费手机号
     */
    public String mobile;
    /**
     * 收货人
     */
    public String receiveName;
    /**
     * 收货人区域
     */
    public int receiveRegion;
    /**
     * 收货人地址
     */
    public String receiveAddress;
    /**
     * 收货人联系电话
     */
    public String receiveTelphone;
    /**
     * 收货人邮编
     */
    public String receiveCode;
}
