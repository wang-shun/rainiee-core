package com.dimeng.p2p.modules.financing.front.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.enums.EarningsWay;
import com.dimeng.p2p.common.enums.EnsureMode;
import com.dimeng.p2p.common.enums.PlanState;
import com.dimeng.p2p.common.enums.SignType;
import com.dimeng.p2p.modules.financing.front.service.FinancingPlanManage;
import com.dimeng.p2p.modules.financing.front.service.entity.FinancingPlan;
import com.dimeng.p2p.modules.financing.front.service.entity.FinancingPlanCount;
import com.dimeng.p2p.modules.financing.front.service.entity.FinancingPlanInfo;
import com.dimeng.p2p.modules.financing.front.service.entity.PlanRecode;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.util.parser.EnumParser;

public class FinancingPlanManageImpl extends AbstractFinancingManage implements
		FinancingPlanManage {

	public FinancingPlanManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	public static class FinancingPlanManageFactory implements
			ServiceFactory<FinancingPlanManage> {
		@Override
		public FinancingPlanManage newInstance(ServiceResource serviceResource) {
			return new FinancingPlanManageImpl(serviceResource);
		}

	}

	@Override
	public FinancingPlanInfo getPlan(int id) throws Throwable {
		if(id<0){
			return null;
		}
		String sql = "SELECT T6042.F02 title, T6042.F25 jhje, T6042.F05 yqsy, T6042.f06 tbfw, T6042.F24 bzfs, T6042.F07 jhzt, T6042.f08 meys, T6042.F11 sdqx, ADDDATE(T6042.F10,INTERVAL 1 DAY) jhjz, T6042.F14 sycl, T6042.F15 jrfl, T6042.F16 fwfl, T6042.F17 tcfl, T6042.F22 minmoney, T6042.F23 maxmoney,T6042.F01,T6042.F09,T6042.F13 ,T6042.F04,T6042.F18 jhjs,CURRENT_TIMESTAMP() FROM T6042 WHERE T6042.F01 = ?";
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(id);
		try(Connection connection = getConnection(P2PConst.DB_USER))
		{
			return select(connection,
					new ItemParser<FinancingPlanInfo>() {
						@Override
						public FinancingPlanInfo parse(ResultSet resultSet)
								throws SQLException {
							FinancingPlanInfo credit = null;
							while (resultSet.next()) {
								if (credit == null) {
									credit = new FinancingPlanInfo();
								}
								credit.planTitle = resultSet.getString(1);
								credit.planMoney = resultSet.getBigDecimal(2);
								credit.yqsy = resultSet.getBigDecimal(3);
								credit.signType = EnumParser.parse(SignType.class,resultSet
										.getString(4));
								credit.ensureMode = EnumParser.parse(EnsureMode.class,resultSet
										.getString(5));
								credit.planState = EnumParser.parse(PlanState.class,resultSet
										.getString(6));
								credit.fullTime = resultSet.getInt(7);
								credit.lockqx = resultSet.getInt(8);
								credit.cutoff = resultSet.getTimestamp(9);
								credit.earningsWay = EnumParser.parse(EarningsWay.class,resultSet
										.getString(10));
								credit.jrfl = resultSet.getDouble(11);
								credit.fwfl = resultSet.getDouble(12);
								credit.tcfl = resultSet.getDouble(13);
								credit.minMoney = resultSet.getBigDecimal(14);
								credit.maxMoney = resultSet.getBigDecimal(15);
								credit.id = resultSet.getInt(16);

								credit.fromSale = resultSet.getTimestamp(17);
								credit.lockEnd = resultSet.getTimestamp(18);
								credit.syMoney = resultSet.getBigDecimal(19);
								credit.introduce = resultSet.getString(20);
								credit.currentTime = resultSet.getTimestamp(21);
							}
							return credit;
						}
					}, sql, parameters);
		}
	}

	@Override
	public PagingResult<FinancingPlan> search(Paging paging) throws Throwable {
		String sql = "SELECT T6042.F02 title, T6042.F25 jhje, count(T6043.F01) joinCount, T6042.F05, ( SELECT IFNULL(sum(T6057.F06), 0) FROM T6057 WHERE T6057.F02 = T6042.F01) earnMoney, T6042.F09 fabusj, T6042.F01 FROM T6042, T6043 WHERE T6043.F02 = T6042.F01 AND T6042.F01 <> (SELECT MAX(F01) FROM T6042) GROUP BY T6042.F01 ORDER BY T6042.F01 DESC";
		try(Connection connection = getConnection(P2PConst.DB_USER))
		{
			return selectPaging(connection,
					new ArrayParser<FinancingPlan>() {
						@Override
						public FinancingPlan[] parse(ResultSet resultSet)
								throws SQLException {
							ArrayList<FinancingPlan> list = null;
							while (resultSet.next()) {
								FinancingPlan credit = new FinancingPlan();
								credit.planTitle = resultSet.getString(1);
								credit.planMoney = resultSet.getString(2);
								credit.joinCount = resultSet.getInt(3);
								credit.avgYield = resultSet.getDouble(4);
								credit.totleEarn = resultSet.getBigDecimal(5);
								credit.releaseTime = resultSet.getTimestamp(6);
								credit.id = resultSet.getInt(7);
								credit.moneyUtilization = 1;
								if (list == null) {
									list = new ArrayList<>();
								}
								list.add(credit);
							}
							return list == null ? null : list
									.toArray(new FinancingPlan[list.size()]);
						}
					}, paging, sql);
		}
	}

	@Override
	public FinancingPlanInfo getNewPlan() throws Throwable {
		String sql = "SELECT T6042.F02, T6042.F25, T6042.F05, T6042.F06, T6042.F24, T6042.F07, T6042.F08, T6042.F11, ADDDATE(T6042.F10,INTERVAL 1 DAY), T6042.F14, T6042.F15, T6042.F16, T6042.F17, T6042.F22, T6042.F23, T6042.F01, T6042.F09, T6042.F13, T6042.F04, T6042.F18, T6042.F03,CURRENT_TIMESTAMP(),T6042.F10 FROM T6042 ORDER BY T6042.F01 DESC LIMIT 1";
		ArrayList<Object> parameters = new ArrayList<>();
		try(Connection connection = getConnection(P2PConst.DB_USER))
		{
			return select(connection,
					new ItemParser<FinancingPlanInfo>() {
						@Override
						public FinancingPlanInfo parse(ResultSet resultSet)
								throws SQLException {
							FinancingPlanInfo credit = null;
							while (resultSet.next()) {
								if (credit == null) {
									credit = new FinancingPlanInfo();
								}
								credit.planTitle = resultSet.getString(1);
								credit.planMoney = resultSet.getBigDecimal(2);
								credit.yqsy = resultSet.getBigDecimal(3);
								credit.signType = EnumParser.parse(SignType.class,resultSet
										.getString(4));
								credit.ensureMode = EnumParser.parse(EnsureMode.class,resultSet
										.getString(5));
								credit.planState = EnumParser.parse(PlanState.class,resultSet
										.getString(6));
								credit.fullTime = resultSet.getInt(7);
								credit.lockqx = resultSet.getInt(8);
								credit.cutoff = resultSet.getTimestamp(9);
								credit.earningsWay = EnumParser.parse(EarningsWay.class,resultSet
										.getString(10));
								credit.jrfl = resultSet.getDouble(11);
								credit.fwfl = resultSet.getDouble(12);
								credit.tcfl = resultSet.getDouble(13);
								credit.minMoney = resultSet.getBigDecimal(14);
								credit.maxMoney = resultSet.getBigDecimal(15);
								credit.id = resultSet.getInt(16);
								credit.fromSale = resultSet.getTimestamp(17);
								credit.lockEnd = resultSet.getTimestamp(18);
								credit.syMoney = resultSet.getBigDecimal(19);
								credit.introduce = resultSet.getString(20);
								credit.ActualMoney = resultSet.getBigDecimal(21);
								credit.currentTime = resultSet.getTimestamp(22);
								credit.sqjsrq = resultSet.getTimestamp(23);
								credit.proess = (credit.planMoney.doubleValue() - credit.syMoney
										.doubleValue())
										/ credit.planMoney.doubleValue();
							}
							return credit;
						}
					}, sql, parameters);
		}
	}

	@Override
	public FinancingPlanCount getStatistics() throws Throwable {
		FinancingPlanCount fc = new FinancingPlanCount();
		try (Connection conn = getConnection(P2PConst.DB_CONSOLE)) {
			try(PreparedStatement ps = conn.prepareStatement("SELECT F01,F02,F04 FROM T7048");){
				try(ResultSet rs = ps.executeQuery();){
					if(rs.next()){
							fc.totleMoney = rs.getBigDecimal(1);
							fc.userEarnMoney =rs.getBigDecimal(2);
							fc.joinCount = rs.getLong(3);
							if(fc.totleMoney.compareTo(BigDecimal.ZERO)>0){
								fc.moneyUse = 1;
							}else{
								fc.moneyUse = 0;
							}
						}
					}
				}
			}
		
		return fc;
	}
	

	@Override
	public PlanRecode[] search(int id) throws Throwable {
		if(id<0){
			return null;
		}
		String sql = "SELECT T6010.F02, T6043.F04, T6043.F05 FROM T6043 INNER JOIN T6010 ON T6043.F03 = T6010.F01 WHERE T6043.F02 = ? ORDER BY T6043.F05 DESC";
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(id);
		try(Connection connection = getConnection(P2PConst.DB_USER))
		{
			return selectAll(connection,
					new ArrayParser<PlanRecode>() {
						@Override
						public PlanRecode[] parse(ResultSet resultSet)
								throws SQLException {
							ArrayList<PlanRecode> list = null;
							while (resultSet.next()) {
								PlanRecode pr = new PlanRecode();
								pr.tzrName = resultSet.getString(1);
								pr.tzMoney = resultSet.getBigDecimal(2);
								pr.tzTime = resultSet.getTimestamp(3);
								if (list == null) {
									list = new ArrayList<>();
								}
								list.add(pr);
							}
							return list == null ? null : list
									.toArray(new PlanRecode[list.size()]);
						}
					}, sql, parameters);
		}
	}


	@Override
	public BigDecimal tzMoney(int planId) throws Throwable {
		if(planId<0){
			return null;
		}
		BigDecimal bd = new BigDecimal(0);
		try (Connection conn = getConnection(P2PConst.DB_USER)) {
			try(PreparedStatement ps = conn.prepareStatement("SELECT F04 FROM T6043 WHERE F02 = ? AND F03 = ?");){
				ps.setInt(1, planId);
				ps.setInt(2, serviceResource.getSession().getAccountId());
				try(ResultSet rs = ps.executeQuery();){
					if(rs.next()){
							bd  = rs.getBigDecimal(1);
						}
					}
				}
			}
		return bd;
	}

}
