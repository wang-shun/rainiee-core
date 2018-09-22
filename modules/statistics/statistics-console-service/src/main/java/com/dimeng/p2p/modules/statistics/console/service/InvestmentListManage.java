package com.dimeng.p2p.modules.statistics.console.service;

import java.io.OutputStream;
import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.statistics.console.service.entity.InvestmentListEntity;
import com.dimeng.p2p.modules.statistics.console.service.query.InvestmentQuery;

public abstract interface InvestmentListManage extends Service {
	
	public abstract PagingResult<InvestmentListEntity> getInvestmentList(InvestmentQuery query, Paging paging) throws Throwable;

	public abstract void export(InvestmentListEntity[] recWits,BigDecimal total, OutputStream outputStream, String charset) throws Throwable;
	
	public abstract BigDecimal getInverstmentTotal(InvestmentQuery query)throws Throwable;
}
