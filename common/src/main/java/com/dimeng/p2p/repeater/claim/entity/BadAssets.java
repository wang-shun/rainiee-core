/*
 * 文 件 名:  BadClaimAmount.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月20日
 */
package com.dimeng.p2p.repeater.claim.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <不良资产金额>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月20日]
 */
public class BadAssets implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 24374951507913800L;
    
    /**
     * 债权总价值（元）
     */
    public BigDecimal claimAmountTatal = BigDecimal.ZERO;
    
    /**
     * 购买价格总金额（元）
     */
    public BigDecimal transferAmountTatal = BigDecimal.ZERO;
    
}
