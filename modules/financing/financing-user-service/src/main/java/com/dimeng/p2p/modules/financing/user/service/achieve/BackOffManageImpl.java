package com.dimeng.p2p.modules.financing.user.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.enums.CreditType;
import com.dimeng.p2p.common.enums.DsStatus;
import com.dimeng.p2p.common.enums.QueryType;
import com.dimeng.p2p.modules.financing.user.service.BackOffManage;
import com.dimeng.p2p.modules.financing.user.service.entity.BackOff;
import com.dimeng.p2p.modules.financing.user.service.entity.BackOffList;
import com.dimeng.p2p.modules.financing.user.service.query.BackOffQuery;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.EnumParser;

public class BackOffManageImpl extends AbstractFinancingManage implements BackOffManage{

	public BackOffManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}
	
	public static class BackOffManageFactory implements
	ServiceFactory<BackOffManage> {
		@Override
		public BackOffManage newInstance(ServiceResource serviceResource) {
			return new BackOffManageImpl(serviceResource);
		}
		
	}
	
	@Override
	public BackOff searchTotle() throws Throwable{
		BackOff info =new BackOff();
		info.dsbx = searchTotleBx();
		info.wlsgy = searchThreeBx();
		info.wlygy = searchOneBx();
		info.wlyn = searchYearBx();
		return info;
	}
	
	

	public final static String QUERY_TOP="SELECT IFNULL(SUM((F05 + F06)), 0) FROM T6056 WHERE F03 = ? AND F10 = ?"; 
	private BigDecimal searchTotleBx() throws Throwable{
		BigDecimal info = new BigDecimal(0);
		try(Connection connection = getConnection())
		{
			try(PreparedStatement ps = connection.prepareStatement(QUERY_TOP))
			{
				ps.setInt(1, serviceResource.getSession().getAccountId());
				ps.setString(2, DsStatus.WS.name());
				try(ResultSet rs = ps.executeQuery())
				{
					if(rs.next()){
						info = rs.getBigDecimal(1);
					}
				}
			}
			return info;
		}
	}
	
	private BigDecimal searchOneBx() throws Throwable {
		StringBuilder sql = new StringBuilder(QUERY_TOP);
		sql.append("AND ADDDATE(CURDATE(),INTERVAL 1 MONTH) >= F08");
		BigDecimal info = new BigDecimal(0);
		try(Connection connection = getConnection())
		{
			try(PreparedStatement ps = connection.prepareStatement(sql.toString()))
			{
				ps.setInt(1, serviceResource.getSession().getAccountId());
				ps.setString(2, DsStatus.WS.name());
				try(ResultSet rs = ps.executeQuery();)
				{
					if(rs.next()){
						info = rs.getBigDecimal(1);
					}
				}
			}
			return info;
		}
	}



	private BigDecimal searchThreeBx() throws Throwable {
		StringBuilder sql = new StringBuilder(QUERY_TOP);
		sql.append("AND ADDDATE(CURDATE(),INTERVAL 3 MONTH) >= F08");
		BigDecimal info = new BigDecimal(0);
		try(Connection connection = getConnection())
		{
			try(PreparedStatement ps = connection.prepareStatement(sql.toString()))
			{
				ps.setInt(1, serviceResource.getSession().getAccountId());
				ps.setString(2, DsStatus.WS.name());
				try(ResultSet rs = ps.executeQuery())
				{
					if(rs.next()){
						info = rs.getBigDecimal(1);
					}
				}
			}
			return info;
		}
	}



	private BigDecimal searchYearBx() throws Throwable {
		StringBuilder sql = new StringBuilder(QUERY_TOP);
		sql.append("AND ADDDATE(CURDATE(),INTERVAL 12 MONTH) >= F08");
		BigDecimal info = new BigDecimal(0);
		try(Connection connection = getConnection())
		{
			try(PreparedStatement ps = connection.prepareStatement(sql.toString()))
			{
				ps.setInt(1, serviceResource.getSession().getAccountId());
				ps.setString(2, DsStatus.WS.name());
				try(ResultSet rs = ps.executeQuery())
				{
					if(rs.next()){
						info = rs.getBigDecimal(1);
					}
				}
			}

			return info;
		}
	}
	
	@Override
	public PagingResult<BackOffList> searchList(BackOffQuery query , Paging paging) throws Throwable{
		String sql="SELECT T6036.F01, T6036.F03, T6010.F02, (T6056.F05 + T6056.F06), T6056.F08, T6036.F19 ,T6056.F10 FROM T6036, T6056, T6010 WHERE T6056.F03 = ? AND T6056.F02 = T6036.F01 AND T6010.F01 = T6036.F02";
		StringBuilder sbSql = new StringBuilder(sql);
		ArrayList<Object> parameters= new ArrayList<Object>();
		parameters.add(serviceResource.getSession().getAccountId());
		
		if (query != null) {
			QueryType queryType = query.getQueryType();
			if (queryType != null) {
				switch (queryType) {
				case YS: {
					sbSql.append(" AND T6056.F10 = ?");
					parameters.add(DsStatus.YS.name());
					break;
				}
				case DS: {
					sbSql.append(" AND T6056.F10 = ?");
					parameters.add(DsStatus.WS.toString());
					break;
				}
				
				default:
					break;
				}
			}
			Date startTime=query.getTimeStart();
			if (startTime != null) {
				sbSql.append(" AND DATE(T6056.F08) >= ?");
				parameters.add(DateParser.format(startTime));
			}
			Date endTime=query.getTimeEnd();
			if (endTime != null) {
				sbSql.append(" AND DATE(T6056.F08) <= ?");
				parameters.add(DateParser.format(endTime));
			}
			
			sbSql.append(" ORDER BY T6056.F08 DESC");
		}
		try(Connection connection = getConnection())
		{
			return selectPaging(connection, new ArrayParser<BackOffList>() {
				@Override
				public BackOffList[] parse(ResultSet resultSet)
						throws SQLException {
					ArrayList<BackOffList> list = null;
					while(resultSet.next()){
						if(list == null){
							list = new ArrayList<>();
						}
						BackOffList backOffList = new BackOffList();
						backOffList.jkbId = resultSet.getInt(1);
						backOffList.assestsId = resultSet.getInt(2);
						backOffList.creditor = resultSet.getString(3);
						backOffList.money = resultSet.getBigDecimal(4);
						backOffList.receiveDate = resultSet.getTimestamp(5);
						backOffList.creditType =EnumParser.parse(CreditType.class,resultSet.getString(6));
						backOffList.dsStatus = EnumParser.parse(DsStatus.class,resultSet.getString(7));
						list.add(backOffList);
					}

					return list == null || list.size() == 0 ? null : list
							.toArray(new BackOffList[list.size()]);

				}
			}, paging,sbSql.toString(),parameters);
		}

	}

}
