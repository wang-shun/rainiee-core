package com.dimeng.p2p.modules.financing.user.service.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.p2p.common.enums.AutoSetStatus;
import com.dimeng.p2p.common.enums.CreditLevel;
import com.dimeng.p2p.modules.financing.user.service.AutoUtilFinacingManage;
import com.dimeng.p2p.modules.financing.user.service.entity.AutoBidSet;
import com.dimeng.p2p.modules.financing.user.service.query.AutoBidQuery;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.TimestampParser;

public class AutoUtilFinacingManageImpl extends AbstractFinancingManage
		implements AutoUtilFinacingManage {
	public static class AutoUtilFinacingManageFactory implements
			ServiceFactory<AutoUtilFinacingManage> {
		@Override
		public AutoUtilFinacingManageImpl newInstance(
				ServiceResource serviceResource) {
			return new AutoUtilFinacingManageImpl(serviceResource);
		}

	}

	public AutoUtilFinacingManageImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}

	@Override
	public void save(AutoBidQuery autoBidSet) throws Throwable {
		if (autoBidSet == null) {
			throw new IllegalArgumentException("插入内容不合法，请检查插入内容是否正确！");
		}
		try (Connection conn = getConnection()) {
			execute(conn,
					"INSERT INTO T6028 (F01,F02 , F03, F04 , F05 , F06 , F07, F08, F09 , F10 , F11) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE F02 = VALUES(F02),F03 = VALUES(F03),F04 = VALUES(F04),F05 = VALUES(F05),F06 = VALUES(F06),F07 = VALUES(F07),F08 = VALUES(F08),F09 = VALUES(F09),F10 = VALUES(F10)",
					serviceResource.getSession().getAccountId(),
					autoBidSet.getTimeMoney(), autoBidSet.getRateStart(),
					autoBidSet.getRateEnd(), autoBidSet.getJkqxStart(),
					autoBidSet.getJkqxEnd(), autoBidSet.getLevelStart(),
					autoBidSet.getLevelEnd(), autoBidSet.getSaveMoney(),
					AutoSetStatus.QY,getCurrentTimestamp(conn));
		}
	}

	@Override
	public void close() throws Throwable {
		try (Connection conn = getConnection()) {
			try (PreparedStatement ps = conn.prepareStatement(
					"UPDATE T6028 SET  F10 = ?, F11 = ? WHERE F01 = ?",
					Statement.RETURN_GENERATED_KEYS)) {
				ps.setString(1, AutoSetStatus.TY.toString());
				ps.setString(2, TimestampParser.format(new Date()));
				ps.setInt(3, serviceResource.getSession().getAccountId());
				ps.execute();
			}
		}

	}

	@Override
	public AutoBidSet search() throws Throwable {
		String sql = "SELECT F02, F03, F04, F05, F06, F07, F08, F09, F10, F01  FROM T6028 WHERE F01 = ?";
		try(Connection connection = getConnection())
		{
			return select(connection, new ItemParser<AutoBidSet>() {
				@Override
				public AutoBidSet parse(ResultSet resultSet) throws SQLException {
					AutoBidSet info = null;
					while (resultSet.next()) {
						if (info == null) {
							info = new AutoBidSet();
						}
						info.timeMoney = resultSet.getBigDecimal(1);
						info.rateStart = resultSet.getBigDecimal(2);
						info.rateEnd = resultSet.getBigDecimal(3);
						info.jkqxStart = resultSet.getInt(4);
						info.jkqxEnd = resultSet.getInt(5);
						info.saveMoney = resultSet.getBigDecimal(8);
						info.autoSetStatus = EnumParser.parse(AutoSetStatus.class,
								resultSet.getString(9));
						info.loginId = resultSet.getInt(10);
						info.levelStart = EnumParser.parse(CreditLevel.class,
								resultSet.getString(6));

						info.levelEnd = EnumParser.parse(CreditLevel.class,
								resultSet.getString(7));;
					}

					return info;
				}
			}, sql, serviceResource.getSession().getAccountId());
		}
	}
}
