package com.dimeng.p2p.modules.finance.console.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
import com.dimeng.p2p.S60.enums.T6036_F39;
import com.dimeng.p2p.S60.enums.T6041_F12;
import com.dimeng.p2p.common.enums.CreditStatus;
import com.dimeng.p2p.common.enums.RestoreStatus;
import com.dimeng.p2p.common.enums.TradeType;
import com.dimeng.p2p.modules.finance.console.service.DfsdzqManage;
import com.dimeng.p2p.modules.finance.console.service.entity.Dfsdzq;
import com.dimeng.p2p.modules.finance.console.service.entity.DfsdzqRecord;
import com.dimeng.p2p.modules.finance.console.service.query.DfsdzqRecordQuery;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.EnumParser;

public class DfsdzqManageImpl extends AbstractFinanceService implements
		DfsdzqManage {

	public static class PlatformPaymentFactory implements
			ServiceFactory<DfsdzqManage> {

		@Override
		public DfsdzqManage newInstance(ServiceResource serviceResource) {
			return new DfsdzqManageImpl(serviceResource);
		}
	}

	public DfsdzqManageImpl(ServiceResource serviceResource) {
		super(serviceResource);

	}

	public Dfsdzq getPlatformPaymentInfo(int advance) throws Throwable {
		Dfsdzq platformPayment = new Dfsdzq();
		String selectT7027 = "SELECT F04 FROM T7027";
		String selectT7028_1 = "SELECT SUM(F04) FROM T7028 WHERE F06=?";
		String selectT7028_2 = "SELECT SUM(F03) FROM T7028 WHERE F06=?";

		String selectT7030_1 = "SELECT SUM(F05) FROM T7030 WHERE F02=? AND F07=?";
		String selectT7030_2 = "SELECT SUM(F04) FROM T7030 WHERE F02=? AND F07=?";
		if (advance > 0) {
			try (Connection connection = getConnection()) {
				List<Integer> loanIds = new ArrayList<>();
				try (PreparedStatement ps = connection
						.prepareStatement("SELECT F09 FROM T7030 WHERE F02=? AND (F07=? OR F07=?)")) {
					ps.setInt(1, advance);
					ps.setString(2, TradeType.DF.name());
					ps.setString(3, TradeType.DFHF.name());
					try (ResultSet rs = ps.executeQuery()) {
						while (rs.next()) {
							loanIds.add(rs.getInt(1));
						}
					}
				}
				platformPayment.paymentAmount = selectBigDecimal(connection,
						selectT7030_1, advance, TradeType.DF);
				platformPayment.paymentRestore = selectBigDecimal(connection,
						selectT7030_2, advance, TradeType.DFHF);
				if (platformPayment.paymentAmount == null) {
					platformPayment.paymentAmount = new BigDecimal(0);
				}
				if (platformPayment.paymentRestore == null) {
					platformPayment.paymentRestore = new BigDecimal(0);
				}
				platformPayment.restoreAmount = getDsAmount(loanIds);
				platformPayment.profit = platformPayment.paymentRestore
						.subtract(platformPayment.paymentAmount);
			}
		} else {
			try (Connection connection = getConnection()) {
				List<Integer> loanIds = new ArrayList<>();
				try (PreparedStatement ps = connection
						.prepareStatement("SELECT F08 FROM T7028 WHERE (F06=? OR F06=?)")) {
					ps.setString(1, TradeType.DF.name());
					ps.setString(2, TradeType.DFHF.name());
					try (ResultSet rs = ps.executeQuery()) {
						while (rs.next()) {
							loanIds.add(rs.getInt(1));
						}
					}
				}
				platformPayment.profit = selectBigDecimal(connection,
						selectT7027);
				platformPayment.paymentAmount = selectBigDecimal(connection,
						selectT7028_1, TradeType.DF);
				platformPayment.paymentRestore = selectBigDecimal(connection,
						selectT7028_2, TradeType.DFHF);
				if (platformPayment.paymentAmount == null) {
					platformPayment.paymentAmount = new BigDecimal(0);
				}
				if (platformPayment.paymentRestore == null) {
					platformPayment.paymentRestore = new BigDecimal(0);
				}
				platformPayment.restoreAmount = getDsAmount(loanIds);
				platformPayment.profit = platformPayment.paymentRestore
						.subtract(platformPayment.paymentAmount);
			}
		}
		return platformPayment;
	}

	private BigDecimal getDsAmount(List<Integer> loanIds) throws SQLException {
		if (loanIds.size() <= 0) {
			return new BigDecimal(0);
		}
		List<Object> parameters = new ArrayList<>();
		StringBuilder sql = new StringBuilder(
				"SELECT SUM(F05+F06+F07) FROM T6041 WHERE F12=? AND F13=?");
		parameters.add(T6041_F12.WH.name());
		parameters.add("F");

		StringBuilder sb = new StringBuilder();
		sql.append(" AND F02 IN(");
		for (Integer loanId : loanIds) {
			if (loanId <= 0) {
				continue;
			}
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(loanId);
		}
		sql.append(sb.toString());
		sql.append(")");
		try(Connection connection = getConnection(P2PConst.DB_USER))
		{
			return selectBigDecimal(connection,
					sql.toString(), parameters);
		}

	}

	public PagingResult<DfsdzqRecord> search(int advance,
			DfsdzqRecordQuery query, Paging paging) throws Throwable {
		ArrayList<Object> parameters = new ArrayList<>();
		String dbName = P2PConst.DB_USER + ".";
		String sql = "";
		if (advance <= 0) {
			sql = "SELECT T6036.F35,SUM(T7028.F04),T6036.F03 AS ZQ,T6010.F02,SUM(T7028.F03),T6036.F08,T6036.F24,T6036.F23,T6036.F20,T6036.F01,T6036.F02 AS USERID,T6036.F39 FROM T7028 INNER JOIN "
					+ dbName
					+ "T6036 ON T7028.F08=T6036.F01 INNER JOIN "
					+ dbName
					+ "T6010 ON T6036.F02=T6010.F01 WHERE (T7028.F06=? OR T7028.F06=?)";
			parameters.add(TradeType.DF);
			parameters.add(TradeType.DFHF);
		} else {
			sql = "SELECT T6036.F35,SUM(T7030.F05),T6036.F03 AS ZQ,T6010.F02,SUM(T7030.F04),T6036.F08,T6036.F24,T6036.F23,T6036.F20,T6036.F01,T6036.F02 AS USERID,T6036.F39 FROM T7030 INNER JOIN "
					+ dbName
					+ "T6036 ON T7030.F09=T6036.F01 INNER JOIN "
					+ dbName
					+ "T6010 ON T6036.F02=T6010.F01 WHERE T7030.F02=? AND (T7030.F07=? OR T7030.F07=?)";
			parameters.add(advance);
			parameters.add(TradeType.DF);
			parameters.add(TradeType.DFHF);
		}
		StringBuilder sb = new StringBuilder();
		sb.append(sql);
		if (query != null) {
			RestoreStatus status = query.getStatus();
			if (status != null) {
				if (status == RestoreStatus.YFK || status == RestoreStatus.YJQ) {
					sb.append(" AND T6036.F20=?");
					parameters.add(status);
				} else if (status == RestoreStatus.YZYQ
						|| status == RestoreStatus.S) {
					sb.append(" AND T6036.F39=?");
					parameters.add(status);
				}
			}
			int zqId = query.getZqId();
			if (zqId > 0) {
				sb.append(" AND T6036.F03 LIKE ?");
				parameters.add(getSQLConnectionProvider().allMatch(
						Integer.toString(zqId)));
			}
			String loginName = query.getLoginName();
			if (!StringHelper.isEmpty(loginName)) {
				sb.append(" AND T6010.F02 LIKE ?");
				parameters.add(getSQLConnectionProvider().allMatch(loginName));
			}
			Timestamp paymentTime = query.getStartPaymentTime();
			if (paymentTime != null) {
				sb.append(" AND DATE(T6036.F35) >=?");
				parameters.add(paymentTime);
			}
			paymentTime = query.getEndPaymentTime();
			if (paymentTime != null) {
				sb.append(" AND DATE(T6036.F35) <=?");
				parameters.add(paymentTime);
			}
		}
		if (advance <= 0) {
			sb.append(" GROUP BY T7028.F08");
		} else {
			sb.append(" GROUP BY T7030.F09");
		}
		sb.append(" ORDER BY T6036.F35 DESC");
		try(Connection connection = getConnection(P2PConst.DB_CONSOLE))
		{
			return selectPaging(connection,
					new ArrayParser<DfsdzqRecord>() {
						ArrayList<DfsdzqRecord> list = new ArrayList<DfsdzqRecord>();

						public DfsdzqRecord[] parse(ResultSet rst)
								throws SQLException {
							while (rst.next()) {
								DfsdzqRecord platform = new DfsdzqRecord();
								platform.paymentTime = rst.getTimestamp(1);
								platform.paymentAmount = rst.getBigDecimal(2);
								platform.id = rst.getInt(3);
								platform.loginName = rst.getString(4);
								platform.paymentRestore = rst.getBigDecimal(5);
								platform.day = rst.getInt(6);
								platform.residueDeadLine = rst.getInt(7);
								platform.nextRestoreTime = rst.getTimestamp(8);
								CreditStatus status = EnumParser.parse(
										CreditStatus.class, rst.getString(9));
								T6036_F39 t6036 = EnumParser.parse(T6036_F39.class,
										rst.getString(12));
								if (t6036 == T6036_F39.F) {
									if (status == CreditStatus.YFK
											|| status == CreditStatus.YDF) {
										platform.status = RestoreStatus.YFK;
									} else if (status == CreditStatus.YJQ) {
										platform.status = RestoreStatus.YJQ;
									}
								} else if (t6036 == T6036_F39.YZYQ) {
									platform.status = RestoreStatus.YZYQ;
								} else if (t6036 == T6036_F39.S) {
									platform.status = RestoreStatus.S;
								}
								platform.restoreAmount = getDsAmount(
										rst.getInt(10), rst.getInt(11));
								list.add(platform);
							}
							return list.toArray(new DfsdzqRecord[list.size()]);
						}
					}, paging, sb.toString(), parameters);
		}
	}

	private BigDecimal getDsAmount(int loadId, int userId) throws SQLException {
		try(Connection connection = getConnection(P2PConst.DB_USER))
		{
			BigDecimal dsAmount = selectBigDecimal(
					connection,
					"SELECT SUM(F05+F06+F07) FROM T6041 WHERE F02=? AND F03=? AND F12=? AND F13=?",
					loadId, userId, T6041_F12.WH, "F");
			return dsAmount;
		}
	}

	@Override
	public void export(DfsdzqRecord[] records, OutputStream outputStream,
			String charset) throws Throwable {
		if (outputStream == null) {
			return;
		}
		if (records == null) {
			return;
		}
		if (StringHelper.isEmpty(charset)) {
			charset = "GBK";
		}
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				outputStream, charset))) {
			writer.write("垫付时间");
			writer.write(",");
			writer.write("垫付金额");
			writer.write(",");
			writer.write("用户名");
			writer.write(",");
			writer.write("垫付返还金额");
			writer.write(",");
			writer.write("剩余期限");
			writer.write(",");
			writer.write("待收金额");
			writer.write(",");
			writer.write("下一还款日");
			writer.write(",");
			writer.write("状态");
			writer.newLine();
			for (DfsdzqRecord record : records) {
				if (record == null) {
					continue;
				}
				writer.write(" " + DateParser.format(record.paymentTime));
				writer.write(",");
				writer.write(format(record.paymentAmount));
				writer.write(",");
				writer.write(record.loginName == null ? "" : record.loginName);
				writer.write(",");
				writer.write(format(record.paymentRestore));
				writer.write(",");
				writer.write(record.residueDeadLine + "/" + record.day);
				writer.write(",");
				writer.write(format(record.restoreAmount));
				writer.write(",");
				writer.write(" " + DateParser.format(record.nextRestoreTime));
				writer.write(",");
				writer.write(record.status != null ? record.status.getName()
						: " ");
				writer.newLine();
			}
			writer.flush();
		}
	}
}
