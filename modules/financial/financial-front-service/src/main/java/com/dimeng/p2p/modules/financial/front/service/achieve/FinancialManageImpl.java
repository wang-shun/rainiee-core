package com.dimeng.p2p.modules.financial.front.service.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.S64.entities.T6410;
import com.dimeng.p2p.S64.entities.T6411;
import com.dimeng.p2p.S64.enums.T6410_F07;
import com.dimeng.p2p.S64.enums.T6410_F14;
import com.dimeng.p2p.S64.enums.T6410_F24;
import com.dimeng.p2p.S64.enums.T6412_F09;
import com.dimeng.p2p.modules.financial.front.service.FinancialManage;
import com.dimeng.p2p.modules.financial.front.service.entity.YxlbEntity;
import com.dimeng.p2p.modules.financial.front.service.entity.YxlcCount;
import com.dimeng.p2p.modules.financial.front.service.entity.YxxqEntity;

public class FinancialManageImpl extends AbstractFinancialManage implements
		FinancialManage {

	public FinancialManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public YxxqEntity getNewPlan() throws Throwable {
		try (Connection connection = getConnection()) {
			YxxqEntity record = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT T6410.F01, T6410.F02, T6410.F03, T6410.F04, T6410.F05, T6410.F06, T6410.F07, T6410.F08, T6410.F09, DATE_ADD(T6410.F10,INTERVAL 1 DAY), T6410.F11, T6410.F12, T6410.F13, T6410.F14, T6410.F15, T6410.F16, T6410.F17, T6410.F18, T6410.F19, T6410.F20, T6410.F21, T6410.F22, T6410.F23, T6410.F24,CURRENT_TIMESTAMP(), T6211.F02 FROM S64.T6410 INNER JOIN S62.T6211 ON T6211.F01 = T6410.F06 ORDER BY T6410.F01 DESC LIMIT 1")) {
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record = new YxxqEntity();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getString(2);
						record.F03 = resultSet.getBigDecimal(3);
						record.F04 = resultSet.getBigDecimal(4);
						record.F05 = resultSet.getBigDecimal(5);
						record.F06 = resultSet.getInt(6);
						record.F07 = T6410_F07.parse(resultSet.getString(7));
						record.F08 = resultSet.getInt(8);
						record.F09 = resultSet.getTimestamp(9);
						record.F10 = resultSet.getDate(10);
						record.F11 = resultSet.getInt(11);
						record.F12 = resultSet.getTimestamp(12);
						record.F13 = resultSet.getDate(13);
						record.F14 = T6410_F14.parse(resultSet.getString(14));
						record.F15 = resultSet.getBigDecimal(15);
						record.F16 = resultSet.getBigDecimal(16);
						record.F17 = resultSet.getBigDecimal(17);
						record.F18 = resultSet.getString(18);
						record.F19 = resultSet.getInt(19);
						record.F20 = resultSet.getTimestamp(20);
						record.F21 = resultSet.getDate(21);
						record.F22 = resultSet.getBigDecimal(22);
						record.F23 = resultSet.getBigDecimal(23);
						record.F24 = T6410_F24.parse(resultSet.getString(24));
						record.currentTime = resultSet.getTimestamp(25);
						record.proess = (record.F03.doubleValue() - record.F04
								.doubleValue()) / record.F03.doubleValue();
						record.bidTypeName = resultSet.getString(26);
					}
				}
			}
			return record;
		}
	}

	@Override
	public YxxqEntity getPlan(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			YxxqEntity record = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT T6410.F01, T6410.F02, T6410.F03, T6410.F04, T6410.F05, T6410.F06, T6410.F07, T6410.F08, T6410.F09, DATE_ADD(T6410.F10,INTERVAL 1 DAY), T6410.F11, T6410.F12, T6410.F13, T6410.F14, T6410.F15, T6410.F16, T6410.F17, T6410.F18, T6410.F19, T6410.F20, T6410.F21, T6410.F22, T6410.F23, T6410.F24,CURRENT_TIMESTAMP(), T6211.F02 AS F26 FROM S64.T6410 INNER JOIN S62.T6211 ON T6211.F01 = T6410.F06 WHERE T6410.F01 = ? LIMIT 1")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						record = new YxxqEntity();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getString(2);
						record.F03 = resultSet.getBigDecimal(3);
						record.F04 = resultSet.getBigDecimal(4);
						record.F05 = resultSet.getBigDecimal(5);
						record.F06 = resultSet.getInt(6);
						record.F07 = T6410_F07.parse(resultSet.getString(7));
						record.F08 = resultSet.getInt(8);
						record.F09 = resultSet.getTimestamp(9);
						record.F10 = resultSet.getDate(10);
						record.F11 = resultSet.getInt(11);
						record.F12 = resultSet.getTimestamp(12);
						record.F13 = resultSet.getDate(13);
						record.F14 = T6410_F14.parse(resultSet.getString(14));
						record.F15 = resultSet.getBigDecimal(15);
						record.F16 = resultSet.getBigDecimal(16);
						record.F17 = resultSet.getBigDecimal(17);
						record.F18 = resultSet.getString(18);
						record.F19 = resultSet.getInt(19);
						record.F20 = resultSet.getTimestamp(20);
						record.F21 = resultSet.getDate(21);
						record.F22 = resultSet.getBigDecimal(22);
						record.F23 = resultSet.getBigDecimal(23);
						record.F24 = T6410_F24.parse(resultSet.getString(24));
						record.currentTime = resultSet.getTimestamp(25);
						record.bidTypeName = resultSet.getString(26);
					}
				}
			}
			return record;
		}
	}

	@Override
	public PagingResult<YxlbEntity> search(Paging paging) throws Throwable {
		String sql = "SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20, F21, F22, F23, F24 FROM S64.T6410 WHERE F01 <> (SELECT MAX(F01) FROM S64.T6410) ORDER BY F01 DESC";
		try(Connection connection = getConnection())
		{
			return selectPaging(connection, new ArrayParser<YxlbEntity>() {
				@Override
				public YxlbEntity[] parse(ResultSet resultSet) throws SQLException {
					ArrayList<T6410> list = null;
					while (resultSet.next()) {
						YxlbEntity record = new YxlbEntity();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getString(2);
						record.F03 = resultSet.getBigDecimal(3);
						record.F04 = resultSet.getBigDecimal(4);
						record.F05 = resultSet.getBigDecimal(5);
						record.F06 = resultSet.getInt(6);
						record.F07 = T6410_F07.parse(resultSet.getString(7));
						record.F08 = resultSet.getInt(8);
						record.F09 = resultSet.getTimestamp(9);
						record.F10 = resultSet.getDate(10);
						record.F11 = resultSet.getInt(11);
						record.F12 = resultSet.getTimestamp(12);
						record.F13 = resultSet.getDate(13);
						record.F14 = T6410_F14.parse(resultSet.getString(14));
						record.F15 = resultSet.getBigDecimal(15);
						record.F16 = resultSet.getBigDecimal(16);
						record.F17 = resultSet.getBigDecimal(17);
						record.F18 = resultSet.getString(18);
						record.F19 = resultSet.getInt(19);
						record.F20 = resultSet.getTimestamp(20);
						record.F21 = resultSet.getDate(21);
						record.F22 = resultSet.getBigDecimal(22);
						record.F23 = resultSet.getBigDecimal(23);
						record.F24 = T6410_F24.parse(resultSet.getString(24));
						record.pjsyl = 1;
						try {
							record.yzje = getYzje(record.F01);
						} catch (Throwable e) {
							logger.error(e, e);
						}
						try {
							record.jrrc = getJrrc(record.F01);
						} catch (Throwable e) {
							logger.error(e, e);
						}
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(record);
					}
					return ((list == null || list.size() == 0) ? null : list
							.toArray(new YxlbEntity[list.size()]));
				}
			}, paging, sql);
		}
	}

	/**
	 * 当期优选理财已赚金额
	 * 
	 * @return
	 * @throws Throwable
	 */
	private BigDecimal getYzje(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT IFNULL(SUM(F07), 0) FROM S64.T6412 WHERE F02 = ? AND F05 = ? AND F09=?")) {
				pstmt.setInt(1, id);
				pstmt.setInt(2, FeeCode.TZ_LX);
				pstmt.setString(3, T6412_F09.YH.name());
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getBigDecimal(1);
					}
				}
			}
		}
		return new BigDecimal(0);
	}

	/**
	 * 根据ID得到当期优选理财加入人
	 * 
	 * @return
	 * @throws Throwable
	 */
	public int getJrrc(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			try (PreparedStatement pstmt = connection
					.prepareStatement(" SELECT COUNT(DISTINCT F01) FROM S64.T6411 WHERE F02 = ?")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getInt(1);
					}
				}
			}
		}
		return 0;
	}

	@Override
	public YxlcCount getStatistics() throws Throwable {
		YxlcCount fc = new YxlcCount();
		try (Connection conn = getConnection()) {
			try (PreparedStatement ps = conn
					.prepareStatement("SELECT F01,F02,F04 FROM S70.T7048 LIMIT 1");) {
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						fc.totleMoney = rs.getBigDecimal(1);
						fc.userEarnMoney = rs.getBigDecimal(2);
						fc.joinCount = rs.getLong(3);
						if (fc.totleMoney.compareTo(BigDecimal.ZERO) > 0) {
							fc.moneyUse = 1;
						} else {
							fc.moneyUse = 0;
						}
					}
				}
			}
		}

		return fc;
	}

	@Override
	public T6411[] search(int id) throws Throwable {
		try (Connection connection = getConnection()) {
			ArrayList<T6411> list = null;
			try (PreparedStatement pstmt = connection
					.prepareStatement("SELECT F01, F02, F03, F04, F05, F06 FROM S64.T6411 WHERE T6411.F02 = ?")) {
				pstmt.setInt(1, id);
				try (ResultSet resultSet = pstmt.executeQuery()) {
					while (resultSet.next()) {
						T6411 record = new T6411();
						record.F01 = resultSet.getInt(1);
						record.F02 = resultSet.getInt(2);
						record.F03 = resultSet.getInt(3);
						record.F04 = resultSet.getBigDecimal(4);
						record.F05 = resultSet.getBigDecimal(5);
						record.F06 = resultSet.getTimestamp(6);
						if (list == null) {
							list = new ArrayList<>();
						}
						list.add(record);
					}
				}
			}
			return ((list == null || list.size() == 0) ? null : list
					.toArray(new T6411[list.size()]));
		}
	}

	@Override
	public BigDecimal tzMoney(int planId) throws Throwable {
		if (planId < 0) {
			return null;
		}
		BigDecimal bd = new BigDecimal(0);
		try (Connection conn = getConnection()) {
			try (PreparedStatement ps = conn
					.prepareStatement("SELECT F04 FROM S64.T6411 WHERE F02 = ? AND F03 = ?");) {
				ps.setInt(1, planId);
				ps.setInt(2, serviceResource.getSession().getAccountId());
				try (ResultSet rs = ps.executeQuery();) {
					if (rs.next()) {
						bd = rs.getBigDecimal(1);
					}
				}
			}
		}
		return bd;
	}

}
