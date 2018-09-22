package com.dimeng.p2p.modules.base.front.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S50.entities.T5017;
import com.dimeng.p2p.common.enums.TermType;

/**
 * 协议管理
 * 
 */
public abstract interface TermManage extends Service {

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据协议类型获取协议信息.
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
	 * <li>如果{@code termType==null}则返回{@code null}</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>查询{@code T5017}表,查询条件:{@code T5017.F01 = termType}</li>
	 * <li>查询字段列表:
	 * <ol>
	 * <li>{@code T5017.F03}</li>
	 * <li>{@code T5017.F06}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 返回结果说明：
	 * <ol>
	 * <li>{@link Term#content}对应{@code T5017.F03}</li>
	 * <li>{@link Term#updateTime}对应{@code T5017.F06}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param termType
	 *            协议类型
	 * @return {@link T5017} 协议信息,不存在则返回{@code null}
	 * @throws Throwable
	 */
	public abstract T5017 get(TermType termType) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据协议类型查询协议附件文件编码列表.
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
	 * <li>如果{@code termType==null}则返回{@code null}</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>查询{@code T5017_1.F02},查询条件:{@code T5017_1.F01 = termType}</li>
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
	 * @param termType
	 *            协议类型
	 * @return {@link String}{@code []} 附件文件编码列表,不存在则返回{@code null}
	 * @throws Throwable
	 */
	public abstract String[] getAttachments(TermType termType)
			throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：累加协议点击次数.
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
	 * <li>如果{@code termType == null}则直接返回</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>修改{@code T5017.F02 = F02+1},修改条件:{@code T5017.F01 = termType}</li>
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
	 * @param termType
	 *            协议类型
	 * @throws Throwable
	 */
	public abstract void view(TermType termType) throws Throwable;
}
