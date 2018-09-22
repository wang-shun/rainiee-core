package com.dimeng.p2p.modules.account.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S71.entities.T7168;
import com.dimeng.p2p.modules.account.console.service.KhjlManage;
import com.dimeng.p2p.modules.account.console.service.entity.Grxx;
import com.dimeng.util.StringHelper;

public class KhjlManageImpl extends AbstractUserService implements KhjlManage {

	public KhjlManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
    public void add(int id, String name)
        throws Throwable
    {
        int khjlID = getAccountID(name);
        if (khjlID <= 0)
        {
            throw new ParameterException("账号不存在,请重新输入");
        }
        try (Connection connection = getConnection())
        {
            if (isExist(id))
            {
                execute(connection, "UPDATE S71.T7167 SET F02 = ? WHERE F03 = ?", khjlID, id);
            }
            else
            {
                insert(connection, "INSERT INTO S71.T7167 SET F02 = ?, F03 = ?", khjlID, id);
            }
        }
    }

	// 通过登录账号得到账号ID
	private int getAccountID(String name) throws SQLException {
		try (Connection connection = getConnection()) {
			// 账号id
			int id = 0;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01 FROM S71.T7110 WHERE T7110.F02 = ? LIMIT 1")) {
				pstmt.setString(1, name);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						id = resultSet.getInt(1);
					}
				}
			}
			return id;
		}
	}

	@Override
	public boolean isExist(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01 FROM S71.T7167 WHERE T7167.F03 = ? LIMIT 1")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
    public PagingResult<Grxx> search(Grxx grxx, Paging paging)
        throws Throwable
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT T6110.F01 AS F01,T6110.F02 AS F02,T6110.F04 AS F03,T6110.F05 AS F04,T6141.F02 AS F05,T6110.F07 AS F06 FROM S71.T7167");
        sql.append(" INNER JOIN S61.T6110 ON T7167.F03 = T6110.F01");
        sql.append(" INNER JOIN S61.T6141 ON T6110.F01 = T6141.F01");
        sql.append(" WHERE T7167.F02 = ?");
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(serviceResource.getSession().getAccountId());
        SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
        String userName = grxx.userName;
        if (!StringHelper.isEmpty(userName))
        {
            sql.append(" AND T6110.F02 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(userName));
        }
        String name = grxx.name;
        if (!StringHelper.isEmpty(name))
        {
            sql.append(" AND T6141.F02 LIKE ?");
            parameters.add(sqlConnectionProvider.allMatch(name));
        }
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<Grxx>()
            {
                
                @Override
                public Grxx[] parse(ResultSet rs)
                    throws SQLException
                {
                    ArrayList<Grxx> list = null;
                    while (rs.next())
                    {
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        Grxx grxx = new Grxx();
                        grxx.id = rs.getInt(1);
                        grxx.userName = rs.getString(2);
                        grxx.phone = rs.getString(3);
                        grxx.email = rs.getString(4);
                        grxx.name = rs.getString(5);
                        grxx.status = T6110_F07.parse(rs.getString(6));
                        grxx.loginTime = getLastLoginTime(rs.getInt(1));
                        list.add(grxx);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new Grxx[list.size()]));
                }
            }, paging, sql.toString(), parameters);
        }
    }

	@Override
	public PagingResult<T7168> searchHfjl(int userID, Paging paging)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T7168>()
            {
                @Override
                public T7168[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<T7168> list = null;
                    while (resultSet.next())
                    {
                        T7168 record = new T7168();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getTimestamp(4);
                        record.F05 = resultSet.getTimestamp(5);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new T7168[list.size()]));
                }
            }, paging, "SELECT F01, F02, F03, F04 ,F05 FROM S71.T7168 WHERE T7168.F02 = ? ORDER BY F04 DESC", userID);
        }
    }

	@Override
    public void addHfjl(T7168 entity)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            insert(connection,
                "INSERT INTO S71.T7168 SET F02 = ?, F03 = ?, F04 = ?",
                entity.F02,
                entity.F03,
                entity.F04);
        }
    }

	//根据用户ID，得到用户的最后登录时间
	private Timestamp getLastLoginTime(int userID) throws SQLException {
		try(Connection connection = getConnection()){
			try (PreparedStatement pstmt = connection.prepareStatement("SELECT F03 FROM S61.T6190 WHERE T6190.F02 = ? AND T6190.F05 = ? ORDER BY F01 DESC LIMIT 1")) {
		        pstmt.setInt(1, userID);
		        pstmt.setString(2, "登录前台系统");
		        try(ResultSet resultSet = pstmt.executeQuery()) {
		            if(resultSet.next()) {
		                return resultSet.getTimestamp(1);
		            }
		        }
		    }
		}
		return null;
	}
}
