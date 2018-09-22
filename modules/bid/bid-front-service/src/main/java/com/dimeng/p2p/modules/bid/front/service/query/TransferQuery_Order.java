package com.dimeng.p2p.modules.bid.front.service.query;

import com.dimeng.p2p.common.enums.CreditStatus;


/**
 * 债权查询接口
 */
public interface TransferQuery_Order extends TransferQuery{

	/**
	 * 排序字段
	 * 
	 * @return {@link CreditStatus}
	 */
	public abstract int getOrder();
}
