package com.dimeng.p2p.modules.financing.front.service.query;

import com.dimeng.p2p.common.enums.CreditLevel;
import com.dimeng.p2p.common.enums.CreditTerm;
import com.dimeng.p2p.common.enums.InvestType;


public abstract interface InvestQuery {
	/**
	 * 查询借款标类型.
	 * 
	 * @return {@link InvestType}
	 */
	public abstract InvestType[] getType();

	/**
	 * 借款期限
	 * 
	 * @return {@link CreditTerm}
	 */
	public abstract CreditTerm[] getTerm();

	/**
	 * 认证等级
	 * 
	 * @return {@link CreditLevel}
	 */
	public abstract CreditLevel[] getCreditLevel();
}
