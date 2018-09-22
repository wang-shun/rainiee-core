package com.dimeng.p2p.modules.account.console.service.query;

import com.dimeng.p2p.common.enums.FundAccountType;

public abstract interface FundsQuery {
	/**
	 * 用户名， 模糊查询
	 * @return {@link String}空值无效
	 */
	public abstract String getLoginName();
	
	
	/**
	 * 资金账号
	 * @return {@link String}空值无效
	 */
	public abstract String getUserName();
	
	/**
	 * 用户类型， 匹配查询
	 * @return {@link String}空值无效
	 */
	public abstract FundAccountType getFundAccountType();
	
}
