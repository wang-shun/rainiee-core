package com.dimeng.p2p.modules.financing.front.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S60.enums.T6036_1_F04;
import com.dimeng.p2p.S60.enums.T6056_F10;
import com.dimeng.p2p.common.enums.CreditLevel;
import com.dimeng.p2p.common.enums.CreditStatus;
import com.dimeng.p2p.common.enums.CreditTerm;
import com.dimeng.p2p.common.enums.CreditType;
import com.dimeng.p2p.common.enums.InvestType;
import com.dimeng.p2p.common.enums.OverdueStatus;
import com.dimeng.p2p.common.enums.RepaymentPeriod;
import com.dimeng.p2p.common.enums.RepaymentType;
import com.dimeng.p2p.modules.financing.front.service.InvestManage;
import com.dimeng.p2p.modules.financing.front.service.entity.CreditInfo;
import com.dimeng.p2p.modules.financing.front.service.entity.InvestStatistics;
import com.dimeng.p2p.modules.financing.front.service.query.InvestQuery;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.util.parser.EnumParser;

public class InvestmentManageImpl extends AbstractFinancingManage implements
		InvestManage {

	public InvestmentManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	public static class InvestmentManageFactory implements
			ServiceFactory<InvestManage> {
		@Override
		public InvestManage newInstance(ServiceResource serviceResource) {
			return new InvestmentManageImpl(serviceResource);
		}

	}

	protected static final String SELECT_ALL_SQL = "SELECT T6036.F01, T6036.F02, T6036.F04, T6036.F05, T6036.F06, T6036.F07, T6036.F08, T6036.F09, T6036.F10, T6036.F11, T6036.F13, T6036.F17, T6036.F19, T6036.F20, T6036.F23, T6036.F24, T6036.F32, T6036.F37, T6010.F02 AS F39, T6036.F40 AS F40, T6023.F05 AS kyMoney ,T6036.F28 AS shTime, T6036.F29 AS mbTime,ADDDATE(T6036.F28,INTERVAL T6036.F12 DAY) AS jsTime,T6036.F35,T6036_1.F03 as fmtp,T6036_1.F04 as sftj,IFNULL(datediff(curdate(), ADDDATE(T6036.F28,INTERVAL T6036.F12 DAY)),0) as sysj FROM T6036 INNER JOIN T6010 ON T6036.F02 = T6010.F01 INNER JOIN T6023 ON T6036.F02 = T6023.F01 LEFT JOIN T6036_1 ON T6036.F01=T6036_1.F01";

	@Override
	public PagingResult<CreditInfo> search(InvestQuery query, Paging paging)
			throws Throwable {
		ArrayList<Object> parameters = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder(SELECT_ALL_SQL);
		sql.append(" WHERE (T6036.F20 = ? OR T6036.F20 = ? OR T6036.F20 = ?) AND F18 = ?");
		parameters.add(CreditStatus.TBZ);
		parameters.add(CreditStatus.YMB);
		parameters.add(CreditStatus.YFK);
		parameters.add(OverdueStatus.S);
		if (query != null) {
			boolean notFirst = false;
			InvestType[] investTypes = query.getType();
			if (investTypes != null && investTypes.length > 0) {
				Set<CreditType> types = new LinkedHashSet<>();
				for (InvestType investType : investTypes) {
					if (investType == null) {
						continue;
					}
					switch (investType) {
					case XYRZB: {
						types.add(CreditType.SYD);
						types.add(CreditType.XJD);
						break;
					}
					case JGDBB: {
						types.add(CreditType.XYDB);
						break;
					}
					case SDRZB: {
						types.add(CreditType.SDRZ);
						break;
					}
					}
				}
				if (types.size() > 0) {
					notFirst = false;
					sql.append(" AND (");
					for (CreditType type : types) {
						if (notFirst) {
							sql.append(" OR ");
						} else {
							notFirst = true;
						}
						sql.append("T6036.F19 = ?");
						parameters.add(type);
					}
					sql.append(")");

				}
			}

			CreditLevel[] levels = query.getCreditLevel();
			if (levels != null && levels.length > 0) {
				Set<CreditLevel> valieLevels = new LinkedHashSet<>();
				for (CreditLevel level : levels) {
					if (level == null) {
						continue;
					}
					valieLevels.add(level);
				}
				if (valieLevels.size() > 0) {
					notFirst = false;
					sql.append(" AND (");
					for (CreditLevel valieLevel : valieLevels) {

						if (notFirst) {
							sql.append(" OR ");
						} else {
							notFirst = true;
						}
						sql.append("T6036.F40 = ?");
						parameters.add(valieLevel);
					}
					sql.append(")");
				}
			}
			CreditTerm[] terms = query.getTerm();
			if (terms != null && terms.length > 0) {
				Set<CreditTerm> validTerms = new LinkedHashSet<>();
				for (CreditTerm term : terms) {
					if (term == null) {
						continue;
					}
					validTerms.add(term);
				}
				if (validTerms.size() > 0) {
					notFirst = false;
					sql.append(" AND (");
					for (CreditTerm term : validTerms) {
						if (notFirst) {
							sql.append(" OR ");
						} else {
							notFirst = true;
						}
						switch (term) {
						case SGYYX: {
							sql.append(" T6230.F09 < 3 ");
							break;
						}
						case SDLGY: {
							sql.append("(T6230.F09 >= 3 AND T6230.F09 <= 6)");
							break;
						}
						case LDJGY: {
							sql.append("(T6230.F09 >= 6 AND T6230.F09 <= 9)");
							break;
						}
						case JDSEGY: {
							sql.append("(T6230.F09 >= 9 AND T6230.F09 <= 12)");
							break;
						}
						case SEGYYS: {
							sql.append(" T6230.F09 > 12 ");
							break;
						}
						default:
							break;
						}
					}
					sql.append(")");

				}
			}
		}
		sql.append("  ORDER BY F20 DESC , F28 DESC");
		try(Connection connection = getConnection(P2PConst.DB_USER))
		{
			return selectPaging(connection,
					new ArrayParser<CreditInfo>() {
						public CreditInfo[] parse(ResultSet resultSet)
								throws SQLException {
							ArrayList<CreditInfo> list = null;
							while (resultSet.next()) {
								CreditInfo info = new CreditInfo();
								info.id = resultSet.getInt(1);
								info.userId = resultSet.getInt(2);
								info.title = resultSet.getString(3);
								info.purpose = resultSet.getString(4);
								info.amount = resultSet.getBigDecimal(5);
								info.remainAmount = resultSet.getBigDecimal(6);
								info.term = resultSet.getInt(7);
								info.rate = resultSet.getDouble(8);
								info.repaymentPeriod = EnumParser.parse(
										RepaymentPeriod.class,
										resultSet.getString(9));
								info.repaymentType = EnumParser.parse(
										RepaymentType.class,
										resultSet.getString(10));
								info.yhkAmount = resultSet.getBigDecimal(11);
								info.goalDesc = resultSet.getString(12);
								info.creditType = EnumParser.parse(
										CreditType.class, resultSet.getString(13));
								info.status = EnumParser.parse(CreditStatus.class,
										resultSet.getString(14));
								info.nextRepayDate = resultSet.getDate(15);
								info.remainTerms = resultSet.getInt(16);
								info.payedDate = resultSet.getTimestamp(17);
								info.perAmount = resultSet.getBigDecimal(18);
								info.userAccountName = resultSet.getString(19);
								info.creditLevel = EnumParser.parse(
										CreditLevel.class, resultSet.getString(20));
								info.kyMoney = resultSet.getBigDecimal(21);
								info.shTime = resultSet.getTimestamp(22);
								info.mbTime = resultSet.getTimestamp(23);
								info.jsTime = resultSet.getTimestamp(24);
								info.dfTime = resultSet.getTimestamp(25);
								info.fmtp = resultSet.getString(26);
								info.sftj = EnumParser.parse(
										T6036_1_F04.class, resultSet.getString(27));
								int sysj = resultSet.getInt(28);
								if(sysj < 0){
									sysj = 0;
								}
								info.sysj = sysj;
								info.progress = (info.amount.doubleValue() - info.remainAmount
										.doubleValue()) / info.amount.doubleValue();

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

	@Override
	public InvestStatistics getStatistics() throws Throwable {
		InvestStatistics statistics = new InvestStatistics();
		String sql = "SELECT IFNULL(SUM(F06),0),COUNT(*) FROM T6036 WHERE F20 IN (?,?,?)";
		try (Connection conn = getConnection(P2PConst.DB_USER)) {
			try (PreparedStatement ps = conn.prepareStatement(sql);) {
				ps.setString(1, CreditStatus.YFK.name());
				ps.setString(2, CreditStatus.YJQ.name());
				ps.setString(3, CreditStatus.YDF.name());
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						statistics.totleMoney = rs.getBigDecimal(1);
						statistics.totleCount = rs.getLong(2);
					}
				}
			}
		}

		statistics.userEarnMoney = getEarnMoney();
		return statistics;
	}

	private BigDecimal getEarnMoney() throws Throwable {
		BigDecimal wyj = new BigDecimal(0);
		try (Connection connection = getConnection(P2PConst.DB_USER)) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT SUM(F09) FROM T6038")) {
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						wyj = rs.getBigDecimal(1);
					}
				}
			}

			BigDecimal yslx = new BigDecimal(0);
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT SUM(F06+F07) FROM T6056 WHERE F10=? AND F14=?")) {
				ps.setString(1, T6056_F10.YS.name());
				ps.setString(2, "F");
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						yslx = rs.getBigDecimal(1);
					}
				}
			}
			return wyj.add(yslx);
		}
	}

	@Override
	public CreditInfo get(int id) throws Throwable {
		if (id <= 0) {
			return null;
		}
		ArrayList<Object> parameters = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer(SELECT_ALL_SQL);
		sql.append(" WHERE T6036.F01 = ?");
		sql.append(" AND T6036.F20 <> ? AND T6036.F20 <>  ?");
		parameters.add(id);
		parameters.add(CreditStatus.SQZ);
		parameters.add(CreditStatus.DSH);
		try(Connection connection = getConnection(P2PConst.DB_USER))
		{
			return select(connection,
					new ItemParser<CreditInfo>() {

						@Override
						public CreditInfo parse(ResultSet resultSet)
								throws SQLException {
							CreditInfo info = null;
							if (resultSet.next()) {
								info = new CreditInfo();
								info.id = resultSet.getInt(1);
								info.userId = resultSet.getInt(2);
								info.title = resultSet.getString(3);
								info.purpose = resultSet.getString(4);
								info.amount = resultSet.getBigDecimal(5);
								info.remainAmount = resultSet.getBigDecimal(6);
								info.term = resultSet.getInt(7);
								info.rate = resultSet.getDouble(8);
								info.repaymentPeriod = EnumParser.parse(
										RepaymentPeriod.class,
										resultSet.getString(9));
								info.repaymentType = EnumParser.parse(
										RepaymentType.class,
										resultSet.getString(10));
								info.yhkAmount = resultSet.getBigDecimal(11);
								info.goalDesc = resultSet.getString(12);
								info.creditType = EnumParser.parse(
										CreditType.class, resultSet.getString(13));
								info.status = EnumParser.parse(CreditStatus.class,
										resultSet.getString(14));
								info.nextRepayDate = resultSet.getDate(15);
								info.remainTerms = resultSet.getInt(16);
								info.payedDate = resultSet.getTimestamp(17);
								info.perAmount = resultSet.getBigDecimal(18);
								info.userAccountName = resultSet.getString(19);
								info.kyMoney = resultSet.getBigDecimal(21);
								info.shTime = resultSet.getTimestamp(22);
								info.mbTime = resultSet.getTimestamp(23);
								info.jsTime = resultSet.getTimestamp(24);
								info.dfTime = resultSet.getTimestamp(25);
								info.progress = (info.amount.doubleValue() - info.remainAmount
										.doubleValue()) / info.amount.doubleValue();
								info.creditLevel = EnumParser.parse(
										CreditLevel.class, resultSet.getString(20));
							}
							return info;
						}
					}, sql.toString(), parameters);
		}
	}

	@Override
	public String getJgName(int jkbId) throws Throwable {
		String sql = "SELECT T7029.F02 FROM T6036 INNER JOIN S70.T7031 ON T6036.F25 = T7031.F01 INNER JOIN S70.T7029 ON T7031.F02 = T7029.F01 WHERE T6036.F01 = ?";
		try (Connection conn = getConnection(P2PConst.DB_USER)) {
			try (PreparedStatement ps = conn.prepareStatement(sql);) {
				ps.setInt(1, jkbId);
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						return rs.getString(1);
					}
				}
			}
		}
		return null;
	}
}
