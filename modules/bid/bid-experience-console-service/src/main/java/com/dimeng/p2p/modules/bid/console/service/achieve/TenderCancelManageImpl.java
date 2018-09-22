package com.dimeng.p2p.modules.bid.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6250;
import com.dimeng.p2p.S62.entities.T6286;
import com.dimeng.p2p.S62.entities.T6288;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F12;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F14;
import com.dimeng.p2p.S62.enums.T6230_F15;
import com.dimeng.p2p.S62.enums.T6230_F16;
import com.dimeng.p2p.S62.enums.T6230_F17;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6230_F27;
import com.dimeng.p2p.S62.enums.T6250_F07;
import com.dimeng.p2p.S62.enums.T6250_F08;
import com.dimeng.p2p.S62.enums.T6286_F06;
import com.dimeng.p2p.S62.enums.T6286_F07;
import com.dimeng.p2p.S62.enums.T6288_F06;
import com.dimeng.p2p.S62.enums.T6288_F07;
import com.dimeng.p2p.S62.enums.T6288_F08;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.modules.bid.console.service.TenderCancelManage;
import com.dimeng.p2p.modules.bid.console.service.entity.BidReturn;

public class TenderCancelManageImpl extends AbstractBidService implements TenderCancelManage
{
    
    public TenderCancelManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public BidReturn cancel(int bidId, String des)
        throws Throwable
    {
        if (bidId <= 0)
        {
            throw new LogicalException("标记录不存在");
        }
        BidReturn bidReturn = new BidReturn();
        int consoleAccountId = serviceResource.getSession().getAccountId();
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                T6230 t6230 = selectT6230(connection, bidId);
                if (t6230 == null)
                {
                    throw new LogicalException("标记录不存在");
                }
                if (t6230.F20 != T6230_F20.DFK && t6230.F20 != T6230_F20.TBZ)
                {
                    throw new LogicalException("不是投资中或待放款状态,不能流标");
                }
                T6250[] t6250s = selectAllT6250(connection, bidId);
                if (t6250s == null || t6250s.length == 0)
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S62.T6230 SET F20 = ? WHERE F01 = ?"))
                    {
                        pstmt.setString(1, T6230_F20.YLB.name());
                        pstmt.setInt(2, bidId);
                        pstmt.execute();
                    }
                    sendLetter(serviceResource, t6230, connection, des);
                    serviceResource.commit(connection);
                    return bidReturn;
                }
                int[] orderIds = new int[t6250s.length];
                int index = 0;
                for (T6250 t6250 : t6250s)
                {
                    orderIds[index++] = addOrder(connection, t6250, consoleAccountId);
                }
                bidReturn.bidOrderIds = orderIds;
                T6286[] t6286s = selectAllT6286(connection, bidId);
                if (t6286s != null)
                {
                    int[] experOrderIds = new int[t6286s.length];
                    StringBuffer strExperOrderId = new StringBuffer();
                    index = 0;
                    for (T6286 t6286 : t6286s)
                    {
                        experOrderIds[index] = addExperienceOrder(connection, t6286, consoleAccountId);
                        strExperOrderId.append(experOrderIds[index] + ",");
                        index++;
                    }
                    bidReturn.experOrderIds = String.valueOf(strExperOrderId);
                }
                
                //sendLetter(serviceResource, t6230, connection, des);
                serviceResource.commit(connection);
                // sendMsg(connection, wlzh.F04, content);
                return bidReturn;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    private int addOrder(Connection connection, T6250 t6250, int consoleAccountId)
        throws Throwable
    {
        T6501 t6501 = new T6501();
        t6501.F02 = OrderType.BID_CANCEL.orderType();
        t6501.F03 = T6501_F03.DTJ;
        t6501.F04 = getCurrentTimestamp(connection);
        t6501.F07 = T6501_F07.HT;
        t6501.F08 = t6250.F03;
        t6501.F09 = consoleAccountId;
        t6501.F13 = t6250.F04;
        int orderId = insertT6501(connection, t6501);
        insertT6508(connection, orderId, t6250.F03, t6250.F01);
        
        //加息券投资取消
        cancelCoupon(connection, t6250, consoleAccountId, orderId);
        return orderId;
    }
    
    /**
     * 加息券投资取消
     * @param connection
     * @param t6250
     * @param consoleAccountId
     * @param cancelOrderId
     * @throws Throwable
     */
    private void cancelCoupon(Connection connection, T6250 t6250, int consoleAccountId, int cancelOrderId)
        throws Throwable
    {
        //根据T6250投资记录ID查询 T6288加息券投资记录
        T6288 t6288 = getT6288(connection, t6250);
        if (t6288 == null)
        {
            return;
        }
        T6501 t6501 = new T6501();
        t6501.F02 = OrderType.BID_COUPON_CANCEL.orderType();
        t6501.F03 = T6501_F03.DTJ;
        t6501.F04 = getCurrentTimestamp(connection);
        t6501.F07 = T6501_F07.HT;
        t6501.F08 = t6288.F03;
        t6501.F09 = consoleAccountId;
        t6501.F13 = t6250.F04;
        int orderId = insertT6501(connection, t6501);
        //插入加息券投资取消订单
        insertT6526(connection, orderId, t6288.F03, t6288.F01, cancelOrderId);
    }
    
    /**
     * 根据T6250投资记录ID查询 T6288加息券投资记录
     * @param connection
     * @return
     * @throws SQLException
     */
    private T6288 getT6288(Connection connection, T6250 t6250)
        throws SQLException
    {
        T6288 t6288 = null;
        try (PreparedStatement pstm =
            connection.prepareStatement("SELECT T6288.F01,T6288.F02,T6288.F03,T6288.F04,T6288.F05,T6288.F06,T6288.F07,T6288.F08,T6288.F09,T6288.F10,T6288.F11 FROM S62.T6288 WHERE T6288.F01 = (SELECT T.F01 FROM S62.T6288 T WHERE T.F09 = ? ) FOR UPDATE "))
        {
            pstm.setInt(1, t6250.F01);
            try (ResultSet resultSet = pstm.executeQuery())
            {
                if (resultSet.next())
                {
                    t6288 = new T6288();
                    t6288.F01 = resultSet.getInt(1);
                    t6288.F02 = resultSet.getInt(2);
                    t6288.F03 = resultSet.getInt(3);
                    t6288.F04 = resultSet.getBigDecimal(4);
                    t6288.F05 = resultSet.getTimestamp(5);
                    t6288.F06 = T6288_F06.parse(resultSet.getString(6));
                    t6288.F07 = T6288_F07.parse(resultSet.getString(7));
                    t6288.F08 = T6288_F08.parse(resultSet.getString(8));
                    t6288.F09 = resultSet.getInt(9);
                    t6288.F10 = resultSet.getInt(10);
                    t6288.F11 = resultSet.getBigDecimal(11);
                    return t6288;
                }
            }
        }
        return t6288;
    }
    
    /**
     * 插入加息券投资取消订单
     * @param connection
     * @param F01
     * @param F02
     * @param F03
     * @throws SQLException
     */
    private void insertT6526(Connection connection, int F01, int F02, int F03, int F04)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6526 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?"))
        {
            pstmt.setInt(1, F01);
            pstmt.setInt(2, F02);
            pstmt.setInt(3, F03);
            pstmt.setInt(4, F04);
            pstmt.execute();
        }
    }
    
    private void insertT6508(Connection connection, int F01, int F02, int F03)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6508 SET F01 = ?, F02 = ?, F03 = ?"))
        {
            pstmt.setInt(1, F01);
            pstmt.setInt(2, F02);
            pstmt.setInt(3, F03);
            pstmt.execute();
        }
    }
    
    private T6230 selectT6230(Connection connection, int F01)
        throws SQLException
    {
        T6230 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25, F26, F27 FROM S62.T6230 WHERE T6230.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6230();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getString(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getBigDecimal(7);
                    record.F08 = resultSet.getInt(8);
                    record.F09 = resultSet.getInt(9);
                    record.F10 = T6230_F10.parse(resultSet.getString(10));
                    record.F11 = T6230_F11.parse(resultSet.getString(11));
                    record.F12 = T6230_F12.parse(resultSet.getString(12));
                    record.F13 = T6230_F13.parse(resultSet.getString(13));
                    record.F14 = T6230_F14.parse(resultSet.getString(14));
                    record.F15 = T6230_F15.parse(resultSet.getString(15));
                    record.F16 = T6230_F16.parse(resultSet.getString(16));
                    record.F17 = T6230_F17.parse(resultSet.getString(17));
                    record.F18 = resultSet.getInt(18);
                    record.F19 = resultSet.getInt(19);
                    record.F20 = T6230_F20.parse(resultSet.getString(20));
                    record.F21 = resultSet.getString(21);
                    record.F22 = resultSet.getTimestamp(22);
                    record.F23 = resultSet.getInt(23);
                    record.F24 = resultSet.getTimestamp(24);
                    record.F25 = resultSet.getString(25);
                    record.F26 = resultSet.getBigDecimal(26);
                    record.F27 = T6230_F27.parse(resultSet.getString(27));
                }
            }
        }
        return record;
    }
    
    private T6250[] selectAllT6250(Connection connection, int F02)
        throws SQLException
    {
        ArrayList<T6250> list = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S62.T6250 WHERE F02 = ? AND F07 = ? AND F08 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F02);
            pstmt.setString(2, T6250_F07.F.name());
            pstmt.setString(3, T6250_F08.F.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    T6250 record = new T6250();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getTimestamp(6);
                    record.F07 = T6250_F07.parse(resultSet.getString(7));
                    record.F08 = T6250_F08.parse(resultSet.getString(8));
                    if (list == null)
                    {
                        list = new ArrayList<>();
                    }
                    list.add(record);
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new T6250[list.size()]));
    }
    
    /**
     * 查询体验金投资记录
     * <功能详细描述>
     * @param connection
     * @param F02
     * @return
     * @throws SQLException
     */
    private T6286[] selectAllT6286(Connection connection, int F02)
        throws SQLException
    {
        ArrayList<T6286> list = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S62.T6286 WHERE F02 = ? AND F06 = ? AND F07 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F02);
            pstmt.setString(2, T6250_F07.F.name());
            pstmt.setString(3, T6250_F08.F.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    T6286 record = new T6286();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getTimestamp(5);
                    record.F06 = T6286_F06.parse(resultSet.getString(6));
                    record.F07 = T6286_F07.parse(resultSet.getString(7));
                    if (list == null)
                    {
                        list = new ArrayList<>();
                    }
                    list.add(record);
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new T6286[list.size()]));
    }
    
    /**
     * 添加体验金投资取消订单
     * <功能详细描述>
     * @param connection
     * @param t6286
     * @param consoleAccountId
     * @return
     * @throws Throwable
     */
    private int addExperienceOrder(Connection connection, T6286 t6286, int consoleAccountId)
        throws Throwable
    {
        T6501 t6501 = new T6501();
        t6501.F02 = OrderType.BID_EXPERIENCE_CANCEL.orderType();
        t6501.F03 = T6501_F03.DTJ;
        t6501.F04 = getCurrentTimestamp(connection);
        t6501.F07 = T6501_F07.HT;
        t6501.F08 = t6286.F03;
        t6501.F09 = consoleAccountId;
        t6501.F13 = t6286.F04;
        int orderId = insertT6501(connection, t6501);
        insertT6522(connection, orderId, t6286.F03, t6286.F01);
        return orderId;
    }
    
    private void insertT6522(Connection connection, int F01, int F02, int F03)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6522 SET F01 = ?, F02 = ?, F03 = ?"))
        {
            pstmt.setInt(1, F01);
            pstmt.setInt(2, F02);
            pstmt.setInt(3, F03);
            pstmt.execute();
        }
    }
    
}
