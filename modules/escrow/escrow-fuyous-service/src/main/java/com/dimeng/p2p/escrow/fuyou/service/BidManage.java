package com.dimeng.p2p.escrow.fuyou.service;

import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S65.entities.T6514;
import com.dimeng.p2p.S65.entities.T6554;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.escrow.fuyou.cond.TbdzCond;
import com.dimeng.p2p.escrow.fuyou.entity.console.TbdzEntity;
import com.dimeng.p2p.modules.bid.pay.service.TenderManage;

/**
 * 
 * 标接口实
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月23日]
 */
public interface BidManage extends TenderManage
{
    /**
     * 根据标号获取该标的投标记录是否存在已放款或已流标的情况 如执行多次放款时，需校验是否已存在不放款的情况
     * 如执行多次不放款时，需校验是否存在房款的情况
     * 
     * @param id
     * @param flag
     * @return
     * @throws Throwable
     */
    public abstract boolean getCheck(int id, String flag)
        throws Throwable;
    
    /**
     * 插入流水号
     * <功能详细描述>
     * @param orderId
     * @param mchnt_txn_ssn
     * @throws Throwable
     */
    public void updateT6501(int orderId, String mchnt_txn_ssn)
        throws Throwable;
    
    /**
     * 获取投标对账信息 
     * <功能详细描述>
     * @param tbdzCond
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<TbdzEntity> search(TbdzCond tbdzCond, Paging paging)
        throws Throwable;
    
    /**
     * 根据订单号获取捐赠订单详情
     * <功能详细描述>
     * @param orderId
     * @return
     * @throws Throwable
     */
    public abstract T6554 selectT6554(int orderId)
        throws Throwable;
    
    /**
     * 用户第三方标识
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract String selectT6119(int usr_id)
        throws Throwable;
    
    /**
     * 查询订单信息
     * <功能详细描述>
     * @param orderId
     * @throws Throwable
     */
    public T6501_F03 selectT6501(int orderId)
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
     * 根据订单查询T6514
     * @param F01
     * @return
     * @throws Throwable 
     */
    public T6514 selectT6514(int F01) throws Throwable;
}
