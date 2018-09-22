package com.dimeng.p2p.modules.statistics.console.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S51.enums.T5131_F02;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S62.enums.T6230_F12;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.modules.statistics.console.service.RepaymentInfoManage;
import com.dimeng.p2p.modules.statistics.console.service.entity.RepaymentStatisticsEntity;
import com.dimeng.p2p.modules.statistics.console.service.query.RepaymentStatisticsQuery;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateParser;

public class RepaymentInfoManageImpl extends AbstractStatisticsService implements RepaymentInfoManage
{
    
    public RepaymentInfoManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<RepaymentStatisticsEntity> getRepaymentList(RepaymentStatisticsQuery query, Paging paging)
        throws Throwable
    {
        List<Object> parameters = new ArrayList<>();
        StringBuffer sqlBf = new StringBuffer();
        sqlBf.append(" SELECT * FROM ( SELECT T6230.F25 AS F01, T6110.F02 AS F02, T6110.F06 AS F03, T6110.F10 AS F04, T6252.F08 AS F05, ");
        sqlBf.append(" T6252.F09 AS F06, T6252.F05 AS F07, T6252.F07 AS F08, ");
        sqlBf.append(" (SELECT F04 FROM S61.T6161 WHERE T6161.F01 = T6230.F02) AS F09,");
        sqlBf.append(" (SELECT F02 FROM S61.T6141 WHERE T6141.F01 = T6230.F02) AS F10, ");
        sqlBf.append(" (SELECT COUNT(1) FROM S62.T6250 WHERE T6250.F02 = T6230.F01 AND T6250.F03 = SK6110.F01) AS F11, ");
        sqlBf.append(" SK6110.F06 AS F12, SK6110.F02 AS F13,T6231.F02 AS F14,T6231.F03 AS F15,T6231.F21 AS F16,DATEDIFF(CURRENT_DATE(),T6252.F08) AS F17,T6252.F06 AS F18 ");
        sqlBf.append(" FROM S62.T6230 ");
        sqlBf.append(" INNER JOIN S61.T6110 ON T6230.F02 = T6110.F01 ");
        sqlBf.append(" INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 ");
        sqlBf.append(" INNER JOIN S62.T6252 ON T6230.F01 = T6252.F02 ");
        sqlBf.append(" INNER JOIN S61.T6110 SK6110 ON T6252.F04 = SK6110.F01 ");
        sqlBf.append(" LEFT JOIN S65.T6514 ON T6514.F03 = T6252.F11 ");
        sqlBf.append(" WHERE T6252.F05 IN(?,?) AND (T6514.F07 = 'F' OR T6514.F01 IS NULL)) tmp where 1=1 ");
        sqlBf.append(" AND tmp.F06 = 'WH' AND tmp.F17 <= 0 ");
        sqlBf.append(" AND (tmp.F12 = 'ZRR' OR tmp.F07 = '7007' OR (tmp.F12 = 'FZRR' AND tmp.F11>0))");
        parameters.add(FeeCode.TZ_BJ);
        parameters.add(FeeCode.TZ_LX);
        // sql语句和查询参数处理
        sqlAndParameterProcess(query, sqlBf, parameters);
        try (Connection connection = getConnection())
        {
            PagingResult<RepaymentStatisticsEntity> pagingResult =
                selectPaging(connection, new ArrayParser<RepaymentStatisticsEntity>()
                {
                    
                    @Override
                    public RepaymentStatisticsEntity[] parse(ResultSet resultSet)
                        throws SQLException
                    {
                        List<RepaymentStatisticsEntity> list = new ArrayList<RepaymentStatisticsEntity>();
                        while (resultSet.next())
                        {
                            RepaymentStatisticsEntity entity = new RepaymentStatisticsEntity();
                            entity.id = resultSet.getString(1); // '借款ID',
                            entity.account = resultSet.getString(2);// '借款账户',
                            String userType = resultSet.getString(3);// '用户类型',
                            String isAssure = resultSet.getString(4);// '是否担保方',
                            if (T6110_F06.ZRR.name().equals(userType))
                            {
                                entity.accountType = "个人账户";
                                entity.loanName = resultSet.getString(10);
                            }
                            else if (T6110_F06.FZRR.name().equals(userType) && T6110_F10.S.name().equals(isAssure))
                            {
                                entity.accountType = "机构账户";
                                entity.loanName = resultSet.getString(9);
                            }
                            else if (T6110_F06.FZRR.name().equals(userType) && T6110_F10.F.name().equals(isAssure))
                            {
                                entity.accountType = "企业账户";
                                entity.loanName = resultSet.getString(9);
                            }
                            entity.shouldTheDate = resultSet.getTimestamp(5);// 合约还款日期
                            int subject = resultSet.getInt(7);// 科目
                            if (FeeCode.TZ_BJ == subject)
                            {
                                entity.subject = "本金";
                            }
                            else if (FeeCode.TZ_LX == subject)
                            {
                                entity.subject = "利息";
                            }
                            entity.repaymentPrice = resultSet.getBigDecimal(8);// '金额'
                            if (T6231_F21.F.equals(T6231_F21.parse(resultSet.getString(16))))
                            {
                                entity.loandeadline = resultSet.getInt(18) + "/" + resultSet.getInt(14);
                            }
                            else
                            {
                                entity.loandeadline = "1/1";
                            }
                            list.add(entity);
                        }
                        return list == null ? null : list.toArray(new RepaymentStatisticsEntity[list.size()]);
                    }
                }, paging, sqlBf.toString(), parameters);
            return pagingResult;
        }
    }
    
    @Override
    public BigDecimal getRepaymentTotal(RepaymentStatisticsQuery query)
        throws Throwable
    {
        List<Object> parameters = new ArrayList<>();
        StringBuffer sqlBf = new StringBuffer();
        sqlBf.append(" SELECT IFNULL(SUM(tmp.F08),0) FROM ( SELECT T6230.F25 AS F01, T6110.F02 AS F02, T6110.F06 AS F03, T6110.F10 AS F04, T6252.F08 AS F05, ");
        sqlBf.append(" T6252.F09 AS F06, T6252.F05 AS F07, T6252.F07 AS F08, ");
        sqlBf.append(" (SELECT F04 FROM S61.T6161 WHERE T6161.F01 = T6230.F02) AS F09,");
        sqlBf.append(" (SELECT F02 FROM S61.T6141 WHERE T6141.F01 = T6230.F02) AS F10, ");
        sqlBf.append(" (SELECT COUNT(1) FROM S62.T6250 WHERE T6250.F02 = T6230.F01 AND T6250.F03 = SK6110.F01) AS F11, ");
        sqlBf.append(" SK6110.F06 AS F12, SK6110.F02 AS F13,T6231.F02 AS F14,T6231.F03 AS F15,T6231.F21 AS F16,DATEDIFF(CURRENT_DATE(),T6252.F08) AS F17,T6252.F06 AS F18 ");
        sqlBf.append(" FROM S62.T6230 ");
        sqlBf.append(" INNER JOIN S61.T6110 ON T6230.F02 = T6110.F01 ");
        sqlBf.append(" INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 ");
        sqlBf.append(" INNER JOIN S62.T6252 ON T6230.F01 = T6252.F02 ");
        sqlBf.append(" INNER JOIN S61.T6110 SK6110 ON T6252.F04 = SK6110.F01 ");
        sqlBf.append(" LEFT JOIN S65.T6514 ON T6514.F03 = T6252.F11 ");
        sqlBf.append(" WHERE T6252.F05 IN(?,?) AND (T6514.F07 = 'F' OR T6514.F01 IS NULL)) tmp where 1=1 ");
        sqlBf.append(" AND tmp.F06 = 'WH' AND tmp.F17 <= 0 ");
        sqlBf.append(" AND (tmp.F12 = 'ZRR' OR tmp.F07 = '7007' OR (tmp.F12 = 'FZRR' AND tmp.F11>0))");
        parameters.add(FeeCode.TZ_BJ);
        parameters.add(FeeCode.TZ_LX);
        // sql语句和查询参数处理
        sqlAndParameterProcess(query, sqlBf, parameters);
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<BigDecimal>()
            {
                @Override
                public BigDecimal parse(ResultSet resultSet)
                    throws SQLException
                {
                    BigDecimal total = BigDecimal.ZERO;
                    if (resultSet.next())
                    {
                        total = resultSet.getBigDecimal(1);
                    }
                    return total;
                }
            }, sqlBf.toString(), parameters);
        }
    }
    
    public void sqlAndParameterProcess(RepaymentStatisticsQuery query, StringBuffer sqlBf, List<Object> parameters)
        throws Throwable
    {
        if (query != null)
        {
            String id = query.getId();
            if (!StringHelper.isEmpty(id))
            {
                sqlBf.append(" AND tmp.F01 like ? ");
                parameters.add(getSQLConnectionProvider().allMatch(id));
            }
            
            Timestamp timestamp = query.getShouldTheDateEnd();
            if (timestamp != null)
            {
                sqlBf.append(" AND DATE(tmp.F05) <= ? ");
                parameters.add(timestamp);
            }
            
            timestamp = query.getShouldTheDateStart();
            if (timestamp != null)
            {
                sqlBf.append(" AND DATE(tmp.F05) >= ? ");
                parameters.add(timestamp);
            }
            
            String loanAccount = query.getLoanAccount();
            if (!StringHelper.isEmpty(loanAccount))
            {
                sqlBf.append(" AND tmp.F02 like ? ");
                parameters.add(getSQLConnectionProvider().allMatch(loanAccount));
            }
            
            String accountState = query.getAccountState();
            if (!StringHelper.isEmpty(accountState))
            {
                if (accountState.equals("ZRR"))
                {
                    sqlBf.append(" AND tmp.F03 = 'ZRR' AND tmp.F04 = 'F'");
                }
                else if (accountState.equals("FZRR"))
                {
                    sqlBf.append(" AND tmp.F03 = 'FZRR' AND tmp.F04 = 'F'");
                }
                else if (accountState.equals("FZRRJG"))
                {
                    sqlBf.append(" AND tmp.F03 = 'FZRR' AND tmp.F04 = 'S'");
                }
            }
            sqlBf.append(" ORDER BY tmp.F05 ASC");
        }
    }
    
    @Override
    public PagingResult<RepaymentStatisticsEntity> getOverdueList(RepaymentStatisticsQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> parameters = new ArrayList<>();
        sql.append(" SELECT * FROM (SELECT T6252.F01 AS F01,T6252.F02 AS F02,T6252.F08 AS F03,DATEDIFF(CURRENT_DATE(),T6252.F08) AS F04,T6110.F01 AS F05,T6110.F02 AS F06,T6110.F06 AS F07,T6110.F10 AS F08,T6141.F02 AS F09,T6231.F02 AS F10,T6231.F03 AS F11,T6231.F21 AS F12,T6231.F22 AS F13,T6230.F25 AS F14,T6161.F04 AS F15,T6252.F06 AS F16 ");
        sql.append(" FROM S62.T6252 LEFT JOIN S62.T6230 ON T6252.F02 = T6230.F01 LEFT JOIN S62.T6231 ON T6252.F02 = T6231.F01 LEFT JOIN S61.T6110 ON T6252.F03 = T6110.F01 LEFT JOIN S61.T6141 ON T6110.F01 = T6141.F01 LEFT JOIN S61.T6161 ON T6110.F01 = T6161.F01 ");
        sql.append(" LEFT JOIN S65.T6514 ON T6252.F02 = T6514.F02 ");
        sql.append(" WHERE DATEDIFF(CURRENT_DATE(),T6252.F08) > 0  AND T6252.F09='WH' AND (T6514.F07 = 'F' OR T6514.F01 IS NULL)) tmp where 1 = 1");
        overdueSearchParameter(sql, query, parameters);
        try (Connection connection = getConnection())
        {
            PagingResult<RepaymentStatisticsEntity> pagingResult =
                selectPaging(connection, new ArrayParser<RepaymentStatisticsEntity>()
                {
                    
                    @Override
                    public RepaymentStatisticsEntity[] parse(ResultSet resultSet)
                        throws SQLException
                    {
                        List<RepaymentStatisticsEntity> list = new ArrayList<RepaymentStatisticsEntity>();
                        while (resultSet.next())
                        {
                            RepaymentStatisticsEntity entity = new RepaymentStatisticsEntity();
                            entity.loanRecordId = resultSet.getInt(2);
                            entity.shouldTheDate = resultSet.getTimestamp(3);// 合约还款日期
                            entity.collectionNumber = resultSet.getInt(4);//逾期天数
                            entity.userId = resultSet.getInt(5);
                            entity.account = resultSet.getString(6);// '借款账户',
                            String userType = resultSet.getString(7);// '用户类型',
                            String isAssure = resultSet.getString(8);// '是否担保方',
                            if (T6110_F06.ZRR.name().equals(userType))
                            {
                                entity.accountType = "个人账户";
                                entity.loanName = resultSet.getString(9);
                            }
                            else if (T6110_F06.FZRR.name().equals(userType) && T6110_F10.S.name().equals(isAssure))
                            {
                                entity.accountType = "机构账户";
                                entity.loanName = resultSet.getString(15);
                            }
                            else if (T6110_F06.FZRR.name().equals(userType) && T6110_F10.F.name().equals(isAssure))
                            {
                                entity.accountType = "企业账户";
                                entity.loanName = resultSet.getString(15);
                            }
                            if (T6231_F21.F.equals(T6231_F21.parse(resultSet.getString(12))))
                            {
                                entity.loandeadline = resultSet.getInt(16) + "/" + resultSet.getInt(10);
                            }
                            else
                            {
                                entity.loandeadline = "1/1";
                            }
                            entity.id = resultSet.getString(14); // '借款ID标编号',
                            /**
                             * 逾期本金
                             */
                            entity.overdueAmount =
                                selectBigDecimal(connection,
                                    "SELECT SUM(hk.F07) total FROM S62.T6252 AS hk WHERE hk.F02 = ? AND hk.F03=? AND hk.F05 = 7001 AND hk.F09 = 'WH' AND DATEDIFF(CURRENT_DATE(),hk.F08) > 0",
                                    entity.loanRecordId,
                                    entity.userId);
                            /**
                             * 逾期利息
                             */
                            entity.overdueInterest =
                                selectBigDecimal(connection,
                                    "SELECT SUM(hk.F07) total FROM S62.T6252 AS hk WHERE hk.F02 = ? AND hk.F03=? AND hk.F05 = 7002 AND hk.F09 = 'WH' AND DATEDIFF(CURRENT_DATE(),hk.F08) > 0",
                                    entity.loanRecordId,
                                    entity.userId);
                            /**
                             * 逾期罚息
                             */
                            entity.overduePenalty =
                                selectBigDecimal(connection,
                                    "SELECT SUM(hk.F07) total FROM S62.T6252 AS hk WHERE hk.F02 = ? AND hk.F03=? AND hk.F05 =7004 AND hk.F09 = 'WH' AND DATEDIFF(CURRENT_DATE(),hk.F08) > 0",
                                    entity.loanRecordId,
                                    entity.userId);
                            list.add(entity);
                        }
                        return list == null ? null : list.toArray(new RepaymentStatisticsEntity[list.size()]);
                    }
                },
                    paging,
                    sql.toString(),
                    parameters);
            return pagingResult;
        }
    }
    
    @Override
    public RepaymentStatisticsEntity getOverdueTotal(RepaymentStatisticsQuery query)
        throws Throwable
    {
        final StringBuilder sql = new StringBuilder();
        ArrayList<Object> parameters = new ArrayList<>();
        sql.append(" SELECT IFNULL(SUM(t1.F07), 0) FROM S62.T6252 t1 JOIN (SELECT T6230.F01 F01 FROM S62.T6252 LEFT JOIN S62.T6230 ON T6252.F02 = T6230.F01 LEFT JOIN S62.T6231 ON T6252.F02 = T6231.F01 LEFT JOIN S61.T6110 ON T6230.F02 = T6110.F01 LEFT JOIN S65.T6514 ON T6252.F02 = T6514.F02 WHERE DATEDIFF(CURRENT_DATE(), T6252.F08) > 0 AND T6252.F09 = 'WH' AND T6252.F05 IN ('7001', '7002', '7003', '7004') AND ( T6514.F07 = 'F' OR T6514.F01 IS NULL ) ");
        yqddfSearchParameter(sql, query, parameters);
        sql.append(" GROUP BY T6252.F02 ORDER BY T6252.F08 DESC ");
        try (Connection connection = getConnection())
        {
            RepaymentStatisticsEntity entity = new RepaymentStatisticsEntity();
            entity.overdueAmount =
                selectBigDecimal(connection,
                    sql.toString()
                        + ") t2 ON t1.F02=t2.F01 WHERE t1.F09 = 'WH' AND t1.F05 = '7001' AND DATEDIFF(CURRENT_DATE(),t1.F08) > 0",
                    parameters);
            entity.overdueInterest =
                selectBigDecimal(connection,
                    sql.toString()
                        + ") t2 ON t1.F02=t2.F01 WHERE t1.F09 = 'WH' AND t1.F05 = '7002' AND DATEDIFF(CURRENT_DATE(),t1.F08) > 0",
                    parameters);
            entity.overduePenalty =
                selectBigDecimal(connection,
                    sql.toString()
                        + ") t2 ON t1.F02=t2.F01 WHERE t1.F09 = 'WH' AND t1.F05 = '7004' AND DATEDIFF(CURRENT_DATE(),t1.F08) > 0",
                    parameters);
            return entity;
        }
    }
    
    private void yqddfSearchParameter(StringBuilder sql, RepaymentStatisticsQuery yqddfQuery, List<Object> parameters)
        throws ResourceNotFoundException, SQLException
    {
        if (yqddfQuery != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            String bidNo = yqddfQuery.getId();
            if (!StringHelper.isEmpty(bidNo))
            {
                sql.append(" AND T6230.F25 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(bidNo));
            }
            String string = yqddfQuery.getLoanAccount();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6110.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            
            int yqFromDays = yqddfQuery.getOverdueDaysMin();
            if (yqFromDays > 0)
            {
                sql.append(" AND DATEDIFF(CURRENT_DATE(), T6252.F08) >= ?");
                parameters.add(yqFromDays);
            }
            
            int yqEndDays = yqddfQuery.getOverdueDaysMax();
            if (yqEndDays > 0)
            {
                sql.append(" AND DATEDIFF(CURRENT_DATE(), T6252.F08) <= ?");
                parameters.add(yqEndDays);
            }
            
            String accountState = yqddfQuery.getAccountState();
            if (!StringHelper.isEmpty(accountState))
            {
                if (accountState.equals("ZRR"))
                {
                    sql.append(" AND T6110.F06 = 'ZRR' AND T6110.F10 = 'F'");
                }
                else if (accountState.equals("FZRR"))
                {
                    sql.append(" AND T6110.F06 = 'FZRR' AND T6110.F10 = 'F'");
                }
                else if (accountState.equals("FZRRJG"))
                {
                    sql.append(" AND T6110.F06 = 'FZRR' AND T6110.F10 = 'S'");
                }
            }
            
            Timestamp timestamp = yqddfQuery.getShouldTheDateEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6252.F08) <= ? ");
                parameters.add(timestamp);
            }
            
            timestamp = yqddfQuery.getShouldTheDateStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6252.F08) >= ? ");
                parameters.add(timestamp);
            }
        }
    }
    
    private void overdueSearchParameter(StringBuilder sql, RepaymentStatisticsQuery query, ArrayList<Object> parameters)
        throws Throwable
    {
        if (query != null)
        {
            String id = query.getId();
            if (!StringHelper.isEmpty(id))
            {
                sql.append(" AND tmp.F14 like ? ");
                parameters.add(getSQLConnectionProvider().allMatch(id));
            }
            
            Timestamp timestamp = query.getShouldTheDateEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(tmp.F03) <=?");
                parameters.add(timestamp);
            }
            
            timestamp = query.getShouldTheDateStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(tmp.F03) >=?");
                parameters.add(timestamp);
            }
            
            String loanAccount = query.getLoanAccount();
            if (!StringHelper.isEmpty(loanAccount))
            {
                sql.append(" AND tmp.F06 like ? ");
                parameters.add(getSQLConnectionProvider().allMatch(loanAccount));
            }
            
            String accountState = query.getAccountState();
            if (!StringHelper.isEmpty(accountState))
            {
                if (accountState.equals("ZRR"))
                {
                    sql.append(" AND tmp.F07 = 'ZRR' AND tmp.F08 = 'F'");
                }
                else if (accountState.equals("FZRR"))
                {
                    sql.append(" AND tmp.F07 = 'FZRR' AND tmp.F08 = 'F'");
                }
                else if (accountState.equals("FZRRJG"))
                {
                    sql.append(" AND tmp.F07 = 'FZRR' AND tmp.F08 = 'S'");
                }
            }
            
            int overdueDaysMin = query.getOverdueDaysMin();
            if (overdueDaysMin > 0)
            {
                sql.append(" AND tmp.F04 >= ? ");
                parameters.add(overdueDaysMin);
            }
            
            int overdueDaysMax = query.getOverdueDaysMax();
            if (overdueDaysMax > 0)
            {
                sql.append(" AND tmp.F04 <= ? ");
                parameters.add(overdueDaysMax);
            }
            
            sql.append(" GROUP BY tmp.F02 ORDER BY tmp.F03 ASC ");
        }
    }
    
    @Override
    public PagingResult<RepaymentStatisticsEntity> getPaymentAccountList(RepaymentStatisticsQuery query, Paging paging)
        throws Throwable
    {
        /*List<Object> parameters = new ArrayList<>();
        StringBuffer sqlBf = new StringBuffer();
        sqlBf.append(" SELECT * FROM ( SELECT   T6230.F25 AS F01,   T6230.F03 AS F02,   T6110.F02 AS F03,   T6110.F06 AS F04,   T6110.F10 AS F05, ");
        sqlBf.append(" CASE WHEN (SELECT  F04 FROM S61.T6161 WHERE T6161.F01 = (SELECT F03 FROM S62.T6236 WHERE F02 = T6230.F01 LIMIT 1 )LIMIT 1) IS NULL THEN '平台' ELSE (SELECT  F04 FROM S61.T6161 WHERE T6161.F01 = (SELECT F03 FROM S62.T6236 WHERE F02 = T6230.F01 LIMIT 1 )LIMIT 1) END AS F06, ");
        sqlBf.append(" T6252.F08 AS F07,T6252.F09 AS F08,T6252.F05 AS F09,T6252.F07 AS F10,T6252.F10 AS F11,SK6110.F06 AS F12,SK6110.F02 AS F13, ");
        sqlBf.append(" (SELECT COUNT(1) FROM S62.T6250 WHERE T6250.F02 = T6230.F01 AND T6250.F03 = SK6110.F01) AS F14, ");
        sqlBf.append(" (SELECT F04 FROM S61.T6161 WHERE T6161.F01 = T6230.F02) AS F15, ");
        sqlBf.append(" (SELECT F02 FROM S61.T6141 WHERE T6141.F01 = T6230.F02) AS F16, ");
        sqlBf.append(" T6231.F02 AS F17,T6231.F03 AS F18,T6231.F21 AS F19,T6253.F05 AS F20,T6253.F10 AS F21,T6253.F07 AS F22,DATEDIFF(CURRENT_DATE(),T6252.F08) AS F23,T6230.F12 AS F24,T6230.F01 AS F25,T6230.F02 AS F26 ");
        sqlBf.append(" FROM S62.T6230 ");
        sqlBf.append(" INNER JOIN S61.T6110 ON T6230.F02 = T6110.F01 ");
        sqlBf.append(" INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 ");
        sqlBf.append(" INNER JOIN S62.T6252 ON T6230.F01 = T6252.F02 ");
        sqlBf.append(" INNER JOIN S61.T6110 SK6110 ON T6252.F04 = SK6110.F01 ");
        sqlBf.append(" INNER JOIN S62.T6253 ON T6253.F02 = T6230.F01");
        sqlBf.append(" WHERE T6252.F05 IN(?,?,?,?,?) ) tmp where 1=1 ");
        sqlBf.append(" AND tmp.F08 = 'WH' AND DATEDIFF(CURRENT_DATE(),tmp.F07) > 0 AND tmp.F12 = 'FZRR' AND tmp.F14<=0 AND tmp.F09<>7007 ");
        parameters.add(FeeCode.TZ_BJ);
        parameters.add(FeeCode.TZ_LX);
        parameters.add(FeeCode.TZ_FX);
        parameters.add(FeeCode.TZ_WYJ);
        parameters.add(FeeCode.TZ_WYJ_SXF);
        paymentAccountParameter(query, sqlBf, parameters);
        sqlBf.append(" GROUP BY tmp.F25 ORDER BY tmp.F07 ASC ");
        try (Connection connection = getConnection())
        {
            PagingResult<RepaymentStatisticsEntity> pagingResult =
                selectPaging(connection, new ArrayParser<RepaymentStatisticsEntity>()
                {
                    @Override
                    public RepaymentStatisticsEntity[] parse(ResultSet resultSet)
                        throws SQLException
                    {
                        List<RepaymentStatisticsEntity> list = new ArrayList<RepaymentStatisticsEntity>();
                        while (resultSet.next())
                        {
                            RepaymentStatisticsEntity entity = new RepaymentStatisticsEntity();
                            entity.id = resultSet.getString(1); // '借款ID',
                            entity.title = resultSet.getString(2);// '借款标题',
                            entity.account = resultSet.getString(3);// '借款账户',
                            String userType = resultSet.getString(4);// '用户类型',
                            String isAssure = resultSet.getString(5);// '是否担保方',
                            if (T6110_F06.ZRR.name().equals(userType))
                            {
                                entity.accountType = "个人账户";
                                entity.loanName = resultSet.getString(16);
                            }
                            else if (T6110_F06.FZRR.name().equals(userType) && T6110_F10.S.name().equals(isAssure))
                            {
                                entity.accountType = "机构账户";
                                entity.loanName = resultSet.getString(15);
                            }
                            else if (T6110_F06.FZRR.name().equals(userType) && T6110_F10.F.name().equals(isAssure))
                            {
                                entity.accountType = "企业账户";
                                entity.loanName = resultSet.getString(15);
                            }
                            entity.guaranteeOrg = resultSet.getString(6);// '担保机构',
                            entity.shouldTheDate = resultSet.getTimestamp(7);// 合约还款日期
                            int subject = resultSet.getInt(9);// 科目
                            if (T6110_F06.FZRR.name().equals(resultSet.getString(12)))
                            {
                                if (StringHelper.isEmpty(entity.guaranteeOrg)
                                    && !StringHelper.isEmpty(resultSet.getString(13)) && FeeCode.TZ_WYJ_SXF != subject
                                    && resultSet.getInt(14) <= 0)
                                {
                                    entity.guaranteeOrg = "平台";
                                }
                            }
                            entity.repaymentPrice = resultSet.getBigDecimal(10);// '金额'
                            if (T6231_F21.F.equals(T6231_F21.parse(resultSet.getString(19))))
                            {
                                entity.loandeadline = resultSet.getInt(18) + "/" + resultSet.getInt(17);
                            }
                            else
                            {
                                entity.loandeadline = "1/1";
                            }
                            entity.paymentAmount = resultSet.getBigDecimal(20);
                            String paymentType = resultSet.getString(21);
                            if (paymentType != null)
                            {
                                if (T5131_F02.BJ.name().equals(paymentType))
                                {
                                    entity.paymentType = "本金垫付";
                                }
                                if (T5131_F02.BX.name().equals(paymentType))
                                {
                                    entity.paymentType = "本息垫付";
                                }
                                if (T5131_F02.N.name().equals(paymentType))
                                {
                                    entity.paymentType = "不能垫付";
                                }
                            }
                            else
                            {
                                if (T6230_F12.BJQEDB.name().equals(resultSet.getString(24)))
                                {
                                    entity.paymentType = "本金垫付";
                                }
                                if (T6230_F12.BXQEDB.name().equals(resultSet.getString(24)))
                                {
                                    entity.paymentType = "本息垫付";
                                }
                            }
                            entity.paymentDate = resultSet.getTimestamp(22);
                            entity.collectionNumber = resultSet.getInt(23);
                            entity.loanRecordId = resultSet.getInt(25);
                            entity.userId = resultSet.getInt(26);
                            *//**
                              * 逾期本金
                              */
        /*
        entity.overdueAmount =
         selectBigDecimal(connection,
             "SELECT SUM(hk.F07) total FROM S62.T6252 AS hk WHERE hk.F02 = ? AND hk.F03=? AND hk.F05 = 7001 AND hk.F09 = 'WH' AND DATEDIFF(CURRENT_DATE(),hk.F08) > 0",
             entity.loanRecordId,
             entity.userId);
        *//**
          * 逾期利息
          */
        /*
        entity.overdueInterest =
         selectBigDecimal(connection,
             "SELECT SUM(hk.F07) total FROM S62.T6252 AS hk WHERE hk.F02 = ? AND hk.F03=? AND hk.F05 = 7002 AND hk.F09 = 'WH' AND DATEDIFF(CURRENT_DATE(),hk.F08) > 0",
             entity.loanRecordId,
             entity.userId);
        *//**
          * 逾期罚息
          */
        /*
        entity.overduePenalty =
         selectBigDecimal(connection,
             "SELECT SUM(hk.F07) total FROM S62.T6252 AS hk WHERE hk.F02 = ? AND hk.F03=? AND hk.F05 =7004 AND hk.F09 = 'WH' AND DATEDIFF(CURRENT_DATE(),hk.F08) > 0",
             entity.loanRecordId,
             entity.userId);
        entity.totalDfBj = entity.totalDfBj.add(entity.overdueAmount);
        entity.totalDfLx = entity.totalDfLx.add(entity.overdueInterest);
        entity.totalDfFx = entity.totalDfFx.add(entity.overduePenalty);
        entity.totalAmount = entity.totalAmount.add(entity.paymentAmount);
        list.add(entity);
        }
        return list.toArray(new RepaymentStatisticsEntity[list.size()]);
        }
        },
        paging,
        sqlBf.toString(),
        parameters);
        return pagingResult;
        }*/
        
        List<Object> params = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM (SELECT T6231.F06 AS F01, T6253.F07 AS F02, ");
        sql.append("(CASE dfrT6110.F06 WHEN 'ZRR' THEN (SELECT T6141.F02 FROM S61.T6141 WHERE T6141.F01 = T6253.F03 LIMIT 1) ELSE T6161.F04 END) AS F03, T6110.F02 AS F04,");
        sql.append("CASE T6110.F06 WHEN 'ZRR' THEN '个人账户' ELSE '企业账户' END AS F05,");
        sql.append("(CASE T6110.F06 WHEN 'ZRR' THEN T6141.F02 ELSE jkrT6161.F04 END) AS F06, T6231.F02 AS F07, T6231.F03 AS F08, CASE T6230.F12 WHEN 'BXQEDB' THEN 'BX' ELSE 'BJ' END AS F09,");
        sql.append("(SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F05 = '7001' AND T6252.F09 = 'WH') AS F10,");
        sql.append("(SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F05 = '7002' AND T6252.F09 = 'WH') AS F11,");
        sql.append("(SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F05 = '7004' AND T6252.F09 = 'WH') AS F12,");
        sql.append("T6253.F05 AS F13, IF(DATEDIFF(CURRENT_DATE(), T6231.F06) > 0,DATEDIFF(CURRENT_DATE(), T6231.F06),0) AS F14, ");
        sql.append("DATEDIFF(CURRENT_DATE(),(SELECT T6252.F08 FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F06 = T6253.F08 GROUP BY T6252.F06)) AS F15,");
        sql.append("T6230.F25 AS F16, T6110.F06 AS F17");
        sql.append(" FROM S62.T6253 ");
        sql.append(" INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01");
        sql.append(" INNER JOIN S62.T6231 ON T6231.F01 = T6230.F01");
        sql.append(" LEFT JOIN S61.T6161 ON T6253.F03 = T6161.F01");
        sql.append(" LEFT JOIN S61.T6110 AS dfrT6110 ON dfrT6110.F01 = T6253.F03");
        sql.append(" INNER JOIN S61.T6110 ON T6110.F01 = T6230.F02");
        sql.append(" INNER JOIN S61.T6141 ON T6110.F01 = T6141.F01");
        sql.append(" LEFT JOIN S61.T6161 AS jkrT6161 ON T6230.F02 = jkrT6161.F01");
        sql.append(" WHERE T6230.F20 = 'YDF') TEMP WHERE 1=1 ");
        paymentAccountParameter(query, sql, params);
        sql.append(" ORDER BY TEMP.F14 DESC ");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<RepaymentStatisticsEntity>()
            {
                @Override
                public RepaymentStatisticsEntity[] parse(ResultSet rs)
                    throws SQLException
                {
                    List<RepaymentStatisticsEntity> list = null;
                    while (rs.next())
                    {
                        RepaymentStatisticsEntity entity = new RepaymentStatisticsEntity();
                        entity.shouldTheDate = rs.getTimestamp(1);
                        entity.paymentDate = rs.getTimestamp(2);
                        entity.guaranteeOrg = rs.getString(3);
                        entity.account = rs.getString(4);
                        entity.accountType = rs.getString(5);
                        entity.loanName = rs.getString(6);
                        entity.loandeadline = rs.getInt(8) + "/" + rs.getInt(7);
                        entity.paymentType = "BX".equals(rs.getString(9)) ? "本息垫付" : "本金垫付";
                        entity.overdueAmount = rs.getBigDecimal(10);
                        entity.overdueInterest = rs.getBigDecimal(11);
                        entity.overduePenalty = rs.getBigDecimal(12);
                        entity.paymentAmount = rs.getBigDecimal(13);
                        entity.collectionNumber = rs.getInt(14);
                        entity.id = rs.getString(16);
                        if (null == list)
                        {
                            list = new ArrayList<RepaymentStatisticsEntity>();
                        }
                        list.add(entity);
                    }
                    return null == list || list.size() <= 0 ? new RepaymentStatisticsEntity[0]
                        : list.toArray(new RepaymentStatisticsEntity[list.size()]);
                }
            },
                paging,
                sql.toString(),
                params);
        }
        
    }
    
    @Override
    public RepaymentStatisticsEntity getPaymentAccountTotal(final RepaymentStatisticsQuery query)
        throws Throwable
    {
        /*List<Object> parameters = new ArrayList<>();
        final StringBuffer sqlBf = new StringBuffer();
        sqlBf.append(" SELECT IFNULL(SUM(tmp.F20), 0) FROM ( SELECT   T6230.F25 AS F01,   T6230.F03 AS F02,   T6110.F02 AS F03,   T6110.F06 AS F04,   T6110.F10 AS F05, ");
        sqlBf.append(" (SELECT  F04 FROM S61.T6161 WHERE T6161.F01 = (SELECT F03 FROM S62.T6236 WHERE F02 = T6230.F01 LIMIT 1 )LIMIT 1) AS F06, ");
        sqlBf.append(" T6252.F08 AS F07,T6252.F09 AS F08,T6252.F05 AS F09,T6252.F07 AS F10,T6252.F10 AS F11,SK6110.F06 AS F12,SK6110.F02 AS F13, ");
        sqlBf.append(" (SELECT COUNT(1) FROM S62.T6250 WHERE T6250.F02 = T6230.F01 AND T6250.F03 = SK6110.F01) AS F14, ");
        sqlBf.append(" (SELECT F04 FROM S61.T6161 WHERE T6161.F01 = T6230.F02) AS F15, ");
        sqlBf.append(" (SELECT F02 FROM S61.T6141 WHERE T6141.F01 = T6230.F02) AS F16, ");
        sqlBf.append(" T6231.F02 AS F17,T6231.F03 AS F18,T6231.F21 AS F19,T6253.F05 AS F20,T6253.F10 AS F21,T6253.F07 AS F22,DATEDIFF(CURRENT_DATE(),T6252.F08) AS F23,T6230.F12 AS F24,T6230.F01 AS F25,T6230.F02 AS F26 ");
        sqlBf.append(" FROM S62.T6230 ");
        sqlBf.append(" INNER JOIN S61.T6110 ON T6230.F02 = T6110.F01 ");
        sqlBf.append(" INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 ");
        sqlBf.append(" INNER JOIN S62.T6252 ON T6230.F01 = T6252.F02 ");
        sqlBf.append(" INNER JOIN S61.T6110 SK6110 ON T6252.F04 = SK6110.F01 ");
        sqlBf.append(" INNER JOIN S62.T6253 ON T6253.F02 = T6230.F01");
        sqlBf.append(" WHERE T6252.F05 IN(?,?,?,?,?) ) tmp where 1=1 ");
        sqlBf.append(" AND tmp.F08 = 'WH' AND DATEDIFF(CURRENT_DATE(),tmp.F07) > 0 AND tmp.F12 = 'FZRR' AND tmp.F14<=0 AND tmp.F09<>7007 ");
        parameters.add(FeeCode.TZ_BJ);
        parameters.add(FeeCode.TZ_LX);
        parameters.add(FeeCode.TZ_FX);
        parameters.add(FeeCode.TZ_WYJ);
        parameters.add(FeeCode.TZ_WYJ_SXF);
        paymentAccountParameter(query, sqlBf, parameters);
        sqlBf.append(" ORDER BY tmp.F07 ASC ");
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<RepaymentStatisticsEntity>()
            {
                @Override
                public RepaymentStatisticsEntity parse(ResultSet resultSet)
                    throws SQLException
                {
                    RepaymentStatisticsEntity count = new RepaymentStatisticsEntity();
                    if (resultSet.next())
                    {
                        count.paymentAmount = resultSet.getBigDecimal(1);
                        
                        sqlBf.setLength(0);
                        List<Object> parameters = new ArrayList<Object>();
                        // sql语句和查询参数处理
                        try
                        {
                            paymentAccountTotalParameter(query, sqlBf, parameters);
                        }
                        catch (Throwable e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        count.overdueAmount =
                            selectBigDecimal(connection,
                                "SELECT IFNULL(SUM(a1.F07), 0) FROM S62.T6252 a1 JOIN (SELECT T6230.F01 F01,T6253.F08 F02  FROM S62.T6253 INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01"
                                    + " LEFT JOIN S62.T6231 ON T6253.F02 = T6231.F01 LEFT JOIN S62.T6252 ON T6252.F02 = T6230.F01 AND T6252.F06=T6253.F08 "
                                    + " LEFT JOIN S71.T7110 ON T6253.F09 = T7110.F01 LEFT JOIN S61.T6110 ON T6253.F04 = T6110.F01 WHERE 1 = 1 "
                                    + " AND DATEDIFF(CURRENT_DATE(), T6252.F08) > 0 "
                                    + sqlBf.toString()
                                    + " GROUP BY T6253.F02, T6253.F08 ORDER BY T6253.F07 DESC) a2 ON a1.F02=a2.F01 AND a1.F06>=a2.F02 WHERE a1.F05='7001'",
                                parameters);
                        count.overdueInterest =
                            selectBigDecimal(connection,
                                "SELECT IFNULL(SUM(a1.F07), 0) FROM S62.T6252 a1 JOIN (SELECT T6230.F01 F01,T6253.F08 F02  FROM S62.T6253 INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01"
                                    + " LEFT JOIN S62.T6231 ON T6253.F02 = T6231.F01 LEFT JOIN S62.T6252 ON T6252.F02 = T6230.F01 AND T6252.F06=T6253.F08 "
                                    + " LEFT JOIN S71.T7110 ON T6253.F09 = T7110.F01 LEFT JOIN S61.T6110 ON T6253.F04 = T6110.F01 WHERE 1 = 1 "
                                    + " AND DATEDIFF(CURRENT_DATE(), T6252.F08) > 0 "
                                    + sqlBf.toString()
                                    + " GROUP BY T6253.F02, T6253.F08 ORDER BY T6253.F07 DESC) a2 ON a1.F02=a2.F01 AND a1.F06>=a2.F02 WHERE a1.F05='7002'",
                                parameters);
                        count.overduePenalty =
                            selectBigDecimal(connection,
                                "SELECT IFNULL(SUM(a1.F07), 0) FROM S62.T6252 a1 JOIN (SELECT T6230.F01 F01,T6253.F08 F02  FROM S62.T6253 INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01"
                                    + " LEFT JOIN S62.T6231 ON T6253.F02 = T6231.F01 LEFT JOIN S62.T6252 ON T6252.F02 = T6230.F01 AND T6252.F06=T6253.F08 "
                                    + " LEFT JOIN S71.T7110 ON T6253.F09 = T7110.F01 LEFT JOIN S61.T6110 ON T6253.F04 = T6110.F01 WHERE 1 = 1 "
                                    + " AND DATEDIFF(CURRENT_DATE(), T6252.F08) > 0 "
                                    + sqlBf.toString()
                                    + " GROUP BY T6253.F02, T6253.F08 ORDER BY T6253.F07 DESC) a2 ON a1.F02=a2.F01 AND a1.F06>=a2.F02 WHERE a1.F05 IN ('7003','7004')",
                                parameters);
                    }
                    return count;
                }
            },
                sqlBf.toString(),
                parameters);
        }*/
        List<Object> params = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT IFNULL(SUM(TEMP.F10),0) AS F01, IFNULL(SUM(TEMP.F11),0) AS F02, IFNULL(SUM(TEMP.F13),0) AS F03, IFNULL(SUM(TEMP.F12),0) AS F04 FROM (SELECT T6231.F06 AS F01, T6253.F07 AS F02, ");
        sql.append("(CASE dfrT6110.F06 WHEN 'ZRR' THEN (SELECT T6141.F02 FROM S61.T6141 WHERE T6141.F01 = T6253.F03 LIMIT 1) ELSE T6161.F04 END) AS F03, T6110.F02 AS F04,");
        sql.append("CASE T6110.F06 WHEN 'ZRR' THEN '个人账户' ELSE '企业账户' END AS F05,");
        sql.append("T6141.F02 AS F06, T6231.F02 AS F07, T6231.F03 AS F08, CASE T6230.F12 WHEN 'BXQEDB' THEN 'BX' ELSE 'BJ' END AS F09,");
        /*sql.append("(SELECT IFNULL(SUM(T6255.F03),0) FROM S62.T6255 WHERE T6255.F02 = T6253.F01 AND T6255.F05 = '7001') AS F10,");
        sql.append("(SELECT IFNULL(SUM(T6255.F03),0) FROM S62.T6255 WHERE T6255.F02 = T6253.F01 AND T6255.F05 = '7002') AS F11,");
        sql.append("(SELECT IFNULL(SUM(T6255.F03),0) FROM S62.T6255 WHERE T6255.F02 = T6253.F01 AND T6255.F05 = '7004') AS F12,");*/
        sql.append("(SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F05 = '7001' AND T6252.F09 = 'WH') AS F10,");
        sql.append("(SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F05 = '7002' AND T6252.F09 = 'WH') AS F11,");
        sql.append("(SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F05 = '7004' AND T6252.F09 = 'WH') AS F12,");
        sql.append("T6253.F05 AS F13, IF(DATEDIFF(CURRENT_DATE(), T6231.F06) > 0,DATEDIFF(CURRENT_DATE(), T6231.F06),0) AS F14, ");
        sql.append("DATEDIFF(CURRENT_DATE(),(SELECT T6252.F08 FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F06 = T6253.F08 GROUP BY T6252.F06)) AS F15,");
        sql.append("T6230.F25 AS F16, T6110.F06 AS F17");
        sql.append(" FROM S62.T6253 ");
        sql.append(" INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01");
        sql.append(" INNER JOIN S62.T6231 ON T6231.F01 = T6230.F01");
        sql.append(" LEFT JOIN S61.T6161 ON T6253.F03 = T6161.F01");
        sql.append(" LEFT JOIN S61.T6110 AS dfrT6110 ON dfrT6110.F01 = T6253.F03");
        sql.append(" INNER JOIN S61.T6110 ON T6110.F01 = T6230.F02");
        sql.append(" INNER JOIN S61.T6141 ON T6110.F01 = T6141.F01");
        sql.append(" WHERE T6230.F20 = 'YDF') TEMP WHERE 1=1 ");
        paymentAccountParameter(query, sql, params);
        sql.append(" ORDER BY TEMP.F02 DESC ");
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<RepaymentStatisticsEntity>()
            {
                @Override
                public RepaymentStatisticsEntity parse(ResultSet rs)
                    throws SQLException
                {
                    if (rs.next())
                    {
                        RepaymentStatisticsEntity entity = new RepaymentStatisticsEntity();
                        entity.overdueAmount = rs.getBigDecimal(1);
                        entity.overdueInterest = rs.getBigDecimal(2);
                        entity.overduePenalty = rs.getBigDecimal(4);
                        entity.paymentAmount = rs.getBigDecimal(3);
                        return entity;
                    }
                    return null;
                }
            }, sql.toString(), params);
        }
    }
    
    private void paymentAccountTotalParameter(RepaymentStatisticsQuery query, StringBuffer sqlBf,
        List<Object> parameters)
        throws Throwable
    {
        if (query != null)
        {
            String id = query.getId();
            if (!StringHelper.isEmpty(id))
            {
                sqlBf.append(" AND T6230.F25 like ? ");
                parameters.add(getSQLConnectionProvider().allMatch(id));
            }
            
            Timestamp timestamp = query.getShouldTheDateEnd();
            if (timestamp != null)
            {
                sqlBf.append(" AND DATE(T6252.F08) <= ? ");
                parameters.add(timestamp);
            }
            
            timestamp = query.getShouldTheDateStart();
            if (timestamp != null)
            {
                sqlBf.append(" AND DATE(T6252.F08) >= ? ");
                parameters.add(timestamp);
            }
            
            String loanAccount = query.getLoanAccount();
            if (!StringHelper.isEmpty(loanAccount))
            {
                sqlBf.append(" AND T6110.F02 like ? ");
                parameters.add(getSQLConnectionProvider().allMatch(loanAccount));
            }
            
            String accountState = query.getAccountState();
            if (!StringHelper.isEmpty(accountState))
            {
                if (accountState.equals("ZRR"))
                {
                    sqlBf.append(" AND T6110.F06 = 'ZRR' AND T6110.F10 = 'F'");
                }
                else if (accountState.equals("FZRR"))
                {
                    sqlBf.append(" AND T6110.F06 = 'FZRR' AND T6110.F10 = 'F'");
                }
                else if (accountState.equals("FZRRJG"))
                {
                    sqlBf.append(" AND T6110.F06 = 'FZRR' AND T6110.F10 = 'S'");
                }
            }
            
            int overdueDaysMin = query.getOverdueDaysMin();
            if (overdueDaysMin > 0)
            {
                sqlBf.append(" AND DATEDIFF(CURRENT_DATE(), T6252.F08) >= ? ");
                parameters.add(overdueDaysMin);
            }
            
            int overdueDaysMax = query.getOverdueDaysMax();
            if (overdueDaysMax > 0)
            {
                sqlBf.append(" AND DATEDIFF(CURRENT_DATE(), T6252.F08) <= ? ");
                parameters.add(overdueDaysMax);
            }
            
            String paymentType = query.getPaymentType();
            if ("BX".equals(paymentType))
            {
                sqlBf.append(" AND T6253.F10 = ? OR T6230.F12 = ? ");
                parameters.add(T5131_F02.BX.name());
                parameters.add(T6230_F12.BXQEDB.name());
            }
            if ("BJ".equals(paymentType))
            {
                sqlBf.append(" AND T6253.F10 = ? OR T6230.F12 = ? ");
                parameters.add(T5131_F02.BJ.name());
                parameters.add(T6230_F12.BJQEDB.name());
            }
            
            /*String guaranteeAgencies = query.getGuaranteeAgencies();
            if (!StringHelper.isEmpty(guaranteeAgencies))
            {
                sqlBf.append(" AND tmp.F06 like ? ");
                parameters.add(getSQLConnectionProvider().allMatch(guaranteeAgencies));
            }*/
            
            Timestamp paymentDate = query.getPaymentDateStart();
            if (paymentDate != null)
            {
                sqlBf.append(" AND DATE(T6253.F07) >= ? ");
                parameters.add(paymentDate);
            }
            
            paymentDate = query.getPaymentDateEnd();
            if (paymentDate != null)
            {
                sqlBf.append(" AND DATE(T6253.F07) <= ? ");
                parameters.add(paymentDate);
            }
        }
    }
    
    private void paymentAccountParameter(RepaymentStatisticsQuery query, StringBuilder sql, List<Object> params)
        throws Throwable
    {
        if (query != null)
        {
            if (!StringHelper.isEmpty(query.getId()))
            {
                sql.append(" AND TEMP.F16 LIKE ?");
                params.add(getSQLConnectionProvider().allMatch(query.getId()));
            }
            
            if (!StringHelper.isEmpty(query.getLoanAccount()))
            {
                sql.append(" AND TEMP.F04 LIKE ?");
                params.add(getSQLConnectionProvider().allMatch(query.getLoanAccount()));
            }
            
            if (query.getOverdueDaysMin() > 0)
            {
                sql.append(" AND TEMP.F14 >= ?");
                params.add(query.getOverdueDaysMin());
            }
            
            if (query.getOverdueDaysMax() > 0)
            {
                sql.append(" AND TEMP.F14 <= ?");
                params.add(query.getOverdueDaysMax());
            }
            
            if (!StringHelper.isEmpty(query.getPaymentType()))
            {
                sql.append(" AND TEMP.F09 = ? ");
                params.add(query.getPaymentType());
            }
            
            if (!StringHelper.isEmpty(query.getGuaranteeAgencies()))
            {
                sql.append(" AND TEMP.F03 LIKE ? ");
                params.add(getSQLConnectionProvider().allMatch(query.getGuaranteeAgencies()));
            }
            
            if (null != query.getShouldTheDateStart())
            {
                sql.append(" AND TEMP.F01 >= ?");
                params.add(query.getShouldTheDateStart());
            }
            
            if (null != query.getShouldTheDateEnd())
            {
                sql.append(" AND TEMP.F01 <= ?");
                params.add(query.getShouldTheDateEnd());
            }
            
            if (null != query.getPaymentDateStart())
            {
                sql.append(" AND TEMP.F02 >= ?");
                params.add(query.getPaymentDateStart());
            }
            if (null != query.getPaymentDateEnd())
            {
                sql.append(" AND TEMP.F02 <= ?");
                params.add(query.getPaymentDateEnd());
            }
            
            if (!StringHelper.isEmpty(query.getAccountState()))
            {
                sql.append(" AND TEMP.F17 = ?");
                params.add(query.getAccountState());
            }
        }
    }
    
    @Override
    public PagingResult<RepaymentStatisticsEntity> getAlreadyList(RepaymentStatisticsQuery query, Paging paging)
        throws Throwable
    {
        List<Object> parameters = new ArrayList<>();
        StringBuffer sqlBf = new StringBuffer();
        sqlBf.append(" SELECT * FROM ( SELECT   T6230.F25 AS F01,   T6230.F03 AS F02,   T6110.F02 AS F03,   T6110.F06 AS F04,   T6110.F10 AS F05, ");
        sqlBf.append(" (SELECT  F04 FROM S61.T6161 WHERE T6161.F01 = (SELECT F03 FROM S62.T6236 WHERE F02 = T6230.F01 LIMIT 1 )LIMIT 1) AS F06, ");
        sqlBf.append(" T6252.F08 AS F07,T6252.F09 AS F08,T6252.F05 AS F09,T6252.F07 AS F10,T6252.F10 AS F11,SK6110.F06 AS F12,SK6110.F02 AS F13, ");
        sqlBf.append(" (SELECT COUNT(1) FROM S62.T6250 WHERE T6250.F02 = T6230.F01 AND T6250.F03 = SK6110.F01) AS F14,");
        sqlBf.append(" (SELECT F04 FROM S61.T6161 WHERE T6161.F01 = T6230.F02) AS F15,");
        sqlBf.append(" (SELECT F02 FROM S61.T6141 WHERE T6141.F01 = T6230.F02) AS F16,");
        sqlBf.append(" T6231.F02 AS F17,T6231.F03 AS F18,T6231.F21 AS F19, ");
        sqlBf.append(" (SELECT COUNT(1) FROM S62.T6265 LEFT JOIN S62.T6264 ON T6264.F01 = T6265.F02 WHERE T6264.F03 = T6230.F01 AND T6264.F04 = 'YZR' AND T6265.F03 = SK6110.F01) AS F20 ");
        sqlBf.append(" ,T6230.F20 AS F21,(SELECT COUNT(1) FROM S62.T6236 WHERE T6236.F02 = T6230.F01 AND T6236.F03 = SK6110.F01) AS F22,T6252.F06 AS F23 FROM S62.T6230 ");
        sqlBf.append(" INNER JOIN S61.T6110 ON T6230.F02 = T6110.F01 ");
        sqlBf.append(" INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 ");
        sqlBf.append(" INNER JOIN S62.T6252 ON T6230.F01 = T6252.F02 ");
        sqlBf.append(" INNER JOIN S61.T6110 SK6110 ON T6252.F04 = SK6110.F01 ");
        sqlBf.append(" WHERE T6252.F05 IN(?,?,?,?,?) ) tmp where 1=1 ");
        sqlBf.append(" AND tmp.F08 = 'YH' AND (tmp.F12 = 'ZRR' OR tmp.F09 = 7007 OR (tmp.F12 = 'FZRR' AND (tmp.F14 > 0 OR tmp.F20 > 0 OR tmp.F22 > 0))) ");
        parameters.add(FeeCode.TZ_BJ);
        parameters.add(FeeCode.TZ_LX);
        parameters.add(FeeCode.TZ_FX);
        parameters.add(FeeCode.TZ_WYJ);
        parameters.add(FeeCode.TZ_WYJ_SXF);
        AlreadyParameter(query, sqlBf, parameters);
        try (Connection connection = getConnection())
        {
            PagingResult<RepaymentStatisticsEntity> pagingResult =
                selectPaging(connection, new ArrayParser<RepaymentStatisticsEntity>()
                {
                    @Override
                    public RepaymentStatisticsEntity[] parse(ResultSet resultSet)
                        throws SQLException
                    {
                        List<RepaymentStatisticsEntity> list = new ArrayList<RepaymentStatisticsEntity>();
                        while (resultSet.next())
                        {
                            RepaymentStatisticsEntity entity = new RepaymentStatisticsEntity();
                            entity.id = resultSet.getString(1); // '借款ID',
                            entity.title = resultSet.getString(2);// '借款标题',
                            entity.account = resultSet.getString(3);// '借款账户',
                            String userType = resultSet.getString(4);// '用户类型',
                            String isAssure = resultSet.getString(5);// '是否担保方',
                            if (T6110_F06.ZRR.name().equals(userType))
                            {
                                entity.accountType = "个人账户";
                                entity.loanName = resultSet.getString(16);
                            }
                            else if (T6110_F06.FZRR.name().equals(userType) && T6110_F10.S.name().equals(isAssure))
                            {
                                entity.accountType = "机构账户";
                                entity.loanName = resultSet.getString(15);
                            }
                            else if (T6110_F06.FZRR.name().equals(userType) && T6110_F10.F.name().equals(isAssure))
                            {
                                entity.accountType = "企业账户";
                                entity.loanName = resultSet.getString(15);
                            }
                            entity.guaranteeOrg = resultSet.getString(6);// '担保机构',
                            entity.shouldTheDate = resultSet.getTimestamp(7);// 合约还款日期
                            int subject = resultSet.getInt(9);// 科目
                            if (FeeCode.TZ_BJ == subject)
                            {
                                entity.subject = "本金";
                            }
                            else if (FeeCode.TZ_LX == subject)
                            {
                                entity.subject = "利息";
                            }
                            else if (FeeCode.TZ_FX == subject)
                            {
                                entity.subject = "逾期罚息";
                            }
                            else if (FeeCode.TZ_WYJ == subject)
                            {
                                entity.subject = "提前还款违约金";
                            }
                            else if (FeeCode.TZ_WYJ_SXF == subject)
                            {
                                entity.subject = "违约金手续费";
                            }
                            entity.repaymentPrice = resultSet.getBigDecimal(10);// '金额'
                            entity.actualDate = resultSet.getTimestamp(11);// '实际还款日期
                            if (T6231_F21.F.equals(T6231_F21.parse(resultSet.getString(19))))
                            {
                                entity.loandeadline = resultSet.getInt(23) + "/" + resultSet.getInt(17);
                            }
                            else
                            {
                                entity.loandeadline = "1/1";
                            }
                            list.add(entity);
                        }
                        return list.toArray(new RepaymentStatisticsEntity[list.size()]);
                    }
                }, paging, sqlBf.toString(), parameters);
            return pagingResult;
        }
    }
    
    @Override
    public BigDecimal getAlreadyTotal(RepaymentStatisticsQuery query)
        throws Throwable
    {
        List<Object> parameters = new ArrayList<>();
        StringBuffer sqlBf = new StringBuffer();
        sqlBf.append(" SELECT IFNULL(SUM(tmp.F10),0) FROM ( SELECT   T6230.F25 AS F01,   T6230.F03 AS F02,   T6110.F02 AS F03,   T6110.F06 AS F04,   T6110.F10 AS F05, ");
        sqlBf.append(" (SELECT  F04 FROM S61.T6161 WHERE T6161.F01 = (SELECT F03 FROM S62.T6236 WHERE F02 = T6230.F01 LIMIT 1 )LIMIT 1) AS F06, ");
        sqlBf.append(" T6252.F08 AS F07,T6252.F09 AS F08,T6252.F05 AS F09,T6252.F07 AS F10,T6252.F10 AS F11,SK6110.F06 AS F12,SK6110.F02 AS F13, ");
        sqlBf.append(" (SELECT COUNT(1) FROM S62.T6250 WHERE T6250.F02 = T6230.F01 AND T6250.F03 = SK6110.F01) AS F14,");
        sqlBf.append(" (SELECT F04 FROM S61.T6161 WHERE T6161.F01 = T6230.F02) AS F15,");
        sqlBf.append(" (SELECT F02 FROM S61.T6141 WHERE T6141.F01 = T6230.F02) AS F16,");
        sqlBf.append(" T6231.F02 AS F17,T6231.F03 AS F18,T6231.F21 AS F19, ");
        sqlBf.append(" (SELECT COUNT(1) FROM S62.T6265 LEFT JOIN S62.T6264 ON T6264.F01 = T6265.F02 WHERE T6264.F03 = T6230.F01 AND T6264.F04 = 'YZR' AND T6265.F03 = SK6110.F01) AS F20, ");
        sqlBf.append(" (SELECT COUNT(1) FROM S62.T6236 WHERE T6236.F02 = T6230.F01 AND T6236.F03 = SK6110.F01) AS F21");
        sqlBf.append(" FROM S62.T6230 ");
        sqlBf.append(" INNER JOIN S61.T6110 ON T6230.F02 = T6110.F01 ");
        sqlBf.append(" INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 ");
        sqlBf.append(" INNER JOIN S62.T6252 ON T6230.F01 = T6252.F02 ");
        sqlBf.append(" INNER JOIN S61.T6110 SK6110 ON T6252.F04 = SK6110.F01 ");
        sqlBf.append(" WHERE T6252.F05 IN(?,?,?,?,?) ) tmp where 1=1 ");
        sqlBf.append(" AND tmp.F08 = 'YH' AND (tmp.F12 = 'ZRR' OR tmp.F09 = 7007 OR (tmp.F12 = 'FZRR' AND (tmp.F14 > 0 OR tmp.F20 > 0 OR tmp.F21 > 0))) ");
        parameters.add(FeeCode.TZ_BJ);
        parameters.add(FeeCode.TZ_LX);
        parameters.add(FeeCode.TZ_FX);
        parameters.add(FeeCode.TZ_WYJ);
        parameters.add(FeeCode.TZ_WYJ_SXF);
        // sql语句和查询参数处理
        AlreadyParameter(query, sqlBf, parameters);
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<BigDecimal>()
            {
                @Override
                public BigDecimal parse(ResultSet resultSet)
                    throws SQLException
                {
                    BigDecimal total = BigDecimal.ZERO;
                    if (resultSet.next())
                    {
                        total = resultSet.getBigDecimal(1);
                    }
                    return total;
                }
            }, sqlBf.toString(), parameters);
        }
    }
    
    private void AlreadyParameter(RepaymentStatisticsQuery query, StringBuffer sqlBf, List<Object> parameters)
        throws Throwable
    {
        if (query != null)
        {
            String id = query.getId();
            if (!StringHelper.isEmpty(id))
            {
                sqlBf.append(" AND tmp.F01 like ? ");
                parameters.add(getSQLConnectionProvider().allMatch(id));
            }
            
            Timestamp timestamp = query.getShouldTheDateEnd();
            if (timestamp != null)
            {
                sqlBf.append(" AND DATE(tmp.F07) <=?");
                parameters.add(timestamp);
            }
            
            timestamp = query.getShouldTheDateStart();
            if (timestamp != null)
            {
                sqlBf.append(" AND DATE(tmp.F07) >=?");
                parameters.add(timestamp);
            }
            
            timestamp = query.getActualDateEnd();
            if (timestamp != null)
            {
                sqlBf.append(" AND DATE(tmp.F11) <=?");
                parameters.add(timestamp);
            }
            
            timestamp = query.getActualDateStart();
            if (timestamp != null)
            {
                sqlBf.append(" AND DATE(tmp.F11) >=?");
                parameters.add(timestamp);
            }
            
            String loanAccount = query.getLoanAccount();
            if (!StringHelper.isEmpty(loanAccount))
            {
                sqlBf.append(" AND tmp.F03 like ? ");
                parameters.add(getSQLConnectionProvider().allMatch(loanAccount));
            }
            
            String accountState = query.getAccountState();
            if (!StringHelper.isEmpty(accountState))
            {
                if (accountState.equals("ZRR"))
                {
                    sqlBf.append(" AND tmp.F04 = 'ZRR' AND tmp.F05 = 'F'");
                }
                else if (accountState.equals("FZRR"))
                {
                    sqlBf.append(" AND tmp.F04 = 'FZRR' AND tmp.F05 = 'F'");
                }
                else if (accountState.equals("FZRRJG"))
                {
                    sqlBf.append(" AND tmp.F04 = 'FZRR' AND tmp.F05 = 'S'");
                }
            }
            sqlBf.append(" ORDER BY tmp.F11 DESC");
        }
    }
    
    @Override
    public void export(RepaymentStatisticsEntity[] recWits, BigDecimal total, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (recWits == null || recWits.length <= 0)
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
            writer.write("合约还款日期");
            writer.write("借款账户");
            writer.write("账户类型");
            writer.write("借款人姓名");
            writer.write("期数");
            writer.write("科目");
            writer.write("金额(元)");
            writer.write("标的ID");
            
            writer.newLine();
            int i = 0;
            for (RepaymentStatisticsEntity recWit : recWits)
            {
                i++;
                writer.write(i);
                writer.write(DateParser.format(recWit.shouldTheDate));
                writer.write(recWit.account);
                writer.write(recWit.accountType);
                writer.write(recWit.loanName);
                writer.write(" " + recWit.loandeadline);
                writer.write(recWit.subject);
                writer.write(recWit.repaymentPrice);
                writer.write(recWit.id);
                writer.newLine();
            }
        }
    }
    
    @Override
    public void exportOverdue(RepaymentStatisticsEntity[] recWits, RepaymentStatisticsEntity overdueTotal,
        OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (recWits == null || recWits.length <= 0)
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
            writer.write("合约还款日期");
            writer.write("逾期天数");
            writer.write("逾期本金(元)");
            writer.write("逾期利息(元)");
            writer.write("逾期罚息(元)");
            writer.write("期数");
            writer.write("借款账户");
            writer.write("账户类型");
            writer.write("借款人姓名");
            writer.write("标的ID");
            
            writer.newLine();
            int i = 0;
            for (RepaymentStatisticsEntity recWit : recWits)
            {
                i++;
                writer.write(i);
                writer.write(DateParser.format(recWit.shouldTheDate));
                writer.write(recWit.collectionNumber);
                writer.write(recWit.overdueAmount);
                writer.write(recWit.overdueInterest);
                writer.write(recWit.overduePenalty);
                writer.write(" " + recWit.loandeadline);
                writer.write(recWit.account);
                writer.write(recWit.accountType);
                writer.write(recWit.loanName);
                writer.write(recWit.id);
                writer.newLine();
            }
        }
    }
    
    @Override
    public void exportPaymentAccount(RepaymentStatisticsEntity[] recWits,
        RepaymentStatisticsEntity paymentAccountTotal, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (recWits == null || recWits.length <= 0)
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
            writer.write("标的ID");
            writer.write("合约还款日期");
            writer.write("垫付日期");
            writer.write("担保机构");
            writer.write("借款账户");
            writer.write("账户类型");
            writer.write("借款人姓名");
            writer.write("期数");
            writer.write("垫付方式");
            writer.write("逾期天数");
            writer.write("本金(元)");
            writer.write("利息(元)");
            writer.write("垫付金额(元)");
            writer.write("逾期罚息(元)");
            
            writer.newLine();
            int i = 0;
            for (RepaymentStatisticsEntity recWit : recWits)
            {
                i++;
                writer.write(i);
                writer.write(recWit.id);
                writer.write(DateParser.format(recWit.shouldTheDate));
                writer.write(DateParser.format(recWit.paymentDate));
                writer.write(recWit.guaranteeOrg);
                writer.write(recWit.account);
                writer.write(recWit.accountType);
                writer.write(recWit.loanName);
                writer.write(recWit.loandeadline + "\t");
                writer.write(recWit.paymentType);
                writer.write(" " + recWit.collectionNumber);
                writer.write(recWit.overdueAmount);
                writer.write(recWit.overdueInterest);
                writer.write(recWit.paymentAmount);
                writer.write(recWit.overduePenalty);
                writer.newLine();
            }
        }
    }
    
    @Override
    public void exportAlready(RepaymentStatisticsEntity[] recWits, BigDecimal total, OutputStream outputStream,
        String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (recWits == null || recWits.length <= 0)
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
            writer.write("标的ID");
            writer.write("借款账户");
            writer.write("账户类型");
            writer.write("借款人姓名");
            writer.write("科目");
            writer.write("金额(元)");
            writer.write("期数");
            writer.write("合约还款日期");
            writer.write("实际还款日期");
            
            writer.newLine();
            int i = 0;
            for (RepaymentStatisticsEntity recWit : recWits)
            {
                i++;
                writer.write(i);
                writer.write(recWit.id);
                writer.write(recWit.account);
                writer.write(recWit.accountType);
                writer.write(recWit.loanName);
                writer.write(recWit.subject);
                writer.write(recWit.repaymentPrice);
                writer.write(" " + recWit.loandeadline);
                writer.write(DateParser.format(recWit.shouldTheDate));
                writer.write(DateParser.format(recWit.actualDate));
                writer.newLine();
            }
        }
    }
    
}
