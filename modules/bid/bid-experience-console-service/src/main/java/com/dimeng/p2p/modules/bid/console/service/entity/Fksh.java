package com.dimeng.p2p.modules.bid.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.enums.T6231_F21;

public class Fksh extends T6230 {

	private static final long serialVersionUID = 1L;

	/**
	 * 还款总期数
	 */
	public int days;
	/**
	 * 满标时间
	 */
	public Timestamp mbTime;
	/**
	 * 用户名
	 */
	public String userName;
	/**
	 * 是否为按天借款,S:是;F:否
	 */
	public T6231_F21 t6231_F21;
	
	/**
	 * 借款天数
	 */
	public int limitDays;
    
    /**
     * 体验金投资金额
     */
    public BigDecimal experAmount = BigDecimal.ZERO;

	/**
	 * 红包金额
	 *
	 */

	public BigDecimal hbAmount = BigDecimal.ZERO;
}
