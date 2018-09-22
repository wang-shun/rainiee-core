package com.dimeng.p2p.modules.systematic.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S70.entities.T7011;
import com.dimeng.p2p.S70.entities.T7021;
import com.dimeng.p2p.modules.systematic.console.service.entity.Skin;
import com.dimeng.p2p.modules.systematic.console.service.entity.Skin.Status;

public abstract interface SkinManage extends Service {

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：添加皮肤
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
	 * <li>新增{@link T7021}表</li>
	 * <li>
	 * 
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@code themeName}对应{@code T7021.F02}</li>
	 * <li>{@code location}对应{@code T7021.F03}</li>
	 * <li>{@code pic}对应{@code T7021.F04}</li>
	 * <li>当前系统登录ID对应{@code T7021.F05}</li>
	 * <li>当前系统时间 对应{@code T7021.F06}</li>
	 * <li>{@link Status#S}对应{@code T7021.F08}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * 
	 * 
	 * @param themeName
	 *            主题名称
	 * @param location
	 *            显示位置
	 * @param pic
	 *            图片路径
	 * @return {@code int} 自增ID
	 * @throws Throwable
	 */
	public abstract int add(String themeName, String location, String pic)
			throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：查询所有皮肤
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
	 * <li>查询{@link T7021}表{@link T7011}表</li>
	 * <li>查询条件:
	 * <ol>
	 * <li>{@code T7021.F05=T7011.F01}</li>
	 * </ol>
	 * </li>
	 * 
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link Skin#id}对应{@code T7021.F01}</li>
	 * <li>{@link Skin#themeName}对应{@code T7021.F02}</li>
	 * <li>{@link Skin#location}对应{@code T7021.F03}</li>
	 * <li>{@link Skin#pic}对应{@code T7021.F04}</li>
	 * <li>{@link Skin#createTime}对应{@code T7021.F06}</li>
	 * <li>{@link Skin#lastUpdateTime}对应{@code T7021.F07}</li>
	 * <li>{@link Skin#isEffective}对应{@code T7021.F08}</li>
	 * <li>{@link Skin#name}对应{@code T7011.F02}</li>
	 * 
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @return {@link Skin}{@code []}查询结果
	 * @throws Throwable
	 */
	public abstract Skin[] getAll() throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：查询皮肤详细信息
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code id <= 0}则抛出参数异常  指定的皮肤记录ID不存在.</li>
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
	 * <li>查询{@link T7021}表{@link T7011}表  查询条件:{@code T7021.F05=T7011.F01 WHERE T7021.F01 = id}</li>
	 * <li>
	 * 
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link Skin#id}对应{@code T7021.F01}</li>
	 * <li>{@link Skin#themeName}对应{@code T7021.F02}</li>
	 * <li>{@link Skin#location}对应{@code T7021.F03}</li>
	 * <li>{@link Skin#pic}对应{@code T7021.F04}</li>
	 * <li>{@link Skin#createTime}对应{@code T7021.F06}</li>
	 * <li>{@link Skin#lastUpdateTime}对应{@code T7021.F07}</li>
	 * <li>{@link Skin#isEffective}对应{@code T7021.F08}</li>
	 * <li>{@link Skin#name}对应{@code T7011.F02}</li>
	 * 
	 *
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id
	 *            主键ID
	 * @return {@link Skin}查询结果
	 * @throws Throwable
	 */
	public abstract Skin get(int id) throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：根据皮肤位置查询图片路径
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
	 * <li>查询{@link T7021}表    查询条件:{@code T7021.F03 = location}</li>
	 * <li>
	 * 
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link String}对应{@code T7021.F04}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * @param location
	 *            皮肤位置
	 * @return {@link String}  图片路径
	 * @throws Throwable
	 */
	public abstract String get(String location) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：修改皮肤信息
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code id<=0} 则抛出参数异常  指定的皮肤记录ID不存在</li>
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
	 * <li>修改{@link T7021}表    修改条件:{@code T7021.F01 = id}</li>
	 * <li>
	 * 
	 * <li>
	 * 修改字段列表:
	 * <ol>
	 * <li>{@code themeName}对应{@code T7021.F02}</li>
	 * <li>{@code location}对应{@code T7021.F03}</li>
	 * <li>{@code pic}对应{@code T7021.F04}</li>
	 * <li>当前系统时间对应{@code T7021.F07}</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * @param id
	 *            主键
	 * @param themeName
	 *            主题名称
	 * @param location
	 *            显示位置
	 * @param pic
	 *            图片路径
	 * @throws Throwable
	 */
	public abstract void update(int id, String themeName, String location,
			String pic) throws Throwable;

	/**
	 *
	 * <dt>
	 * <dl>
	 * 描述： 根据皮肤ID删除皮肤信息
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code ids == null} 则抛出参数异常  未指定记录或指定的记录不存在.</li>
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
	 * <li>删除{@link T7021}表    删除条件:{@code T7021.F01 = ids}</li>
	 * </dl>
	 * </dt>
	 * 
	 * @param ids
	 *            皮肤ID,可以为一个或者多个
	 * @throws Throwable
	 */
	public abstract void del(int... ids) throws Throwable;
}
