/**
 * 
 */
package com.dimeng.p2p.modules.account.console.service.query;

import java.sql.Timestamp;

/**
 * @author guopeng
 * 
 */
public abstract interface OrderQuery {
    /**
     * 
     * <dl>
     * 描述：订单ID.
     * </dl>
     * 
     * @return
     */
    public abstract String getOrderId();
	/**
	 * 
	 * <dl>
	 * 描述： 用户名.
	 * </dl>
	 * 
	 * @return
	 */
	public abstract String getUserName();

	/**
	 * 开始时间
	 */
	public abstract Timestamp getCreateStart();

	/**
	 * 结束时间
	 */
	public abstract Timestamp getEndStart();

	/**
	 * 类型
	 */
	public abstract int getType();
}
