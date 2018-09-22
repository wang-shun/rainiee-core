package com.dimeng.p2p.modules.financing.user.service.achieve;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.p2p.modules.financing.user.service.UserInfoManage;
import com.dimeng.p2p.modules.financing.user.service.entity.UserInfo;

public class UserInfoManageImpl extends AbstractFinancingManage implements UserInfoManage {

	public UserInfoManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}
	
	public static class UserInfoManageFactory implements
	ServiceFactory<UserInfoManage> {
		@Override
		public UserInfoManage newInstance(ServiceResource serviceResource) {
			return new UserInfoManageImpl(serviceResource);
		}
		
	}


	@Override
	public UserInfo search() throws Throwable {
		String sql = "SELECT T6010.F01 ,T6010.F02,T6023.F05 from T6010,T6023  WHERE T6010.F01 = ? AND T6010.F01 = T6023.F01";
		try(Connection connection = getConnection())
		{
			return select(connection,
					new ItemParser<UserInfo>() {
						@Override
						public UserInfo parse(ResultSet resultSet)
								throws SQLException {
							UserInfo userInfo = null;
							while (resultSet.next()) {
								if (userInfo == null) {
									userInfo = new UserInfo();
								}
								userInfo.loginId = resultSet.getInt(1);
								userInfo.loginName = resultSet.getString(2);
								userInfo.kyMoney = resultSet.getBigDecimal(3);
							}

							return userInfo;
						}
					}, sql, serviceResource.getSession().getAccountId());
		}
	}

}
