/**
 * 
 */
package com.dimeng.p2p.modules.base.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.entities.T5125;
import com.dimeng.p2p.modules.base.console.service.entity.Xymb;

/**
 * @author guopeng
 * 
 */
public abstract interface XymbManage extends Service {
	/**
	 * 
	 * <dl>
	 * 描述：查询协议模板信息.
	 * </dl>
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<T5125> srarch(Paging paging) throws Throwable;

	/**
	 * 查询协议模板
	 */
	public abstract Xymb get(int id,int version) throws Throwable;

	/**
	 * 
	 * <dl>
	 * 描述：修改模板协议 .
	 * </dl>
	 * 
	 * 
	 * @param id
	 * @param text
	 * @throws Throwable
	 */
	public abstract void add(int id, String content) throws Throwable;
	
	/**
	 * <dl>
	 * 描述：删除模板协议
	 * </dl>
	 * @param id
	 * @throws Throwable
	 */
	public abstract void delete(int id) throws Throwable;
	
}
