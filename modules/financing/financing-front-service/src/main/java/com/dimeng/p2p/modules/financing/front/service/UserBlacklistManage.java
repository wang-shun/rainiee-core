package com.dimeng.p2p.modules.financing.front.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.financing.front.service.entity.UserBlack;

public interface UserBlacklistManage  extends Service {
	/**
	 * 黑名单列表
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<UserBlack> search(Paging paging) throws Throwable;

}
