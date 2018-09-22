package com.dimeng.p2p.modules.spread.console.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.modules.spread.console.service.SpreadManage;
import com.dimeng.p2p.modules.spread.console.service.entity.BonusList;
import com.dimeng.p2p.modules.spread.console.service.entity.BonusQuery;
import com.dimeng.p2p.modules.spread.console.service.entity.SeriesDetailList;
import com.dimeng.p2p.modules.spread.console.service.entity.SpreadDetailList;
import com.dimeng.p2p.modules.spread.console.service.entity.SpreadDetailQuery;
import com.dimeng.p2p.modules.spread.console.service.entity.SpreadList;
import com.dimeng.p2p.modules.spread.console.service.entity.SpreadQuery;
import com.dimeng.p2p.modules.spread.console.service.entity.SpreadTotalInfo;
import com.dimeng.p2p.modules.spread.console.service.entity.SpreadTotalList;
import com.dimeng.p2p.modules.spread.console.service.entity.SpreadTotalQuery;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.TimestampParser;

public class SpreadManageImp extends AbstractSpreadManage implements SpreadManage
{
    
    public static class SpreadManageFactory implements ServiceFactory<SpreadManage>
    {
        @Override
        public SpreadManage newInstance(ServiceResource serviceResource)
        {
            return new SpreadManageImp(serviceResource);
        }
        
    }
    
    public SpreadManageImp(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<SpreadTotalList> searchTotalList(SpreadTotalQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT S61.T6110.F01 AS F01,S61.T6110.F02 AS F02,S61.T6141.F02 AS F03,S63.T6310.F02 AS F04,S63.T6310.F03 AS F05,S63.T6310.F04 AS F06,S63.T6310.F05 AS F07 ");
        sql.append(" FROM S61.T6110 RIGHT JOIN S61.T6141 ON S61.T6110.F01 = S61.T6141.F01 RIGHT JOIN S63.T6310 ON S61.T6110.F01 = S63.T6310.F01 WHERE S63.T6310.F02 > 0 ");
        ArrayList<Object> parameters = new ArrayList<>();
        searchTotalParameter(query, sql, parameters);
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<SpreadTotalList>()
            {
                
                @Override
                public SpreadTotalList[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<SpreadTotalList> list = null;
                    while (resultSet.next())
                    {
                        SpreadTotalList info = new SpreadTotalList();
                        info.personID = resultSet.getInt(1);
                        info.userName = resultSet.getString(2);
                        info.name = resultSet.getString(3);
                        info.customNum = resultSet.getInt(4);
                        info.spreadMoney = resultSet.getBigDecimal(5);
                        info.seriesRewarMoney = resultSet.getBigDecimal(6);
                        info.validRewardMoney = resultSet.getBigDecimal(7);
                        info.rewardTotalMoney = resultSet.getBigDecimal(6).add(resultSet.getBigDecimal(7));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(info);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new SpreadTotalList[list.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public SpreadTotalList searchTotalListAmount(SpreadTotalQuery query)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder("SELECT IFNULL(SUM(S63.T6310.F04),0) AS F01,IFNULL(SUM(S63.T6310.F05),0) AS F02 ");
        sql.append(" FROM S61.T6110 RIGHT JOIN S61.T6141 ON S61.T6110.F01 = S61.T6141.F01 RIGHT JOIN S63.T6310 ON S61.T6110.F01 = S63.T6310.F01 WHERE S63.T6310.F02 > 0 ");
        List<Object> parameters = new ArrayList<Object>();
        searchTotalParameter(query, sql, parameters);
        // sql语句和查询参数处理
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<SpreadTotalList>()
            {
                @Override
                public SpreadTotalList parse(ResultSet resultSet)
                    throws SQLException
                {
                    SpreadTotalList count = new SpreadTotalList();
                    if (resultSet.next())
                    {
                        count.seriesRewarMoney = resultSet.getBigDecimal(1);
                        count.validRewardMoney = resultSet.getBigDecimal(2);
                        count.rewardTotalMoney = resultSet.getBigDecimal(1).add(resultSet.getBigDecimal(2));
                    }
                    return count;
                }
            }, sql.toString(), parameters);
        }
    }
    
    private void searchTotalParameter(SpreadTotalQuery query, StringBuilder sql, List<Object> parameters)
        throws SQLException
    {
        if (query != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            if (query.id() > 0)
            {
                sql.append(" AND S61.T6110.F01 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.id() + ""));
            }
            if (!StringHelper.isEmpty(query.userName()))
            {
                sql.append(" AND S61.T6110.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.userName()));
            }
            if (!StringHelper.isEmpty(query.name()))
            {
                sql.append(" AND S61.T6141.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.name()));
            }
        }
    }
    
    @Override
    public SpreadTotalInfo getSpreadTotal()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT COUNT(F01),IFNULL(SUM(F02),0),IFNULL(SUM(F03),0),IFNULL(SUM(F04),0),IFNULL(SUM(F05),0) FROM S63.T6310 WHERE F02 > 0"))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    SpreadTotalInfo info = null;
                    if (rs.next())
                    {
                        info = new SpreadTotalInfo();
                        info.personNum = rs.getInt(1);
                        info.spreadPersonNum = rs.getInt(2);
                        info.totalMoney = rs.getBigDecimal(3);
                        info.returnMoney = rs.getBigDecimal(4).add(rs.getBigDecimal(5));
                    }
                    return info;
                }
            }
        }
    }
    
    @Override
    public PagingResult<SeriesDetailList> searchSeriesList(int id, Paging paging)
        throws Throwable
    {
        
        String sql =
            "SELECT S61.T6110.F01 AS F01,S61.T6110.F02 AS F02,S61.T6141.F02 AS F03,S63.T6312.F04 AS F04,S63.T6312.F05 AS F05,S63.T6312.F06 AS F06 FROM S61.T6110 RIGHT JOIN S61.T6141 ON S61.T6110.F01 = S61.T6141.F01 RIGHT JOIN S63.T6312 ON S61.T6110.F01 = S63.T6312.F03 WHERE 1=1 AND S63.T6312.F02=? ORDER BY F06 DESC";
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<SeriesDetailList>()
            {
                ArrayList<SeriesDetailList> list = null;
                
                @Override
                public SeriesDetailList[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    while (resultSet.next())
                    {
                        SeriesDetailList info = new SeriesDetailList();
                        info.id = resultSet.getInt(1);
                        info.userName = resultSet.getString(2);
                        info.name = resultSet.getString(3);
                        info.money = resultSet.getBigDecimal(4);
                        info.rewarMoney = resultSet.getBigDecimal(5);
                        info.investTime = resultSet.getTimestamp(6);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(info);
                    }
                    
                    return list == null || list.size() == 0 ? null : list.toArray(new SeriesDetailList[list.size()]);
                }
                
            }, paging, sql, id);
        }
    }
    
    @Override
    public PagingResult<SpreadList> searchSpreadList(SpreadQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT t.tgrid,t.tgryhm,t.tgrxm,t.btgrid,t1.F02 AS btgryhm,t2.F02 AS btgrxm,t.tzje,t.jlje,t.tzsj FROM ");
        sql.append(" (SELECT S63.T6312.F02 AS tgrid ,S61.T6110.F02 AS tgryhm,S61.T6141.F02 AS tgrxm,S63.T6312.F03 AS btgrid,S63.T6312.F04 AS tzje,S63.T6312.F05 AS jlje,S63.T6312.F06 AS tzsj FROM");
        sql.append(" S63.T6312  LEFT JOIN S61.T6110 ON S63.T6312.F02 = S61.T6110.F01 LEFT JOIN S61.T6141 ON S63.T6312.F02 = S61.T6141.F01 WHERE 1=1");
        ArrayList<Object> parameters = new ArrayList<>();
        searchSpreadParams(query, sql, parameters);
        sql.append(" ORDER BY t.tzsj DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<SpreadList>()
            {
                ArrayList<SpreadList> list = null;
                
                @Override
                public SpreadList[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    while (resultSet.next())
                    {
                        SpreadList info = new SpreadList();
                        info.id = resultSet.getInt(1);
                        info.userName = resultSet.getString(2);
                        info.name = resultSet.getString(3);
                        info.personID = resultSet.getInt(4);
                        info.personUserName = resultSet.getString(5);
                        info.personName = resultSet.getString(6);
                        info.spreadTotalMoney = resultSet.getBigDecimal(7);
                        info.rewardMoney = resultSet.getBigDecimal(8);
                        info.investTime = resultSet.getTimestamp(9);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(info);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new SpreadList[list.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public SpreadList searchSpreadListAmount(SpreadQuery query)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder("SELECT IFNULL(SUM(t.tzje),0),IFNULL(SUM(t.jlje),0) FROM ");
        sql.append(" (SELECT S63.T6312.F02 AS tgrid ,S61.T6110.F02 AS tgryhm,S61.T6141.F02 AS tgrxm,S63.T6312.F03 AS btgrid,S63.T6312.F04 AS tzje,S63.T6312.F05 AS jlje,S63.T6312.F06 AS tzsj FROM");
        sql.append(" S63.T6312  LEFT JOIN S61.T6110 ON S63.T6312.F02 = S61.T6110.F01 LEFT JOIN S61.T6141 ON S63.T6312.F02 = S61.T6141.F01 WHERE 1=1");
        List<Object> parameters = new ArrayList<Object>();
        searchSpreadParams(query, sql, parameters);
        // sql语句和查询参数处理
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<SpreadList>()
            {
                @Override
                public SpreadList parse(ResultSet resultSet)
                    throws SQLException
                {
                    SpreadList count = new SpreadList();
                    if (resultSet.next())
                    {
                        count.spreadTotalMoney = resultSet.getBigDecimal(1);
                        count.rewardMoney = resultSet.getBigDecimal(2);
                    }
                    return count;
                }
            }, sql.toString(), parameters);
        }
    }
    
    private void searchSpreadParams(SpreadQuery query, StringBuilder sql, List<Object> parameters)
        throws SQLException
    {
        SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
        if (query != null)
        {
            if (query.id() > 0)
            {
                sql.append(" AND S63.T6312.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.id() + ""));
            }
            if (query.personID() > 0)
            {
                sql.append(" AND S63.T6312.F03 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.personID() + ""));
            }
            if (!StringHelper.isEmpty(query.userName()))
            {
                sql.append(" AND S61.T6110.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.userName()));
            }
            if (!StringHelper.isEmpty(query.name()))
            {
                sql.append(" AND S61.T6141.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.name()));
            }
            if (query.investStartTime() != null)
            {
                sql.append(" AND S63.T6312.F06 >= ?");
                parameters.add(query.investStartTime());
            }
            if (query.investEndTime() != null)
            {
                sql.append(" AND DATE(S63.T6312.F06) <= ?");
                parameters.add(query.investEndTime());
            }
        }
        sql.append(") t LEFT JOIN S61.T6110 AS t1 ON t.btgrid = t1.F01 LEFT JOIN S61.T6141 AS t2 ON t.btgrid = t2.F01 WHERE 1=1");
        if (query != null)
        {
            if (!StringHelper.isEmpty(query.personUserName()))
            {
                sql.append(" AND t1.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.personUserName()));
            }
            if (!StringHelper.isEmpty(query.personName()))
            {
                sql.append(" AND t2.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.personName()));
            }
        }
    }
    
    @Override
    public PagingResult<SpreadDetailList> searchSpreadDetailList(SpreadDetailQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT t.tgid,t.tgyhm,t.tgxm,t.btgid,S61.T6110.F02 AS btgyhm,S61.T6141.F02 AS btgxm,t.czje,t.czsj,t.jlje FROM ");
        sql.append(" (SELECT S63.T6311.F02 AS tgid,S61.T6110.F02 AS tgyhm,S61.T6141.F02 AS tgxm,S63.T6311.F03 AS btgid,S63.T6311.F04 AS czje,S63.T6311.F06 AS czsj,S63.T6311.F05 AS jlje ");
        sql.append(" FROM S63.T6311 LEFT JOIN S61.T6110 ON S63.T6311.F02 = S61.T6110.F01 LEFT JOIN S61.T6141 ON S63.T6311.F02 = S61.T6141.F01  WHERE 1=1 ");
        ArrayList<Object> parameters = new ArrayList<>();
        searchSpreadDetailParams(query, sql, parameters);
        sql.append(" ORDER BY t.czsj DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<SpreadDetailList>()
            {
                ArrayList<SpreadDetailList> list = null;
                
                @Override
                public SpreadDetailList[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    while (resultSet.next())
                    {
                        SpreadDetailList info = new SpreadDetailList();
                        info.id = resultSet.getInt(1);
                        info.userName = resultSet.getString(2);
                        info.name = resultSet.getString(3);
                        info.personID = resultSet.getInt(4);
                        info.personUserName = resultSet.getString(5);
                        info.personName = resultSet.getString(6);
                        info.firstMoney = resultSet.getBigDecimal(7);
                        info.firstTime = resultSet.getTimestamp(8);
                        info.spreadRewardMoney = resultSet.getBigDecimal(9);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(info);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new SpreadDetailList[list.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public BigDecimal searchSpreadDetailListAmount(SpreadDetailQuery query)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder("SELECT IFNULL(SUM(t.jlje),0) FROM ");
        sql.append(" (SELECT S63.T6311.F02 AS tgid,S61.T6110.F02 AS tgyhm,S61.T6141.F02 AS tgxm,S63.T6311.F03 AS btgid,S63.T6311.F04 AS czje,S63.T6311.F06 AS czsj,S63.T6311.F05 AS jlje ");
        sql.append(" FROM S63.T6311 LEFT JOIN S61.T6110 ON S63.T6311.F02 = S61.T6110.F01 LEFT JOIN S61.T6141 ON S63.T6311.F02 = S61.T6141.F01  WHERE 1=1 ");
        ArrayList<Object> parameters = new ArrayList<>();
        searchSpreadDetailParams(query, sql, parameters);
        try (Connection connection = getConnection())
        {
            return selectBigDecimal(connection, sql.toString(), parameters);
        }
    }
    
    private void searchSpreadDetailParams(SpreadDetailQuery query, StringBuilder sql, List<Object> parameters)
        throws SQLException
    {
        SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
        if (query != null)
        {
            if (query.id() > 0)
            {
                sql.append(" AND S63.T6311.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.id() + ""));
            }
            if (!StringHelper.isEmpty(query.userName()))
            {
                sql.append(" AND S61.T6110.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.userName()));
            }
            if (!StringHelper.isEmpty(query.name()))
            {
                sql.append(" AND S61.T6141.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.name()));
            }
            if (query.personID() > 0)
            {
                sql.append(" AND S63.T6311.F03 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.personID() + ""));
            }
            if (query.startTime() != null)
            {
                sql.append(" AND DATE(S63.T6311.F06) >= ?");
                parameters.add(query.startTime());
            }
            if (query.endTime() != null)
            {
                sql.append(" AND DATE(S63.T6311.F06) <= ?");
                parameters.add(query.endTime());
            }
        }
        sql.append(" ) t LEFT JOIN S61.T6110 ON t.btgid = S61.T6110.F01 LEFT JOIN S61.T6141 ON t.btgid = S61.T6141.F01  WHERE 1=1 ");
        if (query != null)
        {
            if (!StringHelper.isEmpty(query.personName()))
            {
                sql.append(" AND S61.T6110.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.personName()));
            }
            if (!StringHelper.isEmpty(query.personUserName()))
            {
                sql.append(" AND S61.T6141.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.personUserName()));
            }
        }
    }
    
    @Override
    public String getName(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT F02 FROM S61.T6141 WHERE F01 = ?"))
            {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery())
                {
                    String name = null;
                    if (rs.next())
                    {
                        name = rs.getString(1);
                    }
                    return name;
                }
            }
        }
    }
    
    @Override
    public SpreadList[] exportSpreadList(SpreadQuery query)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT t.tgrid,t.tgryhm,t.tgrxm,t.btgrid,t1.F02 AS btgryhm,t2.F02 AS btgrxm,t.tzje,t.jlje,t.tzsj FROM ");
        sql.append(" (SELECT S63.T6312.F02 AS tgrid ,S61.T6110.F02 AS tgryhm,S61.T6141.F02 AS tgrxm,S63.T6312.F03 AS btgrid,S63.T6312.F04 AS tzje,S63.T6312.F05 AS jlje,S63.T6312.F06 AS tzsj FROM");
        sql.append(" S63.T6312  LEFT JOIN S61.T6110 ON S63.T6312.F02 = S61.T6110.F01 LEFT JOIN S61.T6141 ON S63.T6312.F02 = S61.T6141.F01 WHERE 1=1");
        ArrayList<Object> parameters = new ArrayList<>();
        SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
        if (query != null)
        {
            if (query.id() > 0)
            {
                sql.append(" AND S63.T6312.F02=?");
                parameters.add(query.id());
            }
            if (query.personID() > 0)
            {
                sql.append(" AND S63.T6312.F03=?");
                parameters.add(query.personID());
            }
            if (!StringHelper.isEmpty(query.userName()))
            {
                sql.append(" AND S61.T6110.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.userName()));
            }
            if (!StringHelper.isEmpty(query.name()))
            {
                sql.append(" AND S61.T6141.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.name()));
            }
            if (query.investStartTime() != null)
            {
                sql.append(" AND S63.T6312.F06 >= ?");
                parameters.add(query.investStartTime());
            }
            if (query.investEndTime() != null)
            {
                sql.append(" AND DATE(S63.T6312.F06) <= ?");
                parameters.add(query.investEndTime());
            }
        }
        sql.append(") t LEFT JOIN S61.T6110 AS t1 ON t.btgrid = t1.F01 LEFT JOIN S61.T6141 AS t2 ON t.btgrid = t2.F01 WHERE 1=1");
        if (query != null)
        {
            if (!StringHelper.isEmpty(query.personUserName()))
            {
                sql.append(" AND t1.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.personUserName()));
            }
            if (!StringHelper.isEmpty(query.personName()))
            {
                sql.append(" AND t2.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.personName()));
            }
        }
        sql.append(" ORDER BY t.tzsj DESC");
        
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql.toString()))
            {
                int index = 1;
                for (Object obj : parameters)
                {
                    ps.setObject(index++, obj);
                }
                
                try (ResultSet resultSet = ps.executeQuery())
                {
                    ArrayList<SpreadList> list = null;
                    while (resultSet.next())
                    {
                        SpreadList info = new SpreadList();
                        info.id = resultSet.getInt(1);
                        info.userName = resultSet.getString(2);
                        info.name = resultSet.getString(3);
                        info.personID = resultSet.getInt(4);
                        info.personUserName = resultSet.getString(5);
                        info.personName = resultSet.getString(6);
                        info.spreadTotalMoney = resultSet.getBigDecimal(7);
                        info.rewardMoney = resultSet.getBigDecimal(8);
                        info.investTime = resultSet.getTimestamp(9);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(info);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new SpreadList[list.size()]);
                }
            }
        }
    }
    
    @Override
    public SpreadTotalList[] exportSpreadTotalLists(SpreadTotalQuery query)
        throws Throwable
    {
        
        StringBuilder sql =
            new StringBuilder(
                "SELECT S61.T6110.F01 AS F01,S61.T6110.F02 AS F02,S61.T6141.F02 AS F03,S63.T6310.F02 AS F04,S63.T6310.F03 AS F05,S63.T6310.F04 AS F06,S63.T6310.F05 AS F07 ");
        sql.append(" FROM S61.T6110 RIGHT JOIN S61.T6141 ON S61.T6110.F01 = S61.T6141.F01 RIGHT JOIN S63.T6310 ON S61.T6110.F01 = S63.T6310.F01 WHERE S63.T6310.F02 > 0 ");
        ArrayList<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            if (query.id() > 0)
            {
                sql.append(" AND S61.T6110.F01 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.id() + ""));
            }
            if (!StringHelper.isEmpty(query.userName()))
            {
                sql.append(" AND S61.T6110.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.userName()));
            }
            if (!StringHelper.isEmpty(query.name()))
            {
                sql.append(" AND S61.T6141.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.name()));
            }
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql.toString()))
            {
                int index = 1;
                for (Object obj : parameters)
                {
                    ps.setObject(index++, obj);
                }
                
                try (ResultSet resultSet = ps.executeQuery())
                {
                    ArrayList<SpreadTotalList> list = null;
                    while (resultSet.next())
                    {
                        SpreadTotalList info = new SpreadTotalList();
                        info.personID = resultSet.getInt(1);
                        info.userName = resultSet.getString(2);
                        info.name = resultSet.getString(3);
                        info.customNum = resultSet.getInt(4);
                        info.spreadMoney = resultSet.getBigDecimal(5);
                        info.seriesRewarMoney = resultSet.getBigDecimal(6);
                        info.validRewardMoney = resultSet.getBigDecimal(7);
                        info.rewardTotalMoney = resultSet.getBigDecimal(6).add(resultSet.getBigDecimal(7));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(info);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new SpreadTotalList[list.size()]);
                    
                }
            }
            
        }
    }
    
    @Override
    public SpreadDetailList[] exportsSpreadDetailLists(SpreadDetailQuery query)
        throws Throwable
    {
        
        StringBuilder sql =
            new StringBuilder(
                "SELECT t.tgid,t.tgyhm,t.tgxm,t.btgid,S61.T6110.F02 AS btgyhm,S61.T6141.F02 AS btgxm,t.czje,t.czsj,t.jlje FROM ");
        sql.append(" (SELECT S63.T6311.F02 AS tgid,S61.T6110.F02 AS tgyhm,S61.T6141.F02 AS tgxm,S63.T6311.F03 AS btgid,S63.T6311.F04 AS czje,S63.T6311.F06 AS czsj,S63.T6311.F05 AS jlje ");
        sql.append(" FROM S63.T6311 LEFT JOIN S61.T6110 ON S63.T6311.F02 = S61.T6110.F01 LEFT JOIN S61.T6141 ON S63.T6311.F02 = S61.T6141.F01  WHERE 1=1 ");
        SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
        ArrayList<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            if (query.id() > 0)
            {
                sql.append(" AND S63.T6311.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.id() + ""));
            }
            if (!StringHelper.isEmpty(query.userName()))
            {
                sql.append(" AND S61.T6110.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.userName()));
            }
            if (!StringHelper.isEmpty(query.name()))
            {
                sql.append(" AND S61.T6141.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.name()));
            }
            if (query.personID() > 0)
            {
                sql.append(" AND S63.T6311.F03 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.personID() + ""));
            }
            if (query.startTime() != null)
            {
                sql.append(" AND DATE(S63.T6311.F06) >= ?");
                parameters.add(query.startTime());
            }
            if (query.endTime() != null)
            {
                sql.append(" AND DATE(S63.T6311.F06) <= ?");
                parameters.add(query.endTime());
            }
        }
        sql.append(" ) t LEFT JOIN S61.T6110 ON t.btgid = S61.T6110.F01 LEFT JOIN S61.T6141 ON t.btgid = S61.T6141.F01  WHERE 1=1 ");
        if (query != null)
        {
            if (!StringHelper.isEmpty(query.personUserName()))
            {
                sql.append(" AND S61.T6110.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.personUserName()));
            }
            if (!StringHelper.isEmpty(query.personName()))
            {
                sql.append(" AND S61.T6141.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(query.personName()));
            }
        }
        sql.append(" ORDER BY t.czsj DESC");
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql.toString()))
            {
                int index = 1;
                for (Object obj : parameters)
                {
                    ps.setObject(index++, obj);
                }
                
                try (ResultSet resultSet = ps.executeQuery())
                {
                    ArrayList<SpreadDetailList> list = null;
                    while (resultSet.next())
                    {
                        SpreadDetailList info = new SpreadDetailList();
                        info.id = resultSet.getInt(1);
                        info.userName = resultSet.getString(2);
                        info.name = resultSet.getString(3);
                        info.personID = resultSet.getInt(4);
                        info.personUserName = resultSet.getString(5);
                        info.personName = resultSet.getString(6);
                        info.firstMoney = resultSet.getBigDecimal(7);
                        info.firstTime = resultSet.getTimestamp(8);
                        info.spreadRewardMoney = resultSet.getBigDecimal(9);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(info);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new SpreadDetailList[list.size()]);
                }
            }
            
        }
        
    }
    
    /**
     * 奖励金详情
     * @param query
     * @param paging
     * @return
     * @throws Throwable
     */
    @Override
    public PagingResult<BonusList> getBonusList(BonusQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "select t6230.F25 F01,t6110.F02 F02,t6230.F05 F03,t6230.F09 F04,t6230.F06 F05,t6231.F12 F06,s6110.F02 F07,t6505.F05 F08,t6231.F28 F09,t6505.F05*t6231.F28 F10,t6231.F21 F11, t6231.F22 F12 ");
        sql.append("from S65.T6505 t6505 left join S62.T6230 t6230 on t6505.F03 = t6230.F01 left join S62.T6231 t6231 on t6230.F01 = t6231.F01 ");
        sql.append("left join S61.T6110 t6110 on t6230.F02 = t6110.F01 left join S61.T6110 s6110 on t6505.F02 = s6110.F01 LEFT JOIN S65.T6501 st6501 ON t6505.F01 = st6501.F01 where t6231.F27 = 'S' AND st6501.F03='CG' ");
        ArrayList<Object> parameters = new ArrayList<>();
        getBonusListParams(query, sql, parameters);
        sql.append("order by t6231.F12 desc ");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<BonusList>()
            {
                ArrayList<BonusList> list = null;
                
                @Override
                public BonusList[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    while (resultSet.next())
                    {
                        BonusList info = new BonusList();
                        info.F25 = resultSet.getString(1);
                        info.loanUserName = resultSet.getString(2);
                        info.F05 = resultSet.getBigDecimal(3);
                        info.total = resultSet.getInt(4);
                        info.F06 = resultSet.getBigDecimal(5);
                        info.fkTime = resultSet.getTimestamp(6);
                        info.investUserName = resultSet.getString(7);
                        info.investAmount = resultSet.getBigDecimal(8);
                        info.jlRate = resultSet.getBigDecimal(9);
                        info.jlAmount = resultSet.getBigDecimal(10);
                        info.day = resultSet.getString(11);
                        if (T6231_F21.S.name().equalsIgnoreCase(info.day))
                        {
                            info.total = resultSet.getInt(12);
                        }
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(info);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new BonusList[list.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public BonusList getBonusListAmount(BonusQuery query)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder("select IFNULL(SUM(t6505.F05),0) AS F01,IFNULL(SUM(t6505.F05*t6231.F28),0) AS F02 ");
        sql.append("from S65.T6505 t6505 left join S62.T6230 t6230 on t6505.F03 = t6230.F01 left join S62.T6231 t6231 on t6230.F01 = t6231.F01 ");
        sql.append("left join S61.T6110 t6110 on t6230.F02 = t6110.F01 left join S61.T6110 s6110 on t6505.F02 = s6110.F01 LEFT JOIN S65.T6501 st6501 ON t6505.F01 = st6501.F01 WHERE t6231.F27 = 'S' AND st6501.F03='CG' ");
        List<Object> parameters = new ArrayList<Object>();
        getBonusListParams(query, sql, parameters);
        // sql语句和查询参数处理
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<BonusList>()
            {
                @Override
                public BonusList parse(ResultSet resultSet)
                    throws SQLException
                {
                    BonusList count = new BonusList();
                    if (resultSet.next())
                    {
                        count.investAmount = resultSet.getBigDecimal(1);
                        count.jlAmount = resultSet.getBigDecimal(2);
                    }
                    return count;
                }
            }, sql.toString(), parameters);
        }
    }
    
    private void getBonusListParams(BonusQuery query, StringBuilder sql, List<Object> parameters)
        throws SQLException
    {
        SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
        if (query != null)
        {
            if (!StringHelper.isEmpty(query.loanID()))
            {
                sql.append("and t6230.F25 like ? ");
                parameters.add(sqlConnectionProvider.allMatch(query.loanID()));
            }
            if (!StringHelper.isEmpty(query.loanUserName()))
            {
                sql.append("and t6110.F02 like ? ");
                parameters.add(sqlConnectionProvider.allMatch(query.loanUserName()));
            }
            if (!StringHelper.isEmpty(query.investUserName()))
            {
                sql.append("and s6110.F02 like ? ");
                parameters.add(sqlConnectionProvider.allMatch(query.investUserName()));
            }
            if (query.fkStartTime() != null)
            {
                sql.append("and t6231.F12 >= ? ");
                parameters.add(query.fkStartTime());
            }
            if (query.fkEndTime() != null)
            {
                sql.append("and t6231.F12 <= ? ");
                parameters.add(query.fkEndTime());
            }
        }
    }
    
    /**
     *投资奖励金额总计
     * @return
     * @throws Throwable
     */
    @Override
    public BigDecimal getBonusSum()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("select IFNULL(sum(t6505.F05*t6231.F28),0) from S65.T6505 t6505 left join S62.T6231 t6231 on t6505.F03 = t6231.F01 where t6231.F27 = 'S' "))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    BigDecimal bonusSum = null;
                    if (rs.next())
                    {
                        bonusSum = rs.getBigDecimal(1);
                    }
                    return bonusSum;
                }
            }
        }
    }
    
    /**
     * 奖励金导出
     *
     * @param outputStream
     * @throws Throwable
     */
    @Override
    public void export(BonusList[] bonusLists, OutputStream outputStream)
        throws Throwable
    {
        if (bonusLists == null)
        {
            return;
        }
        if (outputStream == null)
        {
            return;
        }
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, "GBK")))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("借款ID");
            writer.write("借款账户");
            writer.write("借款金额");
            writer.write("期限");
            writer.write("年化利率");
            writer.write("放款时间");
            writer.write("投资账户");
            writer.write("投资金额(元)");
            writer.write("奖励利率");
            writer.write("奖励金额（元）");
            writer.newLine();
            int i = 1;
            for (BonusList bonusList : bonusLists)
            {
                writer.write(i++);
                writer.write(bonusList.F25);
                writer.write(bonusList.loanUserName);
                writer.write(Formater.formatAmount(bonusList.F05) + "\t");
                if (T6231_F21.F.name().equalsIgnoreCase(bonusList.day))
                {
                    writer.write(bonusList.total + "个月");
                }
                else
                {
                    writer.write(bonusList.total + "天");
                }
                writer.write(Formater.formatRate(bonusList.F06));
                writer.write(TimestampParser.format(bonusList.fkTime, "yyyy-MM-dd HH:mm:ss") + "\t");
                writer.write(bonusList.investUserName);
                writer.write(Formater.formatAmount(bonusList.investAmount) + "\t");
                writer.write(Formater.formatRate(bonusList.jlRate));
                writer.write(Formater.formatAmount(bonusList.jlAmount) + "\t");
                writer.newLine();
            }
        }
    }
    
}
