package com.dimeng.p2p.modules.base.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.enums.PerformanceReportPublishStatus;
import com.dimeng.p2p.modules.base.console.service.entity.PerformanceReport;
import com.dimeng.p2p.modules.base.console.service.entity.PerformanceReportRecord;
import com.dimeng.p2p.modules.base.console.service.query.PerformanceReportQuery;

/**
 * 业绩报告管理.
 * 
 */
public interface PerformanceReportManage extends Service {
	/**
	 * <dt>
	 * <dl>
	 * 描述：分页查询业绩报告信息列表.
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
	 * <li>查询{@link T5018}表{@link T7110}表</li>
	 * <li>查询条件:
	 * <ol>
	 * <li>{@code T5018.F07 = T7110.F01}</li>
	 * <li>如果{@code query != null} 则
	 * <ol>
	 * <li>如果{@link PerformanceReportQuery#getTitle()}不为空  则{@code T5018.F05 LIKE }{@link PerformanceReportQuery#getTitle()}</li>
	 * <li>如果{@link PerformanceReportQuery#getPublisherName()}不为空  则{@code T7110.F04 LIKE }{@link PerformanceReportQuery#getPublisherName()}</li>
	 * <li>如果{@link PerformanceReportQuery#getCreateTimeStart()}不为空  则{@code DATE(T5018.F08) >= }{@link PerformanceReportQuery#getCreateTimeStart()}</li>
	 * <li>如果{@link PerformanceReportQuery#getCreateTimeEnd()}不为空  则{@code DATE(T5018.F08) <= }{@link PerformanceReportQuery#getCreateTimeEnd()}</li>
	 * <li>如果{@link PerformanceReportQuery#getUpdateTimeStart()}不为空  则{@code DATE(T5018.F09) >= }{@link PerformanceReportQuery#getUpdateTimeStart()}</li>
	 * <li>如果{@link PerformanceReportQuery#getUpdateTimeEnd()}不为空  则{@code  DATE(T5018.F09) <= }{@link PerformanceReportQuery#getUpdateTimeEnd()}</li>
	 * <li>如果{@link PerformanceReportQuery#getPublishStatus()}不为空  则{@code  T5018.F03 = }{@link PerformanceReportQuery#getPublishStatus()}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </li>
	 * <li>按照{@code T5018.F09}字段降序排序</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link PerformanceReportRecord#id}对应{@code T5018.F01}</li>
	 * <li>{@link PerformanceReportRecord#sortIndex}对应{@code T5018.F02}</li>
	 * <li>{@link PerformanceReportRecord#publishStatus}对应{@code T5018.F03}</li>
	 * <li>{@link PerformanceReportRecord#viewTimes}对应{@code T5018.F04}</li>
	 * <li>{@link PerformanceReportRecord#title}对应{@code T5018.F05}</li>
	 * <li>{@link PerformanceReportRecord#attachmentCode}对应{@code T5018.F06}</li>
	 * <li>{@link PerformanceReportRecord#publisherId}对应{@code T5018.F07}</li>
	 * <li>{@link PerformanceReportRecord#createTime}对应{@code T5018.F08}</li>
	 * <li>{@link PerformanceReportRecord#updateTime}对应{@code T5018.F09}</li>
	 * <li>{@link PerformanceReportRecord#publisherName}对应{@code T7110.F04}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param query 查询条件
	 * @param paging 分页参数
	 * @return {@link PagingResult}{@code <}{@link PerformanceReportRecord}{@code >} 分页查询结果,没有结果则返回{@code null}
	 * @throws Throwable
	 */
	public abstract PagingResult<PerformanceReportRecord> search(
			PerformanceReportQuery query, Paging paging) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据id查询业绩报告信息.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code id <= 0} 则直接返回 {@link null}</li>
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
	 * <li>查询{@link T5018}表{@link T7110}表  查询条件:{@code T5018.F07 = T7110.F01 AND T5018.F01 = id}</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link PerformanceReportRecord#id}对应{@code T5018.F01}</li>
	 * <li>{@link PerformanceReportRecord#sortIndex}对应{@code T5018.F02}</li>
	 * <li>{@link PerformanceReportRecord#publishStatus}对应{@code T5018.F03}</li>
	 * <li>{@link PerformanceReportRecord#viewTimes}对应{@code T5018.F04}</li>
	 * <li>{@link PerformanceReportRecord#title}对应{@code T5018.F05}</li>
	 * <li>{@link PerformanceReportRecord#attachmentCode}对应{@code T5018.F06}</li>
	 * <li>{@link PerformanceReportRecord#publisherId}对应{@code T5018.F07}</li>
	 * <li>{@link PerformanceReportRecord#createTime}对应{@code T5018.F08}</li>
	 * <li>{@link PerformanceReportRecord#updateTime}对应{@code T5018.F09}</li>
	 * <li>{@link PerformanceReportRecord#publisherName}对应{@code T7110.F04}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id 业绩报告ID
	 * @return {@link PerformanceReportRecord} 查询结果 不存在则返回 {@code null}
	 * @throws Throwable
	 */
	public abstract PerformanceReportRecord get(int id) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据id批量删除业绩报告信息.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code ids == null}或则{@code ids}的长度等于0  则直接返回 </li>
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
	 * <li>打开事务</li>
	 * <li>批量删除{@link T5018}表  查询条件:{@code T5018.F01 = ids}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param ids 业绩报告ID列表
	 * @throws Throwable
	 */
	public abstract void delete(int... ids) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：添加业绩报告信息.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code performanceReport == null} 则抛出参数异常  没有指定公告信息 </li>
	 * <li>如果{@link PerformanceReport#getPublishStatus() }为空 则{@link PerformanceReport#getPublishStatus() }{@code = }{@link PerformanceReportPublishStatus#WFB } </li>
	 * <li>如果{@link PerformanceReport#getTitle() }为空 则抛出参数异常  标题不能为空</li>
	 * <li>如果{@link PerformanceReport#getAttachment() }为空 则抛出参数异常  业绩报告必须上传附件</li>
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
	 * <li>{@link PerformanceReport#getAttachment() }上传文件  得到文件编码{@link String}</li>
	 * <li>新增{@link T5018}表 <li>
	 * 新增字段列表:
	 * <ol>
	 * <li>{@link PerformanceReport#getPublishStatus()}对应{@code T5018.F03}</li>
	 * <li>{@link PerformanceReport#getTitle()}对应{@code T5018.F05}</li>
	 * <li>文件编码{@link String}对应{@code T5018.F06}</li>
	 * <li>当前系统登录ID 对应{@code T5018.F07}</li>
	 * <li>当前系统时间 对应{@code T5018.F08}</li>
	 * </ol>
	 * 
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param performanceReport 业绩报告信息
	 * @return {@link int} 新增业绩报告ID
	 * @throws Throwable
	 */
	public abstract int add(PerformanceReport performanceReport)
			throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据id修改业绩报告信息.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code  notice == null}或则 {@code  id <= 0}则直接返回 </li>
	 * <li>如果{@link PerformanceReport#getTitle() }为空 则抛出参数异常   标题不能为空 </li>
	 * <li>如果{@link PerformanceReport#getPublishStatus() }为空 则{@link PerformanceReport#getPublishStatus() }{@code = }{@link PerformanceReportPublishStatus#WFB }</li>
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
	 * <li>如果{@link PerformanceReport#getAttachment()() }不为空  则上传得到 文件编码{@link String}</li>
	 * <li>修改{@code T5018}表,修改条件:{@code T5018.F01 = }{@code id}</li>
	 * <li>如果 文件编码{@link String}{@code == null} 则不修改{@code T5018.F06}字段 </li>
	 * <li>
	 * 修改字段列表:
	 * <ol>
	 * <li>{@link PerformanceReport#getPublishStatus()}对应{@code T5018.F03}</li>
	 * <li>{@link PerformanceReport#getTitle()}对应{@code T5018.F05}</li>
	 * <li>文件编码{@link String}对应{@code T5018.F06}</li>
	 * <li>当前系统时间对应{@code T5018.F09}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id 业绩报告ID
	 * @param performanceReport 业绩报告信息
	 * @throws Throwable
	 */
	public abstract void update(int id, PerformanceReport performanceReport)
			throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：设置业绩报告为发布状态.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code ids == null }或则{@code ids }长度为0  或则 {@code publishStatus == null } 则直接返回</li>
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
	 * <li>打开事务</li>
	 * <li>批量修改{@code T5018}表,修改条件:{@code T5018.F01 = ids }</li>
	 * <li>
	 * 修改字段列表:
	 * <ol>
	 * <li>{@link PerformanceReportPublishStatus#name()}对应{@code T5018.F03}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param publishStatus 发布状态
	 * @param ids 业绩报告ID列表
	 * @throws Throwable
	 */
	public abstract void setPublishStatus(
			PerformanceReportPublishStatus publishStatus, int... ids)
			throws Throwable;
}
