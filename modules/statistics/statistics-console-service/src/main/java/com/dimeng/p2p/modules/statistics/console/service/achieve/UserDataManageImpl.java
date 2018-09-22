package com.dimeng.p2p.modules.statistics.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S62.enums.T6250_F07;
import com.dimeng.p2p.S62.enums.T6250_F08;
import com.dimeng.p2p.S62.enums.T6250_F11;
import com.dimeng.p2p.S70.entities.T7052;
import com.dimeng.p2p.modules.statistics.console.service.UserDataManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.AgeDistributionEntity;
import com.dimeng.p2p.modules.statistics.console.service.entity.InvestmentLoanEntity;
import com.dimeng.p2p.modules.statistics.console.service.entity.UserMonthData;
import com.dimeng.p2p.modules.statistics.console.service.entity.UserQuarterData;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.IntegerParser;

public class UserDataManageImpl extends AbstractStatisticsService implements UserDataManage
{
    
    public UserDataManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public UserQuarterData[] getUserQuarterData(int year)
        throws Throwable
    {
        List<UserQuarterData> userQuarterDatas = new ArrayList<>();
        if (year > 0)
        {
            try (Connection connection = getConnection())
            {
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT F02,F03,F04,F05,F06,F08,F09 FROM S70.T7034 WHERE F01=? ORDER BY F02 DESC"))
                {
                    ps.setInt(1, year);
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        while (resultSet.next())
                        {
                            UserQuarterData quarterData = new UserQuarterData();
                            quarterData.quarter = resultSet.getInt(1);
                            quarterData.userCount = resultSet.getInt(2);
                            quarterData.newUserCount = resultSet.getInt(3);
                            quarterData.jkUserCount = resultSet.getInt(4);
                            quarterData.tbUserCount = resultSet.getInt(5);
                            quarterData.jkNewUserCount = resultSet.getInt(6);
                            quarterData.tbNewUserCount = resultSet.getInt(7);
                            userQuarterDatas.add(quarterData);
                        }
                    }
                }
            }
        }
        return userQuarterDatas.toArray(new UserQuarterData[userQuarterDatas.size()]);
    }
    
    @Override
    public UserMonthData[] getUserMonthData(int year)
        throws Throwable
    {
        List<UserMonthData> userMonthDatas = new ArrayList<>();
        if (year > 0)
        {
            try (Connection connection = getConnection())
            {
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT F02,F03,F04,F05,F06,F08,F09 FROM S70.T7035 WHERE F01=? ORDER BY F02 DESC"))
                {
                    ps.setInt(1, year);
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        while (resultSet.next())
                        {
                            UserMonthData monthData = new UserMonthData();
                            monthData.month = resultSet.getInt(1);
                            monthData.userCount = resultSet.getInt(2);
                            monthData.newUserCount = resultSet.getInt(3);
                            monthData.jkUserCount = resultSet.getInt(4);
                            monthData.tbUserCount = resultSet.getInt(5);
                            monthData.jkNewUserCount = resultSet.getInt(6);
                            monthData.tbNewUserCount = resultSet.getInt(7);
                            userMonthDatas.add(monthData);
                        }
                    }
                }
            }
        }
        return userMonthDatas.toArray(new UserMonthData[userMonthDatas.size()]);
    }
    
    @Override
    public int[] getStatisticedYears()
        throws Throwable
    {
        List<Integer> years = new ArrayList<>();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT DISTINCT(F01) FROM S70.T7035"))
            {
                try (ResultSet resultSet = ps.executeQuery())
                {
                    while (resultSet.next())
                    {
                        years.add(resultSet.getInt(1));
                    }
                }
            }
        }
        int[] ys = new int[years.size()];
        for (int i = 0; i < years.size(); i++)
        {
            ys[i] = years.get(i);
        }
        return ys;
    }
    
    @Override
    public UserQuarterData getAllUserData()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT IFNULL(SUM(F05),0),IFNULL(SUM(F06),0) FROM S70.T7034 "))
            {
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        UserQuarterData quarterData = new UserQuarterData();
                        quarterData.jkUserCount = resultSet.getInt(1);
                        quarterData.tbUserCount = resultSet.getInt(2);
                        return quarterData;
                    }
                }
            }
        }
        return new UserQuarterData();
    }
    
    @Override
    public InvestmentLoanEntity getInvestmentLoanData()
        throws Throwable
    {
        StringBuffer sql =
            new StringBuffer(
                "SELECT (SELECT COUNT(1) FROM S61.T6110 WHERE EXISTS (SELECT 1 FROM S62.T6230 WHERE T6230.F02 = T6110.F01 AND T6230.F20 IN ('HKZ','YJQ','YDF','YZR'))) as loan,");
        sql.append("(SELECT COUNT(DISTINCT F03) FROM S62.T6250 WHERE F07 = 'F' AND F08 = 'S') as invest,");
        sql.append("(SELECT COUNT(1) FROM S61.T6110 WHERE NOT EXISTS (SELECT 1 FROM S62.T6230 WHERE T6230.F02 = T6110.F01 AND T6230.F20 IN ('HKZ','YJQ','YDF','YZR')) AND NOT EXISTS (SELECT 1 FROM S62.T6250 WHERE T6250.F03 = T6110.F01 AND T6250.F07 = 'F' AND T6250.F08 = 'S')) as other FROM DUAL");
        InvestmentLoanEntity quarterData = new InvestmentLoanEntity();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql.toString()))
            {
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        quarterData.totalLoan = resultSet.getInt(1);
                        quarterData.totalInvestment = resultSet.getInt(2);
                        quarterData.totalOther = resultSet.getInt(3);
                        return quarterData;
                    }
                }
                return quarterData;
            }
        }
    }
    
    @Override
    public AgeDistributionEntity[] getAgeRanageData()
        throws Throwable
    {
        AgeDistributionEntity[] ageDistributionDatas = new AgeDistributionEntity[5];
        String ageRanage = "";
        try (Connection connection = getConnection())
        {
            for (int i = 0; i < ageDistributionDatas.length - 1; i++)
            {
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT COUNT(1) FROM S61.T6141  WHERE LEFT(F08,3)= ? "))
                {
                    switch (i)
                    {
                        case 0:
                            ps.setString(1, "199");
                            ageRanage = "90后";
                            break;
                        case 1:
                            ps.setString(1, "198");
                            ageRanage = "80后";
                            break;
                        case 2:
                            ps.setString(1, "197");
                            ageRanage = "70后";
                            break;
                        case 3:
                            ps.setString(1, "196");
                            ageRanage = "60后";
                            break;
                    }
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            ageDistributionDatas[i] = new AgeDistributionEntity();
                            ageDistributionDatas[i].number = resultSet.getInt(1);
                            ageDistributionDatas[i].ageRanage = ageRanage;
                        }
                    }
                }
            }
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT COUNT(1) FROM S61.T6141  WHERE LEFT(F08,3) NOT IN (?) LIMIT 1 "))
            {
                ps.setString(1, "'199','198','197','196'");
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        ageDistributionDatas[4] = new AgeDistributionEntity();
                        ageDistributionDatas[4].number = resultSet.getInt(1);
                        ageDistributionDatas[4].ageRanage = "其他";
                    }
                }
            }
        }
        return ageDistributionDatas;
    }
    
    /**
     * @return
     * @throws Throwable
     */
    @Override
    public Map<String, Integer> getUserData()
        throws Throwable
    {
        Map<String, Integer> result = new HashMap<String, Integer>();
        int total = 0;
        try (Connection connection = getConnection())
        {
            //统计不同来源的用户数
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT COUNT(1) AS F01,T6110.F08 AS F02 FROM S61.T6110 WHERE NOT EXISTS (SELECT 1 FROM S71.T7101 WHERE T7101.F01 = T6110.F01) GROUP BY T6110.F08"))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        result.put(rs.getString(2), rs.getInt(1));
                        total += rs.getInt(1);
                    }
                }
            }
            
            //统计充值用户数
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT COUNT(1) FROM S61.T6110 WHERE EXISTS (SELECT 1 FROM S65.T6502 WHERE T6502.F02 = T6110.F01 AND T6502.F08 IS NOT NULL )"))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        result.put("CHARGE", rs.getInt(1));
                    }
                }
            }
            
            //统计投资用户数
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT COUNT(DISTINCT F03) FROM S62.T6250 WHERE F07 = ? AND F08 = ?"))
            {
                ps.setString(1, T6250_F07.F.name());
                ps.setString(2, T6250_F08.S.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        result.put("INVEST", rs.getInt(1));
                    }
                }
            }
            
            //统计借款用户数
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT COUNT(1) FROM S61.T6110 WHERE EXISTS (SELECT 1 FROM S62.T6230 WHERE T6230.F02 = T6110.F01 AND T6230.F20 IN ('HKZ','YJQ','YDF','YZR'))"))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        result.put("LOAN", rs.getInt(1));
                    }
                }
            }
            
            //统计提现用户数
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT COUNT(DISTINCT T6503.F02) FROM S65.T6503 INNER JOIN S65.T6501 ON T6503.F01 = T6501.F01 WHERE T6501.F03 = 'CG'"))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        result.put("WITHDRAW", rs.getInt(1));
                    }
                }
            }
            
            result.put("TOTAL", total);
        }
        return result;
    }
    
    /**
     * 注册用户数统计趋势图数据
     *
     * @param type      查询类型（天、周、月、季、年）
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     * @throws Throwable
     */
    @Override
    public Map<String, Object> getRegisterUserData(String type, String startDate, String endDate)
        throws Throwable
    {
        if (StringHelper.isEmpty(startDate) || StringHelper.isEmpty(endDate))
        {
            throw new ParameterException("注册时间不能为空");
        }
        
        List<Integer> datas = new ArrayList<Integer>();
        List<String> areas = new ArrayList<String>();
        if (StringHelper.isEmpty(type) || "day".equals(type))
        {
            long times = DateParser.parse(endDate).getTime() - DateParser.parse(startDate).getTime();
            int day = IntegerParser.parse(times / (1000 * 3600 * 24));
            if (day > 31)
            {
                throw new ParameterException("按天统计，日期范围不能超过31天");
            }
            setDayDatas(datas, areas, startDate, endDate);
        }
        else if ("week".equals(type))
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateParser.parse(startDate));
            int startWeek = calendar.get(Calendar.WEEK_OF_YEAR);
            int startYear = calendar.get(Calendar.YEAR);
            calendar.setTime(DateParser.parse(endDate));
            int endWeek = calendar.get(Calendar.WEEK_OF_YEAR);
            int endYear = calendar.get(Calendar.YEAR);
            startWeek = IntegerParser.parse(String.valueOf(startYear) + String.valueOf(startWeek));
            endWeek = IntegerParser.parse(String.valueOf(endYear) + String.valueOf(endWeek));
            if ((endWeek - startWeek) > 30)
            {
                throw new ParameterException("按周统计，日期范围不能超过30周");
            }
            setWeekDatas(datas, areas, startDate, endDate);
        }
        else if ("month".equals(type))
        {
            if (!isSameYear(startDate, endDate))
            {
                throw new ParameterException("按月或者按季度统计，日期必须选择同一年");
            }
            setMonthDatas(datas, areas, startDate, endDate);
        }
        else if ("quarter".equals(type))
        {
            if (!isSameYear(startDate, endDate))
            {
                throw new ParameterException("按月或者按季度统计，日期必须选择同一年");
            }
            setQuarterDatas(datas, areas, startDate, endDate);
        }
        else if ("year".equals(type))
        {
            setYearDatas(datas, areas, startDate, endDate);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("datas", datas);
        result.put("areas", areas);
        return result;
    }
    
    /**
     * 判断是否为同一年
     * @param startDate
     * @param endDate
     * @return
     */
    private boolean isSameYear(String startDate, String endDate)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateParser.parse(startDate));
        int startYear = calendar.get(Calendar.YEAR);
        calendar.setTime(DateParser.parse(endDate));
        int endYear = calendar.get(Calendar.YEAR);
        return startYear == endYear;
    }
    
    /**
     * 按天查询注册用户
     * @param datas
     * @param areas
     * @param startDate
     * @param endDate
     * @throws Throwable
     */
    private void setDayDatas(List<Integer> datas, List<String> areas, String startDate, String endDate)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F01, F02 FROM S70.T7052 WHERE F01 >= ? AND F01 <= ? "))
            {
                ps.setString(1, startDate);
                ps.setString(2, endDate);
                try (ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        datas.add(rs.getInt(2));
                        areas.add(rs.getString(1).replace("-", "/"));
                    }
                }
            }
        }
    }
    
    /**
     * 按周查询注册用户
     * @param datas
     * @param areas
     * @param startDate
     * @param endDate
     * @throws Throwable
     */
    private void setWeekDatas(List<Integer> datas, List<String> areas, String startDate, String endDate)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F01, SUM(F02), F16 FROM S70.T7052 WHERE F01 >= ? AND F01 <= ? GROUP BY F16 ORDER BY F16 ASC "))
            {
                ps.setString(1, startDate);
                ps.setString(2, endDate);
                try (ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        datas.add(rs.getInt(2));
                        int year = IntegerParser.parse(rs.getString(3).substring(0, 4));
                        int week = IntegerParser.parse(rs.getString(3).substring(4));
                        areas.add(getDate(year, week).get("startDate").replace("-", "/") + "-"
                            + getDate(year, week).get("endDate").replace("-", "/"));
                    }
                }
            }
        }
    }
    
    /**
     * 按月查询注册用户
     * @param datas
     * @param areas
     * @param startDate
     * @param endDate
     * @throws Throwable
     */
    private void setMonthDatas(List<Integer> datas, List<String> areas, String startDate, String endDate)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F01, SUM(F02), F15 FROM S70.T7052 WHERE F01 >= ? AND F01 <= ? GROUP BY F15 ORDER BY F15 ASC "))
            {
                ps.setString(1, startDate);
                ps.setString(2, endDate);
                try (ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        datas.add(rs.getInt(2));
                        areas.add(rs.getString(3).substring(4) + "月");
                    }
                }
            }
        }
    }
    
    /**
     * 按月查询注册用户
     * @param datas
     * @param areas
     * @param startDate
     * @param endDate
     * @throws Throwable
     */
    private void setQuarterDatas(List<Integer> datas, List<String> areas, String startDate, String endDate)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F01, SUM(F02), F14 FROM S70.T7052 WHERE F01 >= ? AND F01 <= ? GROUP BY F14 ORDER BY F14 ASC "))
            {
                ps.setString(1, startDate);
                ps.setString(2, endDate);
                try (ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        datas.add(rs.getInt(2));
                        areas.add(rs.getString(3).substring(4) + "季度");
                    }
                }
            }
        }
    }
    
    /**
     * 按年查询注册用户
     * @param datas
     * @param areas
     * @param startDate
     * @param endDate
     * @throws Throwable
     */
    private void setYearDatas(List<Integer> datas, List<String> areas, String startDate, String endDate)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F01, SUM(F02), F13 FROM S70.T7052 WHERE F01 >= ? AND F01 <= ? GROUP BY F13 ORDER BY F13 ASC "))
            {
                ps.setString(1, startDate);
                ps.setString(2, endDate);
                try (ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        datas.add(rs.getInt(2));
                        areas.add(rs.getString(3));
                    }
                }
            }
        }
    }
    
    /**
     * 根据年的第几周得到开始日期和结束日期
     * @param year
     * @param week
     * @return
     */
    private Map<String, String> getDate(int year, int week)
    {
        Map<String, String> result = new HashMap<String, String>();
        int dayOfWeek = 0;
        Calendar calFirstDayOfTheYear = new GregorianCalendar(year, Calendar.JANUARY, 1);
        calFirstDayOfTheYear.add(Calendar.DATE, 7 * (week));
        
        dayOfWeek = calFirstDayOfTheYear.get(Calendar.DAY_OF_WEEK);
        
        Calendar calFirstDayInWeek = (Calendar)calFirstDayOfTheYear.clone();
        calFirstDayInWeek.add(Calendar.DATE, calFirstDayOfTheYear.getActualMinimum(Calendar.DAY_OF_WEEK) - dayOfWeek);
        Date firstDayInWeek = calFirstDayInWeek.getTime();
        result.put("startDate", DateParser.format(firstDayInWeek));
        Calendar calLastDayInWeek = (Calendar)calFirstDayOfTheYear.clone();
        calLastDayInWeek.add(Calendar.DATE, calFirstDayOfTheYear.getActualMaximum(Calendar.DAY_OF_WEEK) - dayOfWeek);
        Date lastDayInWeek = calLastDayInWeek.getTime();
        result.put("endDate", DateParser.format(lastDayInWeek));
        return result;
    }
    
    /**
     * 平台用户数统计——统计报表
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws Throwable
     */
    @Override
    public PagingResult<T7052> searchUsersOpData(Paging paging, Date startDate, Date endDate)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S70.T7052 WHERE 1=1 ");
        List<Object> params = new ArrayList<Object>();
        if (null != startDate)
        {
            sql.append(" AND T7052.F01 >= ? ");
            params.add(DateParser.format(startDate, "yyyy-MM-dd"));
        }
        
        if (null != endDate)
        {
            sql.append(" AND T7052.F01 <= ? ");
            params.add(DateParser.format(endDate, "yyyy-MM-dd"));
        }
        sql.append(" ORDER BY F01 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T7052>()
            {
                @Override
                public T7052[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<T7052> list = null;
                    while (resultSet.next())
                    {
                        T7052 t7052 = new T7052();
                        t7052.F01 = resultSet.getString(1);
                        t7052.F02 = resultSet.getInt(2);
                        t7052.F03 = resultSet.getInt(3);
                        t7052.F04 = resultSet.getInt(4);
                        t7052.F05 = resultSet.getInt(5);
                        t7052.F06 = resultSet.getInt(6);
                        t7052.F07 = resultSet.getInt(7);
                        t7052.F08 = resultSet.getInt(8);
                        t7052.F09 = resultSet.getInt(9);
                        t7052.F10 = resultSet.getInt(10);
                        t7052.F11 = resultSet.getInt(11);
                        if (list == null)
                        {
                            list = new ArrayList<T7052>();
                        }
                        list.add(t7052);
                    }
                    return (list == null || list.size() <= 0) ? null : list.toArray(new T7052[list.size()]);
                }
            }, paging, sql.toString(), params);
        }
        
    }
    
    /**
     * 平台用户数统计——统计信息
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws Throwable
     */
    @Override
    public T7052 searchUsersTotalData(Date startDate, Date endDate)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT SUM(F02), SUM(F03), SUM(F04), SUM(F05), SUM(F06), SUM(F07), SUM(F08), SUM(F09), SUM(F10), SUM(F11) FROM S70.T7052 WHERE 1=1 ");
        List<Object> params = new ArrayList<Object>();
        if (null != startDate)
        {
            sql.append(" AND T7052.F01 >= ? ");
            params.add(DateParser.format(startDate, "yyyy-MM-dd"));
        }
        
        if (null != endDate)
        {
            sql.append(" AND T7052.F01 <= ? ");
            params.add(DateParser.format(endDate, "yyyy-MM-dd"));
        }
        sql.append(" ORDER BY F01 DESC");
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<T7052>()
            {
                @Override
                public T7052 parse(ResultSet resultSet)
                    throws SQLException
                {
                    T7052 t7052 = new T7052();
                    while (resultSet.next())
                    {
                        t7052.F02 = resultSet.getInt(1);
                        t7052.F03 = resultSet.getInt(2);
                        t7052.F04 = resultSet.getInt(3);
                        t7052.F05 = resultSet.getInt(4);
                        t7052.F06 = resultSet.getInt(5);
                        t7052.F07 = resultSet.getInt(6);
                        t7052.F08 = resultSet.getInt(7);
                        t7052.F09 = resultSet.getInt(8);
                        t7052.F10 = resultSet.getInt(9);
                        t7052.F11 = resultSet.getInt(10);
                    }
                    return t7052;
                }
            }, sql.toString(), params);
        }
        
    }
    
    @Override
    public AgeDistributionEntity[] getUserAgeData()
        throws Throwable
    {
        
        AgeDistributionEntity[] ageDistributionDatas = new AgeDistributionEntity[6];
        String ageRanage = "";
        try (Connection connection = getConnection())
        {
            int num = 0;
            int slNum = 0;
            for (int i = 0; i < ageDistributionDatas.length - 1; i++)
            {
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT COUNT(1) FROM S61.T6141 WHERE T6141.F08 IS NOT NULL AND (YEAR(NOW())-YEAR(T6141.F08)) BETWEEN ? AND ?"))
                {
                    switch (i)
                    {
                        case 0:
                            ps.setInt(1, 18);
                            ps.setInt(2, 25);
                            ageRanage = "18-25岁";
                            break;
                        case 1:
                            ps.setInt(1, 26);
                            ps.setInt(2, 35);
                            ageRanage = "26-35岁";
                            break;
                        case 2:
                            ps.setInt(1, 36);
                            ps.setInt(2, 45);
                            ageRanage = "36-45岁";
                            break;
                        case 3:
                            ps.setInt(1, 46);
                            ps.setInt(2, 55);
                            ageRanage = "46-55岁";
                            break;
                        case 4:
                            ps.setInt(1, 56);
                            ps.setInt(2, 2000);
                            ageRanage = "56岁以上";
                            break;
                    }
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            slNum = resultSet.getInt(1);
                            num += slNum;
                            ageDistributionDatas[i] = new AgeDistributionEntity();
                            ageDistributionDatas[i].number = slNum;
                            ageDistributionDatas[i].ageRanage = ageRanage;
                        }
                    }
                }
            }
            //统计18岁以下和没有实名认证的
            try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(1) FROM S61.T6141"))
            {
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        ageDistributionDatas[5] = new AgeDistributionEntity();
                        ageDistributionDatas[5].number = resultSet.getInt(1) - num;
                        ageDistributionDatas[5].ageRanage = "其他";
                    }
                }
            }
        }
        return ageDistributionDatas;
    }
    
    @Override
    public AgeDistributionEntity[] getUserSexData()
        throws Throwable
    {
        AgeDistributionEntity[] ageDistributionDatas = new AgeDistributionEntity[3];
        String ageRanage = "";
        String sql = "";
        try (Connection connection = getConnection())
        {
            for (int i = 0; i < ageDistributionDatas.length; i++)
            {
                switch (i)
                {
                    case 0:
                        sql = " SELECT COUNT(1) FROM S61.T6141 WHERE T6141.F09 = 'F' ";
                        ageRanage = "女";
                        break;
                    case 1:
                        sql = " SELECT COUNT(1) FROM S61.T6141 WHERE T6141.F09 = 'M' ";
                        ageRanage = "男";
                        break;
                    case 2:
                        sql = "SELECT COUNT(1) FROM S61.T6141 WHERE ISNULL(T6141.F09) ";
                        ageRanage = "其他";
                        break;
                }
                try (PreparedStatement ps = connection.prepareStatement(sql))
                {
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            if (ageDistributionDatas[i] == null)
                            {
                                ageDistributionDatas[i] = new AgeDistributionEntity();
                            }
                            ageDistributionDatas[i].number = resultSet.getInt(1);
                            ageDistributionDatas[i].ageRanage = ageRanage;
                        }
                    }
                }
            }
        }
        return ageDistributionDatas;
    }
    
    @Override
    public AgeDistributionEntity[] getUserRegisterSourceData()
        throws Throwable
    {
        AgeDistributionEntity[] ageDistributionDatas = new AgeDistributionEntity[4];
        String ageRanage = "";
        try (Connection connection = getConnection())
        {
            for (int i = 0; i < ageDistributionDatas.length; i++)
            {
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT COUNT(1) FROM S61.T6110 WHERE T6110.F08 = ? AND T6110.F02 <> '平台账号' "))
                {
                    switch (i)
                    {
                        case 0:
                            ps.setString(1, T6110_F08.APPZC.name());
                            ageRanage = "APP";
                            break;
                        case 1:
                            ps.setString(1, T6110_F08.WXZC.name());
                            ageRanage = "微信";
                            break;
                        case 2:
                            ps.setString(1, T6110_F08.PCZC.name());
                            ageRanage = "PC注册";
                            break;
                        case 3:
                            ps.setString(1, T6110_F08.HTTJ.name());
                            ageRanage = "后台添加";
                            break;
                    }
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            ageDistributionDatas[i] = new AgeDistributionEntity();
                            ageDistributionDatas[i].number = resultSet.getInt(1);
                            ageDistributionDatas[i].ageRanage = ageRanage;
                        }
                    }
                }
            }
        }
        return ageDistributionDatas;
    }
    
    @Override
    public AgeDistributionEntity[] getUserInvestSourceData()
        throws Throwable
    {
        AgeDistributionEntity[] ageDistributionDatas = new AgeDistributionEntity[3];
        String ageRanage = "";
        try (Connection connection = getConnection())
        {
            for (int i = 0; i < ageDistributionDatas.length; i++)
            {
                try (PreparedStatement ps =
                    connection.prepareStatement("SELECT COUNT(1) FROM S62.T6250 WHERE T6250.F11 = ? AND T6250.F07 = 'F' AND T6250.F08 = 'S' "))
                {
                    switch (i)
                    {
                        case 0:
                            ps.setString(1, T6250_F11.PC.name());
                            ageRanage = "PC";
                            break;
                        case 1:
                            ps.setString(1, T6250_F11.APP.name());
                            ageRanage = "APP";
                            break;
                        case 2:
                            ps.setString(1, T6250_F11.WEIXIN.name());
                            ageRanage = "微信";
                            break;
                    }
                    try (ResultSet resultSet = ps.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            ageDistributionDatas[i] = new AgeDistributionEntity();
                            ageDistributionDatas[i].number = resultSet.getInt(1);
                            ageDistributionDatas[i].ageRanage = ageRanage;
                        }
                    }
                }
            }
        }
        return ageDistributionDatas;
    }
    
    public static class UserDataManageFactory implements ServiceFactory<UserDataManage>
    {
        
        @Override
        public UserDataManage newInstance(ServiceResource serviceResource)
        {
            return new UserDataManageImpl(serviceResource);
        }
        
    }
    
}
