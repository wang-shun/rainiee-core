package com.dimeng.p2p.modules.bid.front.service.query;

/**
 * 标查询接口-贷款金额范围
 * 新手投资：5000-10000 资深投资：10000-30000 vip投资：30000以上
 */
public interface QyBidQueryAccount extends QyBidQuery{
    
	/**
	 * 贷款金额范围
	 * 
	 */
	 String getWay();
}
