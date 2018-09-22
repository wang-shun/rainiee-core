package com.dimeng.p2p.modules.base.front.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S50.entities.T5012;
import com.dimeng.p2p.S50.enums.T5012_F03;

/**
 * 在线客服管理
 * 
 */
public interface CustomerServiceManage extends Service {

	/**
	 * <dt>
	 * <dl>
	 * 描述：按照在线客服类型查询所有在线客服信息.
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
	 * <li>如果{@code customerServiceType==null}则返回{@code null}</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>查询{@code T5012}表,查询条件:{@code T5012.F03 = customerServiceType}</li>
	 * <li>按照{@code T5012.F04}字段降序排序</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@code T5012.F01}</li>
	 * <li>{@code T5012.F02}</li>
	 * <li>{@code T5012.F03}</li>
	 * <li>{@code T5012.F05}</li>
	 * <li>{@code T5012.F06}</li>
	 * <li>{@code T5012.F07}</li>
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
	 * @param customerServiceType
	 *            客服类型
	 * @return {@link T5012}{@code []}客服列表,如果不存在则返回{@code null}
	 * @throws Throwable
	 */
	public abstract T5012[] getAll(T5012_F03 customerServiceType)
			throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据ID获取客服信息.
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
	 * <li>查询{@code T5012}表,查询条件:{@code T5012.F01 = id}</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@code T5012.F01}</li>
	 * <li>{@code T5012.F02}</li>
	 * <li>{@code T5012.F03}</li>
	 * <li>{@code T5012.F05}</li>
	 * <li>{@code T5012.F06}</li>
	 * <li>{@code T5012.F07}</li>
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
	 *            客户ID
	 * @return {@link T5012} 客服信息,如果不存在则返回{@code null}
	 * @throws Throwable
	 */
	public abstract T5012 get(int id) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：累加客服点击次数.
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
	 * <li>修改{@code T5012.F02=F02+1},修改条件:{@code T5012.F01=id}</li>
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
	 *            客服ID
	 * @throws Throwable
	 */
	public abstract void view(int id) throws Throwable;
}
