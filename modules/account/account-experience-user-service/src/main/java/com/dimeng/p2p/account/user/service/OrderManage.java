package com.dimeng.p2p.account.user.service;

import java.sql.Timestamp;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S65.entities.T6501;
/**
 *订单接口
 */
public interface OrderManage extends Service {

	/**
	 * 订单列表
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<T6501> search(int type,Timestamp startTime,Timestamp endTime,Paging paging)throws Throwable;
}
