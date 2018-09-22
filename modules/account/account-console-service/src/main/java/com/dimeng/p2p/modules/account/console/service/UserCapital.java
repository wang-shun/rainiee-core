package com.dimeng.p2p.modules.account.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.account.console.service.entity.YhzjglRecord;
import com.dimeng.p2p.modules.account.console.service.query.YhzjglRecordQuery;

/**
 * 用户资金管理
 * 
 */
public abstract interface UserCapital extends Service {
	/**
	 * 
	 * <dl>
	 * 描述：查询个人资金列表.
	 * </dl>
	 * 
	 * 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<YhzjglRecord> search(YhzjglRecordQuery query,
			Paging paging) throws Throwable;
}
