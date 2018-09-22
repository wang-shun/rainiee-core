package com.dimeng.p2p.modules.bid.console.service;

import java.io.OutputStream;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.bid.console.service.entity.BlacklistDetails;
import com.dimeng.p2p.modules.bid.console.service.entity.BlacklistInfo;
import com.dimeng.p2p.modules.bid.console.service.entity.CollectionRecordInfo;
import com.dimeng.p2p.modules.bid.console.service.entity.Less30;
import com.dimeng.p2p.modules.bid.console.service.entity.Near30;
import com.dimeng.p2p.modules.bid.console.service.entity.StayRefund;
import com.dimeng.p2p.modules.bid.console.service.entity.StayRefundGather;
import com.dimeng.p2p.modules.bid.console.service.entity.StayRefundInfo;
import com.dimeng.p2p.modules.bid.console.service.query.BlacklistQuery;
import com.dimeng.p2p.modules.bid.console.service.query.CollectionRecordQuery;
import com.dimeng.p2p.modules.bid.console.service.query.Greater30Query;
import com.dimeng.p2p.modules.bid.console.service.query.Less30Query;
import com.dimeng.p2p.modules.bid.console.service.query.Near30Query;

public interface CollectionManage extends Service {
	/**
	 * <dt>
	 * <dl>
	 * 描述：查询催收近30天待还列表信息
	 * </dl>
	 * </dt>
	 * 
	 * @param near30Query
	 *            查询信息
	 * @param paging
	 *            分页信息
	 * @return 催收近30天待还列表还信息
	 */
	public abstract PagingResult<Near30> near30Search(Near30Query near30Query, Paging paging)
			throws Throwable;
    
    /**
     * 描述：查询催收近30天待还列表借款总金额、本期应还总金额、剩余应还总金额
     * <功能详细描述>
     * @param near30Query
     * @return
     * @throws Throwable
     */
    public abstract Near30 near30SearchAmount(Near30Query near30Query)
        throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：催收近30天待还统计信息
	 * </dl>
	 * </dt>
	 * 
	 * @throws Throwable
	 */
	public abstract StayRefundGather findStayRefundGather() throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：催收详情信息
	 * </dl>
	 * </dt>
	 * 
	 * @param collectionId
	 *            催收Id
	 * @throws Throwable
	 */
	public abstract StayRefundInfo findStayRefund(int collectionId) throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：催收处理
	 * </dl>
	 * </dt>
	 * 
	 * @param collectionId
	 *            催收Id
	 * @throws Throwable
	 */
	public abstract void stayRefundDispose(StayRefund stayRefund, String method, String csType) throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：查询催收逾期待还列表信息
	 * </dl>
	 * </dt>
	 * 
	 * @param less30Query
	 *            查询信息
	 * @param paging
	 *            分页信息
	 * @return 催收逾期待还列表还信息
	 */
	public abstract PagingResult<Less30> less30Search(Less30Query less30Query, Paging paging)
			throws Throwable;
    
    /**
     * 描述：查询催收逾期待还列表借款总金额、本期应还总金额、剩余应还总金额
     * <功能详细描述>
     * @param less30Query
     * @return
     * @throws Throwable
     */
    public abstract Less30 less30SearchAmount(Less30Query less30Query)
        throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：查询催收严重逾期待还列表信息
	 * </dl>
	 * </dt>
	 * 
	 * @param near30Query
	 *            查询信息
	 * @param paging
	 *            分页信息
	 * @return 催收严重逾期待还列表还信息
	 */
	public abstract PagingResult<Less30> Greater30Search(Greater30Query greater30Query, Paging paging)
			throws Throwable;
    
    /**
     * 描述：查询催收严重逾期待还列表借款总金额、本期应还总金额、剩余应还总金额
     * <功能详细描述>
     * @param greater30Query
     * @return
     * @throws Throwable
     */
    public abstract Less30 Greater30SearchAmount(Greater30Query greater30Query)
        throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：查询线下催收记录列表信息
	 * </dl>
	 * </dt>
	 * 
	 * @param collectionRecordQuery
	 *            查询信息
	 * @param paging
	 *            分页信息
	 * @return 催收记录列表还信息
	 */
	public abstract PagingResult<CollectionRecordInfo> CollectionXxcsRecordSearch(CollectionRecordQuery collectionRecordQuery, Paging paging)
			throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：查询线上催收记录列表信息
	 * </dl>
	 * </dt>
	 * 
	 * @param collectionRecordQuery
	 *            查询信息
	 * @param paging
	 *            分页信息
	 * @return 催收记录列表还信息
	 */
	public abstract PagingResult<CollectionRecordInfo> CollectionXscsRecordSearch(CollectionRecordQuery collectionRecordQuery, Paging paging)
			throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：查询催收详情信息
	 * </dl>
	 * </dt>
	 * 
	 * @param collectionId
	 *            催收Id
	 * @throws Throwable
	 */
	public abstract CollectionRecordInfo findCollectionRecord(int collectionId) throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：查询黑名单列表信息
	 * </dl>
	 * </dt>
	 * 
	 * @param blacklistQuery
	 *            查询信息
	 * @param paging
	 *            分页信息
	 * @return 催收记录列表还信息
	 */
	public abstract PagingResult<BlacklistInfo> blacklistSearch(BlacklistQuery blacklistQuery, Paging paging)
			throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：查询黑名单详情信息
	 * </dl>
	 * </dt>
	 * 
	 * @param blacklistId
	 *            黑名单Id
	 * @return 催收记录列表还信息
	 */
	public abstract BlacklistDetails findBlacklistDetails(int blacklistId)
			throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：催收逾期待还列表导出Excel
	 * </dl>
	 * </dt>
	 * 
	 * @throws Throwable
	 */
	public abstract void exportLess30(Less30[] less30s, OutputStream outputStream,
			String charset) throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：催收近30天待还导出Excel
	 * </dl>
	 * </dt>
	 * 
	 * @throws Throwable
	 */
	public abstract void exportNear30(Near30[] near30s, OutputStream outputStream,
			String charset) throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：黑名单导出Excel
	 * </dl>
	 * </dt>
	 * 
	 * @throws Throwable
	 */
	public abstract void exportBlacklist(BlacklistInfo[] blacklistInfos, OutputStream outputStream,
			String charset) throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：严重逾期导出Excel
     * </dl>
     * </dt>
     * 
     * @throws Throwable
     */
    public abstract void exportGreater30(Less30[] near30s, OutputStream outputStream, String charset)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：催收记录导出Excel
     * </dl>
     * </dt>
     * 
     * @throws Throwable
     */
    public void exportCsList(CollectionRecordInfo[] crList, OutputStream outputStream, String charset)
            throws Throwable;
    
    /**
     * 描述：获取管理员的名字
     * @param adminId
     * @return
     * @throws Throwable
     */
    public abstract String getAdminNameById(int adminId) throws Throwable;
    
    /**
     * 描述：获取用户名
     * @param userId
     * @return
     * @throws Throwable
     */
    public abstract String getUserNameById(int userId) throws Throwable;
}
