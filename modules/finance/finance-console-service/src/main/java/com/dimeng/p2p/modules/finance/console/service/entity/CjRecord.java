package com.dimeng.p2p.modules.finance.console.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.CreditLevel;
import com.dimeng.p2p.common.enums.InvestType;

/**
 * 放款成交记录
 * 
 * @author guopeng
 * 
 */
public class CjRecord implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 借款ID
	 */
	public int id;
	/**
	 * 放款时间
	 */
	public Timestamp loanTime;
	/**
	 * 借款账号
	 */
	public String accountName;
	/**
	 * 信用等级
	 */
	public CreditLevel level;
	/**
	 * 借款类型
	 */
	public InvestType type;
	/**
	 * 借款金额
	 */
	public BigDecimal ammount=new BigDecimal(0);
	/**
	 * 借款期限
	 */
	public int day;
	/**
	 * 借款人所在地
	 */
	public String address;
	/**
	 * 放款人
	 */
	public String loanName;
}
