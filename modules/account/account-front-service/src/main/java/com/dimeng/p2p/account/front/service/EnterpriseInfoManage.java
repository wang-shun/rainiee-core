package com.dimeng.p2p.account.front.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S61.entities.T6163;
import com.dimeng.p2p.account.front.service.entity.Enterprise;

/**
 * 企业信息管理
 */
public interface EnterpriseInfoManage extends Service {

	/**
	 * 根据ID查询企业信息
	 * @param id
	 * @return
	 */
	public abstract Enterprise get(int userId)throws Throwable;
	/**
	 * 企业财务状况
	 * @return
	 * @throws Throwable
	 */
	public abstract T6163[] search(int userId)throws Throwable;
}
