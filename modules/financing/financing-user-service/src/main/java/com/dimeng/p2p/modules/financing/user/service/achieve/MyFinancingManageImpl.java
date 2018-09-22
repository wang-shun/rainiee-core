package com.dimeng.p2p.modules.financing.user.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.enums.CreditLevel;
import com.dimeng.p2p.common.enums.CreditStatus;
import com.dimeng.p2p.common.enums.DsStatus;
import com.dimeng.p2p.common.enums.OverdueStatus;
import com.dimeng.p2p.common.enums.RepayStatus;
import com.dimeng.p2p.modules.financing.user.service.MyFinancingManage;
import com.dimeng.p2p.modules.financing.user.service.entity.AssetsInfo;
import com.dimeng.p2p.modules.financing.user.service.entity.LoanAssests;
import com.dimeng.p2p.modules.financing.user.service.entity.MaySettleFinacing;
import com.dimeng.p2p.modules.financing.user.service.entity.OutAssests;
import com.dimeng.p2p.modules.financing.user.service.entity.RecoverAssests;
import com.dimeng.p2p.modules.financing.user.service.entity.SettleAssests;
import com.dimeng.util.parser.EnumParser;

public class MyFinancingManageImpl extends AbstractFinancingManage implements
		MyFinancingManage {
	public static class MyFinancingManageFactory implements
			ServiceFactory<MyFinancingManage> {
		@Override
		public MyFinancingManageImpl newInstance(ServiceResource serviceResource) {
			return new MyFinancingManageImpl(serviceResource);
		}

	}

	public MyFinancingManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public AssetsInfo getAssetsInfo() throws Throwable {
		AssetsInfo info = new AssetsInfo();
		info.accMakeMoney = earnLx().add(earnWyj());
		info.sellMakeMoney = earnZqzryk();
		info.makeMoney = info.accMakeMoney.add(
				info.sellMakeMoney);
		info.money = earnZc();
		info.assetsNum = earnCount();
		return info;
	}

	/**
	 * 持有数量
	 * 
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	private int earnCount() throws Throwable {
		int count = 0;
		try(Connection connection = getConnection())
		{
			try (PreparedStatement ps = connection.prepareStatement(
					"SELECT COUNT(F01)  FROM T6038 WHERE F03 = ? AND F04 >0")) {
				ps.setInt(1, serviceResource.getSession().getAccountId());
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						count = rs.getInt(1);
					}

				}
			}
			return count;
		}
	}

	/**
	 * 资产
	 * 
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	private BigDecimal earnZc() throws Throwable {
		BigDecimal zc = new BigDecimal(0);
		try(Connection connection = getConnection())
		{
			try (PreparedStatement ps = connection.prepareStatement(
					"SELECT IFNULL(SUM(F05),0)  FROM T6056 WHERE F03 = ? AND F10 = ?")) {
				ps.setInt(1, serviceResource.getSession().getAccountId());
				ps.setString(2, DsStatus.WS.name());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						zc = rs.getBigDecimal(1);
					}

				}
			}
			return zc;
		}
	}

	/**
	 * 利息加罚息
	 * 
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	private BigDecimal earnLx() throws Throwable {
		BigDecimal lx = new BigDecimal(0);
		try(Connection connection = getConnection())
		{
			try (PreparedStatement ps = connection
					.prepareStatement(
							"SELECT IFNULL(SUM(T6056.F06+T6056.F07),0)  FROM T6056 WHERE T6056.F03 = ? AND T6056.F10 = ? AND T6056.F14 = ?")) {
				ps.setInt(1, serviceResource.getSession().getAccountId());
				ps.setString(2, DsStatus.YS.name());
				ps.setString(3, OverdueStatus.F.name());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						lx = rs.getBigDecimal(1);
					}

				}
			}
			return lx;
		}
	}

	/**
	 * 违约金
	 * 
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	private BigDecimal earnWyj() throws Throwable {
		BigDecimal wyj = new BigDecimal(0);
		try(Connection connection = getConnection())
		{
			try (PreparedStatement ps = connection
					.prepareStatement(
							"SELECT IFNULL(SUM(T6038.F09),0) FROM T6038 WHERE T6038.F03 = ?")) {
				ps.setInt(1, serviceResource.getSession().getAccountId());
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						wyj = rs.getBigDecimal(1);
					}

				}
			}
			return wyj;
		}
	}

	/**
	 * 债权转让盈亏
	 * 
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	private BigDecimal earnZqzryk() throws Throwable {
		BigDecimal Zqzryk = new BigDecimal(0);
		try(Connection connection = getConnection())
		{
			try (PreparedStatement ps = connection.prepareStatement(
					"SELECT F02 FROM T6027 WHERE F01 = ?")) {
				ps.setInt(1, serviceResource.getSession().getAccountId());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						Zqzryk = rs.getBigDecimal(1);
					}

				}
			}
			return Zqzryk;
		}
	}

	@Override
	public PagingResult<RecoverAssests> getRecoverAssests(Paging paging)
			throws Throwable {
		ArrayList<Object> parameters = new ArrayList<>();
		String sql = "SELECT T6036.F03, T6038.F10, T6036.F09, ( SELECT IFNULL((T6056.F05 + T6056.F06), 0) FROM T6056 WHERE T6056.F02 = T6038.F02 AND T6056.F03 = T6038.F03 AND T6056.F10 = ? ORDER BY F01 LIMIT 1 ), T6036.F24, T6036.F23 nextDate, T6036.F08, T6036.F20, T6036.F01, T6038.F08 cyfs, ( SELECT IFNULL(SUM(T6056.F05 + T6056.F06), 0) FROM T6056 WHERE T6056.F02 = T6038.F02 AND T6056.F03 = T6038.F03 AND T6056.F10 = ? ), T6038.F04 cyje FROM T6038, T6036 WHERE T6038.F02 = T6036.F01 AND T6038.F03 = ? AND T6036.F20 = ? AND T6038.F04 > 0  ORDER BY T6038.F06 DESC";
		parameters.add(DsStatus.WS);
		parameters.add(DsStatus.WS);
		parameters.add(serviceResource.getSession().getAccountId());
		parameters.add(CreditStatus.YFK);
		try(Connection connection = getConnection())
		{
			return selectPaging(connection, new ArrayParser<RecoverAssests>() {
				@Override
				public RecoverAssests[] parse(ResultSet resultSet)
						throws SQLException {
					ArrayList<RecoverAssests> list = null;
					while (resultSet.next()) {
						if (list == null) {
							list = new ArrayList<>();
						}
						RecoverAssests assests = new RecoverAssests();
						assests.assestsID = resultSet.getInt(1);
						assests.rate = resultSet.getDouble(3);
						assests.recoverMonthAcc = resultSet.getBigDecimal(4);
						assests.assestsNum = resultSet.getInt(5);
						assests.lastDay = resultSet.getTimestamp(6);
						assests.creditNum = resultSet.getInt(7);
						assests.status = EnumParser.parse(CreditStatus.class,
								resultSet.getString(8));
						assests.jkbId = resultSet.getInt(9);
						assests.cyfs = resultSet.getInt(10);
						assests.recoverAcc = resultSet.getBigDecimal(11);
						assests.cyje = resultSet.getBigDecimal(12);
						if (assests.cyfs > 0) {
							assests.originalMoney = resultSet.getBigDecimal(2);
							assests.zqjz = assests.cyje.divide(new BigDecimal(
									assests.cyfs), BigDecimal.ROUND_HALF_UP);
						}
						try {
							for (MaySettleFinacing msf : getMaySettleFinacing()) {
								if (msf.jkbId == assests.jkbId) {
									assests.isTransfer = true;
									break;
								}
							}
						} catch (Throwable e) {
							logger.error(e, e);
						}
						list.add(assests);
					}

					return list == null || list.size() == 0 ? null : list
							.toArray(new RecoverAssests[list.size()]);

				}
			}, paging, sql, parameters);
		}
	}

	/**
	 * 可转出的债权
	 * 
	 * @return
	 * @throws Throwable
	 */
	private List<MaySettleFinacing> getMaySettleFinacing() throws Throwable {
		List<MaySettleFinacing> msfList = new ArrayList<MaySettleFinacing>();
		String sql = "SELECT T6036.F01 FROM T6036, T6038, T6041 WHERE T6036.F20 = ? AND T6038.F02 = T6036.F01 AND T6041.F02 = T6036.F01 AND DATE_ADD(T6036.F31, INTERVAL 90 DAY) < CURRENT_TIMESTAMP () AND DATE_ADD(CURRENT_TIMESTAMP (), INTERVAL 3 DAY) < T6036.F23 AND T6036.F39 = ? AND T6038.F03 = ? AND T6041.F12 = ? AND T6038.F07 = ? GROUP BY T6036.F01 ORDER BY T6036.F31 DESC";
		try(Connection connection = getConnection())
		{
			try(PreparedStatement ps = connection.prepareStatement(sql))
			{
				ps.setString(1, CreditStatus.YFK.name());
				ps.setString(2, OverdueStatus.F.name());
				ps.setInt(3, serviceResource.getSession().getAccountId());
				ps.setString(4, RepayStatus.WH.name());
				ps.setString(5, OverdueStatus.F.name());
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						MaySettleFinacing info = new MaySettleFinacing();
						info.jkbId = rs.getInt(1);
						msfList.add(info);
					}

				}
			}
			return msfList;
		}
	}

	@Override
	public PagingResult<SettleAssests> getSettleAssests(Paging paging)
			throws Throwable {
		String sql = "SELECT T6036.F03, T6038.F10, T6036.F09, T6036.F32, T6036.F20, T6036.F01, ( SELECT IFNULL( SUM(T6056.F05 + T6056.F07) + T6038.F09, 0) FROM T6056 WHERE T6056.F02 = T6038.F02 AND T6056.F03 = T6038.F03 AND T6056.F10 = ? ), ( SELECT IFNULL(SUM(T6056.F06), 0) FROM T6056 WHERE T6056.F02 = T6038.F02 AND T6056.F03 = T6038.F03 AND T6056.F10 = ? AND T6056.F14 = ? ) FROM T6036, T6038 WHERE T6036.F01 = T6038.F02 AND T6038.F03 = ? AND T6036.F20 IN (?, ?) AND T6038.F08 > 0 ORDER BY T6038.F06 DESC";
		try(Connection connection = getConnection())
		{
			PagingResult<SettleAssests> saResult =  selectPaging(connection, new ArrayParser<SettleAssests>() {
				@Override
				public SettleAssests[] parse(ResultSet resultSet)
						throws SQLException {
					ArrayList<SettleAssests> list = null;
					while (resultSet.next()) {
						if (list == null) {
							list = new ArrayList<>();
						}
						SettleAssests assests = new SettleAssests();
						assests.assestsID = resultSet.getInt(1);
						assests.money = resultSet.getBigDecimal(2);
						assests.rate = resultSet.getDouble(3);
						assests.recMoney = resultSet.getBigDecimal(7).add(resultSet.getBigDecimal(8));
						assests.makeMoney = assests.recMoney.subtract(assests.money);
						assests.settleDay = resultSet.getDate(4);
						assests.settleMethod = EnumParser.parse(CreditStatus.class,
								resultSet.getString(5));
						assests.jkbId = resultSet.getInt(6);
						list.add(assests);
					}

					return list == null || list.size() == 0 ? null : list
							.toArray(new SettleAssests[list.size()]);

				}
			}, paging, sql, DsStatus.YS,DsStatus.YS, OverdueStatus.F, serviceResource.getSession()
					.getAccountId(), CreditStatus.YJQ, CreditStatus.YDF);


			for(SettleAssests sa: saResult.getItems()){
				if(sa.settleMethod == CreditStatus.YDF){
					String sql1 = "SELECT T6036.F35, ( SELECT IFNULL(SUM(T6056.F05), 0) FROM T6056 WHERE T6056.F02 = T6038.F02 AND T6056.F03 = T6038.F03 AND T6056.F10 = ? ), ( SELECT IFNULL(SUM(T6056.F06 + T6056.F07) ,0) FROM T6056 WHERE T6056.F02 = T6038.F02 AND T6056.F03 = T6038.F03 AND T6056.F10 = ? AND T6056.F04 <= T6036.F36 ) FROM T6036, T6038 WHERE T6036.F01 = T6038.F02 AND T6038.F03 = ? AND T6036.F20 = ? AND T6038.F08 > 0 AND T6038.F02 = ? LIMIT 1" ;
					try(PreparedStatement ps = connection.prepareStatement(sql1)){
						ps.setString(1,DsStatus.YS.name());
						ps.setString(2,DsStatus.YS.name());
						ps.setInt(3,serviceResource.getSession().getAccountId());
						ps.setString(4,sa.settleMethod.name());
						ps.setInt(5,sa.jkbId);
						try (ResultSet rs = ps.executeQuery()) {
							if (rs.next()) {
								sa.settleDay = rs.getDate(1);
								sa.recMoney = rs.getBigDecimal(2).add(rs.getBigDecimal(3));
								sa.makeMoney = sa.recMoney.subtract(sa.money);
							}

						}
					}
				}
			}

			return saResult;
		}

	}

	@Override
	public PagingResult<LoanAssests> getLoanAssests(Paging paging)
			throws Throwable {
		String sql = "SELECT T6036.F03, T6037.F04, T6036.F09, T6036.F08, T6036.F40, T6036.F12, T6036.F28, T6036.F06, T6036.F07, T6036.F01 FROM T6036, T6037 WHERE T6037.F02 = T6036.F01 AND T6037.F03 = ? AND T6036.F20 IN (?,?) ORDER BY T6037.F06 DESC";
		try(Connection connection = getConnection())
		{
			return selectPaging(
					connection,
					new ArrayParser<LoanAssests>() {
						@Override
						public LoanAssests[] parse(ResultSet resultSet)
								throws SQLException {
							ArrayList<LoanAssests> list = null;
							long dayInMill = 3600000 * 24;
							long a;
							int day;
							int hour;
							int min;
							while (resultSet.next()) {
								if (list == null) {
									list = new ArrayList<>();
								}
								LoanAssests assests = new LoanAssests();
								assests.assestsID = resultSet.getInt(1);
								assests.originalMoney = resultSet.getBigDecimal(2);
								assests.rate = resultSet.getDouble(3);
								assests.assestsNum = resultSet.getInt(4);
								assests.creditLevel = EnumParser.parse(CreditLevel.class,
										resultSet.getString(5));
								assests.ckTime = resultSet.getInt(6);
								assests.shTime = resultSet.getDate(7);
								double total = resultSet.getDouble(8);// 借款总金额
								double banalce = resultSet.getDouble(9);// 可投资金额
								assests.jkbId = resultSet.getInt(10);
								a = dayInMill * assests.ckTime
										+ assests.shTime.getTime()
										- System.currentTimeMillis();
								if (a <= 0) {
									assests.surTime = "";
								} else {
									day = (int) Math.ceil(a / dayInMill);
									hour = (int) Math
											.ceil((a - day * dayInMill) / 3600000);
									min = (int) Math
											.ceil((a - day * dayInMill - hour * 3600000) / 60000);
									assests.surTime = String.format("%d天%d小时%d分钟",
											day, hour, min);
								}
								assests.progress = (total - banalce) / total;
								list.add(assests);
							}
							return list == null || list.size() == 0 ? null : list
									.toArray(new LoanAssests[list.size()]);

						}
					}, paging, sql, serviceResource.getSession().getAccountId(),
					CreditStatus.TBZ, CreditStatus.YMB);
		}
	}

	@Override
	public PagingResult<OutAssests> getOutAssests(Paging paging)
			throws Throwable {
		String sql = "SELECT T6036.F03, T6039.F07, T6039.F05, T6039.F06 F60, T6040.F06, T6040.F04 zrfs, T6036.F01,T6040.F01 AS inid  FROM T6039, T6040, T6036 WHERE T6036.F01 = T6039.F03 AND T6040.F02 = T6039.F01 AND T6039.F04 =? ORDER BY T6040.F05 DESC";
		try(Connection connection = getConnection())
		{
			return selectPaging(connection, new ArrayParser<OutAssests>() {
				@Override
				public OutAssests[] parse(ResultSet resultSet) throws SQLException {
					ArrayList<OutAssests> list = null;
					while (resultSet.next()) {
						if (list == null) {
							list = new ArrayList<>();
						}
						OutAssests assests = new OutAssests();
						assests.asstestsID = resultSet.getInt(1);
						assests.zcNum = resultSet.getInt(2);
						assests.zqjg = resultSet.getBigDecimal(3);
						assests.zrjg = resultSet.getBigDecimal(4);
						assests.cost = resultSet.getBigDecimal(5);
						assests.outNum = resultSet.getInt(6);
						assests.jkbId = resultSet.getInt(7);
						assests.outTotalValue = new BigDecimal(assests.zqjg
								.doubleValue() * assests.outNum);
						assests.outTotalMoney = new BigDecimal(assests.zrjg
								.doubleValue() * assests.outNum);
						assests.realityMoney = assests.outTotalMoney
								.subtract(assests.cost);
						assests.money = assests.realityMoney
								.subtract(assests.outTotalValue);
						assests.inId = resultSet.getInt(8);
						list.add(assests);
					}
					return list == null || list.size() == 0 ? null : list
							.toArray(new OutAssests[list.size()]);

				}
			}, paging, sql, serviceResource.getSession().getAccountId());
		}
	}

}
