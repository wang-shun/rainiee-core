/**
 * 
 */
package com.dimeng.p2p.modules.account.console.service.query;

import java.sql.Timestamp;

/**
 * @author guopeng
 * 
 */
public abstract interface OrderExceptionQuery {
	/**
	 * 
	 * 描述：发生开始时间.
	 * 
	 * @return
	 */
	public abstract Timestamp getTimeStart();

	/**
	 * 
	 * 描述：发生结束时间.
	 * 
	 * @return
	 */
	public abstract Timestamp getTimeEnd();

	/**
	 * 订单号
	 */
	public abstract String getOrderId();

}
