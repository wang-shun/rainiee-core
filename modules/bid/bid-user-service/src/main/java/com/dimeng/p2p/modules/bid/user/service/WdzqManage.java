package com.dimeng.p2p.modules.bid.user.service;

import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.bid.user.service.entity.AssetsInfo;
import com.dimeng.p2p.modules.bid.user.service.entity.BackOff;
import com.dimeng.p2p.modules.bid.user.service.entity.BackOffList;
import com.dimeng.p2p.modules.bid.user.service.entity.Tbzdzq;
import com.dimeng.p2p.modules.bid.user.service.entity.ZqxxEntity;
import com.dimeng.p2p.modules.bid.user.service.query.BackOffQuery;

/**
 * 标管理
 * 
 */
public interface WdzqManage extends Service {
	/**
	 * 查询债权信息实体
	 * @return 
	 */
	public abstract AssetsInfo getAssetsInfo() throws Throwable;
	/**
	 * 分页查询回收中的债权
	 * @param paging
	 * @return
	 */
	public abstract PagingResult<ZqxxEntity> getRecoverAssests(Paging paging) throws Throwable;
	/**
	 * 分页查询已结清的债权
	 * @param paging
	 * @return
	 */
	public abstract PagingResult<ZqxxEntity> getSettleAssests(Paging paging) throws Throwable; 
	/**
	 * 分页查询投资中的债权
	 * @param paging
	 * @return
	 */
	public abstract PagingResult<Tbzdzq> getLoanAssests(Paging paging) throws Throwable; 
	
	/**
	 * 查询待收本息
	 * @return BackOff
	 */
	public abstract BackOff searchTotle() throws Throwable;
	
	/**
	 * 查询已收本息
	 * @return BackOff
	 */
	public abstract BigDecimal searchYsTotle() throws Throwable;
	/**
	 * 查询列表信息
	 * @param backOffQuery
	 * @return PagingResult<BackOffList>
	 */
	public abstract PagingResult<BackOffList> searchList(BackOffQuery backOffQuery,Paging paging)throws Throwable;
	/**
	 * 查询指定条件下（类型、时间）的已收、待收金额
	 * @param backOffQuery
	 * @return PagingResult<BackOffList>
	 */
	public abstract BigDecimal searchList(String type,String strDate,String endDate)throws Throwable;

	/**
	 * 分页功能
	 * 返回拼接好的分页字符串
	 * @param paging 分页对象
	 * @return 分页字符串
	 * @throws Throwable
	 */
    String rendPaging(PagingResult<?> paging) throws Throwable;

}
