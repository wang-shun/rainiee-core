package com.dimeng.p2p.modules.bid.user.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6252;
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
import com.dimeng.p2p.S62.enums.T6231_F19;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6521;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.modules.bid.user.service.TenderPrepaymentManage;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BigDecimalParser;

public class TenderPrepaymentManageImpl extends AbstractBidManage implements TenderPrepaymentManage
{
    
    public TenderPrepaymentManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public int[] prepayment(int bidId, int term)
        throws Throwable
    {
        if (bidId <= 0)
        {
            throw new LogicalException("标记录不存在");
        }
        int accountId = serviceResource.getSession().getAccountId();
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                // 获取标的信息
                T6230 t6230 = selectT6230(connection, bidId, accountId);
                if (t6230 == null)
                {
                    throw new LogicalException("标记录不存在");
                }
                if (isYuqi(bidId, connection))
                {
                    throw new LogicalException("该标的已逾期不能还款");
                }
                if (t6230.F20 != T6230_F20.HKZ && t6230.F20 != T6230_F20.YDF)
                {
                    throw new LogicalException("不是还款中状态,不能还款");
                }
                // 查询本期本息
                BigDecimal bx = selectBx(connection, bidId, term);
                // 查询剩余本金
                BigDecimal sybj = selectSybj(connection, bidId, term);
                ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
                // 提前还款违约金费率
                BigDecimal forwardBp =
                    BigDecimalParser.parse(configureProvider.getProperty(SystemVariable.FORWARD_REPAY_BREACH_POUNDAGE));
                BigDecimal tqhkwyj = sybj.multiply(forwardBp).setScale(2, BigDecimal.ROUND_HALF_UP);
                // 提前还款手续费费率
                BigDecimal forwardRp =
                    BigDecimalParser.parse(configureProvider.getProperty(SystemVariable.FORWARD_REPAY_POUNDAGE));
                BigDecimal tqhksxf = sybj.multiply(forwardRp).setScale(2, BigDecimal.ROUND_HALF_UP);
                // 提前还款所需总额
                BigDecimal temp = bx.add(sybj).add(tqhkwyj).add(tqhksxf);
                //还款人往来账户
                T6101 hkrAcount = selectT6101(connection, accountId, T6101_F03.WLZH, false);
                if (hkrAcount.F06.compareTo(temp) == -1)
                {
                    throw new LogicalException("可用余额不足,不能还款");
                }
                // 获取还款计划
                T6252[] t6252s = selectAllT6252(connection, bidId, term, forwardBp, forwardRp);
                int[] orderIds = new int[t6252s.length];
                int index = 0;
                Timestamp currentTimestamp = getCurrentTimestamp(connection);
                for (T6252 t6252 : t6252s)
                {
                    orderIds[index++] = addOrder(connection, t6252, currentTimestamp, term);
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
    
    private T6230 selectT6230(Connection connection, int F01, int F02)
        throws SQLException
    {
        T6230 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25, F26, F27 FROM S62.T6230 WHERE T6230.F01 = ? AND T6230.F02 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            pstmt.setInt(2, F02);
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
    
    protected T6252[] selectAllT6252(Connection connection, int F02, int F06, BigDecimal forwardBp, BigDecimal forwardRp)
        throws ResourceNotFoundException, Throwable
    {
        ArrayList<T6252> list = null;
        int pid = getPTID(connection);
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F06 = ? AND T6252.F09 = ? ORDER BY T6252.F01 FOR UPDATE"))
        {
            pstmt.setInt(1, F02);
            pstmt.setInt(2, F06);
            pstmt.setString(3, T6252_F09.WH.name());
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
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F06 != ? AND T6252.F09 = ? AND T6252.F05 = ? ORDER BY T6252.F01 FOR UPDATE"))
        {
            pstmt.setInt(1, F02);
            pstmt.setInt(2, F06);
            pstmt.setString(3, T6252_F09.WH.name());
            pstmt.setInt(4, FeeCode.TZ_BJ);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    // 本金还款订单
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
        
        if (forwardBp.compareTo(BigDecimal.ZERO) > 0)
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, SUM(F07), F08, F09, F10, F11 FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F06 > ? AND T6252.F09 = ? AND T6252.F05 = ? GROUP BY T6252.F04 ORDER BY T6252.F01 FOR UPDATE"))
            {
                pstmt.setInt(1, F02);
                pstmt.setInt(2, F06);
                pstmt.setString(3, T6252_F09.WH.name());
                pstmt.setInt(4, FeeCode.TZ_BJ);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        // 提前还款违约金订单
                        list.add(tqhkRecord(resultSet, forwardBp, FeeCode.TZ_WYJ, pid));
                    }
                }
            }
        }
        
        if (forwardRp.compareTo(BigDecimal.ZERO) > 0)
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, SUM(F07), F08, F09, F10, F11 FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F06 > ? AND T6252.F09 = ? AND T6252.F05 = ? GROUP BY T6252.F02 ORDER BY T6252.F01 FOR UPDATE"))
            {
                pstmt.setInt(1, F02);
                pstmt.setInt(2, F06);
                pstmt.setString(3, T6252_F09.WH.name());
                pstmt.setInt(4, FeeCode.TZ_BJ);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        // 提前还款手续费订单
                        list.add(tqhkRecord(resultSet, forwardRp, FeeCode.TZ_WYJ_SXF, pid));
                    }
                }
            }
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new T6252[list.size()]));
    }
    
    private BigDecimal selectBx(Connection connection, int F02, int F06)
        throws SQLException
    {
        BigDecimal bx = BigDecimal.ZERO;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT SUM(F07) AS F07 FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F06 = ? AND T6252.F09 = ? "))
        {
            pstmt.setInt(1, F02);
            pstmt.setInt(2, F06);
            pstmt.setString(3, T6252_F09.WH.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    bx = resultSet.getBigDecimal(1);
                }
            }
        }
        return bx;
    }
    
    private BigDecimal selectSybj(Connection connection, int F02, int F06)
        throws SQLException
    {
        BigDecimal sybj = BigDecimal.ZERO;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT SUM(F07) AS F07 FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F06 != ? AND T6252.F09 = ? AND T6252.F05 = ? "))
        {
            pstmt.setInt(1, F02);
            pstmt.setInt(2, F06);
            pstmt.setString(3, T6252_F09.WH.name());
            pstmt.setInt(4, FeeCode.TZ_BJ);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    sybj = resultSet.getBigDecimal(1);
                }
            }
        }
        return sybj;
    }
    
    private T6252 tqhkRecord(ResultSet resultSet, BigDecimal fl, int F05, int pid)
        throws SQLException
    {
        T6252 tqRecord = new T6252();
        tqRecord.F01 = resultSet.getInt(1);
        tqRecord.F02 = resultSet.getInt(2);
        tqRecord.F03 = resultSet.getInt(3);
        if (FeeCode.TZ_WYJ_SXF == F05)
        {
            tqRecord.F04 = pid;
        }
        else
        {
            tqRecord.F04 = resultSet.getInt(4);
        }
        tqRecord.F05 = F05;
        tqRecord.F06 = resultSet.getInt(6);
        tqRecord.F07 = resultSet.getBigDecimal(7).multiply(fl).setScale(2, BigDecimal.ROUND_HALF_UP);
        tqRecord.F08 = resultSet.getDate(8);
        tqRecord.F09 = T6252_F09.parse(resultSet.getString(9));
        tqRecord.F10 = resultSet.getTimestamp(10);
        tqRecord.F11 = resultSet.getInt(11);
        return tqRecord;
    }
    
    private int addOrder(Connection connection, T6252 t6252, Timestamp currentTimestamp, int F08)
        throws Throwable
    {
        T6501 t6501 = new T6501();
        t6501.F02 = OrderType.PREPAYMENT_LOAN.orderType();
        t6501.F03 = T6501_F03.DTJ;
        t6501.F04 = currentTimestamp;
        t6501.F07 = T6501_F07.YH;
        t6501.F08 = t6252.F03;
        t6501.F13 = t6252.F07;
        int orderId = insertT6501(connection, t6501);
        T6521 t6521 = new T6521();
        t6521.F01 = orderId;
        t6521.F02 = t6252.F04;
        t6521.F03 = t6252.F02;
        t6521.F04 = t6252.F11;
        t6521.F05 = t6252.F06;
        t6521.F06 = t6252.F07;
        t6521.F07 = t6252.F05;
        t6521.F08 = F08;
        t6521.F09 = t6252.F03;
        insertT6521(connection, t6521);
        return orderId;
    }
    
    /*protected int insertT6501(Connection connection, T6501 entity)
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
    
    private void insertT6521(Connection connection, T6521 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6521 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?"))
        {
            pstmt.setInt(1, entity.F01);
            pstmt.setInt(2, entity.F02);
            pstmt.setInt(3, entity.F03);
            pstmt.setInt(4, entity.F04);
            pstmt.setInt(5, entity.F05);
            pstmt.setBigDecimal(6, entity.F06);
            pstmt.setInt(7, entity.F07);
            pstmt.setInt(8, entity.F08);
            pstmt.setInt(9, entity.F09);
            pstmt.execute();
        }
    }
    
    @Override
    public void checkPrepayment(int bidId, int term)
        throws Throwable
    {
        if (bidId <= 0)
        {
            throw new LogicalException("标记录不存在");
        }
        int accountId = serviceResource.getSession().getAccountId();
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                // 获取标的信息
                T6230 t6230 = selectT6230(connection, bidId, accountId);
                
                if (t6230 == null)
                {
                    throw new LogicalException("标记录不存在");
                }
                if (t6230.F20 != T6230_F20.HKZ && t6230.F20 != T6230_F20.YDF)
                {
                    throw new LogicalException("不是还款中状态,不能还款");
                }
                // 查询本期本息
                BigDecimal bx = selectBx(connection, bidId, term);
                // 查询剩余本金
                BigDecimal sybj = selectSybj(connection, bidId, term);
                ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
                // 提前还款违约金费率
                BigDecimal forwardBp =
                    BigDecimalParser.parse(configureProvider.getProperty(SystemVariable.FORWARD_REPAY_BREACH_POUNDAGE));
                BigDecimal tqhkwyj = sybj.multiply(forwardBp).setScale(2, BigDecimal.ROUND_HALF_UP);
                // 提前还款手续费费率
                BigDecimal forwardRp =
                    BigDecimalParser.parse(configureProvider.getProperty(SystemVariable.FORWARD_REPAY_POUNDAGE));
                BigDecimal tqhksxf = sybj.multiply(forwardRp).setScale(2, BigDecimal.ROUND_HALF_UP);
                // 提前还款所需总额
                BigDecimal temp = bx.add(sybj).add(tqhkwyj).add(tqhksxf);
                //还款人往来账户
                T6101 hkrAcount = selectT6101(connection, accountId, T6101_F03.WLZH, false);
                if (hkrAcount.F06.compareTo(temp) == -1)
                {
                    throw new LogicalException("可用余额不足,不能还款");
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    private boolean isYuqi(int loanId, Connection connection)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT F19 FROM S62.T6231 WHERE F01 = ?"))
        {
            pstmt.setInt(1, loanId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next() && !T6231_F19.F.name().equals(resultSet.getString(1)))
                {
                    return true;
                }
            }
        }
        return false;
    }
}
