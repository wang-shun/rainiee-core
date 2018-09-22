package com.dimeng.p2p.modules.base.front.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.entities.T5018;

/**
 * 业绩报告
 * 
 */
public interface PerformanceReportManage extends Service {

	/**
	 * <dt>
	 * <dl>
	 * 描述：分页查询已发布的业绩报告.
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
	 * <li>查询{@link T5018表},查询条件:{@link T5018.F03 = }
	 * {@link PerformanceReportPublishStatus#YFB}</li>
	 * <li>按照{@link T5018.F02}字段降序排序</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link T5018.F01}</li>
	 * <li>{@link T5018.F05}</li>
	 * <li>{@link T5018.F06}</li>
	 * <li>{@link T5018.F09}</li>
	 * </ol>
	 * </li>
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
	 * @param paging
	 *            分页参数
	 * @return {@link PagingResult}{@code <}{@link PerformanceReport}{@code >}
	 *         分页查询结果
	 * @throws Throwable
	 */
	public abstract PagingResult<T5018> search(Paging paging) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据业绩报告ID获取已发布业绩报告信息.
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
	 * <li>如果{@code id<=0}则返回{@code null}</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>查询{@link T5018表},查询条件:{@link T5018.F01 = id AND T5018.F03 = }
	 * {@link PerformanceReportPublishStatus#YFB}</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link T5018.F01}</li>
	 * <li>{@link T5018.F05}</li>
	 * <li>{@link T5018.F06}</li>
	 * <li>{@link T5018.F09}</li>
	 * </ol>
	 * </li>
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
	 * @param id
	 *            业绩报告ID
	 * @return {@link T5018} 业绩报告,不存在记录则返回{@code null}
	 * @throws Throwable
	 */
	public abstract T5018 get(int id) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：累加已发布业绩报告点击次数.
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
	 * <li>如果{@code id<=0}则直接返回</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>修改{@link T5018.F04=F04+1},修改条件:{@link T5018.F01 = id AND T5018.F03 =
	 * * } {@link PerformanceReportPublishStatus#YFB}</li>
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
	 * @param id
	 *            业绩报告ID
	 * @throws Throwable
	 */
	public abstract void view(int id) throws Throwable;
}
