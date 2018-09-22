package com.dimeng.p2p.modules.base.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.base.console.service.entity.Partner;
import com.dimeng.p2p.modules.base.console.service.entity.PartnerRecord;
import com.dimeng.p2p.modules.base.console.service.query.PartnerQuery;

/**
 * 合作伙伴管理.
 * 
 */
public interface PartnerManage extends Service {
	/**
	 * <dt>
	 * <dl>
	 * 描述：分页查询合作伙伴信息列表.
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
	 * <li>查询{@link T5013}表{@link T7110}表</li>
	 * <li>查询条件:
	 * <ol>
	 * <li>{@code T5013.F09 = T7110.F01 }</li>
	 * <li>如果{@code query != null} 则
	 * <ol>
	 * <li>如果{@link PartnerQuery#getName()}不为空  则{@code T5013.F04 LIKE }{@link PartnerQuery#getName()}</li>
	 * <li>如果{@link PartnerQuery#getPublisherName()}不为空  则{@code T7110.F04 LIKE }{@link PartnerQuery#getPublisherName()}</li>
	 * <li>如果{@link PartnerQuery#getCreateTimeStart()}不为空  则{@code DATE(T5013.F10) >= }{@link PartnerQuery#getCreateTimeStart()}</li>
	 * <li>如果{@link PartnerQuery#getCreateTimeEnd()}不为空  则{@code DATE(T5013.F10) <= }{@link PartnerQuery#getCreateTimeEnd()}</li>
	 * <li>如果{@link PartnerQuery#getUpdateTimeStart()}不为空  则{@code DATE(T5013.F11) >= }{@link PartnerQuery#getUpdateTimeStart()}</li>
	 * <li>如果{@link PartnerQuery#getUpdateTimeEnd()}不为空  则{@code  DDATE(T5013.F11) <= }{@link PartnerQuery#getUpdateTimeEnd()}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </li>
	 * <li>按照{@code T5013.F02,T5013.F11}字段降序排序</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link PartnerRecord#id}对应{@code T5013.F01}</li>
	 * <li>{@link PartnerRecord#sortIndex}对应{@code T5013.F02}</li>
	 * <li>{@link PartnerRecord#viewTimes}对应{@code T5013.F03}</li>
	 * <li>{@link PartnerRecord#name}对应{@code T5013.F04}</li>
	 * <li>{@link PartnerRecord#url}对应{@code T5013.F05}</li>
	 * <li>{@link PartnerRecord#imageCode}对应{@code T5013.F06}</li>
	 * <li>{@link PartnerRecord#address}对应{@code T5013.F07}</li>
	 * <li>{@link PartnerRecord#description}对应{@code T5013.F08}</li>
	 * <li>{@link PartnerRecord#publisherId}对应{@code T5013.F09}</li>
	 * <li>{@link PartnerRecord#createTime}对应{@code T5013.F10}</li>
	 * <li>{@link PartnerRecord#updateTime}对应{@code T5013.F11}</li>
	 * <li>{@link PartnerRecord#publisherName}对应{@code T7110.F04}</li>
	 * 
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * @param query 查询条件
	 * @param paging 分页参数
	 * @return {@link PagingResult}{@code <}{@link PartnerRecord}{@code >} 分页查询结果,没有结果则返回{@code null}
	 * @throws Throwable
	 */
	public abstract PagingResult<PartnerRecord> search(PartnerQuery query,
			Paging paging) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据id查询合作伙伴信息.
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
	 * <li>查询{@link T5013}表{@link T7110}表表  查询条件:{@code T5013.F09 = T7110.F01 AND T5013.F01 = id}</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link PartnerRecord#id}对应{@code T5013.F01}</li>
	 * <li>{@link PartnerRecord#sortIndex}对应{@code T5013.F02}</li>
	 * <li>{@link PartnerRecord#viewTimes}对应{@code T5013.F03}</li>
	 * <li>{@link PartnerRecord#name}对应{@code T5013.F04}</li>
	 * <li>{@link PartnerRecord#url}对应{@code T5013.F05}</li>
	 * <li>{@link PartnerRecord#imageCode}对应{@code T5013.F06}</li>
	 * <li>{@link PartnerRecord#address}对应{@code T5013.F07}</li>
	 * <li>{@link PartnerRecord#description}对应{@code T5013.F08}</li>
	 * <li>{@link PartnerRecord#publisherId}对应{@code T5013.F09}</li>
	 * <li>{@link PartnerRecord#createTime}对应{@code T5013.F10}</li>
	 * <li>{@link PartnerRecord#updateTime}对应{@code T5013.F11}</li>
	 * <li>{@link PartnerRecord#publisherName}对应{@code T7110.F04}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * @param id 合作伙伴ID
	 * @return {@link PartnerRecord} 查询结果
	 * @throws Throwable
	 */
	public abstract PartnerRecord get(int id) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据id批量删除合作伙伴信息.
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
	 * <li>批量删除{@link T5013}表  查询条件:{@code T5013.F01 = ids}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param ids 合作伙伴ID列表
	 * @throws Throwable
	 */
	public abstract void delete(int... ids) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：添加合作伙伴信息.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code partner == null} 则抛出参数异常  没有指定合作伙伴信息</li>
	 * <li>如果{@link Partner#getName() }为空 则抛出参数异常  合作伙伴名称不能为空  </li>
	 * <li>如果{@link Partner#getImage() }为空 则抛出参数异常  合作伙伴图片不能为空 </li>
	 * <li>如果{@link Partner#getDescription() }为空 则抛出参数异常  公司简介不能为空 </li>
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
	 * <li>{@link Partner#getImage() }上传图片 得到文件编码{@link String}</li>
	 * <li>新增{@link T5013}表  <li>
	 * 新增字段列表:
	 * <ol>
	 * <li>{@link Partner#getSortIndex()}对应{@code T5013.F02}</li>
	 * <li>{@link Partner#getName()}对应{@code T5013.F04}</li>
	 * <li>{@link Partner#getURL()}对应{@code T5013.F05}</li>
	 * <li>文件编码{@link String} 对应{@code T5013.F06}</li>
	 * <li>{@link Partner#getAddress()} 对应{@code T5013.F07}</li>
	 * <li>{@link Partner#getDescription()} 对应{@code T5013.F08}</li>
	 * <li>当前系统登录ID 对应{@code T5013.F09}</li>
	 * <li>当前系统时间 对应{@code T5013.F10}</li>
	 * 
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * @param partner 合作伙伴信息 
	 * @return {@link int} 合作伙伴ID
	 * @throws Throwable
	 */
	public abstract int add(Partner partner) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据id修改合作伙伴信息.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code partner == null}或则 {@code  id <= 0}则直接返回 </li>
	 * <li>如果{@link Partner#getName() }为空 则抛出参数异常   合作伙伴名称不能为空 </li>
	 * <li>如果{@link Partner#getDescription() }为空 则抛出参数异常   公司简介不能为空 </li>
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
	 * <li>如果{@link Partner#getImage() }不为空 上传文件得到 文件编码{@link String} 否则不上传</li>
	 * <li>修改{@link T5013}表   修改条件：{@code T5013.F01 = id} 如果 文件编码{@link String} 为{@code null} 则不修改{@code T5013.F06 }
	 * <li>
	 * 修改字段列表:
	 * <ol>
	 * <li>{@link Partner#getSortIndex()} 对应{@code T5013.F02}</li>
	 * <li>{@link Partner#getName()} 对应{@code T5013.F04}</li>
	 * <li>{@link Partner#getURL()} 对应{@code T5013.F05}</li>
	 * <li>文件编码{@link String} 对应{@code T5013.F06}</li>
	 * <li>{@link Partner#getAddress()} 对应{@code T5013.F07}</li>
	 * <li>{@link Partner#getDescription()} 对应{@code T5013.F08}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id 合作伙伴ID
	 * @param partner 合作伙伴信息
	 * @throws Throwable
	 */
	public abstract void update(int id, Partner partner) throws Throwable;
}
