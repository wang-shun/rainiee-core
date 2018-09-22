package com.dimeng.p2p.modules.spread.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S63.enums.T6320_F14;
import com.dimeng.p2p.S63.enums.T6322_F07;
import com.dimeng.p2p.common.enums.ActivityStatus;
import com.dimeng.p2p.modules.spread.console.service.ActivityManage;
import com.dimeng.p2p.modules.spread.console.service.entity.Activity;
import com.dimeng.p2p.modules.spread.console.service.entity.ActivityList;
import com.dimeng.p2p.modules.spread.console.service.entity.ActivityQuery;
import com.dimeng.p2p.modules.spread.console.service.entity.ActivityTotalInfo;
import com.dimeng.p2p.modules.spread.console.service.entity.PartakePersonList;
import com.dimeng.p2p.modules.spread.console.service.entity.PartakeQuery;
import com.dimeng.p2p.modules.spread.console.service.entity.PartakeTotalInfo;
import com.dimeng.util.StringHelper;

/**
 * 活动管理
 * 
 */
public class ActivityManageImp extends AbstractSpreadManage implements
		ActivityManage {

	public ActivityManageImp(ServiceResource serviceResource) {
		super(serviceResource);
	}

	public static class ActivityManageFactory implements
			ServiceFactory<ActivityManage> {
		@Override
		public ActivityManage newInstance(ServiceResource serviceResource) {
			return new ActivityManageImp(serviceResource);
		}

	}

	@Override
	public ActivityTotalInfo getActivityTotalInfo() throws Throwable {
		try (Connection connection = getConnection();
				PreparedStatement ps = connection
						.prepareStatement("SELECT IFNULL(SUM(F08),0),IFNULL(SUM(F07),0) FROM S63.T6320")) {
			try (ResultSet rs = ps.executeQuery()) {
				ActivityTotalInfo info = null;
				if (rs.next()) {
					info = new ActivityTotalInfo();
					info.totalMoney = rs.getBigDecimal(1);
					info.totalPerson = rs.getInt(2);
				}
				return info;
			}
		}

	}

	@Override
	public PagingResult<ActivityList> searchActivity(ActivityQuery query,
			Paging paging) throws Throwable {
		StringBuilder sql = new StringBuilder(
				"SELECT S63.T6320.F01 AS F01,S63.T6320.F02  AS F02,S63.T6320.F03  AS F03,S63.T6320.F04 AS F04,S63.T6320.F05 AS F05,S63.T6320.F06 AS F06,S63.T6320.F07 AS F07,S63.T6320.F08 AS F08,S63.T6320.F11 AS F09,S63.T6320.F13 AS F10,S63.T6320.F15 AS F11,S63.T6320.F16 AS F12,S71.T7110.F02 AS F13,S63.T6320.F18 AS F14 ");
		sql.append(" FROM S63.T6320,S71.T7110 WHERE S63.T6320.F17 = S71.T7110.F01");
		ArrayList<Object> parameters = new ArrayList<>();
		if (query != null) {
			SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
			if (!StringHelper.isEmpty(query.title())) {
				sql.append(" AND S63.T6320.F02 like ?");
				parameters.add(sqlConnectionProvider.allMatch(query.title()));
			}
			if (query.startTime() != null) {
				sql.append(" AND DATE(S63.T6320.F03) >= ?");
				parameters.add(query.startTime());
			}
			if (query.endTime() != null) {
				sql.append(" AND DATE(S63.T6320.F03) <= ?");
				parameters.add(query.endTime());
			}
			ActivityStatus status = query.status();
			if (status != null) {
				Timestamp now = new Timestamp(System.currentTimeMillis());
				if (status.equals(ActivityStatus.WKS)) {
					sql.append(" AND S63.T6320.F03 > ?");
					parameters.add(now);
				} else if (status.equals(ActivityStatus.YJS)) {
					sql.append(" AND S63.T6320.F04 < ?");
					parameters.add(now);
				} else if (status.equals(ActivityStatus.JXZ)) {
					sql.append(" AND S63.T6320.F03 <= ? ");
					parameters.add(now);
					sql.append(" AND S63.T6320.F04 >= ?");
					parameters.add(now);
				}
			}

		}
		sql.append(" ORDER BY F01 DESC ");
		try (Connection connection = getConnection()) {
			return selectPaging(connection, new ArrayParser<ActivityList>() {

				@Override
				public ActivityList[] parse(ResultSet rs) throws SQLException {
					ArrayList<ActivityList> list = null;
					Timestamp now = new Timestamp(System.currentTimeMillis());
					while (rs.next()) {
						ActivityList info = new ActivityList();
						info.F01 = rs.getInt(1);
						info.F02 = rs.getString(2);
						info.F03 = rs.getTimestamp(3);
						info.F04 = rs.getTimestamp(4);
						info.F05 = rs.getDate(5);
						info.F06 = rs.getDate(6);
						info.F07 = rs.getInt(7);
						info.F08 = rs.getBigDecimal(8);
						info.F11 = rs.getBigDecimal(9);
						info.F13 = rs.getBigDecimal(10);
						info.F15 = rs.getBigDecimal(11);
						info.F16 = rs.getInt(12);
						info.F17 = rs.getString(13);
						info.F18 = rs.getInt(14);
						if (now.before(rs.getTimestamp(3))) {
							info.F14 = T6320_F14.XJ;
						} else if (now.after(rs.getTimestamp(3))
								&& now.before(rs.getTimestamp(4))) {
							info.F14 = T6320_F14.SX;
						} else {
							info.F14 = T6320_F14.YJS;
						}
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(info);
					}
					return list == null || list.size() == 0 ? null : list
							.toArray(new ActivityList[list.size()]);
				}
			}, paging, sql.toString(), parameters);
		}
	}

	@Override
	public void addActivity(Activity activity) throws Throwable {
		String title = activity.title();
		if (StringHelper.isEmpty(title) || title.length() > 15) {
			throw new ParameterException("活动主题不能为空且15字之内");
		}
		Timestamp startTime = activity.startTime();
		Timestamp endTime = activity.endTime();
		if (startTime == null || endTime == null) {
			throw new ParameterException("开始/结束时间不能为空");
		}
		try (Connection connection = getConnection();
				PreparedStatement ps = connection
						.prepareStatement("INSERT INTO S63.T6320 SET F02=?,F03=?,F04=?,F05=?,F06=?,F07=?,F08=?,F11=?,F13=?,F16=?,F17=?")) {
			ps.setString(1, title);
			ps.setTimestamp(2, startTime);
			ps.setTimestamp(3, endTime);
			ps.setDate(4, activity.couponStartTime());
			ps.setDate(5, activity.couponEndTime());
			ps.setInt(6, activity.amount());
			ps.setBigDecimal(7, activity.money());
			ps.setBigDecimal(8, activity.leastRecharge());
			ps.setBigDecimal(9, activity.leastInvest());
			ps.setInt(10, activity.spreadPersons());
			ps.setInt(11, serviceResource.getSession().getAccountId());
			ps.executeUpdate();
		}

	}

	@Override
	public ActivityList getActivity(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT S63.T6320.F01 AS F01,S63.T6320.F02  AS F02,S63.T6320.F03  AS F03,S63.T6320.F04 AS F04,S63.T6320.F05 AS F05,S63.T6320.F06 AS F06,S63.T6320.F07 AS F07,S63.T6320.F08 AS F08,S63.T6320.F11 AS F09,S63.T6320.F13 AS F10,S63.T6320.F15 AS F11,S63.T6320.F16 AS F12,S71.T7110.F02 AS F13,S63.T6320.F18 AS F14"
							+ " FROM S63.T6320,S71.T7110 WHERE S63.T6320.F17 = S71.T7110.F01 AND S63.T6320.F01 = ?")) {
				ps.setInt(1, id);
				try (ResultSet rs = ps.executeQuery()) {
					ActivityList info = null;
					if (rs.next()) {
						Timestamp now = new Timestamp(
								System.currentTimeMillis());
						info = new ActivityList();
						info.F01 = rs.getInt(1);
						info.F02 = rs.getString(2);
						info.F03 = rs.getTimestamp(3);
						info.F04 = rs.getTimestamp(4);
						info.F05 = rs.getDate(5);
						info.F06 = rs.getDate(6);
						info.F07 = rs.getInt(7);
						info.F08 = rs.getBigDecimal(8);
						info.F11 = rs.getBigDecimal(9);
						info.F13 = rs.getBigDecimal(10);
						info.F15 = rs.getBigDecimal(11);
						info.F16 = rs.getInt(12);
						info.F17 = rs.getString(13);
						info.F18 = rs.getInt(14);
						if (now.before(rs.getTimestamp(3))) {
							info.F14 = T6320_F14.XJ;
						} else if (now.after(rs.getTimestamp(3))
								&& now.before(rs.getTimestamp(4))) {
							info.F14 = T6320_F14.SX;
						} else {
							info.F14 = T6320_F14.YJS;
						}
					}
					return info;
				}
			}
		}

	}

	@Override
	public void updateActivity(int id, Activity activity) throws Throwable {
		String title = activity.title();
		if (StringHelper.isEmpty(title) || title.length() > 15) {
			throw new ParameterException("活动主题不能为空且15字之内");
		}
		Timestamp startTime = activity.startTime();
		Timestamp endTime = activity.endTime();
		if (startTime == null || endTime == null) {
			throw new ParameterException("开始/结束时间不能为空");
		}

		try (Connection connection = getConnection();
				PreparedStatement ps = connection
						.prepareStatement("UPDATE S63.T6320 SET F02=?,F03=?,F04=?,F05=?,F06=?,F07=?,F08=?,F11=?,F13=?,F16=?,F17=? WHERE F01=?")) {
			ps.setString(1, title);
			ps.setTimestamp(2, startTime);
			ps.setTimestamp(3, endTime);
			ps.setDate(4, activity.couponStartTime());
			ps.setDate(5, activity.couponEndTime());
			ps.setInt(6, activity.amount());
			ps.setBigDecimal(7, activity.money());
			ps.setBigDecimal(8, activity.leastRecharge());
			ps.setBigDecimal(9, activity.leastInvest());
			ps.setInt(10, activity.spreadPersons());
			ps.setInt(11, serviceResource.getSession().getAccountId());
			ps.setInt(12, id);
			ps.executeUpdate();
		}

	}

	@Override
	public PagingResult<PartakePersonList> searchPartake(int id,
			PartakeQuery query, Paging paging) throws Throwable {
		StringBuilder sql = new StringBuilder(
				"SELECT S61.T6110.F02 AS F01,S61.T6141.F02 AS F02,S63.T6322.F06 AS F03,S63.T6322.F07 AS F04,S63.T6322.F05 AS F05,S63.T6322.F09 AS F06 FROM ");
		sql.append(" S63.T6322 INNER JOIN ");
		sql.append(" S61.T6110 ON S63.T6322.F03=S61.T6110.F01 INNER JOIN ");
		sql.append(" S61.T6141 ON S63.T6322.F03=S61.T6141.F01 WHERE S63.T6322.F02 = ?");

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(id);
		SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
		if (query != null) {
			if (!StringHelper.isEmpty(query.userName())) {
				sql.append(" AND S61.T6110.F02 LIKE ? ");
				parameters
						.add(sqlConnectionProvider.allMatch(query.userName()));
			}
			if (query.startTime() != null) {
				sql.append(" AND DATE(S63.T6322.F05) >= ?");
				parameters.add(query.startTime());
			}
			if (query.endTime() != null) {
				sql.append(" AND DATE(S63.T6322.F05) <= ? ");
				parameters.add(query.endTime());
			}
		}
		try (Connection connection = getConnection()) {
			return selectPaging(connection,
					new ArrayParser<PartakePersonList>() {

						@Override
						public PartakePersonList[] parse(ResultSet rs)
								throws SQLException {
							ArrayList<PartakePersonList> list = null;
							while (rs.next()) {
								PartakePersonList info = new PartakePersonList();
								info.userName = rs.getString(1);
								info.name = rs.getString(2);
								info.reward = rs.getBigDecimal(3);
								info.T6322_F07 = T6322_F07.valueOf(rs
										.getString(4));
								info.getTime = rs.getTimestamp(5);
								info.useTime = rs.getTimestamp(6);
								if (list == null) {
									list = new ArrayList<>();
								}
								list.add(info);
							}

							return list == null || list.size() == 0 ? null
									: list.toArray(new PartakePersonList[list
											.size()]);
						}
					}, paging, sql.toString(), parameters);
		}
	}

	@Override
	public PartakeTotalInfo getPartakeTotalInfo(int id) throws Throwable {
		try (Connection connection = getConnection();
				PreparedStatement ps = connection
						.prepareStatement("SELECT F02,IFNULL(SUM(F06),0),IFNULL(COUNT(F03),0) FROM S63.T6322 WHERE F02 = ?")) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				PartakeTotalInfo info = null;
				if (rs.next()) {
					info = new PartakeTotalInfo();
					info.id = rs.getInt(1);
					info.totalMoney = rs.getBigDecimal(2);
					info.personNum = rs.getInt(3);
				}
				return info;
			}
		}

	}

	public ActivityList[] exportActivity(ActivityQuery query) throws Throwable {
		StringBuilder sql = new StringBuilder(
				"SELECT S63.T6320.F01 AS F01,S63.T6320.F02  AS F02,S63.T6320.F03  AS F03,S63.T6320.F04 AS F04,S63.T6320.F05 AS F05,S63.T6320.F06 AS F06,S63.T6320.F07 AS F07,S63.T6320.F08 AS F08,S63.T6320.F11 AS F09,S63.T6320.F13 AS F10,S63.T6320.F15 AS F11,S63.T6320.F16 AS F12,S71.T7110.F02 AS F13,S63.T6320.F18 AS F14 ");
		sql.append(" FROM S63.T6320,S71.T7110 WHERE S63.T6320.F17 = S71.T7110.F01");
		ArrayList<Object> parameters = new ArrayList<>();
		if (query != null) {
			SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
			if (!StringHelper.isEmpty(query.title())) {
				sql.append(" AND S63.T6320.F02 like ?");
				parameters.add(sqlConnectionProvider.allMatch(query.title()));
			}
			if (query.startTime() != null) {
				sql.append(" AND DATE(S63.T6320.F03) >= ?");
				parameters.add(query.startTime());
			}
			if (query.endTime() != null) {
				sql.append(" AND DATE(S63.T6320.F03) <= ?");
				parameters.add(query.endTime());
			}
			ActivityStatus status = query.status();
			if (status != null) {
				Timestamp now = new Timestamp(System.currentTimeMillis());
				if (status.equals(ActivityStatus.WKS)) {
					sql.append(" AND S63.T6320.F03 > ?");
					parameters.add(now);
				} else if (status.equals(ActivityStatus.YJS)) {
					sql.append(" AND S63.T6320.F04 < ?");
					parameters.add(now);
				} else if (status.equals(ActivityStatus.JXZ)) {
					sql.append(" AND S63.T6320.F03 <= ? ");
					parameters.add(now);
					sql.append(" AND S63.T6320.F04 >= ?");
					parameters.add(now);
				}
			}

		}
		sql.append(" ORDER BY F01 DESC ");
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(sql
					.toString())) {
				int index = 1;
				for (Object obj : parameters) {
					ps.setObject(index++, obj);
				}

				try (ResultSet rs = ps.executeQuery()) {
					Timestamp now = new Timestamp(System.currentTimeMillis());
					ArrayList<ActivityList> list = null;
					while (rs.next()) {
						ActivityList info = new ActivityList();
						info.F01 = rs.getInt(1);
						info.F02 = rs.getString(2);
						info.F03 = rs.getTimestamp(3);
						info.F04 = rs.getTimestamp(4);
						info.F05 = rs.getDate(5);
						info.F06 = rs.getDate(6);
						info.F07 = rs.getInt(7);
						info.F08 = rs.getBigDecimal(8);
						info.F11 = rs.getBigDecimal(9);
						info.F13 = rs.getBigDecimal(10);
						info.F15 = rs.getBigDecimal(11);
						info.F16 = rs.getInt(12);
						info.F17 = rs.getString(13);
						info.F18 = rs.getInt(14);
						if (now.before(rs.getTimestamp(3))) {
							info.F14 = T6320_F14.XJ;
						} else if (now.after(rs.getTimestamp(3))
								&& now.before(rs.getTimestamp(4))) {
							info.F14 = T6320_F14.SX;
						} else {
							info.F14 = T6320_F14.YJS;
						}
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(info);
					}
					return list == null || list.size() == 0 ? null : list
							.toArray(new ActivityList[list.size()]);
				}
			}
		}

	}

	@Override
	public PartakePersonList[] exportPartakePerson(int id, PartakeQuery query)
			throws Throwable {
		StringBuilder sql = new StringBuilder(
				"SELECT S61.T6110.F02 AS F01,S61.T6141.F02 AS F02,S63.T6322.F06 AS F03,S63.T6322.F07 AS F04,S63.T6322.F05 AS F05,S63.T6322.F09 AS F06 FROM ");
		sql.append(" S63.T6322 INNER JOIN ");
		sql.append(" S61.T6110 ON S63.T6322.F03=S61.T6110.F01 INNER JOIN ");
		sql.append(" S61.T6141 ON S63.T6322.F03=S61.T6141.F01 WHERE S63.T6322.F02 = ?");

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(id);
		SQLConnectionProvider sqlConnectionProvider = getSQLConnectionProvider();
		if (query != null) {
			if (!StringHelper.isEmpty(query.userName())) {
				sql.append(" AND S61.T6110.F02 LIKE ? ");
				parameters
						.add(sqlConnectionProvider.allMatch(query.userName()));
			}
			if (query.startTime() != null) {
				sql.append(" AND DATE(S63.T6322.F05) >= ?");
				parameters.add(query.startTime());
			}
			if (query.endTime() != null) {
				sql.append(" AND DATE(S63.T6322.F05) <= ? ");
				parameters.add(query.endTime());
			}
		}

		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(sql
					.toString())) {
				ArrayList<PartakePersonList> list = null;
				int index = 1;
				for (Object obj : parameters) {
					ps.setObject(index++, obj);
				}
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						PartakePersonList info = new PartakePersonList();
						info.userName = rs.getString(1);
						info.name = rs.getString(2);
						info.reward = rs.getBigDecimal(3);
						info.T6322_F07 = T6322_F07.valueOf(rs.getString(4));
						info.getTime = rs.getTimestamp(5);
						info.useTime = rs.getTimestamp(6);
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(info);
					}
				}

				return list == null || list.size() == 0 ? null : list
						.toArray(new PartakePersonList[list.size()]);
			}
		}
	}

}
