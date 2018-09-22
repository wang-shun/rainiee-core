package com.dimeng.p2p.modules.statistics.console.service;

import java.io.OutputStream;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.statistics.console.service.entity.GrMoneyEntity;



public interface GrMoneyManage extends Service {

	PagingResult<GrMoneyEntity> search(GrMoneyEntity query, Paging paging)
			throws Throwable;
    
    public abstract GrMoneyEntity searchAmount(GrMoneyEntity query)
        throws Throwable;
    
	 public void export(GrMoneyEntity[] grMoneyEntitys, OutputStream outputStream, String charset)
		        throws Throwable;
	
}
