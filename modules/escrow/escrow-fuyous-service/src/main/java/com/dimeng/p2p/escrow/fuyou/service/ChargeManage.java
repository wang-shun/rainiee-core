package com.dimeng.p2p.escrow.fuyou.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S65.entities.T6502;
import com.dimeng.p2p.escrow.fuyou.cond.ChargeCond;
import com.dimeng.p2p.escrow.fuyou.entity.UserQueryEntity;
import com.dimeng.p2p.escrow.fuyou.entity.entities.Auth;

/**
 * 
 * 充值接口类
 * 
 * @author heshiping
 * @version [版本号, 2015年5月25日]
 */
public abstract interface ChargeManage extends Service
{
    
    /**
     * 生成充值订单并返回充值流水号
     * @param amount
     * @param source  pc,wx,ad,ios
     * @param fee 
     * @param payTpye 充值类型
     * @return
     * @throws Throwable
     */
    public abstract String addOrder(BigDecimal amount,String source,  BigDecimal fee,String payTpye) throws Throwable;
    
    /**
     * 获取认证信息
     * 
     * @return
     * @throws Throwable
     */
    public abstract Auth getAutnInfo()
        throws Throwable;
    
    /**
     * 根据流水号查询此订单是否已经被回调过
     * @param connection
     * @param serialNumber
     * @return
     * @throws Throwable
     */
    public abstract Map<String, String> selectT6501Success(String mchnt_txn_ssn)
        throws Throwable;
    
    
    /**
     * 根据流水号查询此订单
     * @param connection
     * @param serialNumber
     * @return
     * @throws Throwable
     */
    public abstract T6502 selectT6502BySsn(String mchnt_txn_ssn)
        throws Throwable;
    
    /**
     * 组装充值请求Map参数
     * @param loginId     登陆用户ID
     * @param amt         充值金额
     * @param mchntTxnSsn 充值流水号
     * @return
     * @throws Throwable
     */
    public abstract Map<String, String> createChargeUrI(String loginId, String amt, String mchntTxnSsn) throws Throwable;
    
    /**
     * 组装充值请求Map参数
     * @param loginId     登陆用户ID
     * @param amt         充值金额
     * @param mchntTxnSsn 充值流水号
     * @return
     * @throws Throwable
     */
    public Map<String, String> createChargeUrI(ChargeCond cond, BigDecimal fee) throws Throwable;
    
    /**
     * 加载用户信息查询-富友
     * 
     * @param Mchnt_cd
     *            商户代号
     * @param user_ids
     *            用户账号
     * @return UserQueryEntity 返回用户信息查询实体类
     * @throws Throwable
     */
    public abstract UserQueryEntity userChargeQuery(String Mchnt_cd, String user_ids)
        throws Throwable;
    
    /**
     * 充值返回参数
     * @param param
     * @return
     * @throws Throwable
     */
    public abstract boolean chargeRetDecode(Map<String, String> param,T6502 t6502)
        throws Throwable;
    
    /**
     * 更新T6501订单状态
     * <功能详细描述>
     * @param orderId 订单ID
     * @throws Throwable
     */
    public abstract void updateT6501(int orderId)
        throws Throwable;
    
    /**
     * 查询推广人ID
     * 
     * @param accountId
     *            用户ID
     * @return int返邀人ID
     * @throws Throwable
     */
    public abstract int selectT6311(Connection connection, int accountId)
        throws Throwable;
    
    /**
     * 查询用户充值次数
     * 
     * @param accountId
     *            用户ID
     * @return int 返回充值次数
     * @throws Throwable
     */
    public abstract int selectChargeCount(Connection connection, int accountId)
        throws Throwable;
    
    /**
     * 首次充值更新T6311表金额
     * 
     * @param amt
     *            金额
     * @param t
     *            时间
     * @param accountId
     *            用户ID
     * @throws Throwable
     */
    public abstract void updateT6311(Connection connection, BigDecimal amt, Timestamp t, int accountId)
        throws Throwable;
    
    /**
     * 查询推广人本月总数
     * 
     * @param exid
     *            推广人ID
     * @return int 返回当月推广数
     * @throws Throwable
     */
    public abstract int selectUpperLimit(Connection connection, int exid)
        throws Throwable;
    
    /**
     * 锁定推广人T6310
     * 
     * @param exid
     *            推广人ID
     * @throws Throwable
     */
    public abstract void selectT6310(Connection connection, int exid)
        throws Throwable;
    
    /**
     * 查询充值订单信息
     * 
     * @param connection
     * @param orderId
     *            充值订单ID
     * @return T6502 返回充值订单信息
     * @throws SQLException
     */
    public abstract T6502 selectT6502(Connection connection, int orderId)
        throws SQLException;
        
    /** 操作类别
     *  日志内容
     * @param type
     * @param log
     * @throws Throwable
     */
    public void writeFrontLog(String type, String log)
        throws Throwable;
}
