package com.dimeng.p2p.modules.base.front.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S50.entities.T5016;
import com.dimeng.p2p.S50.entities.T5016_1;
import com.dimeng.p2p.modules.base.front.service.entity.AdvertSpscRecord;

/**
 * 广告管理.
 * 
 */
public abstract interface AdvertisementManage extends Service {

	/**
	 * <dt>
	 * <dl>
	 * 描述：查询所有发布的焦点图广告信息列表.
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
	 * <li>查询{@link T5016}表</li>
	 * <li>查询条件: {@link T5016#F07} {@code <= CURRENT_TIMESTAMP() AND F08 >=
	 * CURRENT_TIMESTAMP()}</li>
	 * <li>按照{@link T5016#F02}字段降序排序</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link T5016#F01}</li>
	 * <li>{@link T5016#F03}</li>
	 * <li>{@link T5016#F04}</li>
	 * <li>{@link T5016#F05}</li>
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
	 * @return {@link T5016}{@code []} 广告列表,没有记录则返回{@code null}
	 * @throws Throwable
	 */
	public abstract T5016[] getAll() throws Throwable;
	public abstract T5016[] getAll_BZB(String type) throws Throwable;
	public abstract T5016_1[] getAllTTDai() throws Throwable;
	
	public abstract T5016_1 getById(int id) throws Throwable;
	/**
	 * 前台查询视频
	 * */
	
	public abstract AdvertSpscRecord  searchqtSpsc() throws Throwable;
}
