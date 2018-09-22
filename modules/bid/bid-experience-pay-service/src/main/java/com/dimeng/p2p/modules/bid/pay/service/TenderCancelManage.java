package com.dimeng.p2p.modules.bid.pay.service;

import com.dimeng.framework.service.Service;

public abstract interface TenderCancelManage extends Service {
	/**
	 * 投资人主动投资取消
	 * 
	 * @param recordId
	 *            投资记录id
	 * @return 订单id
	 * @throws Throwable
	 */
	public abstract int cancel(int recordId) throws Throwable;
}
