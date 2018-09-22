package com.dimeng.p2p.order;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.data.sql.SQLConnection;
import com.dimeng.framework.resource.Resource;
import com.dimeng.framework.resource.ResourceAnnotation;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6285;
import com.dimeng.p2p.S62.entities.T6289;
import com.dimeng.p2p.S62.enums.*;
import com.dimeng.p2p.S65.entities.T6525;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.util.Formater;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@ResourceAnnotation
public class ActivityRepaymentExecutor extends AbstractOrderExecutor {

	public ActivityRepaymentExecutor(ResourceProvider resourceProvider) {
		super(resourceProvider);
	}

	@Override
	public Class<? extends Resource> getIdentifiedType() {
		return ActivityRepaymentExecutor.class;
	}

	@Override
	protected void doConfirm(SQLConnection connection, int orderId,
		Map<String, String> params) throws Throwable {
		T6525 t6525 = getT6525(connection, orderId);
		if (t6525 == null) {
			throw new LogicalException("订单不存在");
		}
		try {
			// 查询加息券利息返还记录
			T6289 t6289 = selectT6289(connection, t6525.F08);
			if (t6289 == null) {
				throw new LogicalException("无加息券利息返还记录数据");
			}
			if (t6289.F09 == T6289_F09.YFH || t6289.F09 == T6289_F09.YSX) {
				throw new LogicalException("加息券利息返还记录已返还或已失效");
			}
			// 锁定利息返还平台往来账户
			T6101 hkrAcount = selectT6101(connection, t6289.F03, T6101_F03.WLZH, true);
			if (hkrAcount == null) {
				throw new LogicalException("加息券还款，利息返还平台往来账户不存在");
			}
			// 锁定收款人往来账户
			T6101 skrAcount = null;
			if (t6289.F03 == t6289.F04) {
				skrAcount = hkrAcount;
			} else {
				skrAcount = selectT6101(connection, t6289.F04, T6101_F03.WLZH, true);
			}
			if (skrAcount == null) {
				throw new LogicalException("加息券还款，收款人往来账户不存在");
			}
			T6230 t6230 = selectT6230(connection, t6525.F03);
			// 还款
			String comment = String.format("加息券利息返还:%s 第 %s期", t6230.F25,
					Integer.toString(t6289.F06));
			{
				hkrAcount.F06 = hkrAcount.F06.subtract(t6289.F07);
				updateT6101(connection, hkrAcount.F06, hkrAcount.F01);
				T6102 t6102 = new T6102();
				t6102.F02 = hkrAcount.F01;
				t6102.F03 = t6289.F05;
				t6102.F04 = skrAcount.F01;
				t6102.F07 = t6289.F07;
				t6102.F08 = hkrAcount.F06;
				t6102.F09 = comment;
				insertT6102(connection, t6102);
			}
			{
				skrAcount.F06 = skrAcount.F06.add(t6289.F07);
				updateT6101(connection, skrAcount.F06, skrAcount.F01);
				T6102 t6102 = new T6102();
				t6102.F02 = skrAcount.F01;
				t6102.F03 = t6289.F05;
				t6102.F04 = hkrAcount.F01;
				t6102.F06 = t6289.F07;
				t6102.F08 = skrAcount.F06;
				t6102.F09 = comment;
				insertT6102(connection, t6102);
			}
			// 更新还款记录
			updateT6289(connection, T6289_F09.YFH, t6289.F01);
			// 修改订单状态
			updateSubmit(connection, T6501_F03.CG, t6525.F01);


			// 发站内信
			ConfigureProvider configureProvider = resourceProvider
					.getResource(ConfigureProvider.class);
			T6110 t6110 = selectT6110(connection, t6525.F02);
			String subject = selectSubject(connection, t6525.F06);
			String amount = Formater.formatAmount(t6525.F05);
			Envionment envionment = configureProvider.createEnvionment();
			envionment.set("title", t6230.F03);
			envionment.set("subject", subject);
			envionment.set("amount", amount);
			String content = configureProvider.format(LetterVariable.TZR_JXQFHLX, envionment);
			sendLetter(connection, t6525.F02, comment, content);
			String msgContent = configureProvider.format(MsgVariavle.TZR_JXQFHLX, envionment);
			sendMsg(connection, t6110.F04, msgContent);
			
			// 托管调用接口
			callFace(connection, orderId, params);
		} catch (Exception e) {
		    // 托管调用接口
		    rollbackFace(connection, orderId, params);
			logger.error(e, e);
			throw e;
		}
	}

	/**
	 * 查询剩余的加息券返回记录
	 *
	 * @param connection
	 * @param t6525
	 * @return
	 * @throws SQLException
	 */
	private int getRestCount(SQLConnection connection, T6525 t6525, String status)
			throws SQLException {
		int remain = -1;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT COUNT(*) FROM S62.T6289 WHERE F02 = ? AND F09 = ?")) {
			pstmt.setInt(1, t6525.F03);
			pstmt.setString(2, status);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					remain = resultSet.getInt(1);
				}
			}
		}
		return remain;
	}

	/**
	 * 更新加息券利息返还记录
	 *
	 * @param connection
	 * @param F09
	 * @param F01
	 * @throws Throwable
	 */
	protected void updateT6289(Connection connection, T6289_F09 F09, int F01) throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S62.T6289 SET T6289.F09 = ?, T6289.F10 = ? WHERE T6289.F01 = ?")) {
			pstmt.setString(1, F09.name());
			pstmt.setTimestamp(2, getCurrentTimestamp(connection));
			pstmt.setInt(3, F01);
			pstmt.execute();
		}
	}

	/**
	 * 查询是否有加息券还款订单
	 *
	 * @param orderId
	 * @return
	 * @throws Throwable
	 */
	protected T6525 getT6525(SQLConnection connection, int orderId) throws Throwable {
		T6525 t6525 = null;
		try (PreparedStatement pstm = connection
				.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09 FROM S65.T6525 WHERE F01 = ? FOR UPDATE")) {
			pstm.setInt(1, orderId);
			try (ResultSet resultSet = pstm.executeQuery()) {
				if (resultSet.next()) {
					t6525 = new T6525();
					t6525.F01 = resultSet.getInt(1);
					t6525.F02 = resultSet.getInt(2);
					t6525.F03 = resultSet.getInt(3);
					t6525.F04 = resultSet.getInt(4);
					t6525.F05 = resultSet.getBigDecimal(5);
					t6525.F06 = resultSet.getInt(6);
					t6525.F07 = resultSet.getInt(7);
					t6525.F08 = resultSet.getInt(8);
					t6525.F09 = resultSet.getInt(9);
				}
			}
		}
		return t6525;
	}

	/**
	 * 查询加息券利息返还记录
	 *
	 * @param connection
	 * @param F01
	 * @return
	 * @throws SQLException
	 */
	protected T6289 selectT6289(Connection connection, int F01) throws SQLException {
		T6289 record = null;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13 FROM S62.T6289 WHERE T6289.F01 = ? LIMIT 1 FOR UPDATE")) {
			pstmt.setInt(1, F01);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					record = new T6289();
					record.F01 = resultSet.getInt(1);
					record.F02 = resultSet.getInt(2);
					record.F03 = resultSet.getInt(3);
					record.F04 = resultSet.getInt(4);
					record.F05 = resultSet.getInt(5);
					record.F06 = resultSet.getInt(6);
					record.F07 = resultSet.getBigDecimal(7);
					record.F08 = resultSet.getDate(8);
					record.F09 = T6289_F09.parse(resultSet.getString(9));
					record.F10 = resultSet.getTimestamp(10);
					record.F11 = resultSet.getBigDecimal(11);
					record.F12 = resultSet.getInt(12);
					record.F13 = resultSet.getInt(13);
				}
			}
		}
		return record;
	}

	protected T6230 selectT6230(Connection connection, int F01) throws SQLException {
		T6230 record = null;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25, F26, F27 FROM S62.T6230 WHERE T6230.F01 = ?")) {
			pstmt.setInt(1, F01);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					record = new T6230();
					record.F01 = resultSet.getInt(1);
					record.F02 = resultSet.getInt(2);
					record.F03 = resultSet.getString(3);
					record.F04 = resultSet.getInt(4);
					record.F05 = resultSet.getBigDecimal(5);
					record.F06 = resultSet.getBigDecimal(6);
					record.F07 = resultSet.getBigDecimal(7);
					record.F08 = resultSet.getInt(8);
					record.F09 = resultSet.getInt(9);
					record.F10 = T6230_F10.parse(resultSet.getString(10));
					record.F11 = T6230_F11.parse(resultSet.getString(11));
					record.F12 = T6230_F12.parse(resultSet.getString(12));
					record.F13 = T6230_F13.parse(resultSet.getString(13));
					record.F14 = T6230_F14.parse(resultSet.getString(14));
					record.F15 = T6230_F15.parse(resultSet.getString(15));
					record.F16 = T6230_F16.parse(resultSet.getString(16));
					record.F17 = T6230_F17.parse(resultSet.getString(17));
					record.F18 = resultSet.getInt(18);
					record.F19 = resultSet.getInt(19);
					record.F20 = T6230_F20.parse(resultSet.getString(20));
					record.F21 = resultSet.getString(21);
					record.F22 = resultSet.getTimestamp(22);
					record.F23 = resultSet.getInt(23);
					record.F24 = resultSet.getTimestamp(24);
					record.F25 = resultSet.getString(25);
					record.F26 = resultSet.getBigDecimal(26);
					record.F27 = T6230_F27.parse(resultSet.getString(27));
				}
			}
		}
		return record;
	}

	protected String selectSubject(Connection connection, int F01)
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

	private T6230 selectString(Connection connection, int F01)
		throws SQLException {
		T6230 t6230 = new T6230();
		try (PreparedStatement pstmt = connection
			.prepareStatement("SELECT F03,F25 FROM S62.T6230 WHERE T6230.F01 = ? LIMIT 1")) {
			pstmt.setInt(1, F01);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					t6230.F03 = resultSet.getString(1);
					t6230.F25 = resultSet.getString(2);
					return t6230;
				}
			}
		}
		return t6230;
	}

	private void updateT6285(Connection connection, T6285_F09 F09, int F01)
		throws Throwable {
		try (PreparedStatement pstmt = connection
			.prepareStatement("UPDATE S62.T6285 SET T6285.F09 = ?, T6285.F10 = ? WHERE T6285.F01 = ?")) {
			pstmt.setString(1, F09.name());
			pstmt.setTimestamp(2,getCurrentTimestamp(connection));
			pstmt.setInt(3, F01);
			pstmt.execute();
		}
	}

	protected void updateT6101(Connection connection, BigDecimal F01, int F02)
		throws Throwable {
		try (PreparedStatement pstmt = connection
			.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = ? WHERE F01 = ?")) {
			pstmt.setBigDecimal(1, F01);
			pstmt.setTimestamp(2,getCurrentTimestamp(connection));
			pstmt.setInt(3, F02);
			pstmt.execute();
		}
	}

	private T6285 selectT6285(Connection connection, int F01)
		throws SQLException {
		T6285 record = null;
		try (PreparedStatement pstmt = connection
			.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S62.T6285 WHERE T6285.F01 = ? FOR UPDATE")) {
			pstmt.setInt(1, F01);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					record = new T6285();
					record.F01 = resultSet.getInt(1);
					record.F02 = resultSet.getInt(2);
					record.F03 = resultSet.getInt(3);
					record.F04 = resultSet.getInt(4);
					record.F05 = resultSet.getInt(5);
					record.F06 = resultSet.getInt(6);
					record.F07 = resultSet.getBigDecimal(7);
					record.F08 = resultSet.getDate(8);
					record.F09 = T6285_F09.parse(resultSet.getString(9));
					record.F10 = resultSet.getTimestamp(10);
					record.F11 = resultSet.getBigDecimal(11);
				}
			}
		}
		return record;
	}

	protected T6525 selectT6525(Connection connection, int F01)
		throws SQLException {
		T6525 record = null;
		try (PreparedStatement pstmt = connection
			.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S65.T6525 WHERE T6525.F01 = ? LIMIT 1")) {
			pstmt.setInt(1, F01);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					record = new T6525();
					record.F01 = resultSet.getInt(1);
					record.F02 = resultSet.getInt(2);
					record.F03 = resultSet.getInt(3);
					record.F04 = resultSet.getInt(4);
					record.F05 = resultSet.getBigDecimal(5);
					record.F06 = resultSet.getInt(6);
					record.F07 = resultSet.getInt(7);
					record.F08 = resultSet.getInt(8);
				}
			}
		}
		return record;
	}

}
