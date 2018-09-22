package com.dimeng.p2p.modules.finance.console.service.query;

import com.dimeng.p2p.common.enums.CreditStatus;

public interface JgywmxQuery {

	/**
	 * 借款标题， 模糊查询
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract String getTitle();

	/**
	 * 借款账户， 模糊查询
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract String getAccount();

	/**
	 * 状态， 匹配查询
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract CreditStatus getStatus();

	/**
	 * 机构ID
	 * 
	 * @return
	 */
	public abstract int getId();

}
