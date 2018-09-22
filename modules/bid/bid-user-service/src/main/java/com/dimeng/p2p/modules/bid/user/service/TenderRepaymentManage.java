package com.dimeng.p2p.modules.bid.user.service;

import com.dimeng.framework.service.Service;

/**
 * 还款
 * 
 */
public abstract interface TenderRepaymentManage extends Service {
	/**
	 * 用户主动还款,将还款计划更新为HKZ
	 * 
	 * @param bidId
	 *            标ID
	 * @param term
	 *            期号
	 * @return 订单ID列表
	 * @throws Throwable
	 */
	public int[] repayment(final int bidId, final int term) throws Throwable;
	/**
	 * 还款出现异常时，将还款计划更新为未还
	 * @param bidId 标id
	 * @param term	期号
	 * @throws Throwable
	 */
	public void updateT6252(final int bidId, final int term) throws Throwable;
	/**
	 * 用户主动还款,不更新还款计划状态
	 * 
	 * @param bidId
	 *            标ID
	 * @param term
	 *            期号
	 * @return 订单ID列表
	 * @throws Throwable
	 */
	public int[] payment(final int bidId, final int term) throws Throwable;
    
    /** 操作类别
     *  日志内容
     * @param type
     * @param log
     * @throws Throwable
     */
    public void writeFrontLog(String type, String log)
        throws Throwable;
    
    /**
	 * 检查用户是否可还款
	 * 
	 * @param bidId
	 *            标ID
	 * @param term
	 *            期号
	 * @return 订单ID列表
	 * @throws Throwable
	 */
	public void checkRepayment(int bidId, int term) throws Throwable;
}
