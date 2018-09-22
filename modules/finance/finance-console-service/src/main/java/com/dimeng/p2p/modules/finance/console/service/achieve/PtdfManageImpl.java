/*
 * 文 件 名:  CollectionManageImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  dingjinqing
 * 修改时间:  2015年3月12日
 */
package com.dimeng.p2p.modules.finance.console.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.enums.T5131_F02;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.modules.finance.console.service.PtdfManage;
import com.dimeng.p2p.modules.finance.console.service.entity.DfRecord;
import com.dimeng.p2p.modules.finance.console.service.query.DfQuery;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateParser;

public class PtdfManageImpl extends AbstractFinanceService implements PtdfManage
{
    
    /** <默认构造函数>
     */
    public PtdfManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
        // TODO Auto-generated constructor stub
    }
    
    /** {@inheritDoc} */
    
    @Override
    public PagingResult<DfRecord> yqddfSearch(final DfQuery yqddfQuery, Paging paging)
        throws Throwable
    {
        final StringBuilder sql = new StringBuilder();
        sql.append("SELECT T6230.F01 AS F01, T6230.F02 AS F02, T6230.F03 AS F03, T6230.F04 AS F04, T6230.F05 AS F05,"
            + " T6230.F06 AS F06, T6230.F07 AS F07, T6230.F08 AS F08, T6230.F09 AS F09, T6230.F10 AS F10, "
            + "T6230.F11 AS F11, T6230.F12 AS F12, T6230.F20 AS F13, T6230.F22 AS F14, T6230.F25 AS F15,"
            + " T6110.F02 AS F16, T6230.F05 - T6230.F07 AS F17, T6252.F06 AS F18, T6252.F08 AS F19, "
            + "T6231.F02 AS F20, T6231.F03 AS F21, T6231.F21 AS F22, DATEDIFF(CURRENT_DATE(), T6252.F08) "
            + "AS F23,T6231.F22 AS F24,T6110.F01 AS F25 "
            + " FROM S62.T6252 LEFT JOIN S62.T6230 ON T6252.F02 = T6230.F01 LEFT JOIN S62.T6231 "
            + "ON T6252.F02 = T6231.F01 LEFT JOIN S61.T6110 ON T6230.F02 = T6110.F01 LEFT JOIN S65.T6514 "
            + "ON T6252.F02 = T6514.F02 WHERE DATEDIFF(CURRENT_DATE(), T6252.F08) > 0 AND T6252.F09 = 'WH' "
            + "AND T6252.F05 IN ( '7001', '7004', '7003', '7002' ) AND ( T6514.F07 = 'F' OR T6514.F01 IS NULL ) AND T6230.F11 = 'F'");
        ArrayList<Object> parameters = new ArrayList<>();
        yqddfSearchParameter(sql, yqddfQuery, parameters, false);
        sql.append(" GROUP BY T6252.F02 ORDER BY T6252.F08 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<DfRecord>()
            {
                
                @Override
                public DfRecord[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<DfRecord> list = null;
                    while (rs.next())
                    {
                        
                        DfRecord dfRecord = new DfRecord();
                        dfRecord.bidId = rs.getInt(1);
                        dfRecord.loanRecordTitle = rs.getString(3);
                        dfRecord.loanName = rs.getString(16);
                        dfRecord.userID = rs.getInt(25);
                        dfRecord.loanAmount = rs.getBigDecimal(5);
                        dfRecord.yearRate = rs.getBigDecimal(6);
                        dfRecord.period = rs.getInt(21);
                        dfRecord.periods = rs.getInt(20);
                        dfRecord.refundDay = rs.getDate(19);
                        dfRecord.yuqi = rs.getInt(23);
                        dfRecord.bidNo = rs.getString(15);
                        dfRecord.hkfs = T6230_F10.parse(rs.getString(10));
                        
                        dfRecord.dhbj =
                            selectBigDecimal(connection,
                                "SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F02 = ? AND F05  = 7001 AND F09 IN ('WH')",
                                dfRecord.bidId);
                        dfRecord.dhlx =
                            selectBigDecimal(connection,
                                "SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F02 = ? AND F05  = 7002 AND F09 IN ('WH')",
                                dfRecord.bidId);
                        dfRecord.dhbjTotal =
                            selectBigDecimal(connection,
                                "SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F02 = ? AND F05  = 7001  AND F09 IN ('WH')",
                                dfRecord.bidId);
                        dfRecord.dhlxTotal =
                            selectBigDecimal(connection,
                                "SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F02 = ? AND F05  = 7002  AND F09 IN ('WH')",
                                dfRecord.bidId);
                        dfRecord.yuqiAmount =
                            selectBigDecimal(connection,
                                "SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F02 = ? AND F05 IN ('7003','7004') AND DATEDIFF(CURRENT_DATE(),F08) > 0 AND F09 IN ('WH')",
                                dfRecord.bidId);
                        dfRecord.dinfuAmount = dfRecord.dhbjTotal.add(dfRecord.dhlxTotal).add(dfRecord.yuqiAmount);
                        dfRecord.loandeadline = dfRecord.period + "/" + dfRecord.periods;
                        if (T6231_F21.parse(rs.getString(22)).equals(T6231_F21.F))
                        {
                            dfRecord.loandTime = rs.getInt(9) + "个月";
                        }
                        else
                        {
                            dfRecord.loandTime = rs.getInt(24) + "天";
                        }
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(dfRecord);
                        
                    }
                    return list == null ? null : list.toArray(new DfRecord[list.size()]);
                }
            },
                paging,
                sql.toString(),
                parameters);
        }
    }
    
    @Override
    public DfRecord yqddfSearchAmount(final DfQuery yqddfQuery)
        throws Throwable
    {
        
        final StringBuilder sql = new StringBuilder();
        sql.append("SELECT IFNULL(SUM(t1.F01), 0) FROM (SELECT T6230.F05 AS F01 "
            + " FROM S62.T6252 LEFT JOIN S62.T6230 ON T6252.F02 = T6230.F01 LEFT JOIN S62.T6231 "
            + "ON T6252.F02 = T6231.F01 LEFT JOIN S61.T6110 ON T6230.F02 = T6110.F01 LEFT JOIN S65.T6514 "
            + "ON T6252.F02 = T6514.F02 WHERE DATEDIFF(CURRENT_DATE(), T6252.F08) > 0 AND T6252.F09 = 'WH' "
            + "AND T6252.F05 IN ( '7001', '7004', '7003', '7002' ) AND ( T6514.F07 = 'F' OR T6514.F01 IS NULL ) AND T6230.F11 = 'F'");
        List<Object> parameters = new ArrayList<Object>();
        // sql语句和查询参数处理
        yqddfSearchParameter(sql, yqddfQuery, parameters, false);
        sql.append(" GROUP BY T6252.F02 ORDER BY T6252.F08 DESC ) AS t1");
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<DfRecord>()
            {
                @Override
                public DfRecord parse(ResultSet resultSet)
                    throws SQLException
                {
                    DfRecord count = new DfRecord();
                    if (resultSet.next())
                    {
                        sql.setLength(0);
                        List<Object> parameters = new ArrayList<Object>();
                        // sql语句和查询参数处理
                        yqddfSearchParameter(sql, yqddfQuery, parameters, false);
                        sql.append(" GROUP BY T6252.F02 ORDER BY T6252.F08 DESC ");
                        count.loanAmount = resultSet.getBigDecimal(1);
                        count.dhbj =
                            selectBigDecimal(connection,
                                "SELECT IFNULL(SUM(t1.F07), 0) FROM S62.T6252 t1 JOIN (SELECT T6230.F01 F01 FROM S62.T6252 LEFT JOIN S62.T6230 ON T6252.F02 = T6230.F01 LEFT JOIN S62.T6231 ON T6252.F02 = T6231.F01 LEFT JOIN S61.T6110 ON T6230.F02 = T6110.F01 LEFT JOIN S65.T6514 ON T6252.F02 = T6514.F02 WHERE DATEDIFF(CURRENT_DATE(), T6252.F08) > 0 AND T6252.F09 = 'WH' AND T6252.F05 IN ('7001', '7002', '7003', '7004') AND ( T6514.F07 = 'F' OR T6514.F01 IS NULL ) AND T6230.F11 = 'F' "
                                    + sql.toString() + ") t2 ON t1.F02=t2.F01 WHERE t1.F09 = 'WH' AND t1.F05 = '7001'",
                                parameters);
                        count.dhlx =
                            selectBigDecimal(connection,
                                "SELECT IFNULL(SUM(t1.F07), 0) FROM S62.T6252 t1 JOIN (SELECT T6230.F01 F01 FROM S62.T6252 LEFT JOIN S62.T6230 ON T6252.F02 = T6230.F01 LEFT JOIN S62.T6231 ON T6252.F02 = T6231.F01 LEFT JOIN S61.T6110 ON T6230.F02 = T6110.F01 LEFT JOIN S65.T6514 ON T6252.F02 = T6514.F02 WHERE DATEDIFF(CURRENT_DATE(), T6252.F08) > 0 AND T6252.F09 = 'WH' AND T6252.F05 IN ('7001', '7002', '7003', '7004') AND ( T6514.F07 = 'F' OR T6514.F01 IS NULL ) AND T6230.F11 = 'F' "
                                    + sql.toString() + ") t2 ON t1.F02=t2.F01 WHERE t1.F09 = 'WH' AND t1.F05 = '7002'",
                                parameters);
                        count.yuqiAmount =
                            selectBigDecimal(connection,
                                "SELECT IFNULL(SUM(t1.F07), 0) FROM S62.T6252 t1 JOIN (SELECT T6230.F01 F01 FROM S62.T6252 LEFT JOIN S62.T6230 ON T6252.F02 = T6230.F01 LEFT JOIN S62.T6231 ON T6252.F02 = T6231.F01 LEFT JOIN S61.T6110 ON T6230.F02 = T6110.F01 LEFT JOIN S65.T6514 ON T6252.F02 = T6514.F02 WHERE DATEDIFF(CURRENT_DATE(), T6252.F08) > 0 AND T6252.F09 = 'WH' AND T6252.F05 IN ('7001', '7002', '7003', '7004') AND ( T6514.F07 = 'F' OR T6514.F01 IS NULL ) AND T6230.F11 = 'F' "
                                    + sql.toString()
                                    + ") t2 ON t1.F02=t2.F01 WHERE t1.F09 = 'WH' AND t1.F05 IN ('7003', '7004')",
                                parameters);
                    }
                    return count;
                }
            },
                sql.toString(),
                parameters);
        }
    }
    
    private void yqddfSearchParameter(StringBuilder sql, DfQuery yqddfQuery, List<Object> parameters, boolean isYjq)
        throws ResourceNotFoundException, SQLException
    {
        if (yqddfQuery != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            String bidNo = yqddfQuery.getBidNo();
            if (!StringHelper.isEmpty(bidNo))
            {
                sql.append(" AND T6230.F25 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(bidNo));
            }
            String string = yqddfQuery.getLoanName();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6110.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            
            String loanTitle = yqddfQuery.getLoanTitle();
            if (!StringHelper.isEmpty(loanTitle))
            {
                sql.append(" AND T6230.F03 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(loanTitle));
            }
            
            T6230_F10 hkfs = yqddfQuery.getHkfs();
            if (hkfs != null)
            {
                sql.append(" AND T6230.F10 = ?");
                parameters.add(hkfs.name());
            }
            
            int yqFromDays = yqddfQuery.getYuqiFromDays();
            if (yqFromDays > 0)
            {
                //已结清
                if (isYjq)
                {
                    sql.append(" AND DATEDIFF(DATE_FORMAT(T6253.F07,'%Y-%c-%d'), T6252.F08) >= ?");
                }
                else
                {
                    sql.append(" AND DATEDIFF(CURRENT_DATE(), T6252.F08) >= ?");
                }
                parameters.add(yqFromDays);
            }
            
            int yqEndDays = yqddfQuery.getYuqiEndDays();
            if (yqEndDays > 0)
            {
                //已结清
                if (isYjq)
                {
                    sql.append(" AND DATEDIFF(DATE_FORMAT(T6253.F07,'%Y-%c-%d'), T6252.F08) <= ?");
                }
                else
                {
                    sql.append(" AND DATEDIFF(CURRENT_DATE(), T6252.F08) <= ?");
                }
                parameters.add(yqEndDays);
            }
            
            T5131_F02 dffs = yqddfQuery.getDffs();
            if (dffs != null)
            {
                sql.append(" AND T6253.F10 = ?");
                parameters.add(dffs.name());
            }
        }
    }
    
    @Override
    public PagingResult<DfRecord> ydfSearch(final DfQuery yqddfQuery, Paging paging)
        throws Throwable
    {
        final StringBuilder sql = new StringBuilder();
        sql.append("SELECT T6230.F01 AS F01, T6230.F02 AS F02, T6230.F03 AS F03, T6230.F04 AS F04, T6230.F05 AS F05, T6230.F06 AS F06, "
            + "T6230.F07 AS F07, T6230.F08 AS F08, T6230.F09 AS F09, T6230.F10 AS F10, T6230.F11 AS F11, T6230.F12 AS F12, T6230.F20 AS F13, "
            + "T6230.F22 AS F14, T6230.F25 AS F15, T6110.F02 AS F16, T6230.F05 - T6230.F07 AS F17, T7110.F02 AS F18, T6253.F08 AS F19,"
            + " T6231.F02 AS F20, T6231.F03 AS F21, T6231.F21 AS F22, T6253.F07 AS F23, T6253.F05 AS F24, T6253.F09 AS F25, T6253.F10 AS F26, "
            + "T6253.F08 AS F27,T6253.F05 AS F28,T6253.F06 AS F29,DATEDIFF(CURRENT_DATE(), T6252.F08) AS F30 "
            + " FROM S62.T6253 INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01 "
            + "LEFT JOIN S62.T6231 ON T6253.F02 = T6231.F01 LEFT JOIN S62.T6252 ON T6252.F02 = T6230.F01 AND T6252.F06=T6253.F08 "
            + " LEFT JOIN S71.T7110 ON T6253.F09 = T7110.F01 LEFT JOIN S61.T6110 ON T6253.F04 = T6110.F01 WHERE 1 = 1 "
            + "AND T6230.F11 = 'F' AND DATEDIFF(CURRENT_DATE(), T6252.F08) > 0 AND T6230.F20 != 'YJQ'");
        ArrayList<Object> parameters = new ArrayList<>();
        yqddfSearchParameter(sql, yqddfQuery, parameters, false);
        sql.append(" GROUP BY T6253.F02, T6253.F08 ORDER BY T6253.F07 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<DfRecord>()
            {
                
                @Override
                public DfRecord[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<DfRecord> list = null;
                    while (rs.next())
                    {
                        
                        DfRecord dfRecord = new DfRecord();
                        dfRecord.bidId = rs.getInt(1);
                        dfRecord.loanRecordTitle = rs.getString(3);
                        dfRecord.loanName = rs.getString(16);
                        //dfRecord.userID = rs.getInt(25);
                        dfRecord.loanAmount = rs.getBigDecimal(5);
                        dfRecord.yearRate = rs.getBigDecimal(6);
                        //dfRecord.period = rs.getInt(21);
                        dfRecord.periods = rs.getInt(20);
                        //dfRecord.refundDay = rs.getDate(19);
                        dfRecord.yuqi = rs.getInt(30);
                        dfRecord.bidNo = rs.getString(15);
                        dfRecord.hkfs = T6230_F10.parse(rs.getString(10));
                        dfRecord.operate = rs.getString(18);
                        dfRecord.dfTime = rs.getTimestamp(23);
                        dfRecord.dfmethod = T5131_F02.parse(rs.getString(26));
                        dfRecord.dfStartPeriond = rs.getInt(27);
                        dfRecord.ydfAmount = rs.getBigDecimal(28);
                        dfRecord.dffhAmount = rs.getBigDecimal(29);
                        setDfRecord(dfRecord, dfRecord.bidId, dfRecord.dfStartPeriond);
                        
                        dfRecord.dhbj =
                            selectBigDecimal(connection,
                                "SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F02 = ? AND F05  = 7001 AND F06 >= ? LIMIT 1",
                                dfRecord.bidId,
                                dfRecord.dfStartPeriond);
                        dfRecord.dhlx =
                            selectBigDecimal(connection,
                                "SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F02 = ? AND F05  = 7002 AND F06 >= ? LIMIT 1",
                                dfRecord.bidId,
                                dfRecord.dfStartPeriond);
                        dfRecord.yuqiAmount =
                            selectBigDecimal(connection,
                                "SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F02 = ? AND F05 IN ('7003','7004') AND DATEDIFF(CURRENT_DATE(),F08) > 0 AND F06 >= ? LIMIT 1",
                                dfRecord.bidId,
                                dfRecord.dfStartPeriond);
                        
                        //	                    dfRecord.dinfuAmount = dfRecord.dhbj.add(dfRecord.dhlx).add(dfRecord.yuqiAmount);
                        int syqs = rs.getInt(21);
                        //dfRecord.loandeadline = (dfRecord.periods - dfRecord.dfStartPeriond) + "/" + dfRecord.periods;
                        dfRecord.loandeadline = syqs + "/" + dfRecord.periods;
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(dfRecord);
                        
                    }
                    return list == null ? null : list.toArray(new DfRecord[list.size()]);
                }
            },
                paging,
                sql.toString(),
                parameters);
        }
    }
    
    @Override
    public DfRecord ydfSearchAmount(final DfQuery yqddfQuery)
        throws Throwable
    {
        final StringBuilder sql = new StringBuilder();
        sql.append("SELECT IFNULL(SUM(t1.F01), 0),IFNULL(SUM(t1.F02), 0),IFNULL(SUM(t1.F03), 0),t1.F04 FROM (SELECT T6230.F05 AS F01,T6253.F05 AS F02,T6253.F06 AS F03,T6253.F11 AS F04 ");
        sql.append(" FROM S62.T6253 INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01 ");
        sql.append(" LEFT JOIN S62.T6231 ON T6253.F02 = T6231.F01 LEFT JOIN S62.T6252 ON T6252.F02 = T6230.F01 ");
        sql.append(" LEFT JOIN S71.T7110 ON T6253.F09 = T7110.F01 LEFT JOIN S61.T6110 ON T6253.F04 = T6110.F01 WHERE 1 = 1 ");
        sql.append(" AND T6230.F11 = 'F' AND DATEDIFF(CURRENT_DATE(), T6252.F08) > 0 AND T6230.F20 != 'YJQ'");
        List<Object> parameters = new ArrayList<Object>();
        // sql语句和查询参数处理
        yqddfSearchParameter(sql, yqddfQuery, parameters, false);
        sql.append(" GROUP BY T6253.F02, T6253.F08 ORDER BY T6253.F07 DESC ) AS t1");
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<DfRecord>()
            {
                @Override
                public DfRecord parse(ResultSet resultSet)
                    throws SQLException
                {
                    DfRecord count = new DfRecord();
                    if (resultSet.next())
                    {
                        count.loanAmount = resultSet.getBigDecimal(1);
                        count.ydfAmount = resultSet.getBigDecimal(2);
                        count.dffhAmount = resultSet.getBigDecimal(3);
                        count.dfStartPeriond = resultSet.getInt(4);
                        
                        sql.setLength(0);
                        List<Object> parameters = new ArrayList<Object>();
                        // sql语句和查询参数处理
                        yqddfSearchParameter(sql, yqddfQuery, parameters, false);
                        count.dhbj =
                            selectBigDecimal(connection,
                                "SELECT IFNULL(SUM(a1.F07), 0) FROM S62.T6252 a1 JOIN (SELECT T6230.F01 F01,T6253.F08 F02  FROM S62.T6253 INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01"
                                    + " LEFT JOIN S62.T6231 ON T6253.F02 = T6231.F01 LEFT JOIN S62.T6252 ON T6252.F02 = T6230.F01 AND T6252.F06=T6253.F08 "
                                    + " LEFT JOIN S71.T7110 ON T6253.F09 = T7110.F01 LEFT JOIN S61.T6110 ON T6253.F04 = T6110.F01 WHERE 1 = 1 "
                                    + " AND T6230.F11 = 'F' AND DATEDIFF(CURRENT_DATE(), T6252.F08) > 0 AND T6230.F20 != 'YJQ' "
                                    + sql.toString()
                                    + " GROUP BY T6253.F02, T6253.F08 ORDER BY T6253.F07 DESC) a2 ON a1.F02=a2.F01 AND a1.F06>=a2.F02 WHERE a1.F05='7001'",
                                parameters);
                        count.dhlx =
                            selectBigDecimal(connection,
                                "SELECT IFNULL(SUM(a1.F07), 0) FROM S62.T6252 a1 JOIN (SELECT T6230.F01 F01,T6253.F08 F02  FROM S62.T6253 INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01"
                                    + " LEFT JOIN S62.T6231 ON T6253.F02 = T6231.F01 LEFT JOIN S62.T6252 ON T6252.F02 = T6230.F01 AND T6252.F06=T6253.F08 "
                                    + " LEFT JOIN S71.T7110 ON T6253.F09 = T7110.F01 LEFT JOIN S61.T6110 ON T6253.F04 = T6110.F01 WHERE 1 = 1 "
                                    + " AND T6230.F11 = 'F' AND DATEDIFF(CURRENT_DATE(), T6252.F08) > 0 AND T6230.F20 != 'YJQ' "
                                    + sql.toString()
                                    + " GROUP BY T6253.F02, T6253.F08 ORDER BY T6253.F07 DESC) a2 ON a1.F02=a2.F01 AND a1.F06>=a2.F02 WHERE a1.F05='7002'",
                                parameters);
                        count.yuqiAmount =
                            selectBigDecimal(connection,
                                "SELECT IFNULL(SUM(a1.F07), 0) FROM S62.T6252 a1 JOIN (SELECT T6230.F01 F01,T6253.F08 F02  FROM S62.T6253 INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01"
                                    + " LEFT JOIN S62.T6231 ON T6253.F02 = T6231.F01 LEFT JOIN S62.T6252 ON T6252.F02 = T6230.F01 AND T6252.F06=T6253.F08 "
                                    + " LEFT JOIN S71.T7110 ON T6253.F09 = T7110.F01 LEFT JOIN S61.T6110 ON T6253.F04 = T6110.F01 WHERE 1 = 1 "
                                    + " AND T6230.F11 = 'F' AND DATEDIFF(CURRENT_DATE(), T6252.F08) > 0 AND T6230.F20 != 'YJQ' "
                                    + sql.toString()
                                    + " GROUP BY T6253.F02, T6253.F08 ORDER BY T6253.F07 DESC) a2 ON a1.F02=a2.F01 AND a1.F06>=a2.F02 WHERE a1.F05 IN ('7003','7004')",
                                parameters);
                    }
                    return count;
                }
            },
                sql.toString(),
                parameters);
        }
    }
    
    @Override
    public PagingResult<DfRecord> dfyjqSearch(final DfQuery yqddfQuery, Paging paging)
        throws Throwable
    {
        final StringBuilder sql = new StringBuilder();
        sql.append("SELECT T6230.F01 AS F01, T6230.F02 AS F02, T6230.F03 AS F03, T6230.F04 AS F04, T6230.F05 AS F05, T6230.F06 AS F06, "
            + "T6230.F07 AS F07, T6230.F08 AS F08, T6230.F09 AS F09, T6230.F10 AS F10, T6230.F11 AS F11, T6230.F12 AS F12, T6230.F20 AS F13, "
            + "T6230.F22 AS F14, T6230.F25 AS F15, T6110.F02 AS F16, T6230.F05 - T6230.F07 AS F17, T7110.F02 AS F18, T6253.F08 AS F19,"
            + " T6231.F02 AS F20, T6231.F03 AS F21, T6231.F21 AS F22, T6253.F07 AS F23, T6253.F05 AS F24, T6253.F09 AS F25, T6253.F10 AS F26, "
            + "T6253.F08 AS F27,T6253.F05 AS F28,T6253.F06 AS F29,DATEDIFF(DATE_FORMAT(T6253.F07,'%Y-%c-%d'), T6252.F08) AS F30,T6231.F13 AS F31"
            + " FROM S62.T6253 INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01 "
            + "LEFT JOIN S62.T6231 ON T6253.F02 = T6231.F01 LEFT JOIN S62.T6252 ON T6252.F02 = T6230.F01 AND T6252.F06=T6253.F08 "
            + " LEFT JOIN S71.T7110 ON T6253.F09 = T7110.F01 LEFT JOIN S61.T6110 ON T6253.F04 = T6110.F01 WHERE 1 = 1 AND "
            + "T6230.F11 = 'F' AND DATEDIFF(CURRENT_DATE(), T6252.F08) > 0 AND T6230.F20 = 'YJQ'");
        ArrayList<Object> parameters = new ArrayList<>();
        yqddfSearchParameter(sql, yqddfQuery, parameters, true);
        sql.append(" GROUP BY T6253.F02, T6253.F08 ORDER BY T6231.F13 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<DfRecord>()
            {
                
                @Override
                public DfRecord[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<DfRecord> list = null;
                    while (rs.next())
                    {
                        
                        DfRecord dfRecord = new DfRecord();
                        dfRecord.bidId = rs.getInt(1);
                        dfRecord.loanRecordTitle = rs.getString(3);
                        dfRecord.loanName = rs.getString(16);
                        //dfRecord.userID = rs.getInt(25);
                        dfRecord.loanAmount = rs.getBigDecimal(5);
                        dfRecord.yearRate = rs.getBigDecimal(6);
                        //dfRecord.period = rs.getInt(21);
                        dfRecord.periods = rs.getInt(20);
                        //dfRecord.refundDay = rs.getDate(19);
                        dfRecord.yuqi = rs.getInt(30);
                        dfRecord.bidNo = rs.getString(15);
                        dfRecord.hkfs = T6230_F10.parse(rs.getString(10));
                        dfRecord.operate = rs.getString(18);
                        dfRecord.dfTime = rs.getTimestamp(23);
                        dfRecord.dfmethod = T5131_F02.parse(rs.getString(26));
                        dfRecord.dfStartPeriond = rs.getInt(27);
                        dfRecord.ydfAmount = rs.getBigDecimal(28);
                        dfRecord.dffhAmount = rs.getBigDecimal(29);
                        dfRecord.jqTime = rs.getTimestamp(31);
                        setDfRecord(dfRecord, dfRecord.bidId, dfRecord.dfStartPeriond);
                        dfRecord.dhbj =
                            selectBigDecimal(connection,
                                "SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F02 = ? AND F05  = 7001 AND F06 >= ? LIMIT 1",
                                dfRecord.bidId,
                                dfRecord.dfStartPeriond);
                        dfRecord.dhlx =
                            selectBigDecimal(connection,
                                "SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F02 = ? AND F05  = 7002 AND F06 >= ? LIMIT 1",
                                dfRecord.bidId,
                                dfRecord.dfStartPeriond);
                        dfRecord.yuqiAmount =
                            selectBigDecimal(connection,
                                "SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F02 = ? AND F05 IN ('7003','7004') AND DATEDIFF(CURRENT_DATE(),F08) > 0 AND F06 >= ? LIMIT 1",
                                dfRecord.bidId,
                                dfRecord.dfStartPeriond);
                        
                        //	                    dfRecord.dinfuAmount = dfRecord.dhbj.add(dfRecord.dhlx).add(dfRecord.yuqiAmount);
                        int syqs = rs.getInt(21);
                        //dfRecord.loandeadline = (dfRecord.periods - dfRecord.dfStartPeriond + 1) + "/" + dfRecord.periods;
                        dfRecord.loandeadline = syqs + "/" + dfRecord.periods;
                        //	                    if (T6231_F21.parse(rs.getString(22)).equals(T6231_F21.F))
                        //	                    {
                        //	                        dfRecord.loandTime = rs.getInt(9) + "个月";
                        //	                    }
                        //	                    else
                        //	                    {
                        //	                        dfRecord.loandTime = rs.getInt(24) + "天";
                        //	                    }
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(dfRecord);
                        
                    }
                    return list == null ? null : list.toArray(new DfRecord[list.size()]);
                }
            },
                paging,
                sql.toString(),
                parameters);
        }
    }
    
    @Override
    public DfRecord dfyjqSearchAmount(final DfQuery yqddfQuery)
        throws Throwable
    {
        final StringBuilder sql = new StringBuilder();
        sql.append("SELECT IFNULL(SUM(t1.F01), 0),IFNULL(SUM(t1.F02), 0),IFNULL(SUM(t1.F03), 0),t1.F04 FROM (SELECT T6230.F05 AS F01,T6253.F05 AS F02,T6253.F06 AS F03,T6253.F08 AS F04 ");
        sql.append(" FROM S62.T6253 INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01 ");
        sql.append(" LEFT JOIN S62.T6231 ON T6253.F02 = T6231.F01 LEFT JOIN S62.T6252 ON T6252.F02 = T6230.F01 ");
        sql.append(" LEFT JOIN S71.T7110 ON T6253.F09 = T7110.F01 LEFT JOIN S61.T6110 ON T6253.F04 = T6110.F01 WHERE 1 = 1 AND ");
        sql.append("T6230.F11 = 'F' AND DATEDIFF(CURRENT_DATE(), T6252.F08) > 0 AND T6230.F20 = 'YJQ'");
        List<Object> parameters = new ArrayList<Object>();
        // sql语句和查询参数处理
        yqddfSearchParameter(sql, yqddfQuery, parameters, true);
        sql.append(" GROUP BY T6253.F02, T6253.F08 ORDER BY T6231.F13 DESC ) AS t1");
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<DfRecord>()
            {
                @Override
                public DfRecord parse(ResultSet resultSet)
                    throws SQLException
                {
                    DfRecord count = new DfRecord();
                    if (resultSet.next())
                    {
                        count.loanAmount = resultSet.getBigDecimal(1);
                        count.ydfAmount = resultSet.getBigDecimal(2);
                        count.dffhAmount = resultSet.getBigDecimal(3);
                        count.dfStartPeriond = resultSet.getInt(4);
                        
                        sql.setLength(0);
                        List<Object> parameters = new ArrayList<Object>();
                        // sql语句和查询参数处理
                        yqddfSearchParameter(sql, yqddfQuery, parameters, true);
                        count.dhbj =
                            selectBigDecimal(connection,
                                "SELECT IFNULL(SUM(a1.F07), 0) FROM S62.T6252 a1 JOIN (SELECT T6230.F01 F01,T6253.F08 F02 FROM S62.T6253 INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01"
                                    + " LEFT JOIN S62.T6231 ON T6253.F02 = T6231.F01 LEFT JOIN S62.T6252 ON T6252.F02 = T6230.F01 AND T6252.F06=T6253.F08"
                                    + " LEFT JOIN S71.T7110 ON T6253.F09 = T7110.F01 LEFT JOIN S61.T6110 ON T6253.F04 = T6110.F01"
                                    + " WHERE 1 = 1 AND T6230.F11 = 'F' AND DATEDIFF(CURRENT_DATE(), T6252.F08) > 0 AND T6230.F20 = 'YJQ'"
                                    + sql.toString()
                                    + " GROUP BY T6253.F02, T6253.F08 ORDER BY T6231.F13 DESC) a2 ON a1.F02=a2.F01 AND a1.F06>=a2.F02 WHERE a1.F05='7001'",
                                parameters);
                        count.dhlx =
                            selectBigDecimal(connection,
                                "SELECT IFNULL(SUM(a1.F07), 0) FROM S62.T6252 a1 JOIN (SELECT T6230.F01 F01,T6253.F08 F02 FROM S62.T6253 INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01"
                                    + " LEFT JOIN S62.T6231 ON T6253.F02 = T6231.F01 LEFT JOIN S62.T6252 ON T6252.F02 = T6230.F01 AND T6252.F06=T6253.F08"
                                    + " LEFT JOIN S71.T7110 ON T6253.F09 = T7110.F01 LEFT JOIN S61.T6110 ON T6253.F04 = T6110.F01"
                                    + " WHERE 1 = 1 AND T6230.F11 = 'F' AND DATEDIFF(CURRENT_DATE(), T6252.F08) > 0 AND T6230.F20 = 'YJQ' AND T6253.F10='BX' "
                                    + sql.toString()
                                    + " GROUP BY T6253.F02, T6253.F08 ORDER BY T6231.F13 DESC) a2 ON a1.F02=a2.F01 AND a1.F06>=a2.F02 WHERE a1.F05='7002'",
                                parameters);
                        count.yuqiAmount =
                            selectBigDecimal(connection,
                                "SELECT IFNULL(SUM(a1.F07), 0) FROM S62.T6252 a1 JOIN (SELECT T6230.F01 F01,T6253.F08 F02 FROM S62.T6253 INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01"
                                    + " LEFT JOIN S62.T6231 ON T6253.F02 = T6231.F01 LEFT JOIN S62.T6252 ON T6252.F02 = T6230.F01 AND T6252.F06=T6253.F08"
                                    + " LEFT JOIN S71.T7110 ON T6253.F09 = T7110.F01 LEFT JOIN S61.T6110 ON T6253.F04 = T6110.F01"
                                    + " WHERE 1 = 1 AND T6230.F11 = 'F' AND DATEDIFF(CURRENT_DATE(), T6252.F08) > 0 AND T6230.F20 = 'YJQ'"
                                    + sql.toString()
                                    + " GROUP BY T6253.F02, T6253.F08 ORDER BY T6231.F13 DESC) a2 ON a1.F02=a2.F01 AND a1.F06>=a2.F02 WHERE a1.F05 IN ('7003','7004')",
                                parameters);
                    }
                    return count;
                }
            },
                sql.toString(),
                parameters);
        }
    }
    
    @Override
    public void exportYqddf(DfRecord[] items, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (items == null)
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
            writer.write("借款标题");
            writer.write("借款用户名");
            writer.write("借款金额(元)");
            writer.write("还款方式");
            writer.write("剩余期数");
            writer.write("应还本金(元)");
            writer.write("应还利息(元)");
            writer.write("逾期罚息(元)");
            writer.write("逾期时间");
            writer.write("逾期天数");
            //writer.write("待垫付金额(元)");
            writer.newLine();
            int index = 1;
            Calendar yqCal = Calendar.getInstance();
            for (DfRecord dfRecord : items)
            {
                writer.write(index++);
                writer.write(dfRecord.bidNo);
                writer.write(dfRecord.loanRecordTitle);
                writer.write(dfRecord.loanName);
                writer.write(dfRecord.loanAmount);
                writer.write(dfRecord.hkfs.getChineseName());
                writer.write(" " + dfRecord.loandeadline);
                writer.write(dfRecord.dhbj);
                writer.write(dfRecord.dhlx);
                writer.write(dfRecord.yuqiAmount);
                if (dfRecord.refundDay != null)
                {
                    yqCal.setTime(dfRecord.refundDay);
                    yqCal.add(Calendar.DAY_OF_MONTH, 1);
                    writer.write(Formater.formatDate(yqCal.getTime()));
                }
                else
                {
                    writer.write("");
                }
                writer.write(dfRecord.yuqi + "天");
                //writer.write(dfRecord.dinfuAmount);
                writer.newLine();
            }
            out.flush();
        }
        
    }
    
    @Override
    public void exportYdf(DfRecord[] items, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (items == null)
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
            writer.write("借款标题");
            writer.write("借款用户名");
            writer.write("借款金额(元)");
            writer.write("还款方式");
            writer.write("剩余期数");
            writer.write("应还本金(元)");
            writer.write("应还利息(元)");
            writer.write("逾期罚息(元)");
            writer.write("逾期时间");
            writer.write("逾期天数");
            writer.write("垫付时间");
            writer.write("垫付方式");
            writer.write("垫付金额(元)");
            writer.write("垫付返还金额(元)");
            writer.write("垫付操作人");
            writer.newLine();
            int index = 1;
            Calendar yqCal = Calendar.getInstance();
            for (DfRecord dfRecord : items)
            {
                writer.write(index++);
                writer.write(dfRecord.bidNo);
                writer.write(dfRecord.loanRecordTitle);
                writer.write(dfRecord.loanName);
                writer.write(dfRecord.loanAmount);
                writer.write(dfRecord.hkfs.getChineseName());
                writer.write(" " + dfRecord.loandeadline);
                writer.write(dfRecord.dhbj);
                writer.write(dfRecord.dhlx);
                writer.write(dfRecord.yuqiAmount);
                if (dfRecord.refundDay != null)
                {
                    yqCal.setTime(dfRecord.refundDay);
                    yqCal.add(Calendar.DAY_OF_MONTH, 1);
                    writer.write(Formater.formatDate(yqCal.getTime()));
                }
                else
                {
                    writer.write("");
                }
                //writer.write(dfRecord.refundDay == null ? "" : dfRecord.refundDay );
                writer.write(dfRecord.yuqi + "天");
                writer.write(dfRecord.dfTime == null ? "" : DateParser.format(dfRecord.dfTime, "yyyy-MM-dd HH:mm"));
                writer.write(dfRecord.dfmethod == null ? "" : dfRecord.dfmethod.getChineseName());
                writer.write(dfRecord.ydfAmount);
                writer.write(dfRecord.dffhAmount);
                writer.write(dfRecord.operate);
                writer.newLine();
            }
            out.flush();
        }
    }
    
    @Override
    public void exportDffh(DfRecord[] items, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (items == null)
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
            writer.write("借款标题");
            writer.write("借款用户名");
            writer.write("借款金额(元)");
            writer.write("还款方式");
            writer.write("剩余期数");
            writer.write("应还本金(元)");
            writer.write("应还利息(元)");
            writer.write("逾期罚息(元)");
            writer.write("逾期时间");
            writer.write("逾期天数");
            writer.write("垫付时间");
            writer.write("垫付方式");
            writer.write("垫付金额(元)");
            writer.write("垫付返还金额(元)");
            writer.write("垫付操作人");
            writer.write("结清时间");
            writer.newLine();
            int index = 1;
            Calendar yqCal = Calendar.getInstance();
            for (DfRecord dfRecord : items)
            {
                writer.write(index++);
                writer.write(dfRecord.bidNo);
                writer.write(dfRecord.loanRecordTitle);
                writer.write(dfRecord.loanName);
                writer.write(dfRecord.loanAmount);
                writer.write(dfRecord.hkfs.getChineseName());
                writer.write(" " + dfRecord.loandeadline);
                writer.write(dfRecord.dhbj);
                if (dfRecord.dfmethod != null && T5131_F02.BJ.name().equals(dfRecord.dfmethod.name()))
                {
                    writer.write("0.00");
                }
                else
                {
                    writer.write(dfRecord.dhlx);
                }
                
                writer.write(dfRecord.yuqiAmount);
                if (dfRecord.refundDay != null)
                {
                    yqCal.setTime(dfRecord.refundDay);
                    yqCal.add(Calendar.DAY_OF_MONTH, 1);
                    writer.write(Formater.formatDate(yqCal.getTime()));
                }
                else
                {
                    writer.write("");
                }
                //writer.write(dfRecord.refundDay == null ? "" : dfRecord.refundDay );
                writer.write(dfRecord.yuqi + "天");
                writer.write(dfRecord.dfTime == null ? "" : DateParser.format(dfRecord.dfTime, "yyyy-MM-dd HH:mm"));
                writer.write(dfRecord.dfmethod == null ? "" : dfRecord.dfmethod.getChineseName());
                writer.write(dfRecord.ydfAmount);
                writer.write(dfRecord.dffhAmount);
                writer.write(dfRecord.operate);
                writer.write(dfRecord.jqTime);
                writer.newLine();
            }
            out.flush();
        }
    }
    
    @Override
    public T5131_F02 selectT5131()
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT F02 FROM S51.T5131 LIMIT 1"))
            {
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return T5131_F02.parse(resultSet.getString(1));
                    }
                }
            }
        }
        return null;
    }
    
    //查询还款日期
    public void setDfRecord(DfRecord dfRecord, int bid, int period)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F08,DATEDIFF(CURRENT_DATE(),T6252.F08) FROM S62.T6252 WHERE F02 = ? AND F06 = ? LIMIT 1"))
            {
                pstmt.setInt(1, bid);
                pstmt.setInt(2, period);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        dfRecord.refundDay = resultSet.getDate(1);
                        //dfRecord.yuqi = resultSet.getInt(2);
                    }
                }
            }
        }
    }
    
}
