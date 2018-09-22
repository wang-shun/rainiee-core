/**
 * 
 */
package com.dimeng.p2p.modules.account.console.service;

import java.io.OutputStream;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S65.entities.T6550;
import com.dimeng.p2p.modules.account.console.service.entity.Order;
import com.dimeng.p2p.modules.account.console.service.entity.OrderException;
import com.dimeng.p2p.modules.account.console.service.query.OrderExceptionQuery;
import com.dimeng.p2p.modules.account.console.service.query.OrderQuery;

/**
 * 订单查询
 * 
 * @author guopeng
 * 
 */
public abstract interface OrderManager extends Service {
	/**
	 * 
	 * <dl>
	 * 描述：查询订单列表.
	 * </dl>
	 * 
	 * 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<Order> search(OrderQuery query, Paging paging)
			throws Throwable;

	/**
	 * 查询订单异常信息
	 */
	public abstract PagingResult<OrderException> search(
			OrderExceptionQuery query, Paging paging) throws Throwable;

	/**
	 * 查询订单异常详细
	 */
	public abstract T6550 get(int id) throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：订单导出.
	 * </dl>
	 * 
	 * 
	 * @param paramArrayOfYFundRecord
	 * @param outputStream
	 * @param charset
	 * @throws Throwable
	 */
	public abstract void export(Order[] orders, OutputStream outputStream,
			String charset) throws Throwable;
	
	
}
