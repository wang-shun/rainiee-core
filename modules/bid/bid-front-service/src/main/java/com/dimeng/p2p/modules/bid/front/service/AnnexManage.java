package com.dimeng.p2p.modules.bid.front.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S62.entities.T6232;
import com.dimeng.p2p.S62.entities.T6233;

/**
 * 标的附件管理
 * 
 * 
 */
public interface AnnexManage extends Service {

	/**
	 * 查询非公开详情
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract T6233 getFgk(int id) throws Throwable;

	/**
	 * 查询公开详情
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract T6232 getGk(int id) throws Throwable;

}
