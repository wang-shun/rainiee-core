package com.dimeng.p2p.escrow.fuyou.service;

import java.math.BigDecimal;
import java.util.Map;

import com.dimeng.framework.service.Service;

/**
 * 
 * 富友托管充值成功通知
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月1日]
 */
public abstract interface InformManage extends Service
{
    /**
     * 验签返回信息
     * <功能详细描述>
     * @param params
     * @return
     * @throws Throwable 
     */
    public boolean informReturnDecoder(Map<String, String> params)
        throws Throwable;
    
    /**
     * 响应签名
     * <功能详细描述>
     * @param params
     * @throws Exception 
     * @throws Throwable 
     */
    public String encryptByRSA(Map<String, String> params)
        throws Exception, Throwable;
    
    /**
     * 查询订单信息
     * <功能详细描述>
     * @param mchnt_txn_ssn
     * @return
     * @throws Throwable
     */
    public Map<String, String> selectT6501(String mchnt_txn_ssn, boolean flag)
        throws Throwable;
    
    /**
     * POSS机充值单
     * <功能详细描述>
     * @param params
     * @return
     * @throws Throwable
     */
    public void addOrder(Map<String, String> params)
        throws Throwable;
    
    /**
     * 委托提现
     * <富友托管委托提现>
     * @param params
     * @return
     * @throws Throwable
     */
    public void addOrderEntrust(Map<String, String> params)
        throws Throwable;
    
    /**
     * 更新提现订单
     * <功能详细描述>
     * @param params
     * @return
     * @throws Throwable
     */
    public int updateOrder(Map<String, String> params)
        throws Throwable;
    
    /**
     * 更新T6501\T6502
     * <充值功-插入流水号，更新T6502-手续费>
     * @param MCHNT_TXN_SSN 流水号
     * @param orderId 订单号
     * @param mchnt_fee 充值手续费
     * @throws Throwable
     */
    public abstract void updateT6502(String MCHNT_TXN_SSN, int orderId, BigDecimal mchnt_amt)
        throws Throwable;
    
    /**
     * 查询T6502.F04应收手续费
     * <充值订单>
     * @param orderId
     * @return
     * @throws Throwable
     */
    public BigDecimal selectT6502(int orderId)
        throws Throwable;
    
    /**
     * 查询T6502.F04应收手续费
     * <提现订单>
     * @param orderId
     * @return
     * @throws Throwable
     */
    public void updateT6502(int orderId)
        throws Throwable;
    
    /**
     * 查询T6503.F04应收手续费
     * <提现订单>
     * @param orderId
     * @return
     * @throws Throwable
     */
    public void updateT6503(int orderId)
        throws Throwable;
    
    /**
     * 提现退票通知
     * <功能详细描述>
     * @param params
     * @return
     * @throws Throwable
     */
    public boolean refundWithderaw(int orderId, String mchnt_txn_ssn)
        throws Throwable;
}
