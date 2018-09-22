package com.dimeng.p2p.modules.bid.user.service;

import java.math.BigDecimal;
import java.util.Date;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.bid.user.service.entity.LoanInfo;

public abstract interface BuyLoanManage extends Service {
	
	/**
	 * 分页查询借款意向列表
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<LoanInfo> search(Paging paging)throws Throwable;
	
	/**
	 * 分页已购买的历史列表
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<LoanInfo> searchLs(String tel,Date startTime, Date endTime,Paging paging)throws Throwable;
	/**
	 * 查询购买标的详细信息
	 * @return
	 * @throws throwable
	 */
	public abstract LoanInfo search(int gmId)throws Throwable;
	/**
	 * 购买标
	 * @param loanId
	 * @throws Throwable
	 */
	public abstract int buyLoan(int loanId,BigDecimal gmfwf)throws Throwable;
}
