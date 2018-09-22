package com.dimeng.p2p.modules.base.pay.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.enums.T6103_F06;
import com.dimeng.p2p.S62.entities.T6285;
import com.dimeng.p2p.S62.enums.T6285_F09;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6520;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.modules.base.pay.service.ExperienceManage;
import com.dimeng.p2p.variables.defines.SystemVariable;

public class ExperienceManageImpl extends AbstractBaseManage implements ExperienceManage
{
    
    public ExperienceManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public int[] experienceInterestRtn()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                T6285[] t6285s = selectAllT6285(connection);
                if (t6285s == null || t6285s.length == 0)
                {
                    serviceResource.rollback(connection);
                    return new int[0];
                }
                int[] orderIds = new int[t6285s.length];
                int index = 0;
                Timestamp currentTimestamp = getCurrentTimestamp(connection);
                for (T6285 t6285 : t6285s)
                {
                    orderIds[index++] = addExperienceOrder(connection, t6285, currentTimestamp);
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
    
    private int addExperienceOrder(Connection connection, T6285 t6285, Timestamp currentTimestamp)
        throws Throwable
    {
        T6501 t6501 = new T6501();
        t6501.F02 = OrderType.BID_EXPERIENCE_REPAYMENT.orderType();
        t6501.F03 = T6501_F03.DTJ;
        t6501.F04 = currentTimestamp;
        t6501.F07 = T6501_F07.YH;
        t6501.F08 = t6285.F03;
        t6501.F13 = t6285.F07;
        int orderId = insertT6501(connection, t6501);
        T6520 t6520 = new T6520();
        t6520.F01 = orderId;
        t6520.F02 = t6285.F04;
        t6520.F03 = t6285.F02;
        t6520.F04 = t6285.F06;
        t6520.F05 = t6285.F07;
        t6520.F06 = t6285.F05;
        t6520.F07 = t6285.F03;
        t6520.F08 = t6285.F01;
        insertT6520(connection, t6520);
        return orderId;
    }
    
    /*protected int insertT6501(Connection connection, T6501 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO S65.T6501 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?", PreparedStatement.RETURN_GENERATED_KEYS))
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
    
    protected T6285[] selectAllT6285(Connection connection)
        throws Throwable
    {
        ArrayList<T6285> list = null;
        try
        {
            ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
            int EXPERIENCERETCOUNT =
                Integer.parseInt(configureProvider.getProperty(SystemVariable.EXPERIENCE_RET_COUNT));//每次体验金返还条数
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S62.T6285 WHERE T6285.F08 <= ? AND T6285.F09 IN (?,?) ORDER BY T6285.F05 DESC LIMIT ? FOR UPDATE"))
            {
                pstmt.setDate(1, getCurrentDate(connection));
                pstmt.setString(2, T6285_F09.WFH.name());
                pstmt.setString(3, T6285_F09.FHZ.name());
                pstmt.setInt(4, EXPERIENCERETCOUNT);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T6285 record = new T6285();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getInt(4);
                        record.F05 = resultSet.getInt(5);
                        record.F06 = resultSet.getInt(6);
                        record.F07 = resultSet.getBigDecimal(7);
                        record.F08 = resultSet.getDate(8);
                        record.F09 = T6285_F09.parse(resultSet.getString(9));
                        record.F10 = resultSet.getTimestamp(10);
                        record.F11 = resultSet.getBigDecimal(11);
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
                for (T6285 t6285 : list)
                {
                    try (PreparedStatement ps =
                        connection.prepareStatement("UPDATE S62.T6285 SET F09 = ? WHERE F01 =?"))
                    {
                        ps.setString(1, T6285_F09.FHZ.name());
                        ps.setInt(2, t6285.F01);
                        ps.executeUpdate();
                    }
                }
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new T6285[list.size()]));
    }
    
    private void insertT6520(Connection connection, T6520 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6520 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?"))
        {
            pstmt.setInt(1, entity.F01);
            pstmt.setInt(2, entity.F02);
            pstmt.setInt(3, entity.F03);
            pstmt.setInt(4, entity.F04);
            pstmt.setBigDecimal(5, entity.F05);
            pstmt.setInt(6, entity.F06);
            pstmt.setInt(7, entity.F07);
            pstmt.setInt(8, entity.F08);
            pstmt.execute();
        }
    }
    
    @Override
    public void experienceAmountInvalid()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                List<Integer> list = new ArrayList<Integer>();
                // 查询过期的未使用的体验金
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT T6103.F01 FROM S61.T6103 WHERE DATE(T6103.F05) < ? AND T6103.F06 = ?"))
                {
                    pstmt.setDate(1, getCurrentDate(connection));
                    pstmt.setString(2, T6103_F06.WSY.name());
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        while (resultSet.next())
                        {
                            list.add(resultSet.getInt(1));
                        }
                    }
                }
                // 更新体验金的状态
                if (list.size() > 0)
                {
                    for (Integer intId : list)
                    {
                        try (PreparedStatement pstmt =
                            connection.prepareStatement("UPDATE S61.T6103 SET T6103.F06 = ? WHERE T6103.F01 = ?"))
                        {
                            pstmt.setString(1, T6103_F06.YGQ.name());
                            pstmt.setInt(2, intId);
                            pstmt.execute();
                        }
                    }
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
    
    @Override
    public void changExperienceOrderToWFH(int orderId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement selectps =
                    connection.prepareStatement("SELECT F02,F03,F04 FROM S65.T6520 WHERE F01 = ? "))
                {
                    selectps.setInt(1, orderId);
                    try (ResultSet rs = selectps.executeQuery())
                    {
                        if (rs.next())
                        {
                            try (PreparedStatement updateps =
                                connection.prepareStatement("UPDATE S62.T6285 SET F09 = ?, F10 = null WHERE F04 = ? and F02 = ? and F06 = ?"))
                            {
                                updateps.setString(1, T6285_F09.WFH.name());
                                updateps.setInt(2, rs.getInt(1));
                                updateps.setInt(3, rs.getInt(2));
                                updateps.setInt(4, rs.getInt(3));
                                updateps.executeUpdate();
                            }
                        }
                    }
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
            }
        }
    }
    
}
