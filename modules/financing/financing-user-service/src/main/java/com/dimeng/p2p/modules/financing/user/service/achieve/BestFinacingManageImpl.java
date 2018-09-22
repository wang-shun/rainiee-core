package com.dimeng.p2p.modules.financing.user.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S60.enums.T6042_F14;
import com.dimeng.p2p.common.enums.PlanState;
import com.dimeng.p2p.common.enums.RepayStatus;
import com.dimeng.p2p.modules.financing.user.service.BestFinacingManage;
import com.dimeng.p2p.modules.financing.user.service.entity.BestFinacingInfo;
import com.dimeng.p2p.modules.financing.user.service.entity.EndBestFinacing;
import com.dimeng.p2p.modules.financing.user.service.entity.InBestFinacing;
import com.dimeng.p2p.modules.financing.user.service.entity.SqzBestFinacing;
import com.dimeng.util.parser.EnumParser;

public class BestFinacingManageImpl  extends AbstractFinancingManage implements BestFinacingManage{
	public static class BestFinacingManageFactory implements ServiceFactory<BestFinacingManage> {
		@Override
		public BestFinacingManageImpl newInstance(ServiceResource serviceResource) {
			return new BestFinacingManageImpl(serviceResource);
		}

	}
	public BestFinacingManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public BestFinacingInfo getBestFinacingInfo() throws Throwable{
		BestFinacingInfo info = null;
		try(Connection connection = getConnection())
		{
			try(PreparedStatement ps = connection.prepareStatement("SELECT F02,F03,F04,F05,F06 FROM T6026 WHERE F01=?"))
			{
				ps.setInt(1, serviceResource.getSession().getAccountId());
				try(ResultSet rs = ps.executeQuery())
				{
					if(rs.next()){
						info = new BestFinacingInfo();
						info.makeMoney = rs.getBigDecimal(1);
						info.accountMoney = rs.getBigDecimal(2);
						info.rate = rs.getBigDecimal(3);
						info.num = rs.getInt(4);
						info.addMoney = rs.getBigDecimal(5);
					}
				}
			}
			return info;
		}
	}
	
	@Override
	public PagingResult<SqzBestFinacing> getSqzBestFinacing(Paging paging) throws Throwable{
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(serviceResource.getSession().getAccountId());
		parameters.add(PlanState.YFB);
		String sql =  "SELECT T6042.F02, T6043.F04, T6042.F05, T6042.F11, T6042.F07, T6042.F01,ADDDATE(T6042.F10,INTERVAL 1 DAY),T6042.F04 F40,T6042.F25"
				+ "  FROM T6043 LEFT JOIN T6042 ON T6043.F02 = T6042.F01 WHERE T6043.F03 = ? AND T6042.F07  = ?  ORDER BY T6043.F05 DESC";
		try(Connection connection = getConnection())
		{
			return selectPaging(connection, new ArrayParser<SqzBestFinacing>() {
				@Override
				public SqzBestFinacing[] parse(ResultSet resultSet)
						throws SQLException {
					ArrayList<SqzBestFinacing> list = null;
					while(resultSet.next()){
						if(list == null){
							list = new ArrayList<>();
						}
						SqzBestFinacing bestFinacing = new SqzBestFinacing();
						bestFinacing.name = resultSet.getString(1);
						bestFinacing.addMoney = resultSet.getBigDecimal(2);
						bestFinacing.rate = resultSet.getDouble(3);
						bestFinacing.jkTime = resultSet.getInt(4);
						bestFinacing.status =  EnumParser.parse(PlanState.class,resultSet.getString(5));
						bestFinacing.planId = resultSet.getInt(6);


						long hm=1000*3600*24;
						long time =	resultSet.getTimestamp(7).getTime()-System.currentTimeMillis();
						long day=time/hm;
						long hour=(time-day*hm)/(1000*3600);
						long min=(time-day*hm-hour*1000*3600)/(1000*60);
						bestFinacing.sysj = day + "天" + hour +"小时" +min + "分";

						bestFinacing.jindu = (resultSet.getDouble(9)-resultSet.getDouble(8))/resultSet.getDouble(9);

						list.add(bestFinacing);
					}

					return list == null || list.size() == 0 ? null : list
							.toArray(new SqzBestFinacing[list.size()]);

				}
			}, paging,sql,parameters);
		}
	}

	@Override
	public PagingResult<InBestFinacing> getInBestFinacing(Paging paging) throws Throwable{
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(serviceResource.getSession().getAccountId());
		parameters.add(RepayStatus.WH.toString());
		parameters.add(serviceResource.getSession().getAccountId());
		parameters.add(PlanState.YSX);
		String sql =  "SELECT T6042.F02, T6043.F04, T6042.F05, T6042.F21, T6042.F11, T6042.F07, T6043.F07 AS F25,T6042.F01,(SELECT IFNULL((T6057.F05 + T6057.F06),0) FROM T6057 WHERE T6057.F02 = T6043.F02 AND T6057.F04 =?  AND T6057.F08 = ? ORDER BY T6057.F03 LIMIT 1),T6042.F14"
				+ "  FROM T6043 LEFT JOIN T6042 ON T6043.F02 = T6042.F01 WHERE T6043.F03 = ? AND T6042.F07  = ?  ORDER BY T6043.F05 DESC";
		try(Connection connection = getConnection())
		{
			return selectPaging(connection, new ArrayParser<InBestFinacing>() {
				@Override
				public InBestFinacing[] parse(ResultSet resultSet)
						throws SQLException {
					ArrayList<InBestFinacing> list = null;
					while(resultSet.next()){
						if(list == null){
							list = new ArrayList<>();
						}
						InBestFinacing bestFinacing = new InBestFinacing();
						bestFinacing.name = resultSet.getString(1);
						bestFinacing.addMoney = resultSet.getBigDecimal(2);
						bestFinacing.rate = resultSet.getDouble(3);
						bestFinacing.nextDay = resultSet.getDate(4);
						bestFinacing.jkTime = resultSet.getInt(5);
						bestFinacing.status =  EnumParser.parse(PlanState.class,resultSet.getString(6));
						bestFinacing.notMoney = resultSet.getBigDecimal(7);
						bestFinacing.planId = resultSet.getInt(8);
						bestFinacing.monthMoney = resultSet.getBigDecimal(9);
						bestFinacing.type = EnumParser.parse(T6042_F14.class,resultSet.getString(10));
						list.add(bestFinacing);
					}

					return list == null || list.size() == 0 ? null : list
							.toArray(new InBestFinacing[list.size()]);

				}
			}, paging,sql,parameters);
		}
	}

	@Override
	public PagingResult<EndBestFinacing> getEndBestFinacing(Paging paging)  throws Throwable{
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(serviceResource.getSession().getAccountId());
		parameters.add(PlanState.YJZ);
		String sql = "SELECT T6042.F02, T6043.F04, T6042.F05, T6043.F08, T6043.F09, T6042.F13 endTime,T6042.F01 FROM T6042 INNER JOIN T6043 ON T6042.F01 = T6043.F02 WHERE T6043.F03 = ? AND T6042.F07 = ? ORDER BY T6043.F05 DESC";
		try(Connection connection = getConnection())
		{
			return selectPaging(connection, new ArrayParser<EndBestFinacing>() {
				@Override
				public EndBestFinacing[] parse(ResultSet resultSet)
						throws SQLException {
					ArrayList<EndBestFinacing> list = null;
					while(resultSet.next()){
						if(list == null){
							list = new ArrayList<>();
						}
						EndBestFinacing bestFinacing = new EndBestFinacing();
						bestFinacing.name = resultSet.getString(1);
						bestFinacing.addMoney = resultSet.getBigDecimal(2);
						bestFinacing.rate = resultSet.getDouble(3);
						bestFinacing.money =  resultSet.getBigDecimal(4);
						bestFinacing.takeMoney =  resultSet.getBigDecimal(5);
						bestFinacing.endTime = resultSet.getTimestamp(6);
						bestFinacing.planId = resultSet.getInt(7);
						list.add(bestFinacing);
					}

					return list == null || list.size() == 0 ? null : list
							.toArray(new EndBestFinacing[list.size()]);
				}
			}, paging, sql,parameters);
		}
	}

	@Override
	public InBestFinacing getSyTime(int planID) throws Throwable {
		InBestFinacing info =null;
		String sql="SELECT COUNT(*) FROM T6044 WHERE T6044.F02 = ? AND T6044.F07 = ?";
		try(Connection connection = getConnection())
		{
			try(PreparedStatement ps = connection.prepareStatement(sql))
			{
				ps.setInt(1, planID);
				ps.setString(2, RepayStatus.WH.toString());
				try(ResultSet rs = ps.executeQuery())
				{
					if(rs.next()){
						info = new InBestFinacing();
						info.num = rs.getInt(1);
					}
				}
			}
			return info;
		}
	}
}
