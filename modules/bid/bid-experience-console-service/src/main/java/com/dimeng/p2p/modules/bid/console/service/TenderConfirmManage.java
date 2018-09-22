package com.dimeng.p2p.modules.bid.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.modules.bid.console.service.entity.BidReturn;

/**
 * 投资放款
 */
public abstract interface TenderConfirmManage extends Service {

	/**
	 * 后台批量生成放款订单.
	 * 
	 * @param bidId
	 *            标ID
	 * @param consoleAccountId
	 *            后台用户ID
	 * @return 放款订单ID列表
	 * @throws Throwable
	 */
    public abstract BidReturn confirm(int bidId)
        throws Throwable;
	
}
