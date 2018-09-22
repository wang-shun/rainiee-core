package com.dimeng.p2p.escrow.fuyou.service;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S62.entities.T6252;
import com.dimeng.p2p.S62.enums.T6250_F08;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6505;
import com.dimeng.p2p.S65.entities.T6506;
import com.dimeng.p2p.S65.entities.T6521;
import com.dimeng.p2p.escrow.fuyou.executor.FYAdvanceExecutor;
import com.dimeng.p2p.escrow.fuyou.executor.FYPTAdvanceExecutor;
import com.dimeng.p2p.escrow.fuyou.executor.FYPrepaymentExecutor;
import com.dimeng.p2p.escrow.fuyou.executor.FYRepaymentExecutor;
import com.dimeng.p2p.escrow.fuyou.executor.FYTenderConfirmExecutor;

/**
 * 
 * 定时自动任务接口
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月17日]
 */
public interface AutoManage extends Service
{
    
    /**
     * 
     * @param timestamp 时间
     * @param number 条数
     * @return
     * @throws Throwable
     */
    /**
     * 查询未确认的单
     * <T6501>
     * @param orderType 类型编码
     * @param timestamp 时间<当前时间前的单>
     * @param number 条数
     * @return
     * @throws Throwable
     */
    public List<T6501> searchFailedOrder(int orderType, Timestamp timestamp, int number)
        throws Throwable;
    
    /**
     * 对账
     * @param serviceSession
     * @param t6501 订单
     * @param flag 类型
     * @return
     * @throws Throwable
     */
    public boolean selectFuyou(Connection connection, ServiceSession serviceSession, T6501 t6501, boolean flag)
        throws Throwable;
    
    /**
     * 对账 <投标>
     * <对平台所以末成功的作流单处理>
     * @param serviceSession
     * @param timestamp
     * @param number
     * @return
     * @throws Throwable
     */
    public void searchBidFailedOrder(ServiceSession serviceSession, Timestamp timestamp, int number)
        throws Throwable;
    
    /**
     * 修改放示订单状态
     * <功能详细描述>
     * @param F08
     * @param F01
     * @throws Throwable
     */
    public void updateT6250(T6250_F08 F08, int F01)
        throws Throwable;
    
    /**
     * 放款订单信息
     * <功能详细描述>
     * @param F01
     * @return
     * @throws Throwable
     */
    public T6505 selectT6505(Connection connection, int F01)
        throws Throwable;
    
    /**
    * 还款订单信息
    * <功能详细描述>
    * @param F01
    * @return
    * @throws Throwable
    */
    public T6506 selectT6506(Connection connection, int F01)
        throws Throwable;
    
    /**
     * 还款订单信息
     * <功能详细描述>
     * @param F01
     * @return
     * @throws Throwable
     */
    public T6252 selectT6252(Connection connection, int t6506_F07, int t6506_F05, int t6506_F04)
        throws Throwable;
    
    /**
     * 查询标状态
     * T6230
     * @param F01
     * @return
     * @throws Throwable
     */
    public String selectT6230(Connection connection, int F01)
        throws Throwable;
    
    /**
     * 修改订单
     * <功能详细描述>
     * @param F01
     * @param F03
     * @throws Throwable
     */
    public void updateT6501(Connection connection, int F01, String F03, String F11)
        throws Throwable;
    
    /**
     * 查询平台ID
     * @return
     * @throws Throwable
     */
    public int selectT7101(Connection connection)
        throws Throwable;
    
    /**
     * 查询提前还款订单
     * T6521
     * @param F01
     * @return
     * @throws Throwable
     */
    public T6521 selectT6521(Connection connection, int F01)
        throws Throwable;
    
    /**
     * 查询垫付订单状态F07
     * T6514
     * @param F01
     * @return
     * @throws Throwable
     */
    public String selectT6514(Connection connection, int F01)
        throws Throwable;
    
    /**
     * 垫付自动对账处理
     * 
     * @return
     * @throws Throwable
     */
    public void autoAdvanceDZ(FYPTAdvanceExecutor PTExecutor, FYAdvanceExecutor JGExecutor,
        ServiceSession serviceSession, T6501 t6501, Map<String, String> params)
        throws Throwable;
    
    /**
     * 放款自动对账处理
     * @param executor
     * @param serviceSession
     * @param t6501
     * @param params
     * @return
     * @throws Throwable
     */
    public void autoLoanDZ(FYTenderConfirmExecutor executor, ServiceSession serviceSession, T6501 t6501,
        Map<String, String> params)
        throws Throwable;
    
    /**
     * 手动还款对账处理
     * @param executor
     * @param serviceSession
     * @param t6501
     * @param params
     * @return
     * @throws Throwable
     */
    public void autoPaymentDZ(FYRepaymentExecutor executor, ServiceSession serviceSession, T6501 t6501,
        Map<String, String> params)
        throws Throwable;
    
    /**
     * 提前还款对账处理
     * @param executor
     * @param serviceSession
     * @param t6501
     * @param params
     * @return
     * @throws Throwable
     */
    public void antoPrepaymentDZ(FYPrepaymentExecutor executor, ServiceSession serviceSession, T6501 t6501,
        Map<String, String> params)
        throws Throwable;
}
