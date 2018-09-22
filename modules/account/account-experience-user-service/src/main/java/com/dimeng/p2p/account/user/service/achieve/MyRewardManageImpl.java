package com.dimeng.p2p.account.user.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.enums.T6289_F09;
import com.dimeng.p2p.S63.entities.T6342;
import com.dimeng.p2p.S63.enums.T6342_F04;
import com.dimeng.p2p.account.user.service.MyRewardManage;
import com.dimeng.p2p.account.user.service.entity.MyRewardRecod;
import com.dimeng.util.StringHelper;

/**
 * 我的奖励接口 包括 我的红包、我的加息券
 */
public class MyRewardManageImpl extends AbstractAccountService implements MyRewardManage
{
    
    public MyRewardManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<MyRewardRecod> searchMyReward(Map<String, Object> params, Paging paging)
        throws Throwable
    {
        ArrayList<Object> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT T6342.F01 AS F01, T6342.F04 AS F02, T6342.F05 AS F03, ");
        sql.append(" T6342.F06 AS F04, T6342.F07 AS F05, T6342.F08 AS F06, T6340.F04 AS F07, T6344.F05 AS F08,");
        sql.append(" T6344.F07 AS F09, T6344.F06 AS F10 FROM S63.T6342 INNER JOIN S63.T6344 ON T6342.F03 = T6344.F01 ");
        sql.append(" INNER JOIN S63.T6340 ON T6344.F02 = T6340.F01 WHERE T6342.F02 = ? AND T6340.F03 = ? ");
        list.add(serviceResource.getSession().getAccountId());
        list.add(String.valueOf(params.get("type")));
        String status = String.valueOf(params.get("status"));
        if (!StringHelper.isEmpty(status) && !"null".equals(status))
        {
            if (T6342_F04.YSY.name().equals(status))
            {
                sql.append(" AND T6342.F04 = ? ");
                list.add(T6342_F04.YSY.name());
            }
            else
            {
                sql.append(" AND T6342.F04 = ?");
                list.add(status);
            }
        }
        sql.append(" ORDER BY FIELD(T6342.F04,'WSY','YSY','YGQ') , T6342.F07 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<MyRewardRecod>()
            {
                @Override
                public MyRewardRecod[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<MyRewardRecod> list = null;
                    
                    while (resultSet.next())
                    {
                        MyRewardRecod record = new MyRewardRecod();
                        record.F01 = resultSet.getInt(1);
                        record.F04 = T6342_F04.parse(resultSet.getString(2));
                        record.F05 = resultSet.getTimestamp(3);
                        record.F06 = resultSet.getInt(4);
                        record.F07 = resultSet.getTimestamp(5);
                        record.F08 = resultSet.getTimestamp(6);
                        record.type = resultSet.getString(7);
                        record.value = resultSet.getBigDecimal(8);
                        record.useRule = resultSet.getBigDecimal(9).compareTo(BigDecimal.ZERO) > 0 ? 1 : 0;
                        record.investUseRule = resultSet.getBigDecimal(9);
                        record.quota = resultSet.getBigDecimal(10);
                        if (list == null)
                        {
                            list = new ArrayList<MyRewardRecod>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new MyRewardRecod[list.size()]));
                }
            }, paging, sql.toString(), list);
        }
    }
    
    @Override
    public int getJxqCount(Map<String, Object> params)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT COUNT(1) FROM S63.T6342 INNER JOIN S63.T6344 ON T6342.F03 = T6344.F01 INNER JOIN S63.T6340 ON T6344.F02 = T6340.F01 WHERE T6342.F02 = ? AND T6342.F04 = ? AND T6340.F03 = ? "))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, String.valueOf(params.get("useStatus")));
                pstmt.setString(3, String.valueOf(params.get("type")));
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getInt(1);
                    }
                }
            }
        }
        return 0;
    }
    
    @Override
    public int getJxqUsedCount(Map<String, Object> params)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement(" SELECT COUNT(1) FROM S63.T6342 INNER JOIN S63.T6344 ON T6342.F03 = T6344.F01 INNER JOIN S63.T6340 ON T6340.F01 = T6344.F02 WHERE T6342.F02 =? AND T6342.F04 =? AND T6340.F03 =? "))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, T6342_F04.YSY.name());
                pstmt.setString(3, String.valueOf(params.get("type")));
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getInt(1);
                    }
                }
            }
        }
        return 0;
    }
    
    @Override
    public BigDecimal getHbAmount(Map<String, Object> params)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT IFNULL(SUM(T6344.F05), 0) FROM S63.T6342 INNER JOIN S63.T6344 ON T6342.F03 = T6344.F01 INNER JOIN S63.T6340 ON T6340.F01 = T6344.F02 WHERE T6342.F02 = ? ");
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(serviceResource.getSession().getAccountId());
        if (params.get("useStatus") != null && !StringHelper.isEmpty(String.valueOf(params.get("useStatus"))))
        {
            sql.append(" AND T6342.F04=?");
            parameters.add(String.valueOf(params.get("useStatus")));
        }
        if (params.get("type") != null && !StringHelper.isEmpty(String.valueOf(params.get("type"))))
        {
            sql.append(" AND T6340.F03=?");
            parameters.add(String.valueOf(params.get("type")));
        }
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<BigDecimal>()
            {
                @Override
                public BigDecimal parse(ResultSet rs)
                    throws SQLException
                {
                    if (rs.next())
                    {
                        return rs.getBigDecimal(1);
                    }
                    return BigDecimal.ZERO;
                }
            }, sql.toString(), parameters);
        }
    }
    
    /**
     * 查找我的奖励
     * 
     * @return
     * @throws Throwable
     */
    @Override
    public List<MyRewardRecod> getMyRewardRecodList(Map<String, Object> params)
    {
        List<MyRewardRecod> list = new ArrayList<MyRewardRecod>();
        try
        {
            try (Connection connection = getConnection())
            {
                String sql =
                    "SELECT T6342.F01 AS F01, T6342.F04 AS F02, T6342.F05 AS F03, T6342.F06 AS F04, "
                        + "T6342.F07 AS F05, T6342.F08 AS F06, T6340.F04 AS F07, T6344.F05 AS F08, T6344.F07 AS F09,T6344.F06 AS F10 "
                        + "FROM S63.T6342 INNER JOIN S63.T6344 ON T6342.F03 = T6344.F01 INNER JOIN S63.T6340 ON T6344.F02 = T6340.F01 "
                        + "WHERE T6342.F02 = ? AND T6342.F04 = ? AND T6340.F03 = ? ORDER BY T6342.F08";
                try (PreparedStatement pstmt = connection.prepareStatement(sql))
                {
                    pstmt.setInt(1, serviceResource.getSession().getAccountId());
                    pstmt.setString(2, String.valueOf(params.get("status")));
                    pstmt.setString(3, String.valueOf(params.get("type")));
                    
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        while (resultSet.next())
                        {
                            MyRewardRecod record = new MyRewardRecod();
                            record.F01 = resultSet.getInt(1);
                            record.F04 = T6342_F04.parse(resultSet.getString(2));
                            record.F05 = resultSet.getTimestamp(3);
                            record.F06 = resultSet.getInt(4);
                            record.F07 = resultSet.getTimestamp(5);
                            record.F08 = resultSet.getTimestamp(6);
                            record.type = resultSet.getString(7);
                            record.value = resultSet.getBigDecimal(8);
                            record.useRule = resultSet.getBigDecimal(9).compareTo(BigDecimal.ZERO) > 0 ? 1 : 0;
                            record.investUseRule = resultSet.getBigDecimal(9);
                            record.quota = resultSet.getBigDecimal(10);
                            list.add(record);
                        }
                    }
                }
            }
        }
        catch (SQLException sqlException)
        {
            logger.error("MyRewardManageImpl.getMyRewardRecodList() SQLException error:", sqlException);
        }
        catch (Throwable throwable)
        {
            logger.error("MyRewardManageImpl.getMyRewardRecodList() Throwable error:", throwable);
        }
        return list;
    }
    
    @Override
    public T6342 getT6342(int bidId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6342 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S63.T6342 WHERE T6342.F02 = ? AND T6342.F06 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setInt(2, bidId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6342();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = T6342_F04.parse(resultSet.getString(4));
                        record.F05 = resultSet.getTimestamp(5);
                        record.F06 = resultSet.getInt(6);
                        record.F07 = resultSet.getTimestamp(7);
                        record.F08 = resultSet.getTimestamp(8);
                    }
                }
            }
            return record;
        }
    }

    /**
     * 查询用户活动表
     *
     * @param bidId
     * @param rewardType
     * @return
     * @throws Throwable
     */
    @Override
    public boolean IsUsedReward(int bidId, String rewardType) throws Throwable {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                         connection.prepareStatement("SELECT COUNT(1) FROM S63.T6342 LEFT JOIN S63.T6344 ON T6344.F01 = T6342.F03 LEFT JOIN S63.T6340 ON T6340.F01 = T6344.F02 WHERE T6342.F02 = ? AND T6342.F06 = ? AND T6340.F03 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setInt(2, bidId);
                pstmt.setString(3,rewardType);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getInt(1) > 0;
                    }
                }
            }
            return false;
        }
    }

    /**
     * 统计加息券金额
     * 
     * @return
     * @throws Throwable
     */
    @Override
    public MyRewardRecod getJxqAmount()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            MyRewardRecod record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT ds.F01,ys.F02 FROM (SELECT IFNULL(SUM(T6289.F07),0) F01 "
                    + "FROM S63.T6342 JOIN S62.T6289 ON T6342.F01=T6289.F12 WHERE T6342.F02=? AND T6342.F04=? AND T6289.F09=?) ds,"
                    + "(SELECT IFNULL(SUM(T6289.F07),0) F02 FROM S63.T6342 JOIN S62.T6289 ON T6342.F01=T6289.F12 WHERE T6342.F02=? AND T6342.F04=? AND T6289.F09=?) ys"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, T6342_F04.YSY.name());
                pstmt.setString(3, T6289_F09.WFH.name());
                pstmt.setInt(4, serviceResource.getSession().getAccountId());
                pstmt.setString(5, T6342_F04.YSY.name());
                pstmt.setString(6, T6289_F09.YFH.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new MyRewardRecod();
                        record.dsJxjlAmount = resultSet.getBigDecimal(1);
                        record.ysJxjlAmount = resultSet.getBigDecimal(2);
                    }
                }
            }
            return record;
        }
    }
}
