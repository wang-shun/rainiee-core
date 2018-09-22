package com.dimeng.p2p.modules.financial.front.service;

import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S64.entities.T6411;
import com.dimeng.p2p.modules.financial.front.service.entity.YxlbEntity;
import com.dimeng.p2p.modules.financial.front.service.entity.YxlcCount;
import com.dimeng.p2p.modules.financial.front.service.entity.YxxqEntity;

public interface FinancialManage extends Service{

	/**
	 * 获取最新一期的优选理财
	 * @return
	 * @throws Throwable
	 */
	public abstract YxxqEntity getNewPlan() throws Throwable;
	/**
	 * 根据ID查询优选理财
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract YxxqEntity getPlan(int id) throws Throwable;
	/**
	 * 分页查询往期优选理财
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<YxlbEntity> search(Paging paging)
			throws Throwable;
	/**
	 * 查询优选理财统计
	 * @return
	 * @throws Throwable
	 */
	public abstract YxlcCount getStatistics()throws Throwable;
	/**
	 * 查询投资记录
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract T6411[] search(int id)  throws Throwable;
	/**
	 * 根据planID查询用户已投资该优选理财的金额.
	 * @param planId
	 * @return
	 * @throws Throwable
	 */
	public abstract BigDecimal tzMoney(int planId) throws Throwable;
	/**
	 * 根据ID得到当期优选理财加入人
	 * 
	 * @return
	 * @throws Throwable
	 */
	public int getJrrc(int id) throws Throwable;
}
