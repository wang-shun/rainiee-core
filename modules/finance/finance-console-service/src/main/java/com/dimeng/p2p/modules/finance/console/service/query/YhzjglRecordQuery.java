package com.dimeng.p2p.modules.finance.console.service.query;

import com.dimeng.p2p.common.enums.UserType;


public abstract interface YhzjglRecordQuery {	
	
	/**
	 * 用户名， 模糊查询
	 * @return {@link String}空值无效
	 */
	public abstract String getLoginName();
	
	
	/**
	 * 姓名，  模糊查询
	 * @return {@link String}空值无效
	 */
	public abstract String getUserName();
	
	/**
	 * 用户类型， 匹配查询
	 * @return {@link String}空值无效
	 */
	public abstract UserType getUserType();

}
