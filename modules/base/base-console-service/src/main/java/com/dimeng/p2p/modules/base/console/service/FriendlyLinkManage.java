package com.dimeng.p2p.modules.base.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.base.console.service.entity.FriendlyLink;
import com.dimeng.p2p.modules.base.console.service.entity.FriendlyLinkRecord;
import com.dimeng.p2p.modules.base.console.service.query.FriendlyLinkQuery;

/**
 * 友情链接管理.
 */
public interface FriendlyLinkManage extends Service {
	/**
	 * <dt>
	 * <dl>
	 * 描述：分页查询友情链接列表.
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
	 * <li>查询{@link T5014}表{@link T7110}表</li>
	 * <li>查询条件:
	 * <ol>
	 * <li>{@code T5014.F06 = T7110.F01}</li>
	 * <li>如果{@code query != null} 则
	 * <ol>
	 * <li>如果{@link FriendlyLinkQuery#getName()}不为空  则{@code T5014.F04 LIKE }{@link FriendlyLinkQuery#getName()}</li>
	 * <li>如果{@link FriendlyLinkQuery#getPublisherName()}不为空  则{@code T7110.F04 LIKE }{@link FriendlyLinkQuery#getPublisherName()}</li>
	 * <li>如果{@link FriendlyLinkQuery#getCreateTimeStart()}不为空  则{@code DATE(T5014.F07) >= }{@link FriendlyLinkQuery#getCreateTimeStart()}</li>
	 * <li>如果{@link FriendlyLinkQuery#getCreateTimeEnd()}不为空  则{@code DATE(T5014.F07) <= }{@link FriendlyLinkQuery#getCreateTimeEnd()}</li>
	 * <li>如果{@link FriendlyLinkQuery#getUpdateTimeStart()}不为空  则{@code  DATE(T5014.F08) >=  }{@link FriendlyLinkQuery#getUpdateTimeStart()}</li>
	 * <li>如果{@link FriendlyLinkQuery#getUpdateTimeEnd()}不为空  则{@code  DATE(T5014.F08) <=  }{@link FriendlyLinkQuery#getUpdateTimeEnd()}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </li>
	 * <li>按照{@code T5014.F08}字段降序排序</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link FriendlyLinkRecord#id}对应{@code T5014.F01}</li>
	 * <li>{@link FriendlyLinkRecord#viewTimes}对应{@code T5014.F02}</li>
	 * <li>{@link FriendlyLinkRecord#sortIndex}对应{@code T5014.F03}</li>
	 * <li>{@link FriendlyLinkRecord#name}对应{@code T5014.F04}</li>
	 * <li>{@link FriendlyLinkRecord#url}对应{@code T5014.F05}</li>
	 * <li>{@link FriendlyLinkRecord#publisherId}对应{@code T5014.F06}</li>
	 * <li>{@link FriendlyLinkRecord#createTime}对应{@code T5014.F07}</li>
	 * <li>{@link FriendlyLinkRecord#updateTime}对应{@code T5014.F08}</li>
	 * <li>{@link FriendlyLinkRecord#publisherName}对应{@code T7110.F04}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param query 查询条件
	 * @param paging 分页参数
	 * @return {@link PagingResult}{@code <}{@link FriendlyLinkRecord}{@code >} 分页查询结果,没有结果则返回{@code null}
	 * @throws Throwable
	 */
	public abstract PagingResult<FriendlyLinkRecord> search(
			FriendlyLinkQuery query, Paging paging) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据id查询友情链接信息.
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
	 * <li>查询{@link T5014}表{@link T7110}表  查询条件:{@code T5014.F06 = T7110.F01 AND T5014.F01 = id}</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link FriendlyLinkRecord#id}对应{@code T5014.F01}</li>
	 * <li>{@link FriendlyLinkRecord#viewTimes}对应{@code T5014.F02}</li>
	 * <li>{@link FriendlyLinkRecord#sortIndex}对应{@code T5014.F03}</li>
	 * <li>{@link FriendlyLinkRecord#name}对应{@code T5014.F04}</li>
	 * <li>{@link FriendlyLinkRecord#url}对应{@code T5014.F05}</li>
	 * <li>{@link FriendlyLinkRecord#publisherId}对应{@code T5014.F06}</li>
	 * <li>{@link FriendlyLinkRecord#createTime}对应{@code T5014.F07}</li>
	 * <li>{@link FriendlyLinkRecord#updateTime}对应{@code T5014.F08}</li>
	 * <li>{@link FriendlyLinkRecord#publisherName}对应{@code T7110.F04}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * @param id 友情链接id
	 * @return {@link FriendlyLinkRecord}  查询结果  不存在则返回{@code null}
	 * @throws Throwable
	 */
	public abstract FriendlyLinkRecord get(int id) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据id批量删除友情链接信息.
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
	 * <li>批量删除{@link T5014}表  查询条件:{@code T5014.F01 = ids}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param ids 友情链接id列表
	 * @throws Throwable
	 */
	public abstract void delete(int... ids) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：添加友情链接信息.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code friendlyLink == null} 则抛出参数异常  没有指定友情链接信息 </li>
	 * <li>如果{@link FriendlyLink#getName() }为空 则抛出参数异常  名称不能为空  </li>
	 * <li>如果{@link FriendlyLink#getURL() }为空 则抛出参数异常 链接地址不能为空 </li>
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
	 * <li>新增{@link T5014}表   
	 * <li>
	 * 新增字段列表:
	 * <ol>
	 * <li>{@link FriendlyLink#getSortIndex()}对应{@code T5014.F03}</li>
	 * <li>{@link FriendlyLink#getName()}对应{@code T5014.F04}</li>
	 * <li>{@link FriendlyLink#getURL()}对应{@code T5014.F05}</li>
	 * <li>当前登录系统的ID 对应{@code T5014.F06}</li>
	 * <li>当前系统时间 对应{@code T5014.F07}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param friendlyLink 友情链接信息
	 * @return {@link int} 新增的友情链接ID
	 * @throws Throwable
	 */
	public abstract int add(FriendlyLink friendlyLink) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据id修改友情链接信息.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code  friendlyLink == null}或则 {@code  id <= 0}则直接返回 </li>
	 * <li>如果{@link FriendlyLink#getName() }为空 则抛出参数异常  名称不能为空  </li>
	 * <li>如果{@link FriendlyLink#getURL() }为空 则抛出参数异常  链接地址不能为空  </li>
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
	 * <li>修改{@link T5014}表   修改条件：{@code T5014.F01 = id}<li>
	 * 修改字段列表:
	 * <ol>
	 * <li>{@link FriendlyLink#getSortIndex()}对应{@code T5014.F03}</li>
	 * <li>{@link FriendlyLink#getName()}对应{@code T5014.F04}</li>
	 * <li>{@link FriendlyLink#getURL()}对应{@code T5014.F05}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id 友情链接id
	 * @param friendlyLink 友情链接信息
	 * @throws Throwable
	 */
	public abstract void update(int id, FriendlyLink friendlyLink)
			throws Throwable;
	
	/** <一句话功能简述>
     * 批量更新友情链接排序值
     * @param ids
     * @param order
     * @return
     * @throws Throwable
     */
    public abstract void updateBatchOrder(String ids,int order)throws Throwable;
}
