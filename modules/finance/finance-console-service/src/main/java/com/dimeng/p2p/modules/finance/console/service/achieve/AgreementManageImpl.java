package com.dimeng.p2p.modules.finance.console.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.p2p.common.enums.CreditType;
import com.dimeng.p2p.modules.finance.console.service.AgreementManage;
import com.dimeng.p2p.modules.finance.console.service.entity.Borrower;
import com.dimeng.p2p.modules.finance.console.service.entity.CreditDetail;
import com.dimeng.p2p.modules.finance.console.service.entity.CreditTrs;
import com.dimeng.p2p.modules.finance.console.service.entity.JoinedUser;
import com.dimeng.p2p.modules.finance.console.service.entity.Organization;
import com.dimeng.p2p.modules.finance.console.service.entity.YxDetail;
import com.dimeng.p2p.modules.finance.console.service.entity.YxJoined;
import com.dimeng.p2p.modules.finance.console.service.entity.YxUser;
import com.dimeng.p2p.modules.finance.console.service.entity.ZqzrCreditDetail;
import com.dimeng.p2p.modules.finance.console.service.entity.ZqzrInfo;
import com.dimeng.p2p.modules.finance.console.service.entity.ZqzrUser;
import com.dimeng.util.parser.EnumParser;

public class AgreementManageImpl extends AbstractFinanceService implements
		AgreementManage {

	public AgreementManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	public static class AgreementManageFactory implements
			ServiceFactory<AgreementManage> {

		@Override
		public AgreementManage newInstance(ServiceResource serviceResource) {
			return new AgreementManageImpl(serviceResource);
		}

	}

	@Override
	public JoinedUser[] getJoinedUser(int creditId) throws Throwable {
		try(Connection connection = getUserConnection())
		{
			return selectAll(
					connection
					,
					new ArrayParser<JoinedUser>() {

						@Override
						public JoinedUser[] parse(ResultSet resultSet)
								throws SQLException {
							List<JoinedUser> joinedUsers = null;
							while (resultSet.next()) {
								if (joinedUsers == null) {
									joinedUsers = new ArrayList<>();
								}
								JoinedUser joinedUser = new JoinedUser();
								joinedUser.userName = resultSet.getString(1);
								joinedUser.amount = resultSet.getBigDecimal(2);
								joinedUser.limitMonth = resultSet.getInt(3);

								// 月利率
								BigDecimal onehundred = new BigDecimal(100);
								double monthRate = resultSet.getDouble(4) / 12;
								BigDecimal bigDecimal = onehundred
										.multiply(new BigDecimal(monthRate));
								double bigDecimal2 = Math.pow(1 + monthRate,
										joinedUser.limitMonth);
								BigDecimal bigDecimal3 = bigDecimal
										.multiply(new BigDecimal(bigDecimal2));
								double bigDecimal4 = Math.pow(1 + monthRate,
										joinedUser.limitMonth) - 1;
								// 每月还款本息
								BigDecimal payAmount = bigDecimal3.divide(
										new BigDecimal(bigDecimal4),
										BigDecimal.ROUND_DOWN).setScale(2,
										BigDecimal.ROUND_HALF_UP);
								joinedUser.monthAmount = payAmount
										.multiply(joinedUser.amount
												.divide(onehundred));

								joinedUsers.add(joinedUser);
							}
							return joinedUsers == null ? null : joinedUsers
									.toArray(new JoinedUser[joinedUsers.size()]);
						}
					},
					"SELECT T6010.F02 AS F01, T6037.F04 AS F02, T6036.F08 AS F03, T6036.F09 AS F04 FROM T6037 INNER JOIN T6036 ON T6037.F02=T6036.F01 INNER JOIN T6010 ON T6010.F01=T6037.F03 WHERE T6037.F02=?",
					creditId);
		}

	}

	@Override
	public Borrower getBorrower(int creditId) throws Throwable {
		int accountId = 0;
		Borrower borrower = null;
		try (Connection connection = getUserConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F02 FROM T6036 WHERE F01=?")) {
				ps.setInt(1, creditId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						accountId = resultSet.getInt(1);
					}
				}
			}
			if (accountId <= 0) {
				return null;
			}
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F02 FROM T6010 WHERE F01=?")) {
				ps.setInt(1, accountId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						if (borrower == null) {
							borrower = new Borrower();
						}
						borrower.accountName = resultSet.getString(1);
					}
				}
			}
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F06,F07 FROM T6011 WHERE F01=?")) {
				ps.setInt(1, accountId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						if (borrower == null) {
							borrower = new Borrower();
						}
						borrower.realName = resultSet.getString(1);
						borrower.identifyId = resultSet.getString(2);
					}
				}
			}
		}
		return borrower;
	}

	@Override
	public CreditDetail getCreditDetail(int creditId) throws Throwable {

		try(Connection connection = getUserConnection())
		{
			return select(connection, new ItemParser<CreditDetail>() {

				@Override
				public CreditDetail parse(ResultSet resultSet) throws SQLException {
					CreditDetail creditDetail = null;
					if (resultSet.next()) {
						if (creditDetail == null) {
							creditDetail = new CreditDetail();
						}
						creditDetail.use = resultSet.getString(1);
						creditDetail.amount = resultSet.getBigDecimal(2);
						creditDetail.limitMonth = resultSet.getInt(3);
						creditDetail.monthAmount = resultSet.getBigDecimal(4);
						creditDetail.startDate = resultSet.getTimestamp(5);
						Calendar calendar = Calendar.getInstance();
						calendar.setTimeInMillis(creditDetail.startDate.getTime());
						calendar.add(Calendar.MONTH, creditDetail.limitMonth);
						creditDetail.endDate = new Timestamp(calendar
								.getTimeInMillis());
						creditDetail.rate = resultSet.getBigDecimal(6);
					}
					return creditDetail;
				}
			}, "SELECT F05,F06,F08,F13,F31,F09 FROM T6036 WHERE F01=?", creditId);
		}

	}

	@Override
	public CreditTrs[] getCreditTransfers(int creditId) throws Throwable {
		try(Connection connection = getUserConnection())
		{
			CreditTrs[] creditTransfers = selectAll(
					connection,
					new ArrayParser<CreditTrs>() {

						@Override
						public CreditTrs[] parse(ResultSet resultSet)
								throws SQLException {
							List<CreditTrs> transfers = null;
							while (resultSet.next()) {
								if (transfers == null) {
									transfers = new ArrayList<>();
								}
								CreditTrs transfer = new CreditTrs();
								transfer.outId = resultSet.getInt(1);
								transfer.inId = resultSet.getInt(2);
								transfer.amount = resultSet.getBigDecimal(3);
								transfer.time = resultSet.getTimestamp(4);
								transfers.add(transfer);
							}
							return transfers == null ? null : transfers
									.toArray(new CreditTrs[transfers.size()]);
						}
					},
					"SELECT T6039.F04 AS F01,T6040.F03 AS F02,T6040.F04*T6039.F06 AS F03,T6040.F05 AS F04 FROM T6039 INNER JOIN T6040 ON T6039.F01=T6040.F02 WHERE T6039.F03=?",
					creditId);


			if (creditTransfers == null || creditTransfers.length <= 0) {
				return null;
			}

			String sql = "SELECT F02 FROM T6010 WHERE F01=?";
			for (CreditTrs creditTransfer : creditTransfers) {
				try (PreparedStatement ps = connection.prepareStatement(sql)) {
					ps.setInt(1, creditTransfer.outId);
					try (ResultSet resultSet = ps.executeQuery()) {
						if (resultSet.next()) {
							creditTransfer.out = resultSet.getString(1);
						}
					}
				}
				try (PreparedStatement ps = connection.prepareStatement(sql)) {
					ps.setInt(1, creditTransfer.inId);
					try (ResultSet resultSet = ps.executeQuery()) {
						if (resultSet.next()) {
							creditTransfer.in = resultSet.getString(1);
						}
					}
				}
			}

			return creditTransfers;
		}
	}

	@Override
	public Organization getOrganization(int creditId) throws Throwable {
		int contractId = 0;
		try (Connection connection = getUserConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F25 FROM T6036 WHERE F01=?")) {
				ps.setInt(1, creditId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						contractId = resultSet.getInt(1);
					}
				}
			}

			if (contractId <= 0) {
				return null;
			}

			int orgId = 0;
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F02 FROM T7031 WHERE F01=?")) {
				ps.setInt(1, contractId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						orgId = resultSet.getInt(1);
					}
				}
			}
			if (orgId <= 0) {
				return null;
			}
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F02,F04 FROM T7029 WHERE F01=?")) {
				ps.setInt(1, orgId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						Organization org = new Organization();
						org.name = resultSet.getString(1);
						org.address = resultSet.getString(2);
						return org;
					}
				}
			}

			return null;
		}
	}

	@Override
	public CreditType getCreditType(int creditId) throws Throwable {
		if (creditId <= 0) {
			return null;
		}
		try (Connection connection = getUserConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F19 FROM T6036 WHERE F01=?")) {
				ps.setInt(1, creditId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						return EnumParser.parse(CreditType.class,
								resultSet.getString(1));
					}
				}
			}
		}
		return null;
	}

	@Override
	public YxUser getYxUser(int userId) throws Throwable {
		if (serviceResource.getSession() == null
				|| !serviceResource.getSession().isAuthenticated()) {
			return null;
		}
		YxUser yxUser = null;
		try (Connection connection = getUserConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F02,F06,F07 FROM T6011 WHERE F01=?")) {
				ps.setInt(1, userId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						yxUser = new YxUser();
						yxUser.phone = resultSet.getString(1);
						yxUser.name = resultSet.getString(2);
						yxUser.identifyId = resultSet.getString(3);
					}
				}
			}
		}
		return yxUser;
	}

	@Override
	public YxJoined[] getYxJoineds(int yxID, int userId) throws Throwable {
		if (yxID <= 0) {
			return null;
		}
		List<YxJoined> joineds = null;
		try (Connection connection = getUserConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F04,F05 FROM T6043 WHERE F02=? AND F03=?")) {
				ps.setInt(1, yxID);
				ps.setInt(2, userId);
				try (ResultSet resultSet = ps.executeQuery()) {
					while (resultSet.next()) {
						if (joineds == null) {
							joineds = new ArrayList<>();
						}
						YxJoined joined = new YxJoined();
						joined.amount = resultSet.getBigDecimal(1);
						joined.time = resultSet.getTimestamp(2);
						joineds.add(joined);
					}
				}
			}
		}
		return joineds == null ? null : joineds.toArray(new YxJoined[joineds
				.size()]);
	}

	@Override
	public YxDetail getYxDeadline(int yxID) throws Throwable {
		if (yxID <= 0) {
			return null;
		}
		YxDetail detail = null;
		try (Connection connection = getUserConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F02,F09,F10,F11,F15,F16,F17,F22,F23,F12 FROM T6042 WHERE F01=?")) {
				ps.setInt(1, yxID);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						detail = new YxDetail();
						detail.title = resultSet.getString(1);
						detail.sqks = resultSet.getTimestamp(2);
						detail.sqjs = resultSet.getTimestamp(3);
						Calendar calendar = Calendar.getInstance();
						calendar.setTimeInMillis(detail.sqjs.getTime());
						calendar.add(Calendar.MONTH, resultSet.getInt(4));
						detail.lcjs = new Timestamp(calendar.getTimeInMillis());
						detail.jrfl = resultSet.getBigDecimal(5);
						detail.fwfl = resultSet.getBigDecimal(6);
						detail.tcfl = resultSet.getBigDecimal(7);
						detail.low = resultSet.getBigDecimal(8);
						detail.upper = resultSet.getBigDecimal(9);
						detail.mesj = resultSet.getTimestamp(10);
					}
				}
			}
		}
		return detail;
	}

	@Override
	public ZqzrUser getZcz(int id) throws Throwable {
		if (id <= 0) {
			return null;
		}
		int accountId = 0;
		ZqzrUser zqzrUser = new ZqzrUser();
		try (Connection connection = getUserConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT T6039.F04 FROM T6039,T6040 WHERE T6039.F01=T6040.F02 AND T6040.F01=?")) {
				ps.setInt(1, id);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						accountId = resultSet.getInt(1);
					}
				}
			}
			if (accountId <= 0) {
				return null;
			}
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F02 FROM T6010 WHERE F01=?")) {
				ps.setInt(1, accountId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						zqzrUser.account = resultSet.getString(1);
					}
				}
			}
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F06,F07 FROM T6011 WHERE F01=?")) {
				ps.setInt(1, accountId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						zqzrUser.name = resultSet.getString(1);
						zqzrUser.identifyId = resultSet.getString(2);
					}
				}
			}
		}
		return zqzrUser;
	}

	@Override
	public ZqzrUser getZrz(int id) throws Throwable {
		if (id <= 0) {
			return null;
		}
		int accountId = 0;
		ZqzrUser zqzrUser = new ZqzrUser();
		try (Connection connection = getUserConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F03 FROM T6040 WHERE F01=?")) {
				ps.setInt(1, id);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						accountId = resultSet.getInt(1);
					}
				}
			}
			if (accountId <= 0) {
				return null;
			}
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F02 FROM T6010 WHERE F01=?")) {
				ps.setInt(1, accountId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						zqzrUser.account = resultSet.getString(1);
					}
				}
			}
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F06,F07 FROM T6011 WHERE F01=?")) {
				ps.setInt(1, accountId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						zqzrUser.name = resultSet.getString(1);
						zqzrUser.identifyId = resultSet.getString(2);
					}
				}
			}
		}
		return zqzrUser;
	}

	@Override
	public ZqzrCreditDetail getZqzrCreditDetail(int id) throws Throwable {
		if (id <= 0) {
			return null;
		}
		int creditId = 0;
		try (Connection connection = getUserConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT T6039.F03 FROM T6039,T6040 WHERE T6039.F01=T6040.F02 AND T6040.F01=?")) {
				ps.setInt(1, id);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						creditId = resultSet.getInt(1);
					}
				}
			}

			if (creditId <= 0) {
				return null;
			}

			ZqzrCreditDetail creditDetail = select(
					connection,
					new ItemParser<ZqzrCreditDetail>() {

						@Override
						public ZqzrCreditDetail parse(ResultSet resultSet)
								throws SQLException {
							ZqzrCreditDetail creditDetail = null;
							if (resultSet.next()) {
								if (creditDetail == null) {
									creditDetail = new ZqzrCreditDetail();
								}
								creditDetail.bName = resultSet.getString(1);
								creditDetail.amount = resultSet.getBigDecimal(2);
								creditDetail.limitMonth = resultSet.getInt(3);
								creditDetail.monthAmount = resultSet
										.getBigDecimal(4);
								creditDetail.startDate = resultSet.getTimestamp(5);
								Calendar calendar = Calendar.getInstance();
								calendar.setTimeInMillis(creditDetail.startDate
										.getTime());
								calendar.add(Calendar.MONTH,
										creditDetail.limitMonth);
								creditDetail.endDate = new Timestamp(calendar
										.getTimeInMillis());
								creditDetail.rate = resultSet.getBigDecimal(6);
								creditDetail.id = resultSet.getInt(7);
							}
							return creditDetail;
						}
					},
					"SELECT T6011.F06,T6036.F06,T6036.F08,T6036.F13,T6036.F31,T6036.F09,T6036.F01 FROM T6036,T6011 WHERE T6036.F02=T6011.F01 AND T6036.F01=?",
					creditId);

			return creditDetail;
		}
	}

	@Override
	public ZqzrInfo getZqzrInfo(int id) throws Throwable {
		if (id <= 0) {
			return null;
		}
		try(Connection connection = getUserConnection())
		{
			return select(
					connection,
					new ItemParser<ZqzrInfo>() {

						@Override
						public ZqzrInfo parse(ResultSet resultSet)
								throws SQLException {
							ZqzrInfo zqzrInfo = null;
							if (resultSet.next()) {
								zqzrInfo = new ZqzrInfo();
								zqzrInfo.zqjz = resultSet.getBigDecimal(1)
										.multiply(resultSet.getBigDecimal(6));
								zqzrInfo.zrjk = resultSet.getBigDecimal(2)
										.multiply(resultSet.getBigDecimal(6));
								zqzrInfo.zrglf = resultSet.getBigDecimal(3);
								zqzrInfo.zrsj = resultSet.getTimestamp(4);
								zqzrInfo.syqs = resultSet.getInt(5);
							}
							return zqzrInfo;
						}
					},
					"SELECT T6039.F05,T6039.F06,T6040.F06,T6040.F05,T6036.F24,T6040.F04 FROM T6039,T6040,T6036 WHERE T6039.F01=T6040.F02 AND T6039.F03=T6036.F01 AND T6040.F01=?",
					id);
		}
	}
}
