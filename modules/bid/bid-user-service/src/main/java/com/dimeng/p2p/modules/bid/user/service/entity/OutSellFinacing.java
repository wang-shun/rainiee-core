package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 已转出的债权
 *
 */
public class OutSellFinacing {
	

    /** 
     * 转让申请ID,参考T6261.F01
     */
    public int F01;

    /** 
     * 购买人ID,参考T6110.F01
     */
    public int F02;

    /** 
     * 购买债权
     */
    public BigDecimal F03 = new BigDecimal(0);

    /** 
     * 受让价格
     */
    public BigDecimal F04 = new BigDecimal(0);

    /** 
     * 转让手续费
     */
    public BigDecimal F05 = new BigDecimal(0);

    /** 
     * 购买时间
     */
    public Timestamp F06;

    /** 
     * 转入盈亏
     */
    public BigDecimal F07 = new BigDecimal(0);

    /** 
     * 转出盈亏
     */
    public BigDecimal F08 = new BigDecimal(0);
    /**
     * 借款标ID
     */
    public int jkbId;
    /**
     * 债权编号
     */
    public String zqNub;
}
