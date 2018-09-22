package com.dimeng.p2p.modules.bid.pay.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S64.entities.T6410;
import com.dimeng.p2p.S64.entities.T6411;
import com.dimeng.p2p.S64.entities.T6412;
import com.dimeng.p2p.S64.enums.T6410_F07;
import com.dimeng.p2p.S64.enums.T6410_F14;
import com.dimeng.p2p.S64.enums.T6410_F24;
import com.dimeng.p2p.S64.enums.T6412_F09;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6511;
import com.dimeng.p2p.S65.entities.T6512;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.modules.bid.pay.service.FinancialPurchaseManage;
import com.dimeng.util.DateHelper;

public class FinancialPurchaseManageImpl extends AbstractBidService implements FinancialPurchaseManage
{
    
    public FinancialPurchaseManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public int addOrder(int lcId, BigDecimal amount)
        throws Throwable
    {
        if (lcId <= 0)
        {
            throw new ParameterException("不存在的优选理财");
        }
        if (amount.compareTo(new BigDecimal(0)) <= 0)
        {
            throw new ParameterException("投资金额为负数或0");
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                T6410 t6410 = selectT6410(connection, lcId);
                if (t6410 == null)
                {
                    throw new LogicalException("投资的优选理财不存在");
                }
                int accountId = serviceResource.getSession().getAccountId();
                /*int orderId =
                    insertT6501(connection,
                        OrderType.FINANCIAL_PURCHASE.orderType(),
                        T6501_F03.DTJ,
                        T6501_F07.YH,
                        accountId);*/
                
                T6501 t6501 = new T6501();
                t6501.F02 = OrderType.FINANCIAL_PURCHASE.orderType();
                t6501.F03 = T6501_F03.DTJ;
                t6501.F04 = getCurrentTimestamp(connection);
                t6501.F07 = T6501_F07.YH;
                t6501.F08 = accountId;
                t6501.F13 = amount;
                int orderId = insertT6501(connection, t6501);
                
                insertT6510(connection, orderId, accountId, lcId, amount);
                
                serviceResource.commit(connection);
                return orderId;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    private void insertT6510(Connection connection, int F01, int F02, int F03, BigDecimal F04)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6510 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?"))
        {
            pstmt.setInt(1, F01);
            pstmt.setInt(2, F02);
            pstmt.setInt(3, F03);
            pstmt.setBigDecimal(4, F04);
            pstmt.execute();
        }
    }
    
    private T6410 selectT6410(Connection connection, int F01)
        throws SQLException
    {
        T6410 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24 FROM S64.T6410 WHERE T6410.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6410();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getString(2);
                    record.F03 = resultSet.getBigDecimal(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getInt(6);
                    record.F07 = T6410_F07.parse(resultSet.getString(7));
                    record.F08 = resultSet.getInt(8);
                    record.F09 = resultSet.getTimestamp(9);
                    record.F10 = resultSet.getDate(10);
                    record.F11 = resultSet.getInt(11);
                    record.F12 = resultSet.getTimestamp(12);
                    record.F13 = resultSet.getDate(13);
                    record.F14 = T6410_F14.parse(resultSet.getString(14));
                    record.F15 = resultSet.getBigDecimal(15);
                    record.F16 = resultSet.getBigDecimal(16);
                    record.F17 = resultSet.getBigDecimal(17);
                    record.F18 = resultSet.getString(18);
                    record.F19 = resultSet.getInt(19);
                    record.F20 = resultSet.getTimestamp(20);
                    record.F21 = resultSet.getDate(21);
                    record.F22 = resultSet.getBigDecimal(22);
                    record.F23 = resultSet.getBigDecimal(23);
                    record.F24 = T6410_F24.parse(resultSet.getString(24));
                }
            }
        }
        return record;
    }
    
    /*private int insertT6501(Connection connection, int F02, T6501_F03 F03, T6501_F07 F07, int F08)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6501 SET F02 = ?, F03 = ?, F04 = ?, F07 = ?, F08 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, F02);
            pstmt.setString(2, F03.name());
            pstmt.setTimestamp(3, getCurrentTimestamp(connection));
            pstmt.setString(4, F07.name());
            pstmt.setInt(5, F08);
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys();)
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        }
    }*/
    
    @Override
    public int[] addFkOrder()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                int ycId = 0;
                BigDecimal totalAmount = new BigDecimal(0);
                BigDecimal ktAmount = new BigDecimal(0);
                //int yue = 0;
                int[] orderIds = null;
                Date date = getCurrentDate(connection);
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT F01,F03,F04,F11 FROM S64.T6410 WHERE F07 = ? AND (F10 < ? OR F04 = 0) LIMIT 1 FOR UPDATE"))
                {
                    ps.setString(1, T6410_F07.YFB.name());
                    ps.setDate(2, date);
                    try (ResultSet rs = ps.executeQuery())
                    {
                        if (rs.next())
                        {
                            ycId = rs.getInt(1);
                            totalAmount = rs.getBigDecimal(2);
                            ktAmount = rs.getBigDecimal(3);
                            //yue = rs.getInt(4);
                        }
                    }
                }
                if (ycId <= 0)
                {
                    serviceResource.rollback(connection);
                    return orderIds;
                }
                /* Calendar calendar = Calendar.getInstance();
                 calendar.add(Calendar.MONTH, yue);
                 Date date = new Date(calendar.getTimeInMillis());*/
                if (totalAmount.compareTo(ktAmount) == 0)
                {
                    updateT6410(connection, T6410_F07.YJZ, ycId, date);
                    serviceResource.commit(connection);
                    return orderIds;
                }
                T6411[] t6411s = selectAllT6411(connection, ycId);
                if (t6411s == null || t6411s.length <= 0)
                {
                    serviceResource.commit(connection);
                    return orderIds;
                }
                orderIds = new int[t6411s.length];
                int index = 0;
                Timestamp currentTimestamp = getCurrentTimestamp(connection);
                for (T6411 t6411 : t6411s)
                {
                    orderIds[index++] = addOrder(connection, t6411, currentTimestamp);
                }
                serviceResource.commit(connection);
                return orderIds;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    protected void updateT6410(Connection connection, T6410_F07 F01, int F02, Date F03)
        throws Throwable
    {
        Timestamp timestamp = getCurrentTimestamp(connection);
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S64.T6410 SET F04 = ?, F07 = ?, F08 = (UNIX_TIMESTAMP(?) - UNIX_TIMESTAMP(F09)), F12 = ?, F13 = ? WHERE F01 = ?"))
        {
            pstmt.setBigDecimal(1, new BigDecimal(0));
            pstmt.setString(2, F01.name());
            pstmt.setTimestamp(3, timestamp);
            pstmt.setTimestamp(4, timestamp);
            pstmt.setDate(5, F03);
            pstmt.setInt(6, F02);
            pstmt.execute();
        }
    }
    
    @Override
    public int[] addHkOrder()
        throws Throwable
    {
        
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                Date currentDate = getCurrentDate(connection);
                T6412[] t6412s = selectAllT6412(connection, currentDate);
                if (t6412s == null || t6412s.length == 0)
                {
                    serviceResource.rollback(connection);
                    return new int[0];
                }
                if (DateHelper.beforeDate(currentDate, t6412s[0].F08))
                {
                    serviceResource.rollback(connection);
                    return new int[0];
                }
                int[] orderIds = new int[t6412s.length];
                int index = 0;
                Timestamp currentTimestamp = getCurrentTimestamp(connection);
                for (T6412 t6412 : t6412s)
                {
                    orderIds[index++] = addHkOrder(connection, t6412, currentTimestamp);
                }
                serviceResource.commit(connection);
                return orderIds;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    private T6412[] selectAllT6412(Connection connection, Date F08)
        throws SQLException
    {
        ArrayList<T6412> list = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S64.T6412 WHERE T6412.F08 <= ? AND T6412.F09 = ? ORDER BY F02,F06 ASC FOR UPDATE"))
        {
            pstmt.setDate(1, F08);
            pstmt.setString(2, T6412_F09.WH.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    T6412 record = new T6412();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getInt(5);
                    record.F06 = resultSet.getInt(6);
                    record.F07 = resultSet.getBigDecimal(7);
                    record.F08 = resultSet.getDate(8);
                    record.F09 = T6412_F09.parse(resultSet.getString(9));
                    record.F10 = resultSet.getTimestamp(10);
                    if (list == null)
                    {
                        list = new ArrayList<>();
                    }
                    list.add(record);
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new T6412[list.size()]));
    }
    
    private int addOrder(Connection connection, T6411 t6411, Timestamp currentTimestamp)
        throws Throwable
    {
        T6501 t6501 = new T6501();
        t6501.F02 = OrderType.FINANCIAL_LOAN.orderType();
        t6501.F03 = T6501_F03.DTJ;
        t6501.F04 = currentTimestamp;
        t6501.F07 = T6501_F07.XT;
        t6501.F08 = t6411.F03;
        t6501.F13 = t6411.F04;
        int orderId = insertT6501(connection, t6501);
        T6511 t6511 = new T6511();
        t6511.F01 = orderId;
        t6511.F02 = t6411.F03;
        t6511.F03 = t6411.F02;
        t6511.F04 = t6411.F04;
        insertT6511(connection, t6511);
        return orderId;
    }
    
    private int addHkOrder(Connection connection, T6412 t6412, Timestamp currentTimestamp)
        throws Throwable
    {
        T6501 t6501 = new T6501();
        t6501.F02 = OrderType.FINANCIAL_REPAYMENT.orderType();
        t6501.F03 = T6501_F03.DTJ;
        t6501.F04 = currentTimestamp;
        t6501.F07 = T6501_F07.XT;
        t6501.F08 = t6412.F03;
        t6501.F13 = t6412.F07;
        int orderId = insertT6501(connection, t6501);
        T6512 t6512 = new T6512();
        t6512.F01 = orderId;
        t6512.F02 = t6412.F04;
        t6512.F03 = t6412.F02;
        t6512.F04 = t6412.F06;
        t6512.F05 = t6412.F07;
        t6512.F06 = t6412.F05;
        insertT6512(connection, t6512);
        return orderId;
    }
    
    protected void insertT6512(Connection connection, T6512 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6512 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?"))
        {
            pstmt.setInt(1, entity.F01);
            pstmt.setInt(2, entity.F02);
            pstmt.setInt(3, entity.F03);
            pstmt.setInt(4, entity.F04);
            pstmt.setBigDecimal(5, entity.F05);
            pstmt.setInt(6, entity.F06);
            pstmt.execute();
        }
    }
    
    /*@Override
    protected int insertT6501(Connection connection, T6501 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6501 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, entity.F02);
            pstmt.setString(2, entity.F03.name());
            pstmt.setTimestamp(3, entity.F04);
            pstmt.setTimestamp(4, entity.F05);
            pstmt.setTimestamp(5, entity.F06);
            pstmt.setString(6, entity.F07.name());
            pstmt.setInt(7, entity.F08);
            pstmt.setInt(8, entity.F09);
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys();)
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        }
    }*/
    
    protected void insertT6511(Connection connection, T6511 t6511)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6511 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?"))
        {
            pstmt.setInt(1, t6511.F01);
            pstmt.setInt(2, t6511.F02);
            pstmt.setInt(3, t6511.F03);
            pstmt.setBigDecimal(4, t6511.F04);
            pstmt.execute();
        }
    }
    
    protected T6411[] selectAllT6411(Connection connection, int F02)
        throws SQLException
    {
        ArrayList<T6411> list = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S64.T6411 WHERE T6411.F02 = ?"))
        {
            pstmt.setInt(1, F02);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    T6411 record = new T6411();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getTimestamp(6);
                    record.F07 = resultSet.getTimestamp(7);
                    if (list == null)
                    {
                        list = new ArrayList<>();
                    }
                    list.add(record);
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new T6411[list.size()]));
    }
}
