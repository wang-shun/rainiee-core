package com.dimeng.p2p.modules.financial.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 待转让的债权
 *
 */
public class TransferDshEntity {
	/** 
     * 自增ID
     */
    public int F01;
    
	/**
	 * 债权ID
	 */
	public String F02;
	
	/**
	 * 债权转让者
	 */
	public String F03;
	
	/** 
     * 总期数
     */
    public int F04;
    
    /** 
     * 剩余期数
     */
    public int F05;
    
    /** 
     * 年化利率
     */
    public BigDecimal F06 = BigDecimal.ZERO;
    
    /** 
     * 债权价值
     */
    public BigDecimal F07 = BigDecimal.ZERO;
	
    /** 
     * 转让价格
     */
    public BigDecimal F08 = BigDecimal.ZERO;
    
    /** 
     * 转让费率
     */
    public BigDecimal F09 = BigDecimal.ZERO;
    
    /** 
     * 申请时间
     */
    public Timestamp F10;
    
    /**
     * 转让前的债权价值
     */
    public BigDecimal F11 = BigDecimal.ZERO;
    
    /**
     * 标的ID
     */
    public int F12;
    
    /**
     * 借款标题
     */
    public String F13;
    
    /**
     * 标的编号
     */
    public String F14;

}
