package com.dimeng.p2p.modules.bid.user.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S62.entities.T6260;

/**
 * 债权转入购买
 */
public abstract interface TenderTransferManage extends Service {

	/**
	 * 用户主动购买债权
	 * 
	 * @param transferId
	 *            债权转入申请ID
	 * @return 订单ID
	 * @throws Throwable
	 */
	public abstract int purchase(final int transferId) throws Throwable;
    
    /** 操作类别
     *  日志内容
     * @param type
     * @param log
     * @throws Throwable
     */
    public void writeFrontLog(String type, String log)
        throws Throwable;
    
    /**
	 * 判断用户是否可以主动购买债权
	 * 
	 * @param transferId
	 *            债权转入申请ID
	 * @throws Throwable
	 */
	public abstract void checkPurchase(final int transferId) throws Throwable;
	
	
    /**
     * 债权转让申请信息
     * <功能详细描述>
     * @param zqzrId
     * @return
     * @throws Throwable
     */
    public T6260 getT6260(int zqzrId)
        throws Throwable;

}
