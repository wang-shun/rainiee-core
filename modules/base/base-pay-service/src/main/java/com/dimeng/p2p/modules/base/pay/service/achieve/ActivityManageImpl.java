package com.dimeng.p2p.modules.base.pay.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S62.entities.T6289;
import com.dimeng.p2p.S62.enums.T6289_F09;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6525;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.modules.base.pay.service.ActivityManage;

public class ActivityManageImpl extends AbstractBaseManage implements ActivityManage
{
    
    public ActivityManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public int[] activityInterestRtn()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                T6289[] t6289s = selectAllT6289(connection);
                if (t6289s == null || t6289s.length == 0)
                {
                    serviceResource.rollback(connection);
                    return new int[0];
                }
                int[] orderIds = new int[t6289s.length];
                int index = 0;
                Timestamp currentTimestamp = getCurrentTimestamp(connection);
                for (T6289 t6289 : t6289s)
                {
                    orderIds[index++] = addActivityOrder(connection, t6289, currentTimestamp);
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
    
    private int addActivityOrder(Connection connection, T6289 t6289, Timestamp currentTimestamp)
        throws Throwable
    {
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
        insertT6525(connection, t6525);
        return orderId;
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
    
    /*protected int insertT6501(Connection connection, T6501 entity) throws SQLException {
    	try (PreparedStatement pstmt = connection
    			.prepareStatement(
    					"INSERT INTO S65.T6501 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?",
    					PreparedStatement.RETURN_GENERATED_KEYS)) {
    		pstmt.setInt(1, entity.F02);
    		pstmt.setString(2, entity.F03.name());
    		pstmt.setTimestamp(3, entity.F04);
    		pstmt.setTimestamp(4, entity.F05);
    		pstmt.setTimestamp(5, entity.F06);
    		pstmt.setString(6, entity.F07.name());
    		pstmt.setInt(7, entity.F08);
    		pstmt.setInt(8, entity.F09);
    		pstmt.execute();
    		try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
    			if (resultSet.next()) {
    				return resultSet.getInt(1);
    			}
    			return 0;
    		}
    	}
    }*/
    
    /**
     * 获得需要返回利息的记录
     * 
     * @param connection
     * @return
     * @throws Throwable
     */
    protected T6289[] selectAllT6289(Connection connection)
        throws Throwable
    {
        ArrayList<T6289> list = null;
        T6289 t6289 = null;
        try
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13 FROM S62.T6289 WHERE F08 <= ? AND F09 = ? "))
            {
                
                pstmt.setDate(1, getCurrentDate(connection));
                pstmt.setString(2, T6289_F09.WFH.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
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
                        
                        // 如果有债权转让、垫付、提前还款，则不还利息了
                        if (!updateT6289Status(connection, t6289))
                        {
                            continue;
                        }
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(t6289);
                    }
                }
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        return ((list == null || list.size() == 0) ? null : list.toArray(new T6289[list.size()]));
    }
    
    /**
     * 判断是否有债权转让、垫付、提前还款
     * 
     * @param connection
     * @param t6289
     * @return
     * @throws Throwable
     */
    private boolean updateT6289Status(Connection connection, T6289 t6289)
        throws Throwable
    {
        if (!isTransfer(connection, t6289.F13) || !selectT6253(connection, t6289.F02, t6289.F06))
        {
            invalidT6289(connection, t6289);
            return false;
        }
        return true;
    }
    
    /**
     * 将加息券还款记录修改为失效
     * 
     * @throws Throwable
     */
    private void invalidT6289(Connection connection, T6289 t6289)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S62.T6289 SET F09 = ? WHERE F01 = ? "))
        {
            pstmt.setString(1, T6289_F09.YSX.name());
            pstmt.setInt(2, t6289.F01);
            pstmt.execute();
        }
    }
    
    /**
     * 判断债权是否已经转让
     * 
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
    
    /**
     * 判断是否有被垫付
     * 
     * @param connection
     * @param loanId
     * @param periods
     * @return
     * @throws SQLException
     */
    protected boolean selectT6253(Connection connection, int loanId, int periods)
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
                    connection.prepareStatement("SELECT F02,F03,F04 FROM S65.T6525 WHERE F01 = ? "))
                {
                    selectps.setInt(1, orderId);
                    try (ResultSet rs = selectps.executeQuery())
                    {
                        if (rs.next())
                        {
                            try (PreparedStatement updateps =
                                connection.prepareStatement("UPDATE S62.T6289 SET F09 = ?, F10 = null WHERE F04 = ? and F02 = ? and F06 = ?"))
                            {
                                updateps.setString(1, T6289_F09.WFH.name());
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
