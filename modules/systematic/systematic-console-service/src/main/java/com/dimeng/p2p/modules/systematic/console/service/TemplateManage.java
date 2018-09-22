package com.dimeng.p2p.modules.systematic.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S70.entities.T7014;
import com.dimeng.p2p.common.enums.SysMessageType;
import com.dimeng.p2p.modules.systematic.console.service.entity.Template;

public abstract interface TemplateManage extends Service {
	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：查询所有消息模板信息
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
	 * <li>{@code type}为空   则赋值为 {@link SysMessageType#ZNX}</li>
	 * <li>查询{@link T7014}表    查询条件{@code T7014.F02 = type}</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link Template#key}对应{@code T7014.F01}</li>
	 * <li>{@link Template#type}对应{@code T7014.F02}</li>
	 * <li>{@link Template#eventDes}对应{@code T7014.F03}</li>
	 * <li>{@link Template#title}对应{@code T7014.F04}</li>
	 * <li>{@link Template#content}对应{@code T7014.F05}</li>
	 * <li>{@link Template#paramDes}对应{@code T7014.F06}</li>
	 * <li>{@link Template#lastUpdateTime}对应{@code T7014.F07}</li>
	 * <li>{@link Template#defalutContent}对应{@code T7014.F08}</li>
	 * <li>{@link Template#status}对应{@code T7014.F09}</li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param type
	 *            类型
	 * @param paging 分页参数
	 * @return {@link PagingResult}{@code <}{@link Template}{@code >} 分页查询结果
	 * @throws Throwable
	 */
	public abstract PagingResult<Template> serarch(String type, Paging paging)
			throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：修改消息模板信息
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
	 * <li>修改{@link T7014}表    修改条件{@code T7014.F01 = key AND T7014.F02 = type}</li>
	 * <li>
	 * 修改字段列表:
	 * <ol>
	 * <li>{@code title}对应{@code T7014.F04}</li>
	 * <li>{@code content}对应{@code T7014.F05}</li>
	 * <li>当前系统时间对应{@code T7014.F07}</li>
	 * <li>{@code status}对应{@code T7014.F09}</li>
	 * 
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param key 主键
	 * @param status 状态
	 * @param title 标题
	 * @param content 内容
	 * @param type 消息类型
	 * @throws Throwable
	 */
	public abstract void update(String key, String status, String title,
			String content,SysMessageType type) throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：查询模板详细信息
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code key}为空  则抛出参数异常   未指定模板或指定的模板不存在.</li>
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
	 * <li>查询{@link T7014}表    查询条件{@code T7014.F01 = key }</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link Template#key}对应{@code T7014.F01}</li>
	 * <li>{@link Template#type}对应{@code T7014.F02}</li>
	 * <li>{@link Template#eventDes}对应{@code T7014.F03}</li>
	 * <li>{@link Template#title}对应{@code T7014.F04}</li>
	 * <li>{@link Template#content}对应{@code T7014.F05}</li>
	 * <li>{@link Template#paramDes}对应{@code T7014.F06}</li>
	 * <li>{@link Template#lastUpdateTime}对应{@code T7014.F07}</li>
	 * <li>{@link Template#defalutContent}对应{@code T7014.F08}</li>
	 * <li>{@link Template#status}对应{@code T7014.F09}</li>
	 * 
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param key
	 *            主键
	 * @return {@link Template} 查询结果
	 * @throws Throwable
	 */
	public abstract Template get(String key) throws Throwable;
}
