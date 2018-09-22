package com.dimeng.p2p.modules.base.front.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.modules.base.front.service.entity.LcInfo;
import com.dimeng.p2p.modules.base.front.service.entity.PtTzTotal;
import com.dimeng.p2p.modules.base.front.service.entity.PtlcTotal;
import com.dimeng.p2p.modules.base.front.service.entity.RankList;

/**
 * 前台主页接口
 * 
 *
 */
public abstract interface IndexManage extends Service {
	/**
	 * 前台首页理财排行榜
	 * @return
	 * @throws Throwable
	 */
	public abstract	RankList[] getLcList() throws Throwable;
	
	/**
	 * 得到当前月平台统计数据
	 * @return
	 * @throws Throwable
	 */
	public abstract PtlcTotal getPtlcTotal() throws Throwable;

	/**
	 * 得到平台投资统计数据
	 * @return
	 * @throws Throwable
	 */
	public abstract PtTzTotal getPtTzTotal() throws Throwable;
	
	/**
	 * 得到最新的投资信息
	 * @return
	 * @throws Throwable
	 */
	public abstract LcInfo[] getlcInfos() throws Throwable; 
	
	/**
	 * 得到平台总会员
	 * @return
	 * @throws Throwable
	 */
	public abstract int getPersonTotal() throws Throwable;
}
