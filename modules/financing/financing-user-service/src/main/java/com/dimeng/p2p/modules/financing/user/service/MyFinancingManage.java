package com.dimeng.p2p.modules.financing.user.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.financing.user.service.entity.AssetsInfo;
import com.dimeng.p2p.modules.financing.user.service.entity.LoanAssests;
import com.dimeng.p2p.modules.financing.user.service.entity.OutAssests;
import com.dimeng.p2p.modules.financing.user.service.entity.RecoverAssests;
import com.dimeng.p2p.modules.financing.user.service.entity.SettleAssests;
/**
 * 我的债权接口
 *
 */
public abstract interface MyFinancingManage extends Service{
	/**
	 * 查询债权信息实体
	 * @return 
	 */
	public abstract AssetsInfo getAssetsInfo() throws Throwable;
	/**
	 * 分页查询回收中的债权
	 * @param paging
	 * @return
	 */
	public abstract PagingResult<RecoverAssests> getRecoverAssests(Paging paging) throws Throwable;
	/**
	 * 分页查询已结清的债权
	 * @param paging
	 * @return
	 */
	public abstract PagingResult<SettleAssests> getSettleAssests(Paging paging) throws Throwable; 
	/**
	 * 分页查询投资中的债权
	 * @param paging
	 * @return
	 */
	public abstract PagingResult<LoanAssests> getLoanAssests(Paging paging) throws Throwable; 
	/**
	 * 分页查询已转出的的债权
	 * @param paging
	 * @return
	 */
	public abstract PagingResult<OutAssests> getOutAssests(Paging paging) throws Throwable; 
	
}
