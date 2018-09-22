package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 可以转出的债权
 */
public class MaySettleFinacing {
	
	/** 
     * 债权id
     */
    public int F08;
    /** 
     * 债权编码
     */
    public String F01;

    /** 
     * 标ID,参考T6230.F01
     */
    public int F02;

    /** 
     * 持有债权金额
     */
    public BigDecimal F03 = new BigDecimal(0);

    /** 
     * 还款总期数
     */
    public int F04;

    /** 
     * 剩余期数
     */
    public int F05;

    /** 
     * 下次还款日期
     */
    public Date F06;

    /** 
     * 年化利率
     */
    public BigDecimal F07 = new BigDecimal(0);

	/**
	 * 待收本息
	 */
	public BigDecimal money = new BigDecimal(0);

    /**
     * 待收本息:字符串型
     */
    public String F09;

    /**
     * 年化利率
     */
    public String F10;

    /**
     * 下次还款日期
     */
    public String F11;

    /**
     * 持有债权金额
     */
    public String F12;
}
