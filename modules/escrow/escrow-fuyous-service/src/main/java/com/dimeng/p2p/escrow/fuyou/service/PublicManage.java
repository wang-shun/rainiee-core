package com.dimeng.p2p.escrow.fuyou.service;

import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S62.enums.T6250_F08;
import com.dimeng.p2p.modules.bid.console.service.entity.BidReturn;

/**
 * 
 * 还款、机构垫付，公共方法
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月8日]
 */
public interface PublicManage extends Service
{
    
    /**
     * 更新T6501-流水号
     * <功能详细描述>
     * @param orderId 订单ＩＤ
     * @param mchnt_txn_ssn　流水号
     * @throws Throwable
     */
    public abstract void updateT6501(int orderId, String mchnt_txn_ssn)
        throws Throwable;
    
    /**
     * 查询单状态
     * <T6501若失败了则与第三方对账>
     * @param serviceSession
     * @param orderId 订单ID
     * @param params 相关返回集合
     * @param flag 是否可能为平台账号
     * @return
     * @throws Throwable
     */
    public void searchT6501(ServiceSession serviceSession, int orderId, Map<String, String> params, boolean flag)
        throws Throwable;
    
    /**
     * 垫付
     * @param bidId 标id
     * @param term  期号
     * @throws Throwable
     */
    public void updateT6252(final int bidId, final int term)
        throws Throwable;
    
    /**
     * 放款
     * <判断是否已存在放款订单>
     * @param bidId 标Id
     * @return
     * @throws Throwable
     */
    public BidReturn selectLoan(final int bidId)
        throws Throwable;
    
    /**
     *  手动还款
     * <判断是否已存在还款订单>
     * @param bidId 标ID
     * @param number 还款期数
     * @return
     * @throws Throwable
     */
    public int[] selectPayment(final int bidId, int number)
        throws Throwable;
    
    /**
     * 更新放款订单状态
     * @param F08
     * @param F02
     * @throws Throwable
     */
    public void updateT6250(T6250_F08 F08, int F01)
        throws Throwable;
    
    /**
     * 插入流水号
     * <T6501>
     * @param orderIds
     * @param type
     * @throws Throwable
     */
    public void updtateT6501F10(int[] orderIds, String type)
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
