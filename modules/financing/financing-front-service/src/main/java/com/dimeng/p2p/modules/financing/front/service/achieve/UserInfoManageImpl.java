package com.dimeng.p2p.modules.financing.front.service.achieve;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.enums.CreditStatus;
import com.dimeng.p2p.common.enums.CreditType;
import com.dimeng.p2p.common.enums.OverdueStatus;
import com.dimeng.p2p.common.enums.PlanState;
import com.dimeng.p2p.common.enums.RepayStatus;
import com.dimeng.p2p.modules.financing.front.service.UserInfoManage;
import com.dimeng.p2p.modules.financing.front.service.entity.CreditFiles;
import com.dimeng.p2p.modules.financing.front.service.entity.CreditInfo;
import com.dimeng.p2p.modules.financing.front.service.entity.UserInfo;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;

public class UserInfoManageImpl extends AbstractFinancingManage implements
		UserInfoManage {

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
		try(Connection connection = getConnection(P2PConst.DB_USER))
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

	@Override
	public UserInfo getAgeSex(String card) throws Throwable {
		UserInfo userInfo = new UserInfo();
		if (!StringHelper.isEmpty(card)) {

			if (Integer.parseInt(card.substring(card.length() - 2,
					card.length() - 1)) % 2 != 0) {
				userInfo.sex = 1;
			} else {
				userInfo.sex = 0;
			}

			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH) + 1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			if (month > IntegerParser.parse(card.substring(10, 12))) {
				userInfo.age = year
						- IntegerParser.parse(card.substring(6, 10));
			} else if (month == IntegerParser.parse(card.substring(10, 12))
					&& day > IntegerParser.parse(card.substring(12, 14))) {
				userInfo.age = year
						- IntegerParser.parse(card.substring(6, 10));
			} else {
				userInfo.age = year
						- IntegerParser.parse(card.substring(6, 10)) - 1;
			}
		}
		return userInfo;
	}

	@Override
	public String isYuqi() throws Throwable {
		String sql = "SELECT DATEDIFF(?,F10) FROM T6041 WHERE F12=? AND F03=? AND F10 < SYSDATE()";
		try (Connection connection = getConnection(P2PConst.DB_USER)) {
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.setTimestamp(1,getCurrentTimestamp(connection));
				ps.setString(2, RepayStatus.WH.name());
				ps.setInt(3, serviceResource.getSession().getAccountId());
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						int days = rs.getInt(1);
						if (days > 0) {
							return "Y";
						}
					}
				}
			}
		}
		return "N";
	}

	@Override
	public CreditFiles getFile(int userId) throws Throwable {
		if (userId < 0) {
			return null;
		}
		String sql = "SELECT T6045.F02 yqcs, T6045.F03 yzyqcs,  T6021.F03 ,T6021.F02 FROM T6021 LEFT JOIN T6045 ON T6045.F01 = T6021.F01 WHERE T6021.F01 = ?";
		CreditFiles credit = null;
		try (Connection conn = getConnection(P2PConst.DB_USER)) {
			try (PreparedStatement ps = conn.prepareStatement(sql);) {
				ps.setInt(1, userId);
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						if (credit == null) {
							credit = new CreditFiles();
						}
						credit.yqcsCount = rs.getInt(1);
						credit.yzyqCount = rs.getInt(2);
						credit.xyedMoney = rs.getBigDecimal(3);
						// credit.creditLevel = CreditLevel.Level(rs.getInt(4),
						// serviceResource
						// .getResource(ConfigureProvider.class));
					}
				}
			}
		}
		credit.cgCount = getCreditSuccCount(userId);
		credit.dhbxMoney = getDhbx(userId);
		credit.hqjkCount = getHqCount(userId);
		credit.jkzeMoney = getJkTotle(userId);
		credit.sqjkCount = getCreditCount(userId);
		credit.yqjeMoney = getYqMoney(userId);
		return credit;

	}

	/**
	 * 逾期金额
	 * 
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	private BigDecimal getYqMoney(int userId) throws Throwable {
		if (userId < 0) {
			return null;
		}
		BigDecimal yqMoney = new BigDecimal(0);
		try (Connection conn = getConnection(P2PConst.DB_USER)) {
			try (PreparedStatement ps = conn
					.prepareStatement("SELECT IFNULL(SUM(F05+F06+F07+F08+F09),0) FROM T6041 WHERE F03 = ? AND F07 > 0 AND F12 = ?");) {
				ps.setInt(1, userId);
				ps.setString(2, RepayStatus.WH.name());
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						yqMoney = rs.getBigDecimal(1);
					}
				}
			}
		}
		return yqMoney;
	}

	/**
	 * 借款笔数
	 * 
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	private int getCreditCount(int userId) throws Throwable {
		if (userId < 0) {
			return 0;
		}
		int jkCount = 0;
		try (Connection conn = getConnection(P2PConst.DB_USER)) {
			try (PreparedStatement ps = conn
					.prepareStatement("SELECT COUNT(*) FROM T6036 WHERE T6036.F02 = ?");) {
				ps.setInt(1, userId);
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						jkCount = rs.getInt(1);
					}
				}
			}
		}
		return jkCount;
	}

	/**
	 * 借款成功笔数
	 * 
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	private int getCreditSuccCount(int userId) throws Throwable {
		if (userId < 0) {
			return 0;
		}
		int succCount = 0;
		try (Connection conn = getConnection(P2PConst.DB_USER)) {
			try (PreparedStatement ps = conn
					.prepareStatement("SELECT COUNT(*) FROM T6036 WHERE T6036.F02 = ? AND (T6036.F20 =? OR T6036.F20 =? OR T6036.F20 = ?)");) {
				ps.setInt(1, userId);
				ps.setString(2, CreditStatus.YDF.name());
				ps.setString(3, CreditStatus.YFK.name());
				ps.setString(4, CreditStatus.YJQ.name());
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						succCount = rs.getInt(1);
					}
				}
			}
		}
		return succCount;
	}

	/**
	 * 还清笔数
	 * 
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	private int getHqCount(int userId) throws Throwable {
		if (userId < 0) {
			return 0;
		}
		int hqCount = 0;
		try (Connection conn = getConnection(P2PConst.DB_USER)) {
			try (PreparedStatement ps = conn
					.prepareStatement("SELECT COUNT(*) FROM T6036 WHERE T6036.F02 = ? AND T6036.F20 = ?");) {
				ps.setInt(1, userId);
				ps.setString(2, CreditStatus.YJQ.name());
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						hqCount = rs.getInt(1);
					}
				}
			}
		}
		return hqCount;
	}

	/**
	 * 未还清笔数
	 * 
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	private int getWhqCount(int userId) throws Throwable {
		if (userId < 0) {
			return 0;
		}
		int whqCount = 0;
		try (Connection conn = getConnection(P2PConst.DB_USER)) {
			try (PreparedStatement ps = conn
					.prepareStatement("SELECT COUNT(*) FROM T6036 WHERE T6036.F02 = ? AND T6036.F20 IN (?,?)");) {
				ps.setInt(1, userId);
				ps.setString(2, CreditStatus.YDF.name());
				ps.setString(3, CreditStatus.YFK.name());
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						whqCount = rs.getInt(1);
					}
				}
			}
		}
		return whqCount;
	}

	/**
	 * 借款总额
	 * 
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	private BigDecimal getJkTotle(int userId) throws Throwable {
		if (userId < 0) {
			return null;
		}
		BigDecimal jkTotle = new BigDecimal(0);
		try (Connection conn = getConnection(P2PConst.DB_USER)) {
			try (PreparedStatement ps = conn
					.prepareStatement("SELECT IFNULL(SUM(F06),0) FROM T6036 WHERE T6036.F02 = ? AND T6036.F20 IN (?,?,?)");) {
				ps.setInt(1, userId);
				ps.setString(2, CreditStatus.YDF.name());
				ps.setString(3, CreditStatus.YFK.name());
				ps.setString(4, CreditStatus.YJQ.name());
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						jkTotle = rs.getBigDecimal(1);
					}
				}
			}
		}
		return jkTotle;
	}

	/**
	 * 待还本息
	 * 
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	private BigDecimal getDhbx(int userId) throws Throwable {
		if (userId < 0) {
			return null;
		}
		BigDecimal dhTotle = new BigDecimal(0);
		try (Connection conn = getConnection(P2PConst.DB_USER)) {
			try (PreparedStatement ps = conn
					.prepareStatement("SELECT SUM(IFNULL(T6041.F05 + T6041.F06,0)) FROM T6041 WHERE T6041.F03 = ? AND T6041.F12 = ?");) {
				ps.setInt(1, userId);
				ps.setString(2, RepayStatus.WH.name());
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						dhTotle = rs.getBigDecimal(1);
					}
				}
			}
		}
		return dhTotle;
	}

	@Override
	public UserInfo search(int userId) throws Throwable {
		if (userId < 0) {
			return null;
		}
		UserInfo userInfo = new UserInfo();
		String sql = "SELECT T6010.F01, T6010.F02, T6011.F12, T6011.F29 FROM T6010, T6011 WHERE T6010.F01 = ? AND T6010.F01 = T6011.F01";
		try (Connection connection = getConnection(P2PConst.DB_USER)) {
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.setInt(1, userId);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						userInfo.loginId = rs.getInt(1);
						userInfo.loginName = rs.getString(2);
						userInfo.zcsj = rs.getTimestamp(3);
						userInfo.imgPath = rs.getString(4);
					}
				}
			}

			userInfo.cyzqsl = cyzqsl(userId);
			userInfo.cylcjhsl = cylcjhsl(userId);
			userInfo.fbjksl = getCreditCount(userId);
			userInfo.cgjksl = getCreditSuccCount(userId);
			userInfo.whqjksl = getWhqCount(userId);
			userInfo.yqje = getYqMoney(userId);
			String sql1 = "SELECT T6045.F02 yqcs, T6045.F03 yzyqcs,  T6021.F02 FROM T6021 LEFT JOIN T6045 ON T6045.F01 = T6021.F01 WHERE T6021.F01 = ?";
			try (PreparedStatement ps1 = connection.prepareStatement(sql1)) {
				ps1.setInt(1, userId);
				try (ResultSet rs = ps1.executeQuery();) {
					if (rs.next()) {
						userInfo.yqcs = rs.getInt(1);
						userInfo.yzyqcs = rs.getInt(2);
						// userInfo.creditLevel =
						// CreditLevel.Level(rs.getInt(3),
						// serviceResource
						// .getResource(ConfigureProvider.class));
					}
				}
			}

			return userInfo;
		}
	}

	/**
	 * 查询持有债权数量
	 * 
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	private int cyzqsl(int userId) throws Throwable {
		String sql = "SELECT COUNT(*) FROM T6036 , T6038 WHERE T6036.F01 = T6038.F02 AND T6038.F03 = ? AND  T6036.F20 IN (?,?,?)";
		try (Connection connection = getConnection(P2PConst.DB_USER)) {
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.setInt(1, userId);
				ps.setString(2, CreditStatus.TBZ.name());
				ps.setString(3, CreditStatus.YMB.name());
				ps.setString(4, CreditStatus.YFK.name());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getInt(1);
					}
				}
			}
		}
		return 0;
	}

	/**
	 * 查询持有优选理财数量
	 * 
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	private int cylcjhsl(int userId) throws Throwable {
		String sql = "SELECT COUNT(*) FROM T6042 , T6043 WHERE T6042.F01 = T6043.F02 AND T6043.F03 = ? AND  T6042.F07 = ?";
		try (Connection connection = getConnection(P2PConst.DB_USER)) {
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.setInt(1, userId);
				ps.setString(2, PlanState.YSX.name());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						return rs.getInt(1);
					}
				}
			}
		}
		return 0;
	}

	@Override
	public PagingResult<CreditInfo> searchCredit(int userId, Paging paging)
			throws Throwable {
		String sql = "SELECT T6036.F19, T6036.F01, T6036.F02, T6036.F04, T6036.F06, T6036.F08, T6036.F09, T6036.F20, T6036.F28, T6036.F39 FROM T6036 WHERE T6036.F02 = ? AND T6036.F20 IN ( ?, ?, ?, ?, ? ) ORDER BY T6036.F20 DESC,T6036.F28 DESC";
		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(userId);
		parameters.add(CreditStatus.TBZ);
		parameters.add(CreditStatus.YDF);
		parameters.add(CreditStatus.YFK);
		parameters.add(CreditStatus.YMB);
		parameters.add(CreditStatus.YJQ);
		try(Connection connection = getConnection(P2PConst.DB_USER))
		{
			return selectPaging(connection,
					new ArrayParser<CreditInfo>() {
						@Override
						public CreditInfo[] parse(ResultSet resultSet)
								throws SQLException {
							ArrayList<CreditInfo> list = null;
							while (resultSet.next()) {
								CreditInfo info = new CreditInfo();
								info.creditType = EnumParser.parse(
										CreditType.class, resultSet.getString(1));
								info.id = resultSet.getInt(2);
								info.userId = resultSet.getInt(3);
								info.title = resultSet.getString(4);
								info.amount = resultSet.getBigDecimal(5);
								info.term = resultSet.getInt(6);
								info.rate = resultSet.getDouble(7);
								info.status = EnumParser.parse(CreditStatus.class,
										resultSet.getString(8));
								info.shTime = resultSet.getTimestamp(9);
								info.overdueStatus = EnumParser.parse(
										OverdueStatus.class,
										resultSet.getString(10));
								if (list == null) {
									list = new ArrayList<CreditInfo>();
								}
								list.add(info);
							}
							return list == null || list.size() == 0 ? null : list
									.toArray(new CreditInfo[list.size()]);
						}
					}, paging, sql.toString(), parameters);
		}
	}

	protected Timestamp getCurrentTimestamp(Connection connection)
			throws Throwable
	{
		try (PreparedStatement pstmt = connection.prepareStatement("SELECT CURRENT_TIMESTAMP()"))
		{
			try (ResultSet resultSet = pstmt.executeQuery())
			{
				if (resultSet.next())
				{
					return resultSet.getTimestamp(1);
				}
			}
		}
		return null;
	}

}
