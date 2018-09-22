package com.dimeng.p2p.modules.finance.console.service.achieve;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.enums.ChargeStatus;
import com.dimeng.p2p.modules.finance.console.service.CzglManage;
import com.dimeng.p2p.modules.finance.console.service.entity.Czgl;
import com.dimeng.p2p.modules.finance.console.service.entity.CzglRecord;
import com.dimeng.p2p.modules.finance.console.service.query.CzglRecordQuery;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.EnumParser;

public class CzglManageImpl extends AbstractFinanceService implements
		CzglManage {

	public static class RechargeManageFactory implements
			ServiceFactory<CzglManage> {

		@Override
		public CzglManage newInstance(ServiceResource serviceResource) {
			return new CzglManageImpl(serviceResource);
		}
	}

	public CzglManageImpl(ServiceResource serviceResource) {
		super(serviceResource);

	}

	public Czgl getRechargeInfo() throws Throwable {
		String sql = "SELECT F01, F02, F03 FROM T7022 ORDER BY F03 DESC LIMIT 1";
		try(Connection connection = getConnection())
		{
			return select(connection, new ItemParser<Czgl>() {
				@Override
				public Czgl parse(ResultSet resultSet) throws SQLException {
					Czgl recharge = new Czgl();
					if (resultSet.next()) {
						recharge.totalAmount = resultSet.getBigDecimal(1);
						recharge.charge = resultSet.getBigDecimal(2);
						recharge.updateTime = resultSet.getTimestamp(3);
					}
					return recharge;
				}
			}, sql);
		}

	}

	public PagingResult<CzglRecord> getUserRechargeRecordList(
			CzglRecordQuery query, Paging paging) throws Throwable {

		StringBuilder sql = new StringBuilder(
				"SELECT T1.F01, T2.F02, T3.F06 AS F39, T1.F04, T1.F09, T1.F03, T1.F07, T1.F06 AS F40 ,T1.F05 FROM T6033 T1 INNER JOIN T6010 T2 ON T1.F02=T2.F01 INNER JOIN T6011 T3 ON T2.F01=T3.F01");
		ArrayList<Object> parameters = new ArrayList<>();
		if (query != null) {
			SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
			String serialNumber = query.getSerialNumber();
			if (!StringHelper.isEmpty(serialNumber)) {
				sql.append(" AND T1.F07 LIKE ?");
				parameters.add(sqlConnectionProvider.allMatch(serialNumber));
			}
			String loginName = query.getLoginName();
			if (!StringHelper.isEmpty(loginName)) {
				sql.append(" AND T2.F02 LIKE ?");
				parameters.add(sqlConnectionProvider.allMatch(loginName));
			}
			Timestamp timestamp = query.getStartRechargeTime();
			if (timestamp != null) {
				sql.append(" AND DATE(T1.F03) >= ?");
				parameters.add(timestamp);
			}
			timestamp = query.getEndRechargeTime();
			if (timestamp != null) {
				sql.append(" AND DATE(T1.F03) <= ?");
				parameters.add(timestamp);
			}
			String payType = query.getPayType();
			if (!StringHelper.isEmpty(payType)) {
				sql.append(" AND T1.F06 = ?");
				parameters.add(payType);
			}
			ChargeStatus status = query.getStatus();
			if (status != null) {
				sql.append(" AND T1.F05 = ?");
				parameters.add(status.name());
			}
		}
		sql.append(" ORDER BY T1.F03 DESC");
		try(Connection connection = getConnection(P2PConst.DB_USER))
		{
			return selectPaging(connection,
					new ArrayParser<CzglRecord>() {
						ArrayList<CzglRecord> list = new ArrayList<CzglRecord>();

						public CzglRecord[] parse(ResultSet rst)
								throws SQLException {
							while (rst.next()) {
								CzglRecord user = new CzglRecord();
								user.id = rst.getInt(1);
								user.loginName = rst.getString(2);
								user.userName = rst.getString(3);
								user.userAmount = rst.getBigDecimal(4);
								user.charge = rst.getBigDecimal(5);
								user.chargeDateTime = rst.getTimestamp(6);
								user.singleNumber = rst.getString(7);
								user.payType = rst.getString(8);
								user.rechargeStatus = EnumParser.parse(
										ChargeStatus.class, rst.getString(9));
								list.add(user);
							}
							return list.toArray(new CzglRecord[list.size()]);
						}
					}, paging, sql.toString(), parameters);
		}
	}
}
