package com.dimeng.p2p.repeater.score.query;
/**
 * 积分商城-商品类型查询
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  zengzhihua
 * @version  [版本号, 2015年9月1日]
 */
public abstract interface CommodityCategorySearch {

    /**
     * 商品类别
     */
    public abstract String getF02();

    /**
     * 商品类别状态
     */
    public abstract String getF04();

    /**
     * 商品类别属性
     */
    public abstract String getF07();

}
