package com.dimeng.p2p.modules.bid.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S62.enums.T6273_F07;
import com.dimeng.p2p.modules.bid.console.service.entity.Dzqm;
import com.dimeng.p2p.modules.bid.console.service.entity.Qyjkxy;
import com.dimeng.p2p.modules.bid.console.service.entity.Zqzrxy;
import com.dimeng.p2p.modules.bid.console.service.query.DzqmQuery;
import com.dimeng.p2p.modules.bid.console.service.query.DzxyQuery;
import com.dimeng.p2p.modules.bid.console.service.query.ZqzrxyQuery;
/**
 * 电子协议
 * @author gaoshaolong
 *
 */
public interface ElectronManage extends Service {

	/**
	 * 电子签名
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<Dzqm> search(DzqmQuery query,T6273_F07 t6273_F07, Paging paging)throws Throwable; 
	
	
}
