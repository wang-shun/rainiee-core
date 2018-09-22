package com.dimeng.p2p.modules.bid.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
import com.dimeng.p2p.S65.entities.T6505;
import com.dimeng.p2p.S65.entities.T6519;
import com.dimeng.p2p.S65.entities.T6524;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.modules.bid.console.service.TenderConfirmManage;
import com.dimeng.p2p.modules.bid.console.service.entity.BidReturn;

public class TenderConfirmManageImpl extends AbstractBidService implements TenderConfirmManage
{
    
    public TenderConfirmManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public BidReturn confirm(int bidId)
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
                if (t6230.F20 != T6230_F20.DFK)
                {
                    throw new LogicalException("不是待放款状态,不能放款");
                }
                T6250[] t6250s = selectAllT6250(connection, bidId);
                if (t6250s == null || t6250s.length <= 0)
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S62.T6230 SET F20 = ? WHERE F01 = ?"))
                    {
                        pstmt.setString(1, T6230_F20.YLB.name());
                        pstmt.setInt(2, bidId);
                        pstmt.execute();
                    }
                    sendLetter(serviceResource, t6230, connection, null);
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
                if (t6286s != null && t6286s.length >= 0)
                {
                    int[] experOrderIds = new int[t6286s.length];
                    StringBuffer strExperOrderIds = new StringBuffer();
                    index = 0;
                    for (T6286 t6286 : t6286s)
                    {
                        experOrderIds[index] = addExperOrder(connection, t6286, consoleAccountId);
                        strExperOrderIds.append(experOrderIds[index] + ",");
                        index++;
                    }
                    bidReturn.experOrderIds = String.valueOf(strExperOrderIds);
                }
                // 根据加息券投资记录生成加息券放款订单
                T6288[] t6288s = selectAllT6288(connection, bidId);
                if (t6288s != null && t6288s.length > 0)
                {
                    int[] couponOrderIds = new int[t6288s.length];
                    StringBuilder strCouponOrderIds = new StringBuilder();
                    index = 0;
                    for (T6288 t6288 : t6288s)
                    {
                        // 添加加息券放款订单
                        couponOrderIds[index] = addCouponOrder(connection, t6288, consoleAccountId);
                        strCouponOrderIds.append(couponOrderIds[index] + ",");
                        index++;
                    }
                    bidReturn.couponOrderIds = String.valueOf(strCouponOrderIds);
                }
                serviceResource.commit(connection);
                return bidReturn;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    private T6288[] selectAllT6288(Connection connection, int bidId)
        throws SQLException
    {
        List<T6288> list = null;
        String sql =
            "SELECT F01,F02,F03,F04,F05,F06,F07,F08,F09,F10,F11 FROM S62.T6288 WHERE F02=? AND F06=? AND F07=? FOR UPDATE";
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, bidId);
            ps.setString(2, T6288_F06.F.name());
            ps.setString(3, T6288_F07.F.name());
            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    T6288 t6288 = new T6288();
                    t6288.F01 = rs.getInt(1);
                    t6288.F02 = rs.getInt(2);
                    t6288.F03 = rs.getInt(3);
                    t6288.F04 = rs.getBigDecimal(4);
                    t6288.F05 = rs.getTimestamp(5);
                    t6288.F06 = T6288_F06.parse(rs.getString(6));
                    t6288.F07 = T6288_F07.parse(rs.getString(7));
                    t6288.F08 = T6288_F08.parse(rs.getString(8));
                    t6288.F09 = rs.getInt(9);
                    t6288.F10 = rs.getInt(10);
                    t6288.F11 = rs.getBigDecimal(11);
                    if (list == null)
                    {
                        list = new ArrayList<T6288>();
                    }
                    list.add(t6288);
                }
            }
        }
        return list == null || list.size() == 0 ? null : list.toArray(new T6288[list.size()]);
    }
    
    private int addOrder(Connection connection, T6250 t6250, int consoleAccountId)
        throws Throwable
    {
        T6501 t6501 = new T6501();
        t6501.F02 = OrderType.BID_CONFIRM.orderType();
        t6501.F03 = T6501_F03.DTJ;
        t6501.F04 = getCurrentTimestamp(connection);
        t6501.F07 = T6501_F07.HT;
        t6501.F08 = t6250.F03;
        t6501.F09 = consoleAccountId;
        t6501.F13 = t6250.F04;
        int orderId = insertT6501(connection, t6501);
        T6505 t6505 = new T6505();
        t6505.F01 = orderId;
        t6505.F02 = t6250.F03;
        t6505.F03 = t6250.F02;
        t6505.F04 = t6250.F01;
        t6505.F05 = t6250.F04;
        insertT6505(connection, t6505);
        return orderId;
    }
    
    private void insertT6505(Connection connection, T6505 t6505)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6505 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?"))
        {
            pstmt.setInt(1, t6505.F01);
            pstmt.setInt(2, t6505.F02);
            pstmt.setInt(3, t6505.F03);
            pstmt.setInt(4, t6505.F04);
            pstmt.setBigDecimal(5, t6505.F05);
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
    
    private int addExperOrder(Connection connection, T6286 t6286, int consoleAccountId)
        throws Throwable
    {
        T6519 t65191 = this.selectT6519(connection, t6286.F03, t6286.F02, t6286.F01);
        if (t65191 != null)
        {
            return t65191.F01;
        }
        T6501 t6501 = new T6501();
        t6501.F02 = OrderType.BID_EXPERIENCE_CONFIRM.orderType();
        t6501.F03 = T6501_F03.DTJ;
        t6501.F04 = getCurrentTimestamp(connection);
        t6501.F07 = T6501_F07.HT;
        t6501.F08 = t6286.F03;
        t6501.F09 = consoleAccountId;
        t6501.F13 = t6286.F04;
        int orderId = insertT6501(connection, t6501);
        T6519 t6519 = new T6519();
        t6519.F01 = orderId;
        t6519.F02 = t6286.F03;
        t6519.F03 = t6286.F02;
        t6519.F04 = t6286.F01;
        t6519.F05 = t6286.F04;
        insertT6519(connection, t6519);
        return orderId;
    }
    
    /**
     * 添加加息券放款订单
     * 
     * @param connection
     * @param t6288
     * @param consoleAccountId
     * @return
     * @throws Throwable
     */
    private int addCouponOrder(Connection connection, T6288 t6288, int consoleAccountId)
        throws Throwable
    {
        // 检查是否已经生成加息券放款订单
        T6524 t65241 = selectT6524(connection, t6288.F03, t6288.F02, t6288.F01);
        if (t65241 != null)
        {
            return t65241.F01;
        }
        T6501 t6501 = new T6501();
        t6501.F02 = OrderType.BID_COUPON_CONFIRM.orderType();
        t6501.F03 = T6501_F03.DTJ;
        t6501.F04 = getCurrentTimestamp(connection);
        t6501.F07 = T6501_F07.HT;
        t6501.F08 = t6288.F03;
        t6501.F09 = consoleAccountId;
        t6501.F13 = t6288.F11;
        int orderId = insertT6501(connection, t6501);
        
        T6524 t6524 = new T6524();
        t6524.F01 = orderId;
        t6524.F02 = t6288.F03;
        t6524.F03 = t6288.F02;
        t6524.F04 = t6288.F01;
        t6524.F05 = t6288.F04;
        t6524.F06 = t6288.F11;
        insertT6524(connection, t6524);
        return orderId;
    }
    
    private void insertT6519(Connection connection, T6519 t6505)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6519 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?"))
        {
            pstmt.setInt(1, t6505.F01);
            pstmt.setInt(2, t6505.F02);
            pstmt.setInt(3, t6505.F03);
            pstmt.setInt(4, t6505.F04);
            pstmt.setBigDecimal(5, t6505.F05);
            pstmt.execute();
        }
    }
    
    private void insertT6524(Connection connection, T6524 t6524)
        throws SQLException
    {
        try (PreparedStatement ps =
            connection.prepareStatement("INSERT INTO S65.T6524(F01,F02,F03,F04,F05,F06) VALUES(?,?,?,?,?,?)"))
        {
            ps.setInt(1, t6524.F01);
            ps.setInt(2, t6524.F02);
            ps.setInt(3, t6524.F03);
            ps.setInt(4, t6524.F04);
            ps.setBigDecimal(5, t6524.F05);
            ps.setBigDecimal(6, t6524.F06);
            ps.execute();
        }
    }
    
    private T6286[] selectAllT6286(Connection connection, int F02)
        throws SQLException
    {
        ArrayList<T6286> list = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S62.T6286 WHERE F02 = ? AND F06 = ? AND F07 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F02);
            pstmt.setString(2, T6286_F06.F.name());
            pstmt.setString(3, T6286_F07.F.name());
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
     * 查询 放款体验金订单表
     * 
     * @param connection
     * @param F02
     * @param F03
     * @param F04
     * @return
     * @throws Throwable
     */
    protected T6519 selectT6519(Connection connection, int F02, int F03, int F04)
        throws Throwable
    {
        T6519 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01,F02,F03,F04,F05 FROM S65.T6519 WHERE F02=? AND F03=? AND F04=?"))
        {
            pstmt.setInt(1, F02);
            pstmt.setInt(2, F03);
            pstmt.setInt(3, F04);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6519();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getBigDecimal(5);
                }
            }
        }
        return record;
    }
    
    /**
     * 根据用户ID,标的ID,投资记录ID 检查加息券是否已经生成放款订单 <功能详细描述>
     * 
     * @param connection
     * @param F02
     * @param F03
     * @param F04
     * @return
     * @throws Throwable
     */
    protected T6524 selectT6524(Connection connection, int F02, int F03, int F04)
        throws Throwable
    {
        T6524 record = null;
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT F01,F02,F03,F04,F05,F06 FROM S65.T6524 WHERE F02=? AND F03=? AND F04=? "))
        {
            ps.setInt(1, F02);
            ps.setInt(2, F03);
            ps.setInt(3, F04);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    record = new T6524();
                    record.F01 = rs.getInt(1);
                    record.F02 = rs.getInt(2);
                    record.F03 = rs.getInt(3);
                    record.F04 = rs.getInt(4);
                    record.F05 = rs.getBigDecimal(5);
                    record.F06 = rs.getBigDecimal(6);
                }
            }
        }
        return record;
    }
}
