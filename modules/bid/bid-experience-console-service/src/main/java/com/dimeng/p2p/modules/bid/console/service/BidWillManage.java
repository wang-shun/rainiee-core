package com.dimeng.p2p.modules.bid.console.service;

import java.io.OutputStream;
import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.bid.console.service.entity.Grjkyx;
import com.dimeng.p2p.modules.bid.console.service.entity.Qyjkyx;
import com.dimeng.p2p.modules.bid.console.service.query.GrIntentionQuery;
import com.dimeng.p2p.modules.bid.console.service.query.QyIntentionQuery;

/**
 * 融资意向
 * 
 */
public abstract interface BidWillManage extends Service {
	/**
	 * 查询个人融资意向
	 * 
	 * @param loanIntentionQuery
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<Grjkyx> searchPersonal(GrIntentionQuery query,
			Paging paging) throws Throwable;
    
    /**
     * 查询个人融资总金额
     * <功能详细描述>
     * @param query
     * @return
     * @throws Throwable
     */
    public abstract BigDecimal searchPersonalAmount(GrIntentionQuery query)
        throws Throwable;

	/**
	 * 查询企业融资意向
	 * 
	 * @param loanIntentionQuery
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<Qyjkyx> searchEnterprise(
			QyIntentionQuery query, Paging paging) throws Throwable;
    
    /**
     * 查询企业融总金额
     * <功能详细描述>
     * @param query
     * @return
     * @throws Throwable
     */
    public abstract BigDecimal searchEnterpriseAmount(QyIntentionQuery query)
        throws Throwable;
	/**
	 * <dt>
	 * <dl>
	 * 描述：导出个人融资意向Excel
	 * </dl>
	 * </dt>
	 * 
	 * @throws Throwable
	 */
	public abstract void export(Grjkyx[] grjkyxs, OutputStream outputStream,
			String charset) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：导出企业融资意向Excel
	 * </dl>
	 * </dt>
	 * 
	 * @throws Throwable
	 */
	public abstract void export(Qyjkyx[] qyjkyxs, OutputStream outputStream,
			String charset) throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：根据ID查询个人融资意向详情
	 * </dl>
	 * 
	 * @param loanIntentionId
	 *            ID
	 * @return {@link grjkyx} 借款意向信息 不存在则返回{@code null}
	 * @throws Throwable
	 */
	public abstract Grjkyx getGrjkyx(int id) throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：根据ID查询企业融资意向详情
	 * </dl>
	 * 
	 * @param loanIntentionId
	 *            ID
	 * @return {@link grjkyx} 借款意向信息 不存在则返回{@code null}
	 * @throws Throwable
	 */
	public abstract Qyjkyx getQyjkyx(int id) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：个人借款意向处理
	 * </dl>
	 * 
	 * @param id
	 *            借款意向ID
	 * @param disposeDesc
	 *            处理结果
	 * @throws Throwable
	 */
	public abstract void grjkyxCl(int id, String disposeDesc) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：企业借款意向处理
	 * </dl>
	 * 
	 * @param ids
	 *            借款意向ID
	 * @param disposeDesc
	 *            处理结果
	 * @throws Throwable
	 */
	public abstract void qyjkyxCl(int id, String disposeDesc) throws Throwable;
}
