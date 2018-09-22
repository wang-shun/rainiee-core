package com.dimeng.p2p.modules.bid.console.service;

import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S62.entities.T6212;
import com.dimeng.p2p.S62.entities.T6232;
import com.dimeng.p2p.S62.entities.T6233;
import com.dimeng.p2p.modules.bid.console.service.entity.MskAnnex;
import com.dimeng.p2p.modules.bid.console.service.entity.WzAnnex;

/**
 * 标的附件管理
 * 
 * @author guopeng
 * 
 */
public interface AnnexManage extends Service {
	/**
	 * 标附件列表(非公开)
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract WzAnnex[] searchFgk(int loanId) throws Throwable;

	/**
	 * 标附件列表(公开)
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract MskAnnex[] searchGk(int loanId) throws Throwable;

	/**
	 * 查询非公开详情
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract T6233 getFgk(int id) throws Throwable;

	/**
	 * 查询公开详情
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract T6232 getGk(int id) throws Throwable;

	/**
	 * 添加标的附件公开
	 * 
	 * @param query
	 */
	public abstract int addGk(T6232 t6232, UploadFile file) throws Throwable;

	/**
	 * 添加标的附件公开
	 * 
	 * @param query
	 */
	public abstract int addFgk(T6233 t6233, UploadFile file) throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：删除公开附件
	 * </dl>
	 * 
	 * @param ids
	 * @throws Throwable
	 */
	public abstract void delGk(int... ids) throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：删除非公开附件
	 * </dl>
	 * 
	 * @param ids
	 * @throws Throwable
	 */
	public abstract void delFgk(int... ids) throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 查询标的附件类型
	 * </dl>
	 * 
	 * @param ids
	 * @throws Throwable
	 */
	public abstract T6212[] searchAnnexType() throws Throwable;

	/**
	 * 根据标的ID查询公开附件是否存在
	 */
	public abstract boolean isGkExists(int loanId) throws Throwable;

	/**
	 * 根据标的ID查询非公开附件是否存在
	 */
	public abstract boolean isFgkExists(int loanId) throws Throwable;
}
