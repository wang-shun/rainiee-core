package com.dimeng.p2p.modules.bid.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.S62.entities.T6230;

/**
 * 放款成交记录
 * 
 * @author guopeng
 * 
 */
public class CjRecord extends T6230 {

	private static final long serialVersionUID = 1L;
	/**
	 * 借款账号
	 */
	public String accountName;
	/**
	 * 放款时间
	 */
	public Timestamp fkTime;
	/**
	 * 借款期限
	 */
	public int days;
	/**
	 * 放款人
	 */
	public String fkName;
    
    /**
     * 体验金金额
     */
    public BigDecimal experAmount = BigDecimal.ZERO;

	/**
	 * 红包金额
	 *
	 */
	public BigDecimal hbAmount = BigDecimal.ZERO;
}
