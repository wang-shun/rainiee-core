package com.dimeng.p2p.modules.financing.front.service.achieve;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.enums.UserStatus;
import com.dimeng.p2p.modules.financing.front.service.UserBlacklistManage;
import com.dimeng.p2p.modules.financing.front.service.entity.UserBlack;
import com.dimeng.p2p.variables.P2PConst;

public class UserBlacklistManageImpl extends AbstractFinancingManage implements UserBlacklistManage {

	public UserBlacklistManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}
	
	public static class UserBlacklistManageFactory implements
	ServiceFactory<UserBlacklistManage> {
		@Override
		public UserBlacklistManage newInstance(ServiceResource serviceResource) {
			return new UserBlacklistManageImpl(serviceResource);
		}
		
	}

	@Override
	public PagingResult<UserBlack> search(Paging paging) throws Throwable {
		ArrayList<Object> parameters = new ArrayList<Object>();
		String sql="SELECT T6011.F29 img, T6011.F06 userName, T6011.F07 card, T6011.F24 address, T6011.F02 tel, T6011.F04 email, T6045.F02 yqCount, T6045.F03 yzyq, T6045.F04 zcyq, T6045.F05 dhje, T6010.F02 loginName FROM T6011, T6045, T6010 WHERE T6011.F01 = T6045.F01 AND T6011.F01 = T6010.F01 AND T6011.F28 = ?";
		parameters.add(UserStatus.S);
		try(Connection connection= getConnection(P2PConst.DB_USER))
		{
			return selectPaging(connection,
					new ArrayParser<UserBlack>() {
						@Override
						public UserBlack[] parse(ResultSet resultSet)
								throws SQLException {
							ArrayList<UserBlack> list = null;
							while (resultSet.next()) {
								UserBlack ub = new UserBlack();
								ub.imgPath = resultSet.getString(1);
								ub.realName = resultSet.getString(2);
								ub.card = resultSet.getString(3);
								ub.city = resultSet.getString(4);
								ub.telphone = resultSet.getString(5);
								ub.email = resultSet.getString(6);
								ub.yqCount = resultSet.getInt(7);
								ub.yzyqCount = resultSet.getInt(8);
								ub.zcyzyqDay = resultSet.getInt(9);
								ub.dhMoney = resultSet.getBigDecimal(10);
								ub.loginName = resultSet.getString(11);
								if (list == null) {
									list = new ArrayList<>();
								}
								list.add(ub);
							}
							return list == null ? null : list
									.toArray(new UserBlack[list.size()]);
						}
					}, paging, sql,parameters);
		}
	}

}
