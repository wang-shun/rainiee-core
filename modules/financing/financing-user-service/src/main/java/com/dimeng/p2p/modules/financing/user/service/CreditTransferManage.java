package com.dimeng.p2p.modules.financing.user.service;

import com.dimeng.framework.service.Service;
/**
 *债权转让操作 
 *
 */
public interface CreditTransferManage extends Service{
	/**
	* <dt>
	* <dl>
	* 描述：债权转让的操作流程
	* </dl>
	*
	 * <dl>
	* 数据校验：
	* <ol>
	* <li>债权转出ID不能为空</li>
	* <li>份数大于0</li>
	* </ol>
	* </dl>
	*
	 * <dl>
	* 逻辑校验：
	* <ol>
	* <li>判断是否有逾期还款</li>
	* <li>购买人可用金额是不是大余购买金额</li>
	* <li>剩余份数是不是大于等于购买份数</li>
	* </ol>
	* </dl>
	*
	 * <dl>
	* 业务处理：
	* <ol>
	* <li>操作用户资金表，购买者扣除购买费（可用余额减少，余额减少）</li>
	* <li>用户资金交易记录新增一天记录（支出）</li>
	* <li>转让者余额和可用余额增加</li>
	* <li>转让者资金交易记录新增一天记录（收入）</li>
	* <li>债权转让转入表新增一条记录</li>
	* <li>转让人，购买人更新最新债权持有人表信息（有就更新没有就新增）</li>
	* <li>更新债权转让转出表信息（剩余份数）</li>
	* <li>转让者扣除转让费（可用余额减少，余额减少）</li>
	* <li>转让者资金交易记录新增一条记录（支出）</li>
	* <li>平台账户新增一条记录</li>
	* <li>平台平台资金流水表新增一天记录</li>
	* <li>判断是否转让完（如转让完 ，更新转让信息）</li>
	* <li>债权转让统计表更新用户信息（转出者和转入者）</li>
	* <li>更新债权统计表(转入人和转出人)T6025</li>
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
	public abstract void Transfer(int zqzcId, int amount) throws Throwable;
}
