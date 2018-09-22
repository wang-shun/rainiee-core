package com.dimeng.p2p.modules.bid.user.service.entity;

import com.dimeng.p2p.S62.entities.T6252;
import com.dimeng.p2p.common.enums.PaymentStatus;

/**
 * 还款详情信息实体类
 * 
 */
public class RepayLoanDetail extends T6252 {
	private static final long serialVersionUID = 1L;
	/**
	 * 还款信息
	 */
	public RepayInfo repayInfo;
	/**
	 * 状态
	 */
	public PaymentStatus paymentStatus;
	/**
	 * 科目名称
	 */
	public String typeName;
}
