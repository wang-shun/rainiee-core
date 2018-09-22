/*
 * 文 件 名:  SearchGoodsCategory.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2015年9月8日
 */
package com.dimeng.p2p.repeater.score.entity;

import com.dimeng.framework.service.AbstractEntity;

/**
 * <积分商城首页-搜索条件>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年9月8日]
 */
public class SearchGoodsCategory extends AbstractEntity
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 7433659532219947440L;

    public String goodsCategory;

    public String scoreRange;
    
    public String amountRange;

    public String sortWay;

    public String orderBy;
}
