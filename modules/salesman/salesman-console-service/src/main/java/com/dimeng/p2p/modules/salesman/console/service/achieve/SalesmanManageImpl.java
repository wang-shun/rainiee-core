package com.dimeng.p2p.modules.salesman.console.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S63.enums.T6331_F06;
import com.dimeng.p2p.modules.salesman.console.service.SalesmanManage;
import com.dimeng.p2p.modules.salesman.console.service.entity.Customers;
import com.dimeng.p2p.modules.salesman.console.service.entity.Salesman;
import com.dimeng.p2p.modules.salesman.console.service.query.CustomersQuery;
import com.dimeng.p2p.modules.salesman.console.service.query.SalesmanQuery;

public class SalesmanManageImpl extends AbstractSalesmanService implements SalesmanManage {

	public SalesmanManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}
	
	public static class SalesmanManageFactory implements
		ServiceFactory<SalesmanManage> {
	@Override
	public SalesmanManage newInstance(ServiceResource serviceResource) {
		return new SalesmanManageImpl(serviceResource);
	}

}

	@Override
	public PagingResult<Salesman> search(SalesmanQuery query, Paging paging)
        throws Throwable
    {
        StringBuffer sql = new StringBuffer("SELECT F01,F02,F03 FROM S63.T6330 WHERE 1=1");
        ArrayList<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            String ywyId = query.ywyId();
            String name = query.name();
            String tel = query.tel();
            if (ywyId != null && !ywyId.isEmpty())
            {
                sql.append(" AND F01 = ?");
                parameters.add(ywyId);
            }
            if (name != null && !name.isEmpty())
            {
                sql.append(" AND F02 = ?");
                parameters.add(name);
            }
            if (tel != null && !tel.isEmpty())
            {
                sql.append(" AND F03 = ?");
                parameters.add(tel);
            }
        }
        sql.append(" ORDER BY F04 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Salesman>()
            {
                @Override
                public Salesman[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<Salesman> list = null;
                    while (resultSet.next())
                    {
                        Salesman salesman = new Salesman();
                        salesman.ywyId = resultSet.getString(1);
                        salesman.name = resultSet.getString(2);
                        salesman.tel = resultSet.getString(3);
                        if (list == null)
                        {
                            list = new ArrayList<Salesman>();
                        }
                        list.add(salesman);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new Salesman[list.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }
	

	
	
	@Override
	public Salesman[] search(SalesmanQuery query)
        throws Throwable
    {
        StringBuffer sql = new StringBuffer("SELECT F01,F02,F03 FROM S63.T6330 WHERE 1=1");
        ArrayList<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            String ywyId = query.ywyId();
            String name = query.name();
            String tel = query.tel();
            if (ywyId != null && !ywyId.isEmpty())
            {
                sql.append(" AND F01 = ?");
                parameters.add(ywyId);
            }
            if (name != null && !name.isEmpty())
            {
                sql.append(" AND F02 = ?");
                parameters.add(name);
            }
            if (tel != null && !tel.isEmpty())
            {
                sql.append(" AND F03 = ?");
                parameters.add(tel);
            }
        }
        sql.append(" ORDER BY F04 DESC");
        try (Connection connection = getConnection())
        {
            return selectAll(connection, new ArrayParser<Salesman>()
            {
                @Override
                public Salesman[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<Salesman> list = null;
                    while (resultSet.next())
                    {
                        Salesman salesman = new Salesman();
                        salesman.ywyId = resultSet.getString(1);
                        salesman.name = resultSet.getString(2);
                        salesman.tel = resultSet.getString(3);
                        if (list == null)
                        {
                            list = new ArrayList<Salesman>();
                        }
                        list.add(salesman);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new Salesman[list.size()]);
                }
            }, sql.toString(), parameters);
        }
    }

	@Override
    public void add(SalesmanQuery query)
        throws Throwable
    {
        String sql = "INSERT INTO S63.T6330 SET F02 = ?,F03 = ?,F04 = ?";
        try (Connection connection = getConnection())
        {
            insert(connection, sql, query.name(), query.tel(), getCurrentTimestamp(connection));
        }
    }
	@Override
    public void update(SalesmanQuery query)
        throws Throwable
    {
        String sql = "UPDATE S63.T6330 SET F02 = ?,F03 = ? WHERE F01 = ?";
        try (Connection connection = getConnection())
        {
            execute(connection, sql, query.name(), query.tel(), query.ywyId());
        }
    }

	@Override
    public Salesman search(String ywyId)
        throws Throwable
    {
        String sql = "SELECT F01,F02,F03 FROM S63.T6330 WHERE F01 = ?";
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<Salesman>()
            {
                @Override
                public Salesman parse(ResultSet resultSet)
                    throws SQLException
                {
                    Salesman salesman = new Salesman();
                    if (resultSet.next())
                    {
                        salesman.ywyId = resultSet.getString(1);
                        salesman.name = resultSet.getString(2);
                        salesman.tel = resultSet.getString(3);
                    }
                    return salesman;
                }
            }, sql, ywyId);
        }
    }

	@Override
	public PagingResult<Customers> search(CustomersQuery query, Paging paging)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6110.F02 AS F01, T6331.F01 AS F02, T6331.F02 AS F03, T6331.F03 AS F04, T6331.F04 AS F05, T6331.F05 AS F06, T6331.F06 AS F07, T6331.F07 AS F08 FROM S61.T6110 INNER JOIN S63.T6331 ON T6110.F01 = T6331.F03 INNER JOIN S63.T6330 ON T6331.F02 = T6330.F01 WHERE 1 = 1");
        ArrayList<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            String ywyId = query.ywyId();
            String name = query.name();
            String tel = query.tel();
            if (ywyId != null && !ywyId.isEmpty())
            {
                sql.append(" AND T6330.F01 = ?");
                parameters.add(ywyId);
            }
            if (name != null && !name.isEmpty())
            {
                sql.append(" AND T6330.F02 = ?");
                parameters.add(name);
            }
            if (tel != null && !tel.isEmpty())
            {
                sql.append(" AND T6330.F03 = ?");
                parameters.add(tel);
            }
            if (query.startTime() != null)
            {
                sql.append(" AND T6331.F04 >= ?");
                parameters.add(query.startTime());
            }
            if (query.endTime() != null)
            {
                sql.append(" AND T6331.F04 <= ?");
                parameters.add(query.endTime());
            }
        }
        
        sql.append(" ORDER BY T6331.F04 DESC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Customers>()
            {
                @Override
                public Customers[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<Customers> list = null;
                    while (resultSet.next())
                    {
                        Customers record = new Customers();
                        record.loginName = resultSet.getString(1);
                        record.F01 = resultSet.getInt(2);
                        record.F02 = resultSet.getInt(3);
                        record.F03 = resultSet.getInt(4);
                        record.F04 = resultSet.getTimestamp(5);
                        record.F05 = resultSet.getBigDecimal(6);
                        record.F06 = T6331_F06.parse(resultSet.getString(7));
                        record.F07 = resultSet.getBigDecimal(8);
                        record.userName = selectString(connection, record.F03);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new Customers[list.size()]);
                }
            }, paging, sql.toString(), parameters);
        }
    }
	
	@Override
    public BigDecimal ljTotle(CustomersQuery query)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT IFNULL(SUM(T6331.F05),0) FROM S61.T6110 INNER JOIN S63.T6331 ON T6110.F01 = T6331.F03 INNER JOIN S63.T6330 ON T6331.F02 = T6330.F01 WHERE 1 = 1");
        ArrayList<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            String ywyId = query.ywyId();
            String name = query.name();
            String tel = query.tel();
            if (ywyId != null && !ywyId.isEmpty())
            {
                sql.append(" AND T6330.F01 = ?");
                parameters.add(ywyId);
            }
            if (name != null && !name.isEmpty())
            {
                sql.append(" AND T6330.F02 = ?");
                parameters.add(name);
            }
            if (tel != null && !tel.isEmpty())
            {
                sql.append(" AND T6330.F03 = ?");
                parameters.add(tel);
            }
            if (query.startTime() != null)
            {
                sql.append(" AND T6331.F04 >= ?");
                parameters.add(query.startTime());
            }
            if (query.endTime() != null)
            {
                sql.append(" AND T6331.F04 <= ?");
                parameters.add(query.endTime());
            }
        }
        try (Connection connection = getConnection())
        {
            return select(connection, new ItemParser<BigDecimal>()
            {
                @Override
                public BigDecimal parse(ResultSet resultSet)
                    throws SQLException
                {
                    if (resultSet.next())
                    {
                        return resultSet.getBigDecimal(1);
                    }
                    return new BigDecimal(0);
                    
                }
            }, sql.toString(), parameters);
        }
        
    }

	private String selectString(Connection connection, int F01) throws SQLException {
	    try (PreparedStatement pstmt = connection.prepareStatement("SELECT F02 FROM S61.T6141 WHERE T6141.F01 = ? LIMIT 1")) {
	        pstmt.setInt(1, F01);
	        try(ResultSet resultSet = pstmt.executeQuery()) {
	            if(resultSet.next()) {
	                return resultSet.getString(1);
	            }
	        }
	    }
	    return "";
	}
	
	@Override
	public String searchTel(String tel) throws Throwable {
		try (Connection conn = getConnection()) {
			try (PreparedStatement ps = conn
					.prepareStatement("SELECT F01 FROM S63.T6330 WHERE F03 = ?");) {
				ps.setString(1, tel);
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						return rs.getString(1);
					}
				}
			}
		}
		return "";
	}

	@Override
    public Customers[] search(CustomersQuery query)
        throws Throwable
    {
        StringBuilder sql =
            new StringBuilder(
                "SELECT T6110.F02 AS F01, T6331.F01 AS F02, T6331.F02 AS F03, T6331.F03 AS F04, T6331.F04 AS F05, T6331.F05 AS F06, T6331.F06 AS F07, T6331.F07 AS F08 FROM S61.T6110 INNER JOIN S63.T6331 ON T6110.F01 = T6331.F03 INNER JOIN S63.T6330 ON T6331.F02 = T6330.F01 WHERE 1 = 1");
        ArrayList<Object> parameters = new ArrayList<>();
        if (query != null)
        {
            String ywyId = query.ywyId();
            String name = query.name();
            String tel = query.tel();
            if (ywyId != null && !ywyId.isEmpty())
            {
                sql.append(" AND T6330.F01 = ?");
                parameters.add(ywyId);
            }
            if (name != null && !name.isEmpty())
            {
                sql.append(" AND T6330.F02 = ?");
                parameters.add(name);
            }
            if (tel != null && !tel.isEmpty())
            {
                sql.append(" AND T6330.F03 = ?");
                parameters.add(tel);
            }
            if (query.startTime() != null)
            {
                sql.append(" AND T6331.F04 >= ?");
                parameters.add(query.startTime());
            }
            if (query.endTime() != null)
            {
                sql.append(" AND T6331.F04 <= ?");
                parameters.add(query.endTime());
            }
        }
        
        sql.append(" ORDER BY T6331.F04 DESC");
        try (Connection connection = getConnection())
        {
            return selectAll(connection, new ArrayParser<Customers>()
            {
                @Override
                public Customers[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<Customers> list = null;
                    while (resultSet.next())
                    {
                        Customers record = new Customers();
                        record.loginName = resultSet.getString(1);
                        record.F01 = resultSet.getInt(2);
                        record.F02 = resultSet.getInt(3);
                        record.F03 = resultSet.getInt(4);
                        record.F04 = resultSet.getTimestamp(5);
                        record.F05 = resultSet.getBigDecimal(6);
                        record.F06 = T6331_F06.parse(resultSet.getString(7));
                        record.F07 = resultSet.getBigDecimal(8);
                        record.userName = selectString(connection, record.F03);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return list == null || list.size() == 0 ? null : list.toArray(new Customers[list.size()]);
                }
            }, sql.toString(), parameters);
        }
    }
	
}
