package com.dimeng.p2p.modules.financing.user.service;

import java.math.BigDecimal;

import com.dimeng.framework.service.Service;

/**
 *优选理财购买流程 
 *
 */
public interface CreditBestManage extends Service{
	
	/**
	* <dt>
	* <dl>
	* 描述：投资的整个操作流程
	* </dl>
	*
	 * <dl>
	* 数据校验：
	* <ol>
	* <li>优选理财ID不能为空</li>
	* <li>购买金额大于0</li>
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
	* <li>优选理财持有人表,新增一条记录（如存在就执行update）</li>
	* <li>用户资金交易记录表, 新增一条记录</li>
	* <li>更新优选理财计划表（余额）</li>
	* <li>判断是否满额（
	* 				1. 修改用户账户T6023,冻结金额=冻结金额-投资金额,余额=余额-投资金额
	*				2. 增加用户资金交易记录T6032
	*				3. 修改T6042:状态为已生效,更新(计算)锁定结束日期,满额用时,满额时间,下一还款日
	*				4. 生成优选理财还款记录T6044
	*				5. 增加(如果存在加入)平台资金和资金流水记录T7025,T7026
	*				6. 更新用户优选理财统计表T6026）
	*</li>
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
	public abstract void BidBest(int yxlcId, BigDecimal tbMoney) throws Throwable;

}