package com.dimeng.p2p.account.user.service.achieve;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.entities.T6103;
import com.dimeng.p2p.S61.enums.T6103_F06;
import com.dimeng.p2p.S61.enums.T6103_F08;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S62.enums.T6285_F09;
import com.dimeng.p2p.account.user.service.MyExperienceManage;
import com.dimeng.p2p.account.user.service.entity.HzEntity;
import com.dimeng.p2p.account.user.service.entity.MyExperience;
import com.dimeng.p2p.account.user.service.entity.MyExperienceRecod;
import com.dimeng.p2p.account.user.service.entity.TenderAccount;
import com.dimeng.p2p.account.user.service.query.TyjBackAccountQuery;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.TimestampParser;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyExperienceManageImpl extends AbstractAccountService implements MyExperienceManage
{
    
    public MyExperienceManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<MyExperienceRecod> searchAll(Map<String, Object> params, Paging paging)
        throws Throwable
    {
        ArrayList<Object> list = new ArrayList<>();
        StringBuilder sql =
            new StringBuilder(
                "SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F16 FROM S61.T6103 WHERE T6103.F02 = ? ");
        list.add(serviceResource.getSession().getAccountId());
        if (params != null && params.size() > 0)
        {
            String F06 = String.valueOf(params.get("status"));
            if (!StringHelper.isEmpty(F06))
            {
                sql.append(" AND F06 = ?");
                list.add(F06);
            }
        }
        sql.append(" ORDER BY F04 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<MyExperienceRecod>()
            {
                @Override
                public MyExperienceRecod[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<MyExperienceRecod> list = null;
                    
                    while (resultSet.next())
                    {
                        MyExperienceRecod record = new MyExperienceRecod();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getBigDecimal(3);
                        record.F04 = resultSet.getTimestamp(4);
                        record.F05 = resultSet.getTimestamp(5);
                        record.F06 = T6103_F06.parse(resultSet.getString(6));
                        record.F07 = resultSet.getInt(7);
                        record.F08 = T6103_F08.parse(resultSet.getString(8));
                        record.F09 = resultSet.getString(9);
                        record.F10 = resultSet.getTimestamp(10);
                        record.F11 = resultSet.getTimestamp(11);
                        record.F16 = resultSet.getString(12);
                        record.beginDate = TimestampParser.format(record.F04, "yyyy-MM-dd");
                        record.endDate = TimestampParser.format(record.F05, "yyyy-MM-dd");
                        record.fromStr = record.F08.getChineseName();
                        record.state = record.F06.getChineseName();
                        record.expAmount = Formater.formatAmount(record.F03);

                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null
                        : list.toArray(new MyExperienceRecod[list.size()]));
                }
            },
                paging,
                sql.toString(),
                list);
        }
    }
    
    @Override
    public TenderAccount censusAll()
        throws Throwable
    {
        TenderAccount tenderAccount = new TenderAccount();
        int accountId = serviceResource.getSession().getAccountId();
        try (Connection connection = getConnection())
        {
            // 体验金投资总额
            BigDecimal totalTyj = BigDecimal.ZERO;
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(T6286.F04) FROM S62.T6286 WHERE T6286.F03= ?"))
            {
                ps.setInt(1, accountId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        totalTyj = rs.getBigDecimal(1);
                    }
                }
            }
            tenderAccount.tyjze = totalTyj;
            
            // 体验金已赚金额
            BigDecimal tyjyz = BigDecimal.ZERO;
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(T6285.F07) FROM S62.T6285 WHERE T6285.F04 = ? AND T6285.F05 = ? AND T6285.F09 = ?"))
            {
                ps.setInt(1, accountId);
                ps.setInt(2, FeeCode.TYJTZ);
                ps.setString(3, T6285_F09.YFH.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        tyjyz = rs.getBigDecimal(1);
                    }
                }
            }
            tenderAccount.tyjyz = tyjyz;
            
            // 体验金待赚金额
            BigDecimal tyjdz = BigDecimal.ZERO;
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT SUM(T6285.F07) FROM S62.T6285 WHERE T6285.F04 = ? AND T6285.F05 = ? AND T6285.F09 in(?, ?)"))
            {
                ps.setInt(1, accountId);
                ps.setInt(2, FeeCode.TYJTZ);
                ps.setString(3, T6285_F09.WFH.name());
                ps.setString(4, T6285_F09.FHZ.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        tyjdz = rs.getBigDecimal(1);
                    }
                }
            }
            tenderAccount.tyjdz = tyjdz;
            
            // 体验金平均收益率
            if (totalTyj.compareTo(BigDecimal.ZERO) > 0)
            {
                tenderAccount.tyjsyl = tyjyz.divide(totalTyj, 2, BigDecimal.ROUND_HALF_UP);
            }
            tenderAccount.yzzje = tenderAccount.yxyz.add(tenderAccount.tyjyz);
            
            // 回收中的体验金数量
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT COUNT(1) FROM (SELECT COUNT(1) FROM S62.T6285 WHERE T6285.F05 = ? AND F09 IN (?, ?) AND F04 = ? GROUP BY T6285.F12) TEMP"))
            {
                ps.setInt(1, FeeCode.TYJTZ);
                ps.setString(2, T6285_F09.FHZ.name());
                ps.setString(3, T6285_F09.WFH.name());
                ps.setInt(4, accountId);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        tenderAccount.tyjcyl = resultSet.getInt(1);
                    }
                }
            }
            
            // 体验金账户资产
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03 FROM S61.T6103 WHERE T6103.F02 = ? AND T6103.F06 = ? AND DATE(T6103.F04) <= ? AND T6103.F05 >= ? LIMIT 1"))
            {
                pstmt.setInt(1, accountId);
                pstmt.setString(2, T6103_F06.WSY.name());
                pstmt.setDate(3, getCurrentDate(connection));
                pstmt.setDate(4, getCurrentDate(connection));
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        tenderAccount.tyjzhzc = resultSet.getInt(1);
                    }
                }
            }
        }
        
        return tenderAccount;
    }
    
    @Override
    public PagingResult<MyExperience> searMyExperience(String queryType, Paging paging)
        throws Throwable
    {
        ArrayList<Object> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT T6230.F03 AS F01,T6103.F03 AS F02,T6230.F06 AS F03,T6103.F07 AS F04,T6230.F09 AS F05,T6231.F21 AS F06,T6231.F22 AS F07,");
        sql.append("(SELECT SUM(T6285.F07) FROM S62.T6285 WHERE T6285.F02 = T6230.F01 AND T6285.F12 = T6103.F01 AND T6285.F09 IN ('WFH','FHZ')) AS F08,");
        sql.append("(SELECT SUM(T6285.F07) FROM S62.T6285 WHERE T6285.F02 = T6230.F01 AND T6285.F12 = T6103.F01 AND T6285.F09 = 'YFH') AS F09,");
        sql.append("(SELECT MIN(T6285.F08) FROM S62.T6285 WHERE T6285.F02 = T6230.F01 AND T6285.F12 = T6103.F01 AND T6285.F09 IN ('WFH','FHZ')) AS F10,");
        sql.append("(SELECT MAX(T6285.F08) FROM S62.T6285 WHERE T6285.F02 = T6230.F01 AND T6285.F12 = T6103.F01 AND T6285.F09 = 'YFH') AS F11,");
        sql.append("(SELECT T6286.F05 FROM S62.T6286 WHERE T6286.F02 = T6230.F01 AND T6286.F10 = T6103.F01) AS F12,T6231.F12 AS F13, T6103.F16 AS F14 ");
        sql.append("FROM S62.T6230 INNER JOIN S61.T6103 ON T6230.F01 = T6103.F13 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 WHERE T6103.F02 = ? ");
        list.add(serviceResource.getSession().getAccountId());
        if ("SQZ".equals(queryType))
        {
            sql.append("AND T6230.F20 IN ('TBZ','DFK') ");
            sql.append("ORDER BY F12 DESC");
        }
        else if ("CYZ".equals(queryType))
        {
            sql.append("AND (SELECT COUNT(1) FROM S62.T6285 T6285_CY WHERE T6285_CY.F09 IN ('WFH','FHZ') AND T6285_CY.F04 = ? AND T6285_CY.F02 = T6230.F01) > 0 ");
            list.add(serviceResource.getSession().getAccountId());
            sql.append("ORDER BY F13 DESC");
        }
        else if ("YJZ".equals(queryType))
        {
            sql.append("AND (SELECT COUNT(1) FROM S62.T6286 WHERE T6286.F10 = T6103.F01 AND T6286.F07 = 'S') > 0 ");
            sql.append("AND (SELECT COUNT(1) FROM S62.T6285 T6285_CY WHERE T6285_CY.F09 IN ('WFH','FHZ') AND T6285_CY.F04 = ? AND T6285_CY.F02 = T6230.F01) = 0 ");
            list.add(serviceResource.getSession().getAccountId());
            sql.append("ORDER BY F11 DESC");
        }
        /* sql.append("AND TEMP.F08 < 0 AND TEMP.F09 > 0 "); */
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<MyExperience>()
            {
                @Override
                public MyExperience[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<MyExperience> list = null;
                    while (resultSet.next())
                    {
                        MyExperience myExperience = new MyExperience();
                        myExperience.F02 = resultSet.getString(1);
                        myExperience.F03 = resultSet.getBigDecimal(2);
                        myExperience.F04 = resultSet.getBigDecimal(3);
                        myExperience.F10 = resultSet.getInt(4);
                        myExperience.F13 = resultSet.getInt(5);
                        myExperience.F11 = T6231_F21.parse(resultSet.getString(6));
                        myExperience.F12 = resultSet.getInt(7);
                        myExperience.F08 = resultSet.getBigDecimal(8);
                        myExperience.F05 = resultSet.getBigDecimal(9);
                        myExperience.F09 = resultSet.getTimestamp(10);
                        myExperience.F06 = resultSet.getTimestamp(11);
                        myExperience.F15 = resultSet.getString(14);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(myExperience);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new MyExperience[list.size()]));
                }
            }, paging, sql.toString(), list);
        }
    }
    
    @Override
    public BigDecimal getExperienceAmount()
        throws Throwable
    {
        BigDecimal amount = new BigDecimal(0);
        try (Connection connection = getConnection())
        {
            Date currDate =  getCurrentDate(connection);
            // 体验金账户资产
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT SUM(F03) FROM S61.T6103 WHERE T6103.F02 = ? AND T6103.F06 = ? AND DATE(T6103.F04) <= ? AND T6103.F05 >= ?"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, T6103_F06.WSY.name());
                pstmt.setDate(3, currDate);
                pstmt.setDate(4, currDate);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        amount = resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        return amount;
    }
    
    @Override
    public PagingResult<HzEntity> searchHz(TyjBackAccountQuery backOffQuery, Paging paging)
        throws Throwable
    {
        StringBuilder sb = new StringBuilder("");
        sb.append("SELECT (SELECT T6251.F02 FROM S62.T6251 WHERE T6251.F03 =  T6285.F02 AND T6251.F04 = T6285.F04 LIMIT 1) AS F01,T6230.F03 AS F02,(SELECT T5122.F02 FROM S51.T5122 WHERE T5122.F01 = T6285.F05) AS F03,T6285.F07 AS F04,T6285.F08 AS F05,T6285.F09 AS F06 ");
        sb.append("FROM S62.T6285 ");
        sb.append("INNER JOIN S62.T6230 ON T6285.F02=T6230.F01 ");
        sb.append("WHERE T6285.F04= ?  ");
        final List<HzEntity> lists = new ArrayList<>();
        List<Object> paramter = new ArrayList<>();
        paramter.add(serviceResource.getSession().getAccountId());
        if (backOffQuery != null)
        {
            if (backOffQuery.getQueryType() != null)
            {
                switch (backOffQuery.getQueryType())
                {
                    case YS:
                    {
                        sb.append("AND T6285.F09 = ? ");
                        paramter.add(T6285_F09.YFH.name());
                        break;
                    }
                    case DS:
                    {
                        sb.append("AND (T6285.F09 = ? OR T6285.F09 = ?) ");
                        paramter.add(T6285_F09.WFH.name());
                        paramter.add(T6285_F09.FHZ.name());
                        break;
                    }
                    
                    default:
                        break;
                }
            }
            if (backOffQuery.getTimeStart() != null)
            {
                sb.append("AND DATE(T6285.F08) >= ? ");
                paramter.add(DateParser.format(backOffQuery.getTimeStart()));
            }
            if (backOffQuery.getTimeEnd() != null)
            {
                sb.append("AND DATE(T6285.F08) <= ? ");
                paramter.add(DateParser.format(backOffQuery.getTimeEnd()));
            }
        }
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<HzEntity>()
            {
                
                @Override
                public HzEntity[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    while (resultSet.next())
                    {
                        HzEntity hzEntity = new HzEntity();
                        hzEntity.zrId = resultSet.getString(1);
                        hzEntity.title = resultSet.getString(2);
                        hzEntity.orderType = resultSet.getString(3);
                        hzEntity.amount = resultSet.getBigDecimal(4);
                        hzEntity.skDate = resultSet.getDate(5);
                        hzEntity.status = EnumParser.parse(T6285_F09.class, resultSet.getString(6));
                        lists.add(hzEntity);
                    }
                    return lists.toArray(new HzEntity[lists.size()]);
                }
            }, paging, sb.toString(), paramter);
        }
    }
    
    @Override
    public Map<String, Object> getInterestTot()
        throws Throwable
    {
        BigDecimal amount = new BigDecimal(0);
        int count = 0;
        Map<String, Object> retMap = new HashMap<String, Object>();
        try (Connection connection = getConnection())
        {
            // 体验金账户资产
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT SUM(T6285.F07), COUNT(*) FROM S62.T6285 WHERE T6285.F04 = ? AND T6285.F05 = ? AND T6285.F09 IN(?, ?)"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setInt(2, FeeCode.TYJTZ);
                pstmt.setString(3, T6285_F09.WFH.name());
                pstmt.setString(4, T6285_F09.FHZ.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        amount = resultSet.getBigDecimal(1);
                        count = resultSet.getInt(2);
                    }
                }
            }
        }
        retMap.put("amount", amount);
        retMap.put("count", count);
        return retMap;
    }

    /**
     * 查找我的未使用体验金
     * 创 建 人:   luoyi
     * 创 建 时 间 : 2015年4月29日 下午2:14:34
     */
    @Override
    public List<T6103> findMyTyjList(int accountId)
    {
        List<T6103> list = new ArrayList<T6103>();
        try
        {
            try (Connection connection = getConnection())
            {
                Date currDate =  getCurrentDate(connection);
                // 查询我的体验
                String sql =  "SELECT F01,F02,F03,F04,F05,F06,F07,F08,F09,F10,F11,F12,F13,F16 FROM S61.T6103 where F06 = ? AND F02 = ? AND DATE(T6103.F04) <= ? AND T6103.F05 >= ?";
                try (PreparedStatement pstmt = connection.prepareStatement(sql))
                {
                    pstmt.setString(1, T6103_F06.WSY.name());// 未使用
                    pstmt.setInt(2, accountId);// 用户id
                    pstmt.setDate(3, currDate);
                    pstmt.setDate(4, currDate);
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {//查询结果
                        while (resultSet.next())
                        {
                            T6103 record = new T6103();
                            record.F01 = resultSet.getInt(1);
                            record.F02 = resultSet.getInt(2);
                            record.F03 = resultSet.getBigDecimal(3);
                            record.F04 = resultSet.getTimestamp(4);
                            record.F05 = resultSet.getTimestamp(5);
                            record.F06 = T6103_F06.parse(resultSet.getString(6));
                            record.F07 = resultSet.getInt(7);
                            record.F08 = T6103_F08.parse(resultSet.getString(8));
                            record.F09 = resultSet.getString(9);
                            record.F10 = resultSet.getTimestamp(10);
                            record.F11 = resultSet.getTimestamp(11);
                            record.F12 = resultSet.getInt(12);
                            record.F13 = resultSet.getInt(13);
                            record.F16 = resultSet.getString(14);
                            list.add(record);
                        }
                    }
                }
            }
        }
        catch (SQLException sqlException)
        {
            logger.error("MyExperienceManageImpl.findMyTyjList() SQLException error", sqlException);
        }
        catch (Throwable throwable)
        {
            logger.error("MyExperienceManageImpl.findMyTyjList() Throwable error", throwable);
        }
        return list;
    }

    /**
     * 查询某个标是否用过体验金
     * @param bidId
     * @return
     * @throws Throwable
     */
    @Override
    public T6103 getT6103(int bidId)
        throws Throwable
    {
        try(Connection connection = getConnection()){
            T6103 record = null;
              try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01,F02,F03,F04,F05,F06,F07,F08,F09,F10,F11,F12,F13 FROM S61.T6103 where F02 = ? AND F13 = ? LIMIT 1")) {
                  pstmt.setInt(1, serviceResource.getSession().getAccountId());
                  pstmt.setInt(2, bidId);
                  try(ResultSet resultSet = pstmt.executeQuery()) {
                      if(resultSet.next()) {
                          record = new T6103();
                          record.F01 = resultSet.getInt(1);
                          record.F02 = resultSet.getInt(2);
                          record.F03 = resultSet.getBigDecimal(3);
                          record.F04 = resultSet.getTimestamp(4);
                          record.F05 = resultSet.getTimestamp(5);
                          record.F06 = T6103_F06.parse(resultSet.getString(6));
                          record.F07 = resultSet.getInt(7);
                          record.F08 = T6103_F08.parse(resultSet.getString(8));
                          record.F09 = resultSet.getString(9);
                          record.F10 = resultSet.getTimestamp(10);
                          record.F11 = resultSet.getTimestamp(11);
                          record.F12 = resultSet.getInt(12);
                          record.F13 = resultSet.getInt(13);
                      }
                  }
              }
              return record;
         }
    }
    
    @Override
    public PagingResult<MyExperience> searMyExperienceById(Map<String, Object> params, Paging paging)
        throws Throwable
    {
        ArrayList<Object> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT T6230.F03 AS F01,T6103.F03 AS F02,T6230.F06 AS F03,T6103.F07 AS F04,T6230.F09 AS F05,T6231.F21 AS F06,T6231.F22 AS F07,");
        sql.append("(SELECT SUM(T6285.F07) FROM S62.T6285 WHERE T6285.F02 = T6230.F01 AND T6285.F12 = T6103.F01 AND T6285.F09 IN ('WFH','FHZ')) AS F08,");
        sql.append("(SELECT SUM(T6285.F07) FROM S62.T6285 WHERE T6285.F02 = T6230.F01 AND T6285.F12 = T6103.F01 AND T6285.F09 = 'YFH') AS F09,");
        sql.append("(SELECT MIN(T6285.F08) FROM S62.T6285 WHERE T6285.F02 = T6230.F01 AND T6285.F12 = T6103.F01 AND T6285.F09 IN ('WFH','FHZ')) AS F10,");
        sql.append("(SELECT MAX(T6285.F08) FROM S62.T6285 WHERE T6285.F02 = T6230.F01 AND T6285.F12 = T6103.F01 AND T6285.F09 = 'YFH') AS F11,");
        sql.append("(SELECT T6286.F05 FROM S62.T6286 WHERE T6286.F02 = T6230.F01 AND T6286.F10 = T6103.F01) AS F12,T6231.F12 AS F13, T6231.F13 AS F14, T6103.F16 AS F15 ");
        sql.append("FROM S62.T6230 INNER JOIN S61.T6103 ON T6230.F01 = T6103.F13 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 WHERE T6103.F02 = ? ");
        list.add(serviceResource.getSession().getAccountId());
        sql.append(" AND T6103.F01 = ? ");
        list.add(String.valueOf(params.get("expId")));
        
        String queryType = String.valueOf(params.get("status"));
        if ("SQZ".equals(queryType))
        {
            sql.append("AND T6230.F20 IN ('TBZ','DFK') ");
            sql.append("ORDER BY F12 DESC");
        }
        else if ("CYZ".equals(queryType))
        {
            sql.append("AND (SELECT COUNT(1) FROM S62.T6285 T6285_CY WHERE T6285_CY.F09 IN ('WFH','FHZ') AND T6285_CY.F04 = ? AND T6285_CY.F02 = T6230.F01) > 0 ");
            list.add(serviceResource.getSession().getAccountId());
            sql.append("ORDER BY F13 DESC");
        }
        else if ("YJZ".equals(queryType))
        {
            sql.append("AND (SELECT COUNT(1) FROM S62.T6286 WHERE T6286.F10 = T6103.F01 AND T6286.F07 = 'S') > 0 ");
            sql.append("AND (SELECT COUNT(1) FROM S62.T6285 T6285_CY WHERE T6285_CY.F09 IN ('WFH','FHZ') AND T6285_CY.F04 = ? AND T6285_CY.F02 = T6230.F01) = 0 ");
            list.add(serviceResource.getSession().getAccountId());
            sql.append("ORDER BY F11 DESC");
        }
        /* sql.append("AND TEMP.F08 < 0 AND TEMP.F09 > 0 "); */
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<MyExperience>()
            {
                @Override
                public MyExperience[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<MyExperience> list = null;
                    while (resultSet.next())
                    {
                        MyExperience myExperience = new MyExperience();
                        myExperience.F02 = resultSet.getString(1);
                        myExperience.F03 = resultSet.getBigDecimal(2);
                        myExperience.F04 = resultSet.getBigDecimal(3);
                        myExperience.F10 = resultSet.getInt(4);
                        myExperience.F13 = resultSet.getInt(5);
                        myExperience.F11 = T6231_F21.parse(resultSet.getString(6));
                        myExperience.F12 = resultSet.getInt(7);
                        myExperience.F08 = resultSet.getBigDecimal(8);
                        myExperience.F05 = resultSet.getBigDecimal(9);
                        myExperience.F09 = resultSet.getTimestamp(10);
                        myExperience.F06 = resultSet.getTimestamp(11);
                        myExperience.F14 = resultSet.getTimestamp(14);
                        myExperience.F15 = resultSet.getString(15);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(myExperience);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new MyExperience[list.size()]));
                }
            }, paging, sql.toString(), list);
        }
    }
}
