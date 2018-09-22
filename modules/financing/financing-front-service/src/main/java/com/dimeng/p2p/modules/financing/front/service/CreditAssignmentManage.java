package com.dimeng.p2p.modules.financing.front.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.enums.CreditStatus;
import com.dimeng.p2p.common.enums.OverdueStatus;
import com.dimeng.p2p.modules.financing.front.service.entity.CreditAssignment;
import com.dimeng.p2p.modules.financing.front.service.entity.CreditAssignmentCount;
import com.dimeng.p2p.modules.financing.front.service.query.CreditAssignmentQuery;

/**
 * 债权转让
 * 
 * 
 */
public interface CreditAssignmentManage extends Service {
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：分页查询债权转让列表.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 逻辑校验：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>查询{@code T6039}{@code T6036}表,查询条件:{@code T6039.F11 =}{@code T6039.F12 =}{@code T6036.F20 =}
	 * {@link TransferStatus#YX}{@link OverdueStatus#S}{@link CreditStatus#YFK}</li>
	 * <li>按照{@code T6039.F09}字段降序排序</li>
	 * <li>
	 * 查询字段列表
	 * <ol>
	 * <li>{@code T6036.F04}</li>
	 * <li>{@code T6036.F19}</li>
	 * <li>{@code T6036.F09}</li>
	 * <li>{@code T6036.F08}</li>
	 * <li>{@code T6036.F24}</li>
	 * <li>{@code T6036.F40}</li>
	 * <li>{@code T6039.F05}</li>
	 * <li>{@code T6039.F06}</li>
	 * <li>{@code T6039.F08}</li>
	 * <li>{@code T6039.F01}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 返回结果说明：
	 * <ol>
	 * <li>{@link CreditAssignment#title}对应{@code T6036.F04}</li>
	 * <li>{@link CreditAssignment#creditType}对应{@code T6036.F19}</li>
	 * <li>{@link CreditAssignment#rate}对应{@code T6036.F09}</li>
	 * <li>{@link CreditAssignment#jkqx}对应{@code T6036.F08}</li>
	 * <li>{@link CreditAssignment#syqx}对应{@code T6036.F24}</li>
	 * <li>{@link CreditAssignment#creditLevel}对应{@code T6036.F40}</li>
	 * <li>{@link CreditAssignment#zqjz}对应{@code T6039.F05}</li>
	 * <li>{@link CreditAssignment#zrjg}对应{@code T6039.F06}</li>
	 * <li>{@link CreditAssignment#syfs}对应{@code T6039.F08}</li>
	 * <li>{@link CreditAssignment#zcId}对应{@code T6039.F01}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param CreditAssignmentQuery
	 * @param Paging
	 *            
	 * @return {@link PagingResult<CreditAssignment>} 债权转让列表,如果不存在则返回{@code null}
	 * @throws Throwable
	 */
	public abstract PagingResult<CreditAssignment> getList(
			CreditAssignmentQuery creditAssignmentQuery, Paging paging) throws Throwable;
	
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：查询债权的累计总金额和总笔数.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 逻辑校验：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>查询{@code T6039}{@code T6040}表</li>
	 * <li>
	 * 查询字段列表
	 * <ol>
	 * <li>{@code T6040.F04}</li>
	 * <li>{@code T6039.F06}</li>
	 * <li>{@code T6039.F05}</li>
	 * <li>{@code T6040.F01}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 返回结果说明：
	 * <ol>
	 * <li>{@link CreditAssignmentCount#totleMoney}对应{@code SUM(T6040.F04*T6039.F06)}</li>
	 * <li>{@link CreditAssignmentCount#userEarnMoney}对应{@code SUM(T6040.F04*T6039.F05)}</li>
	 * <li>{@link CreditAssignmentCount#totleCount}对应{@code COUNT(T6040.F01)}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 *
	 * @return {@link CreditAssignmentCount} 查询债权的累计总金额和总笔数,如果不存在则返回{@code null}
	 * @throws Throwable
	 */
	public abstract CreditAssignmentCount getStatistics()throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：查询债权转让详情头部.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 逻辑校验：
	 * <ol>
	 * <li>如果{@code id<=0}则直接返回{@code null}</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>查询{@code T6039}{@code T6036}表,查询条件:{@code T6039.F01 =}{@code T6039.F12 =}{@code T6036.F20 =}
	 * {@link id}{@link OverdueStatus#S}{@link CreditStatus#YFK}</li>
	 * <li>
	 * 查询字段列表
	 * <ol>
	 * <li>{@code T6036.F04}</li>
	 * <li>{@code T6036.F19}</li>
	 * <li>{@code T6036.F09}</li>
	 * <li>{@code T6036.F08}</li>
	 * <li>{@code T6036.F24}</li>
	 * <li>{@code T6036.F40}</li>
	 * <li>{@code T6039.F05}</li>
	 * <li>{@code T6039.F06}</li>
	 * <li>{@code T6039.F08}</li>
	 * <li>{@code T6039.F01}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 返回结果说明：
	 * <ol>
	 * <li>{@link CreditAssignment#title}对应{@code T6036.F04}</li>
	 * <li>{@link CreditAssignment#creditType}对应{@code T6036.F19}</li>
	 * <li>{@link CreditAssignment#rate}对应{@code T6036.F09}</li>
	 * <li>{@link CreditAssignment#jkqx}对应{@code T6036.F08}</li>
	 * <li>{@link CreditAssignment#syqx}对应{@code T6036.F24}</li>
	 * <li>{@link CreditAssignment#creditLevel}对应{@code T6036.F40}</li>
	 * <li>{@link CreditAssignment#zqjz}对应{@code T6039.F05}</li>
	 * <li>{@link CreditAssignment#zrjg}对应{@code T6039.F06}</li>
	 * <li>{@link CreditAssignment#syfs}对应{@code T6039.F08}</li>
	 * <li>{@link CreditAssignment#zcId}对应{@code T6039.F01}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id	
	 *			转出表ID 	
	 * @return {@link CreditAssignment} 查询转让记录,如果不存在则返回{@code null}
	 * @throws Throwable
	 */
	public abstract CreditAssignment get(int id)throws Throwable;
}
