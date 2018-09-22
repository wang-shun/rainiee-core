package com.dimeng.p2p.modules.financing.user.service;

import com.dimeng.framework.service.Service;

/**
 * 投资流程
 *
 */
public interface CreditBuyManage extends Service{
	
	/**
	* <dt>
	* <dl>
	* 描述：投资的整个操作流程
	* </dl>
	*
	 * <dl>
	* 数据校验：
	* <ol>
	* <li>借款标ID不能为空</li>
	* <li>用户购入份数大于0</li>
	* </ol>
	* </dl>
	*
	 * <dl>
	* 逻辑校验：
	* <ol>
	* <li>判断是否有逾期还款</li>
	* <li>购买人可用金额是不是大余购买金额</li>
	* <li>标的剩余金额是不是大于等于投资金额</li>
	* </ol>
	* </dl>
	*
	 * <dl>
	* 业务处理：
	* <ol>
	* <li>操作用户资金表，用户可用余额减少，冻结金额增加</li>
	* <li>操作原始投资表，新增一条记录（如存在就执行update）</li>
	* <li>操作借款标表，更新可投资余额</li>
	* <li>判断是否满标（满标更新借款表信息）</li>
	* </ol>
	* </dl>
	*
	 * <dl>
	* 返回结果说明：
	* <ol>
	* <li>无</li>
	* </ol>
	* </dl>
	* </dt>
	*
	* ${tags}
	*/
	public abstract void Bid(int loanId, long amount) throws Throwable;

}