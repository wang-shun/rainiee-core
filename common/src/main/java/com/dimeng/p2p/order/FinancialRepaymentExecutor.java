package com.dimeng.p2p.order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S64.entities.T6410;
import com.dimeng.p2p.S64.entities.T6411;
import com.dimeng.p2p.S64.entities.T6412;
import com.dimeng.p2p.S64.enums.T6410_F07;
import com.dimeng.p2p.S64.enums.T6410_F14;
import com.dimeng.p2p.S64.enums.T6410_F24;
import com.dimeng.p2p.S64.enums.T6412_F09;
import com.dimeng.p2p.S65.entities.T6512;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.util.Formater;

@ResourceAnnotation
public class FinancialRepaymentExecutor extends AbstractOrderExecutor {

	public FinancialRepaymentExecutor(ResourceProvider resourceProvider) {
		super(resourceProvider);
	}

	@Override
	public Class<? extends Resource> getIdentifiedType() {
		return FinancialRepaymentExecutor.class;
	}

	@Override
	protected void doConfirm(SQLConnection connection, int orderId,
			Map<String, String> params) throws Throwable {
		try {
			// 查询订单
			T6512 t6512 = selectT6512(connection, orderId);
			// 优选理财还款记录
			T6412 t6412 = selectT6412(connection, t6512.F03, t6512.F02, t6512.F06,
					t6512.F04);
			if (t6412 == null) {
				throw new LogicalException("还款记录不存在");
			}
			// 锁定还款人账户
			T6101 platform = selectT6101(connection, t6412.F03, T6101_F03.WLZH, true);
			if (platform == null) {
				throw new LogicalException("还款人资金账户不存在");
			}
			// 锁定收款人账户
			T6101 investor = selectT6101(connection, t6412.F04, T6101_F03.WLZH, true);
			if (investor == null) {
				throw new LogicalException("收款人资金账户不存在");
			}
			// 更新账户余额和插入资金流水
			{
				platform.F06 = platform.F06.subtract(t6512.F05);
				if (platform.F06.compareTo(BigDecimal.ZERO) < 0) {
					throw new LogicalException("平台账户余额不足");
				}
				updateT6101(connection, platform.F06, platform.F01);
				T6102 t6102 = new T6102();
				t6102.F02 = platform.F01;
				t6102.F03 = t6512.F06;
				t6102.F04 = investor.F01;
				t6102.F07 = t6512.F05;
				t6102.F08 = platform.F06;
				t6102.F09 = "优选理财还款";
				insertT6102(connection, t6102);
			}
			{
				investor.F06 = investor.F06.add(t6512.F05);
				updateT6101(connection, investor.F06, investor.F01);
				T6102 t6102 = new T6102();
				t6102.F02 = investor.F01;
				t6102.F03 = t6512.F06;
				t6102.F04 = platform.F01;
				t6102.F06 = t6512.F05;
				t6102.F08 = investor.F06;
				t6102.F09 = "优选理财还款";
				insertT6102(connection, t6102);
			}
			updateT6412(connection, T6412_F09.YH, t6412.F01);
			// 更新优选理财
			updateT6411(connection, t6512);
			T6412 t6412_1 = selectT6412(connection, t6512.F03, T6412_F09.WH);
			if (t6412_1 == null) {
				// 更新优选理财状态
				updateT6410(connection, T6410_F07.YJZ, t6512.F03);
			}
			{
				// 查询本期是否还完,更新下次还款日期
				int remain = 0;
				try (PreparedStatement pstmt = connection
						.prepareStatement("SELECT COUNT(*) FROM S64.T6412 WHERE F02 = ? AND F06 = ? AND (F09 = 'WH' OR F09 = 'HKZ')")) {
					pstmt.setInt(1, t6412.F02);
					pstmt.setInt(2, t6412.F06);
					try (ResultSet resultSet = pstmt.executeQuery()) {
						if (resultSet.next()) {
							remain = resultSet.getInt(1);
						}
					}
				}
				if (remain == 0) {
					Date nextDate = null;
					try (PreparedStatement pstmt = connection
							.prepareStatement("SELECT F08 FROM S64.T6412 WHERE F02 = ? AND F06 = ? LIMIT 1")) {
						pstmt.setInt(1, t6412.F02);
						pstmt.setInt(2, t6412.F06 + 1);
						try (ResultSet resultSet = pstmt.executeQuery()) {
							if (resultSet.next()) {
								nextDate = resultSet.getDate(1);
							}
						}
					}
					if (nextDate != null) {
						try (PreparedStatement pstmt = connection
								.prepareStatement("UPDATE S64.T6410 SET F21 = ? WHERE F01 = ?")) {
							pstmt.setDate(1, nextDate);
							pstmt.setInt(2, t6412.F02);
							pstmt.execute();
						}
					}
				}
			}
			// 优选理财记录
			T6410 t6410 = selectT6410(connection, t6512.F03);
			int pid = getPTID(connection);
			// 扣除优选理财服务费
			kcYxlcFwf(connection, t6410, t6512, pid);
			// 发站内信
			ConfigureProvider configureProvider = resourceProvider
					.getResource(ConfigureProvider.class);
			T6110 t6110 = selectT6110(connection, t6512.F02);
			Envionment envionment = configureProvider.createEnvionment();
			envionment.set("title", selectString(connection, t6512.F03));
			envionment.set("subject", selectSubject(connection, t6512.F06));
			envionment.set("amount", Formater.formatAmount(t6512.F05));
			String content = configureProvider.format(LetterVariable.YX_HKCG,
					envionment);
			sendLetter(connection, t6512.F02, "优选理财还款", content);
			sendMsg(connection, t6110.F04, content);
		} catch (Exception e) {
			logger.error(e, e);
			throw e;
		}
	}

	protected void kcYxlcFwf(Connection connection, T6410 t6410, T6512 t6512,
			int pid) throws SQLException {
		// 优选理财服务费
		BigDecimal yxlcfwf = BigDecimal.ZERO;
		if (t6512.F06 == FeeCode.TZ_LX) {
			yxlcfwf = t6512.F05.multiply(t6410.F16);
		} else {
			return;
		}
		// 锁定投资人资金账户
		T6101 t6101 = selectT6101(connection, t6512.F02, T6101_F03.WLZH, true);
		// 平台资金账户
		T6101 platform = selectT6101(connection, pid, T6101_F03.WLZH, true);
		{
			t6101.F06 = t6101.F06.subtract(yxlcfwf);
			T6102 t6102 = new T6102();
			t6102.F02 = t6101.F01;
			t6102.F03 = FeeCode.YXLC_FW;
			t6102.F04 = platform.F01;
			t6102.F07 = yxlcfwf;
			t6102.F08 = t6101.F06;
			t6102.F09 = "优选理财服务费";
			try {
				insertT6102(connection, t6102);
			} catch (Throwable throwable) {
				logger.error(throwable, throwable);
			}
		}
		// 更新投资人账户
		try {
			updateT6101(connection, t6101.F06, t6101.F01);
		} catch (Throwable throwable) {
			logger.error(throwable, throwable);
		}
		{
			// 插平台交易流水
			platform.F06 = platform.F06.add(yxlcfwf);
			T6102 t6102 = new T6102();
			t6102.F02 = platform.F01;
			t6102.F03 = FeeCode.YXLC_FW;
			t6102.F04 = t6101.F01;
			t6102.F06 = yxlcfwf;
			t6102.F08 = platform.F06;
			t6102.F09 = "优选理财服务费";
			try {
				insertT6102(connection, t6102);
			} catch (Throwable throwable) {
				logger.error(throwable, throwable);
			}
		}
		// 更新平台账户
		try {
			updateT6101(connection, platform.F06, platform.F01);
		} catch (Throwable throwable) {
			logger.error(throwable, throwable);
		}
	}

	protected T6411[] selectAllT6411_1(Connection connection, int F02)
			throws SQLException {
		ArrayList<T6411> list = null;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S64.T6411 WHERE T6411.F02 = ? GROUP BY T6411.F03")) {
			pstmt.setInt(1, F02);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				while (resultSet.next()) {
					T6411 record = new T6411();
					record.F01 = resultSet.getInt(1);
					record.F02 = resultSet.getInt(2);
					record.F03 = resultSet.getInt(3);
					record.F04 = resultSet.getBigDecimal(4);
					record.F05 = resultSet.getBigDecimal(5);
					record.F06 = resultSet.getTimestamp(6);
					record.F07 = resultSet.getTimestamp(7);
					if (list == null) {
						list = new ArrayList<>();
					}
					list.add(record);
				}
			}
		}
		return ((list == null || list.size() == 0) ? null : list
				.toArray(new T6411[list.size()]));
	}

	private T6410 selectT6410(Connection connection, int F01)
			throws SQLException {
		T6410 record = null;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24 FROM S64.T6410 WHERE T6410.F01 = ? FOR UPDATE")) {
			pstmt.setInt(1, F01);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					record = new T6410();
					record.F01 = resultSet.getInt(1);
					record.F02 = resultSet.getString(2);
					record.F03 = resultSet.getBigDecimal(3);
					record.F04 = resultSet.getBigDecimal(4);
					record.F05 = resultSet.getBigDecimal(5);
					record.F06 = resultSet.getInt(6);
					record.F07 = T6410_F07.parse(resultSet.getString(7));
					record.F08 = resultSet.getInt(8);
					record.F09 = resultSet.getTimestamp(9);
					record.F10 = resultSet.getDate(10);
					record.F11 = resultSet.getInt(11);
					record.F12 = resultSet.getTimestamp(12);
					record.F13 = resultSet.getDate(13);
					record.F14 = T6410_F14.parse(resultSet.getString(14));
					record.F15 = resultSet.getBigDecimal(15);
					record.F16 = resultSet.getBigDecimal(16);
					record.F17 = resultSet.getBigDecimal(17);
					record.F18 = resultSet.getString(18);
					record.F19 = resultSet.getInt(19);
					record.F20 = resultSet.getTimestamp(20);
					record.F21 = resultSet.getDate(21);
					record.F22 = resultSet.getBigDecimal(22);
					record.F23 = resultSet.getBigDecimal(23);
					record.F24 = T6410_F24.parse(resultSet.getString(24));
				}
			}
		}
		return record;
	}

	protected void updateT6410(Connection connection, Date date, int F02)
			throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S64.T6410 SET F21 = ? WHERE F01 = ?")) {
			pstmt.setDate(1, date);
			pstmt.setInt(2, F02);
			pstmt.execute();
		}
	}

	protected void updateT6410(Connection connection, T6410_F07 F01, int F02)
			throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S64.T6410 SET F07 = ? WHERE F01 = ?")) {
			pstmt.setString(1, F01.name());
			pstmt.setInt(2, F02);
			pstmt.execute();
		}
	}

	protected T6412 selectT6412(Connection connection, int F02, T6412_F09 F09)
			throws SQLException {
		T6412 record = null;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S64.T6412 WHERE T6412.F02 = ? AND T6412.F09 = ?")) {
			pstmt.setInt(1, F02);
			pstmt.setString(2, F09.name());
			try (ResultSet resultSet = pstmt.executeQuery()) {
				while (resultSet.next()) {
					record = new T6412();
					record.F01 = resultSet.getInt(1);
					record.F02 = resultSet.getInt(2);
					record.F03 = resultSet.getInt(3);
					record.F04 = resultSet.getInt(4);
					record.F05 = resultSet.getInt(5);
					record.F06 = resultSet.getInt(6);
					record.F07 = resultSet.getBigDecimal(7);
					record.F08 = resultSet.getDate(8);
					record.F09 = T6412_F09.parse(resultSet.getString(9));
					record.F10 = resultSet.getTimestamp(10);
				}
			}
		}
		return record;
	}

	protected void updateT6411(Connection connection, T6512 t6512)
			throws SQLException {
		try (PreparedStatement ps = connection
				.prepareStatement("UPDATE S64.T6411 SET F05=F05-? WHERE F02=? AND F03=?")) {
			if (t6512.F06 == FeeCode.TZ_BJ) {
				ps.setBigDecimal(1, t6512.F05);
			} else {
				ps.setBigDecimal(1, BigDecimal.ZERO);
			}
			ps.setInt(2, t6512.F03);
			ps.setInt(3, t6512.F02);
			ps.executeUpdate();
		}

	}

	private String selectSubject(Connection connection, int F01)
			throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F02 FROM S51.T5122 WHERE T5122.F01 = ? LIMIT 1")) {
			pstmt.setInt(1, F01);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getString(1);
				}
			}
		}
		return null;
	}

	private String selectString(Connection connection, int F01)
			throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F02 FROM S64.T6410 WHERE T6410.F01 = ? LIMIT 1")) {
			pstmt.setInt(1, F01);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getString(1);
				}
			}
		}
		return null;
	}

	private void updateT6412(Connection connection, T6412_F09 F01, int F03)
			throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S64.T6412 SET F09 = ?, F10 = ? WHERE F01 = ?")) {
			pstmt.setString(1, F01.name());
			pstmt.setTimestamp(2,getCurrentTimestamp(connection));
			pstmt.setInt(3, F03);
			pstmt.execute();
		}
	}

	private void updateT6101(Connection connection, BigDecimal F01, int F02)
			throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = ?  WHERE F01 = ?")) {
			pstmt.setBigDecimal(1, F01);
			pstmt.setTimestamp(2,getCurrentTimestamp(connection));
            pstmt.setInt(3, F02);
			pstmt.execute();
		}
	}

	private T6512 selectT6512(Connection connection, int F01)
			throws SQLException {
		T6512 record = null;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01, F02, F03, F04, F05, F06 FROM S65.T6512 WHERE T6512.F01 = ? LIMIT 1")) {
			pstmt.setInt(1, F01);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					record = new T6512();
					record.F01 = resultSet.getInt(1);
					record.F02 = resultSet.getInt(2);
					record.F03 = resultSet.getInt(3);
					record.F04 = resultSet.getInt(4);
					record.F05 = resultSet.getBigDecimal(5);
					record.F06 = resultSet.getInt(6);
				}
			}
		}
		return record;
	}

	private T6412 selectT6412(Connection connection, int F02, int F04, int F05,
			int F06) throws SQLException {
		T6412 record = null;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S64.T6412 WHERE T6412.F02 = ? AND T6412.F04 = ? AND T6412.F05 = ? AND T6412.F06 = ? FOR UPDATE")) {
			pstmt.setInt(1, F02);
			pstmt.setInt(2, F04);
			pstmt.setInt(3, F05);
			pstmt.setInt(4, F06);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					record = new T6412();
					record.F01 = resultSet.getInt(1);
					record.F02 = resultSet.getInt(2);
					record.F03 = resultSet.getInt(3);
					record.F04 = resultSet.getInt(4);
					record.F05 = resultSet.getInt(5);
					record.F06 = resultSet.getInt(6);
					record.F07 = resultSet.getBigDecimal(7);
					record.F08 = resultSet.getDate(8);
					record.F09 = T6412_F09.parse(resultSet.getString(9));
					record.F10 = resultSet.getTimestamp(10);
				}
			}
		}
		return record;
	}

}