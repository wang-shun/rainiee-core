package com.dimeng.p2p.repeater.score;

import java.io.OutputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S63.entities.T6355;
import com.dimeng.p2p.S63.entities.T6359;
import com.dimeng.p2p.S63.entities.T6360;
import com.dimeng.p2p.S63.enums.T6359_F08;
import com.dimeng.p2p.S63.enums.T6360_F03;
import com.dimeng.p2p.repeater.score.entity.MallRefund;
import com.dimeng.p2p.repeater.score.entity.OperationLog;
import com.dimeng.p2p.repeater.score.entity.ScoreOrderDetailRecord;
import com.dimeng.p2p.repeater.score.entity.ScoreOrderMainRecord;
import com.dimeng.p2p.repeater.score.entity.ScoreOrderStatistics;
import com.dimeng.p2p.repeater.score.query.OrderDetQuery;
import com.dimeng.p2p.repeater.score.query.ScoreOrderQuery;

/**
 * 订单管理功能服务层
 * 
 * @author  jiangly
 * @version  [版本号, 2015年9月14日]
 */
public interface ScoreOrderManage extends Service
{
    
    /**
     * 查询积分分类
     * @return
     */
    public abstract List<Map<String, Object>> queryScoreOrderStatus()
        throws Throwable;
    
    /**
     * 查询积分主订单列表
     * @param scoreOrderQuery
     * @param page
     * @return
     */
    public abstract PagingResult<ScoreOrderMainRecord> queryOrderMainList(ScoreOrderQuery scoreOrderQuery, Paging page)
        throws Throwable;
    
    /**
     * 查询积分主订单列表数量
     * @param scoreOrderQuery
     * @return
     */
    public abstract int queryOrderListCount(ScoreOrderQuery scoreOrderQuery)
        throws Throwable;
    
    /**
     * 查询积分主订单列表数量
     * @param scoreOrderQuery
     * @return
     */
    public abstract ScoreOrderStatistics queryScoreOrderStatistics(ScoreOrderQuery scoreOrderQuery)
        throws Throwable;
    
    /**
     * 查询积分明细订单
     * @param orderNo
     * @return
     */
    public abstract ScoreOrderDetailRecord queryOrderDetailInfo(String orderNo)
        throws Throwable;
    
    /**
     * 查询订单明细
     * @param orderNo
     * @return
     */
    public abstract List<OperationLog> getOperationLogs(String orderNo)
        throws Throwable;
    
    /**
     * 查询收货信息
     * @param receiveId
     * @return
     */
    public abstract T6355 queryT6355(Integer receiveId)
        throws Throwable;
    
    /**
     * 导出订单信息列表
     * <功能详细描述>
     * @param users
     * @param outputStream
     * @param charset
     * @throws Throwable
     */
    public void export(ScoreOrderMainRecord[] mainRecords, OutputStream outputStream, String charset, String state)
        throws Throwable;
    
    /** 更新订单详情信息,无事物专用
     * <功能详细描述>
     * @param user
     * @throws Throwable
     */
    public abstract void updateOrderInfo(T6359 t6359, T6360 t6360)
        throws Throwable;
    
    /** 更新订单详情信息,有事物专用
     * <功能详细描述>
     * @param user
     * @throws Throwable
     */
    public abstract void updateOrderInfo(T6359 t6359, Connection connection)
        throws Throwable;
    
    /** 更新订单详情信息
     * <功能详细描述>
     * @param user
     * @throws Throwable
     */
    public abstract void writeOrderOperateType(Connection connection, T6360 t6360)
        throws Throwable;
    
    /**
     * 查询退款订单列表
     * @param scoreOrderQuery
     * @param page
     * @return
     */
    public abstract PagingResult<MallRefund> queryOrderRefundList(ScoreOrderQuery scoreOrderQuery, Paging page)
        throws Throwable;
    
    /**
     * 导出退款信息列表
     * <功能详细描述>
     * @param mallRefunds
     * @param outputStream
     * @param charset
     * @throws Throwable
     */
    public void exportMallRefund(MallRefund[] mallRefunds, OutputStream outputStream, String charset)
        throws Throwable;
    
    /** 退款
     * <功能详细描述>
     * @param id 平台商城订单明细表id
     * @param reason 拒绝退款原因
     * @param status 退款或拒绝退款
     * @return T6501ID
     * @throws Throwable
     */
    public int refund(int id, String reason, String status)
        throws Throwable;
    
    /** 审核不通过(余额购买)
     * <功能详细描述>
     * @param id 平台商城订单明细表id
     * @param reason 审核不通过原因
     * @return T6501ID
     * @throws Throwable
     */
    public int nopassForBalance(int scoreOrderId)
        throws Throwable;
    
    /** 审核不通过(积分兑换)
     * <功能详细描述>
     * @param id 平台商城订单明细表id
     * @param reason 审核不通过原因
     * @return T6501ID
     * @throws Throwable
     */
    public void nopassForScore(int scoreOrderId)
        throws Throwable;
    
    /** 保存操作日志
     * <功能详细描述>
     * @param id 
     * @param reason 原因
     * @param T6360_F03 
     * @throws Throwable
     */
    public void saveLog(int id, String reason, T6360_F03 F03)
        throws Throwable;
    
    /**
     * 查询批量发货订单信息
     * <功能详细描述>
     * @param ids
     * @return
     * @throws Throwable
     */
    public abstract ScoreOrderMainRecord[] queryBatchSippingList(String ids)
        throws Throwable;
    
    /** 更新发货订单信息
     * <功能详细描述>
     * @param updateList
     * @throws Throwable
     */
    public abstract void updateOrderMessage(List<T6359> updatet6359, List<T6360> updatet6360)
        throws Throwable;
    
    /**
     * （订单审核不通过时）通知用户（短信和站内信）
     * <功能详细描述>
     * @param title 站内信的名称
     * @param variable 站内信和短信内容格式模板来源
     * @param envionment
     * @param scoreOrderId 订单ID
     * @throws Throwable
     */
    public abstract void infoUserNoPass(ConfigureProvider configureProvider, Envionment envionment, int scoreOrderId,
        String reason)
        throws Throwable;
    
    /**
     * （发货时）通知用户（短信和站内信）
     * <功能详细描述>
     * @param title 站内信的名称
     * @param variable 站内信和短信内容格式模板来源
     * @param envionment
     * @param scoreOrderId 订单ID
     * @throws Throwable
     */
    public abstract void infoUserSipping(ConfigureProvider configureProvider, Envionment envionment, int scoreOrderId)
        throws Throwable;
    
    /**
     * 查询平台商城订单明状态
     * @param id
     * @return
     */
    public abstract T6359_F08 queryT6359F08(Integer id)
        throws Throwable;
    
    /**
     * 查询平台商城订单明细表
     * @param id
     * @return
     */
    public abstract T6359 selectT6359(int id)
        throws Throwable;
    
    /**
     * 查询商品订单详情列表
     * @param orderDetQuery
     * @param page
     * @return
     */
    public abstract PagingResult<ScoreOrderMainRecord> queryOrderDetailList(OrderDetQuery orderDetQuery, Paging page)
        throws Throwable;
    
    /**
     * 查询商品订单详情数量
     * @param orderDetQuery
     * @return
     */
    public abstract ScoreOrderStatistics queryOrderDetailStatistics(OrderDetQuery orderDetQuery)
        throws Throwable;
    
    /**
     * 查询商品
     * @param id
     * @return
     */
    public abstract ScoreOrderDetailRecord queryCommodityInfo(int id)
        throws Throwable;
}