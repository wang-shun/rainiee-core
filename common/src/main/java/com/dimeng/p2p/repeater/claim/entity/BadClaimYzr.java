/*
 * 文 件 名:  BadClaimYzr.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月16日
 */
package com.dimeng.p2p.repeater.claim.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * <不良债权已转让>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月16日]
 */
public class BadClaimYzr extends BadClaimZr
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 6657562902381449522L;
    
    /**
     * 债权编号
     */
    public String claimNo;
    
    /**
     * 债权接收方
     */
    public String claimReceiver;
    
    /**
     * 转让价格（元）
     */
    public BigDecimal transferAmount = BigDecimal.ZERO;
    
    /**
     * 购买时间
     */
    public Timestamp buyTime;
}
