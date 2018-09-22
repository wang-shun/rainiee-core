/*
 * 文 件 名:  BadClaimDsh.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月16日
 */
package com.dimeng.p2p.repeater.claim.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * <不良债权待审核>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月16日]
 */
public class BadClaimDsh extends BadClaimZr
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1201269933993585042L;
    
    /**
     * id
     */
    public int id;
    
    /**
     * 债权编号
     */
    public String claimNo;
    
    /**
     * 转让价格（元）
     */
    public BigDecimal transferAmount = BigDecimal.ZERO;
    
    /**
     * 申请时间
     */
    public Timestamp applyTime;
    
}
