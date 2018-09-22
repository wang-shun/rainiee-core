package com.dimeng.p2p.modules.financing.front.service.achieve;

import java.math.BigDecimal;
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
import com.dimeng.p2p.common.enums.AttestationState;
import com.dimeng.p2p.common.enums.AttestationType;
import com.dimeng.p2p.common.enums.CreditType;
import com.dimeng.p2p.common.enums.IsHovestatus;
import com.dimeng.p2p.common.enums.MarriageStatus;
import com.dimeng.p2p.common.enums.OverdueStatus;
import com.dimeng.p2p.common.enums.RepayStatus;
import com.dimeng.p2p.common.enums.Sex;
import com.dimeng.p2p.common.enums.TransferStatus;
import com.dimeng.p2p.common.enums.Zystatus;
import com.dimeng.p2p.modules.financing.front.service.InvestInfoManage;
import com.dimeng.p2p.modules.financing.front.service.entity.AlsoMoney;
import com.dimeng.p2p.modules.financing.front.service.entity.CreditHoldInfo;
import com.dimeng.p2p.modules.financing.front.service.entity.CreditOutRecode;
import com.dimeng.p2p.modules.financing.front.service.entity.CreditUserInfo;
import com.dimeng.p2p.modules.financing.front.service.entity.TenderRecord;
import com.dimeng.p2p.modules.financing.front.service.entity.UserRZInfo;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;

public class InvestInfoManageImpl extends AbstractFinancingManage implements
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

	@Override
	public CreditUserInfo getUser(int id, CreditType creditType)
			throws Throwable {
		if (id < 0) {
			return null;
		}
		ArrayList<Object> parameters = new ArrayList<>();
		String sql = "";
		try(Connection conn = getConnection(P2PConst.DB_USER))
		{
			if (creditType == CreditType.XYDB || creditType == CreditType.SDRZ) {
				try (PreparedStatement ps = conn
						.prepareStatement("SELECT F02 FROM T6036_1 WHERE F01 = ?")) {
					ps.setInt(1, id);
					try (ResultSet rs = ps.executeQuery()) {
						if (rs.next()) {
							Zystatus status = EnumParser.parse(Zystatus.class,
									rs.getString(1));
							if (status == Zystatus.SYQYZ) {
								creditType = CreditType.SYD;
							}
						}
					}
				}
			}

			if (creditType == CreditType.SYD) {
				sql = "SELECT t6010.F02 loginName, t6011.F17 xueli, t6011.F21 xuexiao, t6012.F02 hunyin, t6014.F09 companySize, t6014.F08 companyType, t6014.F05 companyCity, t6014.F13 zhiwei, t6014.F10 workAge, t6014.F11 shouru, t6015.F03 isHouse, t6015.F04 isHouseCredit, t6015.F06 isCar, t6015.F08 isCarCredit, t6010.F01 id, t6011.F16, t6011.F07 FROM T6011 t6011, T6010 t6010, T6012 t6012, T6014 t6014, T6015 t6015, T6036 t6036 WHERE t6036.F02 = t6010.F01 AND t6010.F01 = t6011.F01 AND t6010.F01 = t6012.F01 AND t6010.F01 = t6014.F01 AND t6010.F01 = t6015.F01 AND t6036.F01 = ?";
			} else {
				sql = "SELECT t6010.F02 loginName, t6011.F17 xueli, t6011.F21 xuexiao, t6012.F02 hunyin, t6013.F12 companySize, t6013.F11 companyType, t6013.F08 companyCity, t6013.F05 zhiwei, t6013.F13 workAge, t6013.F14 shouru, t6015.F03 isHouse, t6015.F04 isHouseCredit, t6015.F06 isCar, t6015.F08 isCarCredit, t6010.F01 id, t6011.F16, t6011.F07 FROM T6011 t6011, T6010 t6010, T6012 t6012, T6013 t6013, T6015 t6015, T6036 t6036 WHERE t6036.F02 = t6010.F01 AND t6010.F01 = t6011.F01 AND t6010.F01 = t6012.F01 AND t6010.F01 = t6013.F01 AND t6010.F01 = t6015.F01  AND t6036.F01 = ? ";
			}

			parameters.add(id);
			return select(conn,
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
								credit.city = getRegion(IntegerParser.parse(resultSet.getString(7)));
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
	public TenderRecord[] getRecord(int id) throws Throwable {
		String sql = "SELECT t6037.F04 money, t6037.F05 time, t6010.F02 userName FROM T6036 t6036, T6037 t6037, T6010 t6010 WHERE t6036.F01 = ? AND t6037.F02 = t6036.F01 AND t6037.F03 = t6010.F01";
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(id);
		try(Connection connection = getConnection(P2PConst.DB_USER))
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
	public AlsoMoney[] getAlso(int id) throws Throwable {
		String sql = "SELECT t6041.F10 yinhuantime, t6041.F12 huanstatus, t6041.F05 huanbenjin, t6041.F07 huanfaxi, t6041.F11 shijitime, t6041.F06 huanlixi FROM T6041 t6041, T6036 t6036 WHERE t6036.F01 = ? AND t6041.F02 = t6036.F01";
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(id);
		try(Connection connection = getConnection(P2PConst.DB_USER))
		{
			return selectAll(connection,
					new ArrayParser<AlsoMoney>() {
						@Override
						public AlsoMoney[] parse(ResultSet resultSet)
								throws SQLException {
							ArrayList<AlsoMoney> list = null;
							while (resultSet.next()) {
								AlsoMoney credit = new AlsoMoney();
								credit.hyhkrq = resultSet.getTimestamp(1);
								credit.status = EnumParser.parse(RepayStatus.class,
										resultSet.getString(2));
								credit.yhbj = resultSet.getBigDecimal(3);
								credit.yffx = resultSet.getBigDecimal(4);
								credit.sjhkTime = resultSet.getTimestamp(5);
								credit.yhlx = resultSet.getBigDecimal(6);
								credit.yhbx = credit.yhbj.add(credit.yhlx);
								if (list == null) {
									list = new ArrayList<>();
								}
								list.add(credit);
							}
							return list == null ? null : list
									.toArray(new AlsoMoney[list.size()]);
						}
					}, sql, parameters);
		}
	}

	@Override
	public CreditHoldInfo[] getHoldInfo(int id) throws Throwable {
		String sql = "SELECT T6010.F02 userName, T6038.F04 touzje, T6036.F37 mfje, T6038.F08 FROM T6038, T6010, T6036 WHERE T6036.F01 = ? AND T6036.F01 = T6038.F02 AND T6038.F03 = T6010.F01 AND T6038.F08 > 0 ORDER BY T6038.F01 DESC";
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(id);
		try(Connection connection = getConnection(P2PConst.DB_USER))
		{
			return selectAll(connection,
					new ArrayParser<CreditHoldInfo>() {
						@Override
						public CreditHoldInfo[] parse(ResultSet resultSet)
								throws SQLException {
							ArrayList<CreditHoldInfo> list = null;
							while (resultSet.next()) {
								CreditHoldInfo credit = new CreditHoldInfo();
								credit.userName = resultSet.getString(1);
								credit.touzje = resultSet.getBigDecimal(2);
								credit.mfje = resultSet.getBigDecimal(3);
								credit.cyfs = resultSet.getInt(4);
								if (list == null) {
									list = new ArrayList<>();
								}
								list.add(credit);
							}
							return list == null ? null : list
									.toArray(new CreditHoldInfo[list.size()]);
						}
					}, sql, parameters);
		}
	}

	@Override
	public CreditOutRecode[] getOutRecode(int id) throws Throwable {
		String sql = "SELECT ( SELECT T6010.F02 FROM T6010 WHERE T6010.F01 = T6039.F04 ) zhuanchuName, T6010.F02 zhuanruName, T6040.F04 , T6039.F06 , T6040.F05 FROM T6040, T6039, T6010 WHERE T6039.F03 = ? AND T6040.F02 = T6039.F01 AND T6040.F03 = T6010.F01 ORDER BY T6040.F05 DESC";
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(id);
		try(Connection connection = getConnection(P2PConst.DB_USER))
		{
			return selectAll(connection,
					new ArrayParser<CreditOutRecode>() {
						@Override
						public CreditOutRecode[] parse(ResultSet resultSet)
								throws SQLException {
							ArrayList<CreditOutRecode> list = null;
							while (resultSet.next()) {
								CreditOutRecode credit = new CreditOutRecode();
								credit.creditSell = resultSet.getString(1);
								credit.creditBuy = resultSet.getString(2);
								credit.jyfs = resultSet.getInt(3);
								credit.jydj = resultSet.getBigDecimal(4);
								credit.sellMoney = new BigDecimal(credit.jyfs)
										.multiply(credit.jydj);
								credit.sellTime = resultSet.getTimestamp(5);
								if (list == null) {
									list = new ArrayList<>();
								}
								list.add(credit);
							}
							return list == null ? null : list
									.toArray(new CreditOutRecode[list.size()]);
						}
					}, sql, parameters);
		}
	}

	@Override
	public UserRZInfo[] getRZInfo(int id) throws Throwable {
		final List<UserRZInfo> userRZInfoList = new ArrayList<>();
		String sql = "SELECT T6017.F02, T6017.F04, T6018.F06 FROM T6017 INNER JOIN T6036 ON T6017.F01 = T6036.F02 LEFT JOIN T6018 ON T6017.F03 = T6018.F01 WHERE T6036.F01 = ? AND T6017.F04 = ? AND T6017.F02 <> ?";
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(id);
		parameters.add(AttestationState.YYZ);
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
								credit.attestationType = EnumParser.parse(
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

	@Override
	public BigDecimal getDjyMoney(int id) throws Throwable {
		if (id < 0) {
			return null;
		}
		BigDecimal djyMoney = new BigDecimal(0);
		try (Connection conn = getConnection(P2PConst.DB_USER)) {
			try (PreparedStatement ps = conn
					.prepareStatement("SELECT IFNULL(SUM(T6039.F08*T6039.F06),0) FROM T6039 WHERE F03 = ? AND F11 = ? AND F12 = ?");) {
				ps.setInt(1, id);
				ps.setString(2, TransferStatus.YX.name());
				ps.setString(3, OverdueStatus.S.name());
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						djyMoney = rs.getBigDecimal(1);
					}
				}
			}
		}
		return djyMoney;
	}

	@Override
	public BigDecimal getyjyMoney(int id) throws Throwable {
		if (id < 0) {
			return null;
		}
		BigDecimal yjyMoney = new BigDecimal(0);
		try (Connection conn = getConnection(P2PConst.DB_USER)) {
			try (PreparedStatement ps = conn
					.prepareStatement("SELECT IFNULL(SUM(T6040.F04*T6039.F06),0) FROM T6039, T6040 WHERE T6039.F03 = ? AND T6039.F01 = T6040.F02");) {
				ps.setInt(1, id);
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						yjyMoney = rs.getBigDecimal(1);
					}
				}
			}
		}
		return yjyMoney;
	}

}
