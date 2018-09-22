package com.dimeng.p2p.modules.financing.user.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.common.enums.CreditStatus;
import com.dimeng.p2p.common.enums.RepayStatus;
import com.dimeng.p2p.modules.financing.user.service.CreditBuyManage;
import com.dimeng.p2p.modules.financing.user.service.entity.CreditInfo;
import com.dimeng.p2p.modules.financing.user.service.entity.UserInfo;
import com.dimeng.util.parser.EnumParser;

public class CreditBuyManageImpl extends AbstractFinancingManage implements CreditBuyManage {

	public CreditBuyManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}
	
	public static class BidProcessManageFactory implements
	ServiceFactory<CreditBuyManage> {
		@Override
		public CreditBuyManage newInstance(ServiceResource serviceResource) {
			return new CreditBuyManageImpl(serviceResource);
		}
		
	}
	
	
	/**
	 * 是否逾期逾期
	 */
	private boolean isYuqi() throws Throwable{
		String sql = "SELECT DATEDIFF(?,F10) FROM T6041 WHERE F12=? AND F03=? AND F10 < SYSDATE()";
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.setTimestamp(1,getCurrentTimestamp(connection));
				ps.setString(2, RepayStatus.WH.name());
				ps.setInt(3, serviceResource.getSession().getAccountId());
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						int days = rs.getInt(1);
						if (days > 0) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	
	@Override
	public void Bid(int loanId, long amount) throws Throwable {

		if (loanId < 0) {
			throw new ParameterException("指定的记录不存在");
		}
		if (amount <= 0) {
			throw new ParameterException("投资的数量参数异常");
		}

		/**
		 * 判断是否逾期
		 */
		if (isYuqi()) {
			throw new LogicalException("您有逾期未还的借款，请先还完再操作。");
		}

		UserInfo userInfo = null;
		int loginId = serviceResource.getSession().getAccountId();

		/**
		 * 交易密码和可用金额
		 */
		try (Connection conn = getConnection()) {
			try {
				serviceResource.openTransactions(conn);
				try (PreparedStatement ps = conn.prepareStatement("SELECT F05 FROM T6023 WHERE F01 = ? FOR UPDATE")) {
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

				CreditInfo creditInfo = null;
				/**
				 * 查询还需金额和每份金额
				 */

				try (PreparedStatement ps = conn
						.prepareStatement("SELECT F07,F37,F20 FROM T6036 WHERE F01 = ? FOR UPDATE")) {
					ps.setInt(1, loanId);
					try (ResultSet rs = ps.executeQuery();) {
						if (rs.next()) {
							if (creditInfo == null) {
								creditInfo = new CreditInfo();
							}
							creditInfo.remainAmount = rs.getBigDecimal(1);
							creditInfo.perAmount = rs.getBigDecimal(2);
							creditInfo.creditStatus = EnumParser.parse(CreditStatus.class, rs.getString(3));
						}
					}
				}
				Timestamp time = getCurrentTimestamp(conn);
				/**
				 * 计算购买的金额
				 */
				BigDecimal buyAmount = creditInfo.perAmount.multiply(new BigDecimal(amount));
				/**
				 * 判断账户余额是否大于购买金额
				 */
				if (userInfo.kyMoney.compareTo(buyAmount) < 0) {
					throw new LogicalException("你的账户余额不足进行本次投资 ，请充值");
				}

				/**
				 * 判断剩余金额是否大于购买金额
				 */
				if (creditInfo.remainAmount.compareTo(buyAmount) < 0) {
					throw new LogicalException("你的投资份数大于标的剩余份数");
				}

				if (creditInfo.creditStatus != CreditStatus.TBZ) {
					throw new LogicalException("当前状态不允许投资");
				}

				/**
				 * 操作用户资金表
				 */
				try (PreparedStatement ps = conn
						.prepareStatement("UPDATE T6023 SET F05 = F05 - ? , F04 = F04 + ? WHERE F01 = ?")) {
					ps.setBigDecimal(1, buyAmount);
					ps.setBigDecimal(2, buyAmount);
					ps.setInt(3, loginId);
					ps.executeUpdate();
				}
				/**
				 * 操作投资人列表
				 */
				try (PreparedStatement ps = conn
						.prepareStatement("INSERT INTO T6037(F02,F03,F04,F05) VALUES (?,?,?,?)")) {
					ps.setInt(1, loanId);
					ps.setInt(2, loginId);
					ps.setBigDecimal(3, buyAmount);
					ps.setTimestamp(4, time);
					ps.execute();
				}
				/**
				 * 更新借款标信息
				 */
				try (PreparedStatement ps = conn.prepareStatement("UPDATE T6036 SET F07 = F07 - ? WHERE F01 = ?")) {
					ps.setBigDecimal(1, buyAmount);
					ps.setInt(2, loanId);
					ps.executeUpdate();
				}
				/**
				 * 判断是否满标
				 */
				double syktje = 0;
				try (PreparedStatement ps = conn.prepareStatement("SELECT F07 FROM T6036 WHERE F01 = ?")) {
					ps.setInt(1, loanId);
					try (ResultSet rs = ps.executeQuery();) {
						if (rs.next()) {
							syktje  = rs.getBigDecimal(1).doubleValue();
						}
					}
				}
				if(syktje <= 0) {
					try (PreparedStatement ps = conn
							.prepareStatement("UPDATE T6036 SET F29 = ?, F20 = ? WHERE F01 = ?")) {
						ps.setTimestamp(1, time);
						ps.setString(2, CreditStatus.YMB.toString());
						ps.setInt(3, loanId);
						ps.executeUpdate();
					}
				}
				serviceResource.commit(conn);
			} catch (Exception e) {
				serviceResource.rollback(conn);
				throw e;
			}
		}
	}



}
