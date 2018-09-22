package com.dimeng.p2p.repeater.score.entity;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 积分商城-商品查询VO
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  zengzhihua
 * @version  [版本号, 2015年9月1日]
 */
public class CommoditySearch extends AbstractEntity {
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -4650817935081935308L;

    //商品ID
    public int id;
    
    //商品名称
    public String name;
    
    //商品类别
    public int catId;
    
    //商品状态
    public String status;
    
    //创建时间
    public String startTime;
    
    public String endTime;
    
    //库存
    public String stockMin;
    
    public String stockMax;

}
