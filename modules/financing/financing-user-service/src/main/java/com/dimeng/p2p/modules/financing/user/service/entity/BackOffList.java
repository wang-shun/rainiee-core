package com.dimeng.p2p.modules.financing.user.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.CreditType;
import com.dimeng.p2p.common.enums.DsStatus;

public class BackOffList {

	/**
	 * 借款标ID
	 */
	public int jkbId;
	/**
	 * 债权ID
	 */
	public int assestsId;
	/**
	 * 借款人
	 */
	public String creditor;
	/**
	 * 类型详细
	 */
	public String typeDetail;
	/**
	 * 金额
	 */
	public BigDecimal money = new BigDecimal(0);
	/**
	 * 收款日期
	 */
	public Timestamp receiveDate;
	/**
	 * 标类型
	 */
	public CreditType creditType;
	/**
	 * 回款状态
	 */
	public DsStatus dsStatus;
}
