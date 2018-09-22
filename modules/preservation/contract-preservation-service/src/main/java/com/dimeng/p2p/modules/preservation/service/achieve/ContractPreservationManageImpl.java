/*
 * 文 件 名:  ContractPreservationManageImpl.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2016年6月17日
 */
package com.dimeng.p2p.modules.preservation.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6250;
import com.dimeng.p2p.S62.entities.T6271;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F12;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F14;
import com.dimeng.p2p.S62.enums.T6230_F15;
import com.dimeng.p2p.S62.enums.T6230_F16;
import com.dimeng.p2p.S62.enums.T6230_F17;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6230_F27;
import com.dimeng.p2p.S62.enums.T6250_F07;
import com.dimeng.p2p.S62.enums.T6250_F08;
import com.dimeng.p2p.S62.enums.T6271_F07;
import com.dimeng.p2p.S62.enums.T6271_F08;
import com.dimeng.p2p.S62.enums.T6271_F10;
import com.dimeng.p2p.modules.preservation.service.AbstractPreservationService;
import com.dimeng.p2p.repeater.preservation.ContractPreservationManage;
import com.dimeng.p2p.repeater.preservation.entity.ContractEntity;
import com.dimeng.p2p.repeater.preservation.query.ContractQuery;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.SQLDateParser;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  xiaoqi
 * @version  [7.2.0, 2016年6月17日]
 */
public class ContractPreservationManageImpl extends AbstractPreservationService implements ContractPreservationManage
{
    
    /** 
     * <默认构造函数>
     */
    public ContractPreservationManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public int getAchieveVersion()
    {
        return 0;
    }
    
    @Override
    public PagingResult<ContractEntity> search(ContractQuery contractQuery, Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT T6110.F02 AS F01,T6110.F06 AS F02, T6141.F02 AS F03, T6161.F04 AS F04,");
        sql.append(" T6230.F25 AS F05, T6230.F03 AS F06, T6271.F04 AS F07,T6271.F05 AS F08, T6271.F06 AS F09, ");
        sql.append(" T6271.F07 AS F10,T6110.F01 AS F11,T6230.F01 AS F12,T6271.F01 AS F13,T6271.F08 AS F14 ");
        sql.append(" FROM S62.T6230 INNER JOIN S62.T6271 ON T6230.F01 = T6271.F03");
        sql.append(" INNER JOIN S61.T6110 ON T6110.F01 = T6271.F02");
        sql.append(" INNER JOIN S62.T6231 ON T6230.F01 = T6231.F01");
        sql.append(" LEFT JOIN S61.T6161 ON T6161.F01 = T6110.F01");
        sql.append(" LEFT JOIN S61.T6141 ON T6141.F01 = T6110.F01 WHERE 1 = 1");
        ArrayList<Object> parameters = new ArrayList<>();
        setParameters(sql, contractQuery, parameters);
        sql.append(" ORDER BY T6271.F14 DESC ");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<ContractEntity>()
            {
                @Override
                public ContractEntity[] parse(ResultSet rs)
                    throws SQLException
                {
                    List<ContractEntity> ceList = null;
                    while (rs.next())
                    {
                        ContractEntity ce = new ContractEntity();
                        ce.userName = rs.getString(1);
                        if (T6110_F06.ZRR.name().equals(rs.getString(2)))
                        {
                            ce.realName = rs.getString(3);
                        }
                        else
                        {
                            ce.realName = rs.getString(4);
                        }
                        ce.bidNum = rs.getString(5);
                        ce.loanTitle = rs.getString(6);
                        ce.F04 = rs.getString(7);
                        ce.F05 = rs.getString(8);
                        ce.F06 = rs.getTimestamp(9);
                        ce.F07 = T6271_F07.parse(rs.getString(10));
                        ce.F02 = rs.getInt(11);
                        ce.F03 = rs.getInt(12);
                        ce.F01 = rs.getInt(13);
                        ce.F08 = T6271_F08.parse(rs.getString(14));
                        if (ceList == null)
                        {
                            ceList = new ArrayList<ContractEntity>();
                        }
                        ceList.add(ce);
                    }
                    return ((ceList == null || ceList.size() == 0) ? null
                        : ceList.toArray(new ContractEntity[ceList.size()]));
                }
            },
                paging,
                sql.toString(),
                parameters);
        }
        
    }
    
    /**
     * <一句话功能简述> 参数封装公共方法
     * <功能详细描述>
     * @param sql
     * @param query
     * @param parameters
     * @throws Throwable
     */
    private void setParameters(StringBuilder sql, ContractQuery query, ArrayList<Object> parameters)
        throws Throwable
    {
        SQLConnectionProvider provide = getSQLConnectionProvider();
        if (query != null)
        {
            if (!StringHelper.isEmpty(query.userName))
            {
                sql.append(" AND T6110.F02 LIKE ? ");
                parameters.add(provide.allMatch(query.userName));
            }
            if (!StringHelper.isEmpty(query.realName))
            {
                sql.append(" AND (T6141.F02 LIKE ? OR T6161.F04 LIKE ? )");
                parameters.add(provide.allMatch(query.realName));
                parameters.add(provide.allMatch(query.realName));
            }
            if (!StringHelper.isEmpty(query.preservationId))
            {
                sql.append(" AND T6271.F05 LIKE ?");
                parameters.add(provide.allMatch(query.preservationId));
            }
            if (!StringHelper.isEmpty(query.startTime))
            {
                sql.append(" AND DATE(T6271.F06) >= ?");
                parameters.add(SQLDateParser.parse(query.startTime));
            }
            if (!StringHelper.isEmpty(query.endTime))
            {
                sql.append(" AND DATE(T6271.F06) <= ?");
                parameters.add(SQLDateParser.parse(query.endTime));
            }
            if (!StringHelper.isEmpty(query.bidId))
            {
                sql.append(" AND T6230.F25 LIKE ?");
                parameters.add(provide.allMatch(query.bidId));
            }
            if (!StringHelper.isEmpty(query.loanTitle))
            {
                sql.append(" AND T6230.F03 LIKE ?");
                parameters.add(provide.allMatch(query.loanTitle));
            }
            if (!StringHelper.isEmpty(query.contractNum))
            {
                sql.append(" AND T6271.F04 LIKE ?");
                parameters.add(provide.allMatch(query.contractNum));
            }
            if (query.preservationStatus != null)
            {
                sql.append(" AND T6271.F07 = ?");
                parameters.add(query.preservationStatus);
            }
            if (query.contractType != null)
            {
                sql.append(" AND T6271.F08 = ?");
                parameters.add(query.contractType);
            }
        }
    }
    
    @Override
    public void insertT6271(int bidId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                T6230 t6230 = selectT6230(connection, bidId);
                T6250[] t6250s = selectAllT6250(connection, bidId);
                StringBuilder sql = new StringBuilder();
                ArrayList<Object> parameters = new ArrayList<Object>();
                sql.append("INSERT INTO S62.T6271 (F02,F03,F07,F08,F10) VALUES (?,?,?,?,?)");
                parameters.add(t6230.F02);
                parameters.add(t6230.F01);
                parameters.add(T6271_F07.WBQ);
                parameters.add(T6271_F08.JKHT);
                parameters.add(T6271_F10.JKR);
                for (T6250 t6250 : t6250s)
                {
                    sql.append(",(?,?,?,?,?)");
                    parameters.add(t6250.F03);
                    parameters.add(t6250.F02);
                    parameters.add(T6271_F07.WBQ);
                    parameters.add(T6271_F08.JKHT);
                    parameters.add(T6271_F10.TZR);
                }
                insert(connection, sql.toString(), parameters);
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
     * <一句话功能简述> 根据标的id，查询标的详情
     * <功能详细描述>
     * @param connection
     * @param F01 标的ID，对应T6230.F01
     * @return
     * @throws SQLException
     */
    private T6230 selectT6230(Connection connection, int F01)
        throws SQLException
    {
        T6230 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25, F26, F27 FROM S62.T6230 WHERE T6230.F01 = ? "))
        {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6230();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getString(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getBigDecimal(6);
                    record.F07 = resultSet.getBigDecimal(7);
                    record.F08 = resultSet.getInt(8);
                    record.F09 = resultSet.getInt(9);
                    record.F10 = T6230_F10.parse(resultSet.getString(10));
                    record.F11 = T6230_F11.parse(resultSet.getString(11));
                    record.F12 = T6230_F12.parse(resultSet.getString(12));
                    record.F13 = T6230_F13.parse(resultSet.getString(13));
                    record.F14 = T6230_F14.parse(resultSet.getString(14));
                    record.F15 = T6230_F15.parse(resultSet.getString(15));
                    record.F16 = T6230_F16.parse(resultSet.getString(16));
                    record.F17 = T6230_F17.parse(resultSet.getString(17));
                    record.F18 = resultSet.getInt(18);
                    record.F19 = resultSet.getInt(19);
                    record.F20 = T6230_F20.parse(resultSet.getString(20));
                    record.F21 = resultSet.getString(21);
                    record.F22 = resultSet.getTimestamp(22);
                    record.F23 = resultSet.getInt(23);
                    record.F24 = resultSet.getTimestamp(24);
                    record.F25 = resultSet.getString(25);
                    record.F26 = resultSet.getBigDecimal(26);
                    record.F27 = T6230_F27.parse(resultSet.getString(27));
                }
            }
        }
        return record;
    }
    
    /**
     * <一句话功能简述> 根据标的ID查询该标的的所有投资人
     * <功能详细描述>
     * @param connection 
     * @param F02
     * @return
     * @throws SQLException
     */
    private T6250[] selectAllT6250(Connection connection, int F02)
        throws SQLException
    {
        List<T6250> list = new ArrayList<T6250>();
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F02,F03 FROM S62.T6250 WHERE F02 = ? AND F07 = ? AND F08 = ? GROUP BY F02,F03 "))
        {
            pstmt.setInt(1, F02);
            pstmt.setString(2, T6250_F07.F.name());
            pstmt.setString(3, T6250_F08.S.name());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                while (resultSet.next())
                {
                    T6250 t6250 = new T6250();
                    t6250.F02 = resultSet.getInt(1);
                    t6250.F03 = resultSet.getInt(2);
                    list.add(t6250);
                }
            }
        }
        return list.toArray(new T6250[list.size()]);
    }
    
    @Override
    public void updateT6271PdfPath(int id, String path)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            String sql = "UPDATE S62.T6271 SET F09 =? WHERE F01 =? ";
            try (PreparedStatement pstmt = connection.prepareStatement(sql))
            {
                pstmt.setString(1, path);
                pstmt.setInt(2, id);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public void updateT6271(T6271 t6271)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            String sql = "UPDATE S62.T6271 SET F05 =?, F06 =?, F07 =?, F09 = ? WHERE F01 =? ";
            try (PreparedStatement pstmt = connection.prepareStatement(sql))
            {
                pstmt.setString(1, t6271.F05);
                pstmt.setTimestamp(2, getCurrentTimestamp(connection));
                pstmt.setString(3, t6271.F07.name());
                pstmt.setString(4, t6271.F09);
                pstmt.setInt(5, t6271.F01);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public T6271 getT6271(int id)
        throws Throwable
    {
        T6271 t6271 = null;
        try (Connection connection = getConnection())
        {
            String sql =
                "SELECT F01,F02,F03,F04,F05,F06,F07,F08,F09,F10,F11,F12,F13 FROM S62.T6271 WHERE F01=? LIMIT 1";
            try (PreparedStatement pstmt = connection.prepareStatement(sql))
            {
                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery())
                {
                    if (rs.next())
                    {
                        t6271 = new T6271();
                        t6271.F01 = rs.getInt(1);
                        t6271.F02 = rs.getInt(2);
                        t6271.F03 = rs.getInt(3);
                        t6271.F04 = rs.getString(4);
                        t6271.F05 = rs.getString(5);
                        t6271.F06 = rs.getTimestamp(6);
                        t6271.F07 = T6271_F07.parse(rs.getString(7));
                        t6271.F08 = T6271_F08.parse(rs.getString(8));
                        t6271.F09 = rs.getString(9);
                        t6271.F10 = T6271_F10.parse(rs.getString(10));
                        t6271.F11 = rs.getInt(11);
                        t6271.F12 = rs.getInt(12);
                        t6271.F13 = rs.getInt(13);
                    }
                }
            }
        }
        return t6271;
    }
    
    @Override
    public T6271[] getAllUnPreservedRecord()
        throws Throwable
    {
        List<T6271> list = null;
        try (Connection connection = getConnection())
        {
            String sql = "SELECT F01,F02,F03,F04,F05,F06,F07,F08,F09,F10,F11,F12,F13 FROM S62.T6271 WHERE F07=? ";
            try (PreparedStatement pstmt = connection.prepareStatement(sql))
            {
                pstmt.setString(1, T6271_F07.WBQ.name());
                try (ResultSet rs = pstmt.executeQuery())
                {
                    while (rs.next())
                    {
                        T6271 t6271 = new T6271();
                        t6271.F01 = rs.getInt(1);
                        t6271.F02 = rs.getInt(2);
                        t6271.F03 = rs.getInt(3);
                        t6271.F04 = rs.getString(4);
                        t6271.F05 = rs.getString(5);
                        t6271.F06 = rs.getTimestamp(6);
                        t6271.F07 = T6271_F07.parse(rs.getString(7));
                        t6271.F08 = T6271_F08.parse(rs.getString(8));
                        t6271.F09 = rs.getString(9);
                        t6271.F10 = T6271_F10.parse(rs.getString(10));
                        t6271.F11 = rs.getInt(11);
                        t6271.F12 = rs.getInt(12);
                        t6271.F13 = rs.getInt(13);
                        if (list == null)
                        {
                            list = new ArrayList<T6271>();
                        }
                        list.add(t6271);
                    }
                }
            }
        }
        return (list == null || list.size() == 0) ? null : list.toArray(new T6271[list.size()]);
    }
    
    @Override
    public T6271[] getT6271s(int id)
        throws Throwable
    {
        List<T6271> list = new ArrayList<T6271>();
        try (Connection connection = getConnection())
        {
            String sql =
                "SELECT F01,F02,F03,F04,F05,F06,F07,F08,F09,F10,F11,F12,F13 FROM S62.T6271 WHERE F03=? AND F07=?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql))
            {
                pstmt.setInt(1, id);
                pstmt.setString(2, T6271_F07.WBQ.name());
                try (ResultSet rs = pstmt.executeQuery())
                {
                    while (rs.next())
                    {
                        T6271 t6271 = new T6271();
                        t6271.F01 = rs.getInt(1);
                        t6271.F02 = rs.getInt(2);
                        t6271.F03 = rs.getInt(3);
                        t6271.F04 = rs.getString(4);
                        t6271.F05 = rs.getString(5);
                        t6271.F06 = rs.getTimestamp(6);
                        t6271.F07 = T6271_F07.parse(rs.getString(7));
                        t6271.F08 = T6271_F08.parse(rs.getString(8));
                        t6271.F09 = rs.getString(9);
                        t6271.F10 = T6271_F10.parse(rs.getString(10));
                        t6271.F11 = rs.getInt(11);
                        t6271.F12 = rs.getInt(12);
                        t6271.F13 = rs.getInt(13);
                        list.add(t6271);
                    }
                }
            }
        }
        return list.toArray(new T6271[list.size()]);
    }
}
