/*
 * 文 件 名:  InterestRateManageImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年10月8日
 */
package com.dimeng.p2p.modules.activity.console.service.achieve;

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
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6103_F06;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.S63.enums.T6342_F04;
import com.dimeng.p2p.modules.activity.console.service.ActivityCountManage;
import com.dimeng.p2p.modules.activity.console.service.entity.ActivityCountEntity;
import com.dimeng.p2p.modules.activity.console.service.entity.HbAmountTotalInfo;
import com.dimeng.p2p.modules.activity.console.service.entity.JxqClearAmountTotalInfo;
import com.dimeng.p2p.modules.activity.console.service.entity.JxqClearCountEntity;
import com.dimeng.p2p.modules.activity.console.service.entity.JxqCountTotalInfo;
import com.dimeng.p2p.modules.activity.console.service.query.ActivityCountQuery;
import com.dimeng.p2p.modules.activity.console.service.query.JxqClearCountQuery;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;

/**
 * 红包统计，加息券统计，加息券结算统计 功能实现类
 * 
 * @author xiaoqi
 * @version [版本号, 2015年10月8日]
 */
public class ActivityCountManageImpl extends AbstractActivityService implements ActivityCountManage
{
    
    /**
     * <默认构造函数>
     */
    public ActivityCountManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    public static class InterestRateManageFactory implements ServiceFactory<ActivityCountManage>
    {
        @Override
        public ActivityCountManage newInstance(ServiceResource serviceResource)
        {
            return new ActivityCountManageImpl(serviceResource);
        }
        
    }
    
    @Override
    public PagingResult<ActivityCountEntity> searchActivityCount(ActivityCountQuery query, Paging paging,
        String activityType)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S61.T6110.F02 AS F01, S61.T6141.F02 AS F02, S63.T6344.F05 AS F03, S63.T6342.F07 AS F04, S63.T6342.F08 AS F05, ");
        sql.append("S63.T6340.F02 AS F06, S63.T6340.F05 AS F07, S63.T6342.F04 AS F08, S63.T6342.F05 AS F09, S62.T6230.F25 AS F10 FROM S61.T6110 ");
        sql.append("INNER JOIN S61.T6141 ON S61.T6110.F01 = S61.T6141.F01 INNER JOIN S63.T6342 ON S61.T6110.F01 = S63.T6342.F02 ");
        sql.append("INNER JOIN S63.T6344 ON S63.T6344.F01 = S63.T6342.F03 INNER JOIN S63.T6340 ON S63.T6340.F01 = S63.T6344.F02 ");
        sql.append("LEFT JOIN S62.T6230 ON S62.T6230.F01 = S63.T6342.F06 WHERE S63.T6340.F03 = ? ");
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(activityType);
        sqlAndParametersProcess(query, sql, parameters);
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<ActivityCountEntity>()
            {
                @Override
                public ActivityCountEntity[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<ActivityCountEntity> list = null;
                    while (rs.next())
                    {
                        ActivityCountEntity ace = new ActivityCountEntity();
                        ace.userName = rs.getString(1);
                        ace.realName = rs.getString(2);
                        ace.interestRate = rs.getBigDecimal(3);
                        ace.presentDate = rs.getTimestamp(4);
                        ace.outOfDate = rs.getTimestamp(5);
                        ace.activityNum = rs.getString(6);
                        ace.activityName = rs.getString(7);
                        ace.status = T6342_F04.valueOf(rs.getString(8));
                        ace.useDate = rs.getTimestamp(9);
                        ace.loanNum = rs.getString(10);
                        if (list == null)
                        {
                            list = new ArrayList<ActivityCountEntity>();
                        }
                        list.add(ace);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new ActivityCountEntity[list.size()]);
                }
            },
                paging,
                sql.toString(),
                parameters);
        }
    }
    
    @Override
    public PagingResult<ActivityCountEntity> searchActivityExpCount(ActivityCountQuery query, Paging paging,
        String activityType)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S61.T6110.F02 AS F01, S61.T6141.F02 AS F02, S61.T6103.F03 AS F03,S61.T6103.F04 AS F04,");
        sql.append("S61.T6103.F05 AS F05, S63.T6340.F02 AS F06,S63.T6340.F05 AS F07, ");
        sql.append("S61.T6103.F06 AS F08, S61.T6103.F15 AS F09,S62.T6230.F25 AS F10,S63.T6340.F04 AS F11, ");
        sql.append("S62.T6230.F09 AS F12, S62.T6231.F21 AS F13, S62.T6231.F22 AS F14, S61.T6103.F07 AS F15, S61.T6103.F16 AS F16, S63.T6344.F10 AS F17, S63.T6344.F11 AS F18 ");
        sql.append("FROM S61.T6110 INNER JOIN S61.T6141 ON S61.T6110.F01 = S61.T6141.F01 INNER JOIN S61.T6103 ON S61.T6110.F01 = S61.T6103.F02 ");
        sql.append("INNER JOIN S63.T6344 ON S63.T6344.F01 = S61.T6103.F14 ");
        sql.append("INNER JOIN S63.T6340 ON S63.T6340.F01 = S63.T6344.F02 ");
        sql.append("LEFT JOIN S62.T6230 ON S62.T6230.F01 = S61.T6103.F13 LEFT JOIN S62.T6231 ON S62.T6231.F01 = S61.T6103.F13 WHERE S63.T6340.F03 = ? ");
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(activityType);
        sqlAndParametersExpProcess(query, sql, parameters);
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<ActivityCountEntity>()
            {
                @Override
                public ActivityCountEntity[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<ActivityCountEntity> list = null;
                    while (rs.next())
                    {
                        ActivityCountEntity ace = new ActivityCountEntity();
                        ace.userName = rs.getString(1);
                        ace.realName = rs.getString(2);
                        ace.interestRate = rs.getBigDecimal(3);
                        ace.presentDate = rs.getTimestamp(4);
                        ace.outOfDate = rs.getTimestamp(5);
                        ace.activityNum = rs.getString(6);
                        ace.activityName = rs.getString(7);
                        ace.expStatus = T6103_F06.valueOf(rs.getString(8));
                        ace.useDate = rs.getTimestamp(9);
                        ace.loanNum = rs.getString(10);
                        ace.activityType = T6340_F04.valueOf(rs.getString(11));
                        ace.expectedTerm = "true".equals(rs.getString(18)) ? rs.getInt(17) + "个月" : rs.getInt(17) + "天";
                        getExpectedTerm(ace, rs);
                        
                        if (list == null)
                        {
                            list = new ArrayList<ActivityCountEntity>();
                        }
                        list.add(ace);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new ActivityCountEntity[list.size()]);
                }
            },
                paging,
                sql.toString(),
                parameters);
        }
    }
    
    /**
     * 取体验金预计收益期
     * @param ace
     * @param rs
     */
    private void getExpectedTerm(ActivityCountEntity ace, ResultSet rs)
        throws SQLException
    {
        int t6230_F09 = rs.getInt(12);
        T6231_F21 dayOrMonth = T6231_F21.parse(rs.getString(13));
        int t6103_F07 = rs.getInt(15);
        int t6231_F22 = rs.getInt(14);
        String t6103_F16 = rs.getString(16);
        if ("true".equals(t6103_F16))
        {
            ace.actTerm = t6103_F07 + "个月";
        }
        else
        {
            ace.actTerm = t6103_F07 + "天";
        }
        if (T6103_F06.YWT.name().equals(ace.expStatus.name()))
        {
            if (T6231_F21.S == dayOrMonth)
            {
                if (!BooleanParser.parse(t6103_F16))
                {
                    ace.actTerm = t6231_F22 > t6103_F07 ? t6103_F07 + "天" : t6231_F22 + "天";
                }
                else
                {
                    ace.actTerm = t6231_F22 > t6103_F07 * 30 ? t6103_F07 * 30 + "天" : t6231_F22 + "天";
                }
            }
            else
            {
                if (!BooleanParser.parse(t6103_F16))
                {
                    ace.actTerm = t6230_F09 * 30 > t6103_F07 ? t6103_F07 + "天" : t6230_F09 * 30 + "天";
                }
                else
                {
                    ace.actTerm = t6230_F09 > t6103_F07 ? t6103_F07 + "个月" : t6230_F09 + "个月";
                }
            }
        }
    }
    
    @Override
    public JxqCountTotalInfo getJxqCountTotalInfo()
        throws Throwable
    {
        try (Connection connection = getConnection();
            PreparedStatement ps =
                connection.prepareStatement("SELECT ( SELECT COUNT(1) FROM S63.T6342 INNER JOIN S63.T6344 ON T6342.F03 = T6344.F01 INNER JOIN S63.T6340 ON T6340.F01 = T6344.F02 WHERE S63.T6340.F03 = 'interest' AND S63.T6342.F04 = 'WSY' ) AS F01, ( SELECT COUNT(1) FROM S63.T6342 INNER JOIN S63.T6344 ON T6342.F03 = T6344.F01 INNER JOIN S63.T6340 ON T6340.F01 = T6344.F02 WHERE S63.T6340.F03 = 'interest' AND S63.T6342.F04 = 'YSY' ) AS F02"))
        {
            try (ResultSet rs = ps.executeQuery())
            {
                JxqCountTotalInfo info = null;
                if (rs.next())
                {
                    info = new JxqCountTotalInfo();
                    info.unUsedCount = rs.getInt(1);
                    info.usedCount = rs.getInt(2);
                }
                return info;
            }
        }
    }
    
    @Override
    public ActivityCountEntity[] exportActivityCountList(ActivityCountQuery query, String activityType)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S61.T6110.F02 AS F01, S61.T6141.F02 AS F02, S63.T6344.F05 AS F03, S63.T6342.F07 AS F04, S63.T6342.F08 AS F05, ");
        sql.append(" S63.T6340.F02 AS F06, S63.T6340.F05 AS F07, S63.T6342.F04 AS F08, S63.T6342.F05 AS F09, S62.T6230.F25 AS F10 FROM S61.T6110 ");
        sql.append(" INNER JOIN S61.T6141 ON S61.T6110.F01 = S61.T6141.F01 INNER JOIN S63.T6342 ON S61.T6110.F01 = S63.T6342.F02 INNER JOIN S63.T6344 ");
        sql.append(" ON S63.T6344.F01 = S63.T6342.F03 INNER JOIN S63.T6340 ON S63.T6340.F01 = S63.T6344.F02 LEFT JOIN S62.T6230 ON S62.T6230.F01 = S63.T6342.F06 WHERE S63.T6340.F03 = ? ");
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(activityType);
        sqlAndParametersProcess(query, sql, parameters);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql.toString()))
            {
                int index = 1;
                for (Object obj : parameters)
                {
                    ps.setObject(index++, obj);
                }
                try (ResultSet rs = ps.executeQuery())
                {
                    ArrayList<ActivityCountEntity> list = null;
                    while (rs.next())
                    {
                        ActivityCountEntity ace = new ActivityCountEntity();
                        ace.userName = rs.getString(1);
                        ace.realName = rs.getString(2);
                        ace.interestRate = rs.getBigDecimal(3);
                        ace.presentDate = rs.getTimestamp(4);
                        ace.outOfDate = rs.getTimestamp(5);
                        ace.activityNum = rs.getString(6);
                        ace.activityName = rs.getString(7);
                        ace.status = T6342_F04.valueOf(rs.getString(8));
                        ace.useDate = rs.getTimestamp(9);
                        ace.loanNum = rs.getString(10);
                        if (list == null)
                        {
                            list = new ArrayList<ActivityCountEntity>();
                        }
                        list.add(ace);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new ActivityCountEntity[list.size()]);
                }
            }
        }
    }
    
    @Override
    public ActivityCountEntity[] exportActivityExpCountList(ActivityCountQuery query, String activityType)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT S61.T6110.F02 AS F01, S61.T6141.F02 AS F02, S61.T6103.F03 AS F03,S61.T6103.F04 AS F04,");
        sql.append("S61.T6103.F05 AS F05, S63.T6340.F02 AS F06,S63.T6340.F05 AS F07,");
        sql.append("S61.T6103.F06 AS F08, S61.T6103.F15 AS F09,S62.T6230.F25 AS F10,S63.T6340.F04 AS F11, T6344.F10 AS F12, T6344.F11 AS F13 ");
        sql.append("FROM S61.T6110 INNER JOIN S61.T6141 ON S61.T6110.F01 = S61.T6141.F01 INNER JOIN S61.T6103 ON S61.T6110.F01 = S61.T6103.F02 INNER JOIN S63.T6344 ON S63.T6344.F01 = S61.T6103.F14 ");
        sql.append("INNER JOIN S63.T6340 ON S63.T6340.F01 = S63.T6344.F02 LEFT JOIN S62.T6230 ON S62.T6230.F01 = S61.T6103.F13 WHERE S63.T6340.F03 = ? ");
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(activityType);
        sqlAndParametersExpProcess(query, sql, parameters);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql.toString()))
            {
                int index = 1;
                for (Object obj : parameters)
                {
                    ps.setObject(index++, obj);
                }
                try (ResultSet rs = ps.executeQuery())
                {
                    ArrayList<ActivityCountEntity> list = null;
                    while (rs.next())
                    {
                        ActivityCountEntity ace = new ActivityCountEntity();
                        ace.userName = rs.getString(1);
                        ace.realName = rs.getString(2);
                        ace.interestRate = rs.getBigDecimal(3);
                        ace.presentDate = rs.getTimestamp(4);
                        ace.outOfDate = rs.getTimestamp(5);
                        ace.activityNum = rs.getString(6);
                        ace.activityName = rs.getString(7);
                        ace.expStatus = T6103_F06.valueOf(rs.getString(8));
                        ace.useDate = rs.getTimestamp(9);
                        ace.loanNum = rs.getString(10);
                        ace.activityType = T6340_F04.valueOf(rs.getString(11));
                        ace.expectedTerm = "true".equals(rs.getString(13)) ? rs.getInt(12) + "个月" : rs.getInt(12) + "天";
                        if (list == null)
                        {
                            list = new ArrayList<ActivityCountEntity>();
                        }
                        list.add(ace);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new ActivityCountEntity[list.size()]);
                }
            }
        }
    }
    
    /**
     * <一句话功能简述> <功能详细描述>
     * 
     * @param query
     * @param sql
     * @param parameters
     * @return
     * @throws SQLException
     */
    private void sqlAndParametersProcess(ActivityCountQuery query, StringBuilder sql, List<Object> parameters)
        throws SQLException
    {
        SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
        if (query != null)
        {
            if (!StringHelper.isEmpty(query.userName()))
            {
                sql.append(" AND S61.T6110.F02 LIKE ? ");
                parameters.add(sqlConnectionProvider.allMatch(query.userName()));
            }
            if (!StringHelper.isEmpty(query.status()))
            {
                sql.append(" AND S63.T6342.F04 = ? ");
                parameters.add(query.status());
            }
            if (!StringHelper.isEmpty(query.activityNum()))
            {
                sql.append(" AND S63.T6340.F02 LIKE ? ");
                parameters.add(sqlConnectionProvider.allMatch(query.activityNum()));
            }
            if (!StringHelper.isEmpty(query.loanNum()))
            {
                sql.append(" AND S62.T6230.F25 LIKE ? ");
                parameters.add(sqlConnectionProvider.allMatch(query.loanNum()));
            }
            if (query.presentDateBegin() != null)
            {
                sql.append(" AND DATE(S63.T6342.F07) >= ? ");
                parameters.add(query.presentDateBegin());
            }
            if (query.presentDateEnd() != null)
            {
                sql.append(" AND DATE(S63.T6342.F07) <= ? ");
                parameters.add(query.presentDateEnd());
            }
            if (query.outOfDateBegin() != null)
            {
                sql.append(" AND S63.T6342.F08 >= ? ");
                parameters.add(query.outOfDateBegin());
            }
            if (query.outOfDateEnd() != null)
            {
                sql.append(" AND S63.T6342.F08 <= ? ");
                parameters.add(query.outOfDateEnd());
            }
            if (query.useDateBegin() != null)
            {
                sql.append(" AND DATE(S63.T6342.F05) >= ? ");
                parameters.add(query.useDateBegin());
            }
            if (query.useDateEnd() != null)
            {
                sql.append(" AND DATE(S63.T6342.F05) <= ? ");
                parameters.add(query.useDateEnd());
            }
        }
        sql.append(" ORDER BY S63.T6342.F07 DESC ");
    }
    
    /**
     * 体验金统计条件查询
     *
     * @param query
     * @param sql
     * @param parameters
     * @return
     * @throws SQLException
     */
    private void sqlAndParametersExpProcess(ActivityCountQuery query, StringBuilder sql, List<Object> parameters)
        throws SQLException
    {
        SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
        if (query != null)
        {
            if (!StringHelper.isEmpty(query.userName()))
            {
                sql.append(" AND S61.T6110.F02 LIKE ? ");
                parameters.add(sqlConnectionProvider.allMatch(query.userName()));
            }
            if (!StringHelper.isEmpty(query.status()))
            {
                sql.append(" AND S61.T6103.F06 = ? ");
                parameters.add(query.status());
            }
            if (!StringHelper.isEmpty(query.activityNum()))
            {
                sql.append(" AND S63.T6340.F02 LIKE ? ");
                parameters.add(sqlConnectionProvider.allMatch(query.activityNum()));
            }
            if (!StringHelper.isEmpty(query.loanNum()))
            {
                sql.append(" AND S62.T6230.F25 LIKE ? ");
                parameters.add(sqlConnectionProvider.allMatch(query.loanNum()));
            }
            if (query.presentDateBegin() != null)
            {
                sql.append(" AND DATE(S61.T6103.F04) >= ? ");
                parameters.add(query.presentDateBegin());
            }
            if (query.presentDateEnd() != null)
            {
                sql.append(" AND DATE(S61.T6103.F04) <= ? ");
                parameters.add(query.presentDateEnd());
            }
            if (query.outOfDateBegin() != null)
            {
                sql.append(" AND S61.T6103.F05 >= ? ");
                parameters.add(query.outOfDateBegin());
            }
            if (query.outOfDateEnd() != null)
            {
                sql.append(" AND S61.T6103.F05 <= ? ");
                parameters.add(query.outOfDateEnd());
            }
            if (query.useDateBegin() != null)
            {
                sql.append(" AND DATE(S61.T6103.F15) >= ? ");
                parameters.add(query.useDateBegin());
            }
            if (query.useDateEnd() != null)
            {
                sql.append(" AND DATE(S61.T6103.F15) <= ? ");
                parameters.add(query.useDateEnd());
            }
        }
        sql.append(" ORDER BY S61.T6103.F04 DESC ");
    }
    
    @Override
    public HbAmountTotalInfo getHbAmountTotalInfo()
        throws Throwable
    {
        try (Connection connection = getConnection();
            PreparedStatement ps =
                connection.prepareStatement("SELECT ( SELECT IFNULL(SUM(S63.T6344.F05), 0) FROM S63.T6342 INNER JOIN S63.T6344 ON T6342.F03 = T6344.F01 INNER JOIN S63.T6340 ON T6340.F01 = T6344.F02 WHERE S63.T6340.F03 = 'redpacket' AND S63.T6342.F04 = 'WSY' ) AS F01, ( SELECT IFNULL(SUM(S63.T6344.F05), 0) FROM S63.T6342 INNER JOIN S63.T6344 ON T6342.F03 = T6344.F01 INNER JOIN S63.T6340 ON T6340.F01 = T6344.F02 WHERE S63.T6340.F03 = 'redpacket' AND S63.T6342.F04 = 'YSY' ) AS F02"))
        {
            try (ResultSet rs = ps.executeQuery())
            {
                HbAmountTotalInfo info = null;
                if (rs.next())
                {
                    info = new HbAmountTotalInfo();
                    info.unUsedAmount = rs.getBigDecimal(1);
                    info.usedAmount = rs.getBigDecimal(2);
                }
                return info;
            }
        }
    }
    
    @Override
    public BigDecimal getConditionOfHbAmount(ActivityCountQuery query, String activityType)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT IFNULL(SUM(S63.T6344.F05), 0) AS F01 FROM S61.T6110 INNER JOIN S63.T6342 ON S61.T6110.F01 = S63.T6342.F02 INNER JOIN ");
        sql.append("S63.T6344 ON S63.T6344.F01 = S63.T6342.F03 INNER JOIN S63.T6340 ON S63.T6340.F01 = S63.T6344.F02 LEFT JOIN S62.T6230 ON S62.T6230.F01 = S63.T6342.F06 WHERE S63.T6340.F03 = ?");
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(activityType);
        sqlAndParametersProcess(query, sql, parameters);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql.toString()))
            {
                int index = 1;
                for (Object obj : parameters)
                {
                    ps.setObject(index++, obj);
                }
                try (ResultSet rs = ps.executeQuery())
                {
                    BigDecimal total = null;
                    if (rs.next())
                    {
                        total = rs.getBigDecimal(1);
                    }
                    return total;
                }
            }
        }
    }
    
    @Override
    public PagingResult<JxqClearCountEntity> searchJxqClearCount(JxqClearCountQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT S62.T6230.F25 AS F01, S61.T6110.F02 AS F02, S61.T6141.F02 AS F03, S63.T6344.F05 AS F04, S62.T6289.F07 AS F05, ");
        sql.append(" S62.T6289.F08 AS F06 FROM S62.T6230 INNER JOIN S62.T6289 ON S62.T6230.F01 = S62.T6289.F02 INNER JOIN S63.T6342 ON S63.T6342.F06 = S62.T6230.F01 ");
        sql.append(" INNER JOIN S61.T6110 ON S63.T6342.F02 = S61.T6110.F01 INNER JOIN S61.T6141 ON S61.T6110.F01 = S61.T6141.F01 INNER JOIN S63.T6344 ON S63.T6344.F01 = S63.T6342.F03 ");
        sql.append(" INNER JOIN S63.T6340 ON S63.T6344.F02 = S63.T6340.F01 WHERE S62.T6289.F04 = S61.T6110.F01 AND S63.T6340.F03 = 'interest' ");
        ArrayList<Object> parameters = new ArrayList<>();
        sqlAndParametersByJxqClear(query, sql, parameters);
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<JxqClearCountEntity>()
            {
                @Override
                public JxqClearCountEntity[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<JxqClearCountEntity> list = null;
                    while (rs.next())
                    {
                        JxqClearCountEntity jcce = new JxqClearCountEntity();
                        jcce.loanNum = rs.getString(1);
                        jcce.userName = rs.getString(2);
                        jcce.realName = rs.getString(3);
                        jcce.interestRate = rs.getBigDecimal(4);
                        jcce.rewardAmount = rs.getBigDecimal(5);
                        jcce.payTime = rs.getTimestamp(6);
                        if (list == null)
                        {
                            list = new ArrayList<JxqClearCountEntity>();
                        }
                        list.add(jcce);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new JxqClearCountEntity[list.size()]);
                }
            },
                paging,
                sql.toString(),
                parameters);
        }
    }
    
    @Override
    public JxqClearAmountTotalInfo getJxqClearAmountTotalInfo()
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT ( SELECT IFNULL(SUM(S62.T6289.F07), 0) FROM S62.T6230 INNER JOIN S62.T6289 ON S62.T6230.F01 = S62.T6289.F02 ");
        sql.append(" INNER JOIN S63.T6342 ON S63.T6342.F06 = S62.T6230.F01 INNER JOIN S61.T6110 ON S63.T6342.F02 = S61.T6110.F01 INNER JOIN S63.T6344 ON S63.T6344.F01 = S63.T6342.F03 ");
        sql.append(" INNER JOIN S63.T6340 ON S63.T6340.F01 = S63.T6344.F02 WHERE S62.T6289.F04 = S61.T6110.F01 AND S63.T6340.F03 = 'interest' AND S62.T6289.F09 = 'WFH' ) AS F01,");
        sql.append(" ( SELECT IFNULL(SUM(S62.T6289.F07), 0) FROM S62.T6230 INNER JOIN S62.T6289 ON S62.T6230.F01 = S62.T6289.F02 INNER JOIN S63.T6342 ON S63.T6342.F06 = S62.T6230.F01");
        sql.append(" INNER JOIN S61.T6110 ON S63.T6342.F02 = S61.T6110.F01 INNER JOIN S63.T6344 ON S63.T6344.F01 = S63.T6342.F03 INNER JOIN S63.T6340 ON S63.T6340.F01 = S63.T6344.F02 ");
        sql.append(" WHERE S62.T6289.F04 = S61.T6110.F01 AND S63.T6340.F03 = 'interest' AND S62.T6289.F09 = 'YFH' ) AS F02  ");
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql.toString()))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    JxqClearAmountTotalInfo jca = null;
                    if (rs.next())
                    {
                        jca = new JxqClearAmountTotalInfo();
                        jca.unPayAmount = rs.getBigDecimal(1);
                        jca.paidAmount = rs.getBigDecimal(2);
                    }
                    return jca;
                }
            }
        }
    }
    
    @Override
    public BigDecimal getConditionOfJxqClearAmount(JxqClearCountQuery query)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT IFNULL(SUM(S62.T6289.F07), 0) FROM S62.T6230 INNER JOIN S62.T6289 ON S62.T6230.F01 = S62.T6289.F02 ");
        sql.append(" INNER JOIN S63.T6342 ON S63.T6342.F06 = S62.T6230.F01 INNER JOIN S61.T6110 ON S63.T6342.F02 = S61.T6110.F01 ");
        sql.append(" INNER JOIN S63.T6344 ON S63.T6344.F01 = S63.T6342.F03 INNER JOIN S63.T6340 ON S63.T6344.F02 = S63.T6340.F01 ");
        sql.append(" WHERE S62.T6289.F04 = S61.T6110.F01 AND S63.T6340.F03 = 'interest'");
        ArrayList<Object> parameters = new ArrayList<Object>();
        sqlAndParametersByJxqClear(query, sql, parameters);
        try (Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql.toString()))
        {
            int index = 1;
            for (Object obj : parameters)
            {
                ps.setObject(index++, obj);
            }
            try (ResultSet rs = ps.executeQuery())
            {
                BigDecimal total = null;
                if (rs.next())
                {
                    total = rs.getBigDecimal(1);
                }
                return total;
            }
        }
    }
    
    @Override
    public JxqClearCountEntity[] exportJxqClearCountList(JxqClearCountQuery query)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder("SELECT S62.T6230.F25 AS F01, S61.T6110.F02 AS F02, S61.T6141.F02 AS F03, ");
        sql.append(" S63.T6344.F05 AS F04, S62.T6289.F07 AS F05, S62.T6289.F08 AS F06 FROM S62.T6230 INNER JOIN S62.T6289 ON ");
        sql.append(" S62.T6230.F01 = S62.T6289.F02 INNER JOIN S63.T6342 ON S63.T6342.F06 = S62.T6230.F01 INNER JOIN S61.T6110 ");
        sql.append(" ON S63.T6342.F02 = S61.T6110.F01 INNER JOIN S61.T6141 ON S61.T6110.F01 = S61.T6141.F01 INNER JOIN S63.T6344 ");
        sql.append(" ON S63.T6344.F01 = S63.T6342.F03 INNER JOIN S63.T6340 ON S63.T6344.F02 = S63.T6340.F01 WHERE S62.T6289.F04 = S61.T6110.F01 AND S63.T6340.F03 = 'interest'");
        ArrayList<Object> parameters = new ArrayList<>();
        sqlAndParametersByJxqClear(query, sql, parameters);
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql.toString()))
            {
                int index = 1;
                for (Object object : parameters)
                {
                    ps.setObject(index++, object);
                }
                try (ResultSet rs = ps.executeQuery())
                {
                    List<JxqClearCountEntity> list = null;
                    while (rs.next())
                    {
                        JxqClearCountEntity jcce = new JxqClearCountEntity();
                        jcce.loanNum = rs.getString(1);
                        jcce.userName = rs.getString(2);
                        jcce.realName = rs.getString(3);
                        jcce.interestRate = rs.getBigDecimal(4);
                        jcce.rewardAmount = rs.getBigDecimal(5);
                        jcce.payTime = rs.getTimestamp(6);
                        if (list == null)
                        {
                            list = new ArrayList<JxqClearCountEntity>();
                        }
                        list.add(jcce);
                    }
                    return (list == null || list.size() == 0) ? null
                        : list.toArray(new JxqClearCountEntity[list.size()]);
                }
            }
        }
    }
    
    private void sqlAndParametersByJxqClear(JxqClearCountQuery query, StringBuilder sql, List<Object> parameters)
        throws SQLException
    {
        SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
        if (query != null)
        {
            if ("YF".equals(query.status()))
            {
                sql.append(" AND S62.T6289.F09= ? ");
                parameters.add("YFH");
            }
            else
            {
                sql.append(" AND S62.T6289.F09= ? ");
                parameters.add("WFH");
            }
            if (!StringHelper.isEmpty(query.userName()))
            {
                sql.append(" AND S61.T6110.F02 LIKE ? ");
                parameters.add(sqlConnectionProvider.allMatch(query.userName()));
            }
            if (!StringHelper.isEmpty(query.loanNum()))
            {
                sql.append(" AND S62.T6230.F25 LIKE ? ");
                parameters.add(sqlConnectionProvider.allMatch(query.loanNum()));
            }
            if (query.startTime() != null)
            {
                sql.append(" AND S62.T6289.F08 >= ? ");
                parameters.add(query.startTime());
            }
            if (query.endTime() != null)
            {
                sql.append(" AND S62.T6289.F08 <= ? ");
                parameters.add(query.endTime());
            }
        }
        if (query != null && "YF".equals(query.status()))
        {
            sql.append(" ORDER BY S62.T6289.F08 DESC ");
        }
        else
        {
            sql.append(" ORDER BY S62.T6289.F08 ASC ");
        }
        
    }
    
}
