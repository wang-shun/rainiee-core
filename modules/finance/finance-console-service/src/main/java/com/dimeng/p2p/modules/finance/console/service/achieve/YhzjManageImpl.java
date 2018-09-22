package com.dimeng.p2p.modules.finance.console.service.achieve;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dimeng.framework.service.ServiceFactory;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.ItemParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S60.enums.T6041_F12;
import com.dimeng.p2p.S60.enums.T6056_F10;
import com.dimeng.p2p.common.enums.UserType;
import com.dimeng.p2p.modules.finance.console.service.YhzjManage;
import com.dimeng.p2p.modules.finance.console.service.entity.Yhzjgl;
import com.dimeng.p2p.modules.finance.console.service.entity.YhzjglRecord;
import com.dimeng.p2p.modules.finance.console.service.query.YhzjglRecordQuery;
import com.dimeng.p2p.variables.P2PConst;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.EnumParser;

public class YhzjManageImpl extends AbstractFinanceService implements
		YhzjManage {

	public static class UserFundManageFactory implements
			ServiceFactory<YhzjManage> {

		@Override
		public YhzjManage newInstance(ServiceResource serviceResource) {
			return new YhzjManageImpl(serviceResource);
		}
	}

	public YhzjManageImpl(ServiceResource serviceResource) {
		super(serviceResource);

	}

	public Yhzjgl getUserFundInfo() throws Throwable {
		String sql = "SELECT F01, F02, F03, F04, F05,F06,F07,F08 FROM T7024 LIMIT 1";
		try(Connection connection = getConnection())
		{
			return select(connection, new ItemParser<Yhzjgl>() {
				@Override
				public Yhzjgl parse(ResultSet resultSet) throws SQLException {
					Yhzjgl userFund = new Yhzjgl();
					if (resultSet.next()) {
						userFund = new Yhzjgl();
						userFund.usableAmount = resultSet.getBigDecimal(1);
						userFund.djAmount = resultSet.getBigDecimal(2);
						userFund.userBalance = resultSet.getBigDecimal(3);
						userFund.userIncome = resultSet.getBigDecimal(4);
						userFund.jkfzAmount = resultSet.getBigDecimal(5);
						userFund.yxlcAmount = resultSet.getBigDecimal(6);
						userFund.zqzcAmount = resultSet.getBigDecimal(7);
						userFund.updateTime = resultSet.getTimestamp(8);
					}
					return userFund;
				}
			}, sql);
		}

	}

	public PagingResult<YhzjglRecord> serarch(YhzjglRecordQuery query,
			Paging paging) throws Throwable {
		StringBuilder sql = new StringBuilder(
				"SELECT T6023.F01,T6010.F02,T6011.F06,T6011.F15,T6023.F05,T6023.F04,T6023.F03,T6026.F03 AS YXLCAMOUNT,SUM(T6038.F04) AS ZQZC FROM T6010");
		sql.append(" INNER JOIN T6023 ON T6010.F01=T6023.F01 INNER JOIN T6011 ON T6010.F01=T6011.F01");
		sql.append(" INNER JOIN T6026 ON T6010.F01=T6026.F01 LEFT JOIN T6038 ON T6010.F01=T6038.F03 WHERE 1=1");
		ArrayList<Object> parameters = new ArrayList<>();
		if (query != null) {
			String loginName = query.getLoginName();
			if (!StringHelper.isEmpty(loginName)) {
				sql.append(" AND T6010.F02 LIKE ?");
				parameters.add(getSQLConnectionProvider().allMatch(loginName));
			}
			String userName = query.getUserName();
			if (!StringHelper.isEmpty(userName)) {
				sql.append(" AND T6011.F06 LIKE ?");
				parameters.add(getSQLConnectionProvider().allMatch(userName));
			}
			UserType userType = query.getUserType();
			if (userType != null) {
				sql.append(" AND T6011.F15 = ?");
				parameters.add(userType);
			}
		}
		sql.append(" GROUP BY T6010.F01");
		sql.append(" ORDER BY T6023.F01 DESC");
		try(Connection connection = getConnection(P2PConst.DB_USER))
		{
			return selectPaging(connection,
					new ArrayParser<YhzjglRecord>() {
						ArrayList<YhzjglRecord> list = new ArrayList<YhzjglRecord>();

						public YhzjglRecord[] parse(ResultSet rs)
								throws SQLException {
							while (rs.next()) {
								YhzjglRecord user = new YhzjglRecord();
								user.id = rs.getInt(1);
								user.loginName = rs.getString(2);
								user.userName = rs.getString(3);
								user.userType = EnumParser.parse(UserType.class,
										rs.getString(4));
								user.usableAmount = rs.getBigDecimal(5);
								user.blockAmount = rs.getBigDecimal(6);
								user.userBalance = rs.getBigDecimal(7);
								user.yxlcAmount = rs.getBigDecimal(8);
								user.zqAmount = rs.getBigDecimal(9);
								user.jkhzAmount = getJkfz(user.id);
								user.userIncome = getTotalSy(user.id);
								list.add(user);
							}
							return list.toArray(new YhzjglRecord[list.size()]);
						}
					}, paging, sql.toString(), parameters);
		}
	}

	private BigDecimal getJkfz(int userId) throws SQLException {
		try(Connection connection = getConnection(P2PConst.DB_USER))
		{
			return selectBigDecimal(
					connection,
					"SELECT IFNULL(SUM(F05+F06+F07),0) FROM T6041 WHERE F03=? AND F12=?",
					userId, T6041_F12.WH);
		}
	}

	private BigDecimal getTotalSy(int userId) throws SQLException {
		try(Connection connection = getConnection(P2PConst.DB_USER))
		{
			BigDecimal yslx = selectBigDecimal(connection,
					"SELECT F02 FROM T6026 WHERE F01=?", userId);
			BigDecimal sbsylx = selectBigDecimal(connection,
					"SELECT SUM(F06) FROM T6056 WHERE F03=? AND F10=? AND F14=?",
					userId, T6056_F10.YS, "F");
			BigDecimal sbsyfx = selectBigDecimal(connection,
					"SELECT SUM(F07) FROM T6056 WHERE F03=? AND F10=? AND F14=?",
					userId, T6056_F10.YS, "F");
			BigDecimal wyj = selectBigDecimal(connection,
					"SELECT SUM(F09) FROM T6038 WHERE F03=?", userId);
			BigDecimal zqzryk = selectBigDecimal(connection,
					"SELECT F02 FROM T6027 WHERE F01=?", userId);
			return yslx.add(sbsylx).add(sbsyfx).add(wyj).add(zqzryk);
		}
	}

	@Override
	public void export(YhzjglRecord[] userFundRecord,
			OutputStream outputStream, String charset) throws Throwable {
		if (outputStream == null) {
			return;
		}
		if (userFundRecord == null) {
			return;
		}
		if (StringHelper.isEmpty(charset)) {
			charset = "GBK";
		}
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				outputStream, charset))) {
			writer.write("序号");
			writer.write(",");
			writer.write("用户ID");
			writer.write(",");
			writer.write("用户名");
			writer.write(",");
			writer.write("真实姓名");
			writer.write(",");
			writer.write("用户类型");
			writer.write(",");
			writer.write("可用金额");
			writer.write(",");
			writer.write("冻结金额");
			writer.write(",");
			writer.write("账户余额");
			writer.write(",");
			writer.write("债权资产");
			writer.write(",");
			writer.write("优选理财资产");
			writer.write(",");
			writer.write("借款负债");
			writer.write(",");
			writer.write("用户总收益");
			writer.newLine();

			int i = 1;
			for (YhzjglRecord yhzjglRecord : userFundRecord) {
				if (yhzjglRecord == null) {
					continue;
				}
				writer.write(Integer.toString(i++));
				writer.write(",");
				writer.write(Integer.toString(yhzjglRecord.id));
				writer.write(",");
				writer.write(yhzjglRecord.loginName == null ? ""
						: yhzjglRecord.loginName);
				writer.write(",");
				writer.write(yhzjglRecord.userName == null ? ""
						: yhzjglRecord.userName);
				writer.write(",");
				writer.write(yhzjglRecord.userType != null ? yhzjglRecord.userType
						.getName() : "");
				writer.write(",");
				writer.write(" " + format(yhzjglRecord.usableAmount));
				writer.write(",");
				writer.write(format(yhzjglRecord.blockAmount));
				writer.write(",");
				writer.write(format(yhzjglRecord.userBalance));
				writer.write(",");
				writer.write(format(yhzjglRecord.zqAmount));
				writer.write(",");
				writer.write(format(yhzjglRecord.yxlcAmount));
				writer.write(",");
				writer.write(format(yhzjglRecord.jkhzAmount));
				writer.write(",");
				writer.write(format(yhzjglRecord.userIncome));
				writer.write(",");
				writer.newLine();
			}
			writer.flush();
		}
	}
}
