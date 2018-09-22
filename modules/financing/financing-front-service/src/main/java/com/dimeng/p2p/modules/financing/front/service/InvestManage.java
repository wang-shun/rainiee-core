package com.dimeng.p2p.modules.financing.front.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.financing.front.service.entity.CreditInfo;
import com.dimeng.p2p.modules.financing.front.service.entity.InvestStatistics;
import com.dimeng.p2p.modules.financing.front.service.query.InvestQuery;

/**
 * 散标投资
 * 
 */
public interface InvestManage extends Service {

	/**
	 * 获取散标投资列表
	 * 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<CreditInfo> search(InvestQuery query,
			Paging paging) throws Throwable;

	/**
	 * 获取投资统计信息.
	 * 
	 * @return {@link InvestStatistics}
	 * @throws Throwable
	 */
	public abstract InvestStatistics getStatistics() throws Throwable;

	/**
	 * 获取散标详细信息.
	 * 
	 * @param id
	 * @return {@link CreditInfo}
	 * @throws Throwable
	 */
	public abstract CreditInfo get(int id) throws Throwable;
	/**
	 * 查询机构名称
	 * @param jkbId
	 * @return
	 * @throws Throwable
	 */
	public String getJgName(int jkbId)throws Throwable;
}
