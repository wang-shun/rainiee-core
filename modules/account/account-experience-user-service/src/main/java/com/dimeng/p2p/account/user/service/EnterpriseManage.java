package com.dimeng.p2p.account.user.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.account.user.service.entity.Enterprise;

public abstract interface EnterpriseManage extends Service {

	/**
	 * 根据ID查询企业信息
	 * @param id
	 * @return
	 */
	public abstract Enterprise get(int id)throws Throwable;
}
