package com.dimeng.p2p.escrow.fuyou.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S62.entities.T6252;
import com.dimeng.p2p.S62.entities.T6289;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S62.enums.T6289_F09;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6506;
import com.dimeng.p2p.S65.entities.T6525;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.service.AutoRepaymentManage;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.util.DateHelper;
import com.dimeng.util.StringHelper;

/**
 * 
 * 自动还款 <功能详细描述>
 * 
 * @author heshiping
 * @version [版本号, 2016年3月15日]
 */
public class AutoRepaymentManageImpl extends AbstractEscrowService implements AutoRepaymentManage
{
    
    public AutoRepaymentManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public int[] repayment(ServiceSession serviceSession)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            serviceResource.openTransactions(connection);
            try
            {
                Date currentDate = getCurrentDate(connection);
                // 查询所有用户还款金额
                T6252[] t6252_h = selectT6252User(connection, currentDate);
                // 查询当前时间所有未还的
                T6252[] t6252s = selectAllT6252(connection, currentDate);
                if (t6252s == null || t6252s.length == 0)
                {
                    serviceSession.rollback(connection);
                    return new int[0];
                }
                if (DateHelper.beforeDate(currentDate, t6252s[0].F08))
                {
                    throw new LogicalException("还未到还款时间，不能还款。");
                }
                // 还款账户余额
                BigDecimal userBalance = BigDecimal.ZERO;
                T6101 hkrAcount = null;
                int userId = 0;
                Map<Integer, Integer> tempMap = new HashMap<Integer, Integer>();
                
                // 过虑资金不足用户
                for (int i = 0; i < t6252_h.length; i++)
                {
                    if (userId == t6252_h[i].F03)
                    {
                        userBalance = userBalance.subtract(t6252_h[i].F07);
                    }
                    else
                    {
                        hkrAcount = selectT6101(connection, t6252_h[i].F03, T6101_F03.WLZH, false);
                        userBalance = hkrAcount.F06.subtract(t6252_h[i].F07);
                    }
                    if (userBalance.compareTo(BigDecimal.ZERO) >= 0)
                    {
                        tempMap.put(t6252_h[i].F01, t6252_h[i].F03);
                    }
                    userId = t6252_h[i].F03;
                }
                
                int[] orderIds = null;
                
                if (tempMap.entrySet().size() > 0)
                {
                    orderIds = new int[t6252s.length];
                    int index = 0;
                    Timestamp currentTimestamp = getCurrentTimestamp(connection);
                    int bidId = 0;
                    // 遍历能全部还款数据 &【 已存有还款订单的还款不在产生新订单】
                    for (Integer key : tempMap.keySet())
                    {
                        boolean flag = true;
                        // 查询某标在当前时间未还的所有标还款记录
                        T6252[] t6252_repayment = selectT6252Repayment(connection, currentDate, key, tempMap.get(key));
                        //  如未有过还款订单（T6501）则增加还款订单
                        for (T6252 t6252 : t6252_repayment)
                        {
                            if (bidId != t6252.F02)
                            {
                                bidId = t6252.F02;
                                int[] paymentT6501 = selectT6501Payment(connection, t6252.F02, t6252.F06);
                                if (paymentT6501 != null && paymentT6501.length != 0)
                                {
                                    flag = false;
                                    for (int i : paymentT6501)
                                    {
                                        orderIds[index++] = i;
                                    }
                                }
                            }
                            // 未产过订单还款
                            if (flag)
                            {
                                orderIds[index++] = addOrder(connection, t6252, currentTimestamp);
                            }
                        }
                    }
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
    
    /**
     * 系统时间
     * <功能详细描述>
     * @param connection
     * @return
     * @throws Throwable
     */
    @Override
    protected Timestamp getCurrentTimestamp(Connection connection)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT CURRENT_TIMESTAMP()"))
        {
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getTimestamp(1);
                }
            }
        }
        return null;
    }
    
    /**
     * 统计标还款订单数
     * <还款订单>
     * @param connection
     * @param bidId 标ID
     * @param number 期数
     * @return <还款订单>
     * @throws Throwable
     */
    private int[] selectT6501Payment(Connection connection, int bidId, int number)
        throws Throwable
    {
        int[] orderIds = null;
        int index = 0;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT DISTINCT(T6501.F01) FROM S65.T6501 LEFT JOIN S65.T6506 ON T6506.F01 = T6501.F01"
                + " LEFT JOIN S62.T6252 ON T6252.F02 = T6506.F03 AND T6252.F06 = T6506.F05 WHERE T6501.F02 = ? AND T6252.F02 = ? AND T6252.F06 = ? AND T6501.F03!=? "))
        {
            pstmt.setInt(1, OrderType.BID_REPAYMENT.orderType());
            pstmt.setInt(2, bidId);
            pstmt.setInt(3, number);
            pstmt.setString(4, T6501_F03.CG.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    if (orderIds == null)
                    {
                        orderIds = new int[selectCountT6252(connection, bidId, number)];
                    }
                    selectT6501(connection, resultSet.getInt(1));
                    orderIds[index++] = resultSet.getInt(1);
                }
            }
        }
        return orderIds;
    }
    
    private void selectT6501(Connection connection, int F01)
        throws Throwable
    {
        T6501 t6501 = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F10 FROM S65.T6501 WHERE T6501.F01 = ? "))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    t6501 = new T6501();
                    t6501.F01 = resultSet.getInt(1);
                    t6501.F02 = resultSet.getInt(2);
                    t6501.F03 = T6501_F03.parse(resultSet.getString(3));
                    t6501.F10 = resultSet.getString(4);
                }
            }
        }
        if (t6501.F03 == T6501_F03.SB)
        {
            updateT6501Status(connection, T6501_F03.DQR.name(), F01);
        }
        if (StringHelper.isEmpty(t6501.F10))
        {
            updateT6501(connection, MchntTxnSsn.getMts(FuyouTypeEnum.ZDHK.name()), F01);
        }
    }
    
    /**
     * 统计标还款记录
     * @param connection
     * @param F02
     * @return
     * @throws SQLException
     */
    private int selectCountT6252(Connection connection, int bidiId, int number)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT count(*) FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F06 = ? AND T6252.F09 = ? "))
        {
            pstmt.setInt(1, bidiId);
            pstmt.setInt(2, number);
            pstmt.setString(3, T6252_F09.WH.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }
    
    private int addOrder(Connection connection, T6252 t6252, Timestamp currentTimestamp)
        throws Throwable
    {
        T6501 t6501 = new T6501();
        t6501.F02 = OrderType.BID_REPAYMENT.orderType();
        t6501.F03 = T6501_F03.DTJ;
        t6501.F04 = currentTimestamp;
        t6501.F07 = T6501_F07.HT;
        t6501.F08 = t6252.F03;
        t6501.F10 = MchntTxnSsn.getMts(FuyouTypeEnum.ZDHK.name());
        t6501.F13 = t6252.F07;
        int orderId = insertT6501(connection, t6501);
        T6506 t6506 = new T6506();
        t6506.F01 = orderId;
        t6506.F02 = t6252.F04;
        t6506.F03 = t6252.F02;
        t6506.F04 = t6252.F11;
        t6506.F05 = t6252.F06;
        t6506.F06 = t6252.F07;
        t6506.F07 = t6252.F05;
        insertT6506(connection, t6506);
        //根据T6252.F11债权ID去加息券返回计划表中查询数据，如果有数据，插入T6524加息券利息返还订单表
        addInterestCoupon(connection, t6252, currentTimestamp, orderId);
        return orderId;
    }
    
    /**
     * 将加息券还款记录修改为失效
     * @throws Throwable
     */
    private void invalidT6289(Connection connection, T6252 t6252)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S62.T6289 SET F09 = ? WHERE F13 = ? AND F09 = ?"))
        {
            pstmt.setString(1, T6289_F09.YSX.name());
            pstmt.setInt(2, t6252.F11);
            pstmt.setString(3, T6289_F09.WFH.name());
            pstmt.execute();
        }
    }
    
    /**
     * 创建加息券利息返回订单
     * @param connection
     * @param t6252
     * @param currentTimestamp
     * @throws Throwable
     */
    private void addInterestCoupon(Connection connection, T6252 t6252, Timestamp currentTimestamp, int paymentOrderId)
        throws Throwable
    {
        //如果是转让过的债权 或者 垫付了的债权，则不还加息利息
        if (!isTransfer(connection, t6252.F11) || !selectT6253(connection, t6252.F02, t6252.F06))
        {
            invalidT6289(connection, t6252);
            return;
        }
        //查询需要还的加息券数据
        T6289 t6289 = getCouponDataList(connection, t6252);
        if (t6289 == null)
        {
            return;
        }
        T6501 t6501 = new T6501();
        t6501.F02 = OrderType.BID_COUPON_REPAYMENT.orderType();
        t6501.F03 = T6501_F03.DTJ;
        t6501.F04 = currentTimestamp;
        t6501.F07 = T6501_F07.YH;
        t6501.F08 = t6289.F03;
        t6501.F13 = t6289.F07;
        int orderId = insertT6501(connection, t6501);
        T6525 t6525 = new T6525();
        t6525.F01 = orderId;
        t6525.F02 = t6289.F04;
        t6525.F03 = t6289.F02;
        t6525.F04 = t6289.F06;
        t6525.F05 = t6289.F07;
        t6525.F06 = t6289.F05;
        t6525.F07 = t6289.F03;
        t6525.F08 = t6289.F01;
        t6525.F09 = paymentOrderId;
        insertT6525(connection, t6525);
        
    }
    
    /**
     * 查询需要还的加息券数据
     * @param connection
     * @param t6252
     * @return
     * @throws Throwable
     */
    private T6289 getCouponDataList(Connection connection, T6252 t6252)
        throws Throwable
    {
        T6289 t6289 = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07,  F08, F09, F10,F11, F12, F13 FROM S62.T6289 WHERE F06 = ? AND F13 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, t6252.F06);
            pstmt.setInt(2, t6252.F11);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    t6289 = new T6289();
                    t6289.F01 = resultSet.getInt(1);
                    t6289.F02 = resultSet.getInt(2);
                    t6289.F03 = resultSet.getInt(3);
                    t6289.F04 = resultSet.getInt(4);
                    t6289.F05 = resultSet.getInt(5);
                    t6289.F06 = resultSet.getInt(6);
                    t6289.F07 = resultSet.getBigDecimal(7);
                    t6289.F08 = resultSet.getDate(8);
                    t6289.F09 = T6289_F09.parse(resultSet.getString(9));
                    t6289.F10 = resultSet.getTimestamp(10);
                    t6289.F11 = resultSet.getBigDecimal(11);
                    t6289.F12 = resultSet.getInt(12);
                    t6289.F13 = resultSet.getInt(13);
                }
            }
        }
        return t6289;
    }
    
    /**
     * 判断是否有被垫付
     * @param connection
     * @param loanId
     * @param periods
     * @return
     * @throws SQLException
     */
    private boolean selectT6253(Connection connection, int loanId, int periods)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S62.T6253 WHERE T6253.F02 = ? AND T6253.F08 <= ? LIMIT 1"))
        {
            pstmt.setInt(1, loanId);
            pstmt.setInt(2, periods);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * 判断债权是否已经转让
     * @param zqId
     * @return
     */
    private boolean isTransfer(Connection connection, int zqId)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6262.F01 FROM S62.T6262 INNER JOIN S62.T6260 ON T6262.F02 = T6260.F01 WHERE T6260.F02 = ?"))
        {
            pstmt.setInt(1, zqId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    private void insertT6525(Connection connection, T6525 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6525 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?"))
        {
            pstmt.setInt(1, entity.F01);
            pstmt.setInt(2, entity.F02);
            pstmt.setInt(3, entity.F03);
            pstmt.setInt(4, entity.F04);
            pstmt.setBigDecimal(5, entity.F05);
            pstmt.setInt(6, entity.F06);
            pstmt.setInt(7, entity.F07);
            pstmt.setInt(8, entity.F08);
            pstmt.setInt(9, entity.F09);
            pstmt.execute();
        }
    }
    
    private void insertT6506(Connection connection, T6506 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6506 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?"))
        {
            pstmt.setInt(1, entity.F01);
            pstmt.setInt(2, entity.F02);
            pstmt.setInt(3, entity.F03);
            pstmt.setInt(4, entity.F04);
            pstmt.setInt(5, entity.F05);
            pstmt.setBigDecimal(6, entity.F06);
            pstmt.setInt(7, entity.F07);
            pstmt.execute();
        }
    }
    
    /*private int insertT6501(Connection connection, T6501 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO S65.T6501 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ? ", PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, entity.F02);
            pstmt.setString(2, entity.F03.name());
            pstmt.setTimestamp(3, entity.F04);
            pstmt.setTimestamp(4, entity.F05);
            pstmt.setTimestamp(5, entity.F06);
            pstmt.setString(6, entity.F07.name());
            pstmt.setInt(7, entity.F08);
            pstmt.setInt(8, entity.F09);
            pstmt.setString(9, entity.F10);
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
    
    /**
     * 查询某标在当前时间未还的所有标还款记录
     * <功能详细描述>
     * @param connection
     * @param F08 时间
     * @param F01 T6252.F01
     * @param F03 付款人ID
     * @return
     * @throws SQLException
     */
    private T6252[] selectT6252Repayment(Connection connection, Date F08, int F01, int F03)
        throws SQLException
    {
        ArrayList<T6252> list = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S62.T6252 WHERE T6252.F02 = "
                + "(SELECT F02 FROM S62.T6252 T6252_1 WHERE T6252_1.F01 = ?) AND T6252.F03 = ? AND T6252.F09 = ? AND T6252.F08 <= ?"))
        {
            pstmt.setInt(1, F01);
            pstmt.setInt(2, F03);
            pstmt.setString(3, T6252_F09.WH.name());
            pstmt.setDate(4, F08);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    T6252 record = new T6252();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getInt(5);
                    record.F06 = resultSet.getInt(6);
                    record.F07 = resultSet.getBigDecimal(7);
                    record.F08 = resultSet.getDate(8);
                    record.F09 = T6252_F09.parse(resultSet.getString(9));
                    record.F10 = resultSet.getTimestamp(10);
                    record.F11 = resultSet.getInt(11);
                    if (list == null)
                    {
                        list = new ArrayList<>();
                    }
                    list.add(record);
                }
            }
        }
        if (list != null && list.size() > 0)
        {
            for (T6252 t6252 : list)
            {
                try (PreparedStatement ps = connection.prepareStatement("UPDATE S62.T6252 SET F09 = ? WHERE F01 =?"))
                {
                    ps.setString(1, T6252_F09.HKZ.name());
                    ps.setInt(2, t6252.F01);
                    ps.executeUpdate();
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new T6252[list.size()]));
    }
    
    private T6252[] selectAllT6252(Connection connection, Date F08)
        throws SQLException
    {
        ArrayList<T6252> list = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S62.T6252 WHERE T6252.F08 <= ? AND T6252.F09 = ? ORDER BY F02,F06 ASC FOR UPDATE"))
        {
            pstmt.setDate(1, F08);
            pstmt.setString(2, T6252_F09.WH.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    T6252 record = new T6252();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getInt(5);
                    record.F06 = resultSet.getInt(6);
                    record.F07 = resultSet.getBigDecimal(7);
                    record.F08 = resultSet.getDate(8);
                    record.F09 = T6252_F09.parse(resultSet.getString(9));
                    record.F10 = resultSet.getTimestamp(10);
                    record.F11 = resultSet.getInt(11);
                    if (list == null)
                    {
                        list = new ArrayList<>();
                    }
                    list.add(record);
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new T6252[list.size()]));
    }
    
    /**
     * 自动还款查询还款记录
     * @param connection
     * @param F08
     * @return
     * @throws SQLException
     */
    private T6252[] selectT6252User(Connection connection, Date F08)
        throws SQLException
    {
        ArrayList<T6252> list = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6252.F01,T6252.F02,T6252.F03,SUM(T6252.F07) AS F07 FROM S62.T6252  WHERE T6252.F09 = ? AND T6252.F08 <= ? GROUP BY T6252.F02 ORDER BY T6252.F03,F07 ASC"))
        {
            pstmt.setString(1, T6252_F09.WH.name());
            pstmt.setDate(2, F08);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    T6252 record = new T6252();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F07 = resultSet.getBigDecimal(4);
                    if (list == null)
                    {
                        list = new ArrayList<>();
                    }
                    list.add(record);
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new T6252[list.size()]));
    }
    
    @Override
    protected Date getCurrentDate(Connection connection)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT CURRENT_DATE()"))
        {
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getDate(1);
                }
            }
        }
        return null;
    }
}
