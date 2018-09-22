package com.dimeng.p2p.order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

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
import com.dimeng.p2p.S61.enums.T6103_F06;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6285;
import com.dimeng.p2p.S62.enums.T6285_F09;
import com.dimeng.p2p.S65.entities.T6520;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.util.Formater;

@ResourceAnnotation
public class ExperTenderRepaymentExecutor extends AbstractOrderExecutor {

	public ExperTenderRepaymentExecutor(ResourceProvider resourceProvider) {
		super(resourceProvider);
	}

	@Override
	public Class<? extends Resource> getIdentifiedType() {
		return ExperTenderRepaymentExecutor.class;
	}

	@Override
	protected void doConfirm(SQLConnection connection, int orderId,
		Map<String, String> params) throws Throwable {
		try {
			// 订单查询
			T6520 t6520 = selectT6520(connection, orderId);
			if (t6520 == null) {
				throw new LogicalException("体验金订单不存在");
			}
			// 查询体验金利息返还记录
			T6285 t6285 = selectT6285(connection, t6520.F08);
			if (t6285 == null) {
				throw new LogicalException("体验金利息返还记录不存在");
			}
			if (t6285.F09 == T6285_F09.YFH) {
				throw new LogicalException("体验金利息返还记录不存在");
			}
			// 锁定利息返还平台往来账户
			T6101 hkrAcount = selectT6101(connection, t6285.F03, T6101_F03.WLZH, true);
			if (hkrAcount == null) {
				throw new LogicalException("利息返还平台往来账户不存在");
			}
			// 锁定收款人往来账户
			T6101 skrAcount = null;
			if (t6285.F03 == t6285.F04) {
				skrAcount = hkrAcount;
			} else {
				skrAcount = selectT6101(connection, t6285.F04, T6101_F03.WLZH, true);
			}
			if (skrAcount == null) {
				throw new LogicalException("收款人往来账户不存在");
			}
			T6230 t6230 = selectString(connection, t6520.F03);
			// 还款
			String comment = String.format("体验金利息返还:%s 第 %s期", t6230.F25,
				Integer.toString(t6285.F06));
			{
				hkrAcount.F06 = hkrAcount.F06.subtract(t6285.F07);
				updateT6101(connection, hkrAcount.F06, hkrAcount.F01);
				T6102 t6102 = new T6102();
				t6102.F02 = hkrAcount.F01;
				t6102.F03 = t6285.F05;
				t6102.F04 = skrAcount.F01;
				t6102.F07 = t6285.F07;
				t6102.F08 = hkrAcount.F06;
				t6102.F09 = comment;
				insertT6102(connection, t6102);
			}
			{
				skrAcount.F06 = skrAcount.F06.add(t6285.F07);
				updateT6101(connection, skrAcount.F06, skrAcount.F01);
				T6102 t6102 = new T6102();
				t6102.F02 = skrAcount.F01;
				t6102.F03 = t6285.F05;
				t6102.F04 = hkrAcount.F01;
				t6102.F06 = t6285.F07;
				t6102.F08 = skrAcount.F06;
				t6102.F09 = comment;
				insertT6102(connection, t6102);
			}
			// 更新还款记录
			updateT6285(connection, T6285_F09.YFH, t6285.F01);


			// 查询标剩余期数
			int remain = -1;
			try (PreparedStatement pstmt =
					 connection.prepareStatement("SELECT COUNT(*) FROM S62.T6285 WHERE F02 = ? AND (F09 = 'WFH' OR F09 = 'FHZ')"))
			{
				pstmt.setInt(1, t6520.F03);
				try (ResultSet resultSet = pstmt.executeQuery())
				{
					if (resultSet.next())
					{
						remain = resultSet.getInt(1);
					}
				}
			}
			if (remain == 0)
			{
				try (PreparedStatement pstmt =
						 connection.prepareStatement("UPDATE S61.T6103 SET F06 = ? WHERE F13 = ?"))
				{
					pstmt.setString(1, T6103_F06.YJQ.name());
					pstmt.setInt(2, t6520.F03);
					pstmt.execute();
				}
			}

			// 发站内信
			ConfigureProvider configureProvider = resourceProvider
				.getResource(ConfigureProvider.class);
			T6110 t6110 = selectT6110(connection, t6520.F02);
			String subject = selectSubject(connection, t6520.F06);
			String amount = Formater.formatAmount(t6520.F05);
			Envionment envionment = configureProvider.createEnvionment();
			envionment.set("title", t6230.F03);
			envionment.set("subject", subject);
			envionment.set("amount", amount);
			String content = configureProvider.format(LetterVariable.TZR_TYJFHLX, envionment);
			sendLetter(connection, t6520.F02, comment, content);
			String msgContent = configureProvider.format(MsgVariavle.TZR_TYJFHLX, envionment);
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

	protected T6230 selectString(Connection connection, int F01)
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

	protected void updateT6285(Connection connection, T6285_F09 F09, int F01)
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

	protected T6285 selectT6285(Connection connection, int F01)
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

	protected T6520 selectT6520(Connection connection, int F01)
		throws SQLException {
		T6520 record = null;
		try (PreparedStatement pstmt = connection
			.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S65.T6520 WHERE T6520.F01 = ? LIMIT 1")) {
			pstmt.setInt(1, F01);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					record = new T6520();
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
