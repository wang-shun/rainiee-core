package com.dimeng.p2p.modules.base.pay.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S65.entities.T6501;

/**
 * 订单管理
 * 
 */
public interface OrderManage extends Service {

	/**
	 * 获取一个待提交订单ID.
	 * 
	 * @return T6501 没有则返回null
	 * @throws Throwable
	 */
	public abstract T6501 getToSubmit() throws Throwable;
	/**
	 * 获取一个待提交订单ID.
	 * 
	 * @return T6501 没有则返回null
	 * @throws Throwable
	 */
	public abstract T6501 getToSubmit(OrderType orderType) throws Throwable;
}
