/*package com.dimeng.p2p.app.servlets.pay.service.fuyou.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6119;
import com.dimeng.p2p.S65.entities.T6502;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S71.entities.T7101;
import com.dimeng.p2p.escrow.fuyou.cond.ChargeCond;
import com.dimeng.p2p.escrow.fuyou.entity.UserQueryEntity;
import com.dimeng.p2p.escrow.fuyou.entity.entities.Auth;

*//**
 * 
 * 充值接口类
 * 
 * @author heshiping
 * @version [版本号, 2015年5月25日]
 *//*
public abstract interface ChargeManage extends Service
{
    
    *//**
     * 添加充值订单
     * 
     * @param amount
     *            金额
     * @param payCompanyCode
     *            支付公司代号
     * @param serialNumber
     *            流水号
     * @return ChargeOrder 返回订单信息表
     * @throws Throwable
     *//*
    public abstract String addOrder(BigDecimal amount, int payCompanyCode, BigDecimal fee)
        throws Throwable;
    
    *//**
     * 更新订单状态
     * <充值成功>
     * @param t6501_F03 状态
     * @param orderId   订单ID
     * @param MCHNT_TXN_SSN 流水号
     * @throws Throwable
     *//*
    public abstract void updateOrderStatus(T6501_F03 t6501_F03, int orderId, String MCHNT_TXN_SSN)
        throws Throwable;
    
    *//**
     * 获取认证信息
     * 
     * @return
     * @throws Throwable
     *//*
    public abstract Auth getAutnInfo()
        throws Throwable;
    
    *//**
     * 查询平台信息
     * 
     * @return T7101 表
     * @throws Throwable
     *//*
    public abstract T7101 selectT7101(Connection connection)
        throws Throwable;
    
    *//**
     * 第三方账号查询
     * 
     * @param userId
     * @return T6119返回第三方账号信息
     * @throws Throwable
     *//*
    public abstract T6119 selectT6119(Connection connection, int userId)
        throws Throwable;
    
    *//**
     * 根据流水号查询此订单是否已经被回调过
     * @param connection
     * @param serialNumber
     * @return
     * @throws Throwable
     *//*
    public abstract Map<String, String> selectT6501Success(String mchnt_txn_ssn)
        throws Throwable;
    
    *//**
     * 充值条件
     * <功能详细描述>
     * @param cond
     * @param flag
     * @param fee
     * @param mchnt_txn_ssn
     * @return
     * @throws Throwable
     *//*
    public abstract Map<String, String> createChargeUrI(ChargeCond cond, BigDecimal fee)
        throws Throwable;
    
    *//**
     * 加载用户信息查询-富友
     * 
     * @param Mchnt_cd
     *            商户代号
     * @param user_ids
     *            用户账号
     * @return UserQueryEntity 返回用户信息查询实体类
     * @throws Throwable
     *//*
    public abstract UserQueryEntity userChargeQuery(String Mchnt_cd, String user_ids)
        throws Throwable;
    
    *//**
     * 充值返回参数
     * @param param
     * @return
     * @throws Throwable
     *//*
    public abstract boolean chargeRetDecode(Map<String, String> param)
        throws Throwable;
    
    *//**
     * 更新T6501订单状态
     * <功能详细描述>
     * @param orderId 订单ID
     * @throws Throwable
     *//*
    public abstract void updateT6501(int orderId)
        throws Throwable;
    
    *//**
     * 查询推广人ID
     * 
     * @param accountId
     *            用户ID
     * @return int返邀人ID
     * @throws Throwable
     *//*
    public abstract int selectT6311(Connection connection, int accountId)
        throws Throwable;
    
    *//**
     * 查询用户充值次数
     * 
     * @param accountId
     *            用户ID
     * @return int 返回充值次数
     * @throws Throwable
     *//*
    public abstract int selectChargeCount(Connection connection, int accountId)
        throws Throwable;
    
    *//**
     * 首次充值更新T6311表金额
     * 
     * @param amt
     *            金额
     * @param t
     *            时间
     * @param accountId
     *            用户ID
     * @throws Throwable
     *//*
    public abstract void updateT6311(Connection connection, BigDecimal amt, Timestamp t, int accountId)
        throws Throwable;
    
    *//**
     * 查询推广人本月总数
     * 
     * @param exid
     *            推广人ID
     * @return int 返回当月推广数
     * @throws Throwable
     *//*
    public abstract int selectUpperLimit(Connection connection, int exid)
        throws Throwable;
    
    *//**
     * 锁定推广人T6310
     * 
     * @param exid
     *            推广人ID
     * @throws Throwable
     *//*
    public abstract void selectT6310(Connection connection, int exid)
        throws Throwable;
    
    *//**
     * 查询充值订单信息
     * 
     * @param connection
     * @param orderId
     *            充值订单ID
     * @return T6502 返回充值订单信息
     * @throws SQLException
     *//*
    public abstract T6502 selectT6502(Connection connection, int orderId)
        throws SQLException;
    
    *//**
     * 推广受益处理
     * 
     * @param serviceSession
     * @param orderId
     *            充值订单ID
     * @throws Throwable
     *//*
    public abstract void expand(ServiceSession serviceSession, int orderId)
        throws Throwable;
    
    *//**
     * 更新T6501\T6502
     * <充值功-插入流水号，更新T6502-手续费>
     * @param MCHNT_TXN_SSN 流水号
     * @param orderId 订单号
     * @param mchnt_fee 充值手续费
     * @throws Throwable
     *//*
    public abstract void updateT6502(String MCHNT_TXN_SSN, int orderId, BigDecimal mchnt_fee)
        throws Throwable;
    
    *//** 操作类别
     *  日志内容
     * @param type
     * @param log
     * @throws Throwable
     *//*
    public void writeFrontLog(String type, String log)
        throws Throwable;
}
*/