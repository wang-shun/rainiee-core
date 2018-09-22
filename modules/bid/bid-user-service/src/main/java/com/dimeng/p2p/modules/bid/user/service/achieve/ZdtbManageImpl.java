package com.dimeng.p2p.modules.bid.user.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.p2p.common.enums.AutoSetStatus;
import com.dimeng.p2p.modules.bid.user.service.ZdtbManage;
import com.dimeng.p2p.modules.bid.user.service.entity.AutoBidSet;
import com.dimeng.p2p.modules.bid.user.service.query.AutoBidQuery;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.EnumParser;

public class ZdtbManageImpl extends AbstractBidManage implements ZdtbManage
{
    
    public ZdtbManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public void save(AutoBidQuery autoBidSet)
        throws Throwable
    {
        if (autoBidSet == null)
        {
            throw new IllegalArgumentException("插入内容不合法，请检查插入内容是否正确！");
        }
        
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        
        try (Connection conn = getConnection())
        {
            AutoSetStatus status = null;
            if ("huifu".equals(configureProvider.getProperty(SystemVariable.ESCROW_PREFIX)))
            {
                status = AutoSetStatus.TY;
            }
            else
            {
                status = AutoSetStatus.QY;
            }
            execute(conn,
                
                "INSERT INTO S62.T6280 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F12 = ?, F13 = ? ",
                autoBidSet.getTimeMoney(),
                autoBidSet.getRateStart(),
                autoBidSet.getRateEnd(),
                autoBidSet.getJkqxStart(),
                autoBidSet.getJkqxEnd(),
                autoBidSet.getLevelStart(),
                autoBidSet.getLevelEnd(),
                autoBidSet.getSaveMoney(),
                status,
                serviceResource.getSession().getAccountId(),
                autoBidSet.mctbje());
        }
        
    }
    
    @Override
    public int autoBidCount()
        throws Throwable
    {
        try (Connection conn = getConnection())
        {
            int count = select(conn, new ItemParser<Integer>()
            {
                
                @Override
                public Integer parse(ResultSet rs)
                    throws SQLException
                {
                    if (rs.next())
                    {
                        return rs.getInt(1);
                    }
                    return 0;
                }
            }, "SELECT COUNT(1) FROM S62.T6280 WHERE F12 = ?", serviceResource.getSession().getAccountId());
            return count;
        }
    }
    
    @Override
    public int autoBidCountQY(Connection conn)
        throws Throwable
    {
        int count = 0;
        if (conn == null)
        {
            try (Connection conn2 = getConnection())
            {
                count =
                    select(conn2, new ItemParser<Integer>()
                    {
                        
                        @Override
                        public Integer parse(ResultSet rs)
                            throws SQLException
                        {
                            if (rs.next())
                            {
                                return rs.getInt(1);
                            }
                            return 0;
                        }
                    }, "SELECT COUNT(1) FROM S62.T6280 WHERE F12 = ? AND F10 = 'QY'", serviceResource.getSession()
                        .getAccountId());
                return count;
            }
        }
        else
        {
            count =
                select(conn, new ItemParser<Integer>()
                {
                    
                    @Override
                    public Integer parse(ResultSet rs)
                        throws SQLException
                    {
                        if (rs.next())
                        {
                            return rs.getInt(1);
                        }
                        return 0;
                    }
                }, "SELECT COUNT(1) FROM S62.T6280 WHERE F12 = ? AND F10 = 'QY'", serviceResource.getSession()
                    .getAccountId());
            return count;
        }
    }
    
    @Override
    public int onlySave(AutoBidQuery autoBidSet)
        throws Throwable
    {
        if (autoBidSet == null)
        {
            throw new IllegalArgumentException("插入内容不合法，请检查插入内容是否正确！");
        }
        int accountId = serviceResource.getSession().getAccountId();
        try (Connection conn = getConnection())
        {
            execute(conn,
                "INSERT INTO S62.T6280 (F01,F02, F03, F04, F05, F06, F07, F08, F09, F10) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE F02 = VALUES(F02),F03 = VALUES(F03),F04 = VALUES(F04),F05 = VALUES(F05),F06 = VALUES(F06),F07 = VALUES(F07),F08 = VALUES(F08),F09 = VALUES(F09),F10 = VALUES(F10)",
                accountId,
                autoBidSet.getTimeMoney(),
                autoBidSet.getRateStart(),
                autoBidSet.getRateEnd(),
                autoBidSet.getJkqxStart(),
                autoBidSet.getJkqxEnd(),
                autoBidSet.getLevelStart(),
                autoBidSet.getLevelEnd(),
                autoBidSet.getSaveMoney(),
                AutoSetStatus.TY);
        }
        return accountId;
        
    }
    
    @Override
    public void updateStatus(String status, int id)
        throws Throwable
    {
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps =
                conn.prepareStatement("UPDATE S62.T6280 SET  F10 = ? WHERE F01 = ? AND F12 = ?"))
            {
                ps.setString(1, status);
                ps.setInt(2, id);
                ps.setInt(3, serviceResource.getSession().getAccountId());
                ps.execute();
            }
        }
        
    }
    
    @Override
    public List<AutoBidSet> search()
        throws Throwable
    {
        String sql =
            "SELECT F02, F03, F04, F05, F06, F07, F08, F09, F10, F01, F11, F01, F13  FROM S62.T6280 WHERE F12 = ?";
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<List<AutoBidSet>>()
            {
                @Override
                public List<AutoBidSet> parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<AutoBidSet> list = null;
                    while (resultSet.next())
                    {
                        if (list == null)
                        {
                            list = new ArrayList<AutoBidSet>();
                        }
                        AutoBidSet info = new AutoBidSet();
                        info.timeMoney = resultSet.getBigDecimal(1);
                        info.rateStart = resultSet.getBigDecimal(2);
                        info.rateEnd = resultSet.getBigDecimal(3);
                        info.jkqxStart = resultSet.getInt(4);
                        info.jkqxEnd = resultSet.getInt(5);
                        info.saveMoney = resultSet.getBigDecimal(8);
                        info.autoSetStatus = EnumParser.parse(AutoSetStatus.class, resultSet.getString(9));
                        info.loginId = resultSet.getInt(10);
                        info.levelStart = resultSet.getInt(6);
                        info.levelEnd = resultSet.getInt(7);
                        info.setTime = resultSet.getTimestamp(11);
                        info.id = resultSet.getInt(12);
                        info.all = resultSet.getInt(13);
                        
                        list.add(info);
                    }
                    
                    return list;
                }
            }, sql, serviceResource.getSession().getAccountId());
        }
    }
    
    @Override
    public int queryExist(int start, int end)
        throws Throwable
    {
        try (Connection connection = getConnection();
            PreparedStatement ps =
                connection.prepareStatement("SELECT COUNT(1) FROM S62.T6280 WHERE ((F05 <= ? AND F06 >= ?) OR (F05 <= ? AND F06 >=?) OR (F05 >= ? AND ? >= F06)) AND F12 = ?"))
        {
            ps.setInt(1, start);
            ps.setInt(2, start);
            ps.setInt(3, end);
            ps.setInt(4, end);
            ps.setInt(5, start);
            ps.setInt(6, end);
            ps.setInt(7, serviceResource.getSession().getAccountId());
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
    
    @Override
    public void delete(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            execute(connection, "DELETE FROM S62.T6280 WHERE F01 = ? AND F12 = ?", id, serviceResource.getSession()
                .getAccountId());
        }
    }
    
}
