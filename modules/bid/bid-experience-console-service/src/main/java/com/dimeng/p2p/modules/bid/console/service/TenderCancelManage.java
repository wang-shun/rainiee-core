package com.dimeng.p2p.modules.bid.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.modules.bid.console.service.entity.BidReturn;

public abstract interface TenderCancelManage extends Service {

	/**
	 * 后台流标
	 * 
	 * @param bidId
	 *            标id
	 * @return 订单id列表
	 * @throws Throwable
	 */
    public abstract BidReturn cancel(int bidId, String des)
        throws Throwable;
}
