package com.dimeng.p2p.modules.statistics.console.service;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.statistics.console.service.entity.TransferCreditorEntity;
import com.dimeng.p2p.modules.statistics.console.service.query.TransferCreditorQuery;

public abstract interface TransferCreditorManage extends Service {
	
	public abstract PagingResult<TransferCreditorEntity> getCreditorList(TransferCreditorQuery query, Paging paging) throws Throwable;

	public abstract void export(TransferCreditorEntity[] recWits,Map totalMap, OutputStream outputStream, String charset) throws Throwable;
	
	public abstract Map<String, String> getCreditorTotal(TransferCreditorQuery query)throws Throwable;
}
