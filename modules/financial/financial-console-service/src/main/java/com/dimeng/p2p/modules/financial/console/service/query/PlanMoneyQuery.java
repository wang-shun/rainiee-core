package com.dimeng.p2p.modules.financial.console.service.query;


/**
 * 计划查询信息
 * @author gongliang
 *
 */
public abstract interface PlanMoneyQuery {
	
	/**
	 * 计划名称，模糊查询.
	 * 
	 * @return {@link String}空值无效.
	 */
	public abstract String getPlanName();
	
	/**
	 * 状态(DSQ:待申请;SQZ:申请中;YME:已满额;YJZ:已截止),匹配查询.
	 * 
	 * @return {@link String}空值无效.
	 */
	public abstract String getState();
}
