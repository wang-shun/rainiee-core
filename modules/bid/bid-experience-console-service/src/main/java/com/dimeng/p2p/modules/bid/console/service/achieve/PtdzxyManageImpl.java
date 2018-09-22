package com.dimeng.p2p.modules.bid.console.service.achieve;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.modules.bid.console.service.PtdzxyManage;
import com.dimeng.p2p.modules.bid.console.service.entity.DfxyRecord;
import com.dimeng.p2p.modules.bid.console.service.entity.Qyjkxy;
import com.dimeng.p2p.modules.bid.console.service.entity.Zqzrxy;
import com.dimeng.p2p.modules.bid.console.service.query.DfQuery;
import com.dimeng.p2p.modules.bid.console.service.query.DzxyQuery;
import com.dimeng.p2p.modules.bid.console.service.query.ZqzrxyQuery;
import com.dimeng.util.StringHelper;

public class PtdzxyManageImpl extends AbstractBidService implements PtdzxyManage
{
    
    public PtdzxyManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<Qyjkxy> search(DzxyQuery query, T6110_F06 f06, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6110.F02 AS F01, T6239.F02 AS F02, T6239.F03 AS F03, T6230.F01 AS F04, T6230.F02 AS F05, T6230.F03 AS F06, T6231.F12 AS F07 FROM S62.T6230, S62.T6239, S61.T6110, S62.T6231 WHERE T6239.F01 = T6230.F01 AND T6230.F02 = T6110.F01 AND   T6230.F01=T6231.F01   AND T6230.F20 IN (?,?,?) AND T6110.F06 = ?");
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(T6230_F20.HKZ);
        parameters.add(T6230_F20.YJQ);
        parameters.add(T6230_F20.YDF);
        parameters.add(f06);
        if (query != null)
        {
            String account = query.getName();
            if (!StringHelper.isEmpty(account))
            {
                sql.append(" AND T6110.F02 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(account));
            }
            Timestamp datetime = query.getCreateTimeStart();
            if (datetime != null)
            {
                sql.append(" AND DATE(T6231.F12) >=?");
                parameters.add(datetime);
            }
            Timestamp datetimeEnd = query.getCreateTimeEnd();
            if (datetimeEnd != null)
            {
                sql.append(" AND DATE(T6231.F12) <=?");
                parameters.add(datetimeEnd);
            }
        }
        sql.append(" ORDER BY T6231.F12 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Qyjkxy>()
            {
                @Override
                public Qyjkxy[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<Qyjkxy> list = null;
                    while (resultSet.next())
                    {
                        Qyjkxy record = new Qyjkxy();
                        record.jkLoginName = resultSet.getString(1);
                        record.xyId = resultSet.getInt(2);
                        record.version = resultSet.getInt(3);
                        record.F01 = resultSet.getInt(4);
                        record.F02 = resultSet.getInt(5);
                        record.F03 = resultSet.getString(6);
                        record.F22 = resultSet.getTimestamp(7);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    
                    return ((list == null || list.size() == 0) ? null : list.toArray(new Qyjkxy[list.size()]));
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public PagingResult<Zqzrxy> search(ZqzrxyQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6262.F01 AS F01, T6262.F02 AS F02, T6262.F03 AS F03, T6262.F04 AS F04, T6262.F05 AS F05, T6262.F06 AS F06, T6262.F07 AS F07, T6262.F08 AS F08, T6262.F09 AS F09,  T6110.F02 AS F10 FROM S62.T6262 INNER JOIN S62.T6260 ON T6262.F02 = T6260.F01 INNER JOIN S62.T6261 ON T6260.F01 = T6261.F01 INNER JOIN S62.T6251 ON T6260.F02 = T6251.F01 INNER JOIN S61.T6110 ON T6110.F01 = T6251.F04");
        ArrayList<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            String account = query.getName();
            if (!StringHelper.isEmpty(account))
            {
                sql.append(" AND T6110.F02 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(account));
            }
            Timestamp datetime = query.getCreateTimeStart();
            if (datetime != null)
            {
                sql.append(" AND DATE(T6262.F07) >=?");
                parameters.add(datetime);
            }
            Timestamp datetimeEnd = query.getCreateTimeEnd();
            if (datetimeEnd != null)
            {
                sql.append(" AND DATE(T6262.F07) <=?");
                parameters.add(datetimeEnd);
            }
        }
        sql.append(" ORDER BY T6262.F07 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Zqzrxy>()
            {
                @Override
                public Zqzrxy[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<Zqzrxy> list = null;
                    while (resultSet.next())
                    {
                        Zqzrxy record = new Zqzrxy();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getTimestamp(7);
                        record.F08 = resultSet.getBigDecimal(8);
                        record.F09 = resultSet.getBigDecimal(9);
                        record.zrName = resultSet.getString(10);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    
                    return ((list == null || list.size() == 0) ? null : list.toArray(new Zqzrxy[list.size()]));
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public PagingResult<DfxyRecord> search(DfQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6230.F01 AS F01, T6253.F01 AS F02, T6230.F03 AS F03, jk.F02 AS F04, df.F02 AS F05, T6253.F05 AS F06, T6253.F07 AS F07, jk.F01 AS F08, df.F01 AS F09 FROM S62.T6253 INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01 INNER JOIN S61.T6110 jk ON T6253.F04 = jk.F01 INNER JOIN S61.T6110 df ON T6253.F03 = df.F01 WHERE 1 = 1");
        ArrayList<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            String name = query.getName();
            if (!StringHelper.isEmpty(name))
            {
                sql.append(" AND jk.F02 LIKE ?");
                parameters.add(getSQLConnectionProvider().allMatch(name));
            }
            
            Timestamp datetime = query.getAdvanceTimeStart();
            if (datetime != null)
            {
                sql.append(" AND DATE(T6253.F07) >=?");
                parameters.add(datetime);
            }
            Timestamp datetimeEnd = query.getAdvanceTimeEnd();
            if (datetimeEnd != null)
            {
                sql.append(" AND DATE(T6253.F07) <=?");
                parameters.add(datetimeEnd);
            }
        }
        sql.append(" ORDER BY T6253.F07 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<DfxyRecord>()
            {
                @Override
                public DfxyRecord[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<DfxyRecord> list = null;
                    while (resultSet.next())
                    {
                        DfxyRecord record = new DfxyRecord();
                        record.loanId = resultSet.getInt(1);
                        record.advanceId = resultSet.getInt(2);
                        record.loanTitle = resultSet.getString(3);
                        record.loanAccnout = resultSet.getString(4);
                        record.advanceAccount = resultSet.getString(5);
                        record.advanceSum = resultSet.getBigDecimal(6);
                        record.advanceTime = resultSet.getTimestamp(7);
                        record.loanAccnoutId = resultSet.getInt(8);
                        record.advanceAccountId = resultSet.getInt(9);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    
                    return ((list == null || list.size() == 0) ? null : list.toArray(new DfxyRecord[list.size()]));
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
}
