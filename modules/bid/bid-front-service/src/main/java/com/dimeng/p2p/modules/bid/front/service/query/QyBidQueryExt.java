package com.dimeng.p2p.modules.bid.front.service.query;

import com.dimeng.p2p.S62.entities.T6211;

/**
 * 标查询接口
 */
public interface QyBidQueryExt extends QyBidQuery{
	
	/**
	 * 标类型.
	 */
	public abstract T6211[] getType();
	
}
