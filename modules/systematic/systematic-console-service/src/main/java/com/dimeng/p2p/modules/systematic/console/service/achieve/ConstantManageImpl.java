package com.dimeng.p2p.modules.systematic.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.systematic.console.service.ConstantManage;
import com.dimeng.p2p.modules.systematic.console.service.entity.Constant;
import com.dimeng.p2p.modules.systematic.console.service.query.ConstantLogQuery;
import com.dimeng.util.StringHelper;

/**
 * @author guopeng
 * 
 */
public class ConstantManageImpl extends AbstractSystemService implements
		ConstantManage {

	public ConstantManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	public static class EmailManageFactory implements
			ServiceFactory<ConstantManage> {

		@Override
		public ConstantManage newInstance(ServiceResource serviceResource) {
			return new ConstantManageImpl(serviceResource);
		}
	}

	@Override
	public void addConstantLog(String key, String name, String value1,
			String value2) throws Throwable {
		String sql = "INSERT INTO S71.T7121 SET F02=?,F03=?,F04=?,F05=?,F06=?,F07=?";
        try (Connection connection = getConnection())
        {
            execute(connection,
                sql,
                key,
                name,
                value1,
                value2,
                serviceResource.getSession().getAccountId(),
                new Timestamp(System.currentTimeMillis()));
        }
	}

	@Override
	public PagingResult<Constant> search(ConstantLogQuery query, Paging paging)
			throws Throwable {
		StringBuilder sb = new StringBuilder(
				"SELECT T7121.F01,T7121.F02,T7121.F03,T7121.F04,T7121.F05,T7110.F02 AS NAME,T7121.F07 FROM S71.T7121 INNER JOIN S71.T7110 ON T7121.F06=T7110.F01 WHERE 1=1");
		List<Object> parameters = new ArrayList<>();
		if (query != null) {
			String key = query.getKey();
			if (!StringHelper.isEmpty(key)) {
				sb.append(" AND T7121.F02 LIKE ?");
				parameters.add(getSQLConnectionProvider().allMatch(key));
			}
			String name = query.getName();
			if (!StringHelper.isEmpty(name)) {
				sb.append(" AND T7121.F03 LIKE ?");
				parameters.add(getSQLConnectionProvider().allMatch(name));
			}
			Timestamp timeStart = query.getTimeStart();
			Timestamp timeEnd = query.getTimeEnd();
			if (timeStart != null) {
				sb.append(" AND DATE(T7121.F07)>=?");
				parameters.add(timeStart);
			}
			if (timeEnd != null) {
				sb.append(" AND DATE(T7121.F07)<=?");
				parameters.add(timeEnd);
			}
			sb.append(" ORDER BY T7121.F01 DESC");
		}
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Constant>()
            {
                
                @Override
                public Constant[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    List<Constant> lists = new ArrayList<>();
                    while (resultSet.next())
                    {
                        Constant constant = new Constant();
                        constant.id = resultSet.getInt(1);
                        constant.key = resultSet.getString(2);
                        constant.desc = resultSet.getString(3);
                        constant.value1 = resultSet.getString(4);
                        constant.value2 = resultSet.getString(5);
                        constant.name = resultSet.getString(6);
                        constant.updateTime = resultSet.getTimestamp(7);
                        lists.add(constant);
                    }
                    return lists.toArray(new Constant[lists.size()]);
                }
            }, paging, sb.toString(), parameters);
        }
	}

	@Override
	public Constant selectById(int id) throws Throwable {
		try(Connection connection = getConnection()){
			try(PreparedStatement pstmt =
	                connection.prepareStatement("SELECT T7121.F01,T7121.F02,T7121.F03,T7121.F04,T7121.F05,T7110.F02 AS NAME,T7121.F07 FROM S71.T7121 INNER JOIN S71.T7110 ON T7121.F06=T7110.F01 WHERE T7121.F01 = ?")){
				pstmt.setInt(1, id);
				try(ResultSet rs = pstmt.executeQuery()){
					if(rs.next()){
						Constant constant = new Constant();
						constant.id = rs.getInt(1);
						constant.key = rs.getString(2);
						constant.desc = rs.getString(3);
						constant.value1 = rs.getString(4);
						constant.value2 = rs.getString(5);
						constant.name = rs.getString(6);
						constant.updateTime = rs.getTimestamp(7);
						return constant;
					}
				}
			}
			
		}
		return null;
	}
}
