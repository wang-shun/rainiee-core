package com.dimeng.p2p.modules.base.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.entities.T5012;
import com.dimeng.p2p.S50.enums.T5012_F11;
import com.dimeng.p2p.modules.base.console.service.entity.CustomerService;
import com.dimeng.p2p.modules.base.console.service.entity.CustomerServiceRecord;
import com.dimeng.p2p.modules.base.console.service.query.CustomerServiceQuery;

/**
 * 在线客服管理.
 */
public abstract interface CustomerServiceManage extends Service {

	/**
	 * <dt>
	 * <dl>
	 * 描述：分页查询客服列表.
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
	 * <li>查询{@link T5012}表{@link T7110}表</li>
	 * <li>查询条件:
	 * <ol>
	 * <li>{@code T5012.F08 = T7110.F01}</li>
	 * <li>如果{@code query != null} 则
	 * <ol>
	 * <li>如果{@link CustomerServiceQuery#getName()}不为空  则{@code T5012.F05 LIKE }{@link CustomerServiceQuery#getName()}</li>
	 * <li>如果{@link CustomerServiceQuery#getPublisherName()}不为空  则{@code T7110.F04 LIKE  }{@link CustomerServiceQuery#getPublisherName()}</li>
	 * <li>如果{@link CustomerServiceQuery#getCreateTimeStart()}不为空  则{@code DATE(T5012.F09) >= }{@link CustomerServiceQuery#getCreateTimeStart()}</li>
	 * <li>如果{@link CustomerServiceQuery#getCreateTimeEnd()}不为空  则{@code DATE(T5012.F09) <= }{@link CustomerServiceQuery#getCreateTimeEnd()}</li>
	 * <li>如果{@link CustomerServiceQuery#getUpdateTimeStart()}不为空  则{@code  DATE(T5012.F10) >=  }{@link CustomerServiceQuery#getUpdateTimeStart()}</li>
	 * <li>如果{@link CustomerServiceQuery#getUpdateTimeEnd()}不为空  则{@code  DATE(T5012.F10) <=  }{@link CustomerServiceQuery#getUpdateTimeEnd()}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </li>
	 * <li>按照{@code T5012.F10}字段降序排序</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link CustomerServiceRecord#id}对应{@code T5012.F01}</li>
	 * <li>{@link CustomerServiceRecord#viewTimes}对应{@code T5012.F02}</li>
	 * <li>{@link CustomerServiceRecord#type}对应{@code T5012.F03}</li>
	 * <li>{@link CustomerServiceRecord#sortIndex}对应{@code T5012.F04}</li>
	 * <li>{@link CustomerServiceRecord#name}对应{@code T5012.F05}</li>
	 * <li>{@link CustomerServiceRecord#number}对应{@code T5012.F06}</li>
	 * <li>{@link CustomerServiceRecord#imageCode}对应{@code T5012.F07}</li>
	 * <li>{@link CustomerServiceRecord#publisherId}对应{@code T5012.F08}</li>
	 * <li>{@link CustomerServiceRecord#createTime}对应{@code T5012.F09}</li>
	 * <li>{@link CustomerServiceRecord#updateTime}对应{@code T5012.F10}</li>
	 * <li>{@link CustomerServiceRecord#publisherName}对应{@code T7110.F04}</li>
	 * 
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param query 查询条件
	 * @param paging 分页参数
	 * @return {@link PagingResult}{@code <}{@link CustomerServiceRecord}{@code >} 分页查询结果,没有结果则返回{@code null}
	 * @throws Throwable
	 */
	public abstract PagingResult<CustomerServiceRecord> search(
			CustomerServiceQuery query, Paging paging) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据id查询客服信息.
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
	 * <li>查询{@link T5012}表{@link T7110}表  查询条件:{@code T5012.F08 = T7110.F01 AND T5012.F01 = id}</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link CustomerServiceRecord#id}对应{@code T5012.F01}</li>
	 * <li>{@link CustomerServiceRecord#viewTimes}对应{@code T5012.F02}</li>
	 * <li>{@link CustomerServiceRecord#type}对应{@code T5012.F03}</li>
	 * <li>{@link CustomerServiceRecord#sortIndex}对应{@code T5012.F04}</li>
	 * <li>{@link CustomerServiceRecord#name}对应{@code T5012.F05}</li>
	 * <li>{@link CustomerServiceRecord#number}对应{@code T5012.F06}</li>
	 * <li>{@link CustomerServiceRecord#imageCode}对应{@code T5012.F07}</li>
	 * <li>{@link CustomerServiceRecord#publisherId}对应{@code T5012.F08}</li>
	 * <li>{@link CustomerServiceRecord#createTime}对应{@code T5012.F09}</li>
	 * <li>{@link CustomerServiceRecord#updateTime}对应{@code T5012.F10}</li>
	 * <li>{@link CustomerServiceRecord#publisherName}对应{@code T7110.F04}</li>
	 * 
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id 客服id
	 * @return {@link CustomerServiceRecord} 查询结果 不存在则返回{@code null}
	 * @throws Throwable
	 */
	public abstract CustomerServiceRecord get(int id) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据id批量删除客服信息.
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
	 * <li>批量删除{@link T5012}表  查询条件:{@code T5012.F01 = ids}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * @param ids 客服id列表
	 * @throws Throwable
	 */
	public abstract void delete(int... ids) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据id添加客服信息.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code customerService == null} 则抛出参数异常  没有指定客服信息 </li>
	 * <li>如果{@link CustomerService#getImage() }为空 则抛出参数异常  必须上传客服图片 </li>
	 * <li>如果{@link CustomerService#getName() }为空 则抛出参数异常  客服名称不能为空 </li>
	 * <li>如果{@link CustomerService#getNumber() }为空 则抛出参数异常 客服号不能为空 </li>
	 * <li>如果{@link CustomerService#getType() }为空 则抛出参数异常 客服类型不能为空 </li>
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
	 * <li>上传文件得到 文件编码{@link String}</li>
	 * <li>新增{@link T5012}表  
	 * <li>
	 * 新增字段列表:
	 * <ol>
	 * <li>{@link CustomerServiceRecord#type}对应{@code T5012.F03}</li>
	 * <li>{@link CustomerServiceRecord#sortIndex}对应{@code T5012.F04}</li>
	 * <li>{@link CustomerServiceRecord#name}对应{@code T5012.F05}</li>
	 * <li>{@link CustomerServiceRecord#number}对应{@code T5012.F06}</li>
	 * <li>文件编码{@link String} 对应{@code T5012.F07}</li>
	 * <li>当前登录系统id 对应{@code T5012.F08}</li>
	 * <li>当前系统时间 对应{@code T5012.F09}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * @param customerService 客服信息
	 * @return {@link int} 客服id
	 * @throws Throwable
	 */
	public abstract int add(CustomerService customerService) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据id修改客服信息.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code  customerService == null}或则 {@code  id <= 0}则直接返回 </li>
	 * <li>如果{@link CustomerService#getName() }为空 则抛出参数异常  客服名称不能为空 </li>
	 * <li>如果{@link CustomerService#getNumber() }为空 则抛出参数异常 客服号不能为空 </li>
	 * <li>如果{@link CustomerService#getType() }为空 则抛出参数异常 客服类型不能为空 </li>
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
	 * <li>如果{@link CustomerService#getImage() }不为空 上传文件得到 文件编码{@link String} 否则不上传</li>
	 * <li>修改{@link T5012}表   修改条件：{@code T5012.F01 = id} 如果 文件编码{@link String} 为{@code null} 则不修改{@code T5012.F07}
	 * <li>
	 * 修改字段列表:
	 * <ol>
	 * <li>{@link CustomerServiceRecord#type}对应{@code T5012.F03}</li>
	 * <li>{@link CustomerServiceRecord#sortIndex}对应{@code T5012.F04}</li>
	 * <li>{@link CustomerServiceRecord#name}对应{@code T5012.F05}</li>
	 * <li>{@link CustomerServiceRecord#number}对应{@code T5012.F06}</li>
	 * <li>文件编码{@link String} 对应{@code T5012.F07}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id 客服id
	 * @param customerService 客服信息
	 * @throws Throwable
	 */
	public abstract void update(int id, CustomerService customerService)
			throws Throwable;
	
	/** <一句话功能简述>
     * 批量更新排序值
     * @param ids
     * @param order
     * @return
     * @throws Throwable
     */
    public abstract void updateBatchOrder(String ids,int order)throws Throwable;

	/**
	 * 修改客服状态
	 * @param id
	 * @param t5012
	 * @throws Throwable
	 */
	public abstract void updateStatus(String id, T5012_F11 t5012) throws Throwable;

	/**
	 * 查询指定状态的客服数量
	 * @param t5012
	 * @throws Throwable
	 */
	public abstract Integer getQyCount(T5012_F11 t5012) throws Throwable;
}
