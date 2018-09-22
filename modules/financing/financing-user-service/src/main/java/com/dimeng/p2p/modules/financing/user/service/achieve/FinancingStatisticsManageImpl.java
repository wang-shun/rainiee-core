package com.dimeng.p2p.modules.financing.user.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.p2p.common.enums.CreditType;
import com.dimeng.p2p.common.enums.DsStatus;
import com.dimeng.p2p.modules.financing.user.service.FinancingStatisticsManage;
import com.dimeng.p2p.modules.financing.user.service.entity.AccountBalance;
import com.dimeng.p2p.modules.financing.user.service.entity.EarnFinancingTotal;
import com.dimeng.p2p.modules.financing.user.service.entity.FinancingTotal;
import com.dimeng.p2p.modules.financing.user.service.entity.UnpayEarnings;

public class FinancingStatisticsManageImpl extends AbstractFinancingManage
		implements FinancingStatisticsManage {
	public static class FinacingTotalManageFactory implements
			ServiceFactory<FinancingStatisticsManage> {
		@Override
		public FinancingStatisticsManageImpl newInstance(
				ServiceResource serviceResource) {
			return new FinancingStatisticsManageImpl(serviceResource);
		}

	}

	public FinancingStatisticsManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public EarnFinancingTotal getEarnFinancingTotal() throws Throwable {
		EarnFinancingTotal total = new EarnFinancingTotal();
		try (Connection connection = getConnection()) {
			int accountId = serviceResource.getSession().getAccountId();
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT IFNULL(F02,0) FROM T6026 WHERE F01=?")) {
				ps.setInt(1, accountId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						total.plan = resultSet.getBigDecimal(1);
					}
				}
			}
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT IFNULL(SUM(F06),0),IFNULL(SUM(F07),0) FROM T6056 WHERE F03=? AND F10=? AND F14='F'")) {
				ps.setInt(1, accountId);
				ps.setString(2, DsStatus.YS.name());
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						total.interest = resultSet.getBigDecimal(1);
						total.penalty = resultSet.getBigDecimal(2);
					}
				}
			}
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT IFNULL(SUM(F09),0) FROM T6038 WHERE F03=?")) {
				ps.setInt(1, accountId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						total.breach = resultSet.getBigDecimal(1);
					}
				}
			}
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT IFNULL(SUM(F02),0) FROM T6027 WHERE F01=?")) {
				ps.setInt(1, accountId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						total.transfer = resultSet.getBigDecimal(1);
					}
				}
			}
		}
		total.total = total.breach.add(total.interest).add(total.penalty)
				.add(total.plan).add(total.transfer);
		return total;
	}

	@Override
	public FinancingTotal getFinancingTotal() throws Throwable {
		FinancingTotal total = new FinancingTotal();
		try (Connection connection = getConnection()) {
			int accountId = serviceResource.getSession().getAccountId();
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT IFNULL(F07,0) FROM T6026 WHERE F01=?")) {
				ps.setInt(1, accountId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						total.plan = resultSet.getBigDecimal(1);
					}
				}
			}
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT IFNULL(SUM(T6056.F05),0),T6036.F19 FROM T6056 INNER JOIN T6036 ON T6056.F02=T6036.F01 WHERE T6056.F03=? GROUP BY T6036.F19")) {
				ps.setInt(1, accountId);
				try (ResultSet resultSet = ps.executeQuery()) {
					while (resultSet.next()) {
						if (CreditType.XJD.name()
								.equals(resultSet.getString(2))
								|| CreditType.SYD.name().equals(
										resultSet.getString(2))) {
							total.credit = total.credit.add(resultSet
									.getBigDecimal(1));
						} else if (CreditType.SDRZ.name().equals(
								resultSet.getString(2))) {
							total.certification = resultSet.getBigDecimal(1);
						} else if (CreditType.XYDB.name().equals(
								resultSet.getString(2))) {
							total.warrant = resultSet.getBigDecimal(1);
						}
					}
				}
			}
		}
		total.total = total.certification.add(total.credit).add(total.plan)
				.add(total.warrant);
		return total;
	}

	@Override
	public AccountBalance getAccountBalance() throws Throwable {
		AccountBalance accountBalance = new AccountBalance();
		try (Connection connection = getConnection()) {
			int accountId = serviceResource.getSession().getAccountId();
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT IFNULL(F03,0) FROM T6026 WHERE F01=?")) {
				ps.setInt(1, accountId);
				try (ResultSet resultSet = ps.executeQuery()) {
					if (resultSet.next()) {
						accountBalance.plan = resultSet.getBigDecimal(1);
					}
				}
			}
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT IFNULL(SUM(T6056.F05),0),T6036.F19 FROM T6056 INNER JOIN T6036 ON T6056.F02=T6036.F01 WHERE T6056.F03=? AND T6056.F10=? GROUP BY T6036.F19")) {
				ps.setInt(1, accountId);
				ps.setString(2, DsStatus.WS.name());
				try (ResultSet resultSet = ps.executeQuery()) {
					while (resultSet.next()) {
						if (CreditType.XJD.name()
								.equals(resultSet.getString(2))
								|| CreditType.SYD.name().equals(
										resultSet.getString(2))) {
							accountBalance.credit = accountBalance.credit
									.add(resultSet.getBigDecimal(1));
						} else if (CreditType.SDRZ.name().equals(
								resultSet.getString(2))) {
							accountBalance.certification = resultSet
									.getBigDecimal(1);
						} else if (CreditType.XYDB.name().equals(
								resultSet.getString(2))) {
							accountBalance.warrant = resultSet.getBigDecimal(1);
						}
					}
				}
			}
		}
		accountBalance.total = accountBalance.certification
				.add(accountBalance.credit).add(accountBalance.plan)
				.add(accountBalance.warrant);
		return accountBalance;
	}

	@Override
	public UnpayEarnings getUnpayEarnings() throws Throwable {
		UnpayEarnings unpayEarnings = new UnpayEarnings();
		try (Connection connection = getConnection()) {
			try (PreparedStatement ps = connection
					.prepareStatement("SELECT IFNULL(SUM(T6056.F06+T6056.F07),0),T6036.F19 FROM T6056 INNER JOIN T6036 ON T6056.F02=T6036.F01 WHERE T6056.F03=? AND T6056.F10=? GROUP BY T6036.F19")) {
				ps.setInt(1, serviceResource.getSession().getAccountId());
				ps.setString(2, DsStatus.WS.name());
				try (ResultSet resultSet = ps.executeQuery()) {
					while (resultSet.next()) {
						if (CreditType.XJD.name()
								.equals(resultSet.getString(2))
								|| CreditType.SYD.name().equals(
										resultSet.getString(2))) {
							unpayEarnings.credit = unpayEarnings.credit
									.add(resultSet.getBigDecimal(1));
						} else if (CreditType.SDRZ.name().equals(
								resultSet.getString(2))) {
							unpayEarnings.certification = resultSet
									.getBigDecimal(1);
						} else if (CreditType.XYDB.name().equals(
								resultSet.getString(2))) {
							unpayEarnings.warrant = resultSet.getBigDecimal(1);
						}
					}
				}
			}
		}
		unpayEarnings.total = unpayEarnings.certification.add(
				unpayEarnings.credit).add(unpayEarnings.warrant);
		return unpayEarnings;
	}

}
