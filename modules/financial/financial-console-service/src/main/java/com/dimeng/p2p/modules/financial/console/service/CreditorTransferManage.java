package com.dimeng.p2p.modules.financial.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.enums.T6260_F07;
import com.dimeng.p2p.modules.financial.console.service.entity.CreditorInfo;
import com.dimeng.p2p.modules.financial.console.service.entity.RefundRecord;
import com.dimeng.p2p.modules.financial.console.service.entity.TenderRecord;
import com.dimeng.p2p.modules.financial.console.service.entity.TransferDshEntity;
import com.dimeng.p2p.modules.financial.console.service.entity.TransferFinish;
import com.dimeng.p2p.modules.financial.console.service.entity.TransferProceed;
import com.dimeng.p2p.modules.financial.console.service.entity.TransferRecord;
import com.dimeng.p2p.modules.financial.console.service.entity.ViewLoanRecord;
import com.dimeng.p2p.modules.financial.console.service.entity.ViewTransfer;
import com.dimeng.p2p.modules.financial.console.service.query.TransferDshQuery;
import com.dimeng.p2p.modules.financial.console.service.query.TransferFinishQuery;
import com.dimeng.p2p.modules.financial.console.service.query.TransferProceedQuery;

public interface CreditorTransferManage extends Service {
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：查询待转让的债权
	 * </dl>
	 * </dt>
	 * 
	 * @param transferProceedQuery
	 *            查询信息
	 * @param paging
	 *            分页信息
	 * @return 转让中债权列表信息
	 */
	public abstract PagingResult<TransferDshEntity> transferDshSearch(TransferDshQuery transferDshQuery,T6260_F07 f07, Paging paging)
			throws Throwable;
    
    /**
     * 描述：查询待转让的总债权价值、总转让价值
     * <功能详细描述>
     * @param transferDshQuery
     * @return
     * @throws Throwable
     */
    public abstract TransferDshEntity transferDshAmount(TransferDshQuery transferDshQuery, T6260_F07 f07)
        throws Throwable;
	/**
	 * <dt>
	 * <dl>
	 * 描述：查询转让中债权
	 * </dl>
	 * </dt>
	 * 
	 * @param transferProceedQuery
	 *            查询信息
	 * @param paging
	 *            分页信息
	 * @return 转让中债权列表信息
	 */
	public abstract PagingResult<TransferProceed> transferProceedSearch(TransferProceedQuery transferProceedQuery, Paging paging)
			throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：查询已转出债权
	 * </dl>
	 * </dt>
	 * 
	 * @param transferFinishQuery
	 *            查询信息
	 * @param paging
	 *            分页信息
	 * @return 已转出的债权列表信息
	 */
	public abstract PagingResult<TransferFinish> transferFinishSearch(TransferFinishQuery transferFinishQuery, Paging paging)
			throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：对债权转让审核通过
	 * </dl>
	 * </dt>
	 * 
	 * @param ids
	 *            债权ID
	 * @param creditorTransferState
	 *            债权转让状态
	 * @throws Throwable
	 */
	public abstract void shtg(String ids) throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：对债权转让审核不通过
	 * </dl>
	 * </dt>
	 * 
	 * @param ids
	 *            债权ID
	 * @param creditorTransferState
	 *            债权转让状态
	 * @throws Throwable
	 */
	public abstract void shbtg(String ids) throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：借款详情
	 * </dl>
	 * </dt>
	 * 
	 * @param transferId
	 *            转让ID
	 * @throws Throwable
	 */
	public abstract ViewTransfer findTransferInfo(int transferId) throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：借款详情
	 * </dl>
	 * </dt>
	 * 
	 * @param loanRecordId
	 *            借款标ID
	 * @throws Throwable
	 */
	public abstract ViewLoanRecord findViewLoanRecord(int userId) throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：投资记录
	 * </dl>
	 * </dt>
	 * 
	 * @param loanRecordId
	 *            借款标ID
	 * @throws Throwable
	 */
	public abstract TenderRecord findTenderRecord(int loanRecordId) throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：还款记录
	 * </dl>
	 * </dt>
	 * 
	 * @param loanRecordId
	 *            借款标ID
	 * @throws Throwable
	 */
	public abstract RefundRecord findRefundRecord(int loanRecordId) throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：债权信息
	 * </dl>
	 * </dt>
	 * 
	 * @param loanRecordId
	 *            借款标ID
	 * @param paging
	 *            
	 * @return 债权列表信息
	 */
	public abstract CreditorInfo[] findCreditorInfo(int loanRecordId) throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：转让信息
	 * </dl>
	 * </dt>
	 * 
	 * @param loanRecordId
	 *            借款标ID
	 * @param paging
	 *            分页信息
	 * @return 转让列表信息
	 */
	public abstract TransferRecord findTransferRecord(int loanRecordId) throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述：债权转让下架
	 * </dl>
	 * </dt>
	 * 
	 * @param ids
	 *            债权ID
	 * @throws Throwable
	 */
	public abstract void zqzrxj(String ids) throws Throwable;
	
	
	/**
     * 描述：查询转让失败的总债权价值、总转让价值
     * <功能详细描述>
     * @param transferDshQuery
     * @return
     * @throws Throwable
     */
    public abstract TransferDshEntity transferZrsbAmount(TransferDshQuery transferDshQuery)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：查询转让失败的债权
     * </dl>
     * </dt>
     * 
     * @param transferProceedQuery
     *            查询信息
     * @param paging
     *            分页信息
     * @return 转让中债权列表信息
     */
    public abstract PagingResult<TransferDshEntity> transferZrsbSearch(TransferDshQuery transferDshQuery,Paging paging)
            throws Throwable;
	
}
