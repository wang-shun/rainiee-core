package com.dimeng.p2p.repeater.claim.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.S62.entities.T6230;

/**
 * 已转让的借款
 * 
 * @author  huqinfu
 * @version  [版本号, 2016年6月22日]
 */
public class YZRLoanEntity extends T6230
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 670947648821982831L;
    
    /**
     * 转让总金额
     */
    public BigDecimal zrTotle;
    
    /**
     * 未还期数
     */
    public int leftNum;
    
    /**
     * 总期数
     */
    public int totalNum;
    
    /**
     * 转让时间
     */
    public Timestamp zrTime;
    
    /**
     * 转让记录id
     */
    public int zrId;
    
}
