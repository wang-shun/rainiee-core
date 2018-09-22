package com.dimeng.p2p.modules.capital.user.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.capital.user.service.entity.Order;

public abstract interface UnpayManage extends Service {

	
	public abstract PagingResult<Order> search(Paging paging) throws Throwable;
}
