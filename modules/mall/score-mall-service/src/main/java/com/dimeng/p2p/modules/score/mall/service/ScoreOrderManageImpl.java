package com.dimeng.p2p.modules.score.mall.service;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6105;
import com.dimeng.p2p.S61.entities.T6106;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6106_F05;
import com.dimeng.p2p.S61.enums.T6123_F05;
import com.dimeng.p2p.S63.entities.T6350;
import com.dimeng.p2p.S63.entities.T6351;
import com.dimeng.p2p.S63.entities.T6352;
import com.dimeng.p2p.S63.entities.T6355;
import com.dimeng.p2p.S63.entities.T6359;
import com.dimeng.p2p.S63.entities.T6360;
import com.dimeng.p2p.S63.enums.T6350_F07;
import com.dimeng.p2p.S63.enums.T6351_F11;
import com.dimeng.p2p.S63.enums.T6352_F06;
import com.dimeng.p2p.S63.enums.T6359_F08;
import com.dimeng.p2p.S63.enums.T6360_F03;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6528;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.common.SMSUtils;
import com.dimeng.p2p.common.enums.ConsoleLogType;
import com.dimeng.p2p.common.enums.YesOrNo;
import com.dimeng.p2p.repeater.score.ScoreOrderManage;
import com.dimeng.p2p.repeater.score.entity.MallRefund;
import com.dimeng.p2p.repeater.score.entity.OperationLog;
import com.dimeng.p2p.repeater.score.entity.ScoreOrderDetailRecord;
import com.dimeng.p2p.repeater.score.entity.ScoreOrderMainRecord;
import com.dimeng.p2p.repeater.score.entity.ScoreOrderStatistics;
import com.dimeng.p2p.repeater.score.query.OrderDetQuery;
import com.dimeng.p2p.repeater.score.query.ScoreOrderQuery;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.p2p.variables.defines.smses.SmsVaribles;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateTimeParser;

/**
 * <积分设置>
 * <功能详细描述>
 * 
 * @author  beiweiyuan
 * @version  [版本号, 2015年12月14日]
 */
public class ScoreOrderManageImpl extends AbstractMallService implements ScoreOrderManage
{
    
    public ScoreOrderManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public List<Map<String, Object>> queryScoreOrderStatus()
    {
        ArrayList<Map<String, Object>> list = null;
        return list;
    }
    
    @Override
    public PagingResult<ScoreOrderMainRecord> queryOrderMainList(ScoreOrderQuery scoreOrderQuery, Paging page)
        throws Throwable
    {
		return null;}
    
    @Override
    public int queryOrderListCount(ScoreOrderQuery scoreOrderSeach)
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public ScoreOrderDetailRecord queryOrderDetailInfo(String orderNo)
        throws Throwable
    {
		return null;}
    
    @Override
    public List<OperationLog> getOperationLogs(String orderNo)
        throws Throwable
    {
		return null;}
    
    @Override
    public T6355 queryT6355(Integer receiveId)
        throws Throwable
    {
		return null;}
    
    @Override
    public void export(ScoreOrderMainRecord[] mainRecords, OutputStream outputStream, String charset, String state)
        throws Throwable
    {}
    
    @Override
    public void updateOrderInfo(T6359 t6359, Connection connectionTmp)
        throws Throwable
    {}
    
    @Override
    public void updateOrderInfo(T6359 t6359, T6360 t6360)
        throws Throwable
    {}
    
    private void getUpdateOrderSql(T6359 t6359, StringBuffer updateSql, List<Object> param)
    {}
    
    /** {@inheritDoc} */
    
    @Override
    public void writeOrderOperateType(Connection connection, T6360 t6360)
        throws Throwable
    {}
    
    /** {@inheritDoc} */
    
    @Override
    public ScoreOrderStatistics queryScoreOrderStatistics(ScoreOrderQuery scoreOrderQuery)
        throws Throwable
    {
		return null;}
    
    private String getOrderMainSQL(ScoreOrderQuery scoreOrderQuery, ArrayList<Object> parameters)
        throws Throwable
    {
		return null;}
    
    /**
     * 查询退款订单列表
     * @param scoreOrderQuery
     * @param page
     * @return
     */
    @Override
    public PagingResult<MallRefund> queryOrderRefundList(ScoreOrderQuery scoreOrderQuery, Paging page)
        throws Throwable
    {
		return null;}
    
    /**
     * 导出退款信息列表
     * <功能详细描述>
     * @param mallRefunds
     * @param outputStream
     * @param charset
     * @throws Throwable
     */
    @Override
    public void exportMallRefund(MallRefund[] mallRefunds, OutputStream outputStream, String charset)
        throws Throwable
    {}
    
    /** 退款
     * <功能详细描述>
     * @param id 平台商城订单明细表id
     * @param reason 拒绝退款原因
     * @param status 退款或拒绝退款
     * @return T6501ID
     * @throws Throwable
     */
    @Override
    public int refund(int id, String reason, String status)
        throws Throwable
    {
		return id;}
    
    /** 审核不通过
     * <功能详细描述>
     * @param scoreOrderId 平台商城订单明细表id
     * @param reason 审核不通过原因
     * @param status 退款或拒绝退款
     * @return T6501ID
     * @throws Throwable
     */
    @Override
    public int nopassForBalance(int scoreOrderId)
        throws Throwable
    {
		return scoreOrderId;}
    
    /** 审核不通过(积分兑换)
     * <功能详细描述>
     * @param id 平台商城订单明细表id
     * @param reason 审核不通过原因
     * @return T6501ID
     * @throws Throwable
     */
    @Override
    public void nopassForScore(int scoreOrderId)
        throws Throwable
    {}
    
    /** 根据订单明细ID来回滚商品库存，不会回滚成交数量（产品规定已付款就算是成交，与后台是否审核通过无关）
     * <功能详细描述>
     * @param id 平台商城订单明细表id
     * @param reason 审核不通过原因
     * @return T6501ID
     * @throws Throwable
     */
    protected void rollBackT6351(int scoreOrderId, Connection connection)
        throws Throwable
    {}
    
    private void saveT6106(T6106 t6106, Connection connection)
        throws Throwable
    {}
    
    /**
     * 获取用户总积分
     * <功能详细描述>
     * @param connection
     * @return
     * @throws Throwable
     */
    protected T6105 selectT6105(Connection connection, int userId)
        throws Throwable
    {
		return null;}
    
    protected void updateT6105(Connection connection, int F02, int F04)
        throws Throwable
    {}
    
    private T6351 queryGoodsByIdForUpdate(Connection connection, int id)
        throws Throwable
    {
		return null;}
    
    private T6359 selectT6359(Connection connection, int id)
        throws SQLException
    {
		return null;}
    
    private T6352 selectT6352(Connection connection, int id)
        throws SQLException
    {
		return null;}
    
    // 创建订单
    private int addOrder(Connection connection, T6528 t6528)
        throws Throwable
    {
        int orderId = 0;
		return orderId;
    
    }
    
    private int getSkUserId(Connection connection, int id)
        throws SQLException
    {
		return 0;}
    
    /** 保存操作日志
     * <功能详细描述>
     * @param id 
     * @param reason 原因
     * @param T6360_F03 
     * @throws Throwable
     */
    @Override
    public void saveLog(int id, String reason, T6360_F03 F03)
        throws Throwable
    {}
    
    private void saveLog(Connection connection, int id, String reason, T6360_F03 F03)
        throws Throwable
    {}
    
    @Override
    public ScoreOrderMainRecord[] queryBatchSippingList(String ids)
        throws Throwable
    {
		return null;}
    
    @Override
    public void updateOrderMessage(List<T6359> updatet6359, List<T6360> updatet6360)
        throws Throwable
    {}
    
    @Override
    protected void sendLetter(Connection connection, int userId, String title, String content)
        throws Throwable
    {}
    
    /* @Override
     protected void sendMsg(Connection connection, String mobile, String content)
         throws Throwable
     {
         try
         {
             if (!StringHelper.isEmpty(content) && !StringHelper.isEmpty(mobile))
             {
                 long msgId = 0;
                 try (PreparedStatement ps =
                     connection.prepareStatement("INSERT INTO S10._1040(F02,F03,F04,F05) values(?,?,?,?)",
                         Statement.RETURN_GENERATED_KEYS))
                 {
                     ps.setInt(1, 0);
                     ps.setString(2, content);
                     ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                     ps.setString(4, "W");
                     ps.execute();
                     try (ResultSet resultSet = ps.getGeneratedKeys())
                     {
                         if (resultSet.next())
                         {
                             msgId = resultSet.getLong(1);
                         }
                     }
                 }
                 if (msgId > 0)
                 {
                     try (PreparedStatement ps =
                         connection.prepareStatement("INSERT INTO S10._1041(F01,F02) VALUES(?,?)"))
                     {
                         ps.setLong(1, msgId);
                         ps.setString(2, mobile);
                         ps.execute();
                     }
                 }
                 return;
             }
         }
         catch (Exception e)
         {
             logger.error(e, e);
             throw e;
         }
     }*/
    
    /**
    * 容联云通讯发送短信方法
    * 
    * @param connection
    * @param mobile
    *            手机
    * @param content
    *            内容
    * @param tempId
    *            模板id
    * @throws Throwable
    */
    /*@Override
    protected void sendMsg(Connection connection, String mobile, String content, int tempId)
        throws Throwable
    {
        try
        {
            if (!StringHelper.isEmpty(content) && !StringHelper.isEmpty(mobile))
            {
                long msgId = 0;
                try (PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO S10._1040(F02,F03,F04,F05) values(?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS))
                {
                    ps.setInt(1, tempId);
                    ps.setString(2, content);
                    ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                    ps.setString(4, "W");
                    ps.execute();
                    try (ResultSet resultSet = ps.getGeneratedKeys())
                    {
                        if (resultSet.next())
                        {
                            msgId = resultSet.getLong(1);
                        }
                    }
                }
                if (msgId > 0)
                {
                    try (PreparedStatement ps =
                        connection.prepareStatement("INSERT INTO S10._1041(F01,F02) VALUES(?,?)"))
                    {
                        ps.setLong(1, msgId);
                        ps.setString(2, mobile);
                        ps.execute();
                    }
                }
                return;
            }
        }
        catch (Exception e)
        {
            logger.error(e, e);
            throw e;
        }
    }*/
    
    private int insertT6123(Connection connection, int F02, String F03, T6123_F05 F05)
        throws Throwable
    {
		return 0;}
    
    @Override
    protected void insertT6124(Connection connection, int F01, String F02)
        throws SQLException
    {}
    
    @Override
    public void infoUserNoPass(ConfigureProvider configureProvider, Envionment envionment, int scoreOrderId,
        String reason)
        throws Throwable
    {}
    
    @Override
    public void infoUserSipping(ConfigureProvider configureProvider, Envionment envionment, int scoreOrderId)
        throws Throwable
    {}
    
    protected Map<String, String> sendParams(Connection connection, int F01)
        throws SQLException
    {
		return null;}
    
    @Override
    public T6359_F08 queryT6359F08(Integer id)
        throws Throwable
    {
		return null;}
    
    private T6359_F08 queryT6359F08(Connection connection, Integer id)
        throws Throwable
    {
		return null;}
    
    @Override
    public T6359 selectT6359(int id)
        throws Throwable
    {
		return null;}
    
    @Override
    public PagingResult<ScoreOrderMainRecord> queryOrderDetailList(OrderDetQuery orderDetQuery, Paging page)
        throws Throwable
    {
		return null;}
    
    @Override
    public ScoreOrderStatistics queryOrderDetailStatistics(OrderDetQuery orderDetQuery)
        throws Throwable
    {
		return null;}
    
    private ScoreOrderStatistics getScoreOrderStatistics(final String sql, final ArrayList<Object> parameters)
        throws Throwable
    {
		return null;}
    
    private String getOrderDetailSQL(OrderDetQuery orderDetQuery, ArrayList<Object> parameters)
        throws Throwable
    {
		return null;}
    
    @Override
    public ScoreOrderDetailRecord queryCommodityInfo(int id)
        throws Throwable
    {
		return null;}
}
