package com.dimeng.p2p.modules.bid.user.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6230;

public interface JksqcxManage extends Service{
	/**
	 * <dt>
	 * <dl>
	 * 描述： 分页查询申请中的借款标
	 * </dl>
	 * </dt>
	 * 
	 * @return PagingResult<ApplyLoan> 返回申请中借款信息对象数组
	 */
	public abstract PagingResult<T6230> getApplyLoans(Paging paging)
			throws Throwable;
}
