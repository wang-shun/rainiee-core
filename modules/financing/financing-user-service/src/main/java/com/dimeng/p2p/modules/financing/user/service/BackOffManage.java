package com.dimeng.p2p.modules.financing.user.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.financing.user.service.entity.BackOff;
import com.dimeng.p2p.modules.financing.user.service.entity.BackOffList;
import com.dimeng.p2p.modules.financing.user.service.query.BackOffQuery;
/**
 * 回帐查询
 *
 */
public interface BackOffManage extends Service{

	/**
	 * 查询待收本息
	 * @return BackOff
	 */
	public abstract BackOff searchTotle() throws Throwable;
	/**
	 * 查询列表信息
	 * @param backOffQuery
	 * @return PagingResult<BackOffList>
	 */
	public abstract PagingResult<BackOffList> searchList(BackOffQuery backOffQuery,Paging paging)throws Throwable;
}