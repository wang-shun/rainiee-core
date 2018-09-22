package com.dimeng.p2p.modules.account.console.service.achieve;

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

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.enums.T5123_F03;
import com.dimeng.p2p.S51.enums.T5123_F04;
import com.dimeng.p2p.S51.enums.T5123_F06;
import com.dimeng.p2p.S51.enums.T5127_F02;
import com.dimeng.p2p.S51.enums.T5127_F03;
import com.dimeng.p2p.S51.enums.T5127_F06;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6112;
import com.dimeng.p2p.S61.entities.T6113;
import com.dimeng.p2p.S61.entities.T6121;
import com.dimeng.p2p.S61.entities.T6122;
import com.dimeng.p2p.S61.entities.T6141;
import com.dimeng.p2p.S61.entities.T6142;
import com.dimeng.p2p.S61.entities.T6143;
import com.dimeng.p2p.S61.entities.T6145;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6110_F12;
import com.dimeng.p2p.S61.enums.T6110_F13;
import com.dimeng.p2p.S61.enums.T6118_F03;
import com.dimeng.p2p.S61.enums.T6118_F04;
import com.dimeng.p2p.S61.enums.T6118_F09;
import com.dimeng.p2p.S61.enums.T6120_F05;
import com.dimeng.p2p.S61.enums.T6121_F05;
import com.dimeng.p2p.S61.enums.T6141_F03;
import com.dimeng.p2p.S61.enums.T6141_F04;
import com.dimeng.p2p.S61.enums.T6143_F03;
import com.dimeng.p2p.S61.enums.T6145_F07;
import com.dimeng.p2p.S61.enums.T6147_F04;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S71.entities.T7110;
import com.dimeng.p2p.S71.enums.T7110_F05;
import com.dimeng.p2p.common.enums.Sex;
import com.dimeng.p2p.modules.account.console.service.GrManage;
import com.dimeng.p2p.modules.account.console.service.entity.Attestation;
import com.dimeng.p2p.modules.account.console.service.entity.BasicInfo;
import com.dimeng.p2p.modules.account.console.service.entity.Dbxxmx;
import com.dimeng.p2p.modules.account.console.service.entity.Dfxxmx;
import com.dimeng.p2p.modules.account.console.service.entity.Grxx;
import com.dimeng.p2p.modules.account.console.service.entity.LoanRecord;
import com.dimeng.p2p.modules.account.console.service.entity.Rzxx;
import com.dimeng.p2p.modules.account.console.service.entity.T6143EX;
import com.dimeng.p2p.modules.account.console.service.entity.TenderRecord;
import com.dimeng.p2p.modules.account.console.service.query.DbxxmxQuery;
import com.dimeng.p2p.modules.account.console.service.query.DfxxmxQuery;
import com.dimeng.p2p.modules.account.console.service.query.LoanRecordQuery;
import com.dimeng.p2p.modules.account.console.service.query.TenderRecordQuery;
import com.dimeng.p2p.variables.defines.BusinessVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.io.CVSWriter;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.EnumParser;

public class GrManageImpl extends AbstractUserService implements GrManage
{
    
    public GrManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<Grxx> searchGrxx(Grxx query, Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        if (query.toCheckVideo)
        {
            sql.append("SELECT T6141.F01 AS F01, T6141.F02 AS F02, T6141.F03 AS F03, T6141.F07 AS F04, T6110.F02 AS F05, T6110.F04 AS F06, T6110.F05 AS F07, T6118.F09 AS F08, T6145.F07 AS F09 "
                + "FROM S61.T6145 INNER JOIN S61.T6141 ON T6141.F01 = T6145.F02 INNER JOIN S61.T6110 ON T6110.F01 = T6145.F02 AND T6110.F06 = ? INNER JOIN S61.T6118 ON T6118.F01 = T6145.F02 ");
            sql.append(" WHERE T6145.F07 = 'DSH' AND T6118.F09 = 'BTG' ");
        }
        else
        {
            sql.append("SELECT T6141.F01 AS F01, T6141.F02 AS F02, T6141.F03 AS F03, T6141.F07 AS F04, T6110.F02 AS F05, T6110.F04 AS F06, T6110.F05 AS F07, T6118.F09 AS F08, "
                + "(SELECT F07 FROM S61.T6145 WHERE T6145.F07 = 'DSH' AND T6145.F02 = T6141.F01 LIMIT 1) AS F09 ");
            sql.append(" FROM S61.T6141 INNER JOIN S61.T6110 ON T6141.F01 = T6110.F01 INNER JOIN S61.T6118 ON T6118.F01 = T6141.F01 WHERE T6110.F06 = ?");
        }
        
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(T6110_F06.ZRR.name());
        sql.append(" AND T6110.F13 = ?");
        parameters.add(T6110_F13.F);
        String name = query.name;
        SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
        if (!StringHelper.isEmpty(name))
        {
            sql.append(" AND T6141.F02 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(name));
        }
        T6141_F03 xqlx = query.xqlx;
        if (xqlx != null)
        {
            sql.append(" AND T6141.F03 = ?");
            parameters.add(xqlx);
        }
        String userName = query.userName;
        if (!StringHelper.isEmpty(userName))
        {
            sql.append(" AND T6110.F02 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(userName));
        }
        String phone = query.phone;
        if (!StringHelper.isEmpty(phone))
        {
            sql.append(" AND T6110.F04 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(phone));
        }
        String email = query.email;
        if (!StringHelper.isEmpty(email))
        {
            sql.append(" AND T6110.F05 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(email));
        }
        if (query.toCheckVideo)
        {
            sql.append("ORDER BY T6145.F01 DESC");
        }
        else
        {
            sql.append(" ORDER BY T6110.F09 DESC");
        }
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Grxx>()
            {
                
                @Override
                public Grxx[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<Grxx> list = null;
                    while (resultSet.next())
                    {
                        Grxx record = new Grxx();
                        record.id = resultSet.getInt(1);
                        record.name = resultSet.getString(2);
                        record.xqlx = T6141_F03.parse(resultSet.getString(3));
                        record.sfzh = resultSet.getString(4);
                        record.userName = resultSet.getString(5);
                        record.phone = resultSet.getString(6);
                        record.email = resultSet.getString(7);
                        record.kfjl = getKfjl(resultSet.getInt(1));
                        record.videoExamineStatus = resultSet.getString(8);
                        String existVideoStatus = resultSet.getString(9);
                        if (!StringHelper.isEmpty(existVideoStatus)
                            && T6118_F09.BTG.name().equals(record.videoExamineStatus))
                        {
                            record.videoExamineStatus = existVideoStatus;
                        }
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new Grxx[list.size()]));
                }
            },
                paging,
                sql.toString(),
                parameters);
        }
    }
    
    @Override
    public PagingResult<Grxx> search(Grxx query, Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM (SELECT T6141.F01 AS F01, T6141.F02 AS F02, T6141.F03 AS F03, T6141.F07 AS F04, T6110.F02 AS F05, T6110.F04 AS F06, T6110.F05 AS F07, ");
        sql.append("IFNULL(( SELECT T6120.F05 FROM S61.T6120 INNER JOIN S51.T5123 ON T6120.F02 = T5123.F01 WHERE T6120.F01 = T6110.F01 AND T5123.F04 = 'QY' AND T6120.F05 = 'DSH' LIMIT 1),0) AS F08");
        sql.append(",T6110.F09 AS F09,T6110.F08 AS F10,T6110.F14 AS F11,T6110.F07 AS F12 FROM S61.T6141 INNER JOIN S61.T6110 ON T6141.F01 = T6110.F01 WHERE T6110.F06 = ?");
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(T6110_F06.ZRR.name());
        sql.append(" AND T6110.F13 = ?");
        parameters.add(T6110_F13.F);
        String name = query.name;
        SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
        if (!StringHelper.isEmpty(name))
        {
            sql.append(" AND T6141.F02 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(name));
        }
        T6141_F03 xqlx = query.xqlx;
        if (xqlx != null)
        {
            sql.append(" AND T6141.F03 = ?");
            parameters.add(xqlx);
        }
        String userName = query.userName;
        if (!StringHelper.isEmpty(userName))
        {
            sql.append(" AND T6110.F02 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(userName));
        }
        String phone = query.phone;
        if (!StringHelper.isEmpty(phone))
        {
            sql.append(" AND T6110.F04 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(phone));
        }
        
        String email = query.email;
        if (!StringHelper.isEmpty(email))
        {
            sql.append(" AND T6110.F05 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(email));
        }
        Timestamp startTime = query.startTime;
        if (startTime != null)
        {
            sql.append(" AND DATE(T6110.F09) >= ?");
            parameters.add(startTime);
        }
        Timestamp endTime = query.endTime;
        if (endTime != null)
        {
            sql.append(" AND DATE(T6110.F09) <= ?");
            parameters.add(endTime);
        }
        if (!StringHelper.isEmpty(query.zcType))
        {
            sql.append(" AND T6110.F08 = ? ");
            parameters.add(query.zcType);
        }
        if (!StringHelper.isEmpty(query.employNum))
        {
            sql.append(" AND T6110.F14 LIKE ? ");
            parameters.add(sqlConnectionProvider.allMatch(query.employNum));
        }
        T6110_F07 userState = query.status;
        if (userState != null)
        {
            sql.append(" AND T6110.F07 = ?");
            parameters.add(userState.name());
        }
        sql.append(" ORDER BY F08 DESC,T6110.F09 DESC ) T ");
        String dshFlg = query.dshFlg;
        if (!StringHelper.isEmpty(dshFlg))
        {
            sql.append(" WHERE  T.F08 = ? ");
            parameters.add(dshFlg);
        }
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Grxx>()
            {
                
                @Override
                public Grxx[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<Grxx> list = null;
                    while (resultSet.next())
                    {
                        Grxx record = new Grxx();
                        record.id = resultSet.getInt(1);
                        record.name = resultSet.getString(2);
                        record.xqlx = T6141_F03.parse(resultSet.getString(3));
                        record.sfzh = resultSet.getString(4);
                        record.userName = resultSet.getString(5);
                        record.phone = resultSet.getString(6);
                        record.email = resultSet.getString(7);
                        record.kfjl = getKfjl(resultSet.getInt(1));
                        record.dshFlg = resultSet.getString(8);
                        record.startTime = resultSet.getTimestamp(9);
                        record.F08 = T6110_F08.parse(resultSet.getString(10));
                        record.employNum = resultSet.getString(11);
                        record.status = T6110_F07.parse(resultSet.getString(12));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new Grxx[list.size()]));
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    // 根据用户id得到客户经理
    private String getKfjl(int userID)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T7110.F02 AS F01 FROM S71.T7110 INNER JOIN S71.T7167 ON T7110.F01 = T7167.F02 WHERE T7167.F03 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userID);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getString(1);
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public void update(T6110 user)
        throws Throwable
    {
        if (user == null)
        {
            throw new ParameterException("参数值不能为空！");
        }
        try (Connection connection = getConnection())
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F01 FROM S61.T6110 WHERE F04 = ? AND F01<>?"))
                {
                    pstmt.setString(1, user.F04);
                    pstmt.setInt(2, user.F01);
                    try (ResultSet rs = pstmt.executeQuery())
                    {
                        if (rs.next())
                        {
                            throw new ParameterException("手机号码已经存在！");// 此处的字符串被updateGr.jsp用来进行异常判断,如果要修改，请同步修改.
                        }
                    }
                }
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F01 FROM S61.T6110 WHERE F05 = ? AND F01<>?"))
                {
                    pstmt.setString(1, user.F05);
                    pstmt.setInt(2, user.F01);
                    try (ResultSet rs = pstmt.executeQuery())
                    {
                        if (rs.next())
                        {
                            throw new ParameterException("邮箱已经存在！");// 此处的字符串被updateGr.jsp用来进行异常判断,如果要修改，请同步修改.
                        }
                    }
                }
                try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01 FROM S61.T6110 WHERE F01 = ? "))
                {
                    pstmt.setInt(1, user.F01);
                    try (ResultSet rs = pstmt.executeQuery())
                    {
                        if (!rs.next())
                        {
                            throw new ParameterException("用户账号不存在.");
                        }
                    }
                }
                
                if (user.F14 != null && !StringHelper.isEmpty(user.F14))
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("SELECT u.F01 FROM S61.T6110 u LEFT JOIN S61.T6111 t ON u.F01=t.F01 WHERE u.F01 = ? AND u.F08 LIKE ? AND t.F03 IS NOT NULL"))
                    {
                        pstmt.setInt(1, user.F01);
                        pstmt.setString(2, sqlConnectionProvider.allMatch("ZC"));
                        try (ResultSet rs = pstmt.executeQuery())
                        {
                            if (rs.next())
                            {
                                throw new ParameterException("该用户已经被邀请，无法选择业务员.");
                            }
                        }
                    }
                }
                execute(connection,
                    "UPDATE S61.T6110 SET F04 = ?,F05 = ?,F14 = ? WHERE F01 = ?",
                    user.F04,
                    user.F05,
                    user.F14,
                    user.F01);
                execute(connection,
                    "UPDATE S61.T6118 SET F03 = ?, F04 = ?, F06 = ?, F07 = ? WHERE F01 = ?",
                    T6118_F03.TG.name(),
                    T6118_F04.TG.name(),
                    user.F04,
                    user.F05,
                    user.F01);
                writeLog(connection, "操作日志", "修改个人账户");
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
    public T6141 getPersonal(int userId)
        throws Throwable
    {
        T6141 record = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S61.T6141 WHERE T6141.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6141();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = T6141_F03.parse(resultSet.getString(3));
                        record.F04 = T6141_F04.parse(resultSet.getString(4));
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getString(6);
                        record.F07 = resultSet.getString(7);
                    }
                }
            }
        }
        return record;
    }
    
    @Override
    public T6142 getXl(int userId)
        throws Throwable
    {
        T6142 record = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06 FROM S61.T6142 WHERE T6142.F02 = ? ORDER BY T6142.F04 DESC LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6142();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getInt(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getString(6);
                    }
                }
            }
        }
        return record;
    }
    
    @Override
    public T6143 getWork(int userId)
        throws Throwable
    {
        T6143 record = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S61.T6143 WHERE T6143.F02 = ? AND T6143.F03 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
                pstmt.setString(2, T6143_F03.ZZ.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6143();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = T6143_F03.parse(resultSet.getString(3));
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = resultSet.getString(6);
                        record.F07 = resultSet.getInt(7);
                        record.F08 = resultSet.getString(8);
                        record.F09 = resultSet.getString(9);
                        record.F10 = resultSet.getString(10);
                        record.F11 = resultSet.getString(11);
                    }
                }
            }
        }
        return record;
    }
    
    @Override
    public void updatePersonalBase(T6141 t6141)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO S61.T6141 (F01, F02, F06) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE F02 = VALUES(F02), F06 = VALUES(F06)"))
            {
                pstmt.setInt(1, t6141.F01);
                pstmt.setString(2, t6141.F02);
                pstmt.setString(3, t6141.F06);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public void updatePersonalXl(T6142 t6142)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                if (t6142.F02 > 0)
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S61.T6142 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ? WHERE F01 = ?"))
                    {
                        pstmt.setInt(1, t6142.F02);
                        pstmt.setString(2, t6142.F03);
                        pstmt.setInt(3, t6142.F04);
                        pstmt.setString(4, t6142.F05);
                        pstmt.setString(5, t6142.F06);
                        pstmt.setInt(6, t6142.F01);
                        pstmt.execute();
                    }
                }
                else
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("INSERT INTO S61.T6142 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?"))
                    {
                        pstmt.setInt(1, t6142.F02);
                        pstmt.setString(2, t6142.F03);
                        pstmt.setInt(3, t6142.F04);
                        pstmt.setString(4, t6142.F05);
                        pstmt.setString(5, t6142.F06);
                        pstmt.execute();
                    }
                }
            }
            catch (Exception e)
            {
                throw e;
            }
        }
    }
    
    @Override
    public void updatePersonalWork(T6143 entity)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                if (entity.F02 > 0)
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S61.T6143 SET F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ? WHERE F02 = ?"))
                    {
                        pstmt.setString(1, entity.F03.name());
                        pstmt.setString(2, entity.F04);
                        pstmt.setString(3, entity.F05);
                        pstmt.setString(4, entity.F06);
                        pstmt.setInt(5, entity.F07);
                        pstmt.setString(6, entity.F08);
                        pstmt.setString(7, entity.F09);
                        pstmt.setString(8, entity.F10);
                        pstmt.setString(9, entity.F11);
                        pstmt.setInt(10, entity.F02);
                        pstmt.execute();
                    }
                }
                else
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("INSERT INTO S61.T6143 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ?"))
                    {
                        pstmt.setInt(1, entity.F02);
                        pstmt.setString(2, entity.F03.name());
                        pstmt.setString(3, entity.F04);
                        pstmt.setString(4, entity.F05);
                        pstmt.setString(5, entity.F06);
                        pstmt.setInt(6, entity.F07);
                        pstmt.setString(7, entity.F08);
                        pstmt.setString(8, entity.F09);
                        pstmt.setString(9, entity.F10);
                        pstmt.setString(10, entity.F11);
                        pstmt.execute();
                    }
                }
            }
            catch (Exception e)
            {
                throw e;
            }
        }
    }
    
    @Override
    public BasicInfo findBasicInfo(int userId)
        throws Throwable
    {
        BasicInfo basicInfo = null;
        
        if (userId <= 0)
        {
            throw new ParameterException("参数值不能为空！");
        }
        
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps = conn.prepareStatement("SELECT F02,F04,F05,F09,F14 FROM S61.T6110 WHERE F01=?"))
            {
                ps.setInt(1, userId);
                
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        basicInfo = new BasicInfo();
                        basicInfo.userId = userId;
                        basicInfo.userName = rs.getString(1);
                        basicInfo.msisdn = rs.getString(2);
                        basicInfo.mailbox = rs.getString(3);
                        basicInfo.registrationTime = rs.getTimestamp(4);
                        basicInfo.employNum = rs.getString(5);
                    }
                }
            }
            
            if (basicInfo != null)
            {
                try (PreparedStatement ps =
                    conn.prepareStatement("SELECT T6141.F02,T6141.F04,T6141.F07,T6141.F08 FROM S61.T6141 WHERE T6141.F01=?"))
                {
                    ps.setInt(1, userId);
                    
                    try (ResultSet rs = ps.executeQuery())
                    {
                        if (rs.next())
                        {
                            basicInfo.realName = rs.getString(1);
                            basicInfo.isSmrz = EnumParser.parse(T6141_F04.class, rs.getString(2));
                            basicInfo.identityCard = rs.getString(3);
                            if (!StringHelper.isEmpty(basicInfo.identityCard))
                            {
                                basicInfo.identityCard = StringHelper.decode(basicInfo.identityCard);
                                String temp = basicInfo.identityCard;
                                if (Integer.parseInt(temp.substring(temp.length() - 2, temp.length() - 1)) % 2 != 0)
                                {
                                    basicInfo.sex = Sex.M;
                                }
                                else
                                {
                                    basicInfo.sex = Sex.F;
                                }
                                basicInfo.birthDate = rs.getTimestamp(4);
                            }
                        }
                    }
                }
                
                try (PreparedStatement ps =
                    conn.prepareStatement("SELECT T6142.F03 FROM S61.T6142 WHERE T6142.F02=? ORDER BY T6142.F04 DESC LIMIT 1"))
                {
                    ps.setInt(1, userId);
                    
                    try (ResultSet rs = ps.executeQuery())
                    {
                        if (rs.next())
                        {
                            basicInfo.graduateSchool = rs.getString(1);
                        }
                    }
                }
                
                try (PreparedStatement ps =
                    conn.prepareStatement("SELECT T6143.F05,T6143.F10,T6143.F11 FROM S61.T6143 WHERE T6143.F02 = ? AND T6143.F03 = ? LIMIT 1"))
                {
                    ps.setInt(1, userId);
                    ps.setString(2, T6143_F03.ZZ.name());
                    
                    try (ResultSet rs = ps.executeQuery())
                    {
                        if (rs.next())
                        {
                            basicInfo.position = rs.getString(1);
                            basicInfo.companyBusiness = rs.getString(2);
                            basicInfo.companyScale = rs.getString(3);
                        }
                    }
                }
                
                try (PreparedStatement ps =
                    conn.prepareStatement("SELECT T6116.F02,T5124.F02 FROM S51.T5124,S61.T6116 WHERE T6116.F02 >= T5124.F03 AND T6116.F02 <= T5124.F04 AND T6116.F01=?"))
                {
                    ps.setInt(1, userId);
                    
                    try (ResultSet rs = ps.executeQuery())
                    {
                        if (rs.next())
                        {
                            basicInfo.xyfs = rs.getInt(1);
                            basicInfo.qualityRating = rs.getString(2);
                        }
                    }
                }
                
                // 评估等级,已评估总次数
                try (PreparedStatement ps =
                    conn.prepareStatement("SELECT F04, F05 FROM S61.T6147 WHERE T6147.F02 = ? ORDER BY F06 DESC LIMIT 1"))
                {
                    ps.setInt(1, userId);
                    
                    try (ResultSet rs = ps.executeQuery())
                    {
                        if (rs.next())
                        {
                            basicInfo.riskAssess = T6147_F04.parse(rs.getString(1));
                            basicInfo.assessedNum = rs.getInt(2);
                        }
                    }
                }
                
            }
            
            // 账户余额
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT SUM(T6101.F06) FROM S61.T6101 WHERE T6101.F02 = ? AND F03 IN (?,?,?)"))
            {
                ps.setInt(1, userId);
                ps.setString(2, T6101_F03.WLZH.name());
                ps.setString(3, T6101_F03.SDZH.name());
                ps.setString(4, T6101_F03.FXBZJZH.name());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        basicInfo.accountBalance = basicInfo.accountBalance.add(rs.getBigDecimal(1));
                    }
                }
            }
            
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT COUNT(T5123.F01) FROM S51.T5123 WHERE T5123.F03=? AND T5123.F04=? AND T5123.F06=?"))
            {
                ps.setString(1, T5123_F03.S.name());
                ps.setString(2, T5123_F04.QY.name());
                ps.setString(3, T5123_F06.GR.name());
                
                try (ResultSet rSet = ps.executeQuery())
                {
                    if (rSet.next())
                    {
                        basicInfo.needAttestation = rSet.getInt(1);
                    }
                }
            }
            
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT COUNT(T5123.F01) FROM S51.T5123 WHERE T5123.F03=? AND T5123.F04=? AND T5123.F06=?"))
            {
                ps.setString(1, T5123_F03.F.name());
                ps.setString(2, T5123_F04.QY.name());
                ps.setString(3, T5123_F06.GR.name());
                
                try (ResultSet rSet = ps.executeQuery())
                {
                    if (rSet.next())
                    {
                        basicInfo.notNeedAttestation = rSet.getInt(1);
                    }
                }
            }
            
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT COUNT(T6120.F01) FROM S61.T6120,S51.T5123 WHERE T6120.F02=T5123.F01 AND T6120.F01=? AND T5123.F03=? AND T5123.F04=? AND T6120.F05=? AND T5123.F06=?"))
            {
                ps.setInt(1, userId);
                ps.setString(2, T5123_F03.S.name());
                ps.setString(3, T5123_F04.QY.name());
                ps.setString(4, T6120_F05.TG.name());
                ps.setString(5, T5123_F06.GR.name());
                
                try (ResultSet rSet = ps.executeQuery())
                {
                    if (rSet.next())
                    {
                        basicInfo.byrztg = rSet.getInt(1);
                    }
                }
            }
            
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT COUNT(T6120.F01) FROM S61.T6120,S51.T5123 WHERE T6120.F02=T5123.F01 AND T6120.F01=? AND T5123.F03=? AND T5123.F04=? AND T6120.F05=? AND T5123.F06=?"))
            {
                ps.setInt(1, userId);
                ps.setString(2, T5123_F03.F.name());
                ps.setString(3, T5123_F04.QY.name());
                ps.setString(4, T6120_F05.TG.name());
                ps.setString(5, T5123_F06.GR.name());
                
                try (ResultSet rSet = ps.executeQuery())
                {
                    if (rSet.next())
                    {
                        basicInfo.kxrztg = rSet.getInt(1);
                    }
                }
            }
            
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT F04 as blacklistDesc FROM S71.T7151 WHERE T7151.F02=? ORDER BY F01 DESC"))
            {
                ps.setInt(1, userId);
                
                try (ResultSet rSet = ps.executeQuery())
                {
                    if (rSet.next())
                    {
                        basicInfo.blacklistDesc = rSet.getString(1);
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
            
            // 借款负债
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F03 = ? AND T6252.F09 = ?"))
            {
                ps.setInt(1, userId);
                ps.setString(2, T6252_F09.WH.name());
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        basicInfo.borrowingLiability = resultSet.getBigDecimal(1);
                    }
                }
            }
            
            // 散标投资账户资产
            BigDecimal sbzc = new BigDecimal(0);
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT IFNULL(SUM(F07),0) FROM S62.T6252 WHERE T6252.F04 = ? AND F09=?"))
            {
                ps.setInt(1, userId);
                ps.setString(2, T6252_F09.WH.name());
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        sbzc = resultSet.getBigDecimal(1);
                    }
                }
            }
            // 优选理财账户资产
            BigDecimal yxzc = new BigDecimal(0);
            try (PreparedStatement ps = conn.prepareStatement("SELECT F03 FROM S64.T6413 WHERE T6413.F01 = ? LIMIT 1"))
            {
                ps.setInt(1, userId);
                try (ResultSet resultSet = ps.executeQuery())
                {
                    if (resultSet.next())
                    {
                        yxzc = resultSet.getBigDecimal(1);
                    }
                }
            }
            if (basicInfo != null)
            {
                // 投资资产
                basicInfo.lczc = sbzc.add(yxzc);
                
                // 净资产
                basicInfo.netAssets =
                    basicInfo.lczc.subtract(basicInfo.borrowingLiability).add(basicInfo.accountBalance);
            }
        }
        return basicInfo;
    }
    
    @Override
    public Grxx getUser(int userId)
        throws Throwable
    {
        if (userId <= 0)
        {
            throw new ParameterException("参数值不能为空！");
        }
        
        Grxx user = new Grxx();
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT T6110.F01,T6110.F02,T6110.F04,T6110.F05,T6141.F02,T6141.F07,T6118.F08,T6110.F14,T6111.F03,t.F04,t.F05 FROM S61.T6110 INNER JOIN S61.T6141 ON T6110.F01=T6141.F01 LEFT JOIN S61.T6118 ON T6118.F01 = T6110.F01 LEFT JOIN S61.T6111 ON T6111.F01 = T6110.F01 LEFT JOIN (SELECT F02,F04,F05 FROM S61.T6147 WHERE F02 = ? ORDER BY F06 DESC LIMIT 1) t ON t.F02=T6110.F01 WHERE T6110.F01=? LIMIT 1"))
            {
                ps.setInt(1, userId);
                ps.setInt(2, userId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        user.id = rs.getInt(1);
                        user.userName = rs.getString(2);
                        user.phone = rs.getString(3);
                        user.email = rs.getString(4);
                        user.name = rs.getString(5);
                        user.sfzh = rs.getString(6);
                        user.jymm = rs.getString(7);
                        user.employNum = rs.getString(8);
                        user.code = rs.getString(9);
                        user.riskAssess = T6147_F04.parse(rs.getString(10));
                        user.assessedNum = rs.getInt(11);
                    }
                }
            }
        }
        return user;
    }
    
    @Override
    public Attestation[] needAttestation(int userId)
        throws Throwable
    {
        if (userId <= 0)
        {
            return null;
        }
        
        ArrayList<Attestation> list = null;
        
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT T6120.F07,T5123.F02,T6120.F04,T6120.F05,T6120.F06 FROM S61.T6120 LEFT JOIN S51.T5123 ON T6120.F02=T5123.F01 WHERE T6120.F01=? AND T5123.F03=? AND T5123.F04=? AND T5123.F06=? ORDER BY T6120.F06 DESC "))
            {
                ps.setInt(1, userId);
                ps.setString(2, T5123_F03.S.name());
                ps.setString(3, T5123_F04.QY.name());
                ps.setString(4, T5123_F06.GR.name());
                
                try (ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        Attestation attestation = new Attestation();
                        attestation.id = rs.getInt(1);
                        attestation.attestationName = rs.getString(2);
                        attestation.creditGrades = rs.getInt(3);
                        attestation.attestationState = EnumParser.parse(T6120_F05.class, rs.getString(4));
                        attestation.attestationTime = rs.getTimestamp(5);
                        
                        try (PreparedStatement ps2 =
                            conn.prepareStatement("SELECT T6122.F01 FROM S61.T6122 WHERE T6122.F01=?"))
                        {
                            ps2.setInt(1, attestation.id);
                            
                            String ids = "";
                            
                            try (ResultSet rs2 = ps2.executeQuery())
                            {
                                while (rs2.next())
                                {
                                    if (StringHelper.isEmpty(ids))
                                    {
                                        ids = rs2.getInt(1) + "";
                                    }
                                    else
                                    {
                                        ids = ids + "," + rs2.getInt(1);
                                    }
                                }
                            }
                            
                            attestation.attachments = ids.split(",");
                        }
                        
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(attestation);
                    }
                }
            }
        }
        return list == null ? null : list.toArray(new Attestation[list.size()]);
    }
    
    @Override
    public Attestation[] notNeedAttestation(int userId)
        throws Throwable
    {
        if (userId <= 0)
        {
            return null;
        }
        
        ArrayList<Attestation> list = null;
        
        try (Connection conn = getConnection())
        {
            try (PreparedStatement ps =
                conn.prepareStatement("SELECT T6120.F07,T5123.F02,T6120.F04,T6120.F05,T6120.F06 FROM S61.T6120 INNER JOIN S51.T5123 ON T6120.F02=T5123.F01 WHERE T6120.F01=? AND T5123.F03=? AND T5123.F04=? AND T5123.F06=? "))
            {
                ps.setInt(1, userId);
                ps.setString(2, T5123_F03.F.name());
                ps.setString(3, T5123_F04.QY.name());
                ps.setString(4, T5123_F06.GR.name());
                
                try (ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        Attestation attestation = new Attestation();
                        attestation.id = rs.getInt(1);
                        attestation.attestationName = rs.getString(2);
                        attestation.creditGrades = rs.getInt(3);
                        attestation.attestationState = EnumParser.parse(T6120_F05.class, rs.getString(4));
                        attestation.attestationTime = rs.getTimestamp(5);
                        
                        try (PreparedStatement ps2 =
                            conn.prepareStatement("SELECT T6122.F01 FROM S61.T6122 WHERE T6122.F01=?"))
                        {
                            ps2.setInt(1, attestation.id);
                            
                            String ids = "";
                            
                            try (ResultSet rs2 = ps2.executeQuery())
                            {
                                while (rs2.next())
                                {
                                    if (StringHelper.isEmpty(ids))
                                    {
                                        ids = rs2.getInt(1) + "";
                                    }
                                    else
                                    {
                                        ids = ids + "," + rs2.getInt(1);
                                    }
                                }
                            }
                            
                            attestation.attachments = ids.split(",");
                        }
                        
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(attestation);
                    }
                }
            }
        }
        return list == null ? null : list.toArray(new Attestation[list.size()]);
    }
    
    @Override
    public PagingResult<LoanRecord> findLoanRecord(LoanRecordQuery loanRecordQuery, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6230.F01,T6230.F03,T6230.F06,(T6230.F05 - T6230.F07) AS F04,T6230.F09,T6230.F24,T6230.F20,T6230.F02,T6231.F21, T6231.F22, T6230.F05,T6230.F25  "
                    + " FROM S62.T6230 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 WHERE T6230.F20 in (?,?,?,?,?,?,?) ");
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(T6230_F20.TBZ.name());
        parameters.add(T6230_F20.DFK.name());
        parameters.add(T6230_F20.HKZ.name());
        parameters.add(T6230_F20.YJQ.name());
        parameters.add(T6230_F20.YLB.name());
        parameters.add(T6230_F20.YDF.name());
        parameters.add(T6230_F20.YZR.name());
        
        if (loanRecordQuery.getUserId() > 0)
        {
            sql.append(" AND T6230.F02 = ?");
            parameters.add(loanRecordQuery.getUserId());
        }
        else
        {
            return null;
        }
        
        if (loanRecordQuery != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            
            String string = loanRecordQuery.getLoanRecordTitle();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6230.F03 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            string = loanRecordQuery.getLoanNum();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6230.F25 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            Timestamp timestamp = loanRecordQuery.getCreateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6230.F24) >= ?");
                parameters.add(timestamp);
            }
            timestamp = loanRecordQuery.getCreateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6230.F24) <= ?");
                parameters.add(timestamp);
            }
            T6230_F20 signState = loanRecordQuery.getState();
            if (signState != null)
            {
                sql.append(" AND T6230.F20 = ?");
                parameters.add(signState);
            }
        }
        sql.append(" ORDER BY T6230.F01 DESC");
        try (final Connection connection = getConnection())
        {
            PagingResult<LoanRecord> pagingResult = selectPaging(connection, new ArrayParser<LoanRecord>()
            {
                
                @Override
                public LoanRecord[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<LoanRecord> list = null;
                    while (resultSet.next())
                    {
                        LoanRecord loanRecord = new LoanRecord();
                        loanRecord.loanRecordId = resultSet.getInt(1);
                        loanRecord.loanRecordTitle = resultSet.getString(2);
                        loanRecord.yearRate = resultSet.getDouble(3);
                        loanRecord.amount = resultSet.getBigDecimal(4);
                        
                        loanRecord.loanRecordTime = resultSet.getTimestamp(6);
                        loanRecord.loanRecordState = EnumParser.parse(T6230_F20.class, resultSet.getString(7));
                        loanRecord.overdueCount = getOverdueCount(connection, resultSet.getInt(8));
                        loanRecord.seriousOverdue = getSeriousOverdue(connection, resultSet.getInt(8));
                        loanRecord.dayBorrowFlg = T6231_F21.parse(resultSet.getString(9));
                        if (loanRecord.dayBorrowFlg.equals(T6231_F21.F))
                        {
                            loanRecord.deadline = resultSet.getInt(5);
                        }
                        else
                        {
                            loanRecord.deadline = resultSet.getInt(10);
                        }
                        loanRecord.loanAmount = resultSet.getBigDecimal(11);
                        loanRecord.loanNum = resultSet.getString(12);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(loanRecord);
                    }
                    return list == null ? null : list.toArray(new LoanRecord[list.size()]);
                }
            }, paging, sql.toString(), parameters);
            return pagingResult;
        }
    }
    
    private int getOverdueCount(Connection connection, int userId)
        throws SQLException
    {
        int overdueCount = 0;
        try (PreparedStatement ps = connection.prepareStatement("SELECT F02 FROM S61.T6144 WHERE F01=?"))
        {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    overdueCount = rs.getInt(1);
                }
            }
        }
        return overdueCount;
    }
    
    private int getSeriousOverdue(Connection connection, int userId)
        throws SQLException
    {
        int seriousOverdue = 0;
        try (PreparedStatement ps = connection.prepareStatement("SELECT F03 FROM S61.T6144 WHERE F01=?"))
        {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    seriousOverdue = rs.getInt(1);
                }
            }
        }
        return seriousOverdue;
    }
    
    @Override
    public PagingResult<TenderRecord> findTenderRecord(TenderRecordQuery tenderRecordQuery, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6230.F25 AS F01, T6230.F03 AS F02, T6250.F04 AS F03, T6230.F06 AS F04, T6230.F09 AS F05, T6250.F06 AS F06,T6230.F20 AS F08,T6231.F21 AS F09, T6231.F22 AS F10 "
                    + " FROM S62.T6250 INNER JOIN S62.T6230 ON T6250.F02=T6230.F01 INNER JOIN S62.T6231 ON T6231.F01 = T6230.F01 WHERE 1=1");
        ArrayList<Object> parameters = new ArrayList<>();
        if (tenderRecordQuery.getUserId() > 0)
        {
            sql.append(" AND T6250.F03 = ?");
            parameters.add(tenderRecordQuery.getUserId());
        }
        else
        {
            return null;
        }
        if (tenderRecordQuery != null)
        {
            SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
            
            String string = tenderRecordQuery.getTenderRecordTitle();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6230.F03 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            string = tenderRecordQuery.getTenderRecordId();
            if (!StringHelper.isEmpty(string))
            {
                sql.append(" AND T6230.F25 LIKE ?");
                parameters.add(sqlConnectionProvider.allMatch(string));
            }
            Timestamp timestamp = tenderRecordQuery.getCreateTimeStart();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6250.F06) >= ?");
                parameters.add(timestamp);
            }
            timestamp = tenderRecordQuery.getCreateTimeEnd();
            if (timestamp != null)
            {
                sql.append(" AND DATE(T6250.F06) <= ?");
                parameters.add(timestamp);
            }
            T6230_F20 signState = tenderRecordQuery.getTenderRecordState();
            if (signState != null)
            {
                sql.append(" AND T6230.F20 = ?");
                parameters.add(signState);
            }
        }
        sql.append(" ORDER BY T6250.F06 DESC ");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<TenderRecord>()
            {
                
                @Override
                public TenderRecord[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<TenderRecord> list = null;
                    while (resultSet.next())
                    {
                        TenderRecord tenderRecord = new TenderRecord();
                        tenderRecord.tenderRecordId = resultSet.getString(1);
                        tenderRecord.tenderRecordTitle = resultSet.getString(2);
                        tenderRecord.tenderMoney = resultSet.getBigDecimal(3);
                        tenderRecord.yearRate = resultSet.getDouble(4);
                        tenderRecord.tenderTime = resultSet.getTimestamp(6);
                        tenderRecord.tenderRecordState = EnumParser.parse(T6230_F20.class, resultSet.getString(7));
                        tenderRecord.dayBorrowFlg = T6231_F21.parse(resultSet.getString(8));
                        if (tenderRecord.dayBorrowFlg.equals(T6231_F21.F))
                        {
                            tenderRecord.deadline = resultSet.getInt(5);
                        }
                        else
                        {
                            tenderRecord.deadline = resultSet.getInt(9);
                        }
                        // // 待收本息=(每份金额*期限)
                        // BigDecimal temp = resultSet.getBigDecimal(8);
                        //
                        // tenderRecord.takeCoupon = temp
                        // .multiply(new BigDecimal(
                        // tenderRecord.deadline));
                        
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(tenderRecord);
                    }
                    return list == null ? null : list.toArray(new TenderRecord[list.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public T6122 getAttachment(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            return null;
        }
        
        T6122 record = null;
        
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02, F03, F04, F05 FROM S61.T6122 WHERE T6122.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery())
                {
                    if (rs.next())
                    {
                        record = new T6122();
                        record.F02 = rs.getString(1);
                        record.F03 = rs.getInt(2);
                        record.F04 = rs.getTimestamp(3);
                        record.F05 = rs.getString(4);
                    }
                }
            }
            
        }
        return record;
    }
    
    @Override
    public T6122 get(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            throw new ParameterException("参数不能为空");
        }
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<T6122>()
            {
                
                @Override
                public T6122 parse(ResultSet resultSet)
                    throws SQLException
                {
                    T6122 t6122 = new T6122();
                    if (resultSet.next())
                    {
                        t6122.F01 = resultSet.getInt(1);
                        t6122.F02 = resultSet.getString(2);
                        t6122.F03 = resultSet.getInt(3);
                        t6122.F04 = resultSet.getTimestamp(4);
                        t6122.F05 = resultSet.getString(5);
                    }
                    return t6122;
                }
            }, "SELECT F01,F02,F03,F04,F05 FROM S61.T6122 WHERE F01=?", id);
        }
    }
    
    @Override
    public Rzxx[] getRzxx(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<Rzxx> list = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T5123.F02 AS F01, T5123.F03 AS F02, T5123.F05 AS F03, T6120.F03 AS F04, T6120.F05 AS F05, T6120.F06 AS F06, T6120.F07 AS F07,T6120.F04 AS F08,T6120.F02 AS F09 FROM S51.T5123 INNER JOIN S61.T6120 ON T5123.F01 = T6120.F02 WHERE T5123.F04 = ? AND T6120.F01 = ? ORDER BY FIELD(T6120.F05,'DSH','WYZ','BTG','TG'),T6120.F06 DESC"))
            {
                pstmt.setString(1, T5123_F04.QY.name());
                pstmt.setInt(2, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        Rzxx record = new Rzxx();
                        record.lxmc = resultSet.getString(1);
                        record.mustRz = T5123_F03.parse(resultSet.getString(2));
                        record.fs = resultSet.getInt(3);
                        record.rzcs = resultSet.getInt(4);
                        record.status = T6120_F05.parse(resultSet.getString(5));
                        record.time = resultSet.getTimestamp(6);
                        record.yxjlID = resultSet.getInt(7);
                        record.ds = resultSet.getInt(8);
                        record.rzID = resultSet.getInt(9);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                }
                return ((list == null || list.size() == 0) ? null : list.toArray(new Rzxx[list.size()]));
            }
        }
    }
    
    @Override
    public void rztg(int id, String desc)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                Timestamp timestamp = getCurrentTimestamp(connection);
                execute(connection,
                    "UPDATE S61.T6121 SET F04 = ?, F05 = ?, F06 = ?, F07 = ? WHERE F01 = ?",
                    desc,
                    T6121_F05.TG.name(),
                    serviceResource.getSession().getAccountId(),
                    timestamp,
                    id);
                execute(connection,
                    "UPDATE S61.T6120 SET F03 = F03+1, F04 = ?, F05 = ?, F06 =  ? WHERE F07 = ?",
                    getRzfc(id, connection),
                    T6120_F05.TG.name(),
                    timestamp,
                    id);
                writeLog(connection, "操作日志", "认证项审核通过");
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
    public void rzbtg(int id, String desc)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                Timestamp timestamp = getCurrentTimestamp(connection);
                execute(connection,
                    "UPDATE S61.T6121 SET F04 = ?, F05 = ?, F06 = ?, F07 = ? WHERE F01 = ?",
                    desc,
                    T6121_F05.BTG.name(),
                    serviceResource.getSession().getAccountId(),
                    timestamp,
                    id);
                execute(connection,
                    "UPDATE S61.T6120 SET F03 = F03+1, F05 = ?, F06 =  ? WHERE F07 = ?",
                    T6120_F05.BTG.name(),
                    timestamp,
                    id);
                writeLog(connection, "操作日志", "认证项审核不通过");
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    /**
     * 通过有效认证ID得到认证分数
     * 
     * @param id
     * @return
     * @throws SQLException
     */
    private int getRzfc(int id, Connection connection)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT T5123.F05 AS F01 FROM S51.T5123 INNER JOIN S61.T6120 ON T5123.F01 = T6120.F02 WHERE T6120.F07 = ? LIMIT 1"))
        {
            pstmt.setInt(1, id);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }
    
    @Override
    public T6121[] rzjl(int id)
        throws Throwable
    {
        ArrayList<T6121> list = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01,F04,F05,F06,F07 FROM S61.T6121  WHERE F03 = ? ORDER BY F07 DESC "))
            {
                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery())
                {
                    while (rs.next())
                    {
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        T6121 entity = new T6121();
                        entity.F01 = rs.getInt(1);
                        entity.F04 = rs.getString(2);
                        entity.F05 = T6121_F05.parse(rs.getString(3));
                        entity.F06 = rs.getInt(4);
                        entity.F07 = rs.getTimestamp(5);
                        list.add(entity);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T6121[list.size()]));
        }
        
    }
    
    @Override
    public T6121[] rzjl(int id, int yhId)
        throws Throwable
    {
        ArrayList<T6121> list = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6121.F01 ,T6121.F04,T6121.F05,T6121.F06,T6121.F07,T5123.F02 AS F08 FROM S61.T6121 LEFT JOIN S51.T5123  ON S51.T5123.F01 = S61.T6121.F03 WHERE T6121.F02 = ? ORDER BY F07 DESC,F08 ASC "))
            {
                // pstmt.setInt(1, id);
                pstmt.setInt(1, yhId);
                try (ResultSet rs = pstmt.executeQuery())
                {
                    while (rs.next())
                    {
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        T6121 entity = new T6121();
                        entity.F01 = rs.getInt(1);
                        entity.F04 = rs.getString(2);
                        entity.F05 = T6121_F05.parse(rs.getString(3));
                        entity.F06 = rs.getInt(4);
                        entity.F07 = rs.getTimestamp(5);
                        entity.F08 = rs.getString(6);
                        list.add(entity);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T6121[list.size()]));
        }
        
    }
    
    @Override
    public T7110[] getKfjl()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            ArrayList<T7110> list = null;
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT _1022.F01 AS F01 FROM S10._1020 INNER JOIN S10._1022 ON _1020.F01 = _1022.F02"))
            {
                try (ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        try (PreparedStatement pstmt =
                            connection.prepareStatement("SELECT F01, F02, F04, F06, F07 FROM S71.T7110 WHERE T7110.F05 = ? AND T7110.F01=?"))
                        {
                            pstmt.setString(1, T7110_F05.QY.name());
                            pstmt.setInt(2, rs.getInt(1));
                            try (ResultSet resultSet = pstmt.executeQuery())
                            {
                                while (resultSet.next())
                                {
                                    T7110 record = new T7110();
                                    record.F01 = resultSet.getInt(1);
                                    record.F02 = resultSet.getString(2);
                                    record.F04 = resultSet.getString(3);
                                    record.F06 = resultSet.getTimestamp(4);
                                    record.F07 = resultSet.getTimestamp(5);
                                    if (list == null)
                                    {
                                        list = new ArrayList<>();
                                    }
                                    list.add(record);
                                }
                            }
                        }
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T7110[list.size()]));
        }
    }
    
    @Override
    public T6110 getT6110(int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            T6110 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, userId);
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
                        record.F11 = resultSet.getShort(11);
                        record.F12 = T6110_F12.parse(resultSet.getString(12));
                        record.F13 = T6110_F13.parse(resultSet.getString(13));
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public void videoExamine(int accountId, int videoId, T6145_F07 result, String opinion)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S61.T6145 WHERE F01 = ? "))
                {
                    pstmt.setInt(1, videoId);
                    try (ResultSet rs = pstmt.executeQuery())
                    {
                        if (!rs.next())
                        {
                            throw new ParameterException("视频记录不存在.");
                        }
                    }
                }
                
                try (PreparedStatement pstmt =
                    connection.prepareStatement("UPDATE S61.T6145 SET F07 = ?, F08 = ? WHERE F01 = ?"))
                {
                    pstmt.setString(1, result.name());
                    pstmt.setString(2, opinion);
                    pstmt.setInt(3, videoId);
                    pstmt.execute();
                }
                
                if (T6145_F07.SHTG == result)
                {
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S61.T6118 SET F09 = ? WHERE F01 = ?"))
                    {
                        pstmt.setString(1, T6118_F09.TG.name());
                        pstmt.setInt(2, accountId);
                        pstmt.execute();
                    }
                }
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
    public T6145 getVideo(int accountId)
        throws Throwable
    {
        T6145 record = null;
        if (accountId <= 0)
        {
            return record;
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S61.T6145 WHERE F07 = 'DSH' AND F02 = ? ORDER BY F01 LIMIT 1"))
            {
                pstmt.setInt(1, accountId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6145();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getInt(5);
                        record.F06 = resultSet.getTimestamp(6);
                        record.F07 = T6145_F07.parse(resultSet.getString(7));
                        record.F08 = resultSet.getString(8);
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public T6145 getSingleVideo(int videoId)
        throws Throwable
    {
        T6145 record = null;
        if (videoId <= 0)
        {
            return record;
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S61.T6145 WHERE F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, videoId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6145();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getInt(5);
                        record.F06 = resultSet.getTimestamp(6);
                        record.F07 = T6145_F07.parse(resultSet.getString(7));
                        record.F08 = resultSet.getString(8);
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public T6145[] getHistoryVideos(int accountId)
        throws Throwable
    {
        ArrayList<T6145> records = null;
        if (accountId <= 0)
        {
            return null;
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S61.T6145 WHERE F02 = ? ORDER BY F01 DESC"))
            {
                pstmt.setInt(1, accountId);
                T6145 record;
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        record = new T6145();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getInt(5);
                        record.F06 = resultSet.getTimestamp(6);
                        record.F07 = T6145_F07.parse(resultSet.getString(7));
                        record.F08 = resultSet.getString(8);
                        if (records == null)
                        {
                            records = new ArrayList<>();
                        }
                        records.add(record);
                    }
                }
            }
            return (records == null ? null : records.toArray(new T6145[records.size()]));
        }
    }
    
    @Override
    public T5127_F03 getJkdj(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02 FROM S61.T6115 WHERE T6115.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return getDj(T5127_F02.JK, resultSet.getBigDecimal(1));
                        
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public T5127_F03 getTzdj(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03 FROM S61.T6115 WHERE T6115.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return getDj(T5127_F02.TZ, resultSet.getBigDecimal(1));
                    }
                }
            }
        }
        return null;
    }
    
    // 根据金额,类型得到等级
    private T5127_F03 getDj(T5127_F02 type, BigDecimal money)
        throws SQLException
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03 FROM S51.T5127 WHERE T5127.F02 = ? AND T5127.F04 > ? AND T5127.F05 <= ? AND T5127.F06 = ? LIMIT 1"))
            {
                pstmt.setString(1, type.name());
                pstmt.setBigDecimal(2, money);
                pstmt.setBigDecimal(3, money);
                pstmt.setString(4, T5127_F06.QY.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return T5127_F03.parse(resultSet.getString(1));
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public BigDecimal getJkje(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02 FROM S61.T6115 WHERE T6115.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                        
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public BigDecimal getTzje(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03 FROM S61.T6115 WHERE T6115.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, id);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public PagingResult<Rzxx> getRzxx(int id, Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT T5123.F02 AS F01, T5123.F03 AS F02, T5123.F05 AS F03, T6120.F03 AS F04, T6120.F05 AS F05, T6120.F06 AS F06, T6120.F07 AS F07,T6120.F04 AS F08,T6120.F02 AS F09 FROM S51.T5123 INNER JOIN S61.T6120 ON T5123.F01 = T6120.F02 WHERE T5123.F04 = ? AND T6120.F01 = ? ORDER BY F06 DESC");
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(T5123_F04.QY.name());
        parameters.add(id);
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Rzxx>()
            {
                
                @Override
                public Rzxx[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<Rzxx> list = null;
                    while (resultSet.next())
                    {
                        Rzxx record = new Rzxx();
                        record.lxmc = resultSet.getString(1);
                        record.mustRz = T5123_F03.parse(resultSet.getString(2));
                        record.fs = resultSet.getInt(3);
                        record.rzcs = resultSet.getInt(4);
                        record.status = T6120_F05.parse(resultSet.getString(5));
                        record.time = resultSet.getTimestamp(6);
                        record.yxjlID = resultSet.getInt(7);
                        record.ds = resultSet.getInt(8);
                        record.rzID = resultSet.getInt(9);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new Rzxx[list.size()]));
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public void export(Grxx[] grxxs, OutputStream outputStream, String charset)
        throws Throwable
    {
        if (outputStream == null)
        {
            return;
        }
        if (grxxs == null)
        {
            return;
        }
        if (StringHelper.isEmpty(charset))
        {
            charset = "GBK";
        }
        
        final ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        boolean is_business = Boolean.parseBoolean(configureProvider.getProperty(BusinessVariavle.IS_BUSINESS));
        
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, charset)))
        {
            CVSWriter writer = new CVSWriter(out);
            writer.write("序号");
            writer.write("用户名");
            writer.write("姓名");
            writer.write("手机号码");
            writer.write("身份证");
            writer.write("邮箱 ");
            writer.write("状态 ");
            if (is_business)
            {
                writer.write("业务员工号");
            }
            writer.write("注册来源");
            writer.write("注册时间");
            writer.newLine();
            int index = 1;
            for (Grxx grxx : grxxs)
            {
                writer.write(index++);
                writer.write(grxx.userName);
                writer.write(grxx.name);
                writer.write(StringHelper.isEmpty(grxx.phone) ? "" : grxx.phone + "\t");
                writer.write(StringHelper.isEmpty(grxx.sfzh) ? "" : StringHelper.decode(grxx.sfzh) + "\t");
                writer.write(grxx.email);
                writer.write(grxx.status.getChineseName());
                if (is_business)
                {
                    writer.write(grxx.employNum);
                }
                writer.write(grxx.F08.getChineseName());
                writer.write(DateTimeParser.format(grxx.startTime) + "\t");
                writer.newLine();
            }
        }
    }
    
    @Override
    public PagingResult<T6142> seachXlxx(Paging paging, int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T6142>()
            {
                
                @Override
                public T6142[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<T6142> list = null;
                    while (resultSet.next())
                    {
                        T6142 record = new T6142();
                        record.F01 = resultSet.getInt(1);
                        record.F03 = resultSet.getString(2);
                        record.F04 = resultSet.getInt(3);
                        record.F05 = resultSet.getString(4);
                        record.F06 = resultSet.getString(5);
                        record.F07 = resultSet.getInt(6);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new T6142[list.size()]));
                }
            }, paging, "SELECT F01, F03, F04, F05, F06, F07 FROM S61.T6142 WHERE T6142.F02 = ?", userId);
        }
    }
    
    @Override
    public PagingResult<T6143EX> seachGzxx(Paging paging, int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T6143EX>()
            {
                
                @Override
                public T6143EX[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<T6143EX> list = null;
                    while (resultSet.next())
                    {
                        T6143EX record = new T6143EX();
                        record.F01 = resultSet.getInt(1);
                        record.F03 = T6143_F03.parse(resultSet.getString(2));
                        record.F04 = resultSet.getString(3);
                        record.F05 = resultSet.getString(4);
                        record.F06 = resultSet.getString(5);
                        record.F07 = resultSet.getInt(6);
                        record.F08 = resultSet.getString(7);
                        record.F09 = resultSet.getString(8);
                        record.F10 = resultSet.getString(9);
                        record.F11 = resultSet.getString(10);
                        record.addressStr =
                            resultSet.getString(11) + "," + resultSet.getString(12) + "," + resultSet.getString(13);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new T6143EX[list.size()]));
                }
            },
                paging,
                "SELECT T6143.F01, T6143.F03, T6143.F04, T6143.F05, T6143.F06, T6143.F07, T6143.F08, T6143.F09, T6143.F10, T6143.F11,T5019.F06,T5019.F07,T5019.F08 FROM S61.T6143 INNER JOIN S50.T5019 ON T6143.F07=T5019.F01 WHERE T6143.F02 = ?",
                userId);
        }
    }
    
    @Override
    public PagingResult<T6112> seachFcxx(Paging paging, int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return selectPaging(connection,
                new ArrayParser<T6112>()
                {
                    
                    @Override
                    public T6112[] parse(ResultSet resultSet)
                        throws SQLException
                    {
                        ArrayList<T6112> list = null;
                        while (resultSet.next())
                        {
                            T6112 record = new T6112();
                            record.F01 = resultSet.getInt(1);
                            record.F03 = resultSet.getString(2);
                            record.F04 = resultSet.getFloat(3);
                            record.F05 = resultSet.getInt(4);
                            record.F06 = resultSet.getBigDecimal(5);
                            record.F07 = resultSet.getBigDecimal(6);
                            record.F08 = resultSet.getInt(7);
                            record.F09 = resultSet.getString(8);
                            record.F10 = resultSet.getString(9);
                            record.F11 = resultSet.getBigDecimal(10);
                            if (list == null)
                            {
                                list = new ArrayList<>();
                            }
                            list.add(record);
                        }
                        return ((list == null || list.size() == 0) ? null : list.toArray(new T6112[list.size()]));
                    }
                },
                paging,
                "SELECT F01, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S61.T6112 WHERE T6112.F02 = ?",
                userId);
        }
    }
    
    @Override
    public PagingResult<T6113> seachCcxx(Paging paging, int userId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T6113>()
            {
                
                @Override
                public T6113[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<T6113> list = null;
                    while (resultSet.next())
                    {
                        T6113 record = new T6113();
                        record.F01 = resultSet.getInt(1);
                        record.F03 = resultSet.getString(2);
                        record.F04 = resultSet.getString(3);
                        record.F05 = resultSet.getInt(4);
                        record.F06 = resultSet.getBigDecimal(5);
                        record.F07 = resultSet.getBigDecimal(6);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new T6113[list.size()]));
                }
            }, paging, "SELECT F01, F03, F04, F05, F06, F07 FROM S61.T6113 WHERE T6113.F02 = ?", userId);
        }
    }
    
    @Override
    public String getRegion(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement("SELECT F06,F07,F08 FROM S50.T5019 WHERE F01=?"))
            {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        sb.append(rs.getString(1));
                        sb.append(",");
                        sb.append(rs.getString(2));
                        sb.append(",");
                        sb.append(rs.getString(3));
                    }
                }
            }
        }
        return sb.toString();
    }
    
    @Override
    public PagingResult<Dbxxmx> seachDbjl(DbxxmxQuery dbxxmxQuery, Paging paging)
        throws Throwable
    {
        
        ArrayList<Object> parameters = new ArrayList<Object>();
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6230.F25 F01,T6230.F03 F02,T6230.F05 F03,T6230.F06 F04,T6230.F09 F05,T6231.F21 F06,T6231.F22 F07,T6230.F24 F08,T6230.F20 F09");
        sql.append(" FROM S62.T6230 INNER JOIN S62.T6236 ON T6230.F01 = T6236.F02 INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01 WHERE T6236.F03 = ? ");
        parameters.add(dbxxmxQuery.getUserId());
        SQLConnectionProvider connectionProvider = getSQLConnectionProvider();
        String param = dbxxmxQuery.getJkbh();
        if (!StringHelper.isEmpty(param))
        {
            sql.append(" AND T6230.F25 LIKE ?");
            parameters.add(connectionProvider.allMatch(param));
        }
        param = dbxxmxQuery.getJkbt();
        if (!StringHelper.isEmpty(param))
        {
            sql.append(" AND T6230.F03 LIKE ?");
            parameters.add(connectionProvider.allMatch(param));
        }
        param = dbxxmxQuery.getCreateTimeStart();
        if (!StringHelper.isEmpty(param))
        {
            sql.append(" AND T6230.F24 >= ?");
            parameters.add(param + " 00:00:00");
        }
        param = dbxxmxQuery.getCreateTimeEnd();
        if (!StringHelper.isEmpty(param))
        {
            sql.append(" AND T6230.F24 <= ?");
            parameters.add(param + " 23:59:59");
        }
        param = dbxxmxQuery.getZt();
        if (!StringHelper.isEmpty(param))
        {
            sql.append(" AND T6230.F20 = ?");
            parameters.add(param);
        }
        sql.append(" ORDER BY T6230.F22 DESC");
        try (Connection connection = getConnection())
        {
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
                        record.jkbh = resultSet.getString(1);
                        record.jkbt = resultSet.getString(2);
                        record.jkje = resultSet.getBigDecimal(3);
                        record.nhl = resultSet.getBigDecimal(4);
                        // 2.投资期限
                        String dayOrMonth = resultSet.getInt(5) + "个月";
                        if ("S".equals(resultSet.getString(6)))
                        {
                            dayOrMonth = resultSet.getInt(7) + "天";
                        }
                        record.qx = dayOrMonth;
                        record.jksj = resultSet.getTimestamp(8);
                        record.zt = T6230_F20.parse(resultSet.getString(9)).getChineseName();
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new Dbxxmx[list.size()]));
                }
            }, paging, sql.toString(), parameters);
        }
    }
    
    @Override
    public PagingResult<Dfxxmx> seachDfjl(DfxxmxQuery dfxxmxQuery, Paging paging)
        throws Throwable
    {
        ArrayList<Object> parameters = new ArrayList<Object>();
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6230.F25 F01,T6110.F02 F02,T6230.F03 F03,T6253.F05 F04,T6253.F06 F05,T6253.F08 F06,T6230.F20 F07");
        sql.append(" FROM S62.T6253 INNER JOIN S62.T6230 ON T6253.F02 = T6230.F01 INNER JOIN S61.T6110 ON T6253.F04=T6110.F01 WHERE T6253.F03 = ? ");
        parameters.add(dfxxmxQuery.getUserId());
        SQLConnectionProvider connectionProvider = getSQLConnectionProvider();
        String param = dfxxmxQuery.getJkbh();
        if (!StringHelper.isEmpty(param))
        {
            sql.append(" AND T6230.F25 LIKE ?");
            parameters.add(connectionProvider.allMatch(param));
        }
        param = dfxxmxQuery.getJkbt();
        if (!StringHelper.isEmpty(param))
        {
            sql.append(" AND T6230.F03 LIKE ?");
            parameters.add(connectionProvider.allMatch(param));
        }
        param = dfxxmxQuery.getZt();
        if (!StringHelper.isEmpty(param))
        {
            sql.append(" AND T6230.F20 = ?");
            parameters.add(param);
        }
        sql.append(" ORDER BY T6253.F07 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Dfxxmx>()
            {
                
                @Override
                public Dfxxmx[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<Dfxxmx> list = null;
                    while (resultSet.next())
                    {
                        Dfxxmx record = new Dfxxmx();
                        record.jkbh = resultSet.getString(1);
                        record.yhm = resultSet.getString(2);
                        record.jkbt = resultSet.getString(3);
                        record.dfje = resultSet.getBigDecimal(4);
                        record.dffhje = resultSet.getBigDecimal(5);
                        record.yqqs = resultSet.getInt(6);
                        record.zt = T6230_F20.parse(resultSet.getString(7)).getChineseName();
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new Dfxxmx[list.size()]));
                }
            }, paging, sql.toString(), parameters);
        }
    }
}