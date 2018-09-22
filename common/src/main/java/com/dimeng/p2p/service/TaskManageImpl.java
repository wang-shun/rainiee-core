package com.dimeng.p2p.service;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S66.entities.T6601;
import com.dimeng.p2p.S66.entities.T6602;
import com.dimeng.p2p.S66.entities.T6603;
import com.dimeng.p2p.S66.enums.T6601_F06;
import com.dimeng.util.StringHelper;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 任务信息service
 * @author heluzhu
 *
 */

public class TaskManageImpl extends AbstractP2PService implements TaskManage
{

    public TaskManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }

    @Override
    public int insertoInfo(T6601 t6601)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("INSERT INTO S66.T6601 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F09 = ?, F11 = ? ",
                    PreparedStatement.RETURN_GENERATED_KEYS))
            {
                ps.setString(1, t6601.F02);
                ps.setString(2, t6601.F03);
                ps.setString(3, t6601.F04);
                ps.setString(4, t6601.F05);
                ps.setString(5, T6601_F06.ENABLE.name());
                ps.setTimestamp(6, getCurrentTimestamp(connection));
                ps.setString(7, t6601.F11);
                ps.execute();
                int id = 0;
                try (ResultSet resultSet = ps.getGeneratedKeys())
                {
                    if (resultSet.next())
                    {
                        id = resultSet.getInt(1);
                    }
                }

                return id;
            }
        }

    }

    @Override
    public List<T6601> queryAllInfo(String name, Paging page)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S66.T6601 WHERE 1=1 ");
        List<Object> params = new ArrayList<Object>();
        if (!StringHelper.isEmpty(name))
        {
            sql.append(" AND F02 LIKE ? ");
            params.add(getSQLConnectionProvider().allMatch(name));
        }
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<List<T6601>>()
            {

                @Override
                public List<T6601> parse(ResultSet rs)
                    throws SQLException
                {
                    List<T6601> list = null;
                    while (rs.next())
                    {
                        T6601 t6601 = new T6601();
                        t6601.F01 = rs.getInt(1);
                        t6601.F02 = rs.getString(2);
                        t6601.F03 = rs.getString(3);
                        t6601.F04 = rs.getString(4);
                        t6601.F05 = rs.getString(5);
                        t6601.F06 = T6601_F06.parse(rs.getString(6));
                        t6601.F07 = rs.getTimestamp(7);
                        t6601.F08 = rs.getTimestamp(8);
                        t6601.F09 = rs.getTimestamp(9);
                        t6601.F10 = rs.getString(10);
                        if (list == null)
                        {
                            list = new ArrayList<T6601>();
                        }
                        list.add(t6601);
                    }
                    return list;
                }

            }, sql.toString(), params);
        }

    }

    @Override
    public T6601 queryById(int id)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S66.T6601 WHERE F01 = ? "))
            {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery())
                {
                    T6601 t6601 = null;
                    if (rs.next())
                    {
                        if (t6601 == null)
                        {
                            t6601 = new T6601();
                        }
                        t6601.F01 = rs.getInt(1);
                        t6601.F02 = rs.getString(2);
                        t6601.F03 = rs.getString(3);
                        t6601.F04 = rs.getString(4);
                        t6601.F05 = rs.getString(5);
                        t6601.F06 = T6601_F06.parse(rs.getString(6));
                        t6601.F07 = rs.getTimestamp(7);
                        t6601.F08 = rs.getTimestamp(8);
                        t6601.F09 = rs.getTimestamp(9);
                        t6601.F10 = rs.getString(10);
                        t6601.F11 = rs.getString(11);
                    }
                    return t6601;
                }
            }
        }
    }

    @Override
    public void updateInfo(T6601 t6601)
        throws Throwable
    {

        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                connection.prepareStatement("UPDATE S66.T6601 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F11 = ? WHERE F01 = ? "))
            {
                ps.setString(1, t6601.F02);
                ps.setString(2, t6601.F03);
                ps.setString(3, t6601.F04);
                ps.setString(4, t6601.F05);
                ps.setString(5, t6601.F06.name());
                ps.setTimestamp(6, t6601.F07);
                ps.setTimestamp(7, t6601.F08);
                ps.setString(8, t6601.F11);
                ps.setInt(9, t6601.F01);
                ps.execute();
            }
        }
    }

    /**
     * 修改任务执行的开始时间和结束时间
     *
     * @param t6601
     * @return
     */
    @Override
    public void updateExcuteTime(T6601 t6601) throws Throwable {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ps =
                         connection.prepareStatement("UPDATE S66.T6601 SET F07 = ?, F08 = ? WHERE F01 = ? "))
            {
                ps.setTimestamp(1, t6601.F07);
                ps.setTimestamp(2, t6601.F08);
                ps.setInt(3, t6601.F01);
                ps.execute();
            }
        }
    }

    @Override
    public void insertT6602(T6602 t6602)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            insert(connection,
                "INSERT INTO S66.T6602 SET F02 = ?, F03 = ? ,F04 = ? ",
                t6602.F02,
                t6602.F03,
                getCurrentTimestamp(connection));
        }
    }

    @Override
    public PagingResult<T6601> taskList(String name, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder("SELECT T6601.F01, T6601.F02, T6601.F03, T6601.F04, T6601.F05, T6601.F06, F07, F08, F09, F10, F11 FROM S66.T6601 WHERE 1=1 ");
        List<Object> params = new ArrayList<Object>();
        if (!StringHelper.isEmpty(name))
        {
            sql.append(" AND F02 LIKE ? ");
            params.add(getSQLConnectionProvider().allMatch(name));
        }
        sql.append(" ORDER BY F09 DESC ");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T6601>()
            {

                @Override
                public T6601[] parse(ResultSet rs)
                    throws SQLException
                {
                    List<T6601> list = null;
                    while (rs.next())
                    {
                        T6601 t6601 = new T6601();
                        t6601.F01 = rs.getInt(1);
                        t6601.F02 = rs.getString(2);
                        t6601.F03 = rs.getString(3);
                        t6601.F04 = rs.getString(4);
                        t6601.F05 = rs.getString(5);
                        t6601.F06 = T6601_F06.parse(rs.getString(6));
                        t6601.F07 = rs.getTimestamp(7);
                        t6601.F08 = rs.getTimestamp(8);
                        t6601.F09 = rs.getTimestamp(9);
                        t6601.F10 = rs.getString(10);
                        t6601.F11 = rs.getString(11);
                        if (list == null)
                        {
                            list = new ArrayList<T6601>();
                        }
                        list.add(t6601);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new T6601[list.size()]);
                }

            }, paging, sql.toString(), params);
        }
    }

    public void handExecuteJob(int id)
        throws Throwable
    {

        T6601 t6601 = queryById(id);
        if (t6601 == null)
        {
            throw new LogicalException("此任务不存在id:+" + id);
        }
        else
        {
            if (T6601_F06.ENABLE == t6601.F06)
            {
                initJob(t6601);
            }
            else
            {
                throw new LogicalException("此任务已被禁用，请先开启:" + t6601.F02);
            }
        }

    }

    private void initJob(T6601 t6601)
        throws Throwable
    {

        if (t6601 != null)
        {

            Class<?> clazz = Class.forName(t6601.F03);
            Method method = clazz.getDeclaredMethod(t6601.F04);
            Object obj = clazz.newInstance();

            if (obj == null)
            {
                throw new LogicalException("实体bean未初始化:" + clazz.getName());
            }
            t6601.F07 = new Timestamp(System.currentTimeMillis());
            method.invoke(obj);
            t6601.F08 = new Timestamp(System.currentTimeMillis());
            updateInfo(t6601);
        }

    }

}
