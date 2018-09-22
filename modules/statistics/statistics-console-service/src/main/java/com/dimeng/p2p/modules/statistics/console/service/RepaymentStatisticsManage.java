package com.dimeng.p2p.modules.statistics.console.service;

import java.io.OutputStream;
import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.statistics.console.service.entity.RepaymentStatisticsEntity;
import com.dimeng.p2p.modules.statistics.console.service.query.RepaymentStatisticsQuery;

public abstract interface RepaymentStatisticsManage extends Service {
	
	public abstract PagingResult<RepaymentStatisticsEntity> getRepaymentList(RepaymentStatisticsQuery query, Paging paging) throws Throwable;

	public abstract void export(RepaymentStatisticsEntity[] recWits,BigDecimal total, OutputStream outputStream, String charset) throws Throwable;
	
	public abstract BigDecimal getRepaymentTotal(RepaymentStatisticsQuery query)throws Throwable;
}
