package com.dimeng.p2p.escrow.fuyou.service;

import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.ServiceSession;

/**
 * 
 * 提前还款
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月9日]
 */
public interface PrepaymentManage extends Service
{
    
    /**
     * 提前还款
     * @param bidId 标ID
     * @param term 期号
     * @param params 记录是否第一次提前还款
     * @return 订单ID列表
     * @throws Throwable
     */
    public int[] prepayment(final int bidId, final int term, Map<String, String> params)
        throws Throwable;
    
    /**
     * 还款出现异常时，将还款计划更新为未还
     * @param bidId 标id
     * @param term  期号
     * @throws Throwable
     */
    public void updateT6252(final int bidId, final int term)
        throws Throwable;
    
    /**
     * 查询订单是否成功
     * @param F01
     * @return
     * @throws Throwable
     */
    public void selectT6501(final int F01, ServiceSession serviceSession, Map<String, String> params)
        throws Throwable;
    
    /** 操作类别
     *  日志内容
     * @param type
     * @param log
     * @throws Throwable
     */
    public void writeFrontLog(String type, String log)
        throws Throwable;
}
