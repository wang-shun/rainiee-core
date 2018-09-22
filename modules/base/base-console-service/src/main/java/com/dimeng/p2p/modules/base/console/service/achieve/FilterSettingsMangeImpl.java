package com.dimeng.p2p.modules.base.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.p2p.S62.entities.T6299;
import com.dimeng.p2p.S62.enums.T6299_F04;
import com.dimeng.p2p.modules.base.console.service.FilterSettingsManage;

public class FilterSettingsMangeImpl extends AbstractInformationService implements FilterSettingsManage
{

    public static class TermManageFactory implements ServiceFactory<FilterSettingsManage>
    {

		@Override
        public FilterSettingsManage newInstance(ServiceResource serviceResource)
        {
			return new FilterSettingsMangeImpl(serviceResource);
		}

	}

	public FilterSettingsMangeImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

    @Override
    public T6299 getFirst(T6299_F04 type)
        throws Throwable
    {
        if ("".equals(type) || type == null)
        {
            return null;
        }
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<T6299>()
            {
                @Override
                public T6299 parse(ResultSet rs) throws SQLException {
                    T6299 filterFirst = null;
                    if (rs.next()) {
                        filterFirst = new T6299();
                        filterFirst.F01 = rs.getInt(1);
                        filterFirst.F03 = rs.getInt(3);
                        filterFirst.F04 = T6299_F04.parse(rs.getString(4));
                        filterFirst.F05 = rs.getTimestamp(5);
                    }
                    return filterFirst;
                }
            }, "SELECT F01, F02, F03, F04, F05 FROM S62.T6299 WHERE F02 = 0 AND F04 = ?", type.name());
        }
    }

    @Override
    public T6299 getLast(T6299_F04 type)
        throws Throwable
    {
        if ("".equals(type) || type == null)
        {
            return null;
        }
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<T6299>()
            {
                @Override
                public T6299 parse(ResultSet rs)
                    throws SQLException
                {
                    T6299 filterLast = null;
                    if (rs.next())
                    {
                        filterLast = new T6299();
                        filterLast.F01 = rs.getInt(1);
                        filterLast.F02 = rs.getInt(2);
                        filterLast.F04 = T6299_F04.parse(rs.getString(4));
                        filterLast.F05 = rs.getTimestamp(5);
                    }
                    return filterLast;
                }
            }, "SELECT F01, F02, F03, F04, F05 FROM S62.T6299 WHERE F03 = 0 AND F04 = ?", type.name());
        }
    }

    @Override
    public T6299[] getAddFilter(T6299_F04 type)
        throws Throwable
    {
        if ("".equals(type) || type == null)
        {
            return null;
        }
        try (Connection connection = getConnection())
        {
            String sql =
                "SELECT F01, F02, F03, F04, F05 FROM S62.T6299 WHERE F04 = ? AND F02 !=0 AND F03 !=0 ORDER BY T6299.F01 ASC";
            ArrayList<T6299> list = null;
            try (PreparedStatement pstmt = connection.prepareStatement(sql))
            {
                pstmt.setString(1, type.name());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        T6299 filter = new T6299();
                        filter.F01 = resultSet.getInt(1);
                        filter.F02 = resultSet.getInt(2);
                        filter.F03 = resultSet.getInt(3);
                        filter.F04 = T6299_F04.parse(resultSet.getString(4));
                        filter.F05 = resultSet.getTimestamp(5);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(filter);
                    }
                }
            }
            return ((list == null || list.size() == 0) ? null : list.toArray(new T6299[list.size()]));
        }
    }

    @Override
    public void updateFilterSettings(List<T6299> updateList)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try{
                serviceResource.openTransactions(connection);
                for (T6299 Filtert6299 : updateList)
                {
                    if (Filtert6299 == null)
                    {
                        continue;
                    }
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("UPDATE S62.T6299 SET T6299.F02 = ?, T6299.F03 = ?, T6299.F05 = CURRENT_TIMESTAMP() WHERE T6299.F01 = ?"))
                    {
                        pstmt.setInt(1, Filtert6299.F02);
                        pstmt.setInt(2, Filtert6299.F03);
                        pstmt.setInt(3, Filtert6299.F01);
                        pstmt.executeUpdate();
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
    public void addFilterSettings(List<T6299> addList)
        throws Throwable
    {
        if (addList == null)
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                for (T6299 Filtert6299 : addList)
                {
                    if (Filtert6299 == null)
                    {
                        continue;
                    }
                    try (PreparedStatement pstmt =
                        connection.prepareStatement("INSERT INTO S62.T6299 SET F02 = ?, F03 = ?, F04 = ?, F05 = CURRENT_TIMESTAMP()"))
                    {
                        pstmt.setInt(1, Filtert6299.F02);
                        pstmt.setInt(2, Filtert6299.F03);
                        pstmt.setString(3, Filtert6299.F04.name());
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
    public void delFilterSettings(int id)
        throws Throwable
    {
        if (id <= 0)
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM S62.T6299 WHERE F01 = ?"))
            {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }
        }
    }

}
