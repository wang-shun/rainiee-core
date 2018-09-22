package com.dimeng.p2p.modules.account.console.service.query;

import com.dimeng.p2p.common.enums.OrganizationType;

public interface JgfxbyjglQuery {

	/**
	 * 机构名称， 模糊查询
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract String getOrganizationName();

	/**
	 * 机构类型， 匹配查询
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract OrganizationType getType();

}
