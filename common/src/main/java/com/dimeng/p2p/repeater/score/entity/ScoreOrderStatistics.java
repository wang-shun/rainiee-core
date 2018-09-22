/*
 * 文 件 名:  ScoreOrderStatistics.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  beiweiyuan
 * 修改时间:  2015年12月21日
 */
package com.dimeng.p2p.repeater.score.entity;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 订单管理页面统计信息
 * <功能详细描述>
 * 
 * @author  beiweiyuan
 * @version  [版本号, 2015年12月21日]
 */
public class ScoreOrderStatistics extends AbstractEntity
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 11574875145785415L;
    
    /**
     * 兑换积分总计
     */
    public BigDecimal scoreAmount = BigDecimal.ZERO;
    
    /**
     * 余额购买总计
     */
    public BigDecimal balanceAmount = BigDecimal.ZERO;
    
    /**
     * 商品数量总计
     */
    public int count;

}
