package com.dimeng.p2p.account.user.service.achieve;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6195_EXT;
import com.dimeng.p2p.S61.enums.T6195_F06;
import com.dimeng.p2p.S61.enums.T6195_F08;
import com.dimeng.p2p.account.user.service.TzjyManage;
import com.dimeng.util.StringHelper;

import java.sql.*;
import java.util.ArrayList;

public class TzjyManageImpl extends AbstractAccountService implements TzjyManage {

	public static class TzjyManageFactory implements
			ServiceFactory<TzjyManage> {

		@Override
		public TzjyManage newInstance(ServiceResource serviceResource) {
			return new TzjyManageImpl(serviceResource);
		}

	}

	public TzjyManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	protected static final ArrayParser<T6195_EXT> ARRAY_PARSER = new ArrayParser<T6195_EXT>() {

		@Override
		public T6195_EXT[] parse(ResultSet resultSet) throws SQLException {
			ArrayList<T6195_EXT> list = new ArrayList<>();
			while (resultSet.next()) {
                T6195_EXT record = new T6195_EXT();
                record.F01 = resultSet.getInt(1);
                record.F02 = resultSet.getInt(2);
                record.F03 = resultSet.getString(3);
                record.F04 = resultSet.getTimestamp(4);
                record.F05 = resultSet.getString(5);
                record.F06 = T6195_F06.parse(resultSet.getString(6));
                record.F07 = resultSet.getInt(7);
                record.F08 = T6195_F08.parse(resultSet.getString(8));
                record.F09 = resultSet.getTimestamp(9);
                record.replyName = resultSet.getString(10);
                String userName = resultSet.getString(11);
                record.userName = userName.substring(0, 2) + "***" +userName.substring(userName.length() - 2, userName.length());
				list.add(record);
			}
			return list .toArray(new T6195_EXT[0]);
		}
	};


	protected static final String SELECT_ALL_SQL = "SELECT T6195.F01,T6195.F02,T6195.F03,T6195.F04,T6195.F05,T6195.F06,T6195.F07,T6195.F08,T6195.F09,T7110.F04 AS F10,T6110.F02 AS F11 " +
            "FROM S61.T6195 LEFT JOIN S71.T7110 ON T6195.F07 = T7110.F01 " +
            "INNER JOIN S61.T6110 ON T6195.F02 = T6110.F01 "  ;

	@Override
	public PagingResult<T6195_EXT> search(int userId, Paging paging)
        throws Throwable
    {
        ArrayList<Object> parameters = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder(SELECT_ALL_SQL);
        sql.append(" WHERE 1=1 ");
        if (userId <= 0)
        {
            sql.append(" AND T6195.F08 = ?");
            parameters.add(T6195_F08.YFB.name());
            sql.append(" ORDER BY T6195.F09 DESC");
        }else{
            sql.append(" AND T6195.F02 = ?");
            parameters.add(userId);
            sql.append(" ORDER BY T6195.F04 DESC");
        }

        try (Connection connection = getConnection())
        {
            return selectPaging(connection, ARRAY_PARSER, paging, sql.toString(), parameters);
        }
    }

	@Override
    public void saveInfo(int userId, String content)
        throws Throwable
    {
        if (userId <= 0 || StringHelper.isEmpty(content))
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            execute(connection, "INSERT INTO S61.T6195(F02, F03, F04) VALUES(?, ?, ?)",
                    userId, content, getCurrentTimestamp(connection));
        }
    }

    @Override
    public int getAdvCount(int userId)  throws Throwable{
        String sqlStr = "SELECT COUNT(1) FROM S61.T6195 WHERE F02 = ? AND DATE_FORMAT(F04,'%Y-%m-%d') = ?";
        try(Connection connection = getConnection()){
            try(PreparedStatement pstm = connection.prepareStatement(sqlStr)){
                pstm.setInt(1, userId);
                pstm.setDate(2, getCurrentDate(connection));
                try (ResultSet resultSet = pstm.executeQuery()){
                    if(resultSet.next()){
                        return resultSet.getInt(1);
                    }
                }
            }
        }
        return 0;
    }

}
