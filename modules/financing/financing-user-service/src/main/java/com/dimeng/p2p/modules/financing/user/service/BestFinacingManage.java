package com.dimeng.p2p.modules.financing.user.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.financing.user.service.entity.BestFinacingInfo;
import com.dimeng.p2p.modules.financing.user.service.entity.EndBestFinacing;
import com.dimeng.p2p.modules.financing.user.service.entity.InBestFinacing;
import com.dimeng.p2p.modules.financing.user.service.entity.SqzBestFinacing;

/**
 * 优先理财计划接口
 *
 */
public abstract interface BestFinacingManage  extends Service{
	/**
	 * 得到优先理财信息实体
	 * @return
	 */
	public BestFinacingInfo getBestFinacingInfo() throws Throwable;
	/**
	 * 分页查询持有中的优选理财计划
	 * @param paging
	 * @return
	 */
	public PagingResult<SqzBestFinacing> getSqzBestFinacing(Paging paging) throws Throwable; 
	/**
	 * 分页查询持有中的优选理财计划
	 * @param paging
	 * @return
	 */
	public PagingResult<InBestFinacing> getInBestFinacing(Paging paging) throws Throwable; 
	/**
	 * 分页查询已截止的优选理财计划
	 * @param paging
	 * @return
	 */
	public PagingResult<EndBestFinacing> getEndBestFinacing(Paging paging) throws Throwable; 
	/**
	 * 根据计划ID 查询剩余期数
	 * @param planID
	 * @return
	 * @throws Throwable
	 */
	public InBestFinacing getSyTime(int planID)throws Throwable; 
}
