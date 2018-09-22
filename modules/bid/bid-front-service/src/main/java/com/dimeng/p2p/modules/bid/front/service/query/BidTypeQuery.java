package com.dimeng.p2p.modules.bid.front.service.query;

import java.util.Map;

import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F13;

/**
 * 标查询接口
 */
public interface BidTypeQuery extends QyBidQuery{
	
	/**
	 * 担保标
	 */
    public abstract T6230_F11 getDbTypeBid();
    
    /**
     * 抵押标
     */
    public abstract T6230_F13 getDyTypeBid();
    
    /**
     * 检索条件
     */
    public abstract Map<String,Object> bidConditionQry();
	
}
