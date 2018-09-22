package com.dimeng.p2p.order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6238;
import com.dimeng.p2p.S62.entities.T6251;
import com.dimeng.p2p.S62.entities.T6252;
import com.dimeng.p2p.S62.entities.T6253;
import com.dimeng.p2p.S62.entities.T6264;
import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F12;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F14;
import com.dimeng.p2p.S62.enums.T6230_F15;
import com.dimeng.p2p.S62.enums.T6230_F16;
import com.dimeng.p2p.S62.enums.T6230_F17;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6230_F27;
import com.dimeng.p2p.S62.enums.T6231_F18;
import com.dimeng.p2p.S62.enums.T6231_F19;
import com.dimeng.p2p.S62.enums.T6251_F08;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S62.enums.T6264_F04;
import com.dimeng.p2p.S65.entities.T6506;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.common.SMSUtils;
import com.dimeng.p2p.variables.defines.GuarantorVariavle;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.smses.SmsVaribles;
import com.dimeng.util.Formater;
import com.dimeng.util.parser.BooleanParser;

@ResourceAnnotation
public class TenderRepaymentExecutor extends AbstractOrderExecutor {

	public TenderRepaymentExecutor(ResourceProvider resourceProvider) {
		super(resourceProvider);
	}

	@Override
	public Class<? extends Resource> getIdentifiedType() {
		return TenderRepaymentExecutor.class;
	}

	@Override
	protected void doConfirm(SQLConnection connection, int orderId,
			Map<String, String> params) throws Throwable {
		try {
			// 订单查询
			T6506 t6506 = selectT6506(connection, orderId);
			if (t6506 == null) {
				throw new LogicalException("还款订单明细记录不存在");
			}
			// 锁定标
			T6230 t6230 = selectT6230(connection, t6506.F03);
			if (t6230 == null) {
				throw new LogicalException("标记录不存在");
			}

			// 查询还款记录
			T6252 t6252 = selectT6252(connection, t6506.F07, t6506.F05,
					t6506.F04);
			if (t6252 == null) {
				throw new LogicalException("还款记录不存在");
			}
			if (t6252.F09 == T6252_F09.YH) {
				throw new LogicalException("还款记录不存在");
			}

			/*
			 * 如果当前还款标的状态是已垫付同时还款记录收款人ID同还款订单收款人ID不一致 说明处理还款回调时，该标已完成垫付操作
			 * 需引发异常，取消该笔还款，让用户重新还款
			 */
			String escrow = resourceProvider.getResource(
					ConfigureProvider.class).getProperty(
					SystemVariable.ESCROW_PREFIX);
			if (escrow.equals("yeepay") && t6230.F20 == T6230_F20.YDF
					&& t6252.F04 != t6506.F02) {
				throw new LogicalException(
						"该笔还款已垫付，还款对象已变化，请重新还款！".concat(escrow));
			}

			/**
			 * 由于会有同一个标多期还款同时自动还款的现象，易宝那边发送回调信息 一，不会根据平台的请求顺序返回
			 * 二，回调信息返回间隔时间过短，导致前一条回调信息未处理完，后一条回调信息处理的数据不是最新的
			 * 现将该标T6252表的所有数据进行For update 操作
			 */
			if (escrow.equals("yeepay")) {
				this.selectT6252ForUpdate(connection, t6230.F01);
			}

			// 锁定还款人往来账户
			T6101 hkrAcount = selectT6101(connection, t6252.F03,
					T6101_F03.WLZH, true);
			if (hkrAcount == null) {
				throw new LogicalException("还款人往来账户不存在");
			}
			// 锁定收款人往来账户
			T6101 skrAcount = null;
			if (t6252.F03 == t6252.F04) {
				skrAcount = hkrAcount;
			} else {
				skrAcount = selectT6101(connection, t6252.F04, T6101_F03.WLZH,
						true);
			}
			if (skrAcount == null) {
				throw new LogicalException("收款人往来账户不存在");
			}
			// 还款
			String comment = String.format("散标还款:%s，标题：%s， 第 %s期", t6230.F25,
					t6230.F03, Integer.toString(t6252.F06));
			int bidId = 0;
			T6253 t6253 = selectT6253(connection, t6230.F01);
			if (t6253 != null) {
				comment = String.format("垫付返还:%s，标题：%s", t6230.F25, t6230.F03);
				bidId = t6230.F01;
			}
			{
				hkrAcount.F06 = hkrAcount.F06.subtract(t6506.F06);
				if (hkrAcount.F06.compareTo(BigDecimal.ZERO) < 0) {
					throw new LogicalException("还款人账户余额不足");
				}
				updateT6101(connection, hkrAcount.F06, hkrAcount.F01);
				T6102 t6102 = new T6102();
				t6102.F02 = hkrAcount.F01;
				t6102.F03 = t6252.F05;
				t6102.F04 = skrAcount.F01;
				t6102.F07 = t6506.F06;
				t6102.F08 = hkrAcount.F06;
				t6102.F09 = comment;
				t6102.F12 = bidId;
				insertT6102(connection, t6102);
			}
			{
				skrAcount.F06 = skrAcount.F06.add(t6506.F06);
				updateT6101(connection, skrAcount.F06, skrAcount.F01);
				T6102 t6102 = new T6102();
				t6102.F02 = skrAcount.F01;
				t6102.F03 = t6252.F05;
				t6102.F04 = hkrAcount.F01;
				t6102.F06 = t6506.F06;
				t6102.F08 = skrAcount.F06;
				t6102.F09 = comment;
				t6102.F12 = bidId;
				insertT6102(connection, t6102);
			}
			updateT6252(connection, T6252_F09.YH, t6252.F01);
			ConfigureProvider configureProvider = resourceProvider
					.getResource(ConfigureProvider.class);
			// 更新债权持有金额
			if (t6506.F07 == FeeCode.TZ_BJ) {
				// 锁定债权信息
				T6251 t6251 = selectT6251(connection, t6506.F04);
				if (t6251 == null) {
					throw new LogicalException("债权记录不存在");
				}
				t6251.F07 = t6251.F07.subtract(t6506.F06);
				if (t6251.F07.compareTo(BigDecimal.ZERO) <= 0) {
					t6251.F07 = BigDecimal.ZERO;
				}
				updateT6251(connection, t6251.F07, t6506.F04);
				{
					// 添加借款人信用额度
					BigDecimal ed = BigDecimal.ZERO;
					try (PreparedStatement ps = connection
							.prepareStatement("SELECT F03 FROM S61.T6116 WHERE F01 = ? FOR UPDATE")) {
						ps.setInt(1, t6230.F02);
						try (ResultSet resultSet = ps.executeQuery()) {
							if (resultSet.next()) {
								ed = resultSet.getBigDecimal(1);
							}
						}
					}
					ed = ed.add(t6506.F06);
					try (PreparedStatement ps = connection
							.prepareStatement("UPDATE S61.T6116 SET F03=F03+? WHERE F01=?")) {
						ps.setBigDecimal(1, t6506.F06);
						ps.setInt(2, t6230.F02);
						ps.execute();
					}
					try (PreparedStatement ps = connection
							.prepareStatement("INSERT INTO S61.T6117 SET F02=?,F03=?,F04=?,F05=?,F07=?,F08=?")) {
						ps.setInt(1, t6230.F02);
						ps.setInt(2, FeeCode.XY_HK_FH);
						ps.setTimestamp(3, getCurrentTimestamp(connection));
						ps.setBigDecimal(4, t6506.F06);
						ps.setBigDecimal(5, ed);
						ps.setString(6, "还款信用额度返回");
						ps.execute();
					}

					// 查询担保信息
					int dbjgId = 0;
					try (PreparedStatement ps = connection
							.prepareStatement("SELECT T6236.F03 FROM S62.T6236 WHERE T6236.F02 = ? FOR UPDATE")) {
						ps.setInt(1, t6230.F01);
						try (ResultSet resultSet = ps.executeQuery()) {
							if (resultSet.next()) {
								dbjgId = resultSet.getInt(1);

							}
						}
					}

					// 如果有担保，添加担保机构信用额度
					if (dbjgId > 0) {
						// 还款时退还担保方的担保额度
						rebackAmount(connection, t6506.F06, dbjgId);
					}
				}
			} else if (t6506.F07 == FeeCode.TZ_LX || t6506.F07 == FeeCode.TZ_FX) {
				// 计算投资管理费
				T6238 t6238 = selectT6238(connection, t6230.F01);
				if (t6238 == null) {
					throw new LogicalException("标的费率不存在");
				}
				BigDecimal fee = t6506.F06.multiply(t6238.F03).setScale(2,
						BigDecimal.ROUND_HALF_UP);
				if (t6238.F03.compareTo(BigDecimal.ZERO) > 0
						&& fee.compareTo(BigDecimal.ZERO) > 0 && t6253 == null) {
					int pid = getPTID(connection);
					// 锁定平台往来账户
					T6101 ptwl = selectT6101(connection, pid, T6101_F03.WLZH,
							true);
					if (ptwl == null) {
						throw new LogicalException("平台往来账户不存在");
					}
					String feeComment = String.format("投资管理费:%s，标题：%s， 第 %s期",
							t6230.F25, t6230.F03, Integer.toString(t6252.F06));
					{
						// 扣减收款人管理费
						skrAcount.F06 = skrAcount.F06.subtract(fee);
						updateT6101(connection, skrAcount.F06, skrAcount.F01);
						T6102 t6102 = new T6102();
						t6102.F02 = skrAcount.F01;
						t6102.F03 = FeeCode.GLF;
						t6102.F04 = ptwl.F01;
						t6102.F07 = fee;
						t6102.F08 = skrAcount.F06;
						t6102.F09 = feeComment;
						insertT6102(connection, t6102);
					}
					{
						// 平台收投资管理费
						ptwl.F06 = ptwl.F06.add(fee);
						updateT6101(connection, ptwl.F06, ptwl.F01);
						T6102 t6102 = new T6102();
						t6102.F02 = ptwl.F01;
						t6102.F03 = FeeCode.GLF;
						t6102.F04 = skrAcount.F01;
						t6102.F06 = fee;
						t6102.F08 = ptwl.F06;
						t6102.F09 = feeComment;
						insertT6102(connection, t6102);
					}
				}
			}

			// 查询本期是否还完
			boolean isYet = false;
			{
				// 查询本期是否还完,更新下次还款日期
				int remain = 0;
				try (PreparedStatement pstmt = connection
						.prepareStatement("SELECT COUNT(*) FROM S62.T6252 WHERE F02 = ? AND F06 = ? AND (F09 = 'WH' OR F09 = 'HKZ')")) {
					pstmt.setInt(1, t6230.F01);
					pstmt.setInt(2, t6252.F06);
					try (ResultSet resultSet = pstmt.executeQuery()) {
						if (resultSet.next()) {
							remain = resultSet.getInt(1);
						}
					}
				}
				if (remain == 0) {
					isYet = true;
					Date nextDate = null;
					Date nowDate = getCurrentDate(connection);
					try (PreparedStatement pstmt = connection
							.prepareStatement("SELECT F08 FROM S62.T6252 WHERE F02 = ? AND F06 = ? LIMIT 1")) {
						pstmt.setInt(1, t6230.F01);
						pstmt.setInt(2, t6252.F06 + 1);
						try (ResultSet resultSet = pstmt.executeQuery()) {
							if (resultSet.next()) {
								nextDate = resultSet.getDate(1);
							}
						}
					}
					if (nextDate != null) {
						try (PreparedStatement pstmt = connection
								.prepareStatement("UPDATE S62.T6231 SET F06 = ?,F19 = ? WHERE F01 = ?")) {
							pstmt.setDate(1, nextDate);
							pstmt.setString(
									2,
									(nextDate.compareTo(nowDate) < 0 ? T6231_F19.S
											.name() : T6231_F19.F.name()));
							pstmt.setInt(3, t6230.F01);
							pstmt.execute();
						}
					}
				}
			}
			{
				// 查询标剩余期数
				int remain = -1;
				try (PreparedStatement pstmt = connection
						.prepareStatement("SELECT COUNT(*) FROM S62.T6252 WHERE F02 = ? AND (F09 = 'WH' OR F09 = 'HKZ')")) {
					pstmt.setInt(1, t6230.F01);
					try (ResultSet resultSet = pstmt.executeQuery()) {
						if (resultSet.next()) {
							remain = resultSet.getInt(1);
						}
					}
				}
				if (remain == 0) {
					try (PreparedStatement pstmt = connection
							.prepareStatement("UPDATE S62.T6230 SET F20 = 'YJQ' WHERE F01 = ?")) {
						pstmt.setInt(1, t6230.F01);
						pstmt.execute();
					}
					updateT6231(connection, t6230.F01);
				}
				int count = 0;
				try (PreparedStatement ps = connection
						.prepareStatement("SELECT COUNT(F06) FROM S62.T6252 WHERE F02 = ? AND F06=? AND (F09 = 'WH' OR F09 = 'HKZ')")) {
					ps.setInt(1, t6230.F01);
					ps.setInt(2, t6252.F06);
					try (ResultSet rs = ps.executeQuery()) {
						if (rs.next()) {
							count = rs.getInt(1);
						}
					}
				}
				if (count == 0) {
					int num = 0;
					try (PreparedStatement ps = connection
							.prepareStatement("SELECT F03 FROM S62.T6231 WHERE F01=? FOR UPDATE")) {
						ps.setInt(1, t6230.F01);
						try (ResultSet rs = ps.executeQuery()) {
							if (rs.next()) {
								num = rs.getInt(1);
							}
						}
					}
					if (num > 0) {
						num = num - 1;
						try (PreparedStatement pstmt = connection
								.prepareStatement("UPDATE S62.T6231 SET F03 = ? WHERE F01 = ?")) {
							pstmt.setInt(1, num);
							pstmt.setInt(2, t6230.F01);
							pstmt.execute();
						}
					}
				}
			}
			// 如果该标有垫付
			if (t6253 != null) {
				try (PreparedStatement ps = connection
						.prepareStatement("UPDATE S62.T6253 SET F06=F06+? WHERE F01=?")) {
					ps.setBigDecimal(1, t6506.F06);
					ps.setInt(2, t6253.F01);
					ps.executeUpdate();
				}
			}

			/**
			 * 判断标是否逾期
			 */
			if (!selectIsYuqi(connection, t6230.F01)) {
				try (PreparedStatement ps = connection
						.prepareStatement("UPDATE S62.T6231 SET F19 = ? WHERE F01=?")) {
					ps.setString(1, T6231_F18.F.name());
					ps.setInt(2, t6230.F01);
					ps.executeUpdate();
				}
			}

			// 本期还完，发送信息
			if (isYet) {
				// 投资者用户id
				List<Integer> tzzUserIds = new ArrayList<Integer>();
				try (PreparedStatement pstmt = connection
						.prepareStatement("SELECT DISTINCT T6252.F04 FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F06 = ? AND T6252.F09 = ? ")) {
					pstmt.setInt(1, t6230.F01);
					pstmt.setInt(2, t6252.F06);
					pstmt.setString(3, T6252_F09.YH.name());
					try (ResultSet resultSet = pstmt.executeQuery()) {
						while (resultSet.next()) {
							tzzUserIds.add(resultSet.getInt(1));
						}
					}
				}

				String isUseYtx = configureProvider
						.getProperty(SmsVaribles.SMS_IS_USE_YTX);
				BigDecimal amountB = BigDecimal.ZERO;
				for (Integer tzzUserId : tzzUserIds) {
					try (PreparedStatement pstmt = connection
							.prepareStatement("SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F06 = ? AND T6252.F09 = ? AND T6252.F04 = ?")) {
						pstmt.setInt(1, t6230.F01);
						pstmt.setInt(2, t6252.F06);
						pstmt.setString(3, T6252_F09.YH.name());
						pstmt.setInt(4, tzzUserId);
						try (ResultSet resultSet = pstmt.executeQuery()) {
							if (resultSet.next()) {
								amountB = resultSet.getBigDecimal(1);
							}
						}
					}

					// 给投资人 发站内信
					T6110 t6110 = selectT6110(connection, tzzUserId);
					Envionment envionment = configureProvider
							.createEnvionment();
					envionment.set("title", t6230.F03);
					envionment.set("amount", Formater.formatAmount(amountB));
					envionment.set("periods", t6252.F06 + "");
					String content = configureProvider.format(
							LetterVariable.TZR_TBSK, envionment);
					sendLetter(connection, tzzUserId, comment, content);
					if ("1".equals(isUseYtx)) {
						SMSUtils smsUtils = new SMSUtils(configureProvider);
						int type = smsUtils.getTempleId(MsgVariavle.TZR_TBSK
								.getDescription());
						sendMsg(connection, t6110.F04, smsUtils.getSendContent(
								envionment.get("title"),
								envionment.get("amount"),
								envionment.get("periods")), type);

					} else {
						String msgContent = configureProvider.format(
								MsgVariavle.TZR_TBSK, envionment);
						sendMsg(connection, t6110.F04, msgContent);
					}
				}

				// 给借款人发信息
				try (PreparedStatement pstmt = connection
						.prepareStatement("SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F06 = ? AND T6252.F09 = ? AND T6252.F03 = ?")) {
					pstmt.setInt(1, t6230.F01);
					pstmt.setInt(2, t6252.F06);
					pstmt.setString(3, T6252_F09.YH.name());
					pstmt.setInt(4, t6252.F03);
					try (ResultSet resultSet = pstmt.executeQuery()) {
						if (resultSet.next()) {
							amountB = resultSet.getBigDecimal(1);
						}
					}
				}

				T6110 t6110Jk = selectT6110(connection, t6252.F03);
				Envionment en = configureProvider.createEnvionment();
				en.set("title", t6230.F03);
				en.set("amount", Formater.formatAmount(amountB));
				en.set("periods", t6252.F06 + "");
				String con = configureProvider.format(LetterVariable.JKR_JKHK,
						en);
				sendLetter(connection, t6252.F03, comment, con);
				if ("1".equals(isUseYtx)) {
					SMSUtils smsUtils = new SMSUtils(configureProvider);
					int type = smsUtils.getTempleId(MsgVariavle.JKR_JKHK
							.getDescription());
					sendMsg(connection,
							t6110Jk.F04,
							smsUtils.getSendContent(en.get("title"),
									en.get("amount"), en.get("periods")), type);
				} else {
					String msgCon = configureProvider.format(
							MsgVariavle.JKR_JKHK, en);
					sendMsg(connection, t6110Jk.F04, msgCon);
				}
				// 还款，下架不良债权
				banBlzq(connection, t6230.F01, t6506.F05);
			}
		} catch (Exception e) {
			logger.error(e, e);
			throw e;
		}
	}

	/**
	 * 还款归还担保方的担保额度
	 * 
	 * @param connection
	 * @param amount
	 * @param dbUserId
	 * @throws Throwable
	 */
	private void rebackAmount(Connection connection, BigDecimal amount,
			int dbUserId) throws Throwable {
		BigDecimal dbTotalAmount = new BigDecimal(0);
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F04 FROM S61.T6125 WHERE T6125.F01 = (SELECT T1.F01 FROM S61.T6125 T1 WHERE T1.F02 = ? LIMIT 1) FOR UPDATE")) {
			pstmt.setInt(1, dbUserId);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					dbTotalAmount = resultSet.getBigDecimal(1);
				}
			}
		}
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S61.T6125 SET F04 = ? WHERE F02 = ?")) {
			pstmt.setBigDecimal(1, dbTotalAmount.add(amount));
			pstmt.setInt(2, dbUserId);
			pstmt.execute();
		}
		try (PreparedStatement pstmt = connection
				.prepareStatement("INSERT INTO S61.T6126 SET F02 = ?, F03 = ?, F04 = ?,F05 = ?, F07 = ?, F08 = ?")) {
			pstmt.setInt(1, dbUserId);
			pstmt.setInt(2, FeeCode.DB_HK_FH);
			pstmt.setTimestamp(3, getCurrentTimestamp(connection));
			pstmt.setBigDecimal(4, amount);
			pstmt.setBigDecimal(5, dbTotalAmount.add(amount));
			pstmt.setString(6, "手动还款担保额度返还");
			pstmt.execute();
		}
	}

	/**
	 * 判断标是否逾期
	 * 
	 * @return
	 * @throws Throwable
	 */
	private boolean selectIsYuqi(SQLConnection connection, int loanId)
			throws Throwable {
		Date currentDate = getCurrentDate(connection);

		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01 FROM S62.T6252 WHERE (T6252.F05 = ? OR T6252.F05=?) AND T6252.F08 < ?  AND T6252.F02 = ?  AND (T6252.F09 = 'WH' OR T6252.F09='HKZ')")) {
			pstmt.setInt(1, FeeCode.TZ_BJ);
			pstmt.setInt(2, FeeCode.TZ_LX);
			pstmt.setDate(3, currentDate);
			pstmt.setInt(4, loanId);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					return true;
				}
			}
		}
		return false;

	}

	@Override
	protected Date getCurrentDate(Connection connection) throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT CURRENT_DATE()")) {
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getDate(1);
				}
			}
		}
		return null;
	}

	private void updateT6231(Connection connection, int F02) throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S62.T6231 SET F13 = ? WHERE F01 = ?")) {
			pstmt.setTimestamp(1, getCurrentTimestamp(connection));
			pstmt.setInt(2, F02);
			pstmt.execute();
		}
	}

	private void updateT6251(Connection connection, BigDecimal F01, int F02)
			throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S62.T6251 SET F07 = ? WHERE F01 = ?")) {
			pstmt.setBigDecimal(1, F01);
			pstmt.setInt(2, F02);
			pstmt.execute();
		}
	}

	private T6251 selectT6251(Connection connection, int F01)
			throws SQLException {
		T6251 record = null;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S62.T6251 WHERE T6251.F01 = ? FOR UPDATE")) {
			pstmt.setInt(1, F01);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					record = new T6251();
					record.F01 = resultSet.getInt(1);
					record.F02 = resultSet.getString(2);
					record.F03 = resultSet.getInt(3);
					record.F04 = resultSet.getInt(4);
					record.F05 = resultSet.getBigDecimal(5);
					record.F06 = resultSet.getBigDecimal(6);
					record.F07 = resultSet.getBigDecimal(7);
					record.F08 = T6251_F08.parse(resultSet.getString(8));
					record.F09 = resultSet.getDate(9);
					record.F10 = resultSet.getDate(10);
					record.F11 = resultSet.getInt(11);
				}
			}
		}
		return record;
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

	private void updateT6252(Connection connection, T6252_F09 F01, int F03)
			throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S62.T6252 SET F09 = ?, F10 = ? WHERE F01 = ?")) {
			pstmt.setString(1, F01.name());
			pstmt.setTimestamp(2, getCurrentTimestamp(connection));
			pstmt.setInt(3, F03);
			pstmt.execute();
		}
	}

	private void updateT6101(Connection connection, BigDecimal F01, int F02)
			throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = ?  WHERE F01 = ?")) {
			pstmt.setBigDecimal(1, F01);
			pstmt.setTimestamp(2, getCurrentTimestamp(connection));
			pstmt.setInt(3, F02);
			pstmt.execute();
		}
	}

	private T6252 selectT6252(Connection connection, int F05, int F06, int F11)
			throws SQLException {
		T6252 record = null;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S62.T6252 WHERE T6252.F05 = ? AND T6252.F06 = ? AND T6252.F11 = ? FOR UPDATE")) {
			pstmt.setInt(1, F05);
			pstmt.setInt(2, F06);
			pstmt.setInt(3, F11);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					record = new T6252();
					record.F01 = resultSet.getInt(1);
					record.F02 = resultSet.getInt(2);
					record.F03 = resultSet.getInt(3);
					record.F04 = resultSet.getInt(4);
					record.F05 = resultSet.getInt(5);
					record.F06 = resultSet.getInt(6);
					record.F07 = resultSet.getBigDecimal(7);
					record.F08 = resultSet.getDate(8);
					record.F09 = T6252_F09.parse(resultSet.getString(9));
					record.F10 = resultSet.getTimestamp(10);
					record.F11 = resultSet.getInt(11);
				}
			}
		}
		return record;
	}

	private void selectT6252ForUpdate(Connection connection, int F02)
			throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11 FROM S62.T6252 WHERE T6252.F02 = ? AND T6252.F09 IN( 'WH','HKZ') FOR UPDATE")) {
			pstmt.setInt(1, F02);
		}
	}

	protected T6506 selectT6506(Connection connection, int F01)
			throws SQLException {
		T6506 record = null;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S65.T6506 WHERE T6506.F01 = ? LIMIT 1")) {
			pstmt.setInt(1, F01);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					record = new T6506();
					record.F01 = resultSet.getInt(1);
					record.F02 = resultSet.getInt(2);
					record.F03 = resultSet.getInt(3);
					record.F04 = resultSet.getInt(4);
					record.F05 = resultSet.getInt(5);
					record.F06 = resultSet.getBigDecimal(6);
					record.F07 = resultSet.getInt(7);
				}
			}
		}
		return record;
	}

	protected T6253 selectT6253(Connection connection, int loanId)
			throws SQLException {
		T6253 record = null;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S62.T6253 WHERE T6253.F02 = ? LIMIT 1")) {
			pstmt.setInt(1, loanId);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					record = new T6253();
					record.F01 = resultSet.getInt(1);
					record.F02 = resultSet.getInt(2);
					record.F03 = resultSet.getInt(3);
					record.F04 = resultSet.getInt(4);
					record.F05 = resultSet.getBigDecimal(5);
					record.F06 = resultSet.getBigDecimal(6);
					record.F07 = resultSet.getTimestamp(7);
				}
			}
		}
		return record;
	}

	protected T6230 selectT6230(Connection connection, int F01)
			throws SQLException {
		T6230 record = null;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24, F25, F26, F27 FROM S62.T6230 WHERE T6230.F01 = ? FOR UPDATE")) {
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

	protected T6238 selectT6238(Connection connection, int F01)
			throws SQLException {
		T6238 record = null;
		try (PreparedStatement pstmt = connection
				.prepareStatement("SELECT F01, F02, F03, F04 FROM S62.T6238 WHERE T6238.F01 = ? ")) {
			pstmt.setInt(1, F01);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					record = new T6238();
					record.F01 = resultSet.getInt(1);
					record.F02 = resultSet.getBigDecimal(2);
					record.F03 = resultSet.getBigDecimal(3);
					record.F04 = resultSet.getBigDecimal(4);
				}
			}
		}
		return record;
	}

	@Override
	protected void doSubmit(SQLConnection connection, int orderId,
			Map<String, String> params) throws Throwable {
	}

	@Override
	protected void updateSubmit(Connection connection, T6501_F03 t6501_F03,
			int F02) throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F05 = ? WHERE F01 = ?")) {
			pstmt.setString(1, t6501_F03.name());
			pstmt.setTimestamp(2, getCurrentTimestamp(connection));
			pstmt.setInt(3, F02);
			pstmt.execute();
		}
	}

	/**
	 * <还款，下架不良债权> <功能详细描述>
	 * 
	 * @param connection
	 * @param bidId
	 * @param t6252F06
	 */
	private void banBlzq(Connection connection, int bidId, int t6252F06)
			throws Throwable {
		T6264 t6264 = getT6264ForUpdate(connection, bidId, t6252F06);
		if (null != t6264) {
			Timestamp newTime = getCurrentTimestamp(connection);
			t6264.F04 = T6264_F04.YXJ;
			t6264.F11 = "自动下架：手动还款";
			updateT6264(connection, t6264, newTime);
		}
	}

	private T6264 getT6264ForUpdate(Connection connection, int bidId,
			int t6252F06) throws Throwable {

		final StringBuilder sql = new StringBuilder(
				"SELECT T6264.F01 F01,T6264.F03 F02,T6264.F04 F03,T6264.F06 F04,");
		sql.append("(SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6264.F03 AND T6252.F06 >=? AND T6252.F05 IN ( '7001', '7002', '7003', '7004' )) F05,");
		sql.append("(SELECT (CASE T6230.F11 WHEN 'S' THEN CASE T6230.F12 WHEN 'BXQEDB' THEN (SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F06 >=? AND T6252.F05 IN ( '7001', '7002', '7003', '7004' )) ELSE (SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F06 >=? AND T6252.F05 = '7001') END ELSE (SELECT IFNULL(SUM(T6252.F07),0) FROM S62.T6252 WHERE T6252.F02 = T6230.F01 AND T6252.F06 >=? AND T6252.F05 IN ( '7001', '7002', '7003', '7004' )) END) FROM S62.T6230 WHERE T6230.F01=T6264.F03) F06,");
		sql.append("(SELECT DATEDIFF(?, T6252.F08) FROM S62.T6252 WHERE T6252.F01=T6264.F06) F07 ");
		sql.append("FROM S62.T6264 WHERE T6264.F01=(SELECT T6264.F01 FROM S62.T6264 WHERE T6264.F03=? AND T6264.F04 IN (?,?) LIMIT 1) FOR UPDATE");
		T6264 t6264 = null;
		try (PreparedStatement pstmt = connection.prepareStatement(sql
				.toString())) {
			pstmt.setInt(1, t6252F06);
			pstmt.setInt(2, t6252F06);
			pstmt.setInt(3, t6252F06);
			pstmt.setInt(4, t6252F06);
			pstmt.setDate(5, getCurrentDate(connection));
			pstmt.setInt(6, bidId);
			pstmt.setString(7, T6264_F04.DSH.name());
			pstmt.setString(8, T6264_F04.ZRZ.name());
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					t6264 = new T6264();
					t6264.F01 = resultSet.getInt(1);
					t6264.F03 = resultSet.getInt(2);
					t6264.F04 = T6264_F04.parse(resultSet.getString(3));
					t6264.F06 = resultSet.getInt(4);
					t6264.F09 = resultSet.getBigDecimal(5);
					t6264.F10 = resultSet.getBigDecimal(6);
					t6264.F05 = resultSet.getInt(7);
				}
			}
		}

		return t6264;
	}

	private void updateT6264(Connection connection, T6264 t6264,
			Timestamp newTime) throws Throwable {
		try (PreparedStatement pstmt = connection
				.prepareStatement("UPDATE S62.T6264 SET F04 = ?, F05 = ?, F08 = ?, F09 = ?, F10 = ?, F11 = ? WHERE F01 = ?")) {
			pstmt.setString(1, t6264.F04.name());
			pstmt.setInt(2, t6264.F05);
			pstmt.setTimestamp(3, newTime);
			pstmt.setBigDecimal(4, t6264.F09);
			pstmt.setBigDecimal(5, t6264.F10);
			pstmt.setString(6, t6264.F11);
			pstmt.setInt(7, t6264.F01);
			pstmt.execute();
		}
	}
}