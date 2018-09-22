package com.dimeng.p2p.modules.base.front.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S50.entities.T5014;

/**
 * 友情链接
 * 
 */
public interface FriendlyLinkManage extends Service {

	/**
	 * <dt>
	 * <dl>
	 * 描述：获取所有友情链接列表.
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
	 * <li>查询{@code T5014}表</li>
	 * <li>按照{@code T5014.F03}字段降序排序</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@code T5014.F01}</li>
	 * <li>{@code T5014.F04}</li>
	 * <li>{@code T5014.F05}</li>
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
	 * @return {@link T5014}{@code []} 友情链接列表,没有任何记录则返回{@code null}
	 * @throws Throwable
	 */
	public abstract T5014[] getAll() throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据ID获取友情链接信息.
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
	 * <li>如果{@code id<=0}返回{@code null}</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>查询{@code T5014}表,查询条件:{@code T5014.F01 = id}</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@code T5014.F01}</li>
	 * <li>{@code T5014.F04}</li>
	 * <li>{@code T5014.F05}</li>
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
	 *            友情链接ID
	 * @return {@link T5014} 友情链接信息,不存在则返回{@code null}
	 * @throws Throwable
	 */
	public abstract T5014 get(int id) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：累加友情链接点击次数.
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
	 * <li>修改{@code T5014.F02=F02+1},修改条件:{@code T5014.F01=id}</li>
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
	 *            友情链接ID
	 * @throws Throwable
	 */
	public abstract void view(int id) throws Throwable;

}
