package com.dimeng.p2p.repeater.score.entity;
/**
 * 
 * 收货信息
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月14日]
 */
public class AddressResult
{
    /**
     * 主键id
     */
    public int id;
    /**
     * 区域
     */
    public String regionStr;
    /**
     * 区域id
     */
    public int region;
    /**
     * 详细地址
     */
    public String address;
    /**
     * 联系电话
     */
    public String telephone;
    /**
     * 邮编
     */
    public String code;
    /**
     * 是否为默认
     */
    public String isDefault;
    
    /**
     * 收货人
     */
    public String receiverName;
    
    public int shengId;
    
    public int shiId;
    
    public int xianId;

    public String addressStr;
}
