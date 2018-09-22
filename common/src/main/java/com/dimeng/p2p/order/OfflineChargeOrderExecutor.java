package com.dimeng.p2p.order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S65.entities.T6509;
import com.dimeng.p2p.S71.entities.T7150;
import com.dimeng.p2p.S71.enums.T7150_F03;
import com.dimeng.p2p.S71.enums.T7150_F05;

@ResourceAnnotation
public class OfflineChargeOrderExecutor extends AbstractOrderExecutor
{
    
    public OfflineChargeOrderExecutor(ResourceProvider resourceProvider)
    {
        super(resourceProvider);
    }
    
    @Override
    public Class<? extends Resource> getIdentifiedType()
    {
        return OfflineChargeOrderExecutor.class;
    }
    
    @Override
    protected void doConfirm(SQLConnection connection, int orderId, Map<String, String> params)
        throws Throwable
    {
        try
        {
            T6509 t6509 = selectT6509(connection, orderId);
            if (t6509 == null)
            {
                throw new LogicalException("充值记录不存在");
            }
            T7150 t7150 = selectT7150(connection, t6509.F06);
            T6101 t6101 = selectT6101(connection, t6509.F02, T6101_F03.WLZH, true);
            if (t6101 == null)
            {
                throw new LogicalException("用户资金账户不存在");
            }
            // 平台用户id
            int pid = getPTID(connection);
            // 平台往来账户信息
            T6101 ptwlzh = selectT6101(connection, pid, T6101_F03.WLZH, true);
            if (ptwlzh == null)
            {
                throw new LogicalException("平台往来账户不存在");
            }
            if (ptwlzh.F06.compareTo(t6509.F03) == -1)
            {
                throw new LogicalException("平台账户余额不足");
            }
            Timestamp currentTimestamp = getCurrentTimestamp(connection);
            
            // 更新用户账号信息
            t6101.F06 = t6101.F06.add(t6509.F03);
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = ? WHERE F01 = ?"))
            {
                pstmt.setBigDecimal(1, t6101.F06);
                pstmt.setTimestamp(2, currentTimestamp);
                pstmt.setInt(3, t6101.F01);
                pstmt.executeUpdate();
            }
            // 插入用户资金交易记录，充值
            T6102 t6102 = new T6102();
            t6102.F02 = t6101.F01;
            t6102.F03 = FeeCode.CZ_XX;
            t6102.F04 = ptwlzh.F01;
            t6102.F06 = t6509.F03;
            t6102.F08 = t6101.F06;
            t6102.F09 = String.format("线下充值:%s", t7150.F08);
            insertT6102(connection, t6102);
            
            // 更新平台账号信息
            ptwlzh.F06 = ptwlzh.F06.subtract(t6509.F03);
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = ? WHERE F01 = ?"))
            {
                pstmt.setBigDecimal(1, ptwlzh.F06);
                pstmt.setTimestamp(2, currentTimestamp);
                pstmt.setInt(3, ptwlzh.F01);
                pstmt.executeUpdate();
            }
            // 插入平台资金交易记录，充值
            T6102 ptT6102 = new T6102();
            ptT6102.F02 = ptwlzh.F01;
            ptT6102.F03 = FeeCode.CZ_XX;
            ptT6102.F04 = t6101.F01;
            ptT6102.F07 = t6509.F03;
            ptT6102.F08 = ptwlzh.F06;
            ptT6102.F09 = String.format("线下充值:%s", t7150.F08);
            insertT6102(connection, ptT6102);
            
            try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S71.T7150 SET F05 = ? WHERE F01 = ?"))
            {
                pstmt.setString(1, T7150_F05.YCZ.name());
                pstmt.setInt(2, t7150.F01);
                pstmt.executeUpdate();
            }
        }
        catch (Exception e)
        {
            logger.error(e, e);
            throw e;
        }
    }
    
    @Override
    protected void doSubmit(SQLConnection connection, int orderId, Map<String, String> params)
        throws Throwable
    {
        
    }
    
    protected T6509 selectT6509(Connection connection, int F01)
        throws SQLException
    {
        T6509 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05,F06 FROM S65.T6509 WHERE T6509.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6509();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getBigDecimal(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getString(5);
                    record.F06 = resultSet.getInt(6);
                }
            }
        }
        return record;
    }
    
    protected void updateT6101(Connection connection, BigDecimal F01, int F02)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = ? WHERE F01 = ?"))
        {
            pstmt.setBigDecimal(1, F01);
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, F02);
            pstmt.execute();
        }
    }
    
    protected void updateT6101(Connection connection, BigDecimal F01, int F02, T6101_F03 F03)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6101 SET F06 = F06 + ?, F07 = ? WHERE F02 = ? AND F03 = ?"))
        {
            pstmt.setBigDecimal(1, F01);
            pstmt.setTimestamp(2, getCurrentTimestamp(connection));
            pstmt.setInt(3, F02);
            pstmt.setString(4, F03.name());
            pstmt.execute();
        }
    }
    
    protected void updateT6509(Connection connection, String F01, int F02)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S65.T6509 SET F05 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, F01);
            pstmt.setInt(2, F02);
            pstmt.execute();
        }
    }
    
    protected T7150 selectT7150(Connection connection, int F01)
        throws SQLException
    {
        T7150 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S71.T7150 WHERE T7150.F01 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T7150();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = T7150_F03.parse(resultSet.getString(3));
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = T7150_F05.parse(resultSet.getString(5));
                    record.F06 = resultSet.getInt(6);
                    record.F07 = resultSet.getTimestamp(7);
                    record.F08 = resultSet.getString(8);
                    record.F09 = resultSet.getInt(9);
                    record.F10 = resultSet.getTimestamp(10);
                    record.F11 = resultSet.getString(11);
                }
            }
        }
        return record;
    }
    
}