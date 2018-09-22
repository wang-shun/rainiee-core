package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6231_F21;

public class Tbzdzq  extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 借款用户ID,参考T6110.F01
     */
    public int F01;

    /** 
     * 借款标题
     */
    public String F02;

    /** 
     * 借款标类型ID,参考T6211.F01
     */
    public int F03;

    /** 
     * 借款金额
     */
    public BigDecimal F04 = new BigDecimal(0);

    /** 
     * 年化利率
     */
    public BigDecimal F05 = new BigDecimal(0);

    /** 
     * 可投金额
     */
    public BigDecimal F06 = new BigDecimal(0);

    /** 
     * 筹款期限,单位:天
     */
    public int F07;

    /** 
     * 借款周期,单位:月
     */
    public int F08;

    /** 
     * 状态,SQZ:申请中;DSH:待审核;DFB:待发布;YFB:预发布;TBZ:投资中;DFK:待放款;HKZ:还款中;YJQ:已结清;YLB:已流标;YDF:已垫付;
     */
    public T6230_F20 F09;

    /** 
     * 发布时间,预发布状态有效
     */
    public Timestamp F10;

    /** 
     * 信用等级,参考T5124.F01
     */
    public int F11;

    /** 
     * 申请时间
     */
    public Timestamp F12;

    /** 
     * 购买价格
     */
    public BigDecimal F13 = new BigDecimal(0);

    /** 
     * 投资时间
     */
    public Timestamp F14;
    
    /** 
     * 借款标Id
     */
    public int F15;
    /** 
     * 标编号
     */
    public String F16;
    
	/**
	 * 剩余时间
	 */
	public String surTime;
	/**
	 * 审核时间
	 */
	public Date shTime;
	
	/**
	 * 是否为按天借款,S:是;F:否
	 */
	public T6231_F21 F21;
	/**
	 * 借款天数
	 */
	public int F22;
	
	/** 
     * 加息利
     */
    public BigDecimal jxl = new BigDecimal(0);

}
