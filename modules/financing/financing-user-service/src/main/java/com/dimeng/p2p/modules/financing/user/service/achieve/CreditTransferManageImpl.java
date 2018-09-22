package com.dimeng.p2p.modules.financing.user.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S60.enums.T6038_F07;
import com.dimeng.p2p.common.enums.DsStatus;
import com.dimeng.p2p.common.enums.IssueState;
import com.dimeng.p2p.common.enums.PlatformFundType;
import com.dimeng.p2p.common.enums.RepayStatus;
import com.dimeng.p2p.common.enums.TradingType;
import com.dimeng.p2p.common.enums.TransferState;
import com.dimeng.p2p.common.enums.TransferStatus;
import com.dimeng.p2p.modules.financing.user.service.CreditTransferManage;
import com.dimeng.p2p.modules.financing.user.service.entity.CreditHk;
import com.dimeng.p2p.modules.financing.user.service.entity.CreditInfo;
import com.dimeng.p2p.modules.financing.user.service.entity.CreditTransfer;
import com.dimeng.p2p.variables.defines.SystemVariable;

public class CreditTransferManageImpl extends AbstractFinancingManage implements
		CreditTransferManage {

	public CreditTransferManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	public static class CreditTransferManageFactory implements
			ServiceFactory<CreditTransferManage> {
		@Override
		public CreditTransferManage newInstance(ServiceResource serviceResource) {
			return new CreditTransferManageImpl(serviceResource);
		}

	}

	final ConfigureProvider configureProvider = serviceResource
			.getResource(ConfigureProvider.class);

	@Override
	public void Transfer(int zqzcId, int amount) throws Throwable {

		if (zqzcId < 0) {
			throw new ParameterException("指定的内容不存在");
		}
		if (amount <= 0) {
			throw new ParameterException("投资的数量参数异常");
		}
		try(Connection conn = getConnection())
		{
			/**
			 * 判断是否逾期
			 */
			if (isYuqi(conn)) {
				throw new LogicalException("您有逾期未还的借款，请先还完再操作。");
			}

			CreditTransfer creditTransfer = null;// 债权转让信息
			BigDecimal ptTotleMoney = new BigDecimal(0);// 平台资金

			int loginId = serviceResource.getSession().getAccountId();
			BigDecimal gmzKyMoney = new BigDecimal(0);// 购买者可用金额
			BigDecimal gmzTotleMoney = new BigDecimal(0);// 购买者余额

			BigDecimal mczKyMoney = new BigDecimal(0);// 卖出者可用金额
			BigDecimal mczTotleMoney = new BigDecimal(0);// 卖出者余额
			Timestamp time = getCurrentTimestamp(conn);

			/**
			 * 锁定债权转让表
			 */
			try {
				serviceResource.openTransactions(conn);
				try (PreparedStatement ps = conn
						.prepareStatement("SELECT F06,F08,F04,F03,F05,F02,F11,F12 FROM T6039 WHERE F01 = ? FOR UPDATE");) {
					ps.setInt(1, zqzcId);
					try (ResultSet rs = ps.executeQuery();) {
						if (rs.next()) {
							if (creditTransfer == null) {
								creditTransfer = new CreditTransfer();
							}
							creditTransfer.transerMoney = rs.getBigDecimal(1);
							creditTransfer.overCount = rs.getInt(2);
							creditTransfer.sellUserId = rs.getInt(3);
							creditTransfer.jkbId = rs.getInt(4);
							creditTransfer.creditMoney = rs.getBigDecimal(5);
							creditTransfer.zqId = rs.getInt(6);
							creditTransfer.transferState = Enum.valueOf(
									TransferState.class, rs.getString(7));
							creditTransfer.issueState = Enum.valueOf(
									IssueState.class, rs.getString(8));
						}
					}
				}

				if (creditTransfer.sellUserId == loginId) {
					throw new LogicalException("不能购买自己的债权");
				}

				CreditInfo creditInfo = null;
				/**
				 * 标的信息
				 */
				try (PreparedStatement ps = conn
						.prepareStatement("SELECT F09,F08,F37 FROM S60.T6036 WHERE F01 = ?");) {
					ps.setInt(1, creditTransfer.jkbId);
					try (ResultSet rs = ps.executeQuery();) {
						if (rs.next()) {
							if (creditInfo == null) {
								creditInfo = new CreditInfo();
							}
							creditInfo.rate = rs.getDouble(1);
							creditInfo.jkqx = rs.getInt(2);
							creditInfo.perAmount = rs.getBigDecimal(3);
						}
					}
				}

				/**
				 * 锁定购买者账号信息表
				 */
				try (PreparedStatement ps = conn
						.prepareStatement("SELECT F05,F03 FROM S60.T6023 WHERE F01 = ? FOR UPDATE");) {
					ps.setInt(1, loginId);
					try (ResultSet rs = ps.executeQuery();) {
						if (rs.next()) {
							gmzKyMoney = rs.getBigDecimal(1);
							gmzTotleMoney = rs.getBigDecimal(2);
						}
					}
				}
				/**
				 * 锁定卖出者账号信息表
				 */
				try (PreparedStatement ps = conn
						.prepareStatement("SELECT F05,F03 FROM S60.T6023 WHERE F01 = ? FOR UPDATE");) {
					ps.setInt(1, creditTransfer.sellUserId);
					try (ResultSet rs = ps.executeQuery();) {
						if (rs.next()) {
							mczKyMoney = rs.getBigDecimal(1);
							mczTotleMoney = rs.getBigDecimal(2);
						}
					}
				}

				/**
				 * 锁定卖出者债权转让统计表
				 */
				try (PreparedStatement ps = conn
						.prepareStatement("SELECT F01 FROM S60.T6027 WHERE F01 = ? FOR UPDATE");) {
					ps.setInt(1, creditTransfer.sellUserId);
					ps.executeQuery();
				}

				/**
				 * 锁定买入者债权转让统计表
				 */
				try (PreparedStatement ps = conn
						.prepareStatement("SELECT F01 FROM S60.T6027 WHERE F01 = ? FOR UPDATE");) {
					ps.setInt(1, loginId);
					ps.executeQuery();
				}

				/**
				 * 锁定卖出者债权表
				 */
				try (PreparedStatement ps = conn
						.prepareStatement("SELECT F01 FROM S60.T6038 WHERE F02 =? AND F03 =? FOR UPDATE");) {
					ps.setInt(1, creditTransfer.jkbId);
					ps.setInt(2, creditTransfer.sellUserId);
					ps.executeQuery();
				}

				/**
				 * 锁定购买者债权表
				 */
				try (PreparedStatement ps = conn
						.prepareStatement("SELECT F01 FROM S60.T6038 WHERE F02 =? AND F03 =? FOR UPDATE");) {
					ps.setInt(1, creditTransfer.jkbId);
					ps.setInt(2, loginId);
					ps.executeQuery();
				}

				/**
				 * 锁定卖出者收款纪录表
				 */
				try (PreparedStatement ps = conn
						.prepareStatement("SELECT F01 FROM S60.T6056 WHERE F02 = ? AND F10 = ? AND F03=? FOR UPDATE");) {
					ps.setInt(1, creditTransfer.jkbId);
					ps.setString(2, DsStatus.WS.name());
					ps.setInt(3, creditTransfer.sellUserId);
					ps.executeQuery();
				}
				/**
				 * 锁定购买者收款纪录表
				 */
				try (PreparedStatement ps = conn
						.prepareStatement("SELECT F01 FROM S60.T6056 WHERE F02 = ? AND F10 = ? AND F03=? FOR UPDATE");) {
					ps.setInt(1, creditTransfer.jkbId);
					ps.setString(2, DsStatus.WS.name());
					ps.setInt(3, loginId);
					ps.executeQuery();
				}

				/**
				 * 锁定平台资金表
				 */
				try (PreparedStatement ps = conn
						.prepareStatement("SELECT F01 FROM S70.T7025 LIMIT 1 FOR UPDATE");) {
					try (ResultSet rs = ps.executeQuery();) {
						if (rs.next()) {
							ptTotleMoney = rs.getBigDecimal(1);
						}
					}
				}

				/**
				 * 计算购买的金额
				 */
				BigDecimal buyAmount = creditTransfer.transerMoney
						.multiply(new BigDecimal(amount));
				/**
				 * 计算债权的总金额
				 */
				BigDecimal totleAmount = creditTransfer.creditMoney
						.multiply(new BigDecimal(amount));
				/**
				 * 判断账户余额是否大于购买金额
				 */
				if (gmzKyMoney.compareTo(buyAmount) < 0) {
					throw new LogicalException("你的账户余额不足进行本次购买 ，请充值");
				}

				/**
				 * 判断剩余金额是否大于购买金额
				 */
				if (creditTransfer.overCount < amount) {
					throw new LogicalException("你的投资份数大于债权的剩余份数！");
				}

				if (creditTransfer.issueState == IssueState.F
						|| creditTransfer.transferState == TransferState.WX) {
					throw new LogicalException("该债权已下架！");
				}

				/**
				 * 操作购买用户账户信息（支出）
				 */
				gmzTotleMoney = gmzTotleMoney.subtract(buyAmount);
				gmzKyMoney = gmzKyMoney.subtract(buyAmount);
				try (PreparedStatement ps = conn
						.prepareStatement("UPDATE S60.T6023 SET F05 = ? , F03 = ? WHERE F01 = ?");) {
					ps.setBigDecimal(1, gmzKyMoney);
					ps.setBigDecimal(2, gmzTotleMoney);
					ps.setInt(3, loginId);
					ps.executeUpdate();
				}
				/**
				 * 购买者资金交易记录新增一条记录（购买债权支出）
				 */
				try (PreparedStatement ps = conn
						.prepareStatement("INSERT INTO S60.T6032 (F02,F03,F04,F06,F07,F08,F09) VALUES(?,?,?,?,?,?,?)");) {
					ps.setInt(1, loginId);
					ps.setString(2, TradingType.GMZQ.name());
					ps.setTimestamp(3,time);
					ps.setBigDecimal(4, buyAmount);
					ps.setBigDecimal(5, gmzTotleMoney);
					ps.setInt(6, zqzcId);
					ps.setString(7, "购买债权");
					ps.execute();
				}
				/**
				 * 操作转让者用户账户信息（收入）
				 */
				mczTotleMoney = mczTotleMoney.add(buyAmount);
				mczKyMoney = mczKyMoney.add(buyAmount);
				try (PreparedStatement ps = conn
						.prepareStatement("UPDATE S60.T6023 SET F05 =  ? , F03 =  ? WHERE F01 = ?");) {
					ps.setBigDecimal(1, mczKyMoney);
					ps.setBigDecimal(2, mczTotleMoney);
					ps.setInt(3, creditTransfer.sellUserId);
					ps.executeUpdate();
				}
				/**
				 * 转让者资金交易记录新增一条记录（出售债权收入）
				 */
				try (PreparedStatement ps = conn
						.prepareStatement("INSERT INTO S60.T6032 (F02,F03,F04,F05,F07,F08,F09) VALUES(?,?,?,?,?,?,?)");) {
					ps.setInt(1, creditTransfer.sellUserId);
					ps.setString(2, TradingType.CSZQ.toString());
					ps.setTimestamp(3,time);
					ps.setBigDecimal(4, buyAmount);
					ps.setBigDecimal(5, mczTotleMoney);
					ps.setInt(6, zqzcId);
					ps.setString(7, "出售债权");
					ps.execute();
				}

				/**
				 * 更新债权转让转出表信息
				 */
				try (PreparedStatement ps = conn
						.prepareStatement("UPDATE S60.T6039 SET F08 = F08 - ? ,F10 = SYSDATE() WHERE F01 = ?");) {
					ps.setLong(1, amount);
					ps.setInt(2, zqzcId);
					ps.executeUpdate();
				}

				/**
				 * 计算债权转让费
				 */
				BigDecimal zrfwRate = buyAmount.multiply(new BigDecimal(
						configureProvider.getProperty(SystemVariable.ZQZRGLF_RATE)));
				/**
				 * 操作转让者用户账户信息（支出转让服务费）
				 */
				mczTotleMoney = mczTotleMoney.subtract(zrfwRate);
				mczKyMoney = mczKyMoney.subtract(zrfwRate);
				try (PreparedStatement ps = conn
						.prepareStatement("UPDATE S60.T6023 SET F05 =  ? , F03 =  ? WHERE F01 = ?");) {
					ps.setBigDecimal(1, mczTotleMoney);
					ps.setBigDecimal(2, mczKyMoney);
					ps.setInt(3, creditTransfer.sellUserId);
					ps.executeUpdate();
				}
				/**
				 * 转让者资金交易记录新增一条记录（支出服务管理费）
				 */
				try (PreparedStatement ps = conn
						.prepareStatement("INSERT INTO S60.T6032 (F02,F03,F04,F06,F07,F08,F09) VALUES(?,?,?,?,?,?,?)");) {
					ps.setInt(1, creditTransfer.sellUserId);
					ps.setString(2, TradingType.ZQZRSXF.name());
					ps.setTimestamp(3,time);
					ps.setBigDecimal(4, zrfwRate);
					ps.setBigDecimal(5, mczTotleMoney);
					ps.setInt(6, zqzcId);
					ps.setString(7, "出售债权服务管理费");
					ps.execute();
				}
				/**
				 * 操作平台账户表信息（收入）
				 */
				ptTotleMoney = ptTotleMoney.add(zrfwRate);
				try (PreparedStatement ps = conn
						.prepareStatement("UPDATE S70.T7025 SET F01 = ? , F03 = F03 + ?  , F04 = F04 + ?");) {
					ps.setBigDecimal(1, ptTotleMoney);
					ps.setBigDecimal(2, zrfwRate);
					ps.setBigDecimal(3, zrfwRate);
					ps.executeUpdate();
				}
				/**
				 * 平台资金流水表新增一条记录（收入）
				 */
				try (PreparedStatement ps = conn
						.prepareStatement("INSERT INTO S70.T7026 (F02,F03,F05,F06,F07,F08,F09) VALUES(?,?,?,?,?,?,?)");) {
					ps.setTimestamp(1,time);
					ps.setBigDecimal(2, zrfwRate);
					ps.setBigDecimal(3, ptTotleMoney);
					ps.setString(4, PlatformFundType.ZQZRFY.name());
					ps.setString(5, "债权转让费");
					ps.setInt(6, zqzcId);
					ps.setInt(7, creditTransfer.sellUserId);
					ps.execute();
				}

				/**
				 * 债权转让转入表新增一条记录
				 */
				try (PreparedStatement ps = conn
						.prepareStatement("INSERT INTO S60.T6040 (F02,F03,F04,F05,F06) VALUES(?,?,?,?,?)");) {
					ps.setInt(1, zqzcId);
					ps.setInt(2, loginId);
					ps.setLong(3, amount);
					ps.setTimestamp(4,time);
					ps.setBigDecimal(5, zrfwRate);
					ps.execute();
				}

				/**
				 * 操作最新债权持有人列表（卖出者）
				 */
				try (PreparedStatement ps = conn
						.prepareStatement("UPDATE S60.T6038 SET F04 = F04 - ?, F10= F10 - ? , F08 = F08 - ? WHERE F02 = ? AND F03 = ?");) {
					ps.setBigDecimal(1, totleAmount);
					ps.setBigDecimal(2, buyAmount);
					ps.setInt(3, amount);
					ps.setInt(4, creditTransfer.jkbId);
					ps.setInt(5, creditTransfer.sellUserId);
					ps.execute();
				}
				/**
				 * 操作最新债权持有人列表（购买者）
				 */
				try (PreparedStatement ps = conn
						.prepareStatement("INSERT INTO S60.T6038 (F02,F03 ,F04 ,F05,F08 ,F10 ) VALUES (?, ?, ?, ?, ?, ?) "
								+ " ON DUPLICATE KEY UPDATE F04 = F04 + ?,F08 = F08 + ?,F10 = F10 + ? ")) {
					ps.setInt(1, creditTransfer.jkbId);
					ps.setInt(2, loginId);
					ps.setBigDecimal(3, totleAmount);
					ps.setTimestamp(4,time);
					ps.setInt(5, amount);
					ps.setBigDecimal(6, buyAmount);
					ps.setBigDecimal(7, totleAmount);
					ps.setInt(8, amount);
					ps.setBigDecimal(9, buyAmount);
					ps.execute();
				}

				/**
				 * 卖出者持有份数
				 */
				int mczNum = 0;
				try (PreparedStatement ps = conn
						.prepareStatement("SELECT F08 FROM S60.T6038 WHERE F02 = ? AND F03 = ?");) {
					ps.setInt(1, creditTransfer.jkbId);
					ps.setInt(2, creditTransfer.sellUserId);
					try (ResultSet rs = ps.executeQuery();) {
						if (rs.next()) {
							mczNum = rs.getInt(1);
						}
					}
				}

				/**
				 * 购买者持有份数
				 */
				int gmzNum = 0;
				try (PreparedStatement ps = conn
						.prepareStatement("SELECT F08 FROM S60.T6038 WHERE F02 = ? AND F03 = ?");) {
					ps.setInt(1, creditTransfer.jkbId);
					ps.setInt(2, loginId);
					try (ResultSet rs = ps.executeQuery();) {
						if (rs.next()) {
							gmzNum = rs.getInt(1);
						}
					}
				}

				/**
				 * 债权未还记录
				 */
				List<CreditHk> hkList = new ArrayList<CreditHk>();
				try (PreparedStatement ps = conn
						.prepareStatement("SELECT F04,F08,F12,F13 FROM S60.T6056 WHERE F02 = ? AND F10 = ? AND F03=?");) {
					ps.setInt(1, creditTransfer.jkbId);
					ps.setString(2, DsStatus.WS.name());
					ps.setInt(3, creditTransfer.sellUserId);
					try (ResultSet rs = ps.executeQuery();) {
						while (rs.next()) {
							CreditHk creditHk = new CreditHk();
							creditHk.qh = rs.getInt(1);
							creditHk.ysrq = rs.getDate(2);
							creditHk.mfysbj = rs.getBigDecimal(3);
							creditHk.mfyslx = rs.getBigDecimal(4);
							hkList.add(creditHk);
						}
					}
				}

				for (CreditHk hk : hkList) {
					// 卖出者
					BigDecimal mczdsbj = hk.mfysbj.multiply(new BigDecimal(mczNum));
					BigDecimal mczdslx = hk.mfyslx.multiply(new BigDecimal(mczNum));
					try (PreparedStatement ps = conn
							.prepareStatement("UPDATE S60.T6056 SET F05 =  ?, F06 = ?, F11 = ? WHERE F02 = ? AND F03 = ? AND F04 = ?");) {
						ps.setBigDecimal(1, mczdsbj);
						ps.setBigDecimal(2, mczdslx);
						ps.setInt(3, mczNum);
						ps.setInt(4, creditTransfer.jkbId);
						ps.setInt(5, creditTransfer.sellUserId);
						ps.setInt(6, hk.qh);
						ps.execute();
					}
					// 购买者
					BigDecimal gmzdsbj = hk.mfysbj.multiply(new BigDecimal(gmzNum));
					BigDecimal gmzdslx = hk.mfyslx.multiply(new BigDecimal(gmzNum));
					try (PreparedStatement ps = conn
							.prepareStatement("INSERT INTO S60.T6056 (F02 ,F03,F04,F05,F06,F08,F10,F11,F12 ,F13 ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
									+ " ON DUPLICATE KEY UPDATE F05 = ? ,F06 = ?, F11 = ?")) {
						ps.setInt(1, creditTransfer.jkbId);
						ps.setInt(2, loginId);
						ps.setInt(3, hk.qh);
						ps.setBigDecimal(4, gmzdsbj);
						ps.setBigDecimal(5, gmzdslx);
						ps.setDate(6, hk.ysrq);
						ps.setString(7, DsStatus.WS.name());
						ps.setInt(8, gmzNum);

						ps.setBigDecimal(9, hk.mfysbj);
						ps.setBigDecimal(10, hk.mfyslx);

						ps.setBigDecimal(11, gmzdsbj);
						ps.setBigDecimal(12, gmzdslx);
						ps.setInt(13, gmzNum);

						ps.execute();
					}
				}

				/**
				 * 判断是否转让完
				 */
				try (PreparedStatement ps = conn
						.prepareStatement("SELECT F08 FROM S60.T6039 WHERE F01 = ? FOR UPDATE");) {
					ps.setInt(1, zqzcId);
					try (ResultSet rs = ps.executeQuery();) {
						if (rs.next()) {
							if (rs.getInt(1) <= 0) {
								try (PreparedStatement pss = conn
										.prepareStatement("UPDATE T6039 SET F11 =  ? WHERE F01 = ?");) {
									pss.setString(1, TransferStatus.WX.name());
									pss.setInt(2, zqzcId);
									pss.executeUpdate();
								}
								try (PreparedStatement pss = conn
										.prepareStatement("UPDATE T6038 SET F07 =  ? WHERE F02 = ? AND F03 = ?");) {
									pss.setString(1, T6038_F07.F.name());
									pss.setInt(2, creditTransfer.jkbId);
									pss.setInt(3, creditTransfer.sellUserId);
									pss.executeUpdate();
								}

							}
						}
					}
				}

				/**
				 * 债权转让统计表更新一条记录（转出者）
				 */
				String sql = "UPDATE S60.T6027 SET F02 = F02 + ?, F06 = F06 + ?, F07 = F07 + ?, T6027.F08 = T6027.F08 + 1 WHERE T6027.F01 = ?";
				try (PreparedStatement ps = conn.prepareStatement(sql);) {
					ps.setBigDecimal(1,
							buyAmount.subtract(totleAmount.add(zrfwRate)));
					ps.setBigDecimal(2, totleAmount);
					ps.setBigDecimal(3,
							buyAmount.subtract(totleAmount.add(zrfwRate)));
					ps.setInt(4, creditTransfer.sellUserId);
					ps.execute();
				}

				/**
				 * 债权转让统计表更新一条记录（转入者）
				 */
				sql = "UPDATE S60.T6027 SET T6027.F02 = T6027.F02 + ?, T6027.F03 = T6027.F03 + ?, T6027.F04 = T6027.F04 + ?, T6027.F05 = T6027.F05 + 1 WHERE T6027.F01 = ?";
				try (PreparedStatement ps = conn.prepareStatement(sql);) {
					ps.setBigDecimal(1, totleAmount.subtract(buyAmount));
					ps.setBigDecimal(2, totleAmount);
					ps.setBigDecimal(3, totleAmount.subtract(buyAmount));
					ps.setInt(4, loginId);
					ps.execute();
				}
				serviceResource.commit(conn);
			}
			catch (Exception e)
			{
				serviceResource.rollback(conn);
				throw e;
			}
		}
	}

	/**
	 * 是否逾期逾期
	 */
    private boolean isYuqi(Connection connection)
        throws Throwable
    {
		String sql = "SELECT DATEDIFF(NOW(),F10) FROM T6041 WHERE F12=? AND F03=? AND F10 < SYSDATE()";
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setString(1, RepayStatus.WH.name());
            ps.setInt(2, serviceResource.getSession().getAccountId());
            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    int days = rs.getInt(1);
                    if (days > 0)
                    {
                        return true;
					}
				}
			}
		}
		return false;
	}

}