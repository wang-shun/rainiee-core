package com.dimeng.p2p.modules.account.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S71.entities.T7101;
import com.dimeng.p2p.modules.account.console.service.entity.Ptgl;

/**
 * 平台管理
 */
public interface PtglManage extends Service {
	/**
	 * 查询平台管理信息
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract T7101 search() throws Throwable;
	
	/**
	 * 根据Id得到平台管理信息
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract T7101 get(int id) throws Throwable;
	/**
	 * 修改平台管理信息
	 * 
	 * @param t7101
	 * @throws Throwable
	 */
	public abstract void update(Ptgl ptgl) throws Throwable;
}
