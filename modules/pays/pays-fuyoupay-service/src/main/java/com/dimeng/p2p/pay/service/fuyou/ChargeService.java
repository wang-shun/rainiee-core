package com.dimeng.p2p.pay.service.fuyou;

import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S50.entities.T5020;
import com.dimeng.p2p.S61.entities.T6141;
import com.dimeng.p2p.S65.entities.T6502;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.pay.service.fuyou.entity.ChargeOrder;

public abstract interface ChargeService extends Service
{
    
    /**
     * 添加充值订单
     * @param amount 充值金额
     * @param payCompanyCode  支付公司代号
     * @return
     * 
     * @throws Throwable
     */
    public abstract ChargeOrder addOrder(BigDecimal amount, int payCompanyCode, String bankNum, String payWay,
        int bankId, String city,String source)
            throws Throwable;
            
    /**
     * 获取充值订单
     * @param orderId
     * @return
     * @throws Throwable
     */
    public abstract ChargeOrder getChargeOrder(int orderId)
        throws Throwable;
        
    /**
     * 根据银行ID查询对应的银行编码
     * @param bankId
     * @return
     * @throws Throwable
     */
    public T5020 selectT5020(int bankId)
        throws Throwable;
        
    /**
     * 查询用户身份证信息
     * @param userId
     * @return
     * @throws Throwable
     */
    public T6141 selectT6141(int userId, boolean isSession)
        throws Throwable;
        
    /**
     * 修改订单状态
     * @param orderId
     * @param F03
     * @throws Throwable
     */
    public void updateT6501(int orderId, T6501_F03 F03)
        throws Throwable;
        
    /**
     * 获取充值订单详情
     * @param connection
     * @param F01
     * @return
     * @throws Throwable
     */
    public T6502 selectT6502(int F01)
        throws Throwable;
        
    public void updateT6114(String bankNum)
        throws Throwable;
        
    /**
     * 判断用户是否添加银行卡
     * @return
     * @throws Throwable
     */
    boolean isBindleBanked()
        throws Throwable;
}
