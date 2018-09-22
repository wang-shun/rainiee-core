package com.dimeng.p2p.modules.finance.console.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.p2p.S60.enums.T6041_F12;
import com.dimeng.p2p.common.enums.AttestationState;
import com.dimeng.p2p.common.enums.AttestationType;
import com.dimeng.p2p.common.enums.CreditLevel;
import com.dimeng.p2p.common.enums.CreditStatus;
import com.dimeng.p2p.common.enums.CreditType;
import com.dimeng.p2p.common.enums.IsHovestatus;
import com.dimeng.p2p.common.enums.MarriageStatus;
import com.dimeng.p2p.common.enums.RepaymentPeriod;
import com.dimeng.p2p.common.enums.RepaymentType;
import com.dimeng.p2p.common.enums.Sex;
import com.dimeng.p2p.modules.finance.console.service.InvestInfoManage;
import com.dimeng.p2p.modules.finance.console.service.entity.CreditFiles;
import com.dimeng.p2p.modules.finance.console.service.entity.CreditInfo;
import com.dimeng.p2p.modules.finance.console.service.entity.CreditUserInfo;
import com.dimeng.p2p.modules.finance.console.service.entity.TenderRecord;
import com.dimeng.p2p.modules.finance.console.service.entity.UserRZInfo;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;

public class InvestInfoManageImpl extends AbstractFinanceService implements
		InvestInfoManage {

	public InvestInfoManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	public static class InvestInfoManageFactory implements
			ServiceFactory<InvestInfoManage> {
		@Override
		public InvestInfoManage newInstance(ServiceResource serviceResource) {
			return new InvestInfoManageImpl(serviceResource);
		}

	}

	protected static final String SELECT_ALL_SQL = "SELECT T6036.F01, T6036.F02, T6036.F04, T6036.F05, T6036.F06, T6036.F07, T6036.F08, T6036.F09, T6036.F10, T6036.F11, T6036.F13, T6036.F17, T6036.F19, T6036.F20, T6036.F23, T6036.F24, T6036.F32, T6036.F37, T6010.F02 AS F39, T6036.F40 AS F40, T6023.F05 AS KYMONEY ,T6036.F28 AS SHTIME, T6036.F29 AS MBTIME FROM T6036 INNER JOIN T6010 ON T6036.F02 = T6010.F01 INNER JOIN T6023 ON T6036.F02 = T6023.F01";

	@Override
	public CreditUserInfo getUser(int id, CreditType creditType, String htfblx)
			throws Throwable {
		ArrayList<Object> parameters = new ArrayList<>();
		String sql = "";
		if (creditType == CreditType.SYD || (creditType == CreditType.SDRZ && "SYQYZ".equals(htfblx))) {
			sql = "SELECT t6010.F02 loginName, t6011.F17 xueli, t6011.F21 xuexiao, t6012.F02 hunyin, t6014.F09 companySize, t6014.F08 companyType, t6014.F05 companyCity, t6014.F13 zhiwei, t6014.F10 workAge, t6014.F11 shouru, t6015.F03 isHouse, t6015.F04 isHouseCredit, t6015.F06 isCar, t6015.F08 isCarCredit, t6010.F01 id, t6011.F16, t6011.F07,t6036.F40 FROM T6011 t6011, T6010 t6010, T6012 t6012, T6014 t6014, T6015 t6015, T6036 t6036 WHERE t6036.F02 = t6010.F01 AND t6010.F01 = t6011.F01 AND t6010.F01 = t6012.F01 AND t6010.F01 = t6014.F01 AND t6010.F01 = t6015.F01 AND t6036.F01 = ?";
		} else {
			sql = "SELECT t6010.F02 loginName, t6011.F17 xueli, t6011.F21 xuexiao, t6012.F02 hunyin, t6013.F12 companySize, t6013.F11 companyType, t6013.F08 companyCity, t6013.F05 zhiwei, t6013.F13 workAge, t6013.F14 shouru, t6015.F03 isHouse, t6015.F04 isHouseCredit, t6015.F06 isCar, t6015.F08 isCarCredit, t6010.F01 id, t6011.F16, t6011.F07,t6036.F40 FROM T6011 t6011, T6010 t6010, T6012 t6012, T6013 t6013, T6015 t6015, T6036 t6036 WHERE t6036.F02 = t6010.F01 AND t6010.F01 = t6011.F01 AND t6010.F01 = t6012.F01 AND t6010.F01 = t6013.F01 AND t6010.F01 = t6015.F01  AND t6036.F01 = ? ";
		}
		parameters.add(id);
		try(Connection connection = getConnection(P2PConst.DB_USER))
		{
			return select(connection,
					new ItemParser<CreditUserInfo>() {
						@Override
						public CreditUserInfo parse(ResultSet resultSet)
								throws SQLException {
							CreditUserInfo credit = null;
							while (resultSet.next()) {
								if (credit == null) {
									credit = new CreditUserInfo();
								}
								credit.userName = resultSet.getString(1);
								credit.xueli = resultSet.getString(2);
								credit.university = resultSet.getString(3);
								credit.hunyin = EnumParser.parse(
										MarriageStatus.class,
										resultSet.getString(4));
								credit.companySize = resultSet.getString(5);
								credit.companyType = resultSet.getString(6);
								credit.city = getRegion(IntegerParser
										.parse(resultSet.getString(7)));
								credit.gwzz = resultSet.getString(8);
								credit.workAge = resultSet.getString(9);
								credit.earnMoey = resultSet.getString(10);
								credit.isHouse = EnumParser.parse(
										IsHovestatus.class, resultSet.getString(11));
								credit.isHouseCredit = EnumParser.parse(
										IsHovestatus.class, resultSet.getString(12));
								credit.isCar = EnumParser.parse(IsHovestatus.class,
										resultSet.getString(13));
								credit.isCarCredit = EnumParser.parse(
										IsHovestatus.class, resultSet.getString(14));
								credit.userId = resultSet.getInt(15);
								credit.sex = EnumParser.parse(Sex.class,
										resultSet.getString(16));
								credit.card = resultSet.getString(17);
								credit.level = EnumParser.parse(CreditLevel.class,
										resultSet.getString(18));

							}
							return credit;
						}
					}, sql, parameters);
		}
	}

	/**
	 * 查询区域名称
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	private String getRegion(int id) throws SQLException {
		if (id <= 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		try (Connection connection = getConnection(P2PConst.DB_INFORMATION)) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT F06,F07,F08 FROM T5019 WHERE F01=?")) {
				ps.setInt(1, id);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						sb.append(rs.getString(1));
						sb.append(",");
						sb.append(rs.getString(2));
						sb.append(",");
						sb.append(rs.getString(3));
					}
				}
			}
		}
		return sb.toString();
	}

	@Override
	public CreditFiles getFile(int userId) throws Throwable {
		if (userId < 0) {
			return null;
		}
		String sql = "SELECT T6045.F02,T6045.F03, T6045.F07,T6045.F08,T6045.F09,T6021.F03 FROM T6021 LEFT JOIN T6045 ON T6045.F01 = T6021.F01 WHERE T6021.F01 = ?";
		CreditFiles credit = new CreditFiles();
		try (Connection conn = getConnection(P2PConst.DB_USER)) {
			try (PreparedStatement ps = conn.prepareStatement(sql);) {
				ps.setInt(1, userId);
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						credit.yqcsCount = rs.getInt(1);
						credit.yzyqCount = rs.getInt(2);
						credit.hqjkCount = rs.getInt(3);
						credit.sqjkCount = rs.getInt(4);
						credit.cgCount = rs.getInt(5);
						credit.xyedMoney = rs.getBigDecimal(6);
					}
				}
			}
			credit.jkzeMoney = selectBigDecimal(conn,
					"SELECT F02 FROM T6029 WHERE F01=?", userId);
			credit.dhbxMoney = selectBigDecimal(conn,
					"SELECT SUM(F05+F06) FROM T6041 WHERE F12=? AND F03=?",
					T6041_F12.WH, userId);
			credit.yqjeMoney = selectBigDecimal(
					conn,
					"SELECT SUM(F05+F06+F07+F08+F09) FROM T6041 WHERE DATEDIFF(?,F10)>0 AND F03=? AND F12=?",
					getCurrentDate(conn),userId, T6041_F12.WH);
		}
		return credit;

	}

	@Override
	public TenderRecord[] getRecord(int id) throws Throwable {
		String sql = "SELECT T6037.F04 MONEY, T6037.F05 TIME, T6010.F02 USERNAME FROM T6036 T6036, T6037 T6037, T6010 T6010 WHERE T6036.F01 = ? AND T6037.F02 = T6036.F01 AND T6037.F03 = T6010.F01";
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(id);
		try(Connection connection= getConnection(P2PConst.DB_USER))
		{
			return selectAll(connection,
					new ArrayParser<TenderRecord>() {
						@Override
						public TenderRecord[] parse(ResultSet resultSet)
								throws SQLException {
							ArrayList<TenderRecord> list = null;
							while (resultSet.next()) {
								TenderRecord credit = new TenderRecord();
								credit.tenderMoney = resultSet.getBigDecimal(1);
								credit.tenderTime = resultSet.getTimestamp(2);
								credit.tenderName = resultSet.getString(3);
								if (list == null) {
									list = new ArrayList<>();
								}
								list.add(credit);
							}
							return list == null ? null : list
									.toArray(new TenderRecord[list.size()]);
						}
					}, sql, parameters);
		}
	}

	@Override
	public UserRZInfo[] getRZInfo(int id) throws Throwable {
		final List<UserRZInfo> userRZInfoList = new ArrayList<>();
		String sql = "SELECT T6017.F02,T6017.F04,T6018.F06 FROM T6017 INNER JOIN T6036 ON T6017.F01=T6036.F02 LEFT JOIN T6018 ON T6017.F03=T6018.F01 WHERE T6036.F01=? AND T6017.F02 != ?";
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(id);
		parameters.add(AttestationType.JBXXRZ.name());
		try(Connection connection = getConnection(P2PConst.DB_USER))
		{
			return selectAll(connection,
					new ArrayParser<UserRZInfo>() {
						@Override
						public UserRZInfo[] parse(ResultSet resultSet)
								throws SQLException {
							while (resultSet.next()) {
								UserRZInfo credit = new UserRZInfo();
								credit.type = EnumParser.parse(
										AttestationType.class,
										resultSet.getString(1));
								credit.rzStatus = EnumParser.parse(
										AttestationState.class,
										resultSet.getString(2));
								credit.rzDate = resultSet.getTimestamp(3);
								userRZInfoList.add(credit);
							}
							return userRZInfoList == null ? null : userRZInfoList
									.toArray(new UserRZInfo[userRZInfoList.size()]);
						}
					}, sql, parameters);
		}
	}

	protected static final ItemParser<CreditInfo> ITEM_PARSER = new ItemParser<CreditInfo>() {

		@Override
		public CreditInfo parse(ResultSet resultSet) throws SQLException {
			CreditInfo info = new CreditInfo();
			if (resultSet.next()) {
				info.id = resultSet.getInt(1);
				info.userId = resultSet.getInt(2);
				info.title = resultSet.getString(3);
				info.purpose = resultSet.getString(4);
				info.amount = resultSet.getBigDecimal(5);
				info.remainAmount = resultSet.getBigDecimal(6);
				info.term = resultSet.getInt(7);
				info.rate = resultSet.getDouble(8);
				info.repaymentPeriod = RepaymentPeriod.valueOf(resultSet
						.getString(9));
				info.repaymentType = EnumParser.parse(RepaymentType.class,
						resultSet.getString(10));
				info.yhkAmount = resultSet.getBigDecimal(11);
				info.goalDesc = resultSet.getString(12);
				info.creditType = CreditType.valueOf(resultSet.getString(13));
				info.status = CreditStatus.valueOf(resultSet.getString(14));
				info.nextRepayDate = resultSet.getTimestamp(15);
				info.remainTerms = resultSet.getInt(16);
				info.payedDate = resultSet.getTimestamp(17);
				info.perAmount = resultSet.getBigDecimal(18);
				info.userAccountName = resultSet.getString(19);
				info.creditLevel = EnumParser.parse(CreditLevel.class,
						resultSet.getString(20));
				info.kyMoney = resultSet.getBigDecimal(21);
				info.shTime = resultSet.getTimestamp(22);
				info.mbTime = resultSet.getTimestamp(23);
			}
			return info;
		}
	};

	@Override
	public CreditInfo get(int id) throws Throwable {
		if (id <= 0) {
			return null;
		}
		ArrayList<Object> parameters = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(SELECT_ALL_SQL);
		sql.append(" WHERE T6036.F01 = ?");
		sql.append(" AND T6036.F20 =? AND F18 = 'S'");
		parameters.add(id);
		parameters.add(CreditStatus.YMB);
		try(Connection connection = getConnection(P2PConst.DB_USER))
		{
			CreditInfo creditInfo = select(connection, ITEM_PARSER,
					sql.toString(), parameters);

			if(creditInfo != null){
				if(creditInfo.creditType == CreditType.SDRZ || creditInfo.creditType == CreditType.XYDB){
					try (PreparedStatement ps = connection
							.prepareStatement("SELECT T6036_1.F02 FROM T6036_1 WHERE T6036_1.F01 = ? ")) {
						ps.setInt(1, id);

						try (ResultSet rs = ps.executeQuery()) {
							if (rs.next()) {
								creditInfo.htfblx = rs.getString(1);
							}
						}
					}
				}
			}
			return creditInfo;
		}

	}

}
