package com.dimeng.p2p.modules.base.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.enums.NoticePublishStatus;
import com.dimeng.p2p.modules.base.console.service.entity.Notice;
import com.dimeng.p2p.modules.base.console.service.entity.NoticeRecord;
import com.dimeng.p2p.modules.base.console.service.query.NoticeQuery;

/**
 * 公告管理.
 * 
 */
public interface NoticeManage extends Service {
	/**
	 * <dt>
	 * <dl>
	 * 描述：分页查询公告信息列表.
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
	 * <li>查询{@link T5015}表{@link T7110}表</li>
	 * <li>查询条件:
	 * <ol>
	 * <li>{@code T5015.F07 = T7110.F01}</li>
	 * <li>如果{@code query != null} 则
	 * <ol>
	 * <li>如果{@link NoticeQuery#getType()}不为空  则{@code T5015.F02 = }{@link NoticeQuery#getType()}</li>
	 * <li>如果{@link NoticeQuery#getPublishStatus()}不为空  则{@code T5015.F04 = }{@link NoticeQuery#getPublishStatus()}</li>
	 * <li>如果{@link NoticeQuery#getTitle()}不为空  则{@code T5015.F05 LIKE }{@link NoticeQuery#getTitle()}</li>
	 * <li>如果{@link NoticeQuery#getPublisherName()}不为空  则{@code T7110.F04 LIKE }{@link NoticeQuery#getPublisherName()}</li>
	 * <li>如果{@link NoticeQuery#getCreateTimeStart()}不为空  则{@code  DATE(T5015.F08) >= }{@link NoticeQuery#getCreateTimeStart()}</li>
	 * <li>如果{@link NoticeQuery#getCreateTimeEnd()}不为空  则{@code  DATE(T5015.F08) <= }{@link NoticeQuery#getCreateTimeEnd()}</li>
	 * <li>如果{@link NoticeQuery#getUpdateTimeStart()}不为空  则{@code  DATE(T5015.F09) >= }{@link NoticeQuery#getUpdateTimeStart()}</li>
	 * <li>如果{@link NoticeQuery#getUpdateTimeEnd()}不为空  则{@code  DATE(T5015.F09) <= }{@link NoticeQuery#getUpdateTimeEnd()}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </li>
	 * <li>按照{@code T5015.F09}字段降序排序</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link NoticeRecord#id}对应{@code T5015.F01}</li>
	 * <li>{@link NoticeRecord#type}对应{@code T5015.F02}</li>
	 * <li>{@link NoticeRecord#viewTimes}对应{@code T5015.F03}</li>
	 * <li>{@link NoticeRecord#publishStatus}对应{@code T5015.F04}</li>
	 * <li>{@link NoticeRecord#title}对应{@code T5015.F05}</li>
	 * <li>{@link NoticeRecord#content}对应{@code T5015.F06}</li>
	 * <li>{@link NoticeRecord#publisherId}对应{@code T5015.F07}</li>
	 * <li>{@link NoticeRecord#createTime}对应{@code T5015.F08}</li>
	 * <li>{@link NoticeRecord#updateTime}对应{@code T5015.F09}</li>
	 * <li>{@link NoticeRecord#publisherName}对应{@code T7110.F04}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * @param query 查询条件 
	 * @param paging 分页参数
	 * @return {@link PagingResult}{@code <}{@link NoticeRecord}{@code >} 分页查询结果,没有结果则返回{@code null}
	 * @throws Throwable
	 */
	public abstract PagingResult<NoticeRecord> search(NoticeQuery query,
			Paging paging) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据id查询公告信息.
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
	 * <li>查询{@link T5015}表{@link T7110}表  查询条件:{@code T5015.F07 = T7110.F01 AND T5015.F01 = id}</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link NoticeRecord#id}对应{@code T5015.F01}</li>
	 * <li>{@link NoticeRecord#type}对应{@code T5015.F02}</li>
	 * <li>{@link NoticeRecord#viewTimes}对应{@code T5015.F03}</li>
	 * <li>{@link NoticeRecord#publishStatus}对应{@code T5015.F04}</li>
	 * <li>{@link NoticeRecord#title}对应{@code T5015.F05}</li>
	 * <li>{@link NoticeRecord#content}对应{@code T5015.F06}</li>
	 * <li>{@link NoticeRecord#publisherId}对应{@code T5015.F07}</li>
	 * <li>{@link NoticeRecord#createTime}对应{@code T5015.F08}</li>
	 * <li>{@link NoticeRecord#updateTime}对应{@code T5015.F09}</li>
	 * <li>{@link NoticeRecord#publisherName}对应{@code T7110.F04}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id 公告ID
	 * @return {@link NoticeRecord} 查询结果 不存在则返回 {@code null}
	 * @throws Throwable
	 */
	public abstract NoticeRecord get(int id) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据id批量删除公告信息.
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
	 * <li>批量删除{@link T5015}表  查询条件:{@code T5015.F01 = ids}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param ids 公告ID列表
	 * @throws Throwable
	 */
	public abstract void delete(int... ids) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：添加公告信息.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code notice == null} 则抛出参数异常  没有指定公告信息 </li>
	 * <li>如果{@link Notice#getTitle() }为空 则抛出参数异常  公告标题不能为空  </li>
	 * <li>如果{@link Notice#getContent() }为空 则抛出参数异常 公告内容不能为空 </li>
	 * <li>如果{@link Notice#getType() }为空 则抛出参数异常 公告类型不能为空 </li>
	 * <li>如果{@link Notice#getPublishStatus() }为空 则抛出参数异常 公告发布状态不能为空 </li>
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
	 * <li>新增{@link T5015}表   
	 * <li>
	 * 新增字段列表:
	 * <ol>
	 * <li>{@link Notice#getType()}对应{@code T5015.F02}</li>
	 * <li>{@link Notice#getPublishStatus()}对应{@code T5015.F04}</li>
	 * <li>{@link Notice#getTitle()}对应{@code T5015.F05}</li>
	 * <li>{@link Notice#getContent()} 对应{@code T5015.F06}</li>
	 * <li>当前系统登录ID 对应{@code T5015.F07}</li>
	 * <li>当前系统时间 对应{@code T5015.F08}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param notice 公告信息
	 * @return {@link int} 新增的公告ID
	 * @throws Throwable
	 */
	public abstract int add(Notice notice) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据id修改公告信息.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code  notice == null}或则 {@code  id <= 0}则直接返回 </li>
	 * <li>如果{@link Notice#getTitle() }为空 则抛出参数异常   公告标题不能为空 </li>
	 * <li>如果{@link Notice#getContent() }为空 则抛出参数异常   公告内容不能为空 </li>
	 * <li>如果{@link Notice#getType() }为空 则抛出参数异常  公告类型不能为空</li>
	 * <li>如果{@link Notice#getPublishStatus() }为空 则抛出参数异常  公告发布状态不能为空</li>
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
	 * <li>修改{@link T5015}表   修改条件：{@code T5015.F01 = id}<li>
	 * 修改字段列表:
	 * <ol>
	 * <li>{@link Notice#getType()}对应{@code T5015.F02}</li>
	 * <li>{@link Notice#getPublishStatus()}对应{@code T5015.F04}</li>
	 * <li>{@link Notice#getTitle()}对应{@code T5015.F05}</li>
	 * <li>{@link Notice#getContent()}对应{@code T5015.F06}</li>
	 * <li>当前系统时间 对应{@code T5015.F09}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id 公告ID
	 * @param notice 公告信息
	 * @throws Throwable
	 */
	public abstract void update(int id, Notice notice) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：设置公告为发布状态.
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
	 * <li>批量修改{@code T5015}表,修改条件:{@code T5015.F01 = ids }</li>
	 * <li>
	 * 修改字段列表:
	 * <ol>
	 * <li>{@link NoticePublishStatus#name()}对应{@code T5015.F04}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * @param publishStatus 发布状态
	 * @param ids 公告ID列表
	 * @throws Throwable
	 */
	public abstract void setPublishStatus(NoticePublishStatus publishStatus,
			int... ids) throws Throwable;
}
