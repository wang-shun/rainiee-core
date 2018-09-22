package com.dimeng.p2p.modules.bid.user.service.query;

import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.common.enums.CreditTerm;
import com.dimeng.p2p.common.enums.InvestType;

/**
 * 标查询接口
 */
public interface BidQuery {
	
	/**
	 * 查询借款标类型.
	 * 
	 * @return {@link InvestType}
	 */
	public abstract T6211[] getType();

	/**
	 * 借款期限
	 * 
	 * @return {@link CreditTerm}
	 */
	public abstract CreditTerm[] getTerm();

/*	*//**
	 * 认证等级
	 * 
	 * @return {@link CreditLevel}
	 *//*
	public abstract T5124[] getCreditLevel();*/
	/**
	 * 状态
	 * @return
	 */
	public abstract T6230_F20[] getStatus();
}
