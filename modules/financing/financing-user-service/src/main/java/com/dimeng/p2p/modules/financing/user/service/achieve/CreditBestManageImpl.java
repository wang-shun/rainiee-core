package com.dimeng.p2p.modules.financing.user.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.common.enums.EarningsWay;
import com.dimeng.p2p.common.enums.PlanState;
import com.dimeng.p2p.common.enums.RepayStatus;
import com.dimeng.p2p.modules.financing.user.service.CreditBestManage;
import com.dimeng.p2p.modules.financing.user.service.entity.CreditBest;
import com.dimeng.p2p.modules.financing.user.service.entity.UserInfo;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.util.parser.EnumParser;

public class CreditBestManageImpl extends AbstractFinancingManage implements
		CreditBestManage {

	public CreditBestManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	public static class CreditBestManageFactory implements
			ServiceFactory<CreditBestManage> {
		@Override
		public CreditBestManage newInstance(ServiceResource serviceResource) {
			return new CreditBestManageImpl(serviceResource);
		}

	}

	@Override
	public void BidBest(int yxlcId, BigDecimal tbMoney) throws Throwable {

		if (yxlcId < 0) {
			throw new ParameterException("指定的记录不存在");
		}
		if (tbMoney.compareTo(BigDecimal.ZERO) < 0) {
			throw new ParameterException("投资的金额参数异常");
		}
		/**
		 * 判断是否逾期
		 */
		try (Connection conn = getConnection()) {
			try {
				serviceResource.openTransactions(conn);
				if (isYuqi(conn)) {
					throw new LogicalException("您有逾期未还的借款，请先还完再操作。");
				}

				CreditBest creditBest = null;
				UserInfo userInfo = null;
				int loginId = serviceResource.getSession().getAccountId();

				try (PreparedStatement ps = conn.prepareStatement("SELECT F05 FROM S60.T6023 WHERE F01 = ? FOR UPDATE");) {
					ps.setInt(1, loginId);
					try (ResultSet rs = ps.executeQuery();) {
						if (rs.next()) {
							if (userInfo == null) {
								userInfo = new UserInfo();
							}
							userInfo.kyMoney = rs.getBigDecimal(1);
						}
					}
				}
				/**
				 * 查询还需金额
				 */
				try (PreparedStatement ps = conn
						.prepareStatement("SELECT F04,F09,F23,F22,F07,F15,F16,F05,F11,F14,F25 FROM S60.T6042 WHERE F01 = ? FOR UPDATE");) {
					ps.setInt(1, yxlcId);
					try (ResultSet rs = ps.executeQuery();) {
						if (rs.next()) {
							if (creditBest == null) {
								creditBest = new CreditBest();
							}
							creditBest.remainAmount = rs.getBigDecimal(1);
							creditBest.sqTime = rs.getTimestamp(2);
							creditBest.maxBidMoney = rs.getBigDecimal(3);
							creditBest.minBidMoney = rs.getBigDecimal(4);
							creditBest.planState = EnumParser.parse(PlanState.class, rs.getString(5));
							creditBest.jrfl = rs.getBigDecimal(6);
							creditBest.fwfl = rs.getBigDecimal(7);
							creditBest.rate = rs.getBigDecimal(8);
							creditBest.lockQx = rs.getInt(9);
							creditBest.earningsWay = EnumParser.parse(EarningsWay.class, rs.getString(10));
							creditBest.planMoney = rs.getBigDecimal(11);
						}
					}
				}
				// 加入费
				BigDecimal jrssf = new BigDecimal(0);
				if (creditBest.jrfl.compareTo(BigDecimal.ZERO) > 0) {
					jrssf = tbMoney.multiply(creditBest.jrfl);
				}
				BigDecimal totleTb = jrssf.add(tbMoney);
				BigDecimal tzMoney = new BigDecimal(0);
				try (PreparedStatement ps = conn
						.prepareStatement("SELECT F04 FROM S60.T6043 WHERE F02 = ? AND F03 = ? FOR UPDATE");) {
					ps.setInt(1, yxlcId);
					ps.setInt(2, loginId);
					try (ResultSet rs = ps.executeQuery();) {
						if (rs.next()) {
							tzMoney = rs.getBigDecimal(1);
						}
					}
				}

				if (userInfo.kyMoney.doubleValue() < totleTb.doubleValue()) {
					throw new LogicalException("你的账户余额不足进行本次投资 ，请充值");
				}
				if (creditBest.remainAmount.doubleValue() < tbMoney.doubleValue()) {
					throw new LogicalException("你的投资金额大于计划的剩余投资金额");
				}
				if (creditBest.maxBidMoney.doubleValue() < tbMoney.add(tzMoney).doubleValue()) {
					throw new LogicalException("你的投资已经超出计划的个人投资最大限额");
				}
				if (tbMoney.doubleValue() < creditBest.minBidMoney.doubleValue()) {
					throw new LogicalException("你的投资金额低于计划的最低投资金额");
				}
				if (creditBest.planState != PlanState.YFB) {
					throw new LogicalException("当前状态不允许申请");
				}

				/**
				 * 操作用户账户信息
				 */
				try (PreparedStatement ps = conn
						.prepareStatement("UPDATE S60.T6023 SET F05 = F05 - ? , F04 = F04 + ? WHERE F01 = ?");) {
					ps.setBigDecimal(1, totleTb);
					ps.setBigDecimal(2, totleTb);
					ps.setInt(3, loginId);
					ps.executeUpdate();
				}
				/**
				 * 操作优选理财持有人表
				 */
				try (PreparedStatement ps = conn
						.prepareStatement("INSERT INTO S60.T6043(F02,F03,F04,F05) VALUES (?,?,?,SYSDATE()) ON DUPLICATE KEY UPDATE F04 = F04 + ?, F06 = SYSDATE()");) {
					ps.setInt(1, yxlcId);
					ps.setInt(2, loginId);
					ps.setBigDecimal(3, tbMoney);
					ps.setBigDecimal(4, tbMoney);
					ps.execute();
				}
				/**
				 * 更新优选理财计划表
				 */
				try (PreparedStatement ps = conn.prepareStatement("UPDATE S60.T6042 SET F04 = F04 - ? WHERE F01 = ?");) {
					ps.setBigDecimal(1, tbMoney);
					ps.setInt(2, yxlcId);
					ps.executeUpdate();
				}

				/**
				 * 判断是否满标
				 */
				try (PreparedStatement ps = getConnection(P2PConst.DB_CONSOLE).prepareCall("CALL SP_T6042()")) {
					ps.execute();
				}

				serviceResource.commit(conn);
			} catch (Exception e) {
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
		String sql = "SELECT DATEDIFF(?,F10) FROM T6041 WHERE F12=? AND F03=? AND F10 < SYSDATE()";
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setTimestamp(1,getCurrentTimestamp(connection));
            ps.setString(2, RepayStatus.WH.name());
            ps.setInt(3, serviceResource.getSession().getAccountId());
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
