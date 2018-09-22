package com.dimeng.p2p.modules.financing.user.service;

import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.financing.user.service.entity.InSellFinacing;
import com.dimeng.p2p.modules.financing.user.service.entity.MaySettleFinacing;
import com.dimeng.p2p.modules.financing.user.service.entity.OutSellFinacing;
import com.dimeng.p2p.modules.financing.user.service.entity.SellFinacing;
import com.dimeng.p2p.modules.financing.user.service.entity.SellFinacingInfo;
import com.dimeng.p2p.modules.financing.user.service.query.addTransfer;

/**
 *债权转让接口 
 *
 */
public abstract interface SellFinacingManage  extends Service{
	/**
	 * 得到债权转让信息
	 * @return
	 */
	public  SellFinacingInfo getSellFinacingInfo() throws Throwable;
	/**
	 * 分页查询转让中的债权
	 * @param paging
	 * @return
	 */
	public PagingResult<SellFinacing> getSellFinacing(Paging paging) throws Throwable; 
	/**
	 * 分页查询可转让的债权
	 * @param paging
	 * @return
	 */
	public PagingResult<MaySettleFinacing> getMaySettleFinacing(Paging paging) throws Throwable; 
	/**
	 * 分页查询已转出的债权
	 * @param paging
	 * @return
	 */
	public PagingResult<OutSellFinacing> getOutSellFinacing(Paging paging) throws Throwable; 
	/**
	 * 分页查询已转入的债权
	 * @param paging
	 * @return
	 */
	public PagingResult<InSellFinacing> getInSellFinacing(Paging paging) throws Throwable; 
	
	/**
	 * 取消转让中的债权
	 * @param zcbId
	 * @throws Throwable
	 */
	public void cancel(int zcbId)throws Throwable; 
	
	/**
	 * 转让债权
	 * @param zcbId
	 * @throws Throwable
	 */
	public void transfer(addTransfer addTransfer)throws Throwable;
	/**
	 * 获取最新的债权转让ID
	 * @return
	 * @throws Throwable
	 */
	public int getCrid()throws Throwable;
	/**
	 * 获取预计收益
	 * @param loanId
	 * @return
	 * @throws Throwable
	 */
	public BigDecimal getDslx(int loanId)throws Throwable;
	
	
	
}
