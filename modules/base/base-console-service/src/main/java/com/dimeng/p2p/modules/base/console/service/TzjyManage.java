package com.dimeng.p2p.modules.base.console.service;

import java.io.OutputStream;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6195;
import com.dimeng.p2p.S61.entities.T6195_EXT;
import com.dimeng.p2p.modules.base.console.service.query.TzjyQuery;

/**
 * 投资建议管理.
 * 
 */
public interface TzjyManage extends Service {
	/**
	 * 查询投资建议列表
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<T6195_EXT> search(TzjyQuery query, Paging paging) throws Throwable;

	/**
	 * 回复指定建议
	 * @param t6195
	 * @throws Throwable
	 */
	public abstract void update(T6195 t6195) throws Throwable;

	/**
	 * 修改指定建议状态
	 * @param t6195
	 * @throws Throwable
	 */
	public abstract void setPublishStatus(T6195 t6195) throws Throwable;
	
	/**
     * 导出意见反馈列表
     * <功能详细描述>
     * @param t6195Exts
     * @param outputStream
     * @param charset
     * @throws Throwable
     */
    public void export(T6195_EXT[] t6195Exts, OutputStream outputStream, String charset)
        throws Throwable;
}
