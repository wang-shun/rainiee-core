package com.dimeng.p2p.modules.financial.console.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
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
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.modules.financial.console.service.CreditorManage;
import com.dimeng.p2p.modules.financial.console.service.entity.Creditor;
import com.dimeng.p2p.modules.financial.console.service.query.CreditorQuery;
import com.dimeng.p2p.modules.financial.console.service.query.RichCreditorQuery;
import com.dimeng.util.Formater;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.EnumParser;

public class CreditorManageImpl extends AbstractFinancialService implements CreditorManage
{
    
    public static class CreditorManageFactory implements ServiceFactory<CreditorManage>
    {
        
        @Override
        public CreditorManage newInstance(ServiceResource serviceResource)
        {
            return new CreditorManageImpl(serviceResource);
        }
    }
    
    public CreditorManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<Creditor> creditorTbzSearch(CreditorQuery creditorQuery, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6230.F25,T6230.F03,T6110.F02,T6230.F06,T6230.F09,T6250.F04,T6250.F06 AS F061,T6231.F21,T6231.F22,T6110.F06 AS invest,T6110.F10 AS isDbf FROM S62.T6250 "
                    + "INNER JOIN S62.T6230 ON T6250.F02=T6230.F01 INNER JOIN S62.T6231 ON T6231.F01=T6230.F01 INNER JOIN S61.T6110 ON T6250.F03=T6110.F01 WHERE (T6230.F20 = ? OR T6230.F20=?)");
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(T6230_F20.TBZ.name());
        parameters.add(T6230_F20.DFK.name());
        creditorTbzParameter(sql, creditorQuery, parameters);
        
        sql.append(" ORDER BY T6250.F01 DESC");
        try(Connection connection = getConnection())
        {
            PagingResult<Creditor> pagingResult = selectPaging(connection, new ArrayParser<Creditor>()
            {

                @Override
                public Creditor[] parse(ResultSet resultSet)
                        throws SQLException
                {
                    ArrayList<Creditor> list = null;
                    while (resultSet.next())
                    {
                        Creditor creditor = new Creditor();
                        creditor.creditorId = resultSet.getString(1);
                        creditor.jkbt = resultSet.getString(2);
                        creditor.userName = resultSet.getString(3);
                        creditor.yearRate = resultSet.getDouble(4);
                        creditor.deadline = resultSet.getInt(5);
                        creditor.investmentAmount = resultSet.getBigDecimal(6);
                        creditor.tenderTime = resultSet.getTimestamp(7);
                        creditor.f21 = EnumParser.parse(T6231_F21.class, resultSet.getString(8));
                        creditor.day = resultSet.getInt(9);
                        String investUserType = resultSet.getString(10);//投资人类型
                        String isDbf = resultSet.getString(11);//是否是担保方
                        if (T6110_F06.ZRR.name().equals(investUserType))
                        {
                            creditor.investUserType = "个人";
                        }
                        else if (T6110_F06.FZRR.name().equals(investUserType) && T6110_F10.S.name().equals(isDbf))
                        {
                            creditor.investUserType = "机构";
                        }
                        else if (T6110_F06.FZRR.name().equals(investUserType) && T6110_F10.F.name().equals(isDbf))
                        {
                            creditor.investUserType = "企业";
                        }
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(creditor);
                    }
                    return list == null ? null : list.toArray(new Creditor[list.size()]);
                }
            }, paging, sql.toString(), parameters);
            return pagingResult;
        }

    }
    
    @Override
    public BigDecimal creditorTbzAmountCount(CreditorQuery creditorQuery)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT IFNULL(SUM(T6250.F04),0) AS F01 FROM S62.T6250"
                    + " INNER JOIN S62.T6230 ON T6250.F02=T6230.F01 INNER JOIN S62.T6231 ON T6231.F01=T6230.F01 INNER JOIN S61.T6110 ON T6250.F03=T6110.F01 WHERE (T6230.F20 = ? OR T6230.F20=?)");
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(T6230_F20.TBZ.name());
        parameters.add(T6230_F20.DFK.name());
        creditorTbzParameter(sql, creditorQuery, parameters);
        try (Connection connection = getConnection())
        {
            return selectBigDecimal(connection, sql.toString(), parameters);
        }
    }

    private void creditorTbzParameter(StringBuilder sql, CreditorQuery creditorQuery, List<Object> parameters)
        throws ResourceNotFoundException, SQLException
    {
        if (creditorQuery != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            String string = creditorQuery.getCreditorId();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6230.F25 = ?");
                parameters.add(string);
            }
            string = creditorQuery.getUserName();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6110.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            string = creditorQuery.getLoanRecordTitle();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6230.F03 LIKE ? ");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            Timestamp timestamp = creditorQuery.getCreateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6250.F06) >= ?");
                parameters.add(timestamp);
            }
            timestamp = creditorQuery.getCreateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6250.F06) <= ?");
                parameters.add(timestamp);
            }
            String investUserType = creditorQuery.getInvestUserType();
            if (!StringHelper.isEmpty(investUserType))
            {
                if (investUserType.equals("ZRR"))
                {
                    sql.append(" AND T6110.F06 = 'ZRR' AND T6110.F10 = 'F'");
                }
                else if (investUserType.equals("FZRR"))
                {
                    sql.append(" AND T6110.F06 = 'FZRR' AND T6110.F10 = 'F'");
                }
                else if (investUserType.equals("FZRRJG"))
                {
                    sql.append(" AND T6110.F06 = 'FZRR' AND T6110.F10 = 'S'");
                }
            }
        }
    }
    
    private void creditorYfkParameter(StringBuilder sql, CreditorQuery creditorQuery, List<Object> parameters)
        throws ResourceNotFoundException, SQLException
    {
        if (creditorQuery != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            String string = creditorQuery.getCreditorId();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6251.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            string = creditorQuery.getUserName();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6110.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            string = creditorQuery.getLoanRecordTitle();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6230.F03 LIKE ? ");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            Timestamp timestamp = creditorQuery.getCreateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6250.F06) >= ?");
                parameters.add(timestamp);
            }
            timestamp = creditorQuery.getCreateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6250.F06) <= ?");
                parameters.add(timestamp);
            }
            string = creditorQuery.getCreditorType();
            if (!StringHelper.isEmpty(string))
            {
                if (string.equals("ZRR"))
                {
                    sql.append(" AND T6110.F06 = 'ZRR' AND T6110.F10 = 'F'");
                }
                else if (string.equals("FZRR"))
                {
                    sql.append(" AND T6110.F06 = 'FZRR' AND T6110.F10 = 'F'");
                }
                else if (string.equals("FZRRJG"))
                {
                    sql.append(" AND T6110.F06 = 'FZRR' AND T6110.F10 = 'S'");
                }
            }
        }
    }

    @Override
    public PagingResult<Creditor> creditorTbzRichSearch(RichCreditorQuery creditorQuery, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6230.F25,T6230.F03,T6110.F02,T6230.F06,T6230.F09,T6250.F04,T6250.F06 AS F061,T6231.F21,T6231.F22 FROM S62.T6250 "
                    + "INNER JOIN S62.T6230 ON T6250.F02=T6230.F01 INNER JOIN S62.T6231 ON T6231.F01=T6230.F01 INNER JOIN S61.T6110 ON T6250.F03=T6110.F01 WHERE (T6230.F20 = ? OR T6230.F20=?)");
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(T6230_F20.TBZ.name());
        parameters.add(T6230_F20.DFK.name());
        
        if (creditorQuery != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            String string = creditorQuery.getCreditorId();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6230.F25 = ?");
                parameters.add(string);
            }
            string = creditorQuery.getCreditTitle();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6230.F03 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            string = creditorQuery.getUserName();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6110.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            Timestamp timestamp = creditorQuery.getCreateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6250.F06) >= ?");
                parameters.add(timestamp);
            }
            timestamp = creditorQuery.getCreateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6250.F06) <= ?");
                parameters.add(timestamp);
            }
        }
        
        sql.append(" ORDER BY T6250.F01 DESC");
        try(Connection connection = getConnection())
        {
            PagingResult<Creditor> pagingResult = selectPaging(connection, new ArrayParser<Creditor>()
            {

                @Override
                public Creditor[] parse(ResultSet resultSet)
                        throws SQLException
                {
                    ArrayList<Creditor> list = null;
                    while (resultSet.next())
                    {
                        Creditor creditor = new Creditor();
                        creditor.creditorId = resultSet.getString(1);
                        creditor.jkbt = resultSet.getString(2);
                        creditor.userName = resultSet.getString(3);
                        creditor.yearRate = resultSet.getDouble(4);
                        creditor.deadline = resultSet.getInt(5);
                        creditor.investmentAmount = resultSet.getBigDecimal(6);
                        creditor.tenderTime = resultSet.getTimestamp(7);
                        creditor.f21 = EnumParser.parse(T6231_F21.class, resultSet.getString(8));
                        creditor.day = resultSet.getInt(9);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(creditor);
                    }
                    return list == null ? null : list.toArray(new Creditor[list.size()]);
                }
            }, paging, sql.toString(), parameters);

            return pagingResult;
        }

    }
    
    @Override
    public PagingResult<Creditor> creditorYfkSearch(CreditorQuery creditorQuery, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6251.F02 F01,T6230.F03,T6110.F02,T6230.F06,T6230.F09,T6251.F07,T6250.F06 AS F091,T6231.F21,T6231.F22,T6110.F06 AS creditorType,T6110.F10 AS isDbf FROM S62.T6251 "
                    + " INNER JOIN S62.T6230 ON T6251.F03=T6230.F01 INNER JOIN S62.T6231 ON T6231.F01=T6230.F01 INNER JOIN S61.T6110 ON T6251.F04=T6110.F01 INNER JOIN S62.T6250 ON T6250.F01 = T6251.F11 WHERE T6251.F07 >0 AND  T6230.F20 = ?");
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(T6230_F20.HKZ.name());
        creditorYfkParameter(sql, creditorQuery, parameters);
        
        sql.append(" ORDER BY T6251.F01 DESC");
        try(Connection connection = getConnection())
        {
            PagingResult<Creditor> pagingResult = selectPaging(connection, new ArrayParser<Creditor>()
            {

                @Override
                public Creditor[] parse(ResultSet resultSet)
                        throws SQLException
                {
                    ArrayList<Creditor> list = null;
                    while (resultSet.next())
                    {
                        Creditor creditor = new Creditor();
                        creditor.creditorId = resultSet.getString(1);
                        creditor.jkbt = resultSet.getString(2);
                        creditor.userName = resultSet.getString(3);
                        creditor.yearRate = resultSet.getDouble(4);
                        creditor.deadline = resultSet.getInt(5);
                        creditor.investmentAmount = resultSet.getBigDecimal(6);
                        creditor.tenderTime = resultSet.getTimestamp(7);
                        creditor.f21 = EnumParser.parse(T6231_F21.class, resultSet.getString(8));
                        creditor.day = resultSet.getInt(9);
                        String creditorType = resultSet.getString(10);//债权人类型
                        String isDbf = resultSet.getString(11);//是否是担保方
                        if (T6110_F06.ZRR.name().equals(creditorType))
                        {
                            creditor.creditorType = "个人";
                        }
                        else if (T6110_F06.FZRR.name().equals(creditorType) && T6110_F10.S.name().equals(isDbf))
                        {
                            creditor.creditorType = "机构";
                        }
                        else if (T6110_F06.FZRR.name().equals(creditorType) && T6110_F10.F.name().equals(isDbf))
                        {
                            creditor.creditorType = "企业";
                        }
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(creditor);
                    }
                    return list == null ? null : list.toArray(new Creditor[list.size()]);
                }
            }, paging, sql.toString(), parameters);
            return pagingResult;
        }
    }
    
    @Override
    public BigDecimal creditorYfkAmountCount(CreditorQuery creditorQuery)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT IFNULL(SUM(T6251.F07),0) AS F01 FROM S62.T6251 "
                    + " INNER JOIN S62.T6230 ON T6251.F03=T6230.F01 INNER JOIN S62.T6231 ON T6231.F01=T6230.F01 INNER JOIN S61.T6110 ON T6251.F04=T6110.F01 INNER JOIN S62.T6250 ON T6250.F01 = T6251.F11 WHERE T6251.F07 >0 AND  T6230.F20 = ?");
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(T6230_F20.HKZ.name());
        creditorYfkParameter(sql, creditorQuery, parameters);
        try (Connection connection = getConnection())
        {
            return selectBigDecimal(connection, sql.toString(), parameters);
        }
    }
    
    @Override
    public PagingResult<Creditor> creditorYfkRichSearch(RichCreditorQuery creditorQuery, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6251.F02 F01,T6230.F03,T6110.F02,T6230.F06,T6230.F09,T6251.F07,T6251.F09 AS F091,T6231.F21,T6231.F22 FROM S62.T6251 "
                    + " INNER JOIN S62.T6230 ON T6251.F03=T6230.F01 INNER JOIN S62.T6231 ON T6231.F01=T6230.F01 INNER JOIN S61.T6110 ON T6251.F04=T6110.F01 WHERE T6251.F07 >0 AND  T6230.F20 = ?");
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(T6230_F20.HKZ.name());
        
        if (creditorQuery != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            String string = creditorQuery.getCreditorId();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6251.F02 = ?");
                parameters.add(string);
            }
            string = creditorQuery.getCreditTitle();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6230.F03 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            string = creditorQuery.getUserName();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6110.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            Timestamp timestamp = creditorQuery.getCreateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6251.F09) >= ?");
                parameters.add(timestamp);
            }
            timestamp = creditorQuery.getCreateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6251.F09) <= ?");
                parameters.add(timestamp);
            }
        }
        
        sql.append(" ORDER BY T6251.F01 DESC");
        try(Connection connection = getConnection())
        {
            PagingResult<Creditor> pagingResult = selectPaging(connection, new ArrayParser<Creditor>()
            {

                @Override
                public Creditor[] parse(ResultSet resultSet)
                        throws SQLException
                {
                    ArrayList<Creditor> list = null;
                    while (resultSet.next())
                    {
                        Creditor creditor = new Creditor();
                        creditor.creditorId = resultSet.getString(1);
                        creditor.jkbt = resultSet.getString(2);
                        creditor.userName = resultSet.getString(3);
                        creditor.yearRate = resultSet.getDouble(4);
                        creditor.deadline = resultSet.getInt(5);
                        creditor.investmentAmount = resultSet.getBigDecimal(6);
                        creditor.tenderTime = resultSet.getTimestamp(7);
                        creditor.f21 = EnumParser.parse(T6231_F21.class, resultSet.getString(8));
                        creditor.day = resultSet.getInt(9);
                        String creditorType = resultSet.getString(10);//债权人类型
                        String isDbf = resultSet.getString(11);//是否是担保方
                        if (T6110_F06.ZRR.name().equals(creditorType))
                        {
                            creditor.creditorType = "个人";
                        }
                        else if (T6110_F06.FZRR.name().equals(creditorType) && T6110_F10.S.name().equals(isDbf))
                        {
                            creditor.creditorType = "机构";
                        }
                        else if (T6110_F06.FZRR.name().equals(creditorType) && T6110_F10.F.name().equals(isDbf))
                        {
                            creditor.creditorType = "企业";
                        }
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(creditor);
                    }
                    return list == null ? null : list.toArray(new Creditor[list.size()]);
                }
            }, paging, sql.toString(), parameters);
            return pagingResult;
        }
    }
    
    @Override
    public PagingResult<Creditor> creditorYjqSearch(CreditorQuery creditorQuery, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6251.F02 F01,T6230.F03,T6110.F02,T6230.F06,T6230.F09,T6251.F06 F061,T6231.F13 AS F091,T6231.F21,T6231.F22,T6110.F06 AS creditorType,T6110.F10 AS isDbf FROM S62.T6251 "
                    + " INNER JOIN S62.T6230 ON T6251.F03=T6230.F01 INNER JOIN S61.T6110 ON T6251.F04=T6110.F01 INNER JOIN S62.T6231 ON T6231.F01 = T6230.F01 WHERE T6230.F20 = ?");
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(T6230_F20.YJQ.name());
        creditorYjqParameter(sql, creditorQuery, parameters);
        sql.append(" GROUP BY T6251.F02,T6251.F04,T6251.F03 ");
        sql.append(" ORDER BY T6251.F01 DESC");
        
        try(Connection connection = getConnection())
        {
            PagingResult<Creditor> pagingResult = selectPaging(connection, new ArrayParser<Creditor>()
            {

                @Override
                public Creditor[] parse(ResultSet resultSet)
                        throws SQLException
                {
                    ArrayList<Creditor> list = null;
                    while (resultSet.next())
                    {
                        Creditor creditor = new Creditor();
                        creditor.creditorId = resultSet.getString(1);
                        creditor.jkbt = resultSet.getString(2);
                        creditor.userName = resultSet.getString(3);
                        creditor.yearRate = resultSet.getDouble(4);
                        creditor.deadline = resultSet.getInt(5);
                        creditor.investmentAmount = resultSet.getBigDecimal(6);
                        creditor.tenderTime = resultSet.getTimestamp(7);
                        creditor.f21 = EnumParser.parse(T6231_F21.class, resultSet.getString(8));
                        creditor.day = resultSet.getInt(9);
                        String creditorType = resultSet.getString(10);//债权人类型
                        String isDbf = resultSet.getString(11);//是否是担保方
                        if (T6110_F06.ZRR.name().equals(creditorType))
                        {
                            creditor.creditorType = "个人";
                        }
                        else if (T6110_F06.FZRR.name().equals(creditorType) && T6110_F10.S.name().equals(isDbf))
                        {
                            creditor.creditorType = "机构";
                        }
                        else if (T6110_F06.FZRR.name().equals(creditorType) && T6110_F10.F.name().equals(isDbf))
                        {
                            creditor.creditorType = "企业";
                        }
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(creditor);
                    }
                    return list == null ? null : list.toArray(new Creditor[list.size()]);
                }
            }, paging, sql.toString(), parameters);
            return pagingResult;
        }

    }
    
    @Override
    public BigDecimal creditorYjqAmountCount(CreditorQuery creditorQuery)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT IFNULL(SUM(T6251.F06),0) AS F01 FROM S62.T6251"
                    + " INNER JOIN S62.T6230 ON T6251.F03=T6230.F01 INNER JOIN S61.T6110 ON T6251.F04=T6110.F01 INNER JOIN S62.T6231 ON T6231.F01 = T6230.F01 WHERE T6230.F20 = ?");
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(T6230_F20.YJQ.name());
        creditorYjqParameter(sql, creditorQuery, parameters);
        try (Connection connection = getConnection())
        {
            return selectBigDecimal(connection, sql.toString(), parameters);
        }
    }

    private void creditorYjqParameter(StringBuilder sql, CreditorQuery creditorQuery, List<Object> parameters)
        throws ResourceNotFoundException, SQLException
    {
        if (creditorQuery != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            String string = creditorQuery.getCreditorId();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6251.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            string = creditorQuery.getUserName();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6110.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            string = creditorQuery.getLoanRecordTitle();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6230.F03 LIKE ? ");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            Timestamp timestamp = creditorQuery.getCreateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6231.F13) >= ?");
                parameters.add(timestamp);
            }
            timestamp = creditorQuery.getCreateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6231.F13) <= ?");
                parameters.add(timestamp);
            }
            string = creditorQuery.getCreditorType();
            if (!StringHelper.isEmpty(string))
            {
                if (string.equals("ZRR"))
                {
                    sql.append(" AND T6110.F06 = 'ZRR' AND T6110.F10 = 'F'");
                }
                else if (string.equals("FZRR"))
                {
                    sql.append(" AND T6110.F06 = 'FZRR' AND T6110.F10 = 'F'");
                }
                else if (string.equals("FZRRJG"))
                {
                    sql.append(" AND T6110.F06 = 'FZRR' AND T6110.F10 = 'S'");
                }
            }
        }
    }

    @Override
    public PagingResult<Creditor> creditorYjqRichSearch(RichCreditorQuery creditorQuery, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6251.F02 F01,T6230.F03,T6110.F02,T6230.F06,T6230.F09,T6251.F06 F061,T6231.F13 AS F091,T6231.F21,T6231.F22 FROM S62.T6251 "
                    + "INNER JOIN S62.T6230 ON T6251.F03=T6230.F01 INNER JOIN S61.T6110 ON T6251.F04=T6110.F01 INNER JOIN S62.T6231 ON T6231.F01 = T6230.F01 WHERE T6230.F20 = ?");
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(T6230_F20.YJQ.name());
        
        if (creditorQuery != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            String string = creditorQuery.getCreditorId();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6251.F02 = ?");
                parameters.add(string);
            }
            string = creditorQuery.getCreditTitle();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6230.F03 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            string = creditorQuery.getUserName();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6110.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            Timestamp timestamp = creditorQuery.getCreateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6231.F13) >= ?");
                parameters.add(timestamp);
            }
            timestamp = creditorQuery.getCreateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6231.F13) <= ?");
                parameters.add(timestamp);
            }
        }
        sql.append(" GROUP BY T6251.F04,T6251.F03");
        sql.append(" ORDER BY T6251.F01 DESC");
        try(Connection connection = getConnection())
        {
            PagingResult<Creditor> pagingResult = selectPaging(connection, new ArrayParser<Creditor>()
            {

                @Override
                public Creditor[] parse(ResultSet resultSet)
                        throws SQLException
                {
                    ArrayList<Creditor> list = null;
                    while (resultSet.next())
                    {
                        Creditor creditor = new Creditor();
                        creditor.creditorId = resultSet.getString(1);
                        creditor.jkbt = resultSet.getString(2);
                        creditor.userName = resultSet.getString(3);
                        creditor.yearRate = resultSet.getDouble(4);
                        creditor.deadline = resultSet.getInt(5);
                        creditor.investmentAmount = resultSet.getBigDecimal(6);
                        creditor.tenderTime = resultSet.getTimestamp(7);
                        creditor.f21 = EnumParser.parse(T6231_F21.class, resultSet.getString(8));
                        creditor.day = resultSet.getInt(9);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(creditor);
                    }
                    return list == null ? null : list.toArray(new Creditor[list.size()]);
                }
            }, paging, sql.toString(), parameters);
            return pagingResult;
        }
    }
    
    @Override
    public PagingResult<Creditor> creditorYzcSearch(CreditorQuery creditorQuery, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6251.F02,T6230.F03,T6262.F03 AS F031,T6251.F04,T6230.F06,T6230.F09,T6262.F04 F041,T6262.F07,T6262.F05 F051,T6231.F21,T6231.F22,mcz.F06 AS mczType,mcz.F10 AS mczIsDbf,mrz.F06 AS mrzType,mrz.F10 AS mrzIsDbf FROM S62.T6262 INNER JOIN S62.T6260 ON T6262.F02=T6260.F01 INNER JOIN S62.T6251 ON T6260.F02=T6251.F01 INNER JOIN S62.T6230 ON T6251.F03=T6230.F01 INNER JOIN S62.T6231 ON T6231.F01=T6230.F01 INNER JOIN S61.T6110 AS mrz ON T6262.F03 = mrz.F01 INNER JOIN S61.T6110 AS mcz ON T6251.F04 = mcz.F01 WHERE 1=1");
        ArrayList<Object> parameters = new ArrayList<>();
        creditorYzcParameter(sql, creditorQuery, parameters);
        
        sql.append("  GROUP BY T6262.F03,T6260.F02,T6251.F04 ORDER BY T6262.F01 DESC");
        try(Connection connection = getConnection())
        {
            PagingResult<Creditor> pagingResult = selectPaging(connection, new ArrayParser<Creditor>()
            {

                @Override
                public Creditor[] parse(ResultSet resultSet)
                        throws SQLException
                {
                    ArrayList<Creditor> list = null;
                    while (resultSet.next())
                    {
                        Creditor creditor = new Creditor();
                        creditor.creditorId = resultSet.getString(1);
                        creditor.jkbt = resultSet.getString(2);
                        creditor.mrzName = getMcrName(resultSet.getInt(3));
                        creditor.userName = getMcrName(resultSet.getInt(4));
                        creditor.yearRate = resultSet.getDouble(5);
                        creditor.deadline = resultSet.getInt(6);
                        creditor.gmjg = resultSet.getBigDecimal(7);
                        creditor.tenderTime = resultSet.getTimestamp(8);
                        creditor.srjg = resultSet.getBigDecimal(9);
                        creditor.f21 = EnumParser.parse(T6231_F21.class, resultSet.getString(10));
                        creditor.day = resultSet.getInt(11);
                        String mczType = resultSet.getString(12);//卖出者类型
                        String mczIsDbf = resultSet.getString(13);//卖出者是否为担保方
                        if (T6110_F06.ZRR.name().equals(mczType))
                        {
                            creditor.sellUserType = "个人";
                        }
                        else if (T6110_F06.FZRR.name().equals(mczType) && T6110_F10.S.name().equals(mczIsDbf))
                        {
                            creditor.sellUserType = "机构";
                        }
                        else if (T6110_F06.FZRR.name().equals(mczType) && T6110_F10.F.name().equals(mczIsDbf))
                        {
                            creditor.sellUserType = "企业";
                        }
                        String mrzType = resultSet.getString(14);//买入者类型
                        String mrzIsDbf = resultSet.getString(15);//买入者是否为担保方
                        if (T6110_F06.ZRR.name().equals(mrzType))
                        {
                            creditor.buyUserType = "个人";
                        }
                        else if (T6110_F06.FZRR.name().equals(mrzType) && T6110_F10.S.name().equals(mrzIsDbf))
                        {
                            creditor.buyUserType = "机构";
                        }
                        else if (T6110_F06.FZRR.name().equals(mrzType) && T6110_F10.F.name().equals(mrzIsDbf))
                        {
                            creditor.buyUserType = "企业";
                        }
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }

                        list.add(creditor);
                    }
                    return list == null ? null : list.toArray(new Creditor[list.size()]);
                }
            }, paging, sql.toString(), parameters);
            return pagingResult;
        }
    }
    
    @Override
    public Creditor creditorYzcAmountCount(CreditorQuery creditorQuery)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT IFNULL(SUM(T6262.F04),0) AS F01,IFNULL(SUM(T6262.F05),0) AS F02 FROM S62.T6262 "
                    + " INNER JOIN S62.T6260 ON T6262.F02=T6260.F01 INNER JOIN S62.T6251 ON T6260.F02=T6251.F01 INNER JOIN S62.T6230 ON T6251.F03=T6230.F01 INNER JOIN S62.T6231 ON T6231.F01=T6230.F01 INNER JOIN S61.T6110 AS mrz ON T6262.F03 = mrz.F01 INNER JOIN S61.T6110 AS mcz ON T6251.F04 = mcz.F01 WHERE 1=1");
        List<Object> parameters = new ArrayList<Object>();
        // sql语句和查询参数处理
        creditorYzcParameter(sql, creditorQuery, parameters);
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Creditor>()
            {
                @Override
                public Creditor parse(ResultSet resultSet)
                    throws SQLException
                {
                    Creditor count = new Creditor();
                    if (resultSet.next())
                    {
                        count.creditorValue = resultSet.getBigDecimal(1);
                        count.transferValue = resultSet.getBigDecimal(2);
                    }
                    return count;
                }
            }, sql.toString(), parameters);
        }
    }

    private void creditorYzcParameter(StringBuilder sql, CreditorQuery creditorQuery, List<Object> parameters)
        throws ResourceNotFoundException, SQLException
    {
        if (creditorQuery != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            String string = creditorQuery.getCreditorId();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6251.F02 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            string = creditorQuery.getLoanRecordTitle();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6230.F03 LIKE ? ");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            Timestamp timestamp = creditorQuery.getCreateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6262.F07) >= ?");
                parameters.add(timestamp);
            }
            timestamp = creditorQuery.getCreateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6262.F07) <= ?");
                parameters.add(timestamp);
            }
            string = creditorQuery.getSellUserType();
            if (!StringHelper.isEmpty(string))
            {
                if (string.equals("ZRR"))
                {
                    sql.append(" AND mcz.F06 = 'ZRR' AND mcz.F10 = 'F'");
                }
                else if (string.equals("FZRR"))
                {
                    sql.append(" AND mcz.F06 = 'FZRR' AND mcz.F10 = 'F'");
                }
                else if (string.equals("FZRRJG"))
                {
                    sql.append(" AND mcz.F06 = 'FZRR' AND mcz.F10 = 'S'");
                }
            }
            string = creditorQuery.getBugUserType();
            if (!StringHelper.isEmpty(string))
            {
                if (string.equals("ZRR"))
                {
                    sql.append(" AND mrz.F06 = 'ZRR' AND mrz.F10 = 'F'");
                }
                else if (string.equals("FZRR"))
                {
                    sql.append(" AND mrz.F06 = 'FZRR' AND mrz.F10 = 'F'");
                }
                else if (string.equals("FZRRJG"))
                {
                    sql.append(" AND mrz.F06 = 'FZRR' AND mrz.F10 = 'S'");
                }
            }
        }
    }

    @Override
    public PagingResult<Creditor> creditorYzcRichSearch(RichCreditorQuery creditorQuery, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6251.F02,T6230.F03,T6262.F03 AS F031,T6251.F04,T6230.F06,T6230.F09,T6262.F04 F041,T6262.F07,T6262.F05 F051,T6231.F21,T6231.F22 FROM S62.T6262 INNER JOIN S62.T6260 ON T6262.F02=T6260.F01 INNER JOIN S62.T6251 ON T6260.F02=T6251.F01 INNER JOIN S62.T6230 ON T6251.F03=T6230.F01 INNER JOIN S62.T6231 ON T6231.F01=T6230.F01 WHERE 1=1");
        ArrayList<Object> parameters = new ArrayList<>();
        
        if (creditorQuery != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            String string = creditorQuery.getCreditorId();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6251.F02 = ?");
                parameters.add(string);
            }
            string = creditorQuery.getCreditTitle();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6230.F03 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            Timestamp timestamp = creditorQuery.getCreateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6262.F07) >= ?");
                parameters.add(timestamp);
            }
            timestamp = creditorQuery.getCreateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6262.F07) <= ?");
                parameters.add(timestamp);
            }
        }
        
        sql.append("  GROUP BY T6262.F03,T6260.F02,T6251.F04 ORDER BY T6262.F01 DESC");
        try(Connection connection = getConnection())
        {
            PagingResult<Creditor> pagingResult = selectPaging(connection, new ArrayParser<Creditor>()
            {

                @Override
                public Creditor[] parse(ResultSet resultSet)
                        throws SQLException
                {
                    ArrayList<Creditor> list = null;
                    while (resultSet.next())
                    {
                        Creditor creditor = new Creditor();
                        creditor.creditorId = resultSet.getString(1);
                        creditor.jkbt = resultSet.getString(2);
                        creditor.mrzName = getMcrName(resultSet.getInt(3));
                        creditor.userName = getMcrName(resultSet.getInt(4));
                        creditor.yearRate = resultSet.getDouble(5);
                        creditor.deadline = resultSet.getInt(6);
                        creditor.gmjg = resultSet.getBigDecimal(7);
                        creditor.tenderTime = resultSet.getTimestamp(8);
                        creditor.srjg = resultSet.getBigDecimal(9);
                        creditor.f21 = EnumParser.parse(T6231_F21.class, resultSet.getString(10));
                        creditor.day = resultSet.getInt(11);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }

                        list.add(creditor);
                    }
                    return list == null ? null : list.toArray(new Creditor[list.size()]);
                }
            }, paging, sql.toString(), parameters);
            return pagingResult;
        }
    }
    
    private String getMcrName(int id)
        throws SQLException
    {
        if (id <= 0)
        {
            return "";
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT T6110.F02 FROM S61.T6110 WHERE T6110.F01=?"))
            {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getString(1);
                    }
                }
            }
        }
        return "";
    }
    
    @Override
    public void export(Creditor[] creditors, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (creditors == null)
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
            writer.write("债权ID");
            writer.write("借款标题");
            writer.write("债权人");
            writer.write("债权人类型");
            writer.write("年化利率");
            writer.write("期限 ");
            writer.write("持有债权金额(元)");
            writer.write("投资时间");
            writer.newLine();
            int index = 1;
            for (Creditor creditor : creditors)
            {
                writer.write(index++);
                writer.write(creditor.creditorId);
                writer.write(creditor.jkbt);
                writer.write(creditor.userName);
                writer.write(creditor.creditorType);
                writer.write(Formater.formatRate(creditor.yearRate));
                if (creditor.f21 == T6231_F21.S)
                {
                    writer.write(creditor.day + "天");
                }
                else
                {
                    writer.write(creditor.deadline + "个月");
                }
                writer.write(Formater.formatAmount(creditor.investmentAmount));
                writer.write(DateTimeParser.format(creditor.tenderTime)+ "\t");
                writer.newLine();
            }
        }
    }
    
    @Override
    public void exportTbz(Creditor[] creditors, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (creditors == null)
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
            writer.write("借款标题");
            writer.write("投资人");
            writer.write("投资人类型");
            writer.write("年化利率");
            writer.write("期限 ");
            writer.write("持有债权金额(元)");
            writer.write("投资时间");
            writer.newLine();
            int index = 1;
            for (Creditor creditor : creditors)
            {
                writer.write(index++);
                writer.write(creditor.creditorId);
                writer.write(creditor.jkbt);
                writer.write(creditor.userName);
                writer.write(creditor.investUserType);
                writer.write(Formater.formatRate(creditor.yearRate));
                if (creditor.f21 == T6231_F21.S)
                {
                    writer.write(creditor.day + "天");
                }
                else
                {
                    writer.write(creditor.deadline + "个月");
                }
                writer.write(Formater.formatAmount(creditor.investmentAmount));
                writer.write(DateTimeParser.format(creditor.tenderTime)+ "\t");
                writer.newLine();
            }
        }
    }
    
    @Override
    public void exportYjq(Creditor[] creditors, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (creditors == null)
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
            writer.write("债权ID");
            writer.write("借款标题");
            writer.write("债权人");
            writer.write("债权人类型");
            writer.write("年化利率");
            writer.write("期限 ");
            writer.write("原始债权金额(元)");
            writer.write("结清时间");
            writer.newLine();
            int index = 1;
            for (Creditor creditor : creditors)
            {
                writer.write(index++);
                writer.write(creditor.creditorId);
                writer.write(creditor.jkbt);
                writer.write(creditor.userName);
                writer.write(creditor.creditorType);
                writer.write(Formater.formatRate(creditor.yearRate));
                if (creditor.f21 == T6231_F21.S)
                {
                    writer.write(creditor.day + "天");
                }
                else
                {
                    writer.write(creditor.deadline + "个月");
                }
                writer.write(Formater.formatAmount(creditor.investmentAmount));
                writer.write(DateTimeParser.format(creditor.tenderTime)+ "\t");
                writer.newLine();
            }
        }
        
    }
    
    @Override
    public void exportYzc(Creditor[] creditors, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (creditors == null)
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
            writer.write("债权ID");
            writer.write("借款标题");
            writer.write("卖出者");
            writer.write("卖出者类型");
            writer.write("买入者");
            writer.write("买入者类型");
            writer.write("年化利率");
            writer.write("期限 ");
            writer.write("债权价值(元)");
            writer.write("转让价格(元)");
            writer.write("投资时间");
            writer.newLine();
            int index = 1;
            for (Creditor creditor : creditors)
            {
                writer.write(index++);
                writer.write(creditor.creditorId);
                writer.write(creditor.jkbt);
                writer.write(creditor.userName);
                writer.write(creditor.sellUserType);
                writer.write(creditor.mrzName);
                writer.write(creditor.buyUserType);
                writer.write(Formater.formatRate(creditor.yearRate));
                if (creditor.f21 == T6231_F21.S)
                {
                    writer.write(creditor.day + "天");
                }
                else
                {
                    writer.write(creditor.deadline + "个月");
                }
                writer.write(Formater.formatAmount(creditor.gmjg));
                writer.write(Formater.formatAmount(creditor.srjg));
                writer.write(DateTimeParser.format(creditor.tenderTime)+ "\t");
                writer.newLine();
            }
        }
        
    }
}