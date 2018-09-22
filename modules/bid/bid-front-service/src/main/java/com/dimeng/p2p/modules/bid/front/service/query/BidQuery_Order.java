package com.dimeng.p2p.modules.bid.front.service.query;

import com.dimeng.p2p.common.enums.CreditStatus;

/**
 * 标查询接口
 */
public interface BidQuery_Order extends BidQuery{
	
	/**
	 * 排序字段
	 * 
	 * @return {@link CreditStatus}
	 */
	public abstract int getOrder();


}
