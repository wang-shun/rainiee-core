package com.dimeng.p2p.modules.bid.front.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6262;
import com.dimeng.p2p.modules.bid.front.service.entity.Zqzqlb;
import com.dimeng.p2p.modules.bid.front.service.entity.ZqzrEntity;
import com.dimeng.p2p.modules.bid.front.service.entity.Zqzrtj;
import com.dimeng.p2p.modules.bid.front.service.query.TransferAhQuery;

/**
 * 线上债权转让管理
 */
public interface TransferAhManage extends Service {

	/**
	 * 分页查询债权列表
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<Zqzqlb> search(TransferAhQuery query, Paging paging)
			throws Throwable;
	/**
	 * 线上债权转让统计
	 * @return
	 * @throws Throwable
	 */
	public abstract Zqzrtj getStatistics()throws Throwable;
	/**
	 * 获取线上债权转让头信息
	 * @return
	 * @throws Throwable
	 */
	public abstract ZqzrEntity getZqzrxx(int id) throws Throwable;
	/**
	 * 债权转出列表
	 * @param 转出表id
	 * @return
	 * @throws Throwable
	 */
	public abstract T6262[] getZqzclb(int id) throws Throwable;
	
	
}
