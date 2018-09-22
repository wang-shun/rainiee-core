package com.dimeng.p2p.escrow.fuyou.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.ServiceSession;
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
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6521;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.escrow.fuyou.entity.TransactionQueryDetailedEntity;
import com.dimeng.p2p.escrow.fuyou.entity.TransactionQueryResponseEntity;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouEnum;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.face.TransactionQueryFace;
import com.dimeng.p2p.escrow.fuyou.service.PrepaymentManage;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.dimeng.util.parser.EnumParser;

/**
 * 
 * 提前还款
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月9日]
 */
public class PrepaymentManageImpl extends AbstractEscrowService implements PrepaymentManage
{
    
    public PrepaymentManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    /**
     * {@inheritDoc}
     * 提前还款三种情况
     * 1、失败后
     * 2、非本期
     * 3、重复单
     */
    @Override
    public int[] prepayment(int bidId, int term, Map<String, String> params)
        throws Throwable
    {
        if (bidId <= 0)
        {
            throw new LogicalException("标记录不存在!");
        }
        int accountId = serviceResource.getSession().getAccountId();
        try (Connection connection = getConnection())
        {
            try
            {
                // 打开事务
                serviceResource.openTransactions(connection);
                // 获取标的信息
                T6230 t6230 = selectT6230(connection, bidId, accountId);
                if (t6230 == null)
                {
                    throw new LogicalException("标记录不存在!");
                }
                if (t6230.F20 != T6230_F20.HKZ && t6230.F20 != T6230_F20.YDF)
                {
                    throw new LogicalException("不是还款中状态,不能还款!");
                }
                // 查询本期应还资金<本息>
                BigDecimal bx = selectBx(connection, bidId, term);
                // 查询剩余本金
                BigDecimal sybj = selectSybj(connection, bidId, term);
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
                    throw new LogicalException("可用余额不足,不能还款!");
                }
                // 获取还款计划
                T6252[] t6252s = selectAllT6252(connection, bidId, term, forwardBp, forwardRp);
                if (t6252s == null)
                {
                    return null;
                }
                int[] orderIds = null;
                orderIds = selectT6521(connection, bidId, t6252s[0].F03);
                if (orderIds != null)
                {
                    if (t6252s.length <= orderIds.length)
                    {
                        logger.info("提前还款订单已存在,订单数：" + orderIds.length + "-剩余未还款订单数：" + t6252s.length);
                        params.put("flag", "false");
                        serviceResource.rollback(connection);
                        return orderIds;
                    }
                    logger.info("提前还款订单重新生成,订单数：" + t6252s.length + "原订单数：" + orderIds.length);
                }
                params.put("flag", "true");
                // 提前还款订单数组
                orderIds = new int[t6252s.length];
                int index = 0;
                for (T6252 t6252 : t6252s)
                {
                    orderIds[index++] = addOrder(connection, t6252, term);
                }
                // 提交事务
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
     * 查询统计提还款订单数
     * @param connection
     * @param bidId 标ID
     * @param fkId 还款人ID
     * @return
     * @throws SQLException 
     */
    private int[] selectT6521(Connection connection, int bidId, int fkId)
        throws SQLException
    {
        int[] orderIds = null;
        int i = 0;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT COUNT(*) FROM S65.T6521 WHERE T6521.F03 = ? AND T6521.F09 = ? ORDER BY T6521.F01"))
        {
            pstmt.setInt(1, bidId);
            pstmt.setInt(2, fkId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    i = resultSet.getInt(1);
                }
            }
        }
        if (i == 0)
        {
            return null;
        }
        int index = 0;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01 FROM S65.T6521 WHERE T6521.F03 = ? AND T6521.F09 = ? ORDER BY T6521.F01"))
        {
            pstmt.setInt(1, bidId);
            pstmt.setInt(2, fkId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                orderIds = new int[i];
                while (resultSet.next())
                {
                    orderIds[index++] = resultSet.getInt(1);
                }
            }
        }
        return orderIds;
    }
    
    /**
     * 增加订单
     * @param connection
     * @param t6252
     * @param currentTimestamp
     * @param F08
     * @return
     * @throws Throwable
     */
    private int addOrder(Connection connection, T6252 t6252, int F08)
        throws Throwable
    {
        T6501 t6501 = new T6501();
        t6501.F02 = OrderType.PREPAYMENT_LOAN.orderType();
        t6501.F03 = T6501_F03.DTJ;
        t6501.F04 = getCurrentTimestamp(connection);
        t6501.F07 = T6501_F07.YH;
        t6501.F08 = t6252.F03;
        t6501.F10 = MchntTxnSsn.getMts(FuyouTypeEnum.TQHK.name());
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
    
    /**
     * 增加订单
     * <提前还款订单>
     * @param connection
     * @param entity T6501
     * @return
     * @throws SQLException
     */
    /*protected int insertT6501(Connection connection, T6501 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6501 SET F02 = ?, F03 = ?, F04 = CURRENT_TIMESTAMP(), F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ? , F10 = ?",
                PreparedStatement.RETURN_GENERATED_KEYS))
        {
            pstmt.setInt(1, entity.F02);
            pstmt.setString(2, entity.F03.name());
            pstmt.setTimestamp(3, entity.F04);
            pstmt.setTimestamp(4, entity.F06);
            pstmt.setString(5, entity.F07.name());
            pstmt.setInt(6, entity.F08);
            pstmt.setInt(7, entity.F09);
            pstmt.setString(8, entity.F10);
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
     * 增加提前还款订单
     * @param connection
     * @param entity T6521
     * @throws SQLException
     */
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
    
    /**
     * 查询提前还款单
     * @param connection
     * @param F02 标ID
     * @param F06 期号
     * @param forwardBp 提前还款违约金费率
     * @param forwardRp 提前还款手续费费率
     * @return
     * @throws ResourceNotFoundException
     * @throws Throwable
     */
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
            	T6252 record = null;
                while (resultSet.next())
                {
                    record = new T6252();
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
    
    /**
     * 提前还款手续费
     * <功能详细描述>
     * @param resultSet
     * @param fl
     * @param F05
     * @param pid
     * @return
     * @throws SQLException
     */
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
    
    /**
     * 查询剩余本金
     * @param connection
     * @param F02 标ID
     * @param F06 期号
     * @return
     * @throws SQLException
     */
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
    
    /**
     * 查询本期本应还资金
     * <本金+利期>
     * @param connection
     * @param F02 标ID
     * @param F06 期号
     * @return
     * @throws SQLException
     */
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
    
    /**
     * 查询标信息
     * @param connection
     * @param F01 标ID
     * @param F02 用户ID
     * @return
     * @throws SQLException
     */
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
    
    @Override
    public void updateT6252(int bidId, int term)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S62.T6252 SET F09 = ? WHERE F02 = ? AND F06 = ? AND F09 = ?"))
            {
                pstmt.setString(1, T6252_F09.WH.name());
                //                pstmt.setInt(2, FeeCode.TZ_LX);
                pstmt.setInt(2, bidId);
                pstmt.setInt(3, term);
                pstmt.setString(4, T6252_F09.HKZ.name());
                pstmt.execute();
            }
        }
    }
    
    @Override
    public void selectT6501(int F01, ServiceSession serviceSession, Map<String, String> params)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6501 t6501 = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F10 FROM S65.T6501 WHERE T6501.F01 = ? "))
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
                        t6501.F04 = resultSet.getTimestamp(4);
                        t6501.F05 = resultSet.getTimestamp(5);
                        t6501.F06 = resultSet.getTimestamp(6);
                        t6501.F07 = EnumParser.parse(T6501_F07.class, resultSet.getString(7));
                        t6501.F08 = resultSet.getInt(8);
                        t6501.F10 = resultSet.getString(9);
                    }
                }
            }
            if (t6501 == null)
            {
                throw new LogicalException("订单查询异常!");
            }
            switch (t6501.F03.toString())
            {
                case "DTJ":
                    params.put("state", "DTJ");
                    break;
                case "CG":
                    params.put("state", "CG");
                    break;
                case "DQR":
                case "SB":
                    String czzh = selectT6119(connection, t6501.F08);
                    // 对账<是否已功-成功则确认，未成功则更新流水号>
                    selectFuyou(czzh, t6501, FuyouEnum.PW03.name(), connection, serviceSession, params);
                    break;
            }
        }
    }
    
    /**
     * 对失败订单，对账
     * <成功则确认，未成功则更新流水号>
     * @param czzh
     * @param t6501
     * @param busi_tp 查询类型
     * @param connection
     * @param serviceSession
     * @param params
     * @return
     * @throws Throwable
     */
    private void selectFuyou(String czzh, T6501 t6501, String busi_tp, Connection connection,
        ServiceSession serviceSession, Map<String, String> params)
        throws Throwable
    {
        // 流水号
        String mchnt_txn_ssn = MchntTxnSsn.getMts(FuyouTypeEnum.JYCX.name());
        //起始时间
        String start_day = new SimpleDateFormat("yyyyMMdd").format(t6501.F04);
        // 结束时间
        String end_day = new SimpleDateFormat("yyyyMMdd").format(t6501.F04);
        // 交易流水
        String txn_ssn = t6501.F10;
        //用户
        String cust_no = czzh;
        // 交易查询接口地址
        String tradingQueryUrl = configureProvider.format(FuyouVariable.FUYOU_TRADINGQUERY_URL);
        //调用第三方平台
        TransactionQueryResponseEntity result =
            TransactionQueryFace.executeTransactionQuery(configureProvider.format(FuyouVariable.FUYOU_ACCOUNT_ID),
                mchnt_txn_ssn,
                busi_tp,
                start_day,
                end_day,
                txn_ssn,
                cust_no,
                "",
                "",
                "",
                "",
                tradingQueryUrl,
                serviceSession);
        String resultCode = result.respCode;
        // 定义返回结果详情对象
        List<TransactionQueryDetailedEntity> respDetailList = null;
        if (FuyouRespCode.JYCG.getRespCode().equals(resultCode))
        {
            //结果详情
            respDetailList = result.detailedEntity;
            if (respDetailList != null && respDetailList.size() > 0)
            {
                for (TransactionQueryDetailedEntity detail : respDetailList)
                {
                    // 获取流水号
                    String serialNumStr = detail.getMchntSsn();
                    // 通过流水号 判断该笔交易是不是 要查询的那笔交易
                    if (!StringHelper.isEmpty(serialNumStr) && serialNumStr.equals(t6501.F10))
                    {
                        if (FuyouRespCode.JYCG.getRespCode().equals(detail.getTxnRspCd()))
                        { // 说明在托管平台成功了，在P2P这边意外失败了,现只差确认成功
                            updateT6501(connection, txn_ssn, t6501.F01, T6501_F03.DQR.name());
                            params.put("state", "DQR");
                        }
                        else
                        {
                            // 说明第托管平台失败了，P2P重新生成流水号
                            logger.info("提前还款订单：" + t6501.F01 + " -重新生成流水号");
                            txn_ssn = MchntTxnSsn.getMts(FuyouTypeEnum.NEW.name());
                            updateT6501(connection, txn_ssn, t6501.F01, T6501_F03.DTJ.name());
                            params.put("state", "DTJ");
                        }
                    }
                }
            }
            else
            {
                // 查询结果无，说明托管系统无此记录
                updateT6501(connection, txn_ssn, t6501.F01, T6501_F03.DTJ.name());
                params.put("state", "DTJ");
            }
        }
    }
    
    /**
     * 将订单改为待确认状态
     * @param connection
     * @param mchnt_txn_ssn 流水号
     * @param F01 订单ID
     * @throws SQLException
     */
    private void updateT6501(Connection connection, String mchnt_txn_ssn, int F01, String state)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F10 = ? WHERE F01 = ? "))
        {
            pstmt.setString(1, state);
            pstmt.setString(2, mchnt_txn_ssn);
            pstmt.setInt(3, F01);
            pstmt.execute();
        }
    }
}
