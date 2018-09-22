package com.dimeng.p2p.modules.bid.pay.service;

import java.math.BigDecimal;

import com.dimeng.framework.service.Service;

/**
 * 优选理财
 */
public abstract interface FinancialPurchaseManage extends Service {
	/**
	 * 添加优选理财投资订单
	 * 
	 * @param lcId
	 *            优选理财id
	 * @param amount
	 *            投资金额
	 * @throws Throwable
	 */
	public abstract int addOrder(int lcId, BigDecimal amount) throws Throwable;

	/**
	 * 添加优选理财放款订单
	 * 
	 * 
	 * @throws Throwable
	 */
	public abstract int[] addFkOrder() throws Throwable;

	/**
	 * 添加优选理财还款订单
	 * 
	 * 
	 * @throws Throwable
	 */
	public abstract int[] addHkOrder() throws Throwable;
}
