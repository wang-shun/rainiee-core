package com.dimeng.p2p.escrow.fuyou.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.escrow.fuyou.executor.FYAdvanceExecutor;
import com.dimeng.p2p.escrow.fuyou.executor.FYPTAdvanceExecutor;
import com.dimeng.p2p.escrow.fuyou.executor.FYPrepaymentExecutor;
import com.dimeng.p2p.escrow.fuyou.executor.FYRepaymentExecutor;
import com.dimeng.p2p.order.DonationExecutor;
import com.dimeng.p2p.order.MallChangeExecutor;
import com.dimeng.p2p.order.MallRefundExecutor;

/**
 * 
 * 相关查询接口
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月25日]
 */
public interface QueryManage extends Service
{
    
    /**
     * 订单信息
     * <功能详细描述>
     * @param orderId
     * @return
     * @throws Throwable
     */
    public T6501 selectT6501(int orderId)
        throws Throwable;
    
    /**
     * 更新订单
     * <功能详细描述>
     * @param F01 订单ID
     * @param F11 对账是否
     * @param F12 备注
     * @throws Throwable
     */
    public void updateT6501(int F01, String F11, String F12, String F03)
        throws Throwable;
    
    /**
     * 提现失败订单
     * <并增加T6130提现失败申请记录>
     * @param orderId 订单ID
     * @param F12 原因
     * @throws Throwable
     */
    public void updateT6501TxSb(int orderId, String F12)
        throws Throwable;
    
    /**
     * 查询用户第三方ID
     * <功能详细描述>
     * @param F01
     * @return
     * @throws Throwable
     */
    public String selectT6119(int F01)
        throws Throwable;
    
    /**
     * 对账
     * @param serviceSession
     * @param t6501 订单
     * @param flag 类型
     * @return
     * @throws Throwable
     */
    public boolean selectFuyou(ServiceSession serviceSession, T6501 t6501, Map<String, String> params)
        throws Throwable;
    
    /**
     * 还款手动对账处理
     * <20004-还款手动对账>
     * @param serviceSession
     * @param connection
     * @param t6501 订单信息
     * @param params
     * @param flag 第三方成功是否 true:成功;false:未成功
     * @param executor 执行器
     * @throws Throwable
     */
    public void bid20004(ServiceSession serviceSession, T6501 t6501, Map<String, String> params, boolean flag,
        FYRepaymentExecutor executor)
        throws Throwable;
    
    /**
     * 提前还款手动对账处理
     * <30004-提前还款手动对账>
     * @param serviceSession
     * @param connection
     * @param t6501 订单信息
     * @param params
     * @param flag 第三方成功是否 true:成功;false:未成功
     * @param executor 执行器
     * @throws Throwable
     */
    public void bid30004(ServiceSession serviceSession, T6501 t6501, Map<String, String> params, boolean flag,
        FYPrepaymentExecutor executor)
        throws Throwable;
    
    /**
     * 平台垫付对账处理
     * <10004-平台垫付手动对账>
     * @param serviceSession
     * @param connection
     * @param t6501 订单信息
     * @param params
     * @param flag 第三方成功是否 true:成功;false:未成功
     * @param executor 执行器
     * @throws Throwable
     */
    public void bid10004PT(ServiceSession serviceSession, T6501 t6501, Map<String, String> params, boolean flag,
        FYPTAdvanceExecutor executor, HttpServletRequest request)
        throws Throwable;
    
    /**
     * 机构垫付对账处理
     * <10004-机构垫付手动对账>
     * @param serviceSession
     * @param connection
     * @param t6501 订单信息
     * @param params
     * @param flag 第三方成功是否 true:成功;false:未成功
     * @param executor 执行器
     * @throws Throwable
     */
    public void bid10004JG(ServiceSession serviceSession, T6501 t6501, Map<String, String> params, boolean flag,
        FYAdvanceExecutor executor, HttpServletRequest request)
        throws Throwable;
    
    /**
     * 购买债权对账处理
     * <20005-购买债权手动对账>
     * @param serviceSession
     * @param connection
     * @param t6501 订单信息
     * @param params
     * @param flag 第三方成功是否 true:成功;false:未成功
     * @throws Throwable
     */
    public void bid20005(ServiceSession serviceSession, T6501 t6501, Map<String, String> params, boolean flag,
        HttpServletRequest request)
        throws Throwable;
    
    /**
     * 公益捐款手动对账
     * <50002-公益捐款手动对账>
     * @param serviceSession
     * @param connection
     * @param t6501 订单信息
     * @param params
     * @param flag 第三方成功是否 true:成功;false:未成功
     * @throws Throwable
     */
    public void bid50002(ServiceSession serviceSession, T6501 t6501, Map<String, String> params, boolean flag,
        DonationExecutor executor)
        throws Throwable;
    
    /**
     * 商品购买手动对账
     * <50003-商品购买手动对账>
     * @param serviceSession
     * @param t6501
     * @param params
     * @param flag
     * @param executor
     * @throws Throwable
     */
    public void bid50003(ServiceSession serviceSession, T6501 t6501, Map<String, String> params, boolean flag,
        MallChangeExecutor executor)
        throws Throwable;
    
    /**
     * 商品退款手动对账
     * <50004-商品退款手动对账>
     * @param serviceSession
     * @param t6501
     * @param params
     * @param flag
     * @param executor
     * @throws Throwable
     */
    public void bid50004(ServiceSession serviceSession, T6501 t6501, Map<String, String> params, boolean flag,
        MallRefundExecutor executor)
        throws Throwable;
    
    /**
     * 不良债权购买对账处理
     * <20015-不良债权购买对账>
     * @param serviceSession
     * @param connection
     * @param t6501 订单信息
     * @param params
     * @param flag 第三方成功是否 true:成功;false:未成功
     * @throws Throwable
     */
    public String bid20015(ServiceSession serviceSession, T6501 t6501, Map<String, String> params, boolean flag)
        throws Throwable;
}
