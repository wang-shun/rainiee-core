
package com.dimeng.p2p.modules.bid.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6217;
import com.dimeng.p2p.S62.enums.T6217_F09;
import com.dimeng.p2p.modules.bid.console.service.DxzBidManage;
import com.dimeng.p2p.modules.bid.console.service.entity.DxzInfo;
import com.dimeng.p2p.modules.bid.console.service.query.DxzQuery;
import com.dimeng.util.StringHelper;

/**
 * 
 * 定向组标的业务实现类
 * @author  zhongsai
 * @version  [V7.0, 2018年2月7日]
 */
public class DxzBidManageImpl extends AbstractBidService implements DxzBidManage
{

    public DxzBidManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }

    @Override
    public PagingResult<DxzInfo> search(DxzQuery dxzQuery, Paging paramPaging)
        throws Throwable
    {
        ArrayList<Object> parameters = null;
        StringBuilder sql =
            new StringBuilder(
                "SELECT A.F01, A.F02, A.F03, A.F04, A.F05, A.F06, A.F07, A.F08, A.F09, B.F02 AS founderName, C.F02 AS updateUserName FROM S62.T6217 AS A LEFT JOIN S71.T7110 B ON B.F01 = A.F04 LEFT JOIN S71.T7110 C ON C.F01 = A.F05 WHERE 1=1 ");
        if (dxzQuery != null)
        {
            parameters = new ArrayList<Object>();
            SQLConnectionProvider connectionProvider = getSQLConnectionProvider();
            if (!StringHelper.isEmpty(dxzQuery.getDxzId()))
            {
                sql.append(" AND A.F02 LIKE ?");
                parameters.add(connectionProvider.allMatch(dxzQuery.getDxzId()));
            }
            
            if (!StringHelper.isEmpty(dxzQuery.getDxzTitle()))
            {
                sql.append(" AND A.F03 LIKE ?");
                parameters.add(connectionProvider.allMatch(dxzQuery.getDxzTitle()));
            }
        }
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<DxzInfo>()
            {
                @Override
                public DxzInfo[] parse(ResultSet rs)
                    throws SQLException
                {
                    DxzInfo dxzInfo = null;
                    ArrayList<DxzInfo> list = null;
                    while (rs.next())
                    {
                        dxzInfo = new DxzInfo();
                        dxzInfo.F01 = rs.getInt(1);
                        dxzInfo.F02 = rs.getString(2);
                        dxzInfo.F03 = rs.getString(3);
                        dxzInfo.F04 = rs.getInt(4);
                        dxzInfo.F05 = rs.getInt(5);
                        dxzInfo.F06 = rs.getTimestamp(6);
                        dxzInfo.F07 = rs.getTimestamp(7);
                        dxzInfo.F08 = rs.getString(8);
                        dxzInfo.F09 = T6217_F09.parse(rs.getString(9));
                        dxzInfo.founderName = rs.getString(10);
                        dxzInfo.updateUserName = rs.getString(11);
                        try
                        {
                            dxzInfo.peopleCount = getPeopleCount(connection, dxzInfo.F01);
                        }
                        catch (Throwable e)
                        {
                            e.printStackTrace();
                        }
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(dxzInfo);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new DxzInfo[list.size()]);
                    
                }
            }, paramPaging, sql.toString(), parameters);
        }
    }
    
    private int getPeopleCount(Connection connection, int F01)
        throws Throwable
    {
        try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(1) FROM S62.T6218 WHERE F03 = ? "))
        {
            ps.setInt(1, F01);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    @Override
    public boolean isExist(String checkType, String dxzIdOrName, int F01)
        throws Throwable
    {
        StringBuilder querySQL = new StringBuilder("SELECT F01 FROM S62.T6217 WHERE 1 = 1 ");
        if (F01 > 0)
        {
            querySQL.append(" AND F01 <> ").append(F01);
        }
        if ("dxzId".equals(checkType))
        {
            querySQL.append(" AND F02 = ? ");
        }
        else
        {
            querySQL.append(" AND F03 = ? ");
        }
        querySQL.append("LIMIT 1 ");
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(querySQL.toString()))
            {
                ps.setString(1, dxzIdOrName);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void addT6217(T6217 t6217)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO S62.T6217 SET F02 = ?, F03 = ?, F04 = ?, F06 = ?, F08 = ?, F09 = ? "))
            {
                pstmt.setString(1, t6217.F02);
                pstmt.setString(2, t6217.F03);
                pstmt.setInt(3, serviceResource.getSession().getAccountId());
                pstmt.setTimestamp(4, getCurrentTimestamp(connection));
                pstmt.setString(5, t6217.F08);
                pstmt.setString(6, T6217_F09.QY.name());
                pstmt.execute();
            }
        }
    }

    @Override
    public void updateT6217(T6217 t6217)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            execute(connection,
                "UPDATE S62.T6217 SET F03 = ?, F05 = ?, F07 = ?, F08 = ?, F09 = ?  WHERE F01 = ? ",
                t6217.F03,
                serviceResource.getSession().getAccountId(),
                getCurrentTimestamp(connection),
                t6217.F08,
                t6217.F09.name(),
                t6217.F01);
        }
    }

    @Override
    public void updateT6217Status(int dxzId, String status)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            execute(connection, "UPDATE S62.T6217 SET F09 = ? WHERE F01 = ? ", status, dxzId);
        }
    }

    @Override
    public T6217 getT6217(int F01)
        throws Throwable
    {
        T6217 t6217 = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT A.F01, A.F02, A.F03, A.F04, A.F05, A.F06, A.F07, A.F08, A.F09 FROM S62.T6217 AS A WHERE A.F01 = ? LIMIT 1 "))
            {
                ps.setInt(1, F01);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        t6217 = new T6217();
                        t6217.F01 = rs.getInt(1);
                        t6217.F02 = rs.getString(2);
                        t6217.F03 = rs.getString(3);
                        t6217.F04 = rs.getInt(4);
                        t6217.F05 = rs.getInt(5);
                        t6217.F06 = rs.getTimestamp(6);
                        t6217.F07 = rs.getTimestamp(7);
                        t6217.F08 = rs.getString(8);
                        t6217.F09 = T6217_F09.parse(rs.getString(9));
                    }
                }
            }
        }
        return t6217;
    }

}
