package com.dimeng.p2p.account.user.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.enums.T6230_F12;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6231_F19;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.account.user.service.FxbyjManage;
import com.dimeng.p2p.account.user.service.entity.Dbxxmx;
import com.dimeng.p2p.account.user.service.entity.Dfxxmx;
import com.dimeng.p2p.variables.defines.GuarantorVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.EnumParser;

public class FxbyjManageImpl extends AbstractAccountService implements FxbyjManage
{
    
    public FxbyjManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<Dbxxmx> searchDb(Paging paging)
        throws Throwable
    {
        try (final Connection connection = getConnection())
        {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT T6230.F02 AS F01, T6230.F03 AS F02, T6230.F05 AS F03, T6230.F06 AS F04, T6230.F09 AS F05,");
            sql.append(" T6230.F20 AS F06, T6230.F01 AS F07, ( SELECT CASE COUNT(1) WHEN 0 THEN 'F' ELSE 'S' END AS ISYQ FROM ");
            sql.append("S62.T6252 WHERE F02 = T6230.F01 AND F08 < ? AND F09 NOT IN ('YH','DF') ) AS F08, T6230.F12 AS F09, ");
            sql.append("T6231.F21 F10, T6231.F22 F11, T6230.F07 F12, T6110.F02 F13, T6231.F02 F14, T6231.F03 F15, T6231.F19 AS F16 FROM S62.T6230 INNER JOIN ");
            sql.append("S62.T6236 ON T6230.F01 = T6236.F02 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 INNER JOIN S61.T6110 ON ");
            sql.append("T6230.F02 = T6110.F01 WHERE T6236.F03 = ? AND T6230.F20 IN (?,?,?,?) ORDER BY F08 DESC, F06 DESC, T6230.F22 DESC ");
            List<Object> parameters = new ArrayList<Object>();
            // parameters.add(getCurrentTimestamp(connection));
            parameters.add(getCurrentDate(connection));
            parameters.add(serviceResource.getSession().getAccountId());
            parameters.add(T6230_F20.HKZ);
            parameters.add(T6230_F20.YJQ);
            parameters.add(T6230_F20.YDF);
            parameters.add(T6230_F20.YZR);
            return selectPaging(connection, new ArrayParser<Dbxxmx>()
            {
                @Override
                public Dbxxmx[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<Dbxxmx> list = null;
                    while (resultSet.next())
                    {
                        Dbxxmx record = new Dbxxmx();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = resultSet.getBigDecimal(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getInt(5);
                        record.F06 = T6230_F20.parse(resultSet.getString(6));
                        record.jkbId = resultSet.getInt(7);
                        record.F19 = EnumParser.parse(T6231_F19.class, resultSet.getString(8));
                        
                        T6230_F20 t6230_F20 = EnumParser.parse(T6230_F20.class, resultSet.getString(6));
                        if (t6230_F20 == T6230_F20.YZR)
                        {
                            record.F19 = EnumParser.parse(T6231_F19.class, resultSet.getString(16));
                        }
                        record.F12 = EnumParser.parse(T6230_F12.class, resultSet.getString(9));
                        record.dhbj = selectBigDecimal(connection, record.jkbId, record.F12);
                        T6231 t6231 = new T6231();
                        t6231.F21 = T6231_F21.parse(resultSet.getString(10));
                        t6231.F22 = resultSet.getInt(11);
                        record.F13 = t6231;
                        record.F14 = resultSet.getBigDecimal(12);
                        record.F15 = resultSet.getInt(14);
                        record.F16 = resultSet.getInt(15);
                        record.userName = resultSet.getString(13);
                        record.state = record.F06.getChineseName();
                        record.isYQ = record.F19.getChineseName();
                        record.dbfs = record.F12.getChineseName();
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return list == null ? null : list.toArray(new Dbxxmx[list.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    private BigDecimal selectBigDecimal(Connection connection, int F02, T6230_F12 t6230_F12)
        throws SQLException
    {
        List<Object> parameters = new ArrayList<>();
        StringBuffer sb =
            new StringBuffer("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F09 = ?");
        parameters.add(F02);
        parameters.add(T6252_F09.WH.name());
        if (t6230_F12 == T6230_F12.BJQEDB)
        {
            sb.append(" AND T6252.F05 = ?");
            parameters.add(FeeCode.TZ_BJ);
        }
        else if (t6230_F12 == T6230_F12.BXQEDB)
        {
            sb.append("AND T6252.F05 IN (?,?,?)");
            parameters.add(FeeCode.TZ_BJ);
            parameters.add(FeeCode.TZ_LX);
            parameters.add(FeeCode.TZ_FX);
        }
        try (PreparedStatement pstmt = connection.prepareStatement(sb.toString()))
        {
            serviceResource.setParameters(pstmt, parameters);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getBigDecimal(1);
                }
            }
        }
        return new BigDecimal(0);
    }
    
    @Override
    public PagingResult<Dfxxmx> searchDf(Paging paging)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Dfxxmx>()
            {
                
                @Override
                public Dfxxmx[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<Dfxxmx> list = null;
                    while (resultSet.next())
                    {
                        Dfxxmx record = new Dfxxmx();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getInt(3);
                        record.F04 = resultSet.getInt(4);
                        record.F05 = resultSet.getBigDecimal(5);
                        record.F06 = resultSet.getBigDecimal(6);
                        record.F07 = resultSet.getTimestamp(7);
                        record.status = EnumParser.parse(T6230_F20.class, resultSet.getString(8));
                        record.userName = resultSet.getString(9);
                        record.title = resultSet.getString(10);
                        record.state = record.status.getChineseName();
                        final ConfigureProvider configureProvider =
                            serviceResource.getResource(ConfigureProvider.class);
                        boolean isNetsign =
                            BooleanParser.parse(configureProvider.getProperty(SystemVariable.IS_HAS_NETSIGN));
                        if (isNetsign)
                        {
                            record.contractURL =
                                configureProvider.getProperty(SystemVariable.ACCOUNT_SFTG) + "?id=" + record.F02;
                        }
                        else
                        {
                            record.contractURL = "";
                        }
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return list == null ? null : list.toArray(new Dfxxmx[list.size()]);
                }
            },
                paging,
                "SELECT T6253.F01 AS F01, T6253.F02 AS F02, T6253.F03 AS F03, T6253.F04 AS F04, T6253.F05 AS F05, T6253.F06 AS F06, T6253.F07 AS F07, T6230.F20 AS F08,T6110.F02 AS F09,T6230.F03 AS F10 FROM S62.T6253 INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01 INNER JOIN S61.T6110 ON T6253.F04=T6110.F01 WHERE T6253.F03 = ? ORDER BY T6253.F07 DESC",
                serviceResource.getSession().getAccountId());
        }
    }
    
    // /**
    // * 未收金额
    // */
    // private BigDecimal getWhje(int loanId) throws Throwable {
    // try (Connection connection = getConnection()) {
    // try (PreparedStatement pstmt = connection
    // .prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE F02 = ?  AND F09 = ? AND F05 IN (?,?,?,?)"))
    // {
    // pstmt.setInt(1, loanId);
    // pstmt.setString(2, T6252_F09.WH.name());
    // pstmt.setInt(3, FeeCode.TZ_BJ);
    // pstmt.setInt(4, FeeCode.TZ_LX);
    // pstmt.setInt(5, FeeCode.TZ_FX);
    // pstmt.setInt(6, FeeCode.TZ_WYJ);
    // try (ResultSet resultSet = pstmt.executeQuery()) {
    // if (resultSet.next()) {
    // return resultSet.getBigDecimal(1);
    // }
    // }
    // }
    // }
    // return new BigDecimal(0);
    // }
    
    @Override
    public int recharge(BigDecimal amount, int type)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            int accountId = serviceResource.getSession().getAccountId();
            //int orderId = 0;
            try
            {
                serviceResource.openTransactions(connection);
                
                T6110 t6110 = getT6110(connection);
                ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
                boolean isHasGuarant =
                    Boolean.parseBoolean(configureProvider.getProperty(GuarantorVariavle.IS_HAS_GUARANTOR));
                if (t6110.F10 != T6110_F10.S && !isHasGuarant)
                {
                    throw new LogicalException("不是担保方，不能转入或转出保证金");
                }
                
                /*try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S65.T6501 SET F02 = ?,F03 = ?, F04 = ?, F07 = ?, F08 = ?, F09 = ?",
                        PreparedStatement.RETURN_GENERATED_KEYS))
                {
                    pstmt.setInt(1, type);
                    pstmt.setString(2, T6501_F03.DTJ.name());
                    pstmt.setTimestamp(3, getCurrentTimestamp(connection));
                    pstmt.setString(4, T6501_F07.HT.name());
                    pstmt.setInt(5, accountId);
                    pstmt.setInt(6, serviceResource.getSession().getAccountId());
                    pstmt.execute();
                    try (ResultSet resultSet = pstmt.getGeneratedKeys())
                    {
                        if (resultSet.next())
                        {
                            orderId = resultSet.getInt(1);
                        }
                    }
                }*/
                T6501 t6501 = new T6501();
                t6501.F02 = type;
                t6501.F03 = T6501_F03.DTJ;
                t6501.F04 = getCurrentTimestamp(connection);
                t6501.F07 = T6501_F07.HT;
                t6501.F08 = accountId;
                t6501.F09 = serviceResource.getSession().getAccountId();
                t6501.F13 = amount;
                int orderId = insertT6501(connection, t6501);
                
                try (PreparedStatement pstmt =
                    connection.prepareStatement("INSERT INTO S65.T6513 (F01,F02, F03) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE F02 = VALUES(F02), F03 = VALUES(F03)",
                        PreparedStatement.RETURN_GENERATED_KEYS))
                {
                    pstmt.setInt(1, orderId);
                    pstmt.setInt(2, accountId);
                    pstmt.setBigDecimal(3, amount);
                    pstmt.execute();
                }
                serviceResource.commit(connection);
                return orderId;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public T6101 getT6101()
        throws Throwable
    {
        T6101 info = new T6101();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT * FROM S61.T6101 WHERE T6101.F02 = ? AND T6101.F03=? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                pstmt.setString(2, T6101_F03.FXBZJZH.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        info.F01 = resultSet.getInt(1);
                        info.F02 = resultSet.getInt(2);
                        info.F03 = EnumParser.parse(T6101_F03.class, resultSet.getString(3));
                        info.F04 = resultSet.getString(4);
                        info.F05 = resultSet.getString(5);
                        info.F06 = resultSet.getBigDecimal(6);
                        info.F07 = resultSet.getTimestamp(7);
                    }
                }
            }
        }
        return info;
    }
    
    private T6110 getT6110(Connection connection)
        throws Throwable
    {
        T6110 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F15 FROM S61.T6110 WHERE T6110.F01 = ?"))
        {
            pstmt.setInt(1, serviceResource.getSession().getAccountId());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6110();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getString(2);
                    record.F03 = resultSet.getString(3);
                    record.F04 = resultSet.getString(4);
                    record.F05 = resultSet.getString(5);
                    record.F06 = T6110_F06.parse(resultSet.getString(6));
                    record.F07 = T6110_F07.parse(resultSet.getString(7));
                    record.F08 = T6110_F08.parse(resultSet.getString(8));
                    record.F09 = resultSet.getTimestamp(9);
                    record.F10 = T6110_F10.parse(resultSet.getString(10));
                    record.F15 = resultSet.getTimestamp(11);
                }
            }
        }
        return record;
    }
}
