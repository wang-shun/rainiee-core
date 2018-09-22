package com.dimeng.p2p.modules.bid.pay.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S62.entities.T6242;
import com.dimeng.p2p.S65.entities.T6501;

import java.math.BigDecimal;
import java.sql.Connection;

/**
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  公益标
 * 修 改 人:  wangming
 * 修改时间:  15-3-10
 */
public interface DonationService extends Service {
    /**
     * 查询标的信息
     * @param loadId
     * @return
     * @throws Throwable
     */
    T6242 selectT6242(int loadId) throws Throwable;
    /**
     * 查询标的信息
     * @param loadId
     * @return
     * @throws Throwable
     */
    T6242 selectT6242(Connection connection, int loadId) throws Throwable;

    /**
     * 生成捐款订单
     * @param bidId
     * @param amount
     * @return
     * @throws Throwable
     */
    int bid(final int bidId, final BigDecimal amount) throws Throwable;
    
    T6110 selectT6110() throws Throwable;
    
    T6501 selectT6501() throws Throwable;
    
    /**
     * 生成捐款订单
     * @param bidId
     * @param amount
     * @return
     * @throws Throwable
     */
    int bid(final int bidId, final BigDecimal amount,String tranPwd) throws Throwable;
    
    /**
     * 检测捐款是否有效
     * 
     * @param bidId
     * @param amount
     * @return
     * @throws Throwable
     */
    void checkBid(final int bidId, final BigDecimal amount) throws Throwable;
}
