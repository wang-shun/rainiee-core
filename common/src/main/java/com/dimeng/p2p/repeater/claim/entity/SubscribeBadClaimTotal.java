/*
 * 文 件 名:  SubscribeBadClaim.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  huqinfu
 * 修改时间:  2016年6月15日
 */
package com.dimeng.p2p.repeater.claim.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * 认购不良债权统计实体类
 * 
 * @author  huqinfu
 * @version  [版本号, 2016年6月15日]
 */
public class SubscribeBadClaimTotal implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 累计成交总价值
     */
    public BigDecimal totalCreditPrice = BigDecimal.ZERO;
    
    /**
     * 累计认购价格
     */
    public BigDecimal totalSubscribePrice = BigDecimal.ZERO;
    
    /**
     * 成功认购笔数
     */
    public int subscribeCount;
    
}
