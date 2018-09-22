package com.dimeng.p2p.modules.bid.user.service;

import com.dimeng.framework.service.Service;

/**
 * 提前还款
 * 
 */
public abstract interface TenderPrepaymentManage extends Service {
    /**
     * 提前还款
     * 
     * @param bidId
     *            标ID
     * @param term
     *            期号
     * @return 订单ID列表
     * @throws Throwable
     */
    public int[] prepayment(final int bidId, final int term)
        throws Throwable;
    
    /** 操作类别
     *  日志内容
     * @param type
     * @param log
     * @throws Throwable
     */
    public void writeFrontLog(String type, String log)
        throws Throwable;
    
    /**
     * 检查是否可提前还款
     * 
     * @param bidId
     *            标ID
     * @param term
     *            期号
     * @return 订单ID列表
     * @throws Throwable
     */
    public void checkPrepayment(final int bidId, final int term)
        throws Throwable;
}
