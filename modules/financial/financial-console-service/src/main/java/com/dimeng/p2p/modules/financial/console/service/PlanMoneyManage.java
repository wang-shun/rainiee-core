package com.dimeng.p2p.modules.financial.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.financial.console.service.entity.PlanAddRecord;
import com.dimeng.p2p.modules.financial.console.service.entity.PlanMoney;
import com.dimeng.p2p.modules.financial.console.service.entity.PlanMoneyAdd;
import com.dimeng.p2p.modules.financial.console.service.entity.PlanMoneyView;
import com.dimeng.p2p.modules.financial.console.service.query.PlanMoneyQuery;

public interface PlanMoneyManage extends Service {
	/**
	 * <dt>
	 * <dl>
	 * 描述：查询理财人的理财计划列表
	 * </dl>
	 * </dt>
	 * 
	 * @param planMoneyQuery
	 *            查询信息
	 * @param paging
	 *            分页信息
	 * @return 理财计划列表信息
	 */
	public abstract PagingResult<PlanMoney> planMoneySearch(
			PlanMoneyQuery planMoneyQuery, Paging paging) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：新增理财计划信息
	 * </dl>
	 * </dt>
	 * 
	 * @param planMoneyAdd
	 *            新增信息
	 */
	public abstract void addPlanMoney(PlanMoneyAdd planMoneyAdd)
			throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：理财计划详情信息
	 * </dl>
	 * </dt>
	 * 
	 * @param planMoneyId
	 *            理财计划ID
	 * @throws Throwable
	 */
	public abstract PlanMoneyView findPlanMoneyView(int planMoneyId)
			throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：查询计划加入记录
	 * </dl>
	 * </dt>
	 * 
	 * @param planMoneyId
	 *            理财计划ID
	 * @return 计划加入记录列表信息
	 */
	public abstract PagingResult<PlanAddRecord> addRecord(int planMoneyId,
			Paging paging) throws Throwable;

	/**
	 * 检验待申请、申请中的优选理财是否存在
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract boolean isExist() throws Throwable;
}
