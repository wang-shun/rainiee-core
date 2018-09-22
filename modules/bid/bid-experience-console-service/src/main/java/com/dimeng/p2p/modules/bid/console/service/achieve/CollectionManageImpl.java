package com.dimeng.p2p.modules.bid.console.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S71.enums.T7151_F06;
import com.dimeng.p2p.S71.enums.T7152_F04;
import com.dimeng.p2p.common.enums.IsHovestatus;
import com.dimeng.p2p.modules.bid.console.service.CollectionManage;
import com.dimeng.p2p.modules.bid.console.service.entity.BlacklistDetails;
import com.dimeng.p2p.modules.bid.console.service.entity.BlacklistInfo;
import com.dimeng.p2p.modules.bid.console.service.entity.CollectionRecordInfo;
import com.dimeng.p2p.modules.bid.console.service.entity.Less30;
import com.dimeng.p2p.modules.bid.console.service.entity.Near30;
import com.dimeng.p2p.modules.bid.console.service.entity.StayRefund;
import com.dimeng.p2p.modules.bid.console.service.entity.StayRefundGather;
import com.dimeng.p2p.modules.bid.console.service.entity.StayRefundInfo;
import com.dimeng.p2p.modules.bid.console.service.query.BlacklistQuery;
import com.dimeng.p2p.modules.bid.console.service.query.CollectionRecordQuery;
import com.dimeng.p2p.modules.bid.console.service.query.Greater30Query;
import com.dimeng.p2p.modules.bid.console.service.query.Less30Query;
import com.dimeng.p2p.modules.bid.console.service.query.Near30Query;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.EnumParser;

public class CollectionManageImpl extends AbstractBidService implements CollectionManage
{
    
    public static class CollectionManageFactory implements ServiceFactory<CollectionManage>
    {
        
        @Override
        public CollectionManage newInstance(ServiceResource serviceResource)
        {
            return new CollectionManageImpl(serviceResource);
        }
    }
    
    public CollectionManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<Near30> near30Search(Near30Query near30Query, Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        try (final Connection connection = getConnection())
        {
            sql.append("SELECT hk.F02 h1,us.F02 u, jk.F03 j,bt.F02, jk.F05 - jk.F07, jk.F09, DATEDIFF(hk.F08,CURRENT_DATE()) ts, hk.F08 h3,hk.F01 h4,us.F01 u2,us.F06 F061,us.F10 F101, kz.F21, kz.F22, kz.F02 kz02, kz.F03 kz03,us.F04 usF04,us.F05 usF05 FROM S62.T6252 hk LEFT JOIN S62.T6230 jk ON hk.F02 = jk.F01 LEFT JOIN S62.T6231 kz ON hk.F02 = kz.F01 LEFT JOIN S61.T6110 us ON hk.F03 = us.F01 LEFT JOIN S61.T6141 dus ON us.F02 = dus.F01 LEFT JOIN S62.T6211 AS bt ON jk.F04 =bt.F01 WHERE DATEDIFF(hk.F08,CURRENT_DATE()) >=0 AND DATEDIFF(hk.F08,CURRENT_DATE()) <= 30  AND hk.F09='WH' and hk.F05 IN('7001','6002','7004','7003','7002') ");
            ArrayList<Object> parameters = new ArrayList<>();
            near30SearchParameter(sql, near30Query, parameters);
            sql.append(" GROUP BY hk.F02 ORDER BY hk.F08 ASC,hk.F02 DESC");
            
            return selectPaging(connection, new ArrayParser<Near30>()
            {
                
                @Override
                public Near30[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<Near30> list = null;
                    while (rs.next())
                    {
                        
                        Near30 near30 = new Near30();
                        near30.loanRecordId = rs.getInt(1);
                        near30.userName = rs.getString(2);
                        near30.loanRecordTitle = rs.getString(3);
                        near30.signType = rs.getString(4);
                        near30.loanAmount = rs.getBigDecimal(5);
                        near30.distanceRefund = rs.getInt(7);
                        near30.refundDay = rs.getDate(8);
                        near30.collectionId = rs.getInt(9);
                        near30.userId = rs.getInt(10);
                        near30.userType = EnumParser.parse(T6110_F06.class, rs.getString(11));
                        near30.dbf = EnumParser.parse(T6110_F10.class, rs.getString(12));
                        near30.principalAmount =
                            selectBigDecimal(connection,
                                "SELECT SUM(hk.F07) total FROM S62.T6252 AS hk WHERE F02 = ? AND hk.F03=? AND DATEDIFF(hk.F08,CURRENT_DATE()) >=0 AND DATEDIFF(hk.F08,CURRENT_DATE()) <= 30  AND hk.F09='WH' and hk.F05 IN('7001','6002','7004','7003','7002')",
                                near30.loanRecordId,
                                near30.userId);
                        near30.residueAmount =
                            select(connection,
                                new ItemParser<BigDecimal>()
                                {
                                    @Override
                                    public BigDecimal parse(ResultSet rs)
                                        throws SQLException
                                    {
                                        BigDecimal bl = new BigDecimal(0);
                                        if (rs.next())
                                        {
                                            if (rs.getInt(1) > 0)
                                                bl = rs.getBigDecimal(1);
                                        }
                                        return bl;
                                    }
                                },
                                "SELECT SUM(S62.T6252.F07) total FROM S62.T6252 WHERE F02 = ? AND S62.T6252.F03=? AND S62.T6252.F05 IN('7001','6002','7004','7003','7002') AND S62.T6252.F09 = 'WH'",
                                near30.loanRecordId,
                                near30.userId);
                        near30.mobile = rs.getString(17);
                        near30.email = rs.getString(18);
                        if (T6231_F21.parse(rs.getString(13)).equals(T6231_F21.F))
                        {
                            near30.loandeadline = rs.getInt(16) + "/" + rs.getInt(15);
                        }
                        else
                        {
                            near30.loandeadline = "1/1";
                        }
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(near30);
                    }
                    return list == null ? null : list.toArray(new Near30[list.size()]);
                }
            },
                paging,
                sql.toString(),
                parameters);
        }
    }
    
    @Override
    public Near30 near30SearchAmount(final Near30Query near30Query)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT IFNULL(SUM(t1.F01),0) FROM "
                    + " (SELECT (jk.F05 - jk.F07) AS F01 FROM S62.T6252 hk LEFT JOIN S62.T6230 jk ON hk.F02 = jk.F01 LEFT JOIN S62.T6231 kz ON hk.F02 = kz.F01 LEFT JOIN S61.T6110 us ON hk.F03 = us.F01 LEFT JOIN S61.T6141 dus ON us.F02 = dus.F01 LEFT JOIN S62.T6211 AS bt ON jk.F04 =bt.F01 WHERE DATEDIFF(hk.F08,CURRENT_DATE()) >=0 AND DATEDIFF(hk.F08,CURRENT_DATE()) <= 30  AND hk.F09='WH' and hk.F05 IN('7001','6002','7004','7003','7002') ");
        List<Object> parameters = new ArrayList<Object>();
        // sql语句和查询参数处理
        near30SearchParameter(sql, near30Query, parameters);
        sql.append(" GROUP BY hk.F02 ORDER BY hk.F08 ASC ) AS t1");
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Near30>()
            {
                @Override
                public Near30 parse(ResultSet resultSet)
                    throws SQLException
                {
                    Near30 count = new Near30();
                    if (resultSet.next())
                    {
                        count.loanAmount = resultSet.getBigDecimal(1);
                        StringBuilder bqyhSql = new StringBuilder("SELECT SUM(hk.F07) total FROM S62.T6252 AS hk ");
                        bqyhSql.append(" LEFT JOIN S62.T6230 jk ON hk.F02 = jk.F01");
                        bqyhSql.append(" LEFT JOIN S62.T6231 kz ON hk.F02 = kz.F01");
                        bqyhSql.append(" LEFT JOIN S61.T6110 us ON hk.F03 = us.F01");
                        bqyhSql.append(" WHERE DATEDIFF(hk.F08,CURRENT_DATE()) >=0 AND DATEDIFF(hk.F08,CURRENT_DATE()) <= 30  AND hk.F09='WH' and hk.F05 IN('7001','6002','7004','7003','7002')");
                        ArrayList<Object> parameters1 = new ArrayList<>();
                        near30SearchParameter(bqyhSql, near30Query, parameters1);
                        count.principalAmount = select(connection, new ItemParser<BigDecimal>()
                        {
                            @Override
                            public BigDecimal parse(ResultSet rs)
                                throws SQLException
                            {
                                BigDecimal bl = new BigDecimal(0);
                                if (rs.next())
                                {
                                    if (rs.getInt(1) > 0)
                                        bl = rs.getBigDecimal(1);
                                }
                                return bl;
                            }
                        }, bqyhSql.toString(), parameters1);//selectBigDecimal(connection,bqyhSql.toString(),parameters1);
                        
                        ArrayList<Object> parameters = new ArrayList<>();
                        StringBuilder sySql =
                            new StringBuilder(
                                "SELECT SUM(T.t1) FROM (SELECT (SELECT IFNULL(SUM(S62.T6252.F07),0) total FROM S62.T6252 WHERE F02 = hk.F02 AND S62.T6252.F03=us.F01 AND S62.T6252.F05 IN('7001','6002','7004','7003','7002') AND S62.T6252.F09 = 'WH') AS t1");
                        sySql.append(" FROM S62.T6252 hk");
                        sySql.append(" LEFT JOIN S62.T6230 jk ON hk.F02 = jk.F01");
                        sySql.append(" LEFT JOIN S62.T6231 kz ON hk.F02 = kz.F01");
                        sySql.append(" LEFT JOIN S61.T6110 us ON hk.F03 = us.F01");
                        sySql.append(" LEFT JOIN S61.T6141 dus ON us.F02 = dus.F01");
                        sySql.append(" LEFT JOIN S62.T6211 AS bt ON jk.F04 = bt.F01");
                        sySql.append(" WHERE DATEDIFF(hk.F08, CURRENT_DATE()) >= 0 AND DATEDIFF(hk.F08, CURRENT_DATE()) <= 30");
                        sySql.append(" AND hk.F05 IN('7001','6002','7004','7003','7002') AND hk.F09='WH' ");
                        near30SearchParameter(sySql, near30Query, parameters);
                        sySql.append(" GROUP BY hk.F02 ORDER BY hk.F08 ASC) T");
                        count.residueAmount = select(connection, new ItemParser<BigDecimal>()
                        {
                            @Override
                            public BigDecimal parse(ResultSet rs)
                                throws SQLException
                            {
                                BigDecimal bl = new BigDecimal(0);
                                if (rs.next())
                                {
                                    if (rs.getInt(1) > 0)
                                        bl = rs.getBigDecimal(1);
                                }
                                return bl;
                            }
                        }, sySql.toString(), parameters);
                    }
                    return count;
                }
            },
                sql.toString(),
                parameters);
        }
    }
    
    private void near30SearchParameter(StringBuilder sql, Near30Query near30Query, List<Object> parameters)
        throws ResourceNotFoundException, SQLException
    {
        if (near30Query != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            String string = near30Query.getUserName();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND us.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            string = near30Query.getLoanRecordTitle();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND jk.F03 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            Date date = near30Query.getCreateTimeStart();
            if (date != null)
            {
                sql.append(" AND hk.F08 >= ?");
                parameters.add(date);
            }
            date = near30Query.getCreateTimeEnd();
            if (date != null)
            {
                sql.append(" AND hk.F08 <= ?");
                parameters.add(date);
            }
        }
    }
    
    @Override
    public StayRefundGather findStayRefundGather()
        throws Throwable
    {
        StayRefundGather stayRefundGather = new StayRefundGather();
        
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F09=? AND F05 = ?"))
            {
                ps.setString(1, T6252_F09.WH.name());
                ps.setInt(2, FeeCode.TZ_BJ);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        stayRefundGather.dhAmount = rs.getBigDecimal(1);
                    }
                }
            }
            
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT SUM(S62.T6252.F07) FROM S62.T6252 WHERE S62.T6252.F09='WH' AND S62.T6252.F05 IN('7001','6002','7002','7003','7004') AND DATEDIFF(CURDATE(),S62.T6252.F08)>0 AND DATEDIFF(CURDATE(),S62.T6252.F08)<31"))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        stayRefundGather.yqdhAmount = rs.getBigDecimal(1);
                    }
                }
            }
            
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT SUM(S62.T6252.F07) FROM S62.T6252 WHERE S62.T6252.F09='WH' AND S62.T6252.F05 IN('7001','6002','7002','7003','7004') AND DATEDIFF(CURDATE(),S62.T6252.F08)>=31"))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        stayRefundGather.yzyqdhAmount = rs.getBigDecimal(1);
                    }
                }
            }
        }
        return stayRefundGather;
    }
    
    @Override
    public PagingResult<Less30> less30Search(Less30Query less30Query, Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        ArrayList<Object> parameters = new ArrayList<>();
        sql.append(" SELECT hk.F02 h1,us.F02 u, jk.F03 j,bt.F02, jk.F05 - jk.F07, jk.F09, DATEDIFF(CURRENT_DATE(),hk.F08) ts,hk.F01 h4,us.F01 u2,us.F06 F061,us.F10 F101,  kz.F21, kz.F22, kz.F02 kz02, kz.F03 kz03 ,us.F04 usF04,us.F05 usF05 ");
        less30SearchParameter(sql, less30Query, parameters);
        sql.append(" GROUP BY hk.F02 ORDER BY hk.F08 ASC");
        try (Connection conn = getConnection())
        {
            PagingResult<Less30> pagingResult = selectPaging(conn, new ArrayParser<Less30>()
            {
                
                @Override
                public Less30[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<Less30> list = null;
                    Less30 less30 = null;
                    while (rs.next())
                    {
                        less30 = new Less30();
                        less30.loanRecordId = rs.getInt(1);
                        less30.userName = rs.getString(2);
                        less30.loanRecordTitle = rs.getString(3);
                        less30.signType = rs.getString(4);
                        less30.loanAmount = rs.getBigDecimal(5);
                        less30.collectionNumber = rs.getInt(7);
                        less30.collectionId = rs.getInt(8);
                        less30.userId = rs.getInt(9);
                        less30.userType = EnumParser.parse(T6110_F06.class, rs.getString(10));
                        less30.dbf = EnumParser.parse(T6110_F10.class, rs.getString(11));
                        less30.principalAmount =
                            selectBigDecimal(conn,
                                "SELECT SUM(hk.F07) total FROM S62.T6252 AS hk WHERE F02 = ? AND hk.F03=? AND hk.F05 IN('7001','6002','7004','7003','7002') AND hk.F09 = 'WH' AND DATEDIFF(CURRENT_DATE(),hk.F08) > 0 AND DATEDIFF(CURRENT_DATE(),hk.F08) <= 30",
                                less30.loanRecordId,
                                less30.userId);
                        less30.residueAmount =
                            selectBigDecimal(conn,
                                "SELECT SUM(hk.F07) total FROM S62.T6252 AS hk WHERE F02 = ? AND hk.F03=? AND hk.F05 IN('7001','6002','7004','7003','7002') AND hk.F09 = 'WH'",
                                less30.loanRecordId,
                                less30.userId);
                        // 修改为逾期费用了
                        less30.collectionAmount =
                            selectBigDecimal(conn,
                                "SELECT SUM(hk.F07) total FROM S62.T6252 AS hk WHERE hk.F02 = ? AND hk.F03=? AND hk.F05 =7004 AND hk.F09 = 'WH' AND DATEDIFF(CURRENT_DATE(),hk.F08) > 0 AND DATEDIFF(CURRENT_DATE(),hk.F08) <= 30",
                                less30.loanRecordId,
                                less30.userId);
                        if (T6231_F21.F.equals(T6231_F21.parse(rs.getString(12))))
                        {
                            less30.loandeadline = rs.getInt(15) + "/" + rs.getInt(14);
                        }
                        else
                        {
                            less30.loandeadline = "1/1";
                        }
                        less30.mobile = rs.getString(16);
                        less30.email = rs.getString(17);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(less30);
                    }
                    return list == null ? null : list.toArray(new Less30[list.size()]);
                }
            },
                paging,
                sql.toString(),
                parameters);
            
            Less30[] less30s = pagingResult.getItems();
            if (less30s != null)
            {
                for (int i = 0; i < less30s.length; i++)
                {
                    Less30 entity = less30s[i];
                    if (entity != null)
                    {
                        try (PreparedStatement ps =
                            conn.prepareStatement("SELECT S71.T7152.F08 as refundDay FROM S71.T7152 WHERE F02=? ORDER BY S71.T7152.F08 DESC LIMIT 1"))
                        {
                            ps.setInt(1, entity.loanRecordId);
                            
                            try (ResultSet rs = ps.executeQuery())
                            {
                                if (rs.next())
                                {
                                    
                                    entity.refundDay = rs.getTimestamp(1);
                                }
                            }
                        }
                    }
                }
            }
            
            return pagingResult;
        }
    }
    
    @Override
    public Less30 less30SearchAmount(final Less30Query less30Query)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder("SELECT IFNULL(SUM(t1.F01),0) FROM (SELECT (jk.F05 - jk.F07) AS F01");
        List<Object> parameters = new ArrayList<Object>();
        // sql语句和查询参数处理
        less30SearchParameter(sql, less30Query, parameters);
        sql.append(" GROUP BY hk.F02 ORDER BY hk.F08 ASC ) AS t1");
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Less30>()
            {
                @Override
                public Less30 parse(ResultSet resultSet)
                    throws SQLException
                {
                    Less30 count = new Less30();
                    if (resultSet.next())
                    {
                        count.loanAmount = resultSet.getBigDecimal(1);
                        StringBuilder bqyhSql = new StringBuilder("SELECT SUM(hk.F07) total ");
                        ArrayList<Object> parameters1 = new ArrayList<>();
                        less30SearchParameter(bqyhSql, less30Query, parameters1);
                        //bqyhSql.append(" AND DATEDIFF(hk.F08,CURRENT_DATE()) >=0 AND DATEDIFF(hk.F08,CURRENT_DATE()) <= 30  AND hk.F09='WH' and hk.F05 IN('7001','6002','7004','7003','7002')");
                        count.principalAmount = select(connection, new ItemParser<BigDecimal>()
                        {
                            @Override
                            public BigDecimal parse(ResultSet rs)
                                throws SQLException
                            {
                                BigDecimal bl = new BigDecimal(0);
                                if (rs.next())
                                {
                                    if (rs.getInt(1) > 0)
                                        bl = rs.getBigDecimal(1);
                                }
                                return bl;
                            }
                        }, bqyhSql.toString(), parameters1);
                        StringBuilder sql = new StringBuilder();
                        ArrayList<Object> parameters = new ArrayList<>();
                        sql.append("SELECT SUM(T.t1) FROM(");
                        sql.append("SELECT (SELECT SUM(T6252.F07) FROM S62.T6252 WHERE T6252.F09 = 'WH' AND T6252.F02 = hk.F02 AND  T6252.F03 = hk.F03) t1 ");
                        less30SearchParameter(sql, less30Query, parameters);
                        sql.append(" GROUP BY hk.F02 ORDER BY hk.F08 ASC ) AS T");
                        count.residueAmount = selectBigDecimal(connection, sql.toString(), parameters);
                    }
                    return count;
                }
            },
                sql.toString(),
                parameters);
        }
    }
    
    private void Greater30SearchParameter(StringBuilder sql, Greater30Query greater30Query, List<Object> parameters)
        throws ResourceNotFoundException, SQLException
    {
        if (greater30Query != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            String string = greater30Query.getUserName();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND us.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            string = greater30Query.getLoanRecordTitle();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND jk.F03 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            Date date = greater30Query.getCreateTimeStart();
            if (date != null)
            {
                sql.append(" AND cs.F08 >= ?");
                parameters.add(date);
            }
            date = greater30Query.getCreateTimeEnd();
            if (date != null)
            {
                sql.append(" AND cs.F08 <= ?");
                parameters.add(date);
            }
        }
    }
    
    @Override
    public PagingResult<Less30> Greater30Search(Greater30Query greater30Query, Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT hk.F02 h1,us.F02 u, jk.F03 j,bt.F02, jk.F05 - jk.F07, jk.F09, DATEDIFF(CURRENT_DATE(),hk.F08) ts,hk.F01 h4,us.F01 u2,us.F06 F061,us.F10 F101, kz.F21, kz.F22, kz.F02 kz02, kz.F03 kz03,us.F04 usF04,us.F05 usF05  ");
        sql.append(" FROM S62.T6252 hk LEFT JOIN S62.T6230 jk ON hk.F02 = jk.F01 LEFT JOIN S62.T6231 kz ON hk.F02 = kz.F01 LEFT JOIN S61.T6110 us ON hk.F03 = us.F01 LEFT JOIN S61.T6141 dus ON us.F02 = dus.F01 LEFT JOIN S62.T6211 AS bt ON jk.F04 =bt.F01 LEFT JOIN S71.T7152 cs ON hk.F02 = cs.F02 ");
        sql.append(" WHERE  DATEDIFF(CURRENT_DATE(),hk.F08) > 30  AND hk.F09='WH' ");
        ArrayList<Object> parameters = new ArrayList<>();
        Greater30SearchParameter(sql, greater30Query, parameters);
        sql.append(" GROUP BY hk.F02 ORDER BY hk.F08 ASC");
        try (Connection connection = getConnection())
        {
            PagingResult<Less30> pagingResult = selectPaging(connection, new ArrayParser<Less30>()
            {
                
                @Override
                public Less30[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<Less30> list = null;
                    while (rs.next())
                    {
                        Less30 less30 = new Less30();
                        less30.loanRecordId = rs.getInt(1);
                        less30.userName = rs.getString(2);
                        less30.loanRecordTitle = rs.getString(3);
                        less30.signType = rs.getString(4);
                        less30.loanAmount = rs.getBigDecimal(5);
                        less30.collectionNumber = rs.getInt(7);
                        less30.collectionId = rs.getInt(8);
                        less30.userId = rs.getInt(9);
                        less30.userType = EnumParser.parse(T6110_F06.class, rs.getString(10));
                        less30.dbf = EnumParser.parse(T6110_F10.class, rs.getString(11));
                        less30.principalAmount =
                            selectBigDecimal(connection,
                                "SELECT SUM(hk.F07) total FROM S62.T6252 AS hk WHERE F02 = ? AND hk.F03=? AND hk.F05 IN('7001','6002','7004','7003','7002') AND hk.F09 = 'WH' AND DATEDIFF(CURRENT_DATE(),hk.F08) > 30",
                                less30.loanRecordId,
                                less30.userId);
                        less30.residueAmount =
                            selectBigDecimal(connection,
                                "SELECT SUM(hk.F07) total FROM S62.T6252 AS hk WHERE F02 = ? AND hk.F03=? AND hk.F05 IN('7001','6002','7004','7003','7002') AND hk.F09 = 'WH'",
                                less30.loanRecordId,
                                less30.userId);
                        // 修改为逾期费用了
                        less30.collectionAmount =
                            selectBigDecimal(connection,
                                "SELECT SUM(hk.F07) total FROM S62.T6252 AS hk WHERE hk.F02 = ? AND hk.F03=? AND hk.F05=7004 AND hk.F09 = 'WH' AND DATEDIFF(CURRENT_DATE(),hk.F08) > 30",
                                less30.loanRecordId,
                                less30.userId);
                        // 修改为逾期费用了
                        if (T6231_F21.F.equals(T6231_F21.parse(rs.getString(12))))
                        {
                            less30.loandeadline = rs.getInt(15) + "/" + rs.getInt(14);
                        }
                        else
                        {
                            less30.loandeadline = "1/1";
                        }
                        less30.mobile = rs.getString(16);
                        less30.email = rs.getString(17);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(less30);
                    }
                    return list == null ? null : list.toArray(new Less30[list.size()]);
                }
            },
                paging,
                sql.toString(),
                parameters);
            
            Less30[] less30s = pagingResult.getItems();
            if (less30s != null)
            {
                for (int i = 0; i < less30s.length; i++)
                {
                    Less30 entity = less30s[i];
                    if (entity != null)
                    {
                        try (PreparedStatement ps =
                            connection.prepareStatement("SELECT S71.T7152.F08 as refundDay FROM S71.T7152 WHERE F02=? ORDER BY S71.T7152.F08 DESC LIMIT 1"))
                        {
                            ps.setInt(1, entity.loanRecordId);
                            
                            try (ResultSet rs = ps.executeQuery())
                            {
                                if (rs.next())
                                {
                                    
                                    entity.refundDay = rs.getTimestamp(1);
                                }
                            }
                        }
                        
                        try (PreparedStatement ps =
                            connection.prepareStatement("SELECT S61.T6110.F07 FROM S61.T6110 WHERE S61.T6110.F01 =?"))
                        {
                            ps.setInt(1, entity.userId);
                            
                            try (ResultSet rs = ps.executeQuery())
                            {
                                if (rs.next())
                                {
                                    entity.userStatus = EnumParser.parse(T6110_F07.class, rs.getString(1));
                                }
                            }
                        }
                    }
                }
            }
            
            return pagingResult;
        }
    }
    
    @Override
    public Less30 Greater30SearchAmount(final Greater30Query greater30Query)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT IFNULL(SUM(t1.F01),0) FROM (SELECT (jk.F05 - jk.F07) AS F01 ");
        sql.append(" FROM S62.T6252 hk LEFT JOIN S62.T6230 jk ON hk.F02 = jk.F01 LEFT JOIN S62.T6231 kz ON hk.F02 = kz.F01 LEFT JOIN S61.T6110 us ON hk.F03 = us.F01 LEFT JOIN S61.T6141 dus ON us.F02 = dus.F01 LEFT JOIN S62.T6211 AS bt ON jk.F04 =bt.F01 LEFT JOIN S71.T7152 cs ON hk.F02 = cs.F02 ");
        sql.append(" WHERE  DATEDIFF(CURRENT_DATE(),hk.F08) > 30  AND hk.F09='WH' ");
        List<Object> parameters = new ArrayList<Object>();
        // sql语句和查询参数处理
        Greater30SearchParameter(sql, greater30Query, parameters);
        sql.append(" GROUP BY hk.F02 ORDER BY hk.F08 ASC ) AS t1");
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Less30>()
            {
                @Override
                public Less30 parse(ResultSet resultSet)
                    throws SQLException
                {
                    Less30 count = new Less30();
                    if (resultSet.next())
                    {
                        count.loanAmount = resultSet.getBigDecimal(1);
                        StringBuilder bqyhSql =
                            new StringBuilder(
                                "SELECT SUM(hk.F07) total FROM S62.T6252 AS hk LEFT JOIN S62.T6230 jk ON hk.F02 = jk.F01 LEFT JOIN S62.T6231 kz ON hk.F02 = kz.F01 LEFT JOIN S61.T6110 us ON hk.F03 = us.F01 WHERE hk.F05 IN('7001','6002','7004','7003','7002') AND hk.F09 = 'WH' AND DATEDIFF(CURRENT_DATE(),hk.F08) > 30 ");
                        
                        List<Object> parameters1 = new ArrayList<Object>();
                        // sql语句和查询参数处理
                        Greater30SearchParameter(bqyhSql, greater30Query, parameters1);
                        
                        count.principalAmount = select(connection, new ItemParser<BigDecimal>()
                        {
                            @Override
                            public BigDecimal parse(ResultSet rs)
                                throws SQLException
                            {
                                BigDecimal bl = new BigDecimal(0);
                                if (rs.next())
                                {
                                    if (rs.getInt(1) > 0)
                                        bl = rs.getBigDecimal(1);
                                }
                                return bl;
                            }
                        }, bqyhSql.toString(), parameters1);
                        
                        StringBuilder sql = new StringBuilder();
                        List<Object> parameters = new ArrayList<Object>();
                        sql.append("SELECT IFNULL(SUM(T.t1),0) FROM (SELECT (SELECT SUM(T6252.F07) total FROM S62.T6252 WHERE T6252.F05 IN('7001','6002','7004','7003','7002') AND T6252.F09 = 'WH' AND T6252.F03 =hk.F03 AND T6252.F02 = hk.F02) t1 ");
                        sql.append(" FROM S62.T6252 hk LEFT JOIN S62.T6230 jk ON hk.F02 = jk.F01 LEFT JOIN S62.T6231 kz ON hk.F02 = kz.F01 LEFT JOIN S61.T6110 us ON hk.F03 = us.F01 LEFT JOIN S61.T6141 dus ON us.F02 = dus.F01 LEFT JOIN S62.T6211 AS bt ON jk.F04 =bt.F01 LEFT JOIN S71.T7152 cs ON hk.F02 = cs.F02 ");
                        sql.append(" WHERE  DATEDIFF(CURRENT_DATE(),hk.F08) > 30  AND hk.F09='WH' ");
                        Greater30SearchParameter(sql, greater30Query, parameters);
                        sql.append(" GROUP BY hk.F02 ORDER BY hk.F08 ASC ) AS T");
                        count.residueAmount = select(connection, new ItemParser<BigDecimal>()
                        {
                            @Override
                            public BigDecimal parse(ResultSet rs)
                                throws SQLException
                            {
                                BigDecimal bl = new BigDecimal(0);
                                if (rs.next())
                                {
                                    if (rs.getInt(1) > 0)
                                        bl = rs.getBigDecimal(1);
                                }
                                return bl;
                            }
                        }, sql.toString(), parameters);
                    }
                    return count;
                }
            },
                sql.toString(),
                parameters);
        }
    }
    
    private void less30SearchParameter(StringBuilder sql, Less30Query less30Query, List<Object> parameters)
        throws ResourceNotFoundException, SQLException
    {
        if (less30Query != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            IsHovestatus ih = less30Query.getState();
            if (ih == null)
            {
                // 没有选择催收记录时
                sql.append(" FROM S62.T6252 hk LEFT JOIN S62.T6230 jk ON hk.F02 = jk.F01 LEFT JOIN S62.T6231 kz ON hk.F02 = kz.F01 LEFT JOIN S61.T6110 us ON hk.F03 = us.F01 LEFT JOIN S61.T6141 dus ON us.F02 = dus.F01 LEFT JOIN S62.T6211 AS bt ON jk.F04 =bt.F01 ");
                sql.append(" WHERE 1=1");
            }
            else if (IsHovestatus.Y == ih)
            {
                // 选择有催收记录时
                sql.append(" FROM  S71.T7152 cs LEFT JOIN  S62.T6252 hk  ON hk.F02 = cs.F02 LEFT JOIN S62.T6230 jk ON hk.F02 = jk.F01 LEFT JOIN S62.T6231 kz ON hk.F02 = kz.F01 LEFT JOIN S61.T6110 us ON hk.F03 = us.F01 LEFT JOIN S61.T6141 dus ON us.F02 = dus.F01 LEFT JOIN S62.T6211 AS bt ON jk.F04 =bt.F01");
                sql.append(" WHERE 1=1 AND cs.F05 IS NOT null");
            }
            else
            {
                // 没有催收记录时
                sql.append(" FROM S62.T6252 hk LEFT JOIN S62.T6230 jk ON hk.F02 = jk.F01 LEFT JOIN S62.T6231 kz ON hk.F02 = kz.F01 LEFT JOIN S61.T6110 us ON hk.F03 = us.F01 LEFT JOIN S61.T6141 dus ON us.F02 = dus.F01 LEFT JOIN S62.T6211 AS bt ON jk.F04 =bt.F01 LEFT JOIN S71.T7152 cs ON hk.F02 = cs.F02 ");
                sql.append(" WHERE 1=1 AND cs.F05 IS null");
            }
            sql.append(" AND DATEDIFF(CURRENT_DATE(),hk.F08) > 0 AND DATEDIFF(CURRENT_DATE(),hk.F08) <= 30  AND hk.F09='WH' ");
            String string = less30Query.getUserName();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND us.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            string = less30Query.getLoanRecordTitle();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND jk.F03 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            Date date = less30Query.getCreateTimeStart();
            if (date != null)
            {
                sql.append(" AND cs.F08 >= ?");
                parameters.add(date);
            }
            date = less30Query.getCreateTimeEnd();
            if (date != null)
            {
                sql.append(" AND cs.F08 <= ?");
                parameters.add(date);
            }
        }
        else
        {
            throw new ParameterException("参数错误");
        }
    }
    
    @Override
    public PagingResult<CollectionRecordInfo> CollectionXxcsRecordSearch(CollectionRecordQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sb =
            new StringBuilder(
                "SELECT T7152.F01 AS F01, T7152.F02 AS F02, T7152.F03 AS F03, T7152.F04 AS F04, T7152.F05 AS F05, T7152.F06 AS F06, T7152.F07 AS F07, T7152.F08 AS F08, T7152.F09 AS F09, T7152.F10 AS F10, T6110.F02 AS F11 ,T6110.F04 AS F12,T6110.F06 AS F13,T6110.F10 AS F14, T6230.F25 AS F15 FROM S71.T7152 INNER JOIN S61.T6110 ON T7152.F03 = T6110.F01 LEFT JOIN S62.T6230 ON T6230.F01 = T7152.F02 WHERE 1=1 AND T7152.F11 = 'XXCS' AND T6230.F25 IS NOT NULL ");
        List<Object> parameters = new ArrayList<>();
        //if (query != null) {
        String accountName = query.getUserName();
        if (!StringHelper.isEmpty(accountName))
        {
            sb.append(" AND T6110.F02 LIKE ?");
            parameters.add(getSQLConnectionProvider().allMatch(accountName));
        }
        T7152_F04 type = query.getType();
        if (type != null)
        {
            sb.append(" AND T7152.F04=?");
            parameters.add(type);
        }
        Timestamp timestamp = query.getCreateTimeStart();
        if (timestamp != null)
        {
            sb.append(" AND DATE(T7152.F08) >= ?");
            parameters.add(timestamp);
        }
        timestamp = query.getCreateTimeEnd();
        if (timestamp != null)
        {
            sb.append(" AND DATE(T7152.F08) <= ?");
            parameters.add(timestamp);
        }
        //}
        sb.append(" ORDER BY T7152.F08 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<CollectionRecordInfo>()
            {
                
                @Override
                public CollectionRecordInfo[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<CollectionRecordInfo> lists = new ArrayList<>();
                    while (resultSet.next())
                    {
                        CollectionRecordInfo record = new CollectionRecordInfo();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = T7152_F04.parse(resultSet.getString(4));
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getString(6);
                        record.F07 = resultSet.getString(7);
                        record.F08 = resultSet.getTimestamp(8);
                        record.F09 = resultSet.getInt(9);
                        record.F10 = resultSet.getTimestamp(10);
                        record.userName = resultSet.getString(11);
                        record.phone = resultSet.getString(12);
                        record.userType = T6110_F06.parse(resultSet.getString(13));
                        record.dbf = T6110_F10.parse(resultSet.getString(14));
                        record.bidNum = resultSet.getString(15);
                        lists.add(record);
                    }
                    return lists.toArray(new CollectionRecordInfo[lists.size()]);
                }
            }, paging, sb.toString(), parameters);
        }
    }
    
    @Override
    public PagingResult<CollectionRecordInfo> CollectionXscsRecordSearch(CollectionRecordQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sb =
            new StringBuilder(
                "SELECT T7152.F01 AS F01, T7152.F02 AS F02, T7152.F03 AS F03, T7152.F04 AS F04, T7152.F05 AS F05, T7152.F06 AS F06, T7152.F07 AS F07, T7152.F08 AS F08, T7152.F09 AS F09, T7152.F10 AS F10, T6110.F02 AS F11 ,T6110.F04 AS F12,T6110.F06 AS F13,T6110.F10 AS F14, T6230.F25 AS F15 FROM S71.T7152 INNER JOIN S61.T6110 ON T7152.F03 = T6110.F01 LEFT JOIN S62.T6230 ON T6230.F01 = T7152.F02 WHERE 1=1 AND T7152.F11 = 'XSCS' AND T6230.F25 IS NOT NULL ");
        List<Object> parameters = new ArrayList<>();
        //if (query != null) {
        String accountName = query.getUserName();
        if (!StringHelper.isEmpty(accountName))
        {
            sb.append(" AND T6110.F02 LIKE ?");
            parameters.add(getSQLConnectionProvider().allMatch(accountName));
        }
        T7152_F04 type = query.getType();
        if (type != null)
        {
            sb.append(" AND T7152.F04=?");
            parameters.add(type);
        }
        Timestamp timestamp = query.getCreateTimeStart();
        if (timestamp != null)
        {
            sb.append(" AND DATE(T7152.F08) >= ?");
            parameters.add(timestamp);
        }
        timestamp = query.getCreateTimeEnd();
        if (timestamp != null)
        {
            sb.append(" AND DATE(T7152.F08) <= ?");
            parameters.add(timestamp);
        }
        //}
        sb.append(" ORDER BY T7152.F08 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<CollectionRecordInfo>()
            {
                
                @Override
                public CollectionRecordInfo[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<CollectionRecordInfo> lists = new ArrayList<>();
                    while (resultSet.next())
                    {
                        CollectionRecordInfo record = new CollectionRecordInfo();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = T7152_F04.parse(resultSet.getString(4));
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getString(6);
                        record.F07 = resultSet.getString(7);
                        record.F08 = resultSet.getTimestamp(8);
                        record.F09 = resultSet.getInt(9);
                        record.F10 = resultSet.getTimestamp(10);
                        record.userName = resultSet.getString(11);
                        record.phone = resultSet.getString(12);
                        record.userType = T6110_F06.parse(resultSet.getString(13));
                        record.dbf = T6110_F10.parse(resultSet.getString(14));
                        record.bidNum = resultSet.getString(15);
                        lists.add(record);
                    }
                    return lists.toArray(new CollectionRecordInfo[lists.size()]);
                }
            }, paging, sb.toString(), parameters);
        }
    }
    
    @Override
    public CollectionRecordInfo findCollectionRecord(int collectionId)
        throws Throwable
    {
        if (collectionId <= 0)
        {
            return null;
        }
        
        CollectionRecordInfo collectionRecord = null;
        
        try (Connection conn = getConnection())
        {
            StringBuilder sql =
                new StringBuilder(
                    "SELECT T7152.F01,T7152.F02,T6110.F02,T6141.F02,T7152.F04,T7152.F05,T7152.F08,T7152.F06,T7152.F07 "
                        + "FROM S71.T7152 INNER JOIN S61.T6110 ON T7152.F03 = T6110.F01 INNER JOIN S61.T6141 ON T7152.F03 = T6141.F01 WHERE T7152.F01 =? ");
            
            try (PreparedStatement ps = conn.prepareStatement(sql.toString()))
            {
                ps.setInt(1, collectionId);
                
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        collectionRecord = new CollectionRecordInfo();
                        
                        collectionRecord.F01 = rs.getInt(1);
                        collectionRecord.F02 = rs.getInt(2);
                        collectionRecord.userName = rs.getString(3);
                        collectionRecord.realName = rs.getString(4);
                        collectionRecord.F04 = EnumParser.parse(T7152_F04.class, rs.getString(5));
                        collectionRecord.F05 = rs.getString(6);
                        collectionRecord.F08 = rs.getTimestamp(7);
                        collectionRecord.F06 = rs.getString(8);
                        collectionRecord.F07 = rs.getString(9);
                    }
                }
            }
        }
        
        // if (collectionRecord != null) {
        // try (Connection conn = getConnection()) {
        // try (PreparedStatement ps =
        // conn.prepareStatement("SELECT F06 FROM T6041 WHERE F01=?")) {
        // ps.setInt(1, IntegerParser.parse(collectionRecord.collectionPerson));
        //
        // try (ResultSet rs = ps.executeQuery()) {
        // if (rs.next()) {
        //
        // collectionRecord.collectionPerson = rs.getString(1);
        // }
        // }
        // }
        // }
        // }
        
        return collectionRecord;
    }
    
    @Override
    public StayRefundInfo findStayRefund(int collectionId)
        throws Throwable
    {
        if (collectionId <= 0)
        {
            return null;
        }
        
        StayRefundInfo stayRefundInfo = null;
        
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT T6230.F25,T6252.F02,T6252.F03,T6110.F02,T6141.F02 FROM S62.T6230 INNER JOIN S62.T6252 ON T6230.F01=T6252.F02 INNER JOIN S61.T6110 ON T6252.F03 = T6110.F01 LEFT JOIN S61.T6141 ON T6110.F01 = T6141.F01 WHERE T6252.F01=?"))
            {
                ps.setInt(1, collectionId);
                
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        stayRefundInfo = new StayRefundInfo();
                        stayRefundInfo.bidNum = rs.getString(1);
                        stayRefundInfo.loanRecordId = rs.getInt(2);
                        stayRefundInfo.userId = rs.getInt(3);
                        stayRefundInfo.userName = rs.getString(4);
                        stayRefundInfo.realName = rs.getString(5);
                    }
                }
            }
        }
        return stayRefundInfo;
    }
    
    @Override
    public void stayRefundDispose(StayRefund stayRefund, String method, String csType)
        throws Throwable
    {
        if (stayRefund == null)
        {
            throw new ParameterException("没有指定催收信息");
        }
        int loanRecordId;
        int userId;
        T7152_F04 collectionType;
        String collectionPerson;
        String resultDesc;
        Timestamp collectionTime;
        
        {
            // 校验
            loanRecordId = stayRefund.getLoanRecordId();
            if (loanRecordId <= 0)
            {
                throw new ParameterException("借款Id不能为空.");
            }
            userId = stayRefund.getUserId();
            if (userId <= 0)
            {
                throw new ParameterException("账户Id不能为空.");
            }
            collectionType = stayRefund.getCollectionType();
            if (collectionType == null)
            {
                throw new ParameterException("催收方式不能为空.");
            }
            collectionPerson = stayRefund.getCollectionPerson();
            if (StringHelper.isEmpty(collectionPerson))
            {
                throw new ParameterException("催收人不能为空.");
            }
            resultDesc = stayRefund.getResultDesc();
            if (!method.equals("DX"))
            {
                if (StringHelper.isEmpty(resultDesc))
                {
                    throw new ParameterException("结果概要不能为空.");
                }
            }
            
            collectionTime = stayRefund.getCollectionTime();
            if (collectionTime == null)
            {
                throw new ParameterException("催收时间不能为空.");
            }
        }
        
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                Timestamp now = new Timestamp(System.currentTimeMillis());
                insert(connection,
                    "INSERT INTO S71.T7152 SET F02 = ?,F03 = ?,F04 = ?,F05 = ?,F06 = ?,F08 = ?,F09 = ?,F10 = ?,F07 = ?, F11 = ?",
                    loanRecordId,
                    userId,
                    collectionType,
                    collectionPerson,
                    resultDesc,
                    collectionTime,
                    serviceResource.getSession().getAccountId(),
                    now,
                    stayRefund.getComment(),
                    csType);
                writeLog(connection, "操作日志", "催收还款");
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
        
    }
    
    @Override
    public PagingResult<BlacklistInfo> blacklistSearch(BlacklistQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sb =
            new StringBuilder(
                "SELECT * FROM (SELECT T7151.F01 AS F01, T7151.F02 AS F02, T7151.F03 AS F03, T7151.F04 AS F04, T7151.F05 AS F05, T7151.F06 AS F06, T6110.F02 AS F07, T6110.F04 AS F08, T6110.F05 AS F09, T6110.F06 AS F10, T6110.F10 AS F11,T7110.F02 AS F12, "
                    + "T6141.F02 AS F13, T6161.F04 AS F14, T6141.F07 AS F15, T6161.F03 AS F16, T6161.F19 AS F17, T6161.F20 AS F18,"
                    + "(SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F03 = T7151.F02 AND T6252.F09='WH')AS F19 "
                    + "FROM S71.T7151 INNER JOIN S61.T6110 ON T7151.F02 = T6110.F01 INNER JOIN S71.T7110 ON T7151.F03=T7110.F01 LEFT JOIN S61.T6141 ON T6141.F01 = T7151.F02 LEFT JOIN S61.T6161 ON T6161.F01 = T7151.F02 WHERE T7151.F06 = ? )TMP WHERE 1=1 ");
        List<Object> parameters = new ArrayList<>();
        parameters.add(T7151_F06.LH.name());
        
        //if (query != null) {
        String accountName = query.getUserName();
        if (!StringHelper.isEmpty(accountName))
        {
            sb.append(" AND TMP.F07 LIKE ?");
            parameters.add(getSQLConnectionProvider().allMatch(accountName));
        }
        String phone = query.getMsisdn();
        if (!StringHelper.isEmpty(phone))
        {
            sb.append(" AND TMP.F08 LIKE ?");
            parameters.add(getSQLConnectionProvider().allMatch(phone));
        }
        Timestamp timestamp = query.getCreateTimeStart();
        if (timestamp != null)
        {
            sb.append(" AND DATE(TMP.F05) >= ?");
            parameters.add(timestamp);
        }
        timestamp = query.getCreateTimeEnd();
        if (timestamp != null)
        {
            sb.append(" AND DATE(TMP.F05) <= ?");
            parameters.add(timestamp);
        }
        String realName = query.getRealName();
        if (!StringHelper.isEmpty(realName))
        {
            sb.append(" AND (TMP.F13 LIKE ? OR TMP.F14 LIKE ?)");
            parameters.add(getSQLConnectionProvider().allMatch(realName));
            parameters.add(getSQLConnectionProvider().allMatch(realName));
        }
        
        String idCard = query.getIdCard();
        if (!StringHelper.isEmpty(idCard))
        {
            sb.append(" AND (TMP.F15 LIKE ? OR TMP.F16 LIKE ? OR TMP.F17 LIKE ?)");
            parameters.add(getSQLConnectionProvider().allMatch(StringHelper.encode(idCard)));
            parameters.add(getSQLConnectionProvider().allMatch(idCard));
            parameters.add(getSQLConnectionProvider().allMatch(idCard));
        }
        
        //}
        sb.append("ORDER BY TMP.F01 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<BlacklistInfo>()
            {
                
                @Override
                public BlacklistInfo[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<BlacklistInfo> lists = new ArrayList<>();
                    while (resultSet.next())
                    {
                        BlacklistInfo record = new BlacklistInfo();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getTimestamp(5);
                        record.F06 = T7151_F06.parse(resultSet.getString(6));
                        record.accountName = resultSet.getString(7);
                        record.phone = resultSet.getString(8);
                        record.email = resultSet.getString(9);
                        record.userType = T6110_F06.parse(resultSet.getString(10));
                        record.dbf = T6110_F10.parse(resultSet.getString(11));
                        record.lhName = resultSet.getString(12);
                        if (T6110_F06.ZRR.equals(record.userType))
                        {
                            record.realName = resultSet.getString(13);
                            record.idCard = resultSet.getString(15);
                            if (!StringHelper.isEmpty(record.idCard))
                            {
                                try
                                {
                                    record.idCard = StringHelper.decode(record.idCard);
                                }
                                catch (Throwable e)
                                {
                                    logger.error("CollectionManageImpl.blacklistSearch() error", e);
                                }
                            }
                        }
                        else
                        {
                            record.realName = resultSet.getString(14);
                            if ("Y".equals(resultSet.getString(18)))
                            {
                                record.idCard = resultSet.getString(17);
                            }
                            else
                            {
                                record.idCard = resultSet.getString(16);
                            }
                        }
                        record.whMoney = resultSet.getBigDecimal(19);
                        lists.add(record);
                    }
                    return lists.toArray(new BlacklistInfo[lists.size()]);
                }
            }, paging, sb.toString(), parameters);
        }
    }
    
    @Override
    public BlacklistDetails findBlacklistDetails(int blacklistId)
        throws Throwable
    {
        if (blacklistId <= 0)
        {
            return null;
        }
        
        BlacklistDetails blacklistDetails = null;
        
        try (Connection conn = getConnection())
        {
            StringBuilder sql =
                new StringBuilder(
                    "SELECT T6110.F02,T6141.F02 AS F021,T7151.F04,T6110.F01 FROM S71.T7151 INNER JOIN S61.T6110 ON T7151.F02 = T6110.F01 LEFT JOIN S61.T6141 ON T7151.F02 = T6141.F01 WHERE T7151.F06 = ? AND T7151.F01 = ? ");
            
            try (PreparedStatement ps = conn.prepareStatement(sql.toString()))
            {
                ps.setString(1, T7151_F06.LH.toString());
                ps.setInt(2, blacklistId);
                
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        
                        blacklistDetails = new BlacklistDetails();
                        blacklistDetails.userName = rs.getString(1);
                        blacklistDetails.realName = rs.getString(2);
                        blacklistDetails.registrationDesc = rs.getString(3);
                        int userId = rs.getInt(4);
                        findBasicInfo(blacklistDetails, userId);
                        
                    }
                }
            }
        }
        
        return blacklistDetails;
    }
    
    @Override
    public void exportBlacklist(BlacklistInfo[] blacklistInfos, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (blacklistInfos == null)
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
            writer.write("用户名");
            writer.write("姓名/企业名称");
            writer.write("手机号码");
            writer.write("邮箱地址");
            writer.write("证件号");
            writer.write("待还金额(元)");
            writer.write("拉黑时间");
            writer.write("操作人");
            writer.newLine();
            int index = 1;
            for (BlacklistInfo blacklistInfo : blacklistInfos)
            {
                writer.write(index++);
                writer.write(blacklistInfo.accountName);
                writer.write(blacklistInfo.realName);
                writer.write(blacklistInfo.phone);
                writer.write(blacklistInfo.email);
                writer.write((StringHelper.isEmpty(blacklistInfo.idCard) ? "" : blacklistInfo.idCard) + "\t");
                writer.write(blacklistInfo.whMoney);
                writer.write(blacklistInfo.F05);
                writer.write(blacklistInfo.lhName);
                writer.newLine();
            }
        }
    }
    
    @Override
    public void exportLess30(Less30[] less30s, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (less30s == null)
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
            writer.write("借款标题");
            writer.write("用户名");
            writer.write("借款金额(元)");
            writer.write("期数");
            writer.write("本期应还金额(元)");
            writer.write("剩余应还总额(元)");
            writer.write("逾期金额(元)");
            writer.write("逾期天数");
            writer.write("最近催收时间");
            writer.newLine();
            int index = 1;
            for (Less30 less30 : less30s)
            {
                writer.write(index++);
                writer.write(less30.loanRecordTitle);
                writer.write(less30.userName);
                writer.write(less30.loanAmount);
                writer.write(" " + less30.loandeadline);
                writer.write(less30.principalAmount);
                writer.write(less30.residueAmount);
                writer.write(less30.collectionAmount);
                writer.write(less30.collectionNumber + "天");
                writer.write(Formater.formatDate(less30.refundDay));
                writer.newLine();
            }
        }
    }
    
    @Override
    public void exportNear30(Near30[] near30s, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (near30s == null)
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
            writer.write("借款标题");
            writer.write("用户名");
            writer.write("借款金额(元)");
            writer.write("期数");
            writer.write("本期应还金额(元)");
            writer.write("剩余应还总额(元)");
            writer.write("距离下次还款");
            writer.write("下一还款日");
            writer.newLine();
            int index = 1;
            for (Near30 near30 : near30s)
            {
                writer.write(index++);
                writer.write(near30.loanRecordTitle);
                writer.write(near30.userName);
                writer.write(near30.loanAmount);
                writer.write(" " + near30.loandeadline);
                writer.write(near30.principalAmount);
                writer.write(near30.residueAmount);
                writer.write(near30.distanceRefund + "天");
                writer.write(near30.refundDay);
                writer.newLine();
            }
        }
    }
    
    public void findBasicInfo(BlacklistDetails basicInfo, int userId)
        throws Throwable
    {
        
        if (userId <= 0)
        {
            throw new ParameterException("参数值不能为空！");
        }
        
        try (Connection conn = getConnection())
        {
            
            if (basicInfo != null)
            {
                
                // 信用额度
                try (PreparedStatement ps = conn.prepareStatement("SELECT T6116.F03 FROM S61.T6116 WHERE T6116.F01=?"))
                {
                    ps.setInt(1, userId);
                    
                    try (ResultSet rs = ps.executeQuery())
                    {
                        if (rs.next())
                        {
                            basicInfo.creditLine = rs.getBigDecimal(1);
                        }
                    }
                }
            }
            
            // 逾期次数
            try (PreparedStatement pstmt =
                conn.prepareStatement("SELECT F02, F03 FROM S61.T6144 WHERE T6144.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        basicInfo.overdueCount = resultSet.getInt(1);
                        basicInfo.seriousOverdue = resultSet.getInt(2);
                    }
                }
            }
            
            // 待还本息
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F03 = ? AND T6252.F09 = ? "))
            {
                ps.setInt(1, userId);
                ps.setString(2, T6252_F09.WH.name());
                // ps.setInt(3, FeeCode.TZ_BJ); AND T6252.F05 IN(?,?)
                // ps.setInt(4, FeeCode.TZ_LX);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        basicInfo.borrowingLiability = resultSet.getBigDecimal(1);
                    }
                }
            }
            
            // 申请借款（笔）
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT COUNT(T6230.F01) FROM S62.T6230 WHERE T6230.F02 = ?"))
            {
                ps.setInt(1, userId);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        basicInfo.applyLoanCount = resultSet.getInt(1);
                    }
                }
            }
            
            // 还清借款（笔）
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT COUNT(T6230.F01) FROM S62.T6230 WHERE T6230.F02 = ? AND T6230.F20 = ?"))
            {
                ps.setInt(1, userId);
                ps.setString(2, T6230_F20.YJQ.name());
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        basicInfo.payOffCount = resultSet.getInt(1);
                    }
                }
            }
            
            // 成功申请借款（笔）
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT COUNT(T6230.F01) AS sucLoanCount, IFNULL(SUM(T6230.F05-T6230.F07),0) AS loanMoney FROM S62.T6230 WHERE T6230.F02= ? AND T6230.F20 IN(?,?,?,?) "))
            {
                ps.setInt(1, userId);
                ps.setString(2, T6230_F20.HKZ.name());
                ps.setString(3, T6230_F20.YJQ.name());
                ps.setString(4, T6230_F20.YDF.name());
                ps.setString(5, T6230_F20.YZR.name());
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        basicInfo.sucLoanCount = resultSet.getInt(1);
                        basicInfo.loanMoney = resultSet.getBigDecimal(2);
                    }
                }
            }
        }
    }
    
    @Override
    public void exportGreater30(Less30[] less30s, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (less30s == null)
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
            writer.write("借款标题");
            writer.write("用户名");
            writer.write("借款金额(元)");
            writer.write("期数");
            writer.write("本期应还金额(元)");
            writer.write("剩余应还总额(元)");
            writer.write("逾期费用(元)");
            writer.write("逾期天数");
            writer.write("最近催收时间");
            writer.newLine();
            int index = 1;
            for (Less30 greater30 : less30s)
            {
                writer.write(index++);
                writer.write(greater30.loanRecordTitle);
                writer.write(greater30.userName);
                writer.write(Formater.formatAmount(greater30.loanAmount));
                writer.write(" " + greater30.loandeadline);
                writer.write(Formater.formatAmount(greater30.principalAmount));
                writer.write(Formater.formatAmount(greater30.residueAmount));
                writer.write(Formater.formatAmount(greater30.collectionAmount));
                writer.write(greater30.collectionNumber + "天");
                writer.write(Formater.formatDate(greater30.refundDay));
                writer.newLine();
            }
        }
    }
    
    @Override
    public void exportCsList(CollectionRecordInfo[] crList, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (crList == null)
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
            writer.write("借款ID");
            writer.write("借款者");
            writer.write("催收方式");
            writer.write("催收人");
            writer.write("催收时间");
            writer.write("标题");
            writer.newLine();
            int index = 1;
            for (CollectionRecordInfo crInfo : crList)
            {
                writer.write(index++);
                writer.write(crInfo.bidNum);
                writer.write(crInfo.userName);
                writer.write(crInfo.F04.getChineseName());
                writer.write(crInfo.F05);
                writer.write(Formater.formatDate(crInfo.F08));
                writer.write(crInfo.F06);
                writer.newLine();
            }
        }
    }
    
    @Override
    public String getAdminNameById(int adminId)
        throws Throwable
    {
        String sql = "SELECT F02 FROM S71.T7110 WHERE F01 = ?";
        String adminName = "";
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps = conn.prepareStatement(sql))
            {
                ps.setInt(1, adminId);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        adminName = resultSet.getString(1);
                    }
                }
            }
        }
        return adminName;
    }
    
    @Override
    public String getUserNameById(int userId)
        throws Throwable
    {
        String sql = "SELECT F02 FROM S61.T6110 WHERE F01 = ?";
        String userName = "";
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps = conn.prepareStatement(sql))
            {
                ps.setInt(1, userId);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        userName = resultSet.getString(1);
                    }
                }
            }
        }
        return userName;
    }
}
