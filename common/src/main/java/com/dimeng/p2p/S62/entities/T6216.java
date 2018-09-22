package com.dimeng.p2p.S62.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6216_F04;
import com.dimeng.p2p.S62.enums.T6216_F18;

import java.math.BigDecimal;

/** 
 * 标产品信息
 */
public class T6216 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /**
	 * 自增ID
	 */
	public int F01;

	/**
	 * 产品名称
	 */
	public String F02;

	/**
	 * 标类型
	 */
	public String F03;

	/**
	 * 状态
	 */
	public T6216_F04 F04;

	 /** 
     * 最低借款金额
     */
    public BigDecimal F05 = BigDecimal.ZERO;

    /** 
     * 最高借款金额
     */
    public BigDecimal F06 = BigDecimal.ZERO;

    /** 
     * 最低借款期限
     */
    public int F07;
    
    /** 
     * 最高借款期限
     */
    public int F08;
    
    /** 
     * 最低年化利率
     */
    public BigDecimal F09 = BigDecimal.ZERO;

    /** 
     * 最高年化利率
     */
    public BigDecimal F10 = BigDecimal.ZERO;
    
   
    /**
	 * 还款方式
	 */
	public String F11;
	
	/** 
     * 成交服务费率
     */
    public BigDecimal F12 = BigDecimal.ZERO;
    
    /** 
     * 投资管理费率
     */
    public BigDecimal F13 = BigDecimal.ZERO;
    
    /** 
     * 预期罚息利率
     */
    public BigDecimal F14 = BigDecimal.ZERO;
    
    /** 
     * 起投金额
     */
    public BigDecimal F15 = BigDecimal.ZERO;
    
    /** 
     * 最低借款期限（按天）
     */
    public int F16;
    
    /** 
     * 最高借款期限（按天）
     */
    public int F17;

    /**
     * 标类型
     */
    public String bidType;

    /**
     *投资限制
     */
    public T6216_F18 F18;
}
