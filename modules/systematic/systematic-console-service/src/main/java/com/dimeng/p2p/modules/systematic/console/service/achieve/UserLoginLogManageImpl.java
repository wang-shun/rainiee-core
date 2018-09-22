package com.dimeng.p2p.modules.systematic.console.service.achieve;

import java.sql.Connection;
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
import com.dimeng.p2p.common.enums.FrontLogType;
import com.dimeng.p2p.modules.systematic.console.service.UserLoginLogManage;
import com.dimeng.p2p.modules.systematic.console.service.entity.UserLog;
import com.dimeng.p2p.modules.systematic.console.service.query.UserLogQuery;
import com.dimeng.util.StringHelper;

public class UserLoginLogManageImpl extends AbstractSystemService implements
		UserLoginLogManage {

	public static class UserLoginLogManageFactory implements
			ServiceFactory<UserLoginLogManage> {

		@Override
		public UserLoginLogManage newInstance(ServiceResource serviceResource) {
			return new UserLoginLogManageImpl(serviceResource);
		}
	}

	public UserLoginLogManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public PagingResult<UserLog> seacrch(UserLogQuery query, Paging paging)
			throws Throwable {
		String sql = "SELECT T6190.F01,T6190.F03,T6190.F04,T6190.F05,T6190.F06,T6110.F02 AS NAME FROM S61.T6190 INNER JOIN T6110 ON T6190.F02=T6110.F01 WHERE 1=1";
		StringBuilder sb = new StringBuilder();
		sb.append(sql);
		List<Object> parameters = new ArrayList<>();
		if (query != null) {
			String accountName = query.getAccountName();
            FrontLogType type = query.getType();
			Timestamp createTimeStart = query.getCreateTimeStart();
			Timestamp createTimeEnd = query.getCreateTimeEnd();
            if (type != null)
            {
                sb.append(" AND T6190.F04=? ");
                parameters.add(type.getName());
            }
			if (!StringHelper.isEmpty(accountName)) {
				sb.append(" AND T6110.F02 LIKE ?");
				parameters
						.add(getSQLConnectionProvider().allMatch(accountName));
			}
			if (createTimeStart != null) {
				sb.append(" AND DATE(T6190.F03)>=?");
				parameters.add(createTimeStart);
			}
			if (createTimeEnd != null) {
				sb.append(" AND DATE(T6190.F03)<=?");
				parameters.add(createTimeEnd);
			}
		}
		sb.append(" ORDER BY T6190.F03 DESC");
		try(Connection connection = getConnection("S61")) {
			return selectPaging(connection, new ArrayParser<UserLog>() {

				@Override
				public UserLog[] parse(ResultSet resultSet) throws SQLException {
					List<UserLog> lists = new ArrayList<>();
					while (resultSet.next()) {
						UserLog userLog = new UserLog();
						userLog.F01 = resultSet.getInt(1);
						userLog.F03 = resultSet.getTimestamp(2);
						userLog.F04 = resultSet.getString(3);
						userLog.F05 = resultSet.getString(4);
						userLog.F06 = resultSet.getString(5);
						userLog.accountName = resultSet.getString(6);
						lists.add(userLog);
					}
					return lists.toArray(new UserLog[lists.size()]);
				}
			}, paging, sb.toString(), parameters);
		}
	}
}
