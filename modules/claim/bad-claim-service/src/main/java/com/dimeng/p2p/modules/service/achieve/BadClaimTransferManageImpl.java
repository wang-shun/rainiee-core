/*
 * 文 件 名:  BadClaimTransferManageImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月14日
 */
package com.dimeng.p2p.modules.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6110_F19;
import com.dimeng.p2p.S62.entities.T6264;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F12;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F14;
import com.dimeng.p2p.S62.enums.T6230_F33;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S62.enums.T6264_F04;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.modules.service.AbstractBadClaimService;
import com.dimeng.p2p.repeater.claim.BadClaimTransferManage;
import com.dimeng.p2p.repeater.claim.entity.BadAssets;
import com.dimeng.p2p.repeater.claim.entity.BadClaimDsh;
import com.dimeng.p2p.repeater.claim.entity.BadClaimShDetails;
import com.dimeng.p2p.repeater.claim.entity.BadClaimYzr;
import com.dimeng.p2p.repeater.claim.entity.BadClaimZr;
import com.dimeng.p2p.repeater.claim.entity.BadClaimZrDetails;
import com.dimeng.p2p.repeater.claim.entity.BidInfo;
import com.dimeng.p2p.repeater.claim.query.DshQuery;
import com.dimeng.p2p.repeater.claim.query.DzrQuery;
import com.dimeng.p2p.repeater.claim.query.YzrQuery;
import com.dimeng.p2p.variables.defines.BadClaimVariavle;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.TimestampParser;

/**
 * <不良债权转让管理>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月14日]
 */
public class BadClaimTransferManageImpl extends AbstractBadClaimService implements BadClaimTransferManage
{
    
    public BadClaimTransferManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<BadClaimZr> badClaimDzrSearch(DzrQuery dzrQuery, Paging paging)
        throws Throwable
    {
        
        try (Connection connection = getConnection())
        {
            Date newDate = getCurrentDate(connection);
            ArrayList<Object> parameters = new ArrayList<>();
            final StringBuilder sql =
                new StringBuilder(
                    "SELECT T6230.F01 F01,T6230.F03 F02,(T6230.F05 - T6230.F07) F03,T6230.F06 F04,T6230.F10 F05,T6230.F11 F06,T6230.F13 F07,T6230.F14 F08,T6230.F25 F09,T6230.F33 F10,T6110.F02 F11,T6231.F03 F12,T6231.F02 F13,DATEDIFF(?, a.F08) F14,a.F01 F15,a.F08 F16,(SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F06  = a.F06 AND T6252.F09 ='WH' AND T6252.F05 IN ( '7001', '7002', '7003', '7004' )) F17,(SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F09 ='WH' AND T6252.F05 IN ( '7001', '7002', '7003', '7004' )) F18,T6110.F01 F19 ");
            sql.append("FROM S62.T6252 a JOIN S62.T6231 ON a.F02=T6231.F01 JOIN S62.T6230 ON T6231.F01=T6230.F01 JOIN S61.T6110 ON T6230.F02 = T6110.F01 JOIN (SELECT MIN(b.F01) F01 FROM S62.T6252 b WHERE b.F08 < ? AND b.F09 = 'WH' AND ? >= DATE_ADD(b.F08, INTERVAL ? DAY ) AND b.F05 IN ( '7001', '7002', '7003', '7004' ) GROUP BY b.F02) bb ON a.F01=bb.F01 WHERE a.F02 NOT IN (SELECT T6264.F03 FROM S62.T6264 WHERE T6264.F04 IN ('DSH','ZRZ')) AND T6230.F20 <> 'YDF' ");
            parameters.add(newDate);
            parameters.add(newDate);
            parameters.add(newDate);
            parameters.add(serviceResource.getResource(ConfigureProvider.class)
                .getProperty(BadClaimVariavle.BLZQZR_YQ_DAY));
            badClaimDzrSearchParameter(sql, dzrQuery, parameters);
            sql.append(" ORDER BY a.F08 DESC");
            return selectPaging(connection, new ArrayParser<BadClaimZr>()
            {
                
                @Override
                public BadClaimZr[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<BadClaimZr> list = null;
                    BadClaimZr badClaimZr = null;
                    Calendar yqCal = null;
                    while (rs.next())
                    {
                        
                        badClaimZr = new BadClaimZr();
                        badClaimZr.bidId = rs.getInt(1);
                        badClaimZr.loanTitle = rs.getString(2);
                        badClaimZr.loanAmount = rs.getBigDecimal(3);
                        badClaimZr.yearRate = rs.getBigDecimal(4);
                        badClaimZr.hkfs = T6230_F10.parse(rs.getString(5));
                        badClaimZr.bidNo = rs.getString(9);
                        badClaimZr.loanName = rs.getString(11);
                        badClaimZr.syPeriod = rs.getInt(12);
                        badClaimZr.zPeriods = rs.getInt(13);
                        badClaimZr.lateDays = rs.getInt(14);
                        badClaimZr.periodId = rs.getInt(15);
                        badClaimZr.principalAmount = rs.getBigDecimal(17);
                        badClaimZr.dhAmount = rs.getBigDecimal(18);
                        badClaimZr.userId = rs.getInt(19);
                        badClaimZr.claimAmount = rs.getBigDecimal(18);
                        Date refunDay = rs.getDate(16);
                        if (refunDay != null)
                        {
                            yqCal = Calendar.getInstance();
                            yqCal.setTime(refunDay);
                            yqCal.add(Calendar.DAY_OF_MONTH, 1);
                            badClaimZr.dueTime = yqCal.getTime();
                        }
                        
                        if (T6230_F11.S.name().equals(rs.getString(6)))
                        {
                            badClaimZr.loanAttribute = "担保标";
                        }
                        else if (T6230_F13.S.name().equals(rs.getString(7)))
                        {
                            badClaimZr.loanAttribute = "抵押标";
                        }
                        else if (T6230_F14.S.name().equals(rs.getString(8)))
                        {
                            badClaimZr.loanAttribute = "实地标";
                        }
                        else if (T6230_F33.S.name().equals(rs.getString(10)))
                        {
                            badClaimZr.loanAttribute = "信用标";
                        }
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(badClaimZr);
                        
                    }
                    return list == null ? null : list.toArray(new BadClaimZr[list.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    private void badClaimDzrSearchParameter(StringBuilder sql, DzrQuery dzrQuery, List<Object> parameters)
        throws Throwable
    {
        SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
        String query = dzrQuery.getBidNo();
        if (!StringHelper.isEmpty(query))
        {
            sql.append(" AND T6230.F25 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(query));
        }
        query = dzrQuery.getLoanTitle();
        if (!StringHelper.isEmpty(query))
        {
            sql.append(" AND T6230.F03 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(query));
        }
        query = dzrQuery.getLoanName();
        if (!StringHelper.isEmpty(query))
        {
            sql.append(" AND T6110.F02 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(query));
        }
        
        query = dzrQuery.getYuqiFromDays();
        if (!StringHelper.isEmpty(query))
        {
            sql.append(" AND DATEDIFF(CURRENT_DATE(), a.F08) >= ?");
            parameters.add(query);
        }
        
        query = dzrQuery.getYuqiEndDays();
        if (!StringHelper.isEmpty(query))
        {
            sql.append(" AND DATEDIFF(CURRENT_DATE(), a.F08) <= ?");
            parameters.add(query);
        }
        
        query = dzrQuery.getLoanAttribute();
        if (!StringHelper.isEmpty(query))
        {
            if ("dbb".equals(query))
            {
                sql.append(" AND T6230.F11 = ? ");
                parameters.add(T6230_F11.S.name());
            }
            if ("dyb".equals(query))
            {
                sql.append(" AND T6230.F13 = ? ");
                parameters.add(T6230_F13.S.name());
            }
            if ("sdb".equals(query))
            {
                sql.append(" AND T6230.F14 = ? ");
                parameters.add(T6230_F14.S.name());
            }
            if ("xyb".equals(query))
            {
                sql.append(" AND T6230.F33 = ? ");
                parameters.add(T6230_F33.S.name());
            }
        }
    }
    
    @Override
    public BadClaimZrDetails getBadClaimZrDetailsr(int bidId)
        throws Throwable
    {
        final StringBuilder sql =
            new StringBuilder(
                "SELECT T6230.F03 F01,T6230.F11 F02,T6230.F12 F03,T6110.F02 F04,(SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F09 ='WH' AND T6252.F05 IN ( '7001', '7002', '7003', '7004' )) F05,");
        sql.append("(CASE T6230.F11 WHEN 'S' THEN CASE T6230.F12 WHEN 'BXQEDB' THEN (SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F09 ='WH' AND T6252.F05 IN ( '7001', '7002', '7003', '7004' )) ");
        sql.append("ELSE (SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F09 ='WH' AND T6252.F05 = '7001') ");
        sql.append("END ELSE (SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F09 ='WH' AND T6252.F05 IN ( '7001', '7002', '7003', '7004' )) ");
        sql.append("END) F06,T6161.F04 F07,T6141.F02 F08 FROM S62.T6230 JOIN S61.T6110 ON T6230.F02 = T6110.F01 LEFT JOIN S62.T6236 ON T6230.F01=T6236.F02 LEFT JOIN S61.T6161 ON T6236.F03=T6161.F01 LEFT JOIN S61.T6141 ON T6236.F03=T6141.F01 WHERE T6230.F01=?");
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<BadClaimZrDetails>()
            {
                @Override
                public BadClaimZrDetails parse(ResultSet rs)
                    throws SQLException
                {
                    BadClaimZrDetails badClaimZrDetails = null;
                    String miga = "";
                    if (rs.next())
                    {
                        badClaimZrDetails = new BadClaimZrDetails();
                        badClaimZrDetails.loanTitle = rs.getString(1);
                        badClaimZrDetails.F11 = T6230_F11.parse(rs.getString(2));
                        badClaimZrDetails.F12 = T6230_F12.parse(rs.getString(3));
                        badClaimZrDetails.loanName = rs.getString(4);
                        badClaimZrDetails.claimAmount = rs.getBigDecimal(5);
                        badClaimZrDetails.transferAmount = rs.getBigDecimal(6);
                        miga = rs.getString(7);
                        if (StringHelper.isEmpty(miga))
                        {
                            badClaimZrDetails.miga = rs.getString(8);
                        }
                        else
                        {
                            badClaimZrDetails.miga = miga;
                        }
                        
                    }
                    return badClaimZrDetails;
                }
            }, sql.toString(), bidId);
        }
    }
    
    @Override
    public void transfer(int bidId, int periodId)
        throws Throwable
    {
        if (bidId <= 0 || periodId <= 0)
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
            Timestamp newTime = getCurrentTimestamp(connection);
            Date newDate = new Date(newTime.getTime());
            BidInfo bidInfo = getBidInfo(connection, newDate, periodId);
            if (null == bidInfo)
            {
                throw new LogicalException("不良债权不存在");
            }
            int count = getExistNum(connection, periodId);
            if (count > 0)
            {
                throw new LogicalException("不良债权不是待转让状态");
            }
            insertT6264(connection, newDate, newTime, bidId, periodId);
        }
    }
    
    private BidInfo getBidInfo(Connection connection, Date newDate, int periodId)
        throws Throwable
    {
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(periodId);
        parameters.add(newDate);
        parameters.add(T6252_F09.WH.name());
        parameters.add(newDate);
        parameters.add(serviceResource.getResource(ConfigureProvider.class).getProperty(BadClaimVariavle.BLZQZR_YQ_DAY));
        return select(connection,
            new ItemParser<BidInfo>()
            {
                @Override
                public BidInfo parse(ResultSet rs)
                    throws SQLException
                {
                    BidInfo bidInfo = null;
                    if (rs.next())
                    {
                        bidInfo = new BidInfo();
                        bidInfo.id = rs.getInt(1);
                        bidInfo.loanTitle = rs.getString(2);
                    }
                    return bidInfo;
                }
            },
            "SELECT T6230.F01,T6230.F03 FROM S62.T6230 JOIN S62.T6252 ON T6230.F01=T6252.F02 WHERE T6230.F20 <> 'YDF' AND T6252.F01=? AND T6252.F08 < ? AND T6252.F09 = ? AND ? >= DATE_ADD(T6252.F08, INTERVAL ? DAY ) LIMIT 1",
            parameters);
    }
    
    private BidInfo getBidInfoNotYqDay(Connection connection, Date newDate, int periodId)
        throws Throwable
    {
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(periodId);
        parameters.add(newDate);
        parameters.add(T6252_F09.WH.name());
        return select(connection,
            new ItemParser<BidInfo>()
            {
                @Override
                public BidInfo parse(ResultSet rs)
                    throws SQLException
                {
                    BidInfo bidInfo = null;
                    if (rs.next())
                    {
                        bidInfo = new BidInfo();
                        bidInfo.id = rs.getInt(1);
                        bidInfo.loanTitle = rs.getString(2);
                    }
                    return bidInfo;
                }
            },
            "SELECT T6230.F01,T6230.F03 FROM S62.T6230 JOIN S62.T6252 ON T6230.F01=T6252.F02 WHERE T6230.F20 <> 'YDF' AND T6252.F01=? AND T6252.F08 < ? AND T6252.F09 = ? LIMIT 1",
            parameters);
    }
    
    private Integer getT6264Count(Connection connection, Date newDate)
        throws Throwable
    {
        return select(connection, new ItemParser<Integer>()
        {
            @Override
            public Integer parse(ResultSet rs)
                throws SQLException
            {
                int count = 0;
                if (rs.next())
                {
                    count = rs.getInt(1);
                }
                return count;
            }
        }, "SELECT COUNT(1) FROM S62.T6264 WHERE DATE(T6264.F07)=?", newDate);
    }
    
    /**
     * <生成债权编号>
     * <功能详细描述>
     * @param connection
     * @param newDate
     * @return
     */
    private String getClaimNo(Connection connection, Date newDate)
        throws Throwable
    {
        final StringBuilder claimNo = new StringBuilder("ZQ");
        claimNo.append(DateTimeParser.format(newDate, "yyyyMMdd"));
        Integer count = getT6264Count(connection, newDate);
        if (count >= 1000)
        {
            claimNo.append(String.valueOf((count + 1)));
        }
        else if (count >= 100)
        {
            claimNo.append("0" + (count + 1));
        }
        else if (count >= 10)
        {
            claimNo.append("00" + (count + 1));
        }
        else if (count >= 0)
        {
            claimNo.append("000" + (count + 1));
        }
        return claimNo.toString();
    }
    
    private int insertT6264(Connection connection, Date newDate, Timestamp newTime, int bidId, int periodId)
        throws Throwable
    {
        return insert(connection,
            "INSERT INTO S62.T6264 SET F02 = ?, F03 = ?, F04 = ?, F06 = ?, F07 = ?, F08 = ?",
            getClaimNo(connection, newDate),
            bidId,
            T6264_F04.DSH.name(),
            periodId,
            newTime,
            newTime);
    }
    
    @Override
    public PagingResult<BadClaimDsh> badClaimDshSearch(DshQuery dshQuery, Paging paging)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            Date newDate = getCurrentDate(connection);
            ArrayList<Object> parameters = new ArrayList<>();
            final StringBuilder sql =
                new StringBuilder(
                    "SELECT T6230.F01 F01,T6230.F03 F02,(T6230.F05 - T6230.F07) F03,T6230.F06 F04,T6230.F10 F05,T6230.F11 F06,T6230.F13 F07,T6230.F14 F08,T6230.F25 F09,T6230.F33 F10,T6110.F02 F11,T6231.F03 F12,T6231.F02 F13,DATEDIFF(?, a.F08) F14,a.F01 F15,a.F08 F16,(SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F09 ='WH' AND T6252.F05 IN ( '7001', '7002', '7003', '7004' )) F17,");
            sql.append("(CASE T6230.F11 WHEN 'S' THEN CASE T6230.F12 WHEN 'BXQEDB' THEN (SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F09 ='WH' AND T6252.F05 IN ( '7001', '7002', '7003', '7004' )) ELSE (SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F09 ='WH' AND T6252.F05 = '7001') END ELSE (SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F09 ='WH' AND T6252.F05 IN ( '7001', '7002', '7003', '7004' )) END) F18,");
            sql.append("T6110.F01 F19,T6264.F01 F20,T6264.F07 F21,T6264.F02 F22 ");
            sql.append("FROM S62.T6252 a JOIN S62.T6231 ON a.F02=T6231.F01 JOIN S62.T6230 ON T6231.F01=T6230.F01 JOIN S61.T6110 ON T6230.F02 = T6110.F01 JOIN (SELECT MIN(b.F01) F01 FROM S62.T6252 b WHERE b.F08 < ? AND b.F09 = 'WH' AND b.F05 IN ( '7001', '7002', '7003', '7004' ) GROUP BY b.F02) bb ON a.F01=bb.F01 JOIN S62.T6264 ON a.F02=T6264.F03 WHERE T6264.F04 = ? ");
            parameters.add(newDate);
            parameters.add(newDate);
            parameters.add(dshQuery.getState());
            badClaimDshSearchParameter(sql, dshQuery, parameters, T6264_F04.DSH);
            sql.append(" ORDER BY T6264.F08 DESC");
            return selectPaging(connection, new ArrayParser<BadClaimDsh>()
            {
                
                @Override
                public BadClaimDsh[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<BadClaimDsh> list = null;
                    BadClaimDsh badClaimDsh = null;
                    Calendar yqCal = null;
                    while (rs.next())
                    {
                        
                        badClaimDsh = new BadClaimDsh();
                        badClaimDsh.bidId = rs.getInt(1);
                        badClaimDsh.loanTitle = rs.getString(2);
                        badClaimDsh.loanAmount = rs.getBigDecimal(3);
                        badClaimDsh.yearRate = rs.getBigDecimal(4);
                        badClaimDsh.hkfs = T6230_F10.parse(rs.getString(5));
                        badClaimDsh.bidNo = rs.getString(9);
                        badClaimDsh.loanName = rs.getString(11);
                        badClaimDsh.syPeriod = rs.getInt(12);
                        badClaimDsh.zPeriods = rs.getInt(13);
                        badClaimDsh.lateDays = rs.getInt(14);
                        badClaimDsh.periodId = rs.getInt(15);
                        badClaimDsh.claimAmount = rs.getBigDecimal(17);
                        badClaimDsh.transferAmount = rs.getBigDecimal(18);
                        badClaimDsh.userId = rs.getInt(19);
                        badClaimDsh.id = rs.getInt(20);
                        badClaimDsh.applyTime = rs.getTimestamp(21);
                        badClaimDsh.claimNo = rs.getString(22);
                        Date refunDay = rs.getDate(16);
                        if (refunDay != null)
                        {
                            yqCal = Calendar.getInstance();
                            yqCal.setTime(refunDay);
                            yqCal.add(Calendar.DAY_OF_MONTH, 1);
                            badClaimDsh.dueTime = yqCal.getTime();
                        }
                        
                        if (T6230_F11.S.name().equals(rs.getString(6)))
                        {
                            badClaimDsh.loanAttribute = "担保标";
                        }
                        else if (T6230_F13.S.name().equals(rs.getString(7)))
                        {
                            badClaimDsh.loanAttribute = "抵押标";
                        }
                        else if (T6230_F14.S.name().equals(rs.getString(8)))
                        {
                            badClaimDsh.loanAttribute = "实地标";
                        }
                        else if (T6230_F33.S.name().equals(rs.getString(10)))
                        {
                            badClaimDsh.loanAttribute = "信用标";
                        }
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(badClaimDsh);
                        
                    }
                    return list == null ? null : list.toArray(new BadClaimDsh[list.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    private void badClaimDshSearchParameter(StringBuilder sql, DshQuery dshQuery, List<Object> parameters, T6264_F04 F04)
        throws Throwable
    {
        SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
        String query = dshQuery.getClaimNo();
        if (!StringHelper.isEmpty(query))
        {
            sql.append(" AND T6264.F02 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(query));
        }
        query = dshQuery.getBidNo();
        if (!StringHelper.isEmpty(query))
        {
            sql.append(" AND T6230.F25 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(query));
        }
        query = dshQuery.getLoanTitle();
        if (!StringHelper.isEmpty(query))
        {
            sql.append(" AND T6230.F03 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(query));
        }
        query = dshQuery.getLoanName();
        if (!StringHelper.isEmpty(query))
        {
            sql.append(" AND T6110.F02 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(query));
        }
        
        query = dshQuery.getYuqiFromDays();
        if (!StringHelper.isEmpty(query))
        {
            if (F04 == T6264_F04.DSH)
            {
                sql.append(" AND DATEDIFF(CURRENT_DATE(), a.F08) >= ?");
                parameters.add(query);
            }
            else if (F04 == T6264_F04.ZRSB)
            {
                sql.append(" AND T6264.F05 >= ?");
                parameters.add(query);
            }
        }
        
        query = dshQuery.getYuqiEndDays();
        if (!StringHelper.isEmpty(query))
        {
            if (F04 == T6264_F04.DSH)
            {
                sql.append(" AND DATEDIFF(CURRENT_DATE(), a.F08) <= ?");
                parameters.add(query);
            }
            else if (F04 == T6264_F04.ZRSB)
            {
                sql.append(" AND T6264.F05 <= ?");
                parameters.add(query);
            }
        }
        
        query = dshQuery.getLoanAttribute();
        if (!StringHelper.isEmpty(query))
        {
            if ("dbb".equals(query))
            {
                sql.append(" AND T6230.F11 = ? ");
                parameters.add(T6230_F11.S.name());
            }
            if ("dyb".equals(query))
            {
                sql.append(" AND T6230.F13 = ? ");
                parameters.add(T6230_F13.S.name());
            }
            if ("sdb".equals(query))
            {
                sql.append(" AND T6230.F14 = ? ");
                parameters.add(T6230_F14.S.name());
            }
            if ("xyb".equals(query))
            {
                sql.append(" AND T6230.F33 = ? ");
                parameters.add(T6230_F33.S.name());
            }
        }
        Date time = dshQuery.getStartTime();
        if (null != time)
        {
            sql.append(" AND DATE(T6264.F07) >= ? ");
            parameters.add(time);
        }
        time = dshQuery.getEndTime();
        if (null != time)
        {
            sql.append(" AND DATE(T6264.F07) <= ? ");
            parameters.add(time);
        }
    }
    
    @Override
    public T6264 getT6264ByF01(int id)
        throws Throwable
    {
        final StringBuilder sql = new StringBuilder("SELECT T6264.F07 F01 FROM S62.T6264 WHERE T6264.F01=?");
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<T6264>()
            {
                @Override
                public T6264 parse(ResultSet rs)
                    throws SQLException
                {
                    T6264 t6264 = null;
                    if (rs.next())
                    {
                        t6264 = new T6264();
                        t6264.F07 = rs.getTimestamp(1);
                    }
                    return t6264;
                }
            }, sql.toString(), id);
        }
    }
    
    private T6264 getT6264ForUpdate(Connection connection, int id)
        throws Throwable
    {
        return select(connection, new ItemParser<T6264>()
        {
            @Override
            public T6264 parse(ResultSet rs)
                throws SQLException
            {
                T6264 t6264 = null;
                if (rs.next())
                {
                    t6264 = new T6264();
                    t6264.F03 = rs.getInt(1);
                    t6264.F04 = T6264_F04.parse(rs.getString(2));
                    t6264.F06 = rs.getInt(3);
                }
                return t6264;
            }
        }, "SELECT T6264.F03 F01,T6264.F04 F02,T6264.F06 F03 FROM S62.T6264 WHERE T6264.F01=? FOR UPDATE", id);
    }
    
    /**
     * 根据状态查询已有的数量
     * @param connection
     * @param F06
     * @return
     * @throws Throwable
     */
    private int getExistNum(Connection connection, int F06)
        throws Throwable
    {
        return select(connection, new ItemParser<Integer>()
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
        }, "SELECT COUNT(1) FROM S62.T6264 WHERE T6264.F04 NOT IN ('ZRSB','YXJ') AND T6264.F06 = ? LIMIT 1", F06);
    }
    
    @Override
    public void check(T6264 t6264)
        throws Throwable
    {
        int id = t6264.F01;
        T6264_F04 F04 = t6264.F04;
        if (id <= 0 || (T6264_F04.ZRZ != F04 && T6264_F04.ZRSB != F04))
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                T6264 t6264q = getT6264ForUpdate(connection, id);
                if (null == t6264q)
                {
                    throw new LogicalException("不良债权转让申请不存在");
                }
                if (T6264_F04.DSH != t6264q.F04)
                {
                    throw new LogicalException("不良债权转让申请不是待审核状态");
                }
                Timestamp newTime = getCurrentTimestamp(connection);
                Date newDate = new Date(newTime.getTime());
                BidInfo bidInfo = getBidInfoNotYqDay(connection, newDate, t6264q.F06);
                if (null == bidInfo)
                {
                    throw new LogicalException("不良债权不存在");
                }
                
                //审核通过，发送站内信，短信
                if (T6264_F04.ZRZ == F04)
                {
                    updateT6264F04(connection, id, t6264, newTime);
                    send(connection, bidInfo.id, bidInfo.loanTitle);
                }
                else if (T6264_F04.ZRSB == F04)
                {
                    updateT6264F04F05F09F10F11(connection, id, t6264, newTime);
                }
                serviceResource.commit(connection);
            }
            catch (Throwable e)
            {
                serviceResource.rollback(connection);
                logger.error("BadClaimTransferManageImpl.check()", e);
                throw e;
            }
            
        }
    }
    
    private void updateT6264F04(Connection connection, int id, T6264 t6264, Timestamp newTime)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S62.T6264 SET F04 = ?, F08 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, t6264.F04.name());
            pstmt.setTimestamp(2, newTime);
            pstmt.setInt(3, id);
            pstmt.execute();
        }
    }
    
    private void updateT6264F04F05F09F10F11(Connection connection, int id, T6264 t6264, Timestamp newTime)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S62.T6264 SET F04 = ?, F05 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, t6264.F04.name());
            pstmt.setInt(2, t6264.F05);
            pstmt.setTimestamp(3, newTime);
            pstmt.setBigDecimal(4, t6264.F09);
            pstmt.setBigDecimal(5, t6264.F10);
            pstmt.setString(6, t6264.F11);
            pstmt.setInt(7, id);
            pstmt.execute();
        }
    }
    
    private void send(Connection connection, int bidId, String title)
        throws Throwable
    {
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        List<T6110> t6110List = getT6110List(connection, bidId);
        Envionment envionment = configureProvider.createEnvionment();
        envionment.set("title", title);
        for (T6110 t6110 : t6110List)
        {
            envionment.set("name", t6110.F02);
            String content = configureProvider.format(LetterVariable.BLZQZRTX_SH, envionment);
            // 发站内信
            sendLetter(connection, t6110.F01, "不良债权审核通过", content);
            // 发短信
            sendMsg(connection, t6110.F04, configureProvider.format(MsgVariavle.BLZQZRTX_SH, envionment));
        }
    }
    
    private List<T6110> getT6110List(Connection connection, int bidId)
        throws Throwable
    {
        
        List<T6110> list = new ArrayList<T6110>();
        try (PreparedStatement pst =
            connection.prepareStatement("SELECT T6110.F01,T6110.F02,T6110.F04 FROM S61.T6110 WHERE T6110.F10=? AND T6110.F01 NOT IN (SELECT T6236.F03 FROM S62.T6236 WHERE T6236.F02=?) AND T6110.F19 = ? "))
        {
            pst.setString(1, T6110_F10.S.name());
            pst.setInt(2, bidId);
            pst.setString(3, T6110_F19.S.name());
            try (ResultSet rs = pst.executeQuery())
            {
                T6110 t6110 = null;
                while (rs.next())
                {
                    t6110 = new T6110();
                    t6110.F01 = rs.getInt(1);
                    t6110.F02 = rs.getString(2);
                    t6110.F04 = rs.getString(3);
                    list.add(t6110);
                }
            }
        }
        return list;
    }
    
    @Override
    public void ban(T6264 t6264)
        throws Throwable
    {
        int id = t6264.F01;
        T6264_F04 F04 = t6264.F04;
        if (id <= 0 || T6264_F04.YXJ != F04)
        {
            throw new ParameterException("参数错误");
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                
                T6264 t6264q = getT6264ForUpdate(connection, id);
                if (null == t6264q)
                {
                    throw new LogicalException("不良债权转让申请不存在");
                }
                if (T6264_F04.ZRZ != t6264q.F04)
                {
                    throw new LogicalException("不良债权转让申请不是转让中状态");
                }
                Timestamp newTime = getCurrentTimestamp(connection);
                Date newDate = new Date(newTime.getTime());
                BidInfo bidInfo = getBidInfoNotYqDay(connection, newDate, t6264q.F06);
                if (null == bidInfo)
                {
                    throw new LogicalException("不良债权不存在");
                }
                
                if (!checkBadClaim(connection, id))
                {
                    throw new LogicalException("不良债权已被机构购买，不能进行下架操作！");
                }
                
                updateT6264F04F05F09F10F11(connection, id, t6264, newTime);
                
                serviceResource.commit(connection);
            }
            catch (Throwable e)
            {
                serviceResource.rollback(connection);
                logger.error("BadClaimTransferManageImpl.ban()", e);
                throw e;
            }
        }
        
    }
    
    /** 
     * 校验债权是否已被购买
     * @param connection
     * @param id
     * @return
     * @throws Throwable
     */
    private boolean checkBadClaim(Connection connection, int id)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T6529.F04 FROM S65.T6529 LEFT JOIN S65.T6501 ON T6501.F01 = T6529.F01 WHERE T6529.F03 = ?  AND T6501.F02 = ? AND T6501.F03 = ? "))
        {
            pstmt.setInt(1, id);
            pstmt.setInt(2, OrderType.BUY_BAD_CLAIM.orderType());
            pstmt.setString(3, T6501_F03.CG.name());
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
    public PagingResult<BadClaimDsh> badClaimZrsbSearch(DshQuery dshQuery, Paging paging)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<Object> parameters = new ArrayList<>();
            final StringBuilder sql =
                new StringBuilder(
                    "SELECT T6230.F01 F01,T6230.F03 F02,(T6230.F05 - T6230.F07) F03,T6230.F06 F04,T6230.F10 F05,T6230.F11 F06,T6230.F13 F07,T6230.F14 F08,T6230.F25 F09,T6230.F33 F10,T6110.F02 F11,");
            sql.append("a.F06 F12,T6231.F02 F13,T6264.F05 F14,a.F01 F15,a.F08 F16,T6264.F09 F17,T6264.F10 F18,T6110.F01 F19,T6264.F01 F20,T6264.F07 F21,T6264.F02 F22 ");
            sql.append("FROM S62.T6252 a JOIN S62.T6231 ON a.F02=T6231.F01 JOIN S62.T6230 ON T6231.F01=T6230.F01 JOIN S61.T6110 ON T6230.F02 = T6110.F01 JOIN S62.T6264 ON a.F01=T6264.F06 WHERE T6264.F04 IN (?,?) ");
            parameters.add(T6264_F04.YXJ.name());
            parameters.add(T6264_F04.ZRSB.name());
            badClaimDshSearchParameter(sql, dshQuery, parameters, T6264_F04.ZRSB);
            sql.append(" ORDER BY T6264.F08 DESC");
            return selectPaging(connection, new ArrayParser<BadClaimDsh>()
            {
                
                @Override
                public BadClaimDsh[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<BadClaimDsh> list = null;
                    BadClaimDsh badClaimDsh = null;
                    Calendar yqCal = null;
                    while (rs.next())
                    {
                        
                        badClaimDsh = new BadClaimDsh();
                        badClaimDsh.bidId = rs.getInt(1);
                        badClaimDsh.loanTitle = rs.getString(2);
                        badClaimDsh.loanAmount = rs.getBigDecimal(3);
                        badClaimDsh.yearRate = rs.getBigDecimal(4);
                        badClaimDsh.hkfs = T6230_F10.parse(rs.getString(5));
                        badClaimDsh.bidNo = rs.getString(9);
                        badClaimDsh.loanName = rs.getString(11);
                        badClaimDsh.syPeriod = rs.getInt(13) - rs.getInt(12) + 1;
                        badClaimDsh.zPeriods = rs.getInt(13);
                        badClaimDsh.lateDays = rs.getInt(14);
                        badClaimDsh.periodId = rs.getInt(15);
                        badClaimDsh.claimAmount = rs.getBigDecimal(17);
                        badClaimDsh.transferAmount = rs.getBigDecimal(18);
                        badClaimDsh.userId = rs.getInt(19);
                        badClaimDsh.id = rs.getInt(20);
                        badClaimDsh.applyTime = rs.getTimestamp(21);
                        badClaimDsh.claimNo = rs.getString(22);
                        Date refunDay = rs.getDate(16);
                        if (refunDay != null)
                        {
                            yqCal = Calendar.getInstance();
                            yqCal.setTime(refunDay);
                            yqCal.add(Calendar.DAY_OF_MONTH, 1);
                            badClaimDsh.dueTime = yqCal.getTime();
                        }
                        
                        if (T6230_F11.S.name().equals(rs.getString(6)))
                        {
                            badClaimDsh.loanAttribute = "担保标";
                        }
                        else if (T6230_F13.S.name().equals(rs.getString(7)))
                        {
                            badClaimDsh.loanAttribute = "抵押标";
                        }
                        else if (T6230_F14.S.name().equals(rs.getString(8)))
                        {
                            badClaimDsh.loanAttribute = "实地标";
                        }
                        else if (T6230_F33.S.name().equals(rs.getString(10)))
                        {
                            badClaimDsh.loanAttribute = "信用标";
                        }
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(badClaimDsh);
                        
                    }
                    return list == null ? null : list.toArray(new BadClaimDsh[list.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public PagingResult<BadClaimYzr> badClaimYzrSearch(YzrQuery yzrQuery, Paging paging)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<Object> parameters = new ArrayList<>();
            final StringBuilder sql =
                new StringBuilder(
                    "SELECT T6230.F01 F01,T6230.F03 F02,(T6230.F05 - T6230.F07) F03,T6230.F06 F04,T6230.F10 F05,T6230.F11 F06,T6230.F13 F07,T6230.F14 F08,T6230.F25 F09,T6230.F33 F10,T6110.F02 F11,");
            sql.append("T6231.F03 F12,T6231.F02 F13,T6264.F05 F14,a.F01 F15,a.F08 F16,T6265.F05 F17,T6264.F10 F18,T6264.F02 F19,T6265.F07 F20,T6161.F04 F21, T6230.F02 AS F22 ");
            sql.append("FROM S62.T6252 a JOIN S62.T6231 ON a.F02=T6231.F01 JOIN S62.T6230 ON T6231.F01=T6230.F01 JOIN S61.T6110 ON T6230.F02 = T6110.F01 JOIN S62.T6264 ON a.F01=T6264.F06 JOIN S62.T6265 ON T6264.F01=T6265.F02 JOIN S61.T6161 ON T6265.F03=T6161.F01 WHERE T6264.F04=? ");
            parameters.add(T6264_F04.YZR.name());
            badClaimYzrSearchParameter(sql, yzrQuery, parameters);
            sql.append(" ORDER BY T6264.F08 DESC");
            return selectPaging(connection, new ArrayParser<BadClaimYzr>()
            {
                
                @Override
                public BadClaimYzr[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<BadClaimYzr> list = null;
                    BadClaimYzr badClaimYzr = null;
                    Calendar yqCal = null;
                    while (rs.next())
                    {
                        
                        badClaimYzr = new BadClaimYzr();
                        badClaimYzr.bidId = rs.getInt(1);
                        badClaimYzr.loanTitle = rs.getString(2);
                        badClaimYzr.loanAmount = rs.getBigDecimal(3);
                        badClaimYzr.yearRate = rs.getBigDecimal(4);
                        badClaimYzr.hkfs = T6230_F10.parse(rs.getString(5));
                        badClaimYzr.bidNo = rs.getString(9);
                        badClaimYzr.loanName = rs.getString(11);
                        badClaimYzr.syPeriod = rs.getInt(12);
                        badClaimYzr.zPeriods = rs.getInt(13);
                        badClaimYzr.lateDays = rs.getInt(14);
                        badClaimYzr.periodId = rs.getInt(15);
                        badClaimYzr.claimAmount = rs.getBigDecimal(17);
                        badClaimYzr.transferAmount = rs.getBigDecimal(18);
                        badClaimYzr.claimNo = rs.getString(19);
                        badClaimYzr.buyTime = rs.getTimestamp(20);
                        badClaimYzr.claimReceiver = rs.getString(21);
                        Date refunDay = rs.getDate(16);
                        if (refunDay != null)
                        {
                            yqCal = Calendar.getInstance();
                            yqCal.setTime(refunDay);
                            yqCal.add(Calendar.DAY_OF_MONTH, 1);
                            badClaimYzr.dueTime = yqCal.getTime();
                        }
                        
                        if (T6230_F11.S.name().equals(rs.getString(6)))
                        {
                            badClaimYzr.loanAttribute = "担保标";
                        }
                        else if (T6230_F13.S.name().equals(rs.getString(7)))
                        {
                            badClaimYzr.loanAttribute = "抵押标";
                        }
                        else if (T6230_F14.S.name().equals(rs.getString(8)))
                        {
                            badClaimYzr.loanAttribute = "实地标";
                        }
                        else if (T6230_F33.S.name().equals(rs.getString(10)))
                        {
                            badClaimYzr.loanAttribute = "信用标";
                        }
                        badClaimYzr.userId = rs.getInt(22);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(badClaimYzr);
                        
                    }
                    return list == null ? null : list.toArray(new BadClaimYzr[list.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    private void badClaimYzrSearchParameter(StringBuilder sql, YzrQuery yzrQuery, List<Object> parameters)
        throws Throwable
    {
        SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
        String query = yzrQuery.getClaimNo();
        if (!StringHelper.isEmpty(query))
        {
            sql.append(" AND T6264.F02 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(query));
        }
        query = yzrQuery.getBidNo();
        if (!StringHelper.isEmpty(query))
        {
            sql.append(" AND T6230.F25 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(query));
        }
        query = yzrQuery.getLoanTitle();
        if (!StringHelper.isEmpty(query))
        {
            sql.append(" AND T6230.F03 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(query));
        }
        query = yzrQuery.getLoanName();
        if (!StringHelper.isEmpty(query))
        {
            sql.append(" AND T6110.F02 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(query));
        }
        
        query = yzrQuery.getYuqiFromDays();
        if (!StringHelper.isEmpty(query))
        {
            sql.append(" AND T6264.F05 >= ?");
            parameters.add(query);
        }
        
        query = yzrQuery.getYuqiEndDays();
        if (!StringHelper.isEmpty(query))
        {
            sql.append(" AND T6264.F05 <= ?");
            parameters.add(query);
        }
        
        query = yzrQuery.getLoanAttribute();
        if (!StringHelper.isEmpty(query))
        {
            if ("dbb".equals(query))
            {
                sql.append(" AND T6230.F11 = ? ");
                parameters.add(T6230_F11.S.name());
            }
            if ("dyb".equals(query))
            {
                sql.append(" AND T6230.F13 = ? ");
                parameters.add(T6230_F13.S.name());
            }
            if ("sdb".equals(query))
            {
                sql.append(" AND T6230.F14 = ? ");
                parameters.add(T6230_F14.S.name());
            }
            if ("xyb".equals(query))
            {
                sql.append(" AND T6230.F33 = ? ");
                parameters.add(T6230_F33.S.name());
            }
        }
        int claimReceiver = yzrQuery.getClaimReceiver();
        if (claimReceiver > 0)
        {
            sql.append(" AND T6265.F03 = ?");
            parameters.add(claimReceiver);
        }
        Date time = yzrQuery.getStartTime();
        if (null != time)
        {
            sql.append(" AND DATE(T6265.F07) >= ? ");
            parameters.add(time);
        }
        time = yzrQuery.getEndTime();
        if (null != time)
        {
            sql.append(" AND DATE(T6265.F07) <= ? ");
            parameters.add(time);
        }
    }
    
    @Override
    public List<T6161> getClaimReceiverList()
        throws Throwable
    {
        List<T6161> list = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pst =
                connection.prepareStatement("SELECT DISTINCT T6161.F01 F01,T6161.F04 F02 FROM S61.T6161 JOIN S62.T6265 ON T6161.F01=T6265.F03 JOIN S62.T6264 ON T6265.F02=T6264.F01 WHERE T6264.F04=?"))
            {
                pst.setString(1, T6264_F04.YZR.name());
                try (ResultSet rs = pst.executeQuery())
                {
                    T6161 t6161 = null;
                    while (rs.next())
                    {
                        if (list == null)
                        {
                            list = new ArrayList<T6161>();
                        }
                        t6161 = new T6161();
                        t6161.F01 = rs.getInt(1);
                        t6161.F04 = rs.getString(2);
                        list.add(t6161);
                    }
                }
            }
            return list;
        }
    }
    
    @Override
    public void exportBlzqDzr(BadClaimZr[] badClaimZrs, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (badClaimZrs == null)
        {
            return;
        }
        if (StringHelper.isEmpty(charset))
        {
            charset = "GBK";
        }
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, charset)))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("借款编号");
            writer.write("标的属性");
            writer.write("借款标题");
            writer.write("借款账户");
            writer.write("借款金额(元)");
            writer.write("还款方式");
            writer.write("年化利率");
            writer.write("期数");
            writer.write("逾期时间");
            writer.write("逾期天数");
            writer.write("债权价值(元)");
            writer.write("应还金额(元)");
            writer.write("待还金额(元)");
            writer.newLine();
            int index = 1;
            for (BadClaimZr badClaimZr : badClaimZrs)
            {
                writer.write(index++);
                writer.write(badClaimZr.bidNo);
                writer.write(badClaimZr.loanAttribute);
                writer.write(badClaimZr.loanTitle);
                writer.write(badClaimZr.loanName);
                writer.write(badClaimZr.loanAmount);
                writer.write(badClaimZr.hkfs.getChineseName());
                writer.write(Formater.formatRate(badClaimZr.yearRate));
                writer.write(badClaimZr.syPeriod + "/" + badClaimZr.zPeriods + "\t");
                writer.write(Formater.formatDate(badClaimZr.dueTime) + "\t");
                writer.write(badClaimZr.lateDays);
                writer.write(badClaimZr.claimAmount);
                writer.write(badClaimZr.principalAmount);
                writer.write(badClaimZr.dhAmount);
                writer.newLine();
            }
        }
    }
    
    @Override
    public void exportBlzqDsh(BadClaimDsh[] badClaimDshs, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (badClaimDshs == null)
        {
            return;
        }
        if (StringHelper.isEmpty(charset))
        {
            charset = "GBK";
        }
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, charset)))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("债权编号");
            writer.write("借款编号");
            writer.write("标的属性");
            writer.write("借款标题");
            writer.write("借款账户");
            writer.write("借款金额(元)");
            writer.write("还款方式");
            writer.write("期数");
            writer.write("逾期时间");
            writer.write("逾期天数");
            writer.write("债权价值(元)");
            writer.write("转让价格(元)");
            writer.write("申请时间");
            writer.newLine();
            int index = 1;
            for (BadClaimDsh badClaimDsh : badClaimDshs)
            {
                writer.write(index++);
                writer.write(badClaimDsh.claimNo);
                writer.write(badClaimDsh.bidNo);
                writer.write(badClaimDsh.loanAttribute);
                writer.write(badClaimDsh.loanTitle);
                writer.write(badClaimDsh.loanName);
                writer.write(badClaimDsh.loanAmount);
                writer.write(badClaimDsh.hkfs.getChineseName());
                writer.write(badClaimDsh.syPeriod + "/" + badClaimDsh.zPeriods + "\t");
                writer.write(Formater.formatDate(badClaimDsh.dueTime) + "\t");
                writer.write(badClaimDsh.lateDays);
                writer.write(badClaimDsh.claimAmount);
                writer.write(badClaimDsh.transferAmount);
                writer.write(TimestampParser.format(badClaimDsh.applyTime) + "\t");
                writer.newLine();
            }
        }
    }
    
    @Override
    public void exportBlzqZrz(BadClaimDsh[] badClaimDshs, OutputStream outputStream, String charset)
        throws Throwable
    {
        exportBlzqDsh(badClaimDshs, outputStream, charset);
    }
    
    @Override
    public void exportBlzqYzr(BadClaimYzr[] badClaimYzrs, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (badClaimYzrs == null)
        {
            return;
        }
        if (StringHelper.isEmpty(charset))
        {
            charset = "GBK";
        }
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, charset)))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("债权编号");
            writer.write("借款编号");
            writer.write("标的属性");
            writer.write("借款标题");
            writer.write("借款账户");
            writer.write("借款金额(元)");
            writer.write("还款方式");
            writer.write("期数");
            writer.write("逾期时间");
            writer.write("逾期天数");
            writer.write("债权价值(元)");
            writer.write("购买价格(元)");
            writer.write("债权接收方");
            writer.write("购买时间");
            writer.newLine();
            int index = 1;
            for (BadClaimYzr badClaimYzr : badClaimYzrs)
            {
                writer.write(index++);
                writer.write(badClaimYzr.claimNo);
                writer.write(badClaimYzr.bidNo);
                writer.write(badClaimYzr.loanAttribute);
                writer.write(badClaimYzr.loanTitle);
                writer.write(badClaimYzr.loanName);
                writer.write(badClaimYzr.loanAmount);
                writer.write(badClaimYzr.hkfs.getChineseName());
                writer.write(badClaimYzr.syPeriod + "/" + badClaimYzr.zPeriods + "\t");
                writer.write(Formater.formatDate(badClaimYzr.dueTime) + "\t");
                writer.write(badClaimYzr.lateDays);
                writer.write(badClaimYzr.claimAmount);
                writer.write(badClaimYzr.transferAmount);
                writer.write(badClaimYzr.claimReceiver);
                writer.write(TimestampParser.format(badClaimYzr.buyTime) + "\t");
                writer.newLine();
            }
        }
    }
    
    @Override
    public void exportBlzqZrsb(BadClaimDsh[] badClaimDshs, OutputStream outputStream, String charset)
        throws Throwable
    {
        exportBlzqDsh(badClaimDshs, outputStream, charset);
    }
    
    @Override
    public BadAssets getBadAssetsTotal(YzrQuery query)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<Object> parameters = new ArrayList<>();
            final StringBuilder sql = new StringBuilder("SELECT IFNULL(SUM(a.F01),0),IFNULL(SUM(a.F02),0) FROM");
            sql.append(" (SELECT T6265.F05 F01,T6265.F06 F02 FROM S62.T6252 a JOIN S62.T6231 ON a.F02=T6231.F01 JOIN S62.T6230 ON T6231.F01=T6230.F01 JOIN S61.T6110 ON T6230.F02 = T6110.F01 JOIN S62.T6264 ON a.F01=T6264.F06 JOIN S62.T6265 ON T6264.F01=T6265.F02 JOIN S61.T6161 ON T6265.F03=T6161.F01 WHERE T6264.F04=? ");
            parameters.add(T6264_F04.YZR.name());
            badClaimYzrSearchParameter(sql, query, parameters);
            sql.append(") a LIMIT 1");
            return select(connection, new ItemParser<BadAssets>()
            {
                @Override
                public BadAssets parse(ResultSet resultSet)
                    throws SQLException
                {
                    BadAssets badAssets = new BadAssets();
                    if (resultSet.next())
                    {
                        badAssets.claimAmountTatal = resultSet.getBigDecimal(1);
                        badAssets.transferAmountTatal = resultSet.getBigDecimal(2);
                    }
                    return badAssets;
                }
            }, sql.toString(), parameters);
        }
    }
    
    @Override
    public BadClaimShDetails getBadClaimShDetailsr(int id)
        throws Throwable
    {
        final StringBuilder sql =
            new StringBuilder(
                "SELECT T6230.F03 F01,T6230.F11 F02,T6230.F12 F03,T6110.F02 F04,(SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F09 ='WH' AND T6252.F05 IN ( '7001', '7002', '7003', '7004' )) F05,");
        sql.append("(CASE T6230.F11 WHEN 'S' THEN CASE T6230.F12 WHEN 'BXQEDB' THEN (SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F09 ='WH' AND T6252.F05 IN ( '7001', '7002', '7003', '7004' )) ");
        sql.append("ELSE (SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F09 ='WH' AND T6252.F05 = '7001') ");
        sql.append("END ELSE (SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F09 ='WH' AND T6252.F05 IN ( '7001', '7002', '7003', '7004' )) ");
        sql.append("END) F06,T6161.F04 F07,(SELECT DATEDIFF(?, T6252.F08) FROM S62.T6252 WHERE T6252.F01 = T6264.F06) F08,T6264.F07 F09,T6141.F02 F10 FROM S62.T6230 JOIN S61.T6110 ON T6230.F02 = T6110.F01 LEFT JOIN S62.T6236 ON T6230.F01=T6236.F02 LEFT JOIN S61.T6161 ON T6236.F03=T6161.F01 LEFT JOIN S61.T6141 ON T6236.F03=T6141.F01 JOIN S62.T6264 ON T6264.F03=T6230.F01 WHERE T6264.F01=?");
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<BadClaimShDetails>()
            {
                @Override
                public BadClaimShDetails parse(ResultSet rs)
                    throws SQLException
                {
                    BadClaimShDetails badClaimShDetails = null;
                    String miga = "";
                    if (rs.next())
                    {
                        badClaimShDetails = new BadClaimShDetails();
                        badClaimShDetails.loanTitle = rs.getString(1);
                        badClaimShDetails.F11 = T6230_F11.parse(rs.getString(2));
                        badClaimShDetails.F12 = T6230_F12.parse(rs.getString(3));
                        badClaimShDetails.loanName = rs.getString(4);
                        badClaimShDetails.claimAmount = rs.getBigDecimal(5);
                        badClaimShDetails.transferAmount = rs.getBigDecimal(6);
                        miga = rs.getString(7);
                        if (StringHelper.isEmpty(miga))
                        {
                            badClaimShDetails.miga = rs.getString(10);
                        }
                        else
                        {
                            badClaimShDetails.miga = miga;
                        }
                        badClaimShDetails.lateDays = rs.getInt(8);
                        badClaimShDetails.applyTime = rs.getTimestamp(9);
                    }
                    return badClaimShDetails;
                }
            }, sql.toString(), getCurrentDate(connection), id);
        }
    }
}
