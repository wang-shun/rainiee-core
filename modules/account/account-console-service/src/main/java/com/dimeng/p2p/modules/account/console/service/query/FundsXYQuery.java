package com.dimeng.p2p.modules.account.console.service.query;

import com.dimeng.p2p.modules.account.console.service.entity.FundXYAccountType;

public interface FundsXYQuery {
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
	public abstract FundXYAccountType getFundAccountType();
}
