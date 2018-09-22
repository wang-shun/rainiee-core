package com.dimeng.p2p.account.user.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6195_EXT;

/**
 * 投资建议管理.
 * 
 */
public interface TzjyManage extends Service {
	/**
	 * 查询投资建议列表
	 * @param userId
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<T6195_EXT> search(int userId ,Paging paging) throws Throwable;

	/**
	 * 新建投诉建议
	 * @param userId
	 * @throws Throwable
	 */
	public abstract void saveInfo (int userId, String content) throws Throwable;

	/**
	 * 获得指定用户当天的投诉建议数量
	 * @param userId
	 * @return
	 */
	public abstract int getAdvCount(int userId) throws Throwable;

}
